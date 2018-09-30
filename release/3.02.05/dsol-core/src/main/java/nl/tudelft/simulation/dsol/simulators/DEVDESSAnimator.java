package nl.tudelft.simulation.dsol.simulators;

import java.util.Calendar;

import org.djunits.value.vdouble.scalar.Duration;
import org.djunits.value.vdouble.scalar.Time;
import org.djunits.value.vfloat.scalar.FloatDuration;
import org.djunits.value.vfloat.scalar.FloatTime;

import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.formalisms.eventscheduling.SimEventInterface;
import nl.tudelft.simulation.dsol.logger.SimLogger;
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
 * The reference implementation of the animator.
 * <p>
 * Copyright (c) 2002-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. See for project information <a href="https://simulation.tudelft.nl/" target="_blank">
 * https://simulation.tudelft.nl</a>. The DSOL project is distributed under a three-clause BSD-style license, which can
 * be found at <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @param <A> the absolute storage type for the simulation time, e.g. Calendar, Duration, or Double.
 * @param <R> the relative type for time storage, e.g. Long for the Calendar. For most non-calendar types, the absolute
 *            and relative types are the same.
 * @param <T> the extended type itself to be able to implement a comparator on the simulation time.
 * @since 1.5
 */
public class DEVDESSAnimator<A extends Comparable<A>, R extends Number & Comparable<R>, T extends SimTime<A, R, T>>
        extends DEVDESSSimulator<A, R, T> implements AnimatorInterface
{
    /** */
    private static final long serialVersionUID = 20140804L;

    /**
     * @param initialTimeStep the initial time step to use in the integration.
     * @throws SimRuntimeException when initialTimeStep &lt;= 0, NaN, or Infinity
     */
    public DEVDESSAnimator(final R initialTimeStep) throws SimRuntimeException
    {
        super(initialTimeStep);
    }

    /** AnimationDelay refers to the delay in milliseconds between timeSteps. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected long animationDelay = 100L;

    /** {@inheritDoc} */
    @Override
    public final long getAnimationDelay()
    {
        return this.animationDelay;
    }

    /** {@inheritDoc} */
    @Override
    public final void setAnimationDelay(final long animationDelay)
    {
        this.animationDelay = animationDelay;
        this.fireEvent(ANIMATION_DELAY_CHANGED_EVENT, animationDelay);
    }

    /** {@inheritDoc} */
    @Override
    public final void updateAnimation()
    {
        this.fireEvent(AnimatorInterface.UPDATE_ANIMATION_EVENT, this.simulatorTime);
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("checkstyle:designforextension")
    public void run()
    {
        AnimationThread animationThread = new AnimationThread(this);
        animationThread.start();
        while (this.isRunning() && !this.eventList.isEmpty()
                && this.simulatorTime.le(this.replication.getTreatment().getEndSimTime()))
        {
            try
            {
                if (this.animationDelay > 0)
                {
                    Thread.sleep(this.animationDelay);
                }
            }
            catch (Exception exception)
            {
                exception = null;
                // Let's neglect this sleep..
            }
            T runUntil = this.simulatorTime.plus(this.timeStep);
            while (!this.eventList.isEmpty() && this.running
                    && runUntil.ge(this.eventList.first().getAbsoluteExecutionTime()))
            {
                synchronized (super.semaphore)
                {
                    SimEventInterface<T> event = this.eventList.removeFirst();
                    if (event.getAbsoluteExecutionTime().ne(super.simulatorTime))
                    {
                        super.fireTimedEvent(SimulatorInterface.TIME_CHANGED_EVENT, event.getAbsoluteExecutionTime(),
                                event.getAbsoluteExecutionTime().get());
                    }
                    this.simulatorTime = event.getAbsoluteExecutionTime();
                    try
                    {
                        event.execute();
                    }
                    catch (Exception exception)
                    {
                        SimLogger.always().error(exception, "run");
                    }
                }
            }
            if (this.running)
            {
                this.simulatorTime = runUntil;
            }
            this.fireTimedEvent(SimulatorInterface.TIME_CHANGED_EVENT, this.simulatorTime, this.simulatorTime.get());
        }
        updateAnimation();
        animationThread.stopAnimation();
    }

    /***********************************************************************************************************/
    /************************************* EASY ACCESS CLASS EXTENSIONS ****************************************/
    /***********************************************************************************************************/

    /** Easy access class Animator.TimeDouble. */
    public static class TimeDouble extends DEVDESSAnimator<Double, Double, SimTimeDouble>
            implements DEVDESSSimulatorInterface.TimeDouble
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /**
         * @param initialTimeStep the initial time step to use in the integration.
         * @throws SimRuntimeException when initialTimeStep &lt;= 0, NaN, or Infinity
         */
        public TimeDouble(final Double initialTimeStep) throws SimRuntimeException
        {
            super(initialTimeStep);
        }
    }

    /** Easy access class Animator.TimeFloat. */
    public static class TimeFloat extends DEVDESSAnimator<Float, Float, SimTimeFloat>
            implements DEVDESSSimulatorInterface.TimeFloat
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /**
         * @param initialTimeStep the initial time step to use in the integration.
         * @throws SimRuntimeException when initialTimeStep &lt;= 0, NaN, or Infinity
         */
        public TimeFloat(final Float initialTimeStep) throws SimRuntimeException
        {
            super(initialTimeStep);
        }
    }

    /** Easy access class Animator.TimeLong. */
    public static class TimeLong extends DEVDESSAnimator<Long, Long, SimTimeLong>
            implements DEVDESSSimulatorInterface.TimeLong
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /**
         * @param initialTimeStep the initial time step to use in the integration.
         * @throws SimRuntimeException when initialTimeStep &lt;= 0, NaN, or Infinity
         */
        public TimeLong(final Long initialTimeStep) throws SimRuntimeException
        {
            super(initialTimeStep);
        }
    }

    /** Easy access class Animator.DoubleUnit. */
    public static class DoubleUnit extends DEVDESSAnimator<Time, Duration, SimTimeDoubleUnit>
            implements DEVDESSSimulatorInterface.TimeDoubleUnit
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /**
         * @param initialTimeStep the initial time step to use in the integration.
         * @throws SimRuntimeException when initialTimeStep &lt;= 0, NaN, or Infinity
         */
        public DoubleUnit(final Duration initialTimeStep) throws SimRuntimeException
        {
            super(initialTimeStep);
        }
    }

    /** Easy access class Animator.TimeFloatUnit. */
    public static class TimeFloatUnit extends DEVDESSAnimator<FloatTime, FloatDuration, SimTimeFloatUnit>
            implements DEVDESSSimulatorInterface.TimeFloatUnit
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /**
         * @param initialTimeStep the initial time step to use in the integration.
         * @throws SimRuntimeException when initialTimeStep &lt;= 0, NaN, or Infinity
         */
        public TimeFloatUnit(final FloatDuration initialTimeStep) throws SimRuntimeException
        {
            super(initialTimeStep);
        }
    }

    /** Easy access class Animator.CalendarDouble. */
    public static class CalendarDouble extends DEVDESSAnimator<Calendar, Duration, SimTimeCalendarDouble>
            implements DEVDESSSimulatorInterface.CalendarDouble
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /**
         * @param initialTimeStep the initial time step to use in the integration.
         * @throws SimRuntimeException when initialTimeStep &lt;= 0, NaN, or Infinity
         */
        public CalendarDouble(final Duration initialTimeStep) throws SimRuntimeException
        {
            super(initialTimeStep);
        }
    }

    /** Easy access class Animator.CalendarFloat. */
    public static class CalendarFloat extends DEVDESSAnimator<Calendar, FloatDuration, SimTimeCalendarFloat>
            implements DEVDESSSimulatorInterface.CalendarFloat
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /**
         * @param initialTimeStep the initial time step to use in the integration.
         * @throws SimRuntimeException when initialTimeStep &lt;= 0, NaN, or Infinity
         */
        public CalendarFloat(final FloatDuration initialTimeStep) throws SimRuntimeException
        {
            super(initialTimeStep);
        }
    }

    /** Easy access class Animator.CalendarLong. */
    public static class CalendarLong extends DEVDESSAnimator<Calendar, Long, SimTimeCalendarLong>
            implements DEVDESSSimulatorInterface.CalendarLong
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /**
         * @param initialTimeStep the initial time step to use in the integration.
         * @throws SimRuntimeException when initialTimeStep &lt;= 0, NaN, or Infinity
         */
        public CalendarLong(final Long initialTimeStep) throws SimRuntimeException
        {
            super(initialTimeStep);
        }
    }

}
