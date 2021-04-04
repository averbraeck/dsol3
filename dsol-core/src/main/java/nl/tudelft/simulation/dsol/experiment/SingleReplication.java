package nl.tudelft.simulation.dsol.experiment;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

import javax.naming.NamingException;

import org.djunits.value.vdouble.scalar.Duration;
import org.djunits.value.vdouble.scalar.Time;
import org.djunits.value.vfloat.scalar.FloatDuration;
import org.djunits.value.vfloat.scalar.FloatTime;
import org.djutils.exceptions.Throw;

import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.simtime.SimTime;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simtime.SimTimeDoubleUnit;
import nl.tudelft.simulation.dsol.simtime.SimTimeFloat;
import nl.tudelft.simulation.dsol.simtime.SimTimeFloatUnit;
import nl.tudelft.simulation.dsol.simtime.SimTimeLong;
import nl.tudelft.simulation.jstats.streams.MersenneTwister;
import nl.tudelft.simulation.jstats.streams.StreamInterface;
import nl.tudelft.simulation.naming.context.ContextInterface;
import nl.tudelft.simulation.naming.context.event.InitialEventContext;
import nl.tudelft.simulation.naming.context.util.ContextUtil;

/**
 * A single replication of an Experiment.
 * <p>
 * Copyright (c) 2002-2021 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @param <A> the absolute storage type for the simulation time, e.g. Time, Float, or Double.
 * @param <R> the relative type for time storage, e.g. Duration for absolute Time. For most non-unit types, the absolute and
 *            relative types are the same.
 * @param <T> the extended type itself to be able to implement a comparator on the simulation time.
 */
