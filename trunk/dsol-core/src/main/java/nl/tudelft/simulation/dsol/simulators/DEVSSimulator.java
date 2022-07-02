package nl.tudelft.simulation.dsol.simulators;

import java.io.Serializable;

import org.djunits.value.vdouble.scalar.Duration;
import org.djunits.value.vdouble.scalar.Time;
import org.djunits.value.vfloat.scalar.FloatDuration;
import org.djunits.value.vfloat.scalar.FloatTime;

import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.eventlists.EventListInterface;
import nl.tudelft.simulation.dsol.eventlists.RedBlackTree;
import nl.tudelft.simulation.dsol.experiment.ReplicationInterface;
import nl.tudelft.simulation.dsol.formalisms.eventscheduling.Executable;
import nl.tudelft.simulation.dsol.formalisms.eventscheduling.LambdaSimEvent;
import nl.tudelft.simulation.dsol.formalisms.eventscheduling.SimEvent;
import nl.tudelft.simulation.dsol.formalisms.eventscheduling.SimEventInterface;
import nl.tudelft.simulation.dsol.model.DSOLModel;
import nl.tudelft.simulation.dsol.simtime.SimTime;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simtime.SimTimeDoubleUnit;
import nl.tudelft.simulation.dsol.simtime.SimTimeFloat;
import nl.tudelft.simulation.dsol.simtime.SimTimeFloatUnit;
import nl.tudelft.simulation.dsol.simtime.SimTimeLong;

