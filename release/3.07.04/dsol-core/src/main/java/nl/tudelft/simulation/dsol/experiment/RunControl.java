package nl.tudelft.simulation.dsol.experiment;

import java.io.Serializable;

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

/**
 * RunControl is a data object that contains off-line run control information. It can be fed to an Experiment or a Replication
 * to set the run control parameters for a simulation run.
 * <p>
 * Copyright (c) 2021-2021 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/dsol/manual/" target="_blank">DSOL Manual</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">DSOL License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @param <A> the absolute storage type for the simulation time, e.g. Time, Float, or Double.
 * @param <R> the relative type for time storage, e.g. Duration for absolute Time. For most non-unit types, the absolute and
 *            relative types are the same.
 * @param <T> the simulation time type to be able to implement a comparator on the simulation time.
 */
public class RunControl<A extends Comparable<A> & Serializable, R extends Number & Comparable<R>, T extends SimTime<A, R, T>>
        implements RunControlInterface<A, R, T>
{
    /** */
    private static final long serialVersionUID = 20210409L;

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

    /**
     * Construct an object with off-line run control information.
     * @param id String; the id of the run control that will be used as the id for the replication; should be unique within the
     *            experiment.
     * @param startTime T; the start time as a time object.
     * @param warmupPeriod R; the warmup period, included in the runlength (!)
     * @param runLength R; the total length of the run, including the warm-up period.
     * @throws NullPointerException when id, startTime, warmupPeriod or runLength is null
     * @throws SimRuntimeException when warmup period is negative, or run length is zero or negative, or when the warmup time is
     *             longer than or equal to the runlength
     */
    public RunControl(final String id, final T startTime, final R warmupPeriod, final R runLength)
    {
        Throw.whenNull(id, "id should not be null");
        Throw.whenNull(startTime, "startTime should not be null");
        Throw.whenNull(warmupPeriod, "warmupPeriod should not be null");
        Throw.whenNull(runLength, "runLength should not be null");
        Throw.when(warmupPeriod.doubleValue() < 0.0, SimRuntimeException.class, "warmup period should not be negative");
        Throw.when(runLength.doubleValue() <= 0.0, SimRuntimeException.class, "run length should not be zero or negative");
        Throw.when(warmupPeriod.doubleValue() >= runLength.doubleValue(), IllegalArgumentException.class,
                "the warmup time is longer than or equal to the runlength");

        this.id = id;
        this.description = id;
        this.startTime = startTime;
        this.endTime = startTime.plus(runLength);
        this.warmupTime = startTime.plus(warmupPeriod);
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
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.endTime.hashCode();
        result = prime * result + this.id.hashCode();
        result = prime * result + this.startTime.hashCode();
        result = prime * result + this.warmupTime.hashCode();
        return result;
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("checkstyle:needbraces")
    public boolean equals(final Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        RunControl<?, ?, ?> other = (RunControl<?, ?, ?>) obj;
        if (!this.startTime.equals(other.startTime))
            return false;
        if (!this.endTime.equals(other.endTime))
            return false;
        if (!this.id.equals(other.id))
            return false;
        if (!this.warmupTime.equals(other.warmupTime))
            return false;
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return "RunControl " + this.getId();
    }

    /***********************************************************************************************************/
    /************************************* EASY ACCESS CLASS EXTENSIONS ****************************************/
    /***********************************************************************************************************/

    /**
     * Easy access class Replication.TimeDouble.
     */
    public static class TimeDouble extends RunControl<Double, Double, SimTimeDouble> implements RunControlInterface.TimeDouble
    {
        /** */
        private static final long serialVersionUID = 20210404;

        /**
         * Construct an object with off-line run control information.
         * @param id String; the id of the run control that will be used as the id for the replication.
         * @param startTime double; the start time
         * @param warmupPeriod double; the warmup period, included in the runlength (!)
         * @param runLength double; the total length of the run, including the warm-up period.
         * @throws NullPointerException when id, startTime, warmupPeriod or runLength is null
         * @throws SimRuntimeException when warmup period is negative, or run length is zero or negative
         */
        public TimeDouble(final String id, final double startTime, final double warmupPeriod, final double runLength)
        {
            super(id, new SimTimeDouble(startTime), warmupPeriod, runLength);
        }
    }

    /**
     * Easy access class Replication.TimeFloat.
     */
    public static class TimeFloat extends RunControl<Float, Float, SimTimeFloat> implements RunControlInterface.TimeFloat
    {
        /** */
        private static final long serialVersionUID = 20210404;

        /**
         * Construct an object with off-line run control information.
         * @param id String; the id of the run control that will be used as the id for the replication.
         * @param startTime float; the start time
         * @param warmupPeriod float; the warmup period, included in the runlength (!)
         * @param runLength float; the total length of the run, including the warm-up period.
         * @throws NullPointerException when id, startTime, warmupPeriod or runLength is null
         * @throws SimRuntimeException when warmup period is negative, or run length is zero or negative
         */
        public TimeFloat(final String id, final float startTime, final float warmupPeriod, final float runLength)
        {
            super(id, new SimTimeFloat(startTime), warmupPeriod, runLength);
        }
    }

    /**
     * Easy access class Replication.TimeLong.
     */
    public static class TimeLong extends RunControl<Long, Long, SimTimeLong> implements RunControlInterface.TimeLong
    {
        /** */
        private static final long serialVersionUID = 20210404;

        /**
         * Construct an object with off-line run control information.
         * @param id String; the id of the run control that will be used as the id for the replication.
         * @param startTime long; the start time
         * @param warmupPeriod long; the warmup period, included in the runlength (!)
         * @param runLength long; the total length of the run, including the warm-up period.
         * @throws NullPointerException when id, startTime, warmupPeriod or runLength is null
         * @throws SimRuntimeException when warmup period is negative, or run length is zero or negative
         */
        public TimeLong(final String id, final long startTime, final long warmupPeriod, final long runLength)
        {
            super(id, new SimTimeLong(startTime), warmupPeriod, runLength);
        }
    }

    /**
     * Easy access class Replication.TimeDoubleUnit.
     */
    public static class TimeDoubleUnit extends RunControl<Time, Duration, SimTimeDoubleUnit>
            implements RunControlInterface.TimeDoubleUnit
    {
        /** */
        private static final long serialVersionUID = 20210404;

        /**
         * Construct an object with off-line run control information.
         * @param id String; the id of the run control that will be used as the id for the replication.
         * @param startTime Time; the start time
         * @param warmupPeriod Duration; the warmup period, included in the runlength (!)
         * @param runLength Duration; the total length of the run, including the warm-up period.
         * @throws NullPointerException when id, startTime, warmupPeriod or runLength is null
         * @throws SimRuntimeException when warmup period is negative, or run length is zero or negative
         */
        public TimeDoubleUnit(final String id, final Time startTime, final Duration warmupPeriod, final Duration runLength)
        {
            super(id, new SimTimeDoubleUnit(startTime), warmupPeriod, runLength);
        }
    }

    /**
     * Easy access class Replication.TimeDoubleUnit.
     */
    public static class TimeFloatUnit extends RunControl<FloatTime, FloatDuration, SimTimeFloatUnit>
            implements RunControlInterface.TimeFloatUnit
    {
        /** */
        private static final long serialVersionUID = 20210404;

        /**
         * Construct an object with off-line run control information.
         * @param id String; the id of the run control that will be used as the id for the replication.
         * @param startTime FloatTime; the start time
         * @param warmupPeriod FloatDuration; the warmup period, included in the runlength (!)
         * @param runLength FloatDuration; the total length of the run, including the warm-up period.
         * @throws NullPointerException when id, startTime, warmupPeriod or runLength is null
         * @throws SimRuntimeException when warmup period is negative, or run length is zero or negative
         */
        public TimeFloatUnit(final String id, final FloatTime startTime, final FloatDuration warmupPeriod,
                final FloatDuration runLength)
        {
            super(id, new SimTimeFloatUnit(startTime), warmupPeriod, runLength);
        }

    }

}
