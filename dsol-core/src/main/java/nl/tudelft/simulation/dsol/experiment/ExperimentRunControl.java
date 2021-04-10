package nl.tudelft.simulation.dsol.experiment;

import java.io.Serializable;

import org.djunits.value.vdouble.scalar.Duration;
import org.djunits.value.vdouble.scalar.Time;
import org.djunits.value.vfloat.scalar.FloatDuration;
import org.djunits.value.vfloat.scalar.FloatTime;
import org.djutils.exceptions.Throw;

import nl.tudelft.simulation.dsol.simtime.SimTime;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simtime.SimTimeDoubleUnit;
import nl.tudelft.simulation.dsol.simtime.SimTimeFloat;
import nl.tudelft.simulation.dsol.simtime.SimTimeFloatUnit;
import nl.tudelft.simulation.dsol.simtime.SimTimeLong;

/**
 * ExperimentRunControl.java.
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
public class ExperimentRunControl<A extends Comparable<A> & Serializable, R extends Number & Comparable<R>,
        T extends SimTime<A, R, T>> extends RunControl<A, R, T>
{
    /** */
    private static final long serialVersionUID = 20210410L;

    /** The number of replications to execute. */
    private final int numberOfReplications;

    /**
     * Construct an object with off-line run control information for an experiment.
     * @param id String; the id of the run control that will be used as the id for the replication; should be unique within the
     *            experiment.
     * @param startTime T; the start time as a time object.
     * @param warmupPeriod R; the warmup period, included in the runlength (!)
     * @param runLength R; the total length of the run, including the warm-up period.
     * @param numberOfReplications int; the number of replications to execute
     * @throws NullPointerException when id, startTime, warmupPeriod or runLength is null
     * @throws IllegalArgumentException when warmup period is negative, or run length is zero or negative, or when the warmup
     *             time is longer than or equal to the runlength, or when number of replications is zero or negative
     */
    public ExperimentRunControl(final String id, final T startTime, final R warmupPeriod, final R runLength,
            final int numberOfReplications)
    {
        super(id, startTime, warmupPeriod, runLength);
        Throw.when(numberOfReplications <= 0, IllegalArgumentException.class,
                "number of replications can not be zero or negative");
        this.numberOfReplications = numberOfReplications;
    }

    /**
     * Return the total number of replications to execute.
     * @return int; the total number of replications to execute
     */
    public final int getNumberOfReplications()
    {
        return this.numberOfReplications;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + this.numberOfReplications;
        return result;
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("checkstyle:needbraces")
    public boolean equals(final Object obj)
    {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        ExperimentRunControl<?, ?, ?> other = (ExperimentRunControl<?, ?, ?>) obj;
        if (this.numberOfReplications != other.numberOfReplications)
            return false;
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return "ExperimentRunControl " + this.getId() + ", NumberOfReplications=" + this.numberOfReplications;
    }

    /***********************************************************************************************************/
    /************************************* EASY ACCESS CLASS EXTENSIONS ****************************************/
    /***********************************************************************************************************/

    /**
     * Easy access class ExperimentRunControl.TimeDouble.
     */
    public static class TimeDouble extends ExperimentRunControl<Double, Double, SimTimeDouble>
    {
        /** */
        private static final long serialVersionUID = 20210404;

        /**
         * Construct an object with off-line run control information.
         * @param id String; the id of the run control that will be used as the id for the replication.
         * @param startTime double; the start time
         * @param warmupPeriod double; the warmup period, included in the runlength (!)
         * @param runLength double; the total length of the run, including the warm-up period.
         * @param numberOfReplications int; the number of replications to execute
         * @throws NullPointerException when id, startTime, warmupPeriod or runLength is null
         * @throws IllegalArgumentException when warmup period is negative, or run length is zero or negative, or when number of
         *             replications is zero or negative
         */
        public TimeDouble(final String id, final double startTime, final double warmupPeriod, final double runLength,
                final int numberOfReplications)
        {
            super(id, new SimTimeDouble(startTime), warmupPeriod, runLength, numberOfReplications);
        }
    }

    /**
     * Easy access class ExperimentRunControl.TimeFloat.
     */
    public static class TimeFloat extends ExperimentRunControl<Float, Float, SimTimeFloat>
    {
        /** */
        private static final long serialVersionUID = 20210404;

        /**
         * Construct an object with off-line run control information.
         * @param id String; the id of the run control that will be used as the id for the replication.
         * @param startTime float; the start time
         * @param warmupPeriod float; the warmup period, included in the runlength (!)
         * @param runLength float; the total length of the run, including the warm-up period.
         * @param numberOfReplications int; the number of replications to execute
         * @throws NullPointerException when id, startTime, warmupPeriod or runLength is null
         * @throws IllegalArgumentException when warmup period is negative, or run length is zero or negative, or when number of
         *             replications is zero or negative
         */
        public TimeFloat(final String id, final float startTime, final float warmupPeriod, final float runLength,
                final int numberOfReplications)
        {
            super(id, new SimTimeFloat(startTime), warmupPeriod, runLength, numberOfReplications);
        }
    }

    /**
     * Easy access class ExperimentRunControl.TimeLong.
     */
    public static class TimeLong extends ExperimentRunControl<Long, Long, SimTimeLong>
    {
        /** */
        private static final long serialVersionUID = 20210404;

        /**
         * Construct an object with off-line run control information.
         * @param id String; the id of the run control that will be used as the id for the replication.
         * @param startTime long; the start time
         * @param warmupPeriod long; the warmup period, included in the runlength (!)
         * @param runLength long; the total length of the run, including the warm-up period.
         * @param numberOfReplications int; the number of replications to execute
         * @throws NullPointerException when id, startTime, warmupPeriod or runLength is null
         * @throws IllegalArgumentException when warmup period is negative, or run length is zero or negative, or when number of
         *             replications is zero or negative
         */
        public TimeLong(final String id, final long startTime, final long warmupPeriod, final long runLength,
                final int numberOfReplications)
        {
            super(id, new SimTimeLong(startTime), warmupPeriod, runLength, numberOfReplications);
        }
    }

    /**
     * Easy access class ExperimentRunControl.TimeDoubleUnit.
     */
    public static class TimeDoubleUnit extends ExperimentRunControl<Time, Duration, SimTimeDoubleUnit>
    {
        /** */
        private static final long serialVersionUID = 20210404;

        /**
         * Construct an object with off-line run control information.
         * @param id String; the id of the run control that will be used as the id for the replication.
         * @param startTime Time; the start time
         * @param warmupPeriod Duration; the warmup period, included in the runlength (!)
         * @param runLength Duration; the total length of the run, including the warm-up period.
         * @param numberOfReplications int; the number of replications to execute
         * @throws NullPointerException when id, startTime, warmupPeriod or runLength is null
         * @throws IllegalArgumentException when warmup period is negative, or run length is zero or negative, or when number of
         *             replications is zero or negative
         */
        public TimeDoubleUnit(final String id, final Time startTime, final Duration warmupPeriod, final Duration runLength,
                final int numberOfReplications)
        {
            super(id, new SimTimeDoubleUnit(startTime), warmupPeriod, runLength, numberOfReplications);
        }
    }

    /**
     * Easy access class ExperimentRunControl.TimeDoubleUnit.
     */
    public static class TimeFloatUnit extends ExperimentRunControl<FloatTime, FloatDuration, SimTimeFloatUnit>
    {
        /** */
        private static final long serialVersionUID = 20210404;

        /**
         * Construct an object with off-line run control information.
         * @param id String; the id of the run control that will be used as the id for the replication.
         * @param startTime FloatTime; the start time
         * @param warmupPeriod FloatDuration; the warmup period, included in the runlength (!)
         * @param runLength FloatDuration; the total length of the run, including the warm-up period.
         * @param numberOfReplications int; the number of replications to execute
         * @throws NullPointerException when id, startTime, warmupPeriod or runLength is null
         * @throws IllegalArgumentException when warmup period is negative, or run length is zero or negative, or when number of
         *             replications is zero or negative
         */
        public TimeFloatUnit(final String id, final FloatTime startTime, final FloatDuration warmupPeriod,
                final FloatDuration runLength, final int numberOfReplications)
        {
            super(id, new SimTimeFloatUnit(startTime), warmupPeriod, runLength, numberOfReplications);
        }

    }

}
