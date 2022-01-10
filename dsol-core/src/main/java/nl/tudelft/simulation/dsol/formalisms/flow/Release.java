package nl.tudelft.simulation.dsol.formalisms.flow;

import java.io.Serializable;

import org.djunits.value.vdouble.scalar.Duration;
import org.djunits.value.vdouble.scalar.Time;
import org.djunits.value.vfloat.scalar.FloatDuration;
import org.djunits.value.vfloat.scalar.FloatTime;

import nl.tudelft.simulation.dsol.formalisms.Resource;
import nl.tudelft.simulation.dsol.simtime.SimTime;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simtime.SimTimeDoubleUnit;
import nl.tudelft.simulation.dsol.simtime.SimTimeFloat;
import nl.tudelft.simulation.dsol.simtime.SimTimeFloatUnit;
import nl.tudelft.simulation.dsol.simtime.SimTimeLong;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;

/**
 * The release station releases a given quantity of a claimed resource.
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
public class Release<A extends Comparable<A> & Serializable, R extends Number & Comparable<R>, T extends SimTime<A, R, T>>
        extends Station<A, R, T>
{
    /** */
    private static final long serialVersionUID = 20151028L;

    /** resource refers to the resource released. */
    private Resource<A, R, T> resource;

    /** amount defines the amount to be released. */
    private double amount = 1.0;

    /**
     * Constructor for Release.
     * @param id Serializable; the id of the Station
     * @param simulator DEVSSimulatorInterface&lt;A,R,T&gt;; on which is scheduled
     * @param resource Resource&lt;A,R,T&gt;; which is released
     */
    public Release(final Serializable id, final DEVSSimulatorInterface<A, R, T> simulator, final Resource<A, R, T> resource)
    {
        this(id, simulator, resource, 1.0);
    }

    /**
     * Constructor for Release.
     * @param id Serializable; the id of the Station
     * @param simulator DEVSSimulatorInterface&lt;A,R,T&gt;; on which is scheduled
     * @param resource Resource&lt;A,R,T&gt;; which is released
     * @param amount double; of resource which is released
     */
    public Release(final Serializable id, final DEVSSimulatorInterface<A, R, T> simulator, final Resource<A, R, T> resource,
            final double amount)
    {
        super(id, simulator);
        this.resource = resource;
        this.amount = amount;
    }

    /** {@inheritDoc} */
    @Override
    public synchronized void receiveObject(final Object object)
    {
        super.receiveObject(object);
        try
        {
            this.resource.releaseCapacity(this.amount);
            this.releaseObject(object);
        }
        catch (Exception exception)
        {
            this.simulator.getLogger().always().warn(exception, "receiveObject");
        }
    }

    /***********************************************************************************************************/
    /************************************* EASY ACCESS CLASS EXTENSIONS ****************************************/
    /***********************************************************************************************************/

    /** Easy access class Release.TimeDouble. */
    public static class TimeDouble extends Release<Double, Double, SimTimeDouble> implements StationInterface.TimeDouble
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * Constructor for Release.
         * @param id Serializable; the id of the Station
         * @param simulator DEVSSimulatorInterface.TimeDouble; on which is scheduled
         * @param resource Resource&lt;Double,Double,SimTimeDouble&gt;; which is released
         */
        public TimeDouble(final Serializable id, final DEVSSimulatorInterface.TimeDouble simulator,
                final Resource<Double, Double, SimTimeDouble> resource)
        {
            super(id, simulator, resource);
        }

        /**
         * Constructor for Release.
         * @param id Serializable; the id of the Station
         * @param simulator DEVSSimulatorInterface.TimeDouble; on which is scheduled
         * @param resource Resource&lt;Double,Double,SimTimeDouble&gt;; which is released
         * @param amount double; of resource which is released
         */
        public TimeDouble(final Serializable id, final DEVSSimulatorInterface.TimeDouble simulator,
                final Resource<Double, Double, SimTimeDouble> resource, final double amount)
        {
            super(id, simulator, resource, amount);
        }
    }

    /** Easy access class Release.TimeFloat. */
    public static class TimeFloat extends Release<Float, Float, SimTimeFloat> implements StationInterface.TimeFloat
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * Constructor for Release.
         * @param id Serializable; the id of the Station
         * @param simulator DEVSSimulatorInterface.TimeFloat; on which is scheduled
         * @param resource Resource&lt;Float,Float,SimTimeFloat&gt;; which is released
         */
        public TimeFloat(final Serializable id, final DEVSSimulatorInterface.TimeFloat simulator,
                final Resource<Float, Float, SimTimeFloat> resource)
        {
            super(id, simulator, resource);
        }

        /**
         * Constructor for Release.
         * @param id Serializable; the id of the Station
         * @param simulator DEVSSimulatorInterface.TimeFloat; on which is scheduled
         * @param resource Resource&lt;Float,Float,SimTimeFloat&gt;; which is released
         * @param amount double; of resource which is released
         */
        public TimeFloat(final Serializable id, final DEVSSimulatorInterface.TimeFloat simulator,
                final Resource<Float, Float, SimTimeFloat> resource, final double amount)
        {
            super(id, simulator, resource, amount);
        }
    }

    /** Easy access class Release.TimeLong. */
    public static class TimeLong extends Release<Long, Long, SimTimeLong> implements StationInterface.TimeLong
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * Constructor for Release.
         * @param id Serializable; the id of the Station
         * @param simulator DEVSSimulatorInterface.TimeLong; on which is scheduled
         * @param resource Resource&lt;Long,Long,SimTimeLong&gt;; which is released
         */
        public TimeLong(final Serializable id, final DEVSSimulatorInterface.TimeLong simulator,
                final Resource<Long, Long, SimTimeLong> resource)
        {
            super(id, simulator, resource);
        }

        /**
         * Constructor for Release.
         * @param id Serializable; the id of the Station
         * @param simulator DEVSSimulatorInterface.TimeLong; on which is scheduled
         * @param resource Resource&lt;Long,Long,SimTimeLong&gt;; which is released
         * @param amount double; of resource which is released
         */
        public TimeLong(final Serializable id, final DEVSSimulatorInterface.TimeLong simulator,
                final Resource<Long, Long, SimTimeLong> resource, final double amount)
        {
            super(id, simulator, resource, amount);
        }
    }

    /** Easy access class Release.TimeDoubleUnit. */
    public static class TimeDoubleUnit extends Release<Time, Duration, SimTimeDoubleUnit>
            implements StationInterface.TimeDoubleUnit
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * Constructor for Release.
         * @param id Serializable; the id of the Station
         * @param simulator DEVSSimulatorInterface.TimeDoubleUnit; on which is scheduled
         * @param resource Resource&lt;Time,Duration,SimTimeDoubleUnit&gt;; which is released
         */
        public TimeDoubleUnit(final Serializable id, final DEVSSimulatorInterface.TimeDoubleUnit simulator,
                final Resource<Time, Duration, SimTimeDoubleUnit> resource)
        {
            super(id, simulator, resource);
        }

        /**
         * Constructor for Release.
         * @param id Serializable; the id of the Station
         * @param simulator DEVSSimulatorInterface.TimeDoubleUnit; on which is scheduled
         * @param resource Resource&lt;Time,Duration,SimTimeDoubleUnit&gt;; which is released
         * @param amount double; of resource which is released
         */
        public TimeDoubleUnit(final Serializable id, final DEVSSimulatorInterface.TimeDoubleUnit simulator,
                final Resource<Time, Duration, SimTimeDoubleUnit> resource, final double amount)
        {
            super(id, simulator, resource, amount);
        }
    }

    /** Easy access class Release.TimeFloatUnit. */
    public static class TimeFloatUnit extends Release<FloatTime, FloatDuration, SimTimeFloatUnit>
            implements StationInterface.TimeFloatUnit
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * Constructor for Release.
         * @param id Serializable; the id of the Station
         * @param simulator DEVSSimulatorInterface.TimeFloatUnit; on which is scheduled
         * @param resource Resource&lt;FloatTime,FloatDuration,SimTimeFloatUnit&gt;; which is released
         */
        public TimeFloatUnit(final Serializable id, final DEVSSimulatorInterface.TimeFloatUnit simulator,
                final Resource<FloatTime, FloatDuration, SimTimeFloatUnit> resource)
        {
            super(id, simulator, resource);
        }

        /**
         * Constructor for Release.
         * @param id Serializable; the id of the Station
         * @param simulator DEVSSimulatorInterface.TimeFloatUnit; on which is scheduled
         * @param resource Resource&lt;FloatTime,FloatDuration,SimTimeFloatUnit&gt;; which is released
         * @param amount double; of resource which is released
         */
        public TimeFloatUnit(final Serializable id, final DEVSSimulatorInterface.TimeFloatUnit simulator,
                final Resource<FloatTime, FloatDuration, SimTimeFloatUnit> resource, final double amount)
        {
            super(id, simulator, resource, amount);
        }
    }

}
