package nl.tudelft.simulation.dsol.simulators;

import java.util.Calendar;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
import nl.tudelft.simulation.dsol.simtime.UnitTimeDouble;
import nl.tudelft.simulation.dsol.simtime.UnitTimeFloat;
import nl.tudelft.simulation.dsol.simtime.UnitTimeLong;

/**
 * The reference implementation of the animator.
 * <p>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl"> www.simulation.tudelft.nl </a> <br>
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
public class DEVDESSAnimator<A extends Comparable<A>, R extends Number & Comparable<R>, T extends SimTime<A, R, T>>
        extends DEVDESSSimulator<A, R, T> implements AnimatorInterface
{
    /** */
    private static final long serialVersionUID = 20140804L;
    
    /** the logger. */
    private static Logger logger = LogManager.getLogger(DEVDESSAnimator.class);

    /**
     * @param initialTimeStep the initial time step to use in the integration.
     */
    public DEVDESSAnimator(final R initialTimeStep)
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
                && this.simulatorTime.le(this.replication.getTreatment().getEndTime()))
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
                    this.simulatorTime = event.getAbsoluteExecutionTime();
                    this.fireTimedEvent(SimulatorInterface.TIME_CHANGED_EVENT, this.simulatorTime, this.simulatorTime.get());
                    try
                    {
                        event.execute();
                    }
                    catch (Exception exception)
                    {
                        logger.error("run", exception);
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
         */
        public TimeDouble(final Double initialTimeStep)
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
         */
        public TimeFloat(final Float initialTimeStep)
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
         */
        public TimeLong(final Long initialTimeStep)
        {
            super(initialTimeStep);
        }
    }

    /** Easy access class Animator.DoubleUnit. */
    public static class DoubleUnit extends DEVDESSAnimator<UnitTimeDouble, UnitTimeDouble, SimTimeDoubleUnit>
            implements DEVDESSSimulatorInterface.TimeDoubleUnit
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /**
         * @param initialTimeStep the initial time step to use in the integration.
         */
        public DoubleUnit(final UnitTimeDouble initialTimeStep)
        {
            super(initialTimeStep);
        }
    }

    /** Easy access class Animator.TimeFloatUnit. */
    public static class TimeFloatUnit extends DEVDESSAnimator<UnitTimeFloat, UnitTimeFloat, SimTimeFloatUnit>
            implements DEVDESSSimulatorInterface.TimeFloatUnit
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /**
         * @param initialTimeStep the initial time step to use in the integration.
         */
        public TimeFloatUnit(final UnitTimeFloat initialTimeStep)
        {
            super(initialTimeStep);
        }
    }

    /** Easy access class Animator.TimeLongUnit. */
    public static class TimeLongUnit extends DEVDESSAnimator<UnitTimeLong, UnitTimeLong, SimTimeLongUnit>
            implements DEVDESSSimulatorInterface.TimeLongUnit
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /**
         * @param initialTimeStep the initial time step to use in the integration.
         */
        public TimeLongUnit(final UnitTimeLong initialTimeStep)
        {
            super(initialTimeStep);
        }
    }

    /** Easy access class Animator.CalendarDouble. */
    public static class CalendarDouble extends DEVDESSAnimator<Calendar, UnitTimeDouble, SimTimeCalendarDouble>
            implements DEVDESSSimulatorInterface.CalendarDouble
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /**
         * @param initialTimeStep the initial time step to use in the integration.
         */
        public CalendarDouble(final UnitTimeDouble initialTimeStep)
        {
            super(initialTimeStep);
        }
    }

    /** Easy access class Animator.CalendarFloat. */
    public static class CalendarFloat extends DEVDESSAnimator<Calendar, UnitTimeFloat, SimTimeCalendarFloat>
            implements DEVDESSSimulatorInterface.CalendarFloat
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /**
         * @param initialTimeStep the initial time step to use in the integration.
         */
        public CalendarFloat(final UnitTimeFloat initialTimeStep)
        {
            super(initialTimeStep);
        }
    }

    /** Easy access class Animator.CalendarLong. */
    public static class CalendarLong extends DEVDESSAnimator<Calendar, UnitTimeLong, SimTimeCalendarLong>
            implements DEVDESSSimulatorInterface.CalendarLong
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /**
         * @param initialTimeStep the initial time step to use in the integration.
         */
        public CalendarLong(final UnitTimeLong initialTimeStep)
        {
            super(initialTimeStep);
        }
    }

}
