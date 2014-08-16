/*
 * @(#) RealTimeClock.java Sep 6, 2003 Copyright (c) 2002-2005 Delft University
 * of Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. This software is proprietary information of Delft University of
 * Technology 
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
import nl.tudelft.simulation.event.EventType;
import nl.tudelft.simulation.logger.Logger;

/**
 * The reference implementation of the realTimeClock. The realTime clock is a DEVDESS simulator which runs at a ratio of
 * realTime. If the executionTime exceeds the timeStep, a catchup mechanism is triggered to make up lost time in
 * consecutive steps.
 * <p>
 * (c) copyright 2004 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="http://www.peter-jacobs.com/index.htm">Peter Jacobs </a>
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:36:44 $
 * @param <A> the absolute storage type for the simulation time, e.g. Calendar, UnitTimeDouble, or Double.
 * @param <R> the relative type for time storage, e.g. Long for the Calendar. For most non-calendar types, the absolute
 *            and relative types are the same.
 * @param <T> the extended type itself to be able to implement a comparator on the simulation time.
 * @since 1.5
 */
public class RealTimeClock<A extends Comparable<A>, R extends Number & Comparable<R>, T extends SimTime<A, R, T>>
        extends DEVDESSAnimator<A, R, T> implements DEVDESSSimulatorInterface<A, R, T>
{
    /** */
    private static final long serialVersionUID = 20140805L;

    /** the backlog event */
    public static final EventType BACKLOG_EVENT = new EventType("BACKLOG_EVENT");

    /** the backLog of the clock */
    private long backlog = 0L;

    /** the starttime of the clock */
    private long startTime = 0L;

    /**
     * constructs a new RealTimeClock.
     * @param initialTimeStep the initial time step to use in the integration.
     */
    public RealTimeClock(final R initialTimeStep)
    {
        super(initialTimeStep);
    }

    /**
     * @see nl.tudelft.simulation.dsol.simulators.DEVSSimulator#run()
     */
    @Override
    public void run()
    {
        super.worker.setPriority(Thread.MAX_PRIORITY);
        this.startTime = System.currentTimeMillis();
        int count = 0;
        long animationFactor = Math.round(this.animationDelay); // TODO: / this.timeStep.doubleValue());
        while (this.isRunning() && !this.eventList.isEmpty()
                && this.simulatorTime.le(this.replication.getTreatment().getEndTime()))
        {
            long now = System.currentTimeMillis();
            T runUntil = this.simulatorTime.plus(this.timeStep); // TODO: (now - this.startTime) +
                                                                 // this.timeStep.longValue();
            while (!this.eventList.isEmpty() && this.running
                    && runUntil.ge(this.eventList.first().getAbsoluteExecutionTime()))
            {
                synchronized (super.semaphore)
                {
                    SimEventInterface<T> event = this.eventList.removeFirst();
                    this.simulatorTime = event.getAbsoluteExecutionTime();
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
            if ((count % animationFactor) == 0)
            {
                this.fireEvent(AnimatorInterface.UPDATE_ANIMATION_EVENT, this.simulatorTime, this.simulatorTime);
            }
            count++;
            try
            {
                long used = System.currentTimeMillis() - now;
                long delay = 0L; // TODO: Math.round(this.timeStep.doubleValue() - used);
                if (delay >= 0)
                {
                    long catchUp = Math.min(this.backlog, delay);
                    this.backlog = this.backlog - catchUp;
                    super.fireEvent(BACKLOG_EVENT, (-delay + catchUp));
                    Thread.sleep(delay - catchUp);
                }
                else
                {
                    this.backlog = this.backlog + (-1 * delay);
                    super.fireEvent(BACKLOG_EVENT, -1 * delay);
                }
            }
            catch (InterruptedException interruptedException)
            {
                // Nothing to be done.
                interruptedException = null;
            }
        }
    }

    /**
     * @see nl.tudelft.simulation.dsol.simulators.AnimatorInterface #getAnimationDelay()
     */
    @Override
    public long getAnimationDelay()
    {
        return this.animationDelay;
    }

    /**
     * @see nl.tudelft.simulation.dsol.simulators.AnimatorInterface #setAnimationDelay(long)
     */
    @Override
    public void setAnimationDelay(final long animationDelay)
    {
        this.animationDelay = animationDelay;
    }

    /***********************************************************************************************************/
    /************************************* EASY ACCESS CLASS EXTENSIONS ****************************************/
    /***********************************************************************************************************/

    /** Easy access class RealTimeClock.Double */
    public class Double extends RealTimeClock<java.lang.Double, java.lang.Double, SimTimeDouble> implements
            DEVDESSSimulatorInterface.Double
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

    /** Easy access class RealTimeClock.Float */
    public class Float extends RealTimeClock<java.lang.Float, java.lang.Float, SimTimeFloat> implements
            DEVDESSSimulatorInterface.Float
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

    /** Easy access class RealTimeClock.Long */
    public class Long extends RealTimeClock<java.lang.Long, java.lang.Long, SimTimeLong> implements
            DEVDESSSimulatorInterface.Long
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

    /** Easy access class RealTimeClock.DoubleUnit */
    public class DoubleUnit extends RealTimeClock<UnitTimeDouble, UnitTimeDouble, SimTimeDoubleUnit> implements
            DEVDESSSimulatorInterface.DoubleUnit
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

    /** Easy access class RealTimeClock.FloatUnit */
    public class FloatUnit extends RealTimeClock<UnitTimeFloat, UnitTimeFloat, SimTimeFloatUnit> implements
            DEVDESSSimulatorInterface.FloatUnit
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

    /** Easy access class RealTimeClock.LongUnit */
    public class LongUnit extends RealTimeClock<UnitTimeLong, UnitTimeLong, SimTimeLongUnit> implements
            DEVDESSSimulatorInterface.LongUnit
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

    /** Easy access class RealTimeClock.CalendarDouble */
    public class CalendarDouble extends RealTimeClock<Calendar, UnitTimeDouble, SimTimeCalendarDouble> implements
            DEVDESSSimulatorInterface.CalendarDouble
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

    /** Easy access class RealTimeClock.CalendarFloat */
    public class CalendarFloat extends RealTimeClock<Calendar, UnitTimeFloat, SimTimeCalendarFloat> implements
            DEVDESSSimulatorInterface.CalendarFloat
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

    /** Easy access class RealTimeClock.CalendarLong */
    public class CalendarLong extends RealTimeClock<Calendar, UnitTimeLong, SimTimeCalendarLong> implements
            DEVDESSSimulatorInterface.CalendarLong
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