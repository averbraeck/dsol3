package nl.tudelft.simulation.dsol.simulators;

import java.io.Serializable;
import java.util.Calendar;

import org.djunits.value.vdouble.scalar.Duration;
import org.djunits.value.vdouble.scalar.Time;
import org.djunits.value.vfloat.scalar.FloatDuration;
import org.djunits.value.vfloat.scalar.FloatTime;
import org.djutils.event.Event;

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

/**
 * The DEVS defines the interface of the DEVS simulator. DEVS stands for the Discrete Event System Specification. More
 * information on Discrete Event Simulation can be found in "Theory of Modeling and Simulation" by Bernard Zeigler et.al.
 * <p>
 * Copyright (c) 2002-2020 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @param <A> the absolute storage type for the simulation time, e.g. Calendar, Duration, or Double.
 * @param <R> the relative type for time storage, e.g. Long for the Calendar. For most non-calendar types, such as Double or
 *            Long, the absolute and relative types are the same.
 * @param <T> the simulation time type based on the absolute and relative time.
 * @since 1.5
 */
public class DEVSSimulator<A extends Comparable<A> & Serializable, R extends Number & Comparable<R>, T extends SimTime<A, R, T>>
        extends Simulator<A, R, T> implements DEVSSimulatorInterface<A, R, T>
{
    /** */
    private static final long serialVersionUID = 20140804L;

    /** eventList represents the future event list. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected EventListInterface<T> eventList = new RedBlackTree<T>();

    /** Does the simulation pause on error when executing an event? */
    private boolean pauseOnError = false;

    /**
     * Constructs a new DEVSSimulator.
     * @param id the id of the simulator, used in logging and firing of events.
     */
    public DEVSSimulator(final Serializable id)
    {
        super(id);
    }

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
    public void initialize(final Replication<A, R, T, ? extends SimulatorInterface<A, R, T>> initReplication,
            final ReplicationMode replicationMode) throws SimRuntimeException
    {
        synchronized (super.semaphore)
        {
            this.eventList.clear();
            super.initialize(initReplication, replicationMode);
            this.scheduleEvent(new SimEvent<T>(this.getReplication().getTreatment().getEndSimTime(),
                    (short) (SimEventInterface.MIN_PRIORITY - 1), this, this, "endReplication", null));
            Object[] args = {new Event(SimulatorInterface.WARMUP_EVENT, getSourceId(), null)};
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
            if (event.getAbsoluteExecutionTime().lt(super.simulatorTime))
            {
                throw new SimRuntimeException("cannot schedule event " + event.toString() + " in past " + this.simulatorTime
                        + ">" + event.getAbsoluteExecutionTime());
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
    public final SimEventInterface<T> scheduleEventRel(final R relativeDelay, final short priority, final Executable executable)
            throws SimRuntimeException
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
    public final SimEventInterface<T> scheduleEventAbs(final A absoluteTime, final short priority, final Executable executable)
            throws SimRuntimeException
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
    public final SimEventInterface<T> scheduleEventAbs(final T absoluteTime, final short priority, final Executable executable)
            throws SimRuntimeException
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
                this.fireTimedEvent(SimulatorInterface.TIME_CHANGED_EVENT, this.simulatorTime, this.simulatorTime.get());
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
                if (event.getAbsoluteExecutionTime().ne(super.simulatorTime))
                {
                    super.fireTimedEvent(SimulatorInterface.TIME_CHANGED_EVENT, event.getAbsoluteExecutionTime(),
                            event.getAbsoluteExecutionTime().get());
                }
                super.simulatorTime = event.getAbsoluteExecutionTime();
                try
                {
                    event.execute();
                }
                catch (Exception exception)
                {
                    getLogger().always().error(exception);
                    if (this.isPauseOnError())
                    {
                        try
                        {
                            this.stop();
                        }
                        catch (SimRuntimeException stopException)
                        {
                            getLogger().always().error(stopException);
                        }
                    }
                }
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("checkstyle:designforextension")
    public void endReplication()
    {
        super.endReplication();
        this.eventList.clear();
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
            try
            {
                this.stop();
            }
            catch (SimRuntimeException stopException)
            {
                getLogger().always().error(stopException);
            }
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

        /**
         * Constructs a new DEVSSimulator.TimeDouble.
         * @param id the id of the simulator, used in logging and firing of events.
         */
        public TimeDouble(final Serializable id)
        {
            super(id);
        }

        /** {@inheritDoc} */
        @SuppressWarnings("unchecked")
        @Override
        public Replication.TimeDouble<? extends DEVSSimulatorInterface.TimeDouble> getReplication()
        {
            return (Replication.TimeDouble<? extends DEVSSimulatorInterface.TimeDouble>) super.getReplication();
        }
    }

    /** Easy access class DEVSSimulator.TimeFloat. */
    public static class TimeFloat extends DEVSSimulator<Float, Float, SimTimeFloat> implements DEVSSimulatorInterface.TimeFloat
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /**
         * Constructs a new DEVSSimulator.TimeFloat.
         * @param id the id of the simulator, used in logging and firing of events.
         */
        public TimeFloat(final Serializable id)
        {
            super(id);
        }

        /** {@inheritDoc} */
        @SuppressWarnings("unchecked")
        @Override
        public Replication.TimeFloat<? extends DEVSSimulatorInterface.TimeFloat> getReplication()
        {
            return (Replication.TimeFloat<? extends DEVSSimulatorInterface.TimeFloat>) super.getReplication();
        }
    }

    /** Easy access class DEVSSimulator.TimeLong. */
    public static class TimeLong extends DEVSSimulator<Long, Long, SimTimeLong> implements DEVSSimulatorInterface.TimeLong
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /**
         * Constructs a new DEVSSimulator.TimeLong.
         * @param id the id of the simulator, used in logging and firing of events.
         */
        public TimeLong(final Serializable id)
        {
            super(id);
        }

        /** {@inheritDoc} */
        @SuppressWarnings("unchecked")
        @Override
        public Replication.TimeLong<? extends DEVSSimulatorInterface.TimeLong> getReplication()
        {
            return (Replication.TimeLong<? extends DEVSSimulatorInterface.TimeLong>) super.getReplication();
        }
    }

    /** Easy access class DEVSSimulator.TimeDoubleUnit. */
    public static class TimeDoubleUnit extends DEVSSimulator<Time, Duration, SimTimeDoubleUnit>
            implements DEVSSimulatorInterface.TimeDoubleUnit
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /**
         * Constructs a new DEVSSimulator.TimeDoubleUnit.
         * @param id the id of the simulator, used in logging and firing of events.
         */
        public TimeDoubleUnit(final Serializable id)
        {
            super(id);
        }

        /** {@inheritDoc} */
        @SuppressWarnings("unchecked")
        @Override
        public Replication.TimeDoubleUnit<? extends DEVSSimulatorInterface.TimeDoubleUnit> getReplication()
        {
            return (Replication.TimeDoubleUnit<? extends DEVSSimulatorInterface.TimeDoubleUnit>) super.getReplication();
        }
    }

    /** Easy access class DEVSSimulator.TimeFloatUnit. */
    public static class TimeFloatUnit extends DEVSSimulator<FloatTime, FloatDuration, SimTimeFloatUnit>
            implements DEVSSimulatorInterface.TimeFloatUnit
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /**
         * Constructs a new DEVSSimulator.TimeFloatUnit.
         * @param id the id of the simulator, used in logging and firing of events.
         */
        public TimeFloatUnit(final Serializable id)
        {
            super(id);
        }

        /** {@inheritDoc} */
        @SuppressWarnings("unchecked")
        @Override
        public Replication.TimeFloatUnit<? extends DEVSSimulatorInterface.TimeFloatUnit> getReplication()
        {
            return (Replication.TimeFloatUnit<? extends DEVSSimulatorInterface.TimeFloatUnit>) super.getReplication();
        }
    }

    /** Easy access class DEVSSimulator.CalendarDouble. */
    public static class CalendarDouble extends DEVSSimulator<Calendar, Duration, SimTimeCalendarDouble>
            implements DEVSSimulatorInterface.CalendarDouble
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /**
         * Constructs a new DEVSSimulator.CalendarDouble.
         * @param id the id of the simulator, used in logging and firing of events.
         */
        public CalendarDouble(final Serializable id)
        {
            super(id);
        }

        /** {@inheritDoc} */
        @SuppressWarnings("unchecked")
        @Override
        public Replication.CalendarDouble<? extends DEVSSimulatorInterface.CalendarDouble> getReplication()
        {
            return (Replication.CalendarDouble<? extends DEVSSimulatorInterface.CalendarDouble>) super.getReplication();
        }
    }

    /** Easy access class DEVSSimulator.CalendarFloat. */
    public static class CalendarFloat extends DEVSSimulator<Calendar, FloatDuration, SimTimeCalendarFloat>
            implements DEVSSimulatorInterface.CalendarFloat
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /**
         * Constructs a new DEVSSimulator.CalendarFloat.
         * @param id the id of the simulator, used in logging and firing of events.
         */
        public CalendarFloat(final Serializable id)
        {
            super(id);
        }

        /** {@inheritDoc} */
        @SuppressWarnings("unchecked")
        @Override
        public Replication.CalendarFloat<? extends DEVSSimulatorInterface.CalendarFloat> getReplication()
        {
            return (Replication.CalendarFloat<? extends DEVSSimulatorInterface.CalendarFloat>) super.getReplication();
        }
    }

    /** Easy access class DEVSSimulator.CalendarLong. */
    public static class CalendarLong extends DEVSSimulator<Calendar, Long, SimTimeCalendarLong>
            implements DEVSSimulatorInterface.CalendarLong
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /**
         * Constructs a new DEVSSimulator.CalendarLong.
         * @param id the id of the simulator, used in logging and firing of events.
         */
        public CalendarLong(final Serializable id)
        {
            super(id);
        }

        /** {@inheritDoc} */
        @SuppressWarnings("unchecked")
        @Override
        public Replication.CalendarLong<? extends DEVSSimulatorInterface.CalendarLong> getReplication()
        {
            return (Replication.CalendarLong<? extends DEVSSimulatorInterface.CalendarLong>) super.getReplication();
        }
    }

}
