/*
 * @(#) Animator.java Sep 6, 2003 Copyright (c) 2002-2005 Delft University of
 * Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved.
 * This software is proprietary information of Delft University of Technology
 * 
 */
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
import nl.tudelft.simulation.dsol.simtime.UnitTimeDouble;
import nl.tudelft.simulation.dsol.simtime.UnitTimeFloat;
import nl.tudelft.simulation.dsol.simtime.UnitTimeLong;
import nl.tudelft.simulation.logger.Logger;

/**
 * The reference implementation of the animator.
 * <p>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl"> www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="http://www.peter-jacobs.com">Peter Jacobs </a>
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:36:44 $
 * @param <A> the absolute storage type for the simulation time, e.g. Calendar, UnitTimeDouble, or Double.
 * @param <R> the relative type for time storage, e.g. Long for the Calendar. For most non-calendar types, the absolute
 *            and relative types are the same.
 * @param <T> the extended type itself to be able to implement a comparator on the simulation time.
 * @since 1.5
 */
public class Animator<A extends Comparable<A>, R extends Number & Comparable<R>, T extends SimTime<A, R, T>> extends
        DEVDESSSimulator<A, R, T> implements AnimatorInterface<A, R, T>
{
    /** */
    private static final long serialVersionUID = 20140804L;

    /**
     * @param initialTimeStep
     */
    public Animator(final R initialTimeStep)
    {
        super(initialTimeStep);
    }

    /** AnimationDelay refers to the delay in milliseconds between timeSteps */
    protected long animationDelay = 100L;

    /**
     * @see nl.tudelft.simulation.dsol.simulators.AnimatorInterface #getAnimationDelay()
     */
    public long getAnimationDelay()
    {
        return this.animationDelay;
    }

    /**
     * @see nl.tudelft.simulation.dsol.simulators.AnimatorInterface #setAnimationDelay(long)
     */
    public void setAnimationDelay(final long animationDelay)
    {
        this.animationDelay = animationDelay;
        Logger.finer(this, "setAnimationDelay", "set the animationDelay to " + animationDelay);
        this.fireEvent(ANIMATION_DELAY_CHANGED_EVENT, animationDelay);
    }

    /**
     * @see nl.tudelft.simulation.dsol.simulators.DEVSSimulator#run()
     */
    @Override
    public void run()
    {
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
                // Let's neglect this sleep...
            }
            T runUntil = this.simulatorTime.plus(this.timeStep);
            while (!this.eventList.isEmpty() && this.running
                    && runUntil.ge(this.eventList.first().getAbsoluteExecutionTime()))
            {
                synchronized (super.semaphore)
                {
                    SimEventInterface<T> event = this.eventList.removeFirst();
                    this.simulatorTime = event.getAbsoluteExecutionTime();
                    this.fireEvent(SimulatorInterface.TIME_CHANGED_EVENT, this.simulatorTime, this.simulatorTime);
                    try
                    {
                        event.execute();
                    }
                    catch (Exception exception)
                    {
                        Logger.severe(this, "run", exception);
                    }
                }
            }
            if (this.running)
            {
                this.simulatorTime = runUntil;
            }
            this.fireEvent(SimulatorInterface.TIME_CHANGED_EVENT, this.simulatorTime, this.simulatorTime);
            this.fireEvent(AnimatorInterface.UPDATE_ANIMATION_EVENT, this.simulatorTime);
        }
    }

    /***********************************************************************************************************/
    /************************************* EASY ACCESS CLASS EXTENSIONS ****************************************/
    /***********************************************************************************************************/

    /** Easy access class Animator.Double */
    public static class Double extends Animator<java.lang.Double, java.lang.Double, SimTimeDouble> implements
            AnimatorInterface.Double
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /**
         * @param initialTimeStep
         */
        public Double(final java.lang.Double initialTimeStep)
        {
            super(initialTimeStep);
        }
    }

    /** Easy access class Animator.Float */
    public static class Float extends Animator<java.lang.Float, java.lang.Float, SimTimeFloat> implements
            AnimatorInterface.Float
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /**
         * @param initialTimeStep
         */
        public Float(final java.lang.Float initialTimeStep)
        {
            super(initialTimeStep);
        }
    }

    /** Easy access class Animator.Long */
    public static class Long extends Animator<java.lang.Long, java.lang.Long, SimTimeLong> implements
            AnimatorInterface.Long
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /**
         * @param initialTimeStep
         */
        public Long(final java.lang.Long initialTimeStep)
        {
            super(initialTimeStep);
        }
    }

    /** Easy access class Animator.DoubleUnit */
    public static class DoubleUnit extends Animator<UnitTimeDouble, UnitTimeDouble, SimTimeDoubleUnit> implements
            AnimatorInterface.DoubleUnit
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /**
         * @param initialTimeStep
         */
        public DoubleUnit(final UnitTimeDouble initialTimeStep)
        {
            super(initialTimeStep);
        }
    }

    /** Easy access class Animator.FloatUnit */
    public static class FloatUnit extends Animator<UnitTimeFloat, UnitTimeFloat, SimTimeFloatUnit> implements
            AnimatorInterface.FloatUnit
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /**
         * @param initialTimeStep
         */
        public FloatUnit(final UnitTimeFloat initialTimeStep)
        {
            super(initialTimeStep);
        }
    }

    /** Easy access class Animator.LongUnit */
    public static class LongUnit extends Animator<UnitTimeLong, UnitTimeLong, SimTimeLongUnit> implements
            AnimatorInterface.LongUnit
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /**
         * @param initialTimeStep
         */
        public LongUnit(final UnitTimeLong initialTimeStep)
        {
            super(initialTimeStep);
        }
    }

    /** Easy access class Animator.CalendarDouble */
    public static class CalendarDouble extends Animator<Calendar, UnitTimeDouble, SimTimeCalendarDouble> implements
            AnimatorInterface.CalendarDouble
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /**
         * @param initialTimeStep
         */
        public CalendarDouble(final UnitTimeDouble initialTimeStep)
        {
            super(initialTimeStep);
        }
    }

    /** Easy access class Animator.CalendarFloat */
    public static class CalendarFloat extends Animator<Calendar, UnitTimeFloat, SimTimeCalendarFloat> implements
            AnimatorInterface.CalendarFloat
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /**
         * @param initialTimeStep
         */
        public CalendarFloat(final UnitTimeFloat initialTimeStep)
        {
            super(initialTimeStep);
        }
    }

    /** Easy access class Animator.CalendarLong */
    public static class CalendarLong extends Animator<Calendar, UnitTimeLong, SimTimeCalendarLong> implements
            AnimatorInterface.CalendarLong
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /**
         * @param initialTimeStep
         */
        public CalendarLong(final UnitTimeLong initialTimeStep)
        {
            super(initialTimeStep);
        }
    }

}