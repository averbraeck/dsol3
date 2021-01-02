package nl.tudelft.simulation.dsol.formalisms.flow;

import java.io.Serializable;
import java.util.Calendar;

import org.djunits.value.vdouble.scalar.Duration;
import org.djunits.value.vdouble.scalar.Time;
import org.djunits.value.vfloat.scalar.FloatDuration;
import org.djunits.value.vfloat.scalar.FloatTime;

import nl.tudelft.simulation.dsol.simtime.SimTime;
import nl.tudelft.simulation.dsol.simtime.SimTimeCalendarDouble;
import nl.tudelft.simulation.dsol.simtime.SimTimeCalendarFloat;
import nl.tudelft.simulation.dsol.simtime.SimTimeCalendarLong;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simtime.SimTimeDoubleUnit;
import nl.tudelft.simulation.dsol.simtime.SimTimeFloat;
import nl.tudelft.simulation.dsol.simtime.SimTimeFloatUnit;
import nl.tudelft.simulation.dsol.simtime.SimTimeLong;
import nl.tudelft.simulation.dsol.simtime.dist.DistContinuousSimulationTime;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;

/**
 * The Delay object is a station which delays an entity by some time units. When an entity arrives at a delay object, dsol
 * delays the entity by the resulting time period. During the time delay, the entity is held in the delay object.
 * <p>
 * Copyright (c) 2002-2021 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @param <A> the absolute storage type for the simulation time, e.g. Calendar, Duration, or Double.
 * @param <R> the relative type for time storage, e.g. Long for the Calendar. For most non-calendar types, the absolute and
 *            relative types are the same.
 * @param <T> the extended type itself to be able to implement a comparator on the simulation time.
 * @since 1.5
 */