/**
 * The DEVS defines the interface of the DEVS simulator. DEVS stands for the Discrete Event System Specification. More
 * information on Discrete Event Simulation can be found in "Theory of Modeling and Simulation" by Bernard Zeigler et.al.
 * <p>
 * Copyright (c) 2002-2022 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @param <A> the absolute storage type for the simulation time, e.g. Time, Float, or Double.
 * @param <R> the relative type for time storage, e.g. Duration. For most non-unit types, such as Double or Long, the absolute
 *            and relative types are the same.
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
    public boolean cancelEvent(final SimEventInterface<T> event)
    {
        return this.eventList.remove(event);
    }

    /** {@inheritDoc} */
    @Override
    public EventListInterface<T> getEventList()
    {
        return this.eventList;
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings({"hiding", "checkstyle:hiddenfield"})
    public void initialize(final DSOLModel<A, R, T, ? extends SimulatorInterface<A, R, T>> model,
            final ReplicationInterface<A, R, T> replication) throws SimRuntimeException
    {
        synchronized (super.semaphore)
        {
            this.eventList.clear();
            super.initialize(model, replication);
            this.scheduleEvent(new SimEvent<T>(this.getReplication().getEndSimTime(),
                    (short) (SimEventInterface.MIN_PRIORITY - 1), this, this, "endReplication", null));
            this.scheduleEvent(new SimEvent<T>(this.getReplication().getWarmupSimTime(),
                    (short) (SimEventInterface.MAX_PRIORITY + 1), this, this, "warmup", null));
        }
    }

    /** {@inheritDoc} */
    @Override
    public SimEventInterface<T> scheduleEvent(final SimEventInterface<T> event) throws SimRuntimeException
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
    public SimEventInterface<T> scheduleEventRel(final R relativeDelay, final short priority, final Object source,
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
    public SimEventInterface<T> scheduleEventRel(final R relativeDelay, final Object source, final Object target,
            final String method, final Object[] args) throws SimRuntimeException
    {
        return scheduleEventRel(relativeDelay, SimEventInterface.NORMAL_PRIORITY, source, target, method, args);
    }

    /** {@inheritDoc} */
    @Override
    public SimEventInterface<T> scheduleEventAbs(final T absoluteTime, final short priority, final Object source,
            final Object target, final String method, final Object[] args) throws SimRuntimeException
    {
        return scheduleEvent(new SimEvent<T>(absoluteTime, priority, source, target, method, args));
    }

    /** {@inheritDoc} */
    @Override
    public SimEventInterface<T> scheduleEventAbs(final T absoluteTime, final Object source, final Object target,
            final String method, final Object[] args) throws SimRuntimeException
    {
        return scheduleEventAbs(absoluteTime, SimEventInterface.NORMAL_PRIORITY, source, target, method, args);
    }

    /** {@inheritDoc} */
    @Override
    public SimEventInterface<T> scheduleEventAbs(final A absoluteTime, final short priority, final Object source,
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
    public SimEventInterface<T> scheduleEventAbs(final A absoluteTime, final Object source, final Object target,
            final String method, final Object[] args) throws SimRuntimeException
    {
        return scheduleEventAbs(absoluteTime, SimEventInterface.NORMAL_PRIORITY, source, target, method, args);
    }

    /** {@inheritDoc} */
    @Override
    public SimEventInterface<T> scheduleEventNow(final short priority, final Object source, final Object target,
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
    public SimEventInterface<T> scheduleEventNow(final Object source, final Object target, final String method,
            final Object[] args) throws SimRuntimeException
    {
        return scheduleEventNow(SimEventInterface.NORMAL_PRIORITY, source, target, method, args);
    }

    /** {@inheritDoc} */
    @Override
    public SimEventInterface<T> scheduleEventRel(final R relativeDelay, final short priority, final Executable executable)
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
    public SimEventInterface<T> scheduleEventRel(final R relativeDelay, final Executable executable) throws SimRuntimeException
    {
        return scheduleEventRel(relativeDelay, SimEventInterface.NORMAL_PRIORITY, executable);
    }

    /** {@inheritDoc} */
    @Override
    public SimEventInterface<T> scheduleEventAbs(final A absoluteTime, final short priority, final Executable executable)
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
    public SimEventInterface<T> scheduleEventAbs(final A absoluteTime, final Executable executable) throws SimRuntimeException
    {
        return scheduleEventAbs(absoluteTime, SimEventInterface.NORMAL_PRIORITY, executable);
    }

    /** {@inheritDoc} */
    @Override
    public SimEventInterface<T> scheduleEventAbs(final T absoluteTime, final short priority, final Executable executable)
            throws SimRuntimeException
    {
        return scheduleEvent(new LambdaSimEvent<T>(absoluteTime, priority, executable));
    }

    /** {@inheritDoc} */
    @Override
    public SimEventInterface<T> scheduleEventAbs(final T absoluteTime, final Executable executable) throws SimRuntimeException
    {
        return scheduleEventAbs(absoluteTime, SimEventInterface.NORMAL_PRIORITY, executable);
    }

    /** {@inheritDoc} */
    @Override
    public SimEventInterface<T> scheduleEventNow(final short priority, final Executable executable) throws SimRuntimeException
    {
        synchronized (super.semaphore)
        {
            T absEventTime = this.simulatorTime.copy();
            return scheduleEvent(new LambdaSimEvent<T>(absEventTime, priority, executable));
        }
    }

    /** {@inheritDoc} */
    @Override
    public SimEventInterface<T> scheduleEventNow(final Executable executable) throws SimRuntimeException
    {
        return scheduleEventNow(SimEventInterface.NORMAL_PRIORITY, executable);
    }

    /** {@inheritDoc} */
    @Override
    public synchronized void setEventList(final EventListInterface<T> eventList)
    {
        this.eventList = eventList;
        this.fireEvent(EVENTLIST_CHANGED_EVENT);
    }

    /** {@inheritDoc} */
    @Override
    protected void stepImpl()
    {
        synchronized (super.semaphore)
        {
            if (!this.eventList.isEmpty())
            {
                SimEventInterface<T> event = this.eventList.removeFirst();
                fireUnverifiedTimedEvent(SimulatorInterface.TIME_CHANGED_EVENT, null, event.getAbsoluteExecutionTime().get());
                super.simulatorTime = event.getAbsoluteExecutionTime();
                event.execute();
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    public void run()
    {
        while (!isStoppingOrStopped())
        {
            synchronized (super.semaphore)
            {
                int cmp = this.eventList.first().getAbsoluteExecutionTime().compareTo(this.runUntilTime);
                if ((cmp == 0 && !this.runUntilIncluding) || cmp > 0)
                {
                    this.simulatorTime.set(this.runUntilTime.get());
                    this.runState = RunState.STOPPING;
                    break;
                }

                SimEventInterface<T> event = this.eventList.removeFirst();
                if (event.getAbsoluteExecutionTime().ne(super.simulatorTime))
                {
                    fireUnverifiedTimedEvent(SimulatorInterface.TIME_CHANGED_EVENT, null,
                            event.getAbsoluteExecutionTime().get());
                }
                super.simulatorTime = event.getAbsoluteExecutionTime();
                try
                {
                    event.execute();
                    if (this.eventList.isEmpty())
                    {
                        this.simulatorTime.set(this.runUntilTime.get());
                        this.runState = RunState.STOPPING;
                        break;
                    }
                }
                catch (Exception exception)
                {
                    handleSimulationException(exception);
                }
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    public void endReplication()
    {
        super.endReplication();
        this.eventList.clear();
    }

    /** {@inheritDoc} */
    @Override
    @Deprecated
    public boolean isPauseOnError()
    {
        return getErrorStrategy().equals(ErrorStrategy.WARN_AND_PAUSE);
    }

    /** {@inheritDoc} */
    @Override
    @Deprecated
    public void setPauseOnError(final boolean pauseOnError)
    {
        setErrorStrategy(pauseOnError ? ErrorStrategy.WARN_AND_PAUSE : ErrorStrategy.LOG_AND_CONTINUE);
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
        @Override
        public ReplicationInterface.TimeDouble getReplication()
        {
            return (ReplicationInterface.TimeDouble) super.getReplication();
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
        @Override
        public ReplicationInterface.TimeFloat getReplication()
        {
            return (ReplicationInterface.TimeFloat) super.getReplication();
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
        @Override
        public ReplicationInterface.TimeLong getReplication()
        {
            return (ReplicationInterface.TimeLong) super.getReplication();
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
        @Override
        public ReplicationInterface.TimeDoubleUnit getReplication()
        {
            return (ReplicationInterface.TimeDoubleUnit) super.getReplication();
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
        @Override
        public ReplicationInterface.TimeFloatUnit getReplication()
        {
            return (ReplicationInterface.TimeFloatUnit) super.getReplication();
        }
    }

}