public class SingleReplication<A extends Comparable<A> & Serializable, R extends Number & Comparable<R>,
        T extends SimTime<A, R, T>> implements ReplicationInterface<A, R, T>
{
    /** The default serial version UID for serializable classes. */
    private static final long serialVersionUID = 20210404L;

    /** the id of the replication. */
    private final String id;

    /** the description of the replication (if not set, the id will be used). */
    private String description;

    /** the start time of the simulation. */
    private final T startTime;

    /** the end time of the simulation. */
    private final T endTime;

    /** the warmup time of the simulation (included in the total run length). */
    private final T warmupTime;

    /** the contextRoot of this replication. */
    private ContextInterface context;

    /** streams used in the replication. */
    private final Map<String, StreamInterface> streams = new HashMap<String, StreamInterface>();

    /**
     * construct a stand-alone replication.
     * @param id String; the id of the replication; should be unique within the experiment.
     * @param startTime T; the start time as a time object.
     * @param warmupPeriod R; the warmup period, included in the runlength (!)
     * @param runLength R; the total length of the run, including the warm-up period.
     * @throws NullPointerException when id, startTime, warmupPeriod or runLength is null
     * @throws SimRuntimeException when warmup period is negative, or run length is zero or negative, or when a context for the
     *             replication cannot be created
     */
    public SingleReplication(final String id, final T startTime, final R warmupPeriod, final R runLength)
    {
        Throw.whenNull(id, "id should not be null");
        Throw.whenNull(startTime, "startTime should not be null");
        Throw.whenNull(warmupPeriod, "warmupPeriod should not be null");
        Throw.whenNull(runLength, "runLength should not be null");
        Throw.when(warmupPeriod.doubleValue() < 0.0, SimRuntimeException.class, "warmup period should not be negative");
        Throw.when(runLength.doubleValue() <= 0.0, SimRuntimeException.class, "run length should not be zero or negative");

        this.id = id;
        this.description = id;
        this.startTime = startTime;
        this.endTime = startTime.plus(runLength);
        this.warmupTime = startTime.plus(warmupPeriod);
        this.streams.put("default", new MersenneTwister(10L));
        setContext();
    }

    /**
     * Set the context for this replication.
     * @throws SimRuntimeException in case a context for the replication cannot be created
     */
    private void setContext()
    {
        try
        {
            ContextInterface rootContext = InitialEventContext.instantiate("root");
            this.context = ContextUtil.lookupOrCreateSubContext(rootContext, String.valueOf(this.id.hashCode()));
        }
        catch (RemoteException | NamingException exception)
        {
            throw new SimRuntimeException(
                    "Cannot lookup or create context for experiment. Error is: " + exception.getMessage());
        }
    }

    /** {@inheritDoc} */
    @Override
    public final ContextInterface getContext()
    {
        return this.context;
    }

    /** {@inheritDoc} */
    @Override
    public String getId()
    {
        return this.id;
    }

    /** {@inheritDoc} */
    @Override
    public void setDescription(final String description)
    {
        this.description = description;
    }

    /** {@inheritDoc} */
    @Override
    public String getDescription()
    {
        return this.description;
    }

    /** {@inheritDoc} */
    @Override
    public Map<String, StreamInterface> getStreams()
    {
        synchronized (this.streams)
        {
            return new HashMap<>(this.streams);
        }
    }

    /** {@inheritDoc} */
    @Override
    public StreamInterface getStream(final String streamId)
    {
        synchronized (this.streams)
        {
            return this.streams.get(streamId);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void setStreams(final Map<String, StreamInterface> streams)
    {
        synchronized (this.streams)
        {
            this.streams.clear();
            this.streams.putAll(streams);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void reset()
    {
        synchronized (this.streams)
        {
            for (StreamInterface stream : getStreams().values())
            {
                stream.reset();
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    public T getStartSimTime()
    {
        return this.startTime;
    }

    /** {@inheritDoc} */
    @Override
    public T getEndSimTime()
    {
        return this.endTime;
    }

    /** {@inheritDoc} */
    @Override
    public T getWarmupSimTime()
    {
        return this.warmupTime;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return "Replication " + this.getDescription();
    }

    /***********************************************************************************************************/
    /************************************* EASY ACCESS CLASS EXTENSIONS ****************************************/
    /***********************************************************************************************************/

    /**
     * Easy access class Replication.TimeDouble.
     */
    public static class TimeDouble extends SingleReplication<Double, Double, SimTimeDouble>
            implements ReplicationInterface.TimeDouble
    {
        /** */
        private static final long serialVersionUID = 20210404;

        /**
         * Construct a stand-alone Replication.
         * @param id String; the id of the replication.
         * @param startTime double; the start time
         * @param warmupPeriod double; the warmup period, included in the runlength (!)
         * @param runLength double; the total length of the run, including the warm-up period.
         * @throws NamingException in case a context for the replication cannot be created
         */
        public TimeDouble(final String id, final double startTime, final double warmupPeriod, final double runLength)
                throws NamingException
        {
            super(id, new SimTimeDouble(startTime), warmupPeriod, runLength);
        }
    }

    /**
     * Easy access class Replication.TimeFloat.
     */
    public static class TimeFloat extends SingleReplication<Float, Float, SimTimeFloat>
            implements ReplicationInterface.TimeFloat
    {
        /** */
        private static final long serialVersionUID = 20210404;

        /**
         * Construct a stand-alone Replication.
         * @param id String; the id of the replication.
         * @param startTime float; the start time
         * @param warmupPeriod float; the warmup period, included in the runlength (!)
         * @param runLength float; the total length of the run, including the warm-up period.
         * @throws NamingException in case a context for the replication cannot be created
         */
        public TimeFloat(final String id, final float startTime, final float warmupPeriod, final float runLength)
                throws NamingException
        {
            super(id, new SimTimeFloat(startTime), warmupPeriod, runLength);
        }
    }

    /**
     * Easy access class Replication.TimeLong.
     */
    public static class TimeLong extends SingleReplication<Long, Long, SimTimeLong> implements ReplicationInterface.TimeLong
    {
        /** */
        private static final long serialVersionUID = 20210404;

        /**
         * Construct a stand-alone Replication.
         * @param id String; the id of the replication.
         * @param startTime long; the start time
         * @param warmupPeriod long; the warmup period, included in the runlength (!)
         * @param runLength long; the total length of the run, including the warm-up period.
         * @throws NamingException in case a context for the replication cannot be created
         */
        public TimeLong(final String id, final long startTime, final long warmupPeriod, final long runLength)
                throws NamingException
        {
            super(id, new SimTimeLong(startTime), warmupPeriod, runLength);
        }
    }

    /**
     * Easy access class Replication.TimeDoubleUnit.
     */
    public static class TimeDoubleUnit extends SingleReplication<Time, Duration, SimTimeDoubleUnit>
            implements ReplicationInterface.TimeDoubleUnit
    {
        /** */
        private static final long serialVersionUID = 20210404;

        /**
         * Construct a stand-alone Replication.
         * @param id String; the id of the replication.
         * @param startTime Time; the start time
         * @param warmupPeriod Duration; the warmup period, included in the runlength (!)
         * @param runLength Duration; the total length of the run, including the warm-up period.
         * @throws NamingException in case a context for the replication cannot be created
         */
        public TimeDoubleUnit(final String id, final Time startTime, final Duration warmupPeriod, final Duration runLength)
                throws NamingException
        {
            super(id, new SimTimeDoubleUnit(startTime), warmupPeriod, runLength);
        }
    }

    /**
     * Easy access class Replication.TimeDoubleUnit.
     */
    public static class TimeFloatUnit extends SingleReplication<FloatTime, FloatDuration, SimTimeFloatUnit>
            implements ReplicationInterface.TimeFloatUnit
    {
        /** */
        private static final long serialVersionUID = 20210404;

        /**
         * Construct a stand-alone Replication.
         * @param id String; the id of the replication.
         * @param startTime FloatTime; the start time
         * @param warmupPeriod FloatDuration; the warmup period, included in the runlength (!)
         * @param runLength FloatDuration; the total length of the run, including the warm-up period.
         * @throws NamingException in case a context for the replication cannot be created
         */
        public TimeFloatUnit(final String id, final FloatTime startTime, final FloatDuration warmupPeriod,
                final FloatDuration runLength) throws NamingException
        {
            super(id, new SimTimeFloatUnit(startTime), warmupPeriod, runLength);
        }

    }

}
