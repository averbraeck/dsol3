package nl.tudelft.simulation.dsol.formalisms.flow;

import java.io.Serializable;
import java.util.Collections;
import java.util.SortedMap;
import java.util.TreeMap;

import org.djunits.value.vdouble.scalar.Duration;
import org.djunits.value.vdouble.scalar.Time;
import org.djunits.value.vfloat.scalar.FloatDuration;
import org.djunits.value.vfloat.scalar.FloatTime;

import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.formalisms.eventscheduling.SimEvent;
import nl.tudelft.simulation.dsol.logger.Cat;
import nl.tudelft.simulation.dsol.simtime.SimTime;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simtime.SimTimeDoubleUnit;
import nl.tudelft.simulation.dsol.simtime.SimTimeFloat;
import nl.tudelft.simulation.dsol.simtime.SimTimeFloatUnit;
import nl.tudelft.simulation.dsol.simtime.SimTimeLong;
import nl.tudelft.simulation.dsol.simtime.dist.DistContinuousSimTime;
import nl.tudelft.simulation.dsol.simtime.dist.DistContinuousSimulationTime;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;

/**
 * The schedule is an extension to the generate which accepts a schedule of interarrival times. Instead of generating with a
 * continuous interarrival distribution we submit a map consiting of keys (execution times). Each key indicates the <i>starting
 * time </i> of a new interval, while the value in the map is the continuous distribution function to use to draw the
 * interarrival times. If no values have to be generated in a certain interval, use a large interarrival time value in the
 * distribution function, or use DistConstant(stream, 1E20) to indicate that the next drawing will take place <i>after </i> the
 * end of the interval.
 * <p>
 * Copyright (c) 2002-2022 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @param <A> the absolute storage type for the simulation time, e.g. Time, Float, or Double.
 * @param <R> the relative type for time storage, e.g. Duration for absolute Time. For most non-unit types, the absolute and
 *            relative types are the same.
 * @param <T> the extended type itself to be able to implement a comparator on the simulation time.
 * @since 1.5
 */
