package nl.tudelft.simulation.dsol.simulators;

import java.util.Calendar;

import org.djunits.value.vdouble.scalar.Duration;
import org.djunits.value.vdouble.scalar.Time;
import org.djunits.value.vfloat.scalar.FloatDuration;
import org.djunits.value.vfloat.scalar.FloatTime;

import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.eventlists.EventListInterface;
import nl.tudelft.simulation.dsol.eventlists.RedBlackTree;
import nl.tudelft.simulation.dsol.experiment.Replication;
import nl.tudelft.simulation.dsol.experiment.ReplicationMode;
import nl.tudelft.simulation.dsol.formalisms.eventscheduling.Executable;
import nl.tudelft.simulation.dsol.formalisms.eventscheduling.LambdaSimEvent;
import nl.tudelft.simulation.dsol.formalisms.eventscheduling.SimEvent;
import nl.tudelft.simulation.dsol.formalisms.eventscheduling.SimEventInterface;
import nl.tudelft.simulation.dsol.simtime.SimTime;
import nl.tudelft.simulation.dsol.simtime.SimTimeCalendarDouble;
import nl.tudelft.simulation.dsol.simtime.SimTimeCalendarFloat;
import nl.tudelft.simulation.dsol.simtime.SimTimeCalendarLong;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simtime.SimTimeDoubleUnit;
import nl.tudelft.simulation.dsol.simtime.SimTimeFloat;
import nl.tudelft.simulation.dsol.simtime.SimTimeFloatUnit;
import nl.tudelft.simulation.dsol.simtime.SimTimeLong;
import nl.tudelft.simulation.event.Event;

/**
 * The DEVS defines the interface of the DEVS simulator. DEVS stands for the Discrete Event System Specification. More
 * information on Discrete Event Simulation can be found in "Theory of Modeling and Simulation" by Bernard Zeigler et.
 * al.
 * <p>
 * (c) 2002-2018 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl"> www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:36:44 $
 * @param <A> the absolute storage type for the simulation time, e.g. Calendar, Duration, or Double.
 * @param <R> the relative type for time storage, e.g. Long for the Calendar. For most non-calendar types, such as
 *            Double or Long, the absolute and relative types are the same.
 * @param <T> the simulation time type based on the absolute and relative time.
 * @since 1.5
 */
