package nl.tudelft.simulation.dsol.simulators;

import java.io.Serializable;
import java.util.Calendar;

import org.djunits.unit.DurationUnit;
import org.djunits.value.vdouble.scalar.Duration;
import org.djunits.value.vdouble.scalar.Time;
import org.djunits.value.vfloat.scalar.FloatDuration;
import org.djunits.value.vfloat.scalar.FloatTime;
import org.djutils.event.EventType;
import org.djutils.event.TimedEventType;
import org.djutils.metadata.MetaData;
import org.djutils.metadata.ObjectDescriptor;

import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.experiment.Replication;
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
 * The reference implementation of the realTimeClock. The realTime clock is a DEVS simulator which runs at a ratio of realTime.
 * If the executionTime exceeds the timeStep, a catchup mechanism can be triggered to make up lost time in consecutive steps.
 * <p>
 * Copyright (c) 2004-2020 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @param <A> the absolute storage type for the simulation time, e.g. Calendar, Duration, or Double.
 * @param <R> the relative type for time storage, e.g. Long for the Calendar. For most non-calendar types, the absolute and
 *            relative types are the same.
 * @param <T> the extended type itself to be able to implement a comparator on the simulation time.
 * @since 1.5
 */
public abstract class DEVSRealTimeClock<A extends Comparable<A> & Serializable, R extends Number & Comparable<R>,
        T extends SimTime<A, R, T>> extends DEVSAnimator<A, R, T> implements DEVSSimulatorInterface<A, R, T>
{
    /** */
    private static final long serialVersionUID = 20150428L;

    // TODO: Fire the BACKLOG_EVENT when we are behind in the run thread
    /** the backlog event. */
    public static final TimedEventType BACKLOG_EVENT =
            new TimedEventType(new MetaData("BACKLOG_EVENT", "Real time simulation is behind"));

    /** the speed factor event. */
    public static final EventType CHANGE_SPEED_FACTOR_EVENT = new EventType(new MetaData("CHANGE_SPEED_FACTOR_EVENT",
            "Change speed factor", new ObjectDescriptor("newSpeedFactor", "New speed factor", Double.class)));

    /** the speed factor compared to real time clock. &lt;1 is slower, &gt;1 is faster, 1 is real time speed. */
    private double speedFactor = 1.0;

    /**
     * The relative update delay in milliseconds is the step size in wall clock time by which the time is updated between
     * events. Default, this value is set at 10 msec, which means that the simulation updates its clock with 100 Hz between
     * events. When this is too course, set e.g. to 1, which means that the clock will be updated with 1 kHz between events. The
     * latter can be important in real time simulations. Note that the housekeeping of the simulation clock takes time as well,
     * so 1 kHz can be too fine grained in some situations. This factor takes care that animation updates between events happen
     * fine grained and in small increments.
     */
    private int updateMsec = 10;

    /** catch up or not catch up after running behind. */
    private boolean catchup = true;

    /** Start an animation thread or not. */
    private Boolean animation = true;

    /** the current animation thread; null if none. */
    private AnimationThread animationThread = null;

    /**
     * Calculate the how much simulation duration corresponds to the number of wall clock milliseconds indicated in the
     * parameter. When the DEVSRealTimeClock works with djunits Time or Duration, and the simulation is scaled to milliseconds,
     * the simulatorTimeForWallClockMillis of a millisecond is a Duration of 1 millisecond. When 1 simulated time unit stands
     * for a second, the simulatorTimeForWallClockMillis is 0.001.
     * @param wallMilliseconds double; the number of milliseconds to calculate the corresponding simulation time for
     * @return the relative time step.
     */
    protected abstract R simulatorTimeForWallClockMillis(double wallMilliseconds);

    /**
     * Constructs a new DEVSRealTimeClock.
     * @param id the id of the simulator, used in logging and firing of events.
     */
    public DEVSRealTimeClock(final Serializable id)
    {
        super(id);
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings({"checkstyle:designforextension", "checkstyle:methodlength"})
    public void run()
    {
        synchronized (this.animation)
        {
            if (this.animation)
            {
                this.animationThread = new AnimationThread(this);
                this.animationThread.start();
            }
        }

        /* Baseline point for the wallclock time. */
        long wallTime0 = System.currentTimeMillis();

        /* Baseline point for the simulator time. */
        T simTime0 = this.simulatorTime.copy();

        /* Speed factor is simulation seconds per 1 wallclock second. */
        double currentSpeedFactor = this.speedFactor;

        /* wall clock milliseconds per 1 simulation clock millisecond. */
        double msec1 = simulatorTimeForWallClockMillis(1.0).doubleValue();

        while (!this.stoppingState && !this.eventList.isEmpty()
                && this.simulatorTime.le(this.replication.getTreatment().getEndSimTime()))
        {
            // check if speedFactor has changed. If yes: re-baseline.
            if (currentSpeedFactor != this.speedFactor)
            {
                wallTime0 = System.currentTimeMillis();
                simTime0.set(this.simulatorTime.get());
                currentSpeedFactor = this.speedFactor;
            }

            // check if we are behind; wantedSimTime is the needed current time on the wall-clock
            double wantedSimTime = (System.currentTimeMillis() - wallTime0) * msec1 * currentSpeedFactor;
            double simTimeSinceBaseline = this.simulatorTime.diff(simTime0).doubleValue();

            if (simTimeSinceBaseline < wantedSimTime)
            {
                // we are behind
                if (!this.catchup)
                {
                    // if no catch-up: re-baseline.
                    wallTime0 = System.currentTimeMillis();
                    simTime0.set(this.simulatorTime.get());
                }
                else
                {
                    // jump to the required wall-clock related time or to the time of the next event, whichever comes
                    // first
                    synchronized (super.semaphore)
                    {
                        R delta = simulatorTimeForWallClockMillis((wantedSimTime - simTimeSinceBaseline) / msec1);
                        T absSyncTime = this.simulatorTime.plus(delta);
                        T eventTime = this.eventList.first().getAbsoluteExecutionTime();
                        if (absSyncTime.lt(eventTime))
                        {
                            this.simulatorTime.set(absSyncTime.get());
                        }
                        else
                        {
                            this.simulatorTime.set(eventTime.get());
                        }
                    }
                }
            }

            // peek at the first event and determine the time difference relative to RT speed; that determines
            // how long we have to wait.
            SimEventInterface<T> nextEvent = this.eventList.first();
            double wallMillisNextEventSinceBaseline =
                    (nextEvent.getAbsoluteExecutionTime().diff(simTime0)).doubleValue() / (msec1 * currentSpeedFactor);

            // wallMillisNextEventSinceBaseline gives the number of milliseconds on the wall clock since baselining for the
            // expected execution time of the next event on the event list .
            if (wallMillisNextEventSinceBaseline >= (System.currentTimeMillis() - wallTime0))
            {
                while (wallMillisNextEventSinceBaseline > System.currentTimeMillis() - wallTime0)
                {
                    try
                    {
                        Thread.sleep(this.updateMsec);
                    }
                    catch (InterruptedException ie)
                    {
                        // do nothing
                        ie = null;
                        Thread.interrupted(); // clear the flag
                    }

                    // did we stop running between events?
                    if (this.stoppingState)
                    {
                        wallMillisNextEventSinceBaseline = 0.0; // jump out of the while loop for sleeping
                        break;
                    }

                    // check if speedFactor has changed. If yes: rebaseline. Try to avoid a jump.
                    if (currentSpeedFactor != this.speedFactor)
                    {
                        // rebaseline
                        wallTime0 = System.currentTimeMillis();
                        simTime0.set(this.simulatorTime.get());
                        currentSpeedFactor = this.speedFactor;
                        wallMillisNextEventSinceBaseline = (nextEvent.getAbsoluteExecutionTime().diff(simTime0)).doubleValue()
                                / (msec1 * currentSpeedFactor);
                    }

                    // check if an event has been inserted. In a real-time situation this can be done by other threads
                    if (!nextEvent.equals(this.eventList.first())) // event inserted by a thread...
                    {
                        nextEvent = this.eventList.first();
                        wallMillisNextEventSinceBaseline = (nextEvent.getAbsoluteExecutionTime().diff(simTime0)).doubleValue()
                                / (msec1 * currentSpeedFactor);
                    }

                    // make a small time step for the animation during wallclock waiting, but never beyond the next event
                    // time. Changed 2019-04-30: this is now recalculated based on latest system time after the 'sleep'.
                    synchronized (super.semaphore)
                    {
                        A nextEventSimTime = nextEvent.getAbsoluteExecutionTime().get();
                        R deltaToWall0inSimTime =
                                simulatorTimeForWallClockMillis((System.currentTimeMillis() - wallTime0) * currentSpeedFactor);
                        A currentWallSimTime = simTime0.plus(deltaToWall0inSimTime).get();
                        if (nextEventSimTime.compareTo(currentWallSimTime) < 0)
                        {
                            if (nextEventSimTime.compareTo(this.simulatorTime.get()) > 0) // don't go back in time
                            {
                                this.simulatorTime.set(nextEventSimTime);
                            }
                            wallMillisNextEventSinceBaseline = 0.0; // force breakout of the loop
                        }
                        else
                        {
                            if (currentWallSimTime.compareTo(this.simulatorTime.get()) > 0) // don't go back in time
                            {
                                this.simulatorTime.set(currentWallSimTime);
                            }
                        }
                    }
                }
            }

            // only execute an event if we are still running...
            if (!this.stoppingState)
            {
                synchronized (super.semaphore)
                {
                    if (nextEvent.getAbsoluteExecutionTime().ne(this.simulatorTime))
                    {
                        fireUnverifiedTimedEvent(SimulatorInterface.TIME_CHANGED_EVENT, nextEvent.getAbsoluteExecutionTime(),
                                nextEvent.getAbsoluteExecutionTime().get());
                    }
                    this.simulatorTime.set(nextEvent.getAbsoluteExecutionTime().get());

                    // carry out all events scheduled on this simulation time, as long as we are still running.
                    while (!this.stoppingState && !this.eventList.isEmpty()
                            && nextEvent.getAbsoluteExecutionTime().eq(this.simulatorTime))
                    {
                        nextEvent = this.eventList.removeFirst();
                        try
                        {
                            nextEvent.execute();
                            if (this.eventList.isEmpty())
                            {
                                this.simulatorTime.set(this.runUntilTime);
                                this.stoppingState = true;
                                break;
                            }
                            int cmp = this.eventList.first().getAbsoluteExecutionTime().get().compareTo(this.runUntilTime);
                            if ((cmp == 0 && !this.runUntilIncluding) || cmp > 0)
                            {
                                this.simulatorTime.set(this.runUntilTime);
                                this.stoppingState = true;
                                break;
                            }
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
                        if (!this.eventList.isEmpty())
                        {
                            // peek at next event for while loop.
                            nextEvent = this.eventList.first();
                        }
                    }
                }
            }
        }
        fireTimedEvent(SimulatorInterface.TIME_CHANGED_EVENT, null, this.simulatorTime.get());

        synchronized (this.animation)
        {
            if (this.animation && this.animationThread != null)
            {
                updateAnimation();
                this.animationThread.stopAnimation();
            }
        }
    }

    /**
     * Indicate whether we support animation or not.
     * @param animation boolean; whether we support animation or not
     */
    public void setAnimation(final boolean animation)
    {
        synchronized (this.animation)
        {
            if (this.animation == animation)
            {
                return; // nothing changed
            }
            if (this.animation)
            {
                if (this.animationThread != null)
                {
                    this.animationThread.stopAnimation();
                    this.animationThread = null;
                }
            }
            else
            {
                if (isRunning())
                {
                    this.animationThread = new AnimationThread(this);
                    this.animationThread.start();
                }
            }
            this.animation = animation;
        }
    }

    /**
     * @return animation boolean; whether we support animation or not
     */
    public final boolean isAnimation()
    {
        return this.animation;
    }

    /**
     * @return speedFactor
     */
    public final double getSpeedFactor()
    {
        return this.speedFactor;
    }

    /**
     * Set the speedFactor, and send a CHANGE_SPEED_FACTOR event.
     * @param newSpeedFactor double; the new speed factor to set
     * @param fireChangeSpeedFactorEvent boolean; whether to fire a CHANGE_SPEED_FACTOR event or not
     */
    public final void setSpeedFactor(final double newSpeedFactor, final boolean fireChangeSpeedFactorEvent)
    {
        this.speedFactor = newSpeedFactor;
        if (fireChangeSpeedFactorEvent)
        {
            this.fireEvent(CHANGE_SPEED_FACTOR_EVENT, newSpeedFactor);
        }
    }

    /**
     * Set the speedFactor, and send a CHANGE_SPEED_FACTOR event.
     * @param newSpeedFactor double; set speedFactor
     */
    public final void setSpeedFactor(final double newSpeedFactor)
    {
        setSpeedFactor(newSpeedFactor, true);
    }

    /**
     * @return catchup
     */
    public final boolean isCatchup()
    {
        return this.catchup;
    }

    /**
     * @param catchup boolean; set catchup
     */
    public final void setCatchup(final boolean catchup)
    {
        this.catchup = catchup;
    }

    /**
     * The relative update delay in milliseconds is the step size in wall clock time by which the time is updated between
     * events. Default, this value is set at 10 msec, which means that the simulation updates its clock with 100 Hz between
     * events.
     * @return the relative update delay in milliseconds
     */
    public final int getUpdateMsec()
    {
        return this.updateMsec;
    }

    /**
     * The relative update delay in milliseconds is the step size in wall clock time by which the time is updated between
     * events. Default, this value is set at 10 msec, which means that the simulation updates its clock with 100 Hz between
     * events. When this is too course, set e.g. to 1, which means that the clock will be updated with 1 kHz between events. The
     * latter can be important in real time simulations. Note that the housekeeping of the simulation clock takes time as well,
     * so 1 kHz can be too fine grained in some situations.
     * @param updateMsec int; set the relative update delay in milliseconds
     */
    public final void setUpdateMsec(final int updateMsec)
    {
        this.updateMsec = updateMsec;
    }

    /***********************************************************************************************************/
    /************************************* EASY ACCESS CLASS EXTENSIONS ****************************************/
    /***********************************************************************************************************/

    /** Easy access class RealTimeClock.TimeDouble. */
    public static class TimeDouble extends DEVSRealTimeClock<Double, Double, SimTimeDouble>
            implements DEVSSimulatorInterface.TimeDouble
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /**
         * the translation from a millisecond on the wall clock to '1.0' in the simulation time. This means that if the wall
         * clock runs in seconds, the factor should be 0.001.
         */
        private final double msecWallClockToSimTimeUnit;

        /**
         * Construct a DEVSRealTimeClock.TimeDouble.
         * @param id the id of the simulator, used in logging and firing of events.
         * @param msecWallClockToSimTimeUnit double; the translation between a millisecond on the clock and '1.0' in the
         *            simulation time.
         */
        public TimeDouble(final Serializable id, final double msecWallClockToSimTimeUnit)
        {
            super(id);
            this.msecWallClockToSimTimeUnit = msecWallClockToSimTimeUnit;
        }

        /** {@inheritDoc} */
        @Override
        protected final Double simulatorTimeForWallClockMillis(final double wallMilliseconds)
        {
            return this.msecWallClockToSimTimeUnit * wallMilliseconds;
        }

        /** {@inheritDoc} */
        @SuppressWarnings("unchecked")
        @Override
        public Replication.TimeDouble<? extends DEVSSimulatorInterface.TimeDouble> getReplication()
        {
            return (Replication.TimeDouble<? extends DEVSSimulatorInterface.TimeDouble>) super.getReplication();
        }
    }

    /** Easy access class RealTimeClock.TimeFloat. */
    public abstract static class TimeFloat extends DEVSRealTimeClock<Float, Float, SimTimeFloat>
            implements DEVSSimulatorInterface.TimeFloat
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /**
         * Construct a DEVSRealTimeClock.TimeFloat.
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

    /** Easy access class RealTimeClock.TimeLong. */
    public abstract static class TimeLong extends DEVSRealTimeClock<Long, Long, SimTimeLong>
            implements DEVSSimulatorInterface.TimeLong
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /**
         * Construct a DEVSRealTimeClock.TimeLong.
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

    /** Easy access class RealTimeClock.TimeDoubleUnit. */
    public static class TimeDoubleUnit extends DEVSRealTimeClock<Time, Duration, SimTimeDoubleUnit>
            implements DEVSSimulatorInterface.TimeDoubleUnit
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /**
         * Construct a DEVSRealTimeClock.TimeDoubleUnit.
         * @param id the id of the simulator, used in logging and firing of events.
         */
        public TimeDoubleUnit(final Serializable id)
        {
            super(id);
        }

        /** {@inheritDoc} */
        @Override
        protected final Duration simulatorTimeForWallClockMillis(final double wallMilliseconds)
        {
            return new Duration(wallMilliseconds, DurationUnit.MILLISECOND);
        }

        /** {@inheritDoc} */
        @SuppressWarnings("unchecked")
        @Override
        public Replication.TimeDoubleUnit<? extends DEVSSimulatorInterface.TimeDoubleUnit> getReplication()
        {
            return (Replication.TimeDoubleUnit<? extends DEVSSimulatorInterface.TimeDoubleUnit>) super.getReplication();
        }
    }

    /** Easy access class RealTimeClock.TimeFloatUnit. */
    public static class TimeFloatUnit extends DEVSRealTimeClock<FloatTime, FloatDuration, SimTimeFloatUnit>
            implements DEVSSimulatorInterface.TimeFloatUnit
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /**
         * Construct a DEVSRealTimeClock.TimeFloatUnit.
         * @param id the id of the simulator, used in logging and firing of events.
         */
        public TimeFloatUnit(final Serializable id)
        {
            super(id);
        }

        /** {@inheritDoc} */
        @Override
        protected final FloatDuration simulatorTimeForWallClockMillis(final double wallMilliseconds)
        {
            return new FloatDuration((float) wallMilliseconds, DurationUnit.MILLISECOND);
        }

        /** {@inheritDoc} */
        @SuppressWarnings("unchecked")
        @Override
        public Replication.TimeFloatUnit<? extends DEVSSimulatorInterface.TimeFloatUnit> getReplication()
        {
            return (Replication.TimeFloatUnit<? extends DEVSSimulatorInterface.TimeFloatUnit>) super.getReplication();
        }
    }

    /** Easy access class RealTimeClock.CalendarDouble. */
    public static class CalendarDouble extends DEVSRealTimeClock<Calendar, Duration, SimTimeCalendarDouble>
            implements DEVSSimulatorInterface.CalendarDouble
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /**
         * Construct a DEVSRealTimeClock.CalendarDouble.
         * @param id the id of the simulator, used in logging and firing of events.
         */
        public CalendarDouble(final Serializable id)
        {
            super(id);
        }

        /** {@inheritDoc} */
        @Override
        protected final Duration simulatorTimeForWallClockMillis(final double wallMilliseconds)
        {
            return new Duration(wallMilliseconds, DurationUnit.MILLISECOND);
        }

        /** {@inheritDoc} */
        @SuppressWarnings("unchecked")
        @Override
        public Replication.CalendarDouble<? extends DEVSSimulatorInterface.CalendarDouble> getReplication()
        {
            return (Replication.CalendarDouble<? extends DEVSSimulatorInterface.CalendarDouble>) super.getReplication();
        }
    }

    /** Easy access class RealTimeClock.CalendarFloat. */
    public static class CalendarFloat extends DEVSRealTimeClock<Calendar, FloatDuration, SimTimeCalendarFloat>
            implements DEVSSimulatorInterface.CalendarFloat
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /**
         * Construct a DEVSRealTimeClock.CalendarFloat.
         * @param id the id of the simulator, used in logging and firing of events.
         */
        public CalendarFloat(final Serializable id)
        {
            super(id);
        }

        /** {@inheritDoc} */
        @Override
        protected final FloatDuration simulatorTimeForWallClockMillis(final double wallMilliseconds)
        {
            return new FloatDuration((float) wallMilliseconds, DurationUnit.MILLISECOND);
        }

        /** {@inheritDoc} */
        @SuppressWarnings("unchecked")
        @Override
        public Replication.CalendarFloat<? extends DEVSSimulatorInterface.CalendarFloat> getReplication()
        {
            return (Replication.CalendarFloat<? extends DEVSSimulatorInterface.CalendarFloat>) super.getReplication();
        }
    }

    /** Easy access class RealTimeClock.CalendarLong. */
    public static class CalendarLong extends DEVSRealTimeClock<Calendar, Long, SimTimeCalendarLong>
            implements DEVSSimulatorInterface.CalendarLong
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /**
         * Construct a DEVSRealTimeClock.CalendarLong.
         * @param id the id of the simulator, used in logging and firing of events.
         */
        public CalendarLong(final Serializable id)
        {
            super(id);
        }

        /** {@inheritDoc} */
        @Override
        protected final Long simulatorTimeForWallClockMillis(final double wallMilliseconds)
        {
            return (long) wallMilliseconds;
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
