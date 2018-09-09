package nl.tudelft.simulation.dsol.simulators;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Calendar;

import org.djunits.value.vdouble.scalar.Duration;
import org.djunits.value.vdouble.scalar.Time;
import org.djunits.value.vfloat.scalar.FloatDuration;
import org.djunits.value.vfloat.scalar.FloatTime;

import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.experiment.Replication;
import nl.tudelft.simulation.dsol.experiment.ReplicationMode;
import nl.tudelft.simulation.dsol.simtime.SimTime;
import nl.tudelft.simulation.dsol.simtime.SimTimeCalendarDouble;
import nl.tudelft.simulation.dsol.simtime.SimTimeCalendarFloat;
import nl.tudelft.simulation.dsol.simtime.SimTimeCalendarLong;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simtime.SimTimeDoubleUnit;
import nl.tudelft.simulation.dsol.simtime.SimTimeFloat;
import nl.tudelft.simulation.dsol.simtime.SimTimeFloatUnit;
import nl.tudelft.simulation.dsol.simtime.SimTimeLong;
import nl.tudelft.simulation.event.EventProducer;
import nl.tudelft.simulation.jstats.statistics.StatisticsObject;
import nl.tudelft.simulation.language.concurrent.WorkerThread;

/**
 * The Simulator class is an abstract implementation of the SimulatorInterface.
 * <p>
 * (c) 2002-2018 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl"> www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:36:44 $
 * @param <A> the absolute storage type for the simulation time, e.g. Calendar, Duration, or Double.
 * @param <R> the relative type for time storage, e.g. Long for the Calendar. For most non-calendar types, the absolute
 *            and relative types are the same.
 * @param <T> the extended type itself to be able to implement a comparator on the simulation time.
 * @since 1.5
 */
