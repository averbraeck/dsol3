package nl.tudelft.simulation.dsol;

import java.rmi.RemoteException;

import javax.naming.NamingException;

import nl.tudelft.simulation.dsol.experiment.Replication;
import nl.tudelft.simulation.dsol.experiment.ReplicationMode;
import nl.tudelft.simulation.dsol.model.AbstractDSOLModel;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulator;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;
import nl.tudelft.simulation.jstats.distributions.DistContinuous;
import nl.tudelft.simulation.jstats.distributions.DistUniform;
import nl.tudelft.simulation.jstats.streams.MersenneTwister;
import nl.tudelft.simulation.jstats.streams.StreamInterface;

/**
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class TestRunUpTo
{
    /** */
    private DEVSSimulatorInterface.TimeDouble devsSimulator;

    /**
     * @throws SimRuntimeException in case of error
     * @throws RemoteException in case of error
     * @throws NamingException in case of error
     */
    public TestRunUpTo() throws SimRuntimeException, RemoteException, NamingException
    {
        this.devsSimulator = new DEVSSimulator.TimeDouble();
        Model model = new Model(this.devsSimulator);
        Replication.TimeDouble rep = Replication.TimeDouble.create("rep1", 0.0, 0.0, 1000.0, model);
        this.devsSimulator.initialize(rep, ReplicationMode.TERMINATING);
        StreamInterface stream = new MersenneTwister();
        DistContinuous dist = new DistUniform(stream, 0, 1000);

        for (int i = 0; i < 1000; i++)
        {
            this.devsSimulator.scheduleEventAbs(dist.draw(), this, this, "print", null);
        }
        for (double t = 0.0; t < 1000.0; t += 1.0)
        {
            this.devsSimulator.runUpTo(t);
            while (this.devsSimulator.isRunning())
            {
                try
                {
                    Thread.sleep(1);
                    System.out.print(".");
                }
                catch (InterruptedException exception)
                {
                    System.err.println("interrupted");
                }
            }
        }
        System.out.println();
    }

    /**
     * @throws RemoteException in case of error
     */
    protected final void print() throws RemoteException
    {
        // spend some time...
        long total = 0;
        for (long l = 0; l < 1; l++)
        {
            total += l;
        }
        System.out.println("print at t=" + this.devsSimulator.getSimulatorTime().doubleValue() + ", total = " + total);
    }

    /**
     * @param args args for main, not used.
     * @throws SimRuntimeException in case of error
     * @throws RemoteException in case of error
     * @throws NamingException in case of error
     */
    public static void main(final String[] args) throws SimRuntimeException, RemoteException, NamingException
    {
        new TestRunUpTo();
    }

    /** */
    public class Model extends AbstractDSOLModel.TimeDouble<DEVSSimulatorInterface.TimeDouble>
    {
        /** */
        private static final long serialVersionUID = 1L;

        /**
         * @param simulator the simulator.
         */
        public Model(final DEVSSimulatorInterface.TimeDouble simulator)
        {
            super(simulator);
        }

        /** {@inheritDoc} */
        @Override
        public void constructModel() throws SimRuntimeException
        {
            //
        }

    }
}