public class DEVSSimulator<A extends Comparable<A>, R extends Number & Comparable<R>, T extends SimTime<A, R, T>>
        extends Simulator<A, R, T> implements DEVSSimulatorInterface<A, R, T>
{
    /** */
    private static final long serialVersionUID = 20140804L;

    /** eventList represents the future event list. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected EventListInterface<T> eventList = new RedBlackTree<T>();

    /** Does the simulation pause on error when executing an event? */
    private boolean pauseOnError = false;

    /** {@inheritDoc} */
    @Override
    public final boolean cancelEvent(final SimEventInterface<T> event)
    {
        return this.eventList.remove(event);
    }

    /** {@inheritDoc} */
    @Override
    public final EventListInterface<T> getEventList()
    {
        return this.eventList;
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("checkstyle:designforextension")
    public void initialize(final Replication<A, R, T> initReplication, final ReplicationMode replicationMode)
            throws SimRuntimeException
    {
        synchronized (super.semaphore)
        {
            super.initialize(initReplication, replicationMode);
            this.eventList.clear();
            this.replication.getTreatment().getExperiment().getModel().constructModel(this);
            this.scheduleEvent(new SimEvent<T>(this.getReplication().getTreatment().getEndSimTime(),
                    (short) (SimEventInterface.MIN_PRIORITY - 1), this, this, "stop", null));
            Object[] args = {new Event(SimulatorInterface.WARMUP_EVENT, this, null)};
            this.scheduleEvent(new SimEvent<T>(this.getReplication().getTreatment().getWarmupSimTime(),
                    (short) (SimEventInterface.MAX_PRIORITY + 1), this, this, "fireEvent", args));
        }
    }

    /** {@inheritDoc} */
    @Override
    public final SimEventInterface<T> scheduleEvent(final SimEventInterface<T> event) throws SimRuntimeException
    {
        synchronized (super.semaphore)
        {
            if (event.getAbsoluteExecutionTime().lt(super.simulatorTime)) // TODO can the time be NaN? If so, exclude!
            {
                throw new SimRuntimeException("cannot schedule event " + event.toString() + " in past "
                        + this.simulatorTime + ">" + event.getAbsoluteExecutionTime());
            }
            this.eventList.add(event);
            return event;
        }
    }

    /** {@inheritDoc} */
    @Override
    public final SimEventInterface<T> scheduleEventRel(final R relativeDelay, final short priority, final Object source,
            final Object target, final String method, final Object[] args) throws SimRuntimeException
    {
        synchronized (super.semaphore)
        {
            T absEventTime = this.simulatorTime.copy();
            absEventTime.add(relativeDelay);
            return scheduleEvent(new SimEvent<T>(absEventTime, priority, source, target, method, args));
        }
    }

    /** {@inheritDoc} */
    @Override
    public final SimEventInterface<T> scheduleEventRel(final R relativeDelay, final Object source, final Object target,
            final String method, final Object[] args) throws SimRuntimeException
    {
        return scheduleEventRel(relativeDelay, SimEventInterface.NORMAL_PRIORITY, source, target, method, args);
    }

    /** {@inheritDoc} */
    @Override
    public final SimEventInterface<T> scheduleEventAbs(final T absoluteTime, final short priority, final Object source,
            final Object target, final String method, final Object[] args) throws SimRuntimeException
    {
        return scheduleEvent(new SimEvent<T>(absoluteTime, priority, source, target, method, args));
    }

    /** {@inheritDoc} */
    @Override
    public final SimEventInterface<T> scheduleEventAbs(final T absoluteTime, final Object source, final Object target,
            final String method, final Object[] args) throws SimRuntimeException
    {
        return scheduleEventAbs(absoluteTime, SimEventInterface.NORMAL_PRIORITY, source, target, method, args);
    }

    /** {@inheritDoc} */
    @Override
    public final SimEventInterface<T> scheduleEventAbs(final A absoluteTime, final short priority, final Object source,
            final Object target, final String method, final Object[] args) throws SimRuntimeException
    {
        synchronized (super.semaphore)
        {
            T absTime = this.simulatorTime.copy();
            absTime.set(absoluteTime);
            return scheduleEvent(new SimEvent<T>(absTime, priority, source, target, method, args));
        }
    }

    /** {@inheritDoc} */
    @Override
    public final SimEventInterface<T> scheduleEventAbs(final A absoluteTime, final Object source, final Object target,
            final String method, final Object[] args) throws SimRuntimeException
    {
        return scheduleEventAbs(absoluteTime, SimEventInterface.NORMAL_PRIORITY, source, target, method, args);
    }

    /** {@inheritDoc} */
    @Override
    public final SimEventInterface<T> scheduleEventNow(final short priority, final Object source, final Object target,
            final String method, final Object[] args) throws SimRuntimeException
    {
        synchronized (super.semaphore)
        {
            T absEventTime = this.simulatorTime.copy();
            return scheduleEvent(new SimEvent<T>(absEventTime, priority, source, target, method, args));
        }
    }

    /** {@inheritDoc} */
    @Override
    public final SimEventInterface<T> scheduleEventNow(final Object source, final Object target, final String method,
            final Object[] args) throws SimRuntimeException
    {
        return scheduleEventNow(SimEventInterface.NORMAL_PRIORITY, source, target, method, args);
    }

    /** {@inheritDoc} */
    @Override
    public final SimEventInterface<T> scheduleEventRel(final R relativeDelay, final short priority,
            final Executable executable) throws SimRuntimeException
    {
        synchronized (super.semaphore)
        {
            T absEventTime = this.simulatorTime.copy();
            absEventTime.add(relativeDelay);
            return scheduleEvent(new LambdaSimEvent<T>(absEventTime, priority, executable));
        }
    }

    /** {@inheritDoc} */
    @Override
    public final SimEventInterface<T> scheduleEventRel(final R relativeDelay, final Executable executable)
            throws SimRuntimeException
    {
        return scheduleEventRel(relativeDelay, SimEventInterface.NORMAL_PRIORITY, executable);
    }

    /** {@inheritDoc} */
    @Override
    public final SimEventInterface<T> scheduleEventAbs(final A absoluteTime, final short priority,
            final Executable executable) throws SimRuntimeException
    {
        synchronized (super.semaphore)
        {
            T absTime = this.simulatorTime.copy();
            absTime.set(absoluteTime);
            return scheduleEvent(new LambdaSimEvent<T>(absTime, priority, executable));
        }
    }

    /** {@inheritDoc} */
    @Override
    public final SimEventInterface<T> scheduleEventAbs(final A absoluteTime, final Executable executable)
            throws SimRuntimeException
    {
        return scheduleEventAbs(absoluteTime, SimEventInterface.NORMAL_PRIORITY, executable);
    }

    /** {@inheritDoc} */
    @Override
    public final SimEventInterface<T> scheduleEventAbs(final T absoluteTime, final short priority,
            final Executable executable) throws SimRuntimeException
    {
        return scheduleEvent(new LambdaSimEvent<T>(absoluteTime, priority, executable));
    }

    /** {@inheritDoc} */
    @Override
    public final SimEventInterface<T> scheduleEventAbs(final T absoluteTime, final Executable executable)
            throws SimRuntimeException
    {
        return scheduleEventAbs(absoluteTime, SimEventInterface.NORMAL_PRIORITY, executable);
    }

    /** {@inheritDoc} */
    @Override
    public final SimEventInterface<T> scheduleEventNow(final short priority, final Executable executable)
            throws SimRuntimeException
    {
        synchronized (super.semaphore)
        {
            T absEventTime = this.simulatorTime.copy();
            return scheduleEvent(new LambdaSimEvent<T>(absEventTime, priority, executable));
        }
    }

    /** {@inheritDoc} */
    @Override
    public final SimEventInterface<T> scheduleEventNow(final Executable executable) throws SimRuntimeException
    {
        return scheduleEventNow(SimEventInterface.NORMAL_PRIORITY, executable);
    }

    /** {@inheritDoc} */
    @Override
    public final synchronized void setEventList(final EventListInterface<T> eventList)
    {
        this.eventList = eventList;
        this.fireEvent(EVENTLIST_CHANGED_EVENT);
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("checkstyle:designforextension")
    public void step(final boolean fireStepEvent) throws SimRuntimeException
    {
        synchronized (super.semaphore)
        {
            super.step(fireStepEvent);
            if (!this.eventList.isEmpty())
            {
                this.running = true;
                SimEventInterface<T> event = this.eventList.removeFirst();
                this.simulatorTime = event.getAbsoluteExecutionTime();
                this.fireTimedEvent(SimulatorInterface.TIME_CHANGED_EVENT, this.simulatorTime,
                        this.simulatorTime.get());
                event.execute();
                this.running = false;
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("checkstyle:designforextension")
    public void run()
    {
        while (super.isRunning())
        {
            synchronized (super.semaphore)
            {
                SimEventInterface<T> event = this.eventList.removeFirst();
                super.simulatorTime = event.getAbsoluteExecutionTime();
                super.fireTimedEvent(SimulatorInterface.TIME_CHANGED_EVENT, super.simulatorTime,
                        super.simulatorTime.get());
                try
                {
                    event.execute();
                }
                catch (Exception exception)
                {
                    exception.printStackTrace();
                    if (this.isPauseOnError())
                    {
                        this.stop();
                    }
                }
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("checkstyle:designforextension")
    public void stop(final boolean fireStopEvent)
    {
        super.stop(fireStopEvent);
        if (this.getReplication() != null
                && this.simulatorTime.ge(this.getReplication().getTreatment().getEndSimTime()))
        {
            this.eventList.clear();
        }
        if (this.eventList.isEmpty())
        {
            this.fireEvent(SimulatorInterface.END_OF_REPLICATION_EVENT);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("checkstyle:designforextension")
    public void runUpTo(final A when) throws SimRuntimeException
    {
        scheduleEventAbs(when, SimEventInterface.MAX_PRIORITY, this, this, "autoPauseSimulator", null);
        if (!isRunning())
        {
            start();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("checkstyle:designforextension")
    public void runUpToAndIncluding(final A when) throws SimRuntimeException
    {
        scheduleEventAbs(when, SimEventInterface.MIN_PRIORITY, this, this, "autoPauseSimulator", null);
        if (!isRunning())
        {
            start();
        }
    }

    /**
     * Pause the simulator.
     */
    @SuppressWarnings("checkstyle:designforextension")
    protected void autoPauseSimulator()
    {
        if (isRunning())
        {
            stop();
        }
    }

    /** {@inheritDoc} */
    @Override
    public final boolean isPauseOnError()
    {
        return this.pauseOnError;
    }

    /** {@inheritDoc} */
    @Override
    public final void setPauseOnError(final boolean pauseOnError)
    {
        this.pauseOnError = pauseOnError;
    }

    /***********************************************************************************************************/
    /************************************* EASY ACCESS CLASS EXTENSIONS ****************************************/
    /***********************************************************************************************************/

    /** Easy access class DEVSSimulator.TimeDouble. */
    public static class TimeDouble extends DEVSSimulator<Double, Double, SimTimeDouble>
            implements DEVSSimulatorInterface.TimeDouble
    {
        /** */
        private static final long serialVersionUID = 20140805L;
    }

    /** Easy access class DEVSSimulator.TimeFloat. */
    public static class TimeFloat extends DEVSSimulator<Float, Float, SimTimeFloat>
            implements DEVSSimulatorInterface.TimeFloat
    {
        /** */
        private static final long serialVersionUID = 20140805L;
    }

    /** Easy access class DEVSSimulator.TimeLong. */
    public static class TimeLong extends DEVSSimulator<Long, Long, SimTimeLong>
            implements DEVSSimulatorInterface.TimeLong
    {
        /** */
        private static final long serialVersionUID = 20140805L;
    }

    /** Easy access class DEVSSimulator.TimeDoubleUnit. */
    public static class TimeDoubleUnit extends DEVSSimulator<Time, Duration, SimTimeDoubleUnit>
            implements DEVSSimulatorInterface.TimeDoubleUnit
    {
        /** */
        private static final long serialVersionUID = 20140805L;
    }

    /** Easy access class DEVSSimulator.TimeFloatUnit. */
    public static class TimeFloatUnit extends DEVSSimulator<FloatTime, FloatDuration, SimTimeFloatUnit>
            implements DEVSSimulatorInterface.TimeFloatUnit
    {
        /** */
        private static final long serialVersionUID = 20140805L;
    }

    /** Easy access class DEVSSimulator.CalendarDouble. */
    public static class CalendarDouble extends DEVSSimulator<Calendar, Duration, SimTimeCalendarDouble>
            implements DEVSSimulatorInterface.CalendarDouble
    {
        /** */
        private static final long serialVersionUID = 20140805L;
    }

    /** Easy access class DEVSSimulator.CalendarFloat. */
    public static class CalendarFloat extends DEVSSimulator<Calendar, FloatDuration, SimTimeCalendarFloat>
            implements DEVSSimulatorInterface.CalendarFloat
    {
        /** */
        private static final long serialVersionUID = 20140805L;
    }

    /** Easy access class DEVSSimulator.CalendarLong. */
    public static class CalendarLong extends DEVSSimulator<Calendar, Long, SimTimeCalendarLong>
            implements DEVSSimulatorInterface.CalendarLong
    {
        /** */
        private static final long serialVersionUID = 20140805L;
    }

}