public class Schedule<A extends Comparable<A> & Serializable, R extends Number & Comparable<R>, T extends SimTime<A, R, T>>
        extends Generator<A, R, T>
{
    /** */
    private static final long serialVersionUID = 20140805L;

    /**
     * schedule is a time sorted map of distributions.
     */
    private SortedMap<T, DistContinuousSimulationTime<R>> schedule =
            Collections.synchronizedSortedMap(new TreeMap<T, DistContinuousSimulationTime<R>>());

    /**
     * constructs a new Schedule.
     * @param id Serializable; the id of the Station
     * @param simulator DEVSSimulatorInterface&lt;A,R,T&gt;; is the on which the construction of the objects must be scheduled.
     * @param myClass Class&lt;?&gt;; is the class of which entities are created
     * @param constructorArguments Object[]; are the parameters for the constructor of myClass. of arguments.
     *            <code>constructorArgument[n]=Integer.valueOf(12)</code> may have constructorArgumentClasses[n]=int.class;
     * @throws SimRuntimeException on constructor invocation.
     */
    public Schedule(final Serializable id, final DEVSSimulatorInterface<A, R, T> simulator, final Class<?> myClass,
            final Object[] constructorArguments) throws SimRuntimeException
    {
        super(id, simulator, myClass, constructorArguments);
    }

    /**
     * returns the schedule.
     * @return SortedMap the schedule
     */
    public SortedMap<T, DistContinuousSimulationTime<R>> getSchedule()
    {
        return this.schedule;
    }

    /**
     * sets the schedule.
     * @param map SortedMap&lt;T, DistContinuousSimulationTime&lt;R&gt;&gt;; is the new map
     */
    public synchronized void setSchedule(final SortedMap<T, DistContinuousSimulationTime<R>> map)
    {
        this.schedule = map;
        this.changeIntervalTime();
    }

    /**
     * changes the intervalTime of the schedule.
     */
    public synchronized void changeIntervalTime()
    {
        try
        {
            if (!this.schedule.isEmpty())
            {
                this.simulator.cancelEvent(super.nextEvent);
                this.interval = this.schedule.values().iterator().next();
                this.schedule.remove(this.schedule.firstKey());
                this.simulator.scheduleEvent(new SimEvent<T>(this.schedule.firstKey(), this, this, "changeIntervalTime", null));
                this.generate(this.constructorArguments);
                this.simulator.getLogger().filter(Cat.DSOL).trace("changeIntervalTime: set the intervalTime to {}",
                        this.interval);
            }
        }
        catch (Exception exception)
        {
            this.simulator.getLogger().always().warn(exception, "changeIntervalTime");
        }
    }

    /***********************************************************************************************************/
    /************************************* EASY ACCESS CLASS EXTENSIONS ****************************************/
    /***********************************************************************************************************/

    /** Easy access class Schedule.TimeDouble. */
    public static class TimeDouble extends Schedule<Double, Double, SimTimeDouble> implements StationInterface.TimeDouble
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * Constructs a new generator for objects in a simulation. Constructed objects are sent to the 'destination' of the
         * Schedule when a destination has been indicated with the setDestination method. This constructor has a maximum number
         * of entities generated, which results in stopping the generator when the maximum number of entities has been reached.
         * @param id Serializable; the id of the Station
         * @param simulator DEVSSimulatorInterface.TimeDouble; is the on which the construction of the objects must be
         *            scheduled.
         * @param myClass Class&lt;?&gt;; is the class of which entities are created
         * @param constructorArguments Object[]; are the parameters for the constructor of myClass. of arguments.
         *            <code>constructorArgument[n]=Integer.valueOf(12)</code> may have constructorArgumentClasses[n]=int.class;
         * @throws SimRuntimeException on constructor invocation.
         */
        public TimeDouble(final Serializable id, final DEVSSimulatorInterface.TimeDouble simulator, final Class<?> myClass,
                final Object[] constructorArguments) throws SimRuntimeException
        {
            super(id, simulator, myClass, constructorArguments);
        }

        /**
         * sets the interarrival distribution.
         * @param interval DistContinuousSimulationTime.TimeDouble; is the interarrival time
         */
        public void setInterval(final DistContinuousSimulationTime.TimeDouble interval)
        {
            super.setInterval(interval);
        }

        /** {@inheritDoc} */
        @Override
        public DistContinuousSimulationTime.TimeDouble getInterval()
        {
            return (DistContinuousSimulationTime.TimeDouble) this.interval;
        }

        /**
         * sets the startTime.
         * @param startTime DistContinuousSimTime.TimeDouble; is the absolute startTime
         */
        public synchronized void setStartTime(final DistContinuousSimTime.TimeDouble startTime)
        {
            super.setStartTime(startTime);
        }

        /** {@inheritDoc} */
        @Override
        public DistContinuousSimTime.TimeDouble getStartTime()
        {
            return (DistContinuousSimTime.TimeDouble) this.startTime;
        }
    }

    /** Easy access class Schedule.TimeFloat. */
    public static class TimeFloat extends Schedule<Float, Float, SimTimeFloat> implements StationInterface.TimeFloat
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * Constructs a new generator for objects in a simulation. Constructed objects are sent to the 'destination' of the
         * Schedule when a destination has been indicated with the setDestination method. This constructor has a maximum number
         * of entities generated, which results in stopping the generator when the maximum number of entities has been reached.
         * @param id Serializable; the id of the Station
         * @param simulator DEVSSimulatorInterface.TimeFloat; is the on which the construction of the objects must be scheduled.
         * @param myClass Class&lt;?&gt;; is the class of which entities are created
         * @param constructorArguments Object[]; are the parameters for the constructor of myClass. of arguments.
         *            <code>constructorArgument[n]=Integer.valueOf(12)</code> may have constructorArgumentClasses[n]=int.class;
         * @throws SimRuntimeException on constructor invocation.
         */
        public TimeFloat(final Serializable id, final DEVSSimulatorInterface.TimeFloat simulator, final Class<?> myClass,
                final Object[] constructorArguments) throws SimRuntimeException
        {
            super(id, simulator, myClass, constructorArguments);
        }

        /**
         * sets the interarrival distribution.
         * @param interval DistContinuousSimulationTime.TimeFloat; is the interarrival time
         */
        public void setInterval(final DistContinuousSimulationTime.TimeFloat interval)
        {
            super.setInterval(interval);
        }

        /** {@inheritDoc} */
        @Override
        public DistContinuousSimulationTime.TimeFloat getInterval()
        {
            return (DistContinuousSimulationTime.TimeFloat) this.interval;
        }

        /**
         * sets the startTime.
         * @param startTime DistContinuousSimTime.TimeFloat; is the absolute startTime
         */
        public synchronized void setStartTime(final DistContinuousSimTime.TimeFloat startTime)
        {
            super.setStartTime(startTime);
        }

        /** {@inheritDoc} */
        @Override
        public DistContinuousSimTime.TimeFloat getStartTime()
        {
            return (DistContinuousSimTime.TimeFloat) this.startTime;
        }
    }

    /** Easy access class Schedule.TimeLong. */
    public static class TimeLong extends Schedule<Long, Long, SimTimeLong> implements StationInterface.TimeLong
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * Constructs a new generator for objects in a simulation. Constructed objects are sent to the 'destination' of the
         * Schedule when a destination has been indicated with the setDestination method. This constructor has a maximum number
         * of entities generated, which results in stopping the generator when the maximum number of entities has been reached.
         * @param id Serializable; the id of the Station
         * @param simulator DEVSSimulatorInterface.TimeLong; is the on which the construction of the objects must be scheduled.
         * @param myClass Class&lt;?&gt;; is the class of which entities are created
         * @param constructorArguments Object[]; are the parameters for the constructor of myClass. of arguments.
         *            <code>constructorArgument[n]=Integer.valueOf(12)</code> may have constructorArgumentClasses[n]=int.class;
         * @throws SimRuntimeException on constructor invocation.
         */
        public TimeLong(final Serializable id, final DEVSSimulatorInterface.TimeLong simulator, final Class<?> myClass,
                final Object[] constructorArguments) throws SimRuntimeException
        {
            super(id, simulator, myClass, constructorArguments);
        }

        /**
         * sets the interarrival distribution.
         * @param interval DistContinuousSimulationTime.TimeLong; is the interarrival time
         */
        public void setInterval(final DistContinuousSimulationTime.TimeLong interval)
        {
            super.setInterval(interval);
        }

        /** {@inheritDoc} */
        @Override
        public DistContinuousSimulationTime.TimeLong getInterval()
        {
            return (DistContinuousSimulationTime.TimeLong) this.interval;
        }

        /**
         * sets the startTime.
         * @param startTime DistContinuousSimTime.TimeLong; is the absolute startTime
         */
        public synchronized void setStartTime(final DistContinuousSimTime.TimeLong startTime)
        {
            super.setStartTime(startTime);
        }

        /** {@inheritDoc} */
        @Override
        public DistContinuousSimTime.TimeLong getStartTime()
        {
            return (DistContinuousSimTime.TimeLong) this.startTime;
        }
    }

    /** Easy access class Schedule.TimeDoubleUnit. */
    public static class TimeDoubleUnit extends Schedule<Time, Duration, SimTimeDoubleUnit>
            implements StationInterface.TimeDoubleUnit
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * Constructs a new generator for objects in a simulation. Constructed objects are sent to the 'destination' of the
         * Schedule when a destination has been indicated with the setDestination method. This constructor has a maximum number
         * of entities generated, which results in stopping the generator when the maximum number of entities has been reached.
         * @param id Serializable; the id of the Station
         * @param simulator DEVSSimulatorInterface.TimeDoubleUnit; is the on which the construction of the objects must be
         *            scheduled.
         * @param myClass Class&lt;?&gt;; is the class of which entities are created
         * @param constructorArguments Object[]; are the parameters for the constructor of myClass. of arguments.
         *            <code>constructorArgument[n]=Integer.valueOf(12)</code> may have constructorArgumentClasses[n]=int.class;
         * @throws SimRuntimeException on constructor invocation.
         */
        public TimeDoubleUnit(final Serializable id, final DEVSSimulatorInterface.TimeDoubleUnit simulator,
                final Class<?> myClass, final Object[] constructorArguments) throws SimRuntimeException
        {
            super(id, simulator, myClass, constructorArguments);
        }

        /**
         * sets the interarrival distribution.
         * @param interval DistContinuousSimulationTime.TimeDoubleUnit; is the interarrival time
         */
        public void setInterval(final DistContinuousSimulationTime.TimeDoubleUnit interval)
        {
            super.setInterval(interval);
        }

        /** {@inheritDoc} */
        @Override
        public DistContinuousSimulationTime.TimeDoubleUnit getInterval()
        {
            return (DistContinuousSimulationTime.TimeDoubleUnit) this.interval;
        }

        /**
         * sets the startTime.
         * @param startTime DistContinuousSimTime.TimeDoubleUnit; is the absolute startTime
         */
        public synchronized void setStartTime(final DistContinuousSimTime.TimeDoubleUnit startTime)
        {
            super.setStartTime(startTime);
        }

        /** {@inheritDoc} */
        @Override
        public DistContinuousSimTime.TimeDoubleUnit getStartTime()
        {
            return (DistContinuousSimTime.TimeDoubleUnit) this.startTime;
        }
    }

    /** Easy access class Schedule.TimeDoubleUnitUnit. */
    public static class TimeFloatUnit extends Schedule<FloatTime, FloatDuration, SimTimeFloatUnit>
            implements StationInterface.TimeFloatUnit
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * Constructs a new generator for objects in a simulation. Constructed objects are sent to the 'destination' of the
         * Schedule when a destination has been indicated with the setDestination method. This constructor has a maximum number
         * of entities generated, which results in stopping the generator when the maximum number of entities has been reached.
         * @param id Serializable; the id of the Station
         * @param simulator DEVSSimulatorInterface.TimeFloatUnit; is the on which the construction of the objects must be
         *            scheduled.
         * @param myClass Class&lt;?&gt;; is the class of which entities are created
         * @param constructorArguments Object[]; are the parameters for the constructor of myClass. of arguments.
         *            <code>constructorArgument[n]=Integer.valueOf(12)</code> may have constructorArgumentClasses[n]=int.class;
         * @throws SimRuntimeException on constructor invocation.
         */
        public TimeFloatUnit(final Serializable id, final DEVSSimulatorInterface.TimeFloatUnit simulator,
                final Class<?> myClass, final Object[] constructorArguments) throws SimRuntimeException
        {
            super(id, simulator, myClass, constructorArguments);
        }

        /**
         * sets the interarrival distribution.
         * @param interval DistContinuousSimulationTime.TimeFloatUnit; is the interarrival time
         */
        public void setInterval(final DistContinuousSimulationTime.TimeFloatUnit interval)
        {
            super.setInterval(interval);
        }

        /** {@inheritDoc} */
        @Override
        public DistContinuousSimulationTime.TimeFloatUnit getInterval()
        {
            return (DistContinuousSimulationTime.TimeFloatUnit) this.interval;
        }

        /**
         * sets the startTime.
         * @param startTime DistContinuousSimTime.TimeFloatUnit; is the absolute startTime
         */
        public synchronized void setStartTime(final DistContinuousSimTime.TimeFloatUnit startTime)
        {
            super.setStartTime(startTime);
        }

        /** {@inheritDoc} */
        @Override
        public DistContinuousSimTime.TimeFloatUnit getStartTime()
        {
            return (DistContinuousSimTime.TimeFloatUnit) this.startTime;
        }
    }

}