public class Delay<A extends Comparable<A> & Serializable, R extends Number & Comparable<R>, T extends SimTime<A, R, T>>
        extends Station<A, R, T>
{
    /** */
    private static final long serialVersionUID = 20140805L;

    /** delayDistribution which is the distribution defining the delay. */
    private DistContinuousSimulationTime<R> delayDistribution;

    /**
     * Constructor for Delay. * @param id Serializable; the id of the Station
     * @param simulator DEVSSimulatorInterface&lt;A,R,T&gt;; is the simulator
     * @param id Serializable; the id of the Station
     * @param delayDistribution DistContinuousSimulationTime&lt;R&gt;; is the delayDistribution
     */
    public Delay(final Serializable id, final DEVSSimulatorInterface<A, R, T> simulator,
            final DistContinuousSimulationTime<R> delayDistribution)
    {
        super(id, simulator);
        this.delayDistribution = delayDistribution;
    }

    /** {@inheritDoc} */
    @Override
    public final synchronized void receiveObject(final Object object)
    {
        super.receiveObject(object);
        try
        {
            this.simulator.scheduleEventRel(this.delayDistribution.draw(), this, this, "releaseObject", new Object[] {object});
        }
        catch (Exception exception)
        {
            this.simulator.getLogger().always().warn(exception, "receiveObject");
        }
    }

    /***********************************************************************************************************/
    /************************************* EASY ACCESS CLASS EXTENSIONS ****************************************/
    /***********************************************************************************************************/

    /** Easy access class Delay.TimeDouble. */
    public static class TimeDouble extends Delay<Double, Double, SimTimeDouble> implements StationInterface.TimeDouble
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * Constructor for TimeDouble Delay.
         * @param id Serializable; the id of the Station
         * @param simulator DEVSSimulatorInterface.TimeDouble; is the simulator
         * @param delayDistribution DistContinuousSimulationTime.TimeDouble; is the delayDistribution
         */
        public TimeDouble(final Serializable id, final DEVSSimulatorInterface.TimeDouble simulator,
                final DistContinuousSimulationTime.TimeDouble delayDistribution)
        {
            super(id, simulator, delayDistribution);
        }
    }

    /** Easy access class Delay.TimeFloat. */
    public static class TimeFloat extends Delay<Float, Float, SimTimeFloat> implements StationInterface.TimeFloat
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * Constructor for TimeFloat Delay.
         * @param id Serializable; the id of the Station
         * @param simulator DEVSSimulatorInterface.TimeFloat; is the simulator
         * @param delayDistribution DistContinuousSimulationTime.TimeFloat; is the delayDistribution
         */
        public TimeFloat(final Serializable id, final DEVSSimulatorInterface.TimeFloat simulator,
                final DistContinuousSimulationTime.TimeFloat delayDistribution)
        {
            super(id, simulator, delayDistribution);
        }
    }

    /** Easy access class Delay.TimeLong. */
    public static class TimeLong extends Delay<Long, Long, SimTimeLong> implements StationInterface.TimeLong
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * Constructor for TimeLong Delay.
         * @param id Serializable; the id of the Station
         * @param simulator DEVSSimulatorInterface.TimeLong; is the simulator
         * @param delayDistribution DistContinuousSimulationTime.TimeLong; is the delayDistribution
         */
        public TimeLong(final Serializable id, final DEVSSimulatorInterface.TimeLong simulator,
                final DistContinuousSimulationTime.TimeLong delayDistribution)
        {
            super(id, simulator, delayDistribution);
        }
    }

    /** Easy access class Delay.TimeDoubleUnit. */
    public static class TimeDoubleUnit extends Delay<Time, Duration, SimTimeDoubleUnit>
            implements StationInterface.TimeDoubleUnit
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * Constructor for TimeDoubleUnit Delay.
         * @param id Serializable; the id of the Station
         * @param simulator DEVSSimulatorInterface.TimeDoubleUnit; is the simulator
         * @param delayDistribution DistContinuousSimulationTime.TimeDoubleUnit; is the delayDistribution
         */
        public TimeDoubleUnit(final Serializable id, final DEVSSimulatorInterface.TimeDoubleUnit simulator,
                final DistContinuousSimulationTime.TimeDoubleUnit delayDistribution)
        {
            super(id, simulator, delayDistribution);
        }
    }

    /** Easy access class Delay.TimeFloatUnit. */
    public static class TimeFloatUnit extends Delay<FloatTime, FloatDuration, SimTimeFloatUnit>
            implements StationInterface.TimeFloatUnit
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * Constructor for TimeFloatUnit Delay.
         * @param id Serializable; the id of the Station\n\r @param simulator
         * @param simulator DEVSSimulatorInterface.TimeFloatUnit; is the simulator
         * @param delayDistribution DistContinuousSimulationTime.TimeFloatUnit; is the delayDistribution
         */
        public TimeFloatUnit(final Serializable id, final DEVSSimulatorInterface.TimeFloatUnit simulator,
                final DistContinuousSimulationTime.TimeFloatUnit delayDistribution)
        {
            super(id, simulator, delayDistribution);
        }
    }

    /** Easy access class Delay.CalendarDouble. */
    public static class CalendarDouble extends Delay<Calendar, Duration, SimTimeCalendarDouble>
            implements StationInterface.CalendarDouble
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * Constructor for CalendarDouble Delay.
         * @param id Serializable; the id of the Station
         * @param simulator DEVSSimulatorInterface.CalendarDouble; is the simulator
         * @param delayDistribution DistContinuousSimulationTime.CalendarDouble; is the delayDistribution
         */
        public CalendarDouble(final Serializable id, final DEVSSimulatorInterface.CalendarDouble simulator,
                final DistContinuousSimulationTime.CalendarDouble delayDistribution)
        {
            super(id, simulator, delayDistribution);
        }
    }

    /** Easy access class Delay.CalendarFloat. */
    public static class CalendarFloat extends Delay<Calendar, FloatDuration, SimTimeCalendarFloat>
            implements StationInterface.CalendarFloat
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * Constructor for CalendarFloat Delay.
         * @param id Serializable; the id of the Station
         * @param simulator DEVSSimulatorInterface.CalendarFloat; is the simulator
         * @param delayDistribution DistContinuousSimulationTime.CalendarFloat; is the delayDistribution
         */
        public CalendarFloat(final Serializable id, final DEVSSimulatorInterface.CalendarFloat simulator,
                final DistContinuousSimulationTime.CalendarFloat delayDistribution)
        {
            super(id, simulator, delayDistribution);
        }
    }

    /** Easy access class Delay.CalendarLong. */
    public static class CalendarLong extends Delay<Calendar, Long, SimTimeCalendarLong> implements StationInterface.CalendarLong
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * Constructor for CalendarLong Delay.
         * @param id Serializable; the id of the Station
         * @param simulator DEVSSimulatorInterface.CalendarLong; is the simulator
         * @param delayDistribution DistContinuousSimulationTime.CalendarLong; is the delayDistribution
         */
        public CalendarLong(final Serializable id, final DEVSSimulatorInterface.CalendarLong simulator,
                final DistContinuousSimulationTime.CalendarLong delayDistribution)
        {
            super(id, simulator, delayDistribution);
        }
    }
}
