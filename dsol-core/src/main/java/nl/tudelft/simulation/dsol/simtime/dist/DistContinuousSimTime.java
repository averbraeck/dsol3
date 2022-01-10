package nl.tudelft.simulation.dsol.simtime.dist;

import java.io.Serializable;

import org.djunits.unit.TimeUnit;
import org.djunits.value.vdouble.scalar.Duration;
import org.djunits.value.vdouble.scalar.Time;
import org.djunits.value.vfloat.scalar.FloatDuration;
import org.djunits.value.vfloat.scalar.FloatTime;

import nl.tudelft.simulation.dsol.simtime.SimTime;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simtime.SimTimeDoubleUnit;
import nl.tudelft.simulation.dsol.simtime.SimTimeFloat;
import nl.tudelft.simulation.dsol.simtime.SimTimeFloatUnit;
import nl.tudelft.simulation.dsol.simtime.SimTimeLong;
import nl.tudelft.simulation.jstats.distributions.Dist;
import nl.tudelft.simulation.jstats.distributions.DistContinuous;

/**
 * Definitions of distributions over SimTime. The distributions wrap a ContinuousDist from the
 * nl.tudelft.simulation.jstats.distributions package in dsol-core.
 * <p>
 * Copyright (c) 2016-2022 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @param <A> the absolute time type.
 * @param <R> the relative time type.
 * @param <T> the simulation time type.
 */
public abstract class DistContinuousSimTime<A extends Comparable<A> & Serializable, R extends Number & Comparable<R>,
        T extends SimTime<A, R, T>> extends Dist
{
    /** */
    private static final long serialVersionUID = 20140805L;

    /** the wrapped distribution. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    public DistContinuous wrappedDistribution;

    /**
     * constructs a new continuous distribution.
     * @param wrappedDistribution DistContinuous; the wrapped continuous distribution
     */
    public DistContinuousSimTime(final DistContinuous wrappedDistribution)
    {
        super(wrappedDistribution.getStream());
        this.wrappedDistribution = wrappedDistribution;
    }

    /**
     * draws the next stream value according to the probability of this this distribution.
     * @return the next double value drawn.
     */
    public abstract T draw();

    /**
     * returns the probability density for a value x.
     * @param x double; the value for which to calculate the probability density.
     * @return double; the probability density for value x
     */
    public double probDensity(final double x)
    {
        return this.wrappedDistribution.getProbabilityDensity(x);
    }

    /***********************************************************************************************************/
    /************************************* EASY ACCESS CLASS EXTENSIONS ****************************************/
    /***********************************************************************************************************/

    /** Easy access class DistContinuousSimTime.Double. */
    public static class TimeDouble extends DistContinuousSimTime<java.lang.Double, java.lang.Double, SimTimeDouble>
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /**
         * @param wrappedDistribution DistContinuous; the wrapped continuous distribution
         */
        public TimeDouble(final DistContinuous wrappedDistribution)
        {
            super(wrappedDistribution);
        }

        /** {@inheritDoc} */
        @Override
        public SimTimeDouble draw()
        {
            return new SimTimeDouble(super.wrappedDistribution.draw());
        }
    }

    /** Easy access class DistContinuousSimTime.Float. */
    public static class TimeFloat extends DistContinuousSimTime<java.lang.Float, java.lang.Float, SimTimeFloat>
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /**
         * @param wrappedDistribution DistContinuous; the wrapped continuous distribution
         */
        public TimeFloat(final DistContinuous wrappedDistribution)
        {
            super(wrappedDistribution);
        }

        /** {@inheritDoc} */
        @Override
        public SimTimeFloat draw()
        {
            return new SimTimeFloat((float) super.wrappedDistribution.draw());
        }
    }

    /** Easy access class DistContinuousSimTime.Long. */
    public static class TimeLong extends DistContinuousSimTime<java.lang.Long, java.lang.Long, SimTimeLong>
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /**
         * @param wrappedDistribution DistContinuous; the wrapped continuous distribution
         */
        public TimeLong(final DistContinuous wrappedDistribution)
        {
            super(wrappedDistribution);
        }

        /** {@inheritDoc} */
        @Override
        public SimTimeLong draw()
        {
            return new SimTimeLong((long) super.wrappedDistribution.draw());
        }
    }

    /** Easy access class DistContinuousSimTime.DoubleUnit. */
    public static class TimeDoubleUnit extends DistContinuousSimTime<Time, Duration, SimTimeDoubleUnit>
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /** the time unit. */
        private final TimeUnit unit;

        /**
         * @param wrappedDistribution DistContinuous; the wrapped continuous distribution
         * @param unit TimeUnit; the unit for the parameters (and drawn values) of the wrapped distribution
         */
        public TimeDoubleUnit(final DistContinuous wrappedDistribution, final TimeUnit unit)
        {
            super(wrappedDistribution);
            this.unit = unit;
        }

        /** {@inheritDoc} */
        @Override
        public SimTimeDoubleUnit draw()
        {
            return new SimTimeDoubleUnit(new Time(super.wrappedDistribution.draw(), this.unit));
        }
    }

    /** Easy access class DistContinuousSimTime.FloatUnit. */
    public static class TimeFloatUnit extends DistContinuousSimTime<FloatTime, FloatDuration, SimTimeFloatUnit>
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /** the time unit. */
        private final TimeUnit unit;

        /**
         * @param wrappedDistribution DistContinuous; the wrapped continuous distribution
         * @param unit TimeUnit; the unit for the parameters (and drawn values) of the wrapped distribution
         */
        public TimeFloatUnit(final DistContinuous wrappedDistribution, final TimeUnit unit)
        {
            super(wrappedDistribution);
            this.unit = unit;
        }

        /** {@inheritDoc} */
        @Override
        public SimTimeFloatUnit draw()
        {
            return new SimTimeFloatUnit(new FloatTime((float) super.wrappedDistribution.draw(), this.unit));
        }
    }

}
