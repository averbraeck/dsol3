package nl.tudelft.simulation.dsol.simulators;

import java.util.Calendar;

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
import nl.tudelft.simulation.dsol.simtime.SimTimeLongUnit;
import nl.tudelft.simulation.dsol.simtime.TimeUnit;
import nl.tudelft.simulation.dsol.simtime.UnitTimeDouble;
import nl.tudelft.simulation.dsol.simtime.UnitTimeFloat;
import nl.tudelft.simulation.dsol.simtime.UnitTimeLong;
import nl.tudelft.simulation.event.EventType;

/**
 * The reference implementation of the realTimeClock. The realTime clock is a DEVS simulator which runs at a ratio of
 * realTime. If the executionTime exceeds the timeStep, a catchup mechanism can be triggered to make up lost time in
 * consecutive steps.
 * <p>
 * (c) copyright 2004 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:36:44 $
 * @param <A> the absolute storage type for the simulation time, e.g. Calendar, UnitTimeDouble, or Double.
 * @param <R> the relative type for time storage, e.g. Long for the Calendar. For most non-calendar types, the absolute
 *            and relative types are the same.
 * @param <T> the extended type itself to be able to implement a comparator on the simulation time.
 * @since 1.5
 */
public abstract class DEVSRealTimeClock<A extends Comparable<A>, R extends Number & Comparable<R>, T extends SimTime<A, R, T>>
        extends DEVSAnimator<A, R, T> implements DEVSSimulatorInterface<A, R, T>
{
    /** */
    private static final long serialVersionUID = 20150428L;

    /** the backlog event. */
    public static final EventType BACKLOG_EVENT = new EventType("BACKLOG_EVENT");

    /** the speed factor compared to real time clock. &lt;1 is slower, &gt;1 is faster, 1 is real time speed. */
    private double speedFactor = 1.0;

    /**
     * The relative update delay in milliseconds is the step size in wall clock time by which the time is updated
     * between events. Default, this value is set at 10 msec, which means that the simulation updates its clock with 100
     * Hz between events. When this is too course, set e.g. to 1, which means that the clock will be updated with 1 kHz
     * between events. The latter can be important in real time simulations. Note that the housekeeping of the
     * simulation clock takes time as well, so 1 kHz can be too fine grained in some situations.
     */
    private int updateMsec = 10;

    /** catch up or not catch up after running behind. */
    private boolean catchup = true;

    /**
     * Calculate the relative time step to do "factor" milliseconds on the simulation clock.
     * @param factor the factor to multiply the milliseconds with
     * @return the relative time step.
     */
    protected abstract R relativeMillis(double factor);

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("checkstyle:designforextension")
    public void run()
    {
        AnimationThread animationThread = new AnimationThread(this);
        animationThread.start();

        long clockTime0 = System.currentTimeMillis(); // _________ current zero for the wall clock
        T simTime0 = this.simulatorTime; // ______________________ current zero for the sim clock
        double factor = this.speedFactor; // _____________________ local copy of speed factor to detect change
        double msec1 = relativeMillis(1.0).doubleValue(); // _____ translation factor for 1 msec for sim clock
        R rSim = this.relativeMillis(this.updateMsec * factor); // sim clock change for 'updateMsec' wall clock

        while (this.isRunning() && !this.eventList.isEmpty()
                && this.simulatorTime.le(this.replication.getTreatment().getEndTime()))
        {
            // check if speedFactor has changed. If yes: re-baseline.
            if (factor != this.speedFactor)
            {
                clockTime0 = System.currentTimeMillis();
                simTime0 = this.simulatorTime;
                factor = this.speedFactor;
                rSim = this.relativeMillis(this.updateMsec * factor);
            }

            // check if we are behind; syncTime is the needed current time on the wall-clock
            double syncTime = (System.currentTimeMillis() - clockTime0) * msec1 * factor;
            // delta is the time we might be behind
            double simTime = this.simulatorTime.minus(simTime0).doubleValue();

            if (syncTime > simTime)
            {
                // we are behind
                if (!this.catchup)
                {
                    // if no catch-up: re-baseline.
                    clockTime0 = System.currentTimeMillis();
                    simTime0 = this.simulatorTime;
                }
                else
                {
                    // jump to the required wall-clock related time or to the time of the next event, whichever comes
                    // first
                    synchronized (super.semaphore)
                    {
                        R delta = relativeMillis((syncTime - simTime) / msec1);
                        T absSyncTime = this.simulatorTime.plus(delta);
                        T eventTime = this.eventList.first().getAbsoluteExecutionTime();
                        if (absSyncTime.lt(eventTime))
                        {
                            this.simulatorTime = absSyncTime;
                        }
                        else
                        {
                            this.simulatorTime = eventTime;
                        }
                    }
                }
            }

            // peek at the first event and determine the time difference relative to RT speed; that determines
            // how long we have to wait.
            SimEventInterface<T> event = this.eventList.first();
            double simTimeDiffMillis =
                    (event.getAbsoluteExecutionTime().minus(simTime0)).doubleValue() / (msec1 * factor);

            /*
             * simTimeDiff gives the number of milliseconds between the last event and this event. if speed == 1, this
             * is the number of milliseconds we have to wait. if speed == 10, we have to wait 1/10 of that. If the speed
             * == 0.1, we have to wait 10 times that amount. We might also be behind.
             */
            if (simTimeDiffMillis >= (System.currentTimeMillis() - clockTime0))
            {
                while (simTimeDiffMillis > System.currentTimeMillis() - clockTime0)
                {
                    try
                    {
                        Thread.sleep(this.updateMsec);

                        // check if speedFactor has changed. If yes: break out of this loop and execute event.
                        // this could cause a jump.
                        if (factor != this.speedFactor)
                        {
                            simTimeDiffMillis = 0.0;
                        }

                    }
                    catch (InterruptedException ie)
                    {
                        // do nothing
                        ie = null;
                    }

                    // check if an event has been inserted. In a real-time situation this can be dome by other threads
                    if (!event.equals(this.eventList.first())) // event inserted by a thread...
                    {
                        event = this.eventList.first();
                        simTimeDiffMillis =
                                (event.getAbsoluteExecutionTime().minus(simTime0)).doubleValue() / (msec1 * factor);
                    }
                    else
                    {
                        // make a small time step for the animation during wallclock waiting.
                        // but never beyond the next event time.
                        if (this.simulatorTime.plus(rSim).lt(event.getAbsoluteExecutionTime()))
                        {
                            synchronized (super.semaphore)
                            {
                                this.simulatorTime.add(rSim);
                            }
                        }
                    }
                }
            }

            synchronized (super.semaphore)
            {
                this.simulatorTime = event.getAbsoluteExecutionTime();
                this.fireTimedEvent(SimulatorInterface.TIME_CHANGED_EVENT, this.simulatorTime, this.simulatorTime);

                // carry out all events scheduled on this simulation time, as long as we are still running.
                while (this.isRunning() && !this.eventList.isEmpty()
                        && event.getAbsoluteExecutionTime().eq(this.simulatorTime))
                {
                    event = this.eventList.removeFirst();
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
                    if (!this.eventList.isEmpty())
                    {
                        // peek at next event for while loop.
                        event = this.eventList.first();
                    }
                }
            }
        }
        this.fireTimedEvent(SimulatorInterface.TIME_CHANGED_EVENT, this.simulatorTime, this.simulatorTime);
        updateAnimation();
        animationThread.stopAnimation();
    }

    /**
     * @return speedFactor
     */
    public final double getSpeedFactor()
    {
        return this.speedFactor;
    }

    /**
     * @param speedFactor set speedFactor
     */
    public final void setSpeedFactor(final double speedFactor)
    {
        this.speedFactor = speedFactor;
    }

    /**
     * @return catchup
     */
    public final boolean isCatchup()
    {
        return this.catchup;
    }

    /**
     * @param catchup set catchup
     */
    public final void setCatchup(final boolean catchup)
    {
        this.catchup = catchup;
    }

    /**
     * The relative update delay in milliseconds is the step size in wall clock time by which the time is updated
     * between events. Default, this value is set at 10 msec, which means that the simulation updates its clock with 100
     * Hz between events.
     * @return the relative update delay in milliseconds
     */
    public final int getUpdateMsec()
    {
        return this.updateMsec;
    }

    /**
     * The relative update delay in milliseconds is the step size in wall clock time by which the time is updated
     * between events. Default, this value is set at 10 msec, which means that the simulation updates its clock with 100
     * Hz between events. When this is too course, set e.g. to 1, which means that the clock will be updated with 1 kHz
     * between events. The latter can be important in real time simulations. Note that the housekeeping of the
     * simulation clock takes time as well, so 1 kHz can be too fine grained in some situations.
     * @param updateMsec set the relative update delay in milliseconds
     */
    public final void setUpdateMsec(final int updateMsec)
    {
        this.updateMsec = updateMsec;
    }

    /***********************************************************************************************************/
    /************************************* EASY ACCESS CLASS EXTENSIONS ****************************************/
    /***********************************************************************************************************/

    /** Easy access class RealTimeClock.TimeDouble. */
    public static class TimeDouble extends DEVSRealTimeClock<Double, Double, SimTimeDouble> implements
            DEVSSimulatorInterface.TimeDouble
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /**
         * the translation from a millisecond on the wall clock to '1.0' in the simulation time. This means that if the
         * wall clock runs in seconds, the factor should be 0.001.
         */
        private final double msecWallClockToSimTimeUnit;

        /**
         * @param msecWallClockToSimTimeUnit the translation between a millisecond on the clock and '1.0' in the
         *            simulation time.
         */
        public TimeDouble(final double msecWallClockToSimTimeUnit)
        {
            super();
            this.msecWallClockToSimTimeUnit = msecWallClockToSimTimeUnit;
        }

        /** {@inheritDoc} */
        @Override
        protected final Double relativeMillis(final double factor)
        {
            return this.msecWallClockToSimTimeUnit * factor;
        }
    }

    /** Easy access class RealTimeClock.TimeFloat. */
    public abstract static class TimeFloat extends DEVSRealTimeClock<Float, Float, SimTimeFloat> implements
            DEVSSimulatorInterface.TimeFloat
    {
        /** */
        private static final long serialVersionUID = 20140805L;
    }

    /** Easy access class RealTimeClock.TimeLong. */
    public abstract static class TimeLong extends DEVSRealTimeClock<Long, Long, SimTimeLong> implements
            DEVSSimulatorInterface.TimeLong
    {
        /** */
        private static final long serialVersionUID = 20140805L;
    }

    /** Easy access class RealTimeClock.TimeDoubleUnit. */
    public static class TimeDoubleUnit extends DEVSRealTimeClock<UnitTimeDouble, UnitTimeDouble, SimTimeDoubleUnit>
            implements DEVSSimulatorInterface.TimeDoubleUnit
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /** {@inheritDoc} */
        @Override
        protected final UnitTimeDouble relativeMillis(final double factor)
        {
            return new UnitTimeDouble(factor, TimeUnit.MILLISECOND);
        }
    }

    /** Easy access class RealTimeClock.TimeFloatUnit. */
    public static class TimeFloatUnit extends DEVSRealTimeClock<UnitTimeFloat, UnitTimeFloat, SimTimeFloatUnit>
            implements DEVSSimulatorInterface.TimeFloatUnit
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /** {@inheritDoc} */
        @Override
        protected final UnitTimeFloat relativeMillis(final double factor)
        {
            return new UnitTimeFloat((float) factor, TimeUnit.MILLISECOND);
        }
    }

    /** Easy access class RealTimeClock.TimeLongUnit. */
    public static class TimeLongUnit extends DEVSRealTimeClock<UnitTimeLong, UnitTimeLong, SimTimeLongUnit> implements
            DEVSSimulatorInterface.TimeLongUnit
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /** {@inheritDoc} */
        @Override
        protected final UnitTimeLong relativeMillis(final double factor)
        {
            return new UnitTimeLong((long) factor, TimeUnit.MILLISECOND);
        }
    }

    /** Easy access class RealTimeClock.CalendarDouble. */
    public static class CalendarDouble extends DEVSRealTimeClock<Calendar, UnitTimeDouble, SimTimeCalendarDouble>
            implements DEVSSimulatorInterface.CalendarDouble
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /** {@inheritDoc} */
        @Override
        protected final UnitTimeDouble relativeMillis(final double factor)
        {
            return new UnitTimeDouble(factor, TimeUnit.MILLISECOND);
        }
    }

    /** Easy access class RealTimeClock.CalendarFloat. */
    public static class CalendarFloat extends DEVSRealTimeClock<Calendar, UnitTimeFloat, SimTimeCalendarFloat>
            implements DEVSSimulatorInterface.CalendarFloat
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /** {@inheritDoc} */
        @Override
        protected final UnitTimeFloat relativeMillis(final double factor)
        {
            return new UnitTimeFloat((float) factor, TimeUnit.MILLISECOND);
        }
    }

    /** Easy access class RealTimeClock.CalendarLong. */
    public static class CalendarLong extends DEVSRealTimeClock<Calendar, UnitTimeLong, SimTimeCalendarLong> implements
            DEVSSimulatorInterface.CalendarLong
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /** {@inheritDoc} */
        @Override
        protected final UnitTimeLong relativeMillis(final double factor)
        {
            return new UnitTimeLong((long) factor, TimeUnit.MILLISECOND);
        }
    }

}