public abstract class Simulator<A extends Comparable<A>, R extends Number & Comparable<R>, T extends SimTime<A, R, T>>
        extends EventProducer implements SimulatorInterface<A, R, T>, Runnable
{
    /** */
    private static final long serialVersionUID = 20140805L;

    /** simulatorTime represents the simulationTime. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected T simulatorTime;

    /** running represents the binary state of the simulator. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected transient boolean running = false;

    /** replication represents the currently active replication. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected Replication<A, R, T> replication = null;

    /** a worker. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected transient WorkerThread worker = null;

    /** the simulatorSemaphore. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected transient Object semaphore = new Object();

    /**
     * constructs a new Simulator.
     */
    public Simulator()
    {
        super();
        this.worker = new WorkerThread(this.getClass().getName(), this);
    }

    /** {@inheritDoc} */
    @Override
    public final A getSimulatorTime()
    {
        return this.simulatorTime == null ? null : this.simulatorTime.get();
    }

    /** {@inheritDoc} */
    @Override
    public T getSimTime()
    {
        return this.simulatorTime;
    }

    /** {@inheritDoc} */
    @Override
    public final Replication<A, R, T> getReplication()
    {
        return this.replication;
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("checkstyle:designforextension")
    public void initialize(final Replication<A, R, T> initReplication, final ReplicationMode replicationMode)
            throws SimRuntimeException
    {
        if (initReplication == null)
        {
            throw new IllegalArgumentException("replication == null ?");
        }
        if (this.isRunning())
        {
            throw new SimRuntimeException("Cannot initialize a running simulator");
        }
        synchronized (this.semaphore)
        {
            this.removeAllListeners(StatisticsObject.class);
            this.replication = initReplication;
            this.simulatorTime = initReplication.getTreatment().getStartSimTime().copy();
            this.fireTimedEvent(SimulatorInterface.START_REPLICATION_EVENT, this.simulatorTime,
                    this.simulatorTime.get());
            this.fireTimedEvent(SimulatorInterface.TIME_CHANGED_EVENT, this.simulatorTime, this.simulatorTime.get());
        }
    }

    /** {@inheritDoc} */
    @Override
    public final boolean isRunning()
    {
        return this.running;
    }

    /**
     * The run method defines the actual time step mechanism of the simulator. The implementation of this method depends
     * on the formalism. Where discrete event formalisms loop over an eventlist continuous simulators take pre-defined
     * time steps.
     */
    @Override
    public abstract void run();

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("checkstyle:designforextension")
    public void start(final boolean fireStartEvent) throws SimRuntimeException
    {
        if (this.isRunning())
        {
            throw new SimRuntimeException("Cannot start a running simulator");
        }
        if (this.replication == null)
        {
            throw new SimRuntimeException("Cannot start a simulator" + " without replication details");
        }
        if (this.simulatorTime.ge(this.replication.getTreatment().getEndSimTime()))
        {
            throw new SimRuntimeException("Cannot start simulator : " + "simulatorTime = runLength");
        }
        synchronized (this.semaphore)
        {
            this.running = true;
            if (fireStartEvent)
            {
                this.fireEvent(START_EVENT);
            }
            this.fireTimedEvent(SimulatorInterface.TIME_CHANGED_EVENT, this.simulatorTime, this.simulatorTime.get());
            if (!Thread.currentThread().getName().equals(this.worker.getName()))
            {
                this.worker.interrupt();
            }
            else
            {
                this.run();
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("checkstyle:designforextension")
    public final void start() throws SimRuntimeException
    {
        start(true);
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("checkstyle:designforextension")
    public void step(final boolean fireStepEvent) throws SimRuntimeException
    {
        if (this.isRunning())
        {
            throw new SimRuntimeException("Cannot step a running simulator");
        }
        if (this.replication == null)
        {
            throw new SimRuntimeException("Cannot step a simulator " + "without replication details");
        }
        if (this.simulatorTime.ge(this.replication.getTreatment().getEndSimTime()))
        {
            throw new SimRuntimeException("Cannot step simulator: " + "SimulatorTime = runControl.runLength");
        }
        if (fireStepEvent)
        {
            this.fireEvent(SimulatorInterface.STEP_EVENT);
        }
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("checkstyle:designforextension")
    public final void step() throws SimRuntimeException
    {
        step(true);
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("checkstyle:designforextension")
    public void stop(final boolean fireStopEvent)
    {
        if (this.isRunning())
        {
            this.running = false;
            if (this.simulatorTime.ge(this.getReplication().getTreatment().getEndSimTime()))
            {
                this.fireTimedEvent(SimulatorInterface.END_OF_REPLICATION_EVENT, this, this.simulatorTime.get());
            }
            if (fireStopEvent)
            {
                this.fireEvent(SimulatorInterface.STOP_EVENT);
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("checkstyle:designforextension")
    public final void stop()
    {
        stop(true);
    }

    /**
     * Clean up the simulator. Remove the worker thread.
     */
    public final void cleanUp()
    {
        this.running = false;
        if (this.listeners != null)
        {
            this.removeAllListeners();
        }
        if (this.worker != null)
        {
            this.worker.cleanUp();
        }
        this.worker = null;
    }

    /**
     * writes a serializable method to stream.
     * @param out the outputstream
     * @throws IOException on IOException
     */
    private synchronized void writeObject(final ObjectOutputStream out) throws IOException
    {
        out.writeObject(this.simulatorTime);
        out.writeObject(this.replication);
    }

    /**
     * reads a serializable method from stream.
     * @param in the inputstream
     * @throws IOException on IOException
     */
    @SuppressWarnings("unchecked")
    private synchronized void readObject(final java.io.ObjectInputStream in) throws IOException
    {
        try
        {
            this.simulatorTime = (T) in.readObject();
            this.replication = (Replication<A, R, T>) in.readObject();
            this.running = false;
            this.semaphore = new Object();
            this.worker = new WorkerThread(this.getClass().getName(), this);
        }
        catch (Exception exception)
        {
            throw new IOException(exception.getMessage());
        }
    }

    /***********************************************************************************************************/
    /************************************* EASY ACCESS CLASS EXTENSIONS ****************************************/
    /***********************************************************************************************************/

    /** Easy access class Simulator.TimeDouble. */
    public abstract static class TimeDouble extends Simulator<Double, Double, SimTimeDouble>
            implements SimulatorInterface.TimeDouble
    {
        /** */
        private static final long serialVersionUID = 20140805L;
    }

    /** Easy access class Simulator.TimeFloat. */
    public abstract static class TimeFloat extends Simulator<Float, Float, SimTimeFloat>
            implements SimulatorInterface.TimeFloat
    {
        /** */
        private static final long serialVersionUID = 20140805L;
    }

    /** Easy access class Simulator.TimeLong. */
    public abstract static class TimeLong extends Simulator<Long, Long, SimTimeLong>
            implements SimulatorInterface.TimeLong
    {
        /** */
        private static final long serialVersionUID = 20140805L;
    }

    /** Easy access class Simulator.TimeDoubleUnit. */
    public abstract static class TimeDoubleUnit extends Simulator<Time, Duration, SimTimeDoubleUnit>
            implements SimulatorInterface.TimeDoubleUnit
    {
        /** */
        private static final long serialVersionUID = 20140805L;
    }

    /** Easy access class Simulator.TimeFloatUnit. */
    public abstract static class TimeFloatUnit extends Simulator<FloatTime, FloatDuration, SimTimeFloatUnit>
            implements SimulatorInterface.TimeFloatUnit
    {
        /** */
        private static final long serialVersionUID = 20140805L;
    }

    /** Easy access class Simulator.CalendarDouble. */
    public abstract static class CalendarDouble extends Simulator<Calendar, Duration, SimTimeCalendarDouble>
            implements SimulatorInterface.CalendarDouble
    {
        /** */
        private static final long serialVersionUID = 20140805L;
    }

    /** Easy access class Simulator.CalendarFloat. */
    public abstract static class CalendarFloat extends Simulator<Calendar, FloatDuration, SimTimeCalendarFloat>
            implements SimulatorInterface.CalendarFloat
    {
        /** */
        private static final long serialVersionUID = 20140805L;
    }

    /** Easy access class Simulator.CalendarLong. */
    public abstract static class CalendarLong extends Simulator<Calendar, Long, SimTimeCalendarLong>
            implements SimulatorInterface.CalendarLong
    {
        /** */
        private static final long serialVersionUID = 20140805L;
    }

}
