package nl.tudelft.simulation.dsol.formalisms.eventscheduling;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.concurrent.TimeoutException;

import javax.naming.NamingException;

import org.djutils.event.EventInterface;
import org.djutils.event.EventListenerInterface;
import org.junit.Test;

import net.jodah.concurrentunit.Waiter;
import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.experiment.Replication;
import nl.tudelft.simulation.dsol.experiment.ReplicationMode;
import nl.tudelft.simulation.dsol.model.AbstractDSOLModel;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulator;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;
import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;
import nl.tudelft.simulation.jstats.distributions.DistContinuous;
import nl.tudelft.simulation.jstats.distributions.DistUniform;
import nl.tudelft.simulation.jstats.streams.MersenneTwister;
import nl.tudelft.simulation.jstats.streams.StreamInterface;

/**
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class DEVSSimulationTestDouble implements EventListenerInterface
{
    /** */
    private static final long serialVersionUID = 1L;

    /** */
    protected DEVSSimulatorInterface.TimeDouble devsSimulator;

    /** the Waiter from ConcurrentUnit that catches AssertionErrors in other threads. */
    protected Waiter waiter;

    /**
     * @throws SimRuntimeException in case of error
     * @throws RemoteException in case of error
     * @throws NamingException in case of error
     * @throws InterruptedException on error
     * @throws TimeoutException on error
     */
    @Test
    public void testDEVSSimulationDouble()
            throws SimRuntimeException, RemoteException, NamingException, TimeoutException, InterruptedException
    {
        this.waiter = new Waiter();
        this.devsSimulator = new DEVSSimulator.TimeDouble("testDEVSSimulationDouble");
        this.devsSimulator.addListener(this, SimulatorInterface.END_REPLICATION_EVENT);
        ModelDouble model = new ModelDouble(this.devsSimulator);
        Replication.TimeDouble<DEVSSimulatorInterface.TimeDouble> rep =
                Replication.TimeDouble.create("rep1", 0.0, 0.0, 100.0, model);
        this.devsSimulator.initialize(rep, ReplicationMode.TERMINATING);
        this.devsSimulator.scheduleEventAbs(1.0, this, this, "step1", new Object[] {1.0});
        this.devsSimulator.start();
        this.waiter.await(1000);
    }

    /**
     * Do a simulation step and check the time.
     * @param checkTime the step on the simulator
     * @throws SimRuntimeException on error
     */
    protected void step1(final double checkTime) throws SimRuntimeException
    {
        this.waiter.assertEquals(this.devsSimulator.getSimulatorTime(), checkTime, 0.0001);
        this.devsSimulator.scheduleEventRel(1.0, this, this, "step1", new Object[] {checkTime + 1.0});
    }

    /** {@inheritDoc} */
    @Override
    public void notify(EventInterface event) throws RemoteException
    {
        this.waiter.resume();
    }

    /**
     * @throws SimRuntimeException in case of error
     * @throws RemoteException in case of error
     * @throws NamingException in case of error
     * @throws InterruptedException on error
     * @throws TimeoutException on error
     */
    @Test
    public void testRunUpTo()
            throws SimRuntimeException, RemoteException, NamingException, TimeoutException, InterruptedException
    {
        this.waiter = new Waiter();
        this.devsSimulator = new DEVSSimulator.TimeDouble("testRunUpTo");
        this.devsSimulator.addListener(this, SimulatorInterface.END_REPLICATION_EVENT);
        ModelDouble model = new ModelDouble(this.devsSimulator);
        Replication.TimeDouble<DEVSSimulatorInterface.TimeDouble> rep =
                Replication.TimeDouble.create("rep1", 0.0, 0.0, 1000.0, model);
        this.devsSimulator.initialize(rep, ReplicationMode.TERMINATING);
        final DEVSSimulatorInterface.TimeDouble sim = this.devsSimulator;
        final Waiter w = this.waiter;
        final Object target = this;

        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                StreamInterface stream = new MersenneTwister();
                DistContinuous dist = new DistUniform(stream, 0, 1000);
                for (int i = 0; i < 1000; i++)
                {
                    double time = dist.draw();
                    sim.scheduleEventAbs(time, this, target, "doWork", new Object[] {time});
                }
                for (double t = 0.0; t < 1000.0; t += 1.0)
                {
                    sim.runUpTo(t);
                    while (sim.isRunning())
                    {
                        try
                        {
                            Thread.sleep(0, 10);
                        }
                        catch (InterruptedException exception)
                        {
                            w.fail(exception);
                        }
                    }
                    w.assertEquals(t, sim.getSimulatorTime(), 0.0001);
                }
                sim.start();
            }
        }).start();
        this.waiter.await(10000);
    }

    /**
     * do some work.
     * @param time the expected time when the event should be executed
     */
    protected void doWork(final double time)
    {
        this.waiter.assertEquals(time, this.devsSimulator.getSimulatorTime(), 0.0001);
    }

    /**
     * @throws SimRuntimeException in case of error
     * @throws RemoteException in case of error
     * @throws NamingException in case of error
     * @throws InterruptedException on error
     * @throws TimeoutException on error
     */
    @Test
    public void testSimLambda()
            throws SimRuntimeException, RemoteException, NamingException, TimeoutException, InterruptedException
    {
        this.waiter = new Waiter();
        this.devsSimulator = new DEVSSimulator.TimeDouble("testSimLambda");
        this.devsSimulator.addListener(this, SimulatorInterface.END_REPLICATION_EVENT);
        ModelDouble model = new ModelDouble(this.devsSimulator);
        Replication.TimeDouble<DEVSSimulatorInterface.TimeDouble> rep =
                Replication.TimeDouble.create("rep1", 0.0, 0.0, 100.0, model);
        this.devsSimulator.initialize(rep, ReplicationMode.TERMINATING);

        for (int i = 0; i < 10; i++)
        {
            this.devsSimulator.scheduleEventAbs(1.0d * i, new Executable()
            {
                @Override
                public void execute()
                {
                    DEVSSimulationTestDouble.this.waiter
                            .assertTrue(DEVSSimulationTestDouble.this.devsSimulator.getSimulatorTime() <= 10.0);
                }
            });
        }
        this.devsSimulator.start();
        this.waiter.await(1000);
    }

    /**
     * THE MODEL.
     */
    public static class ModelDouble extends AbstractDSOLModel.TimeDouble<DEVSSimulatorInterface.TimeDouble>
    {
        /** */
        private static final long serialVersionUID = 1L;

        /**
         * @param simulator the simulator.
         */
        public ModelDouble(final DEVSSimulatorInterface.TimeDouble simulator)
        {
            super(simulator);
        }

        /** {@inheritDoc} */
        @Override
        public void constructModel() throws SimRuntimeException
        {
            //
        }

        /** {@inheritDoc} */
        @Override
        public Serializable getSourceId()
        {
            return "ModelDouble";
        }
    }
}
