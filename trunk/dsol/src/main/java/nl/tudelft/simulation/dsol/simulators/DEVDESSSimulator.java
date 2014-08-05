/*
 * @(#)AnimatorInterface.java Aug 18, 2003 Copyright (c) 2002-2005 Delft
 * University of Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All
 * rights reserved. This software is proprietary information of Delft University
 * of Technology 
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
 * The reference implementation of the DEVDESS simulator.
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
public class DEVDESSSimulator<A extends Comparable<A>, R extends Number & Comparable<R>, T extends SimTime<A, R, T>>
        extends DEVSSimulator<A, R, T> implements DEVDESSSimulatorInterface<A, R, T>
{
    /** */
    private static final long serialVersionUID = 20140805L;

    /** timeStep represents the timestep of the DESS simulator */
    protected R timeStep;

    /**
     * Construct a DEVDESSSimulator with an initial time step for the integration process.
     * @param initialTimeStep the initial time step to use in the integration.
     */
    public DEVDESSSimulator(final R initialTimeStep)
    {
        super();
        setTimeStep(initialTimeStep);
    }

    /**
     * @see nl.tudelft.simulation.dsol.simulators.DESSSimulatorInterface #getTimeStep()
     */
    public R getTimeStep()
    {
        return this.timeStep;
    }

    /**
     * @see nl.tudelft.simulation.dsol.simulators.DESSSimulatorInterface #setTimeStep(double)
     */
    @Override
    public void setTimeStep(final R timeStep)
    {
        synchronized (super.semaphore)
        {
            // TODO: how to find out that a timestep < 0 and throw an exception if that is the case.
            this.timeStep = timeStep;
            this.fireEvent(TIME_STEP_CHANGED_EVENT, timeStep);
        }
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
            synchronized (super.semaphore)
            {
                T runUntil = this.simulatorTime.plus(this.timeStep);
                while (!this.eventList.isEmpty() && this.running
                        && runUntil.ge(this.eventList.first().getAbsoluteExecutionTime()))
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
                if (this.running)
                {
                    this.simulatorTime = runUntil;
                }
                this.fireEvent(SimulatorInterface.TIME_CHANGED_EVENT, this.simulatorTime, this.simulatorTime);
            }
        }
    }

    /***********************************************************************************************************/
    /************************************* EASY ACCESS CLASS EXTENSIONS ****************************************/
    /***********************************************************************************************************/

    /** Easy access class DEVDESSSimulator.Double */
    public static class Double extends DEVDESSSimulator<java.lang.Double, java.lang.Double, SimTimeDouble> implements
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

    /** Easy access class DEVDESSSimulator.Float */
    public static class Float extends DEVDESSSimulator<java.lang.Float, java.lang.Float, SimTimeFloat> implements
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

    /** Easy access class DEVDESSSimulator.Long */
    public static class Long extends DEVDESSSimulator<java.lang.Long, java.lang.Long, SimTimeLong> implements
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

    /** Easy access class DEVDESSSimulator.DoubleUnit */
    public static class DoubleUnit extends DEVDESSSimulator<UnitTimeDouble, UnitTimeDouble, SimTimeDoubleUnit>
            implements DEVDESSSimulatorInterface.DoubleUnit
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

    /** Easy access class DEVDESSSimulator.FloatUnit */
    public static class FloatUnit extends DEVDESSSimulator<UnitTimeFloat, UnitTimeFloat, SimTimeFloatUnit> implements
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

    /** Easy access class DEVDESSSimulator.LongUnit */
    public static class LongUnit extends DEVDESSSimulator<UnitTimeLong, UnitTimeLong, SimTimeLongUnit> implements
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

    /** Easy access class DEVDESSSimulator.CalendarDouble */
    public static class CalendarDouble extends DEVDESSSimulator<Calendar, UnitTimeDouble, SimTimeCalendarDouble>
            implements DEVDESSSimulatorInterface.CalendarDouble
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

    /** Easy access class DEVDESSSimulator.CalendarFloat */
    public static class CalendarFloat extends DEVDESSSimulator<Calendar, UnitTimeFloat, SimTimeCalendarFloat> implements
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

    /** Easy access class DEVDESSSimulator.CalendarLong */
    public static class CalendarLong extends DEVDESSSimulator<Calendar, UnitTimeLong, SimTimeCalendarLong> implements
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