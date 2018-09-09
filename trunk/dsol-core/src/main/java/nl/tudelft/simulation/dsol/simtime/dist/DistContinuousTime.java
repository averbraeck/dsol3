package nl.tudelft.simulation.dsol.simtime.dist;

import org.djunits.unit.DurationUnit;
import org.djunits.value.vdouble.scalar.Duration;
import org.djunits.value.vfloat.scalar.FloatDuration;

import nl.tudelft.simulation.jstats.distributions.Dist;
import nl.tudelft.simulation.jstats.distributions.DistContinuous;

/**
 * <p>
 * Copyright (c) 2002-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved.
 * <p>
 * See for project information <a href="http://www.simulation.tudelft.nl/"> www.simulation.tudelft.nl</a>.
 * <p>
 * The DSOL project is distributed under the following BSD-style license:<br>
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
 * following conditions are met:
 * <ul>
 * <li>Redistributions of source code must retain the above copyright notice, this list of conditions and the following
 * disclaimer.</li>
 * <li>Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the
 * following disclaimer in the documentation and/or other materials provided with the distribution.</li>
 * <li>Neither the name of Delft University of Technology, nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.</li>
 * </ul>
 * This software is provided by the copyright holders and contributors "as is" and any express or implied warranties,
 * including, but not limited to, the implied warranties of merchantability and fitness for a particular purpose are
 * disclaimed. In no event shall the copyright holder or contributors be liable for any direct, indirect, incidental,
 * special, exemplary, or consequential damages (including, but not limited to, procurement of substitute goods or
 * services; loss of use, data, or profits; or business interruption) however caused and on any theory of liability,
 * whether in contract, strict liability, or tort (including negligence or otherwise) arising in any way out of the use
 * of this software, even if advised of the possibility of such damage.
 * @version Aug 5, 2014 <br>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @param <R> the relative time type.
 */
public abstract class DistContinuousTime<R extends Number & Comparable<R>> extends Dist
{
    /** */
    private static final long serialVersionUID = 20140805L;

    /** the wrapped distribution. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected final DistContinuous wrappedDistribution;

    /**
     * constructs a new continuous distribution.
     * @param wrappedDistribution the wrapped continuous distribution
     */
    public DistContinuousTime(final DistContinuous wrappedDistribution)
    {
        super(wrappedDistribution.getStream());
        this.wrappedDistribution = wrappedDistribution;
    }

    /**
     * draws the next stream value according to the probability of this this distribution.
     * @return the next double value drawn.
     */
    public abstract R draw();

    /**
     * returns the probability density value of an observation.
     * @param observation the observation.
     * @return double the probability density.
     */
    public final double probDensity(final double observation)
    {
        return this.wrappedDistribution.probDensity(observation);
    }

    /***********************************************************************************************************/
    /************************************* EASY ACCESS CLASS EXTENSIONS ****************************************/
    /***********************************************************************************************************/

    /** Easy access class DistContinuousTime.Double. */
    public static class TimeDouble extends DistContinuousTime<java.lang.Double>
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /**
         * @param wrappedDistribution the wrapped continuous distribution
         */
        public TimeDouble(final DistContinuous wrappedDistribution)
        {
            super(wrappedDistribution);
        }

        /** {@inheritDoc} */
        @Override
        public final Double draw()
        {
            return super.wrappedDistribution.draw();
        }
    }

    /** Easy access class DistContinuousTime.Float. */
    public static class TimeFloat extends DistContinuousTime<java.lang.Float>
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /**
         * @param wrappedDistribution the wrapped continuous distribution
         */
        public TimeFloat(final DistContinuous wrappedDistribution)
        {
            super(wrappedDistribution);
        }

        /** {@inheritDoc} */
        @Override
        public final Float draw()
        {
            return (float) super.wrappedDistribution.draw();
        }
    }

    /** Easy access class DistContinuousTime.Long. */
    public static class TimeLong extends DistContinuousTime<java.lang.Long>
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /**
         * @param wrappedDistribution the wrapped continuous distribution
         */
        public TimeLong(final DistContinuous wrappedDistribution)
        {
            super(wrappedDistribution);
        }

        /** {@inheritDoc} */
        @Override
        public final Long draw()
        {
            return (long) super.wrappedDistribution.draw();
        }
    }

    /** Easy access class DistContinuousTime.DoubleUnit. */
    public static class TimeDoubleUnit extends DistContinuousTime<Duration>
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /** the time unit. */
        private final DurationUnit unit;

        /**
         * @param wrappedDistribution the wrapped continuous distribution
         * @param unit the unit for the parameters (and drawn values) of the wrapped distribution
         */
        public TimeDoubleUnit(final DistContinuous wrappedDistribution, final DurationUnit unit)
        {
            super(wrappedDistribution);
            this.unit = unit;
        }

        /** {@inheritDoc} */
        @Override
        public final Duration draw()
        {
            return new Duration(super.wrappedDistribution.draw(), this.unit);
        }
    }

    /** Easy access class DistContinuousTime.FloatUnit. */
    public static class TimeFloatUnit extends DistContinuousTime<FloatDuration>
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /** the time unit. */
        private final DurationUnit unit;

        /**
         * @param wrappedDistribution the wrapped continuous distribution
         * @param unit the unit for the parameters (and drawn values) of the wrapped distribution
         */
        public TimeFloatUnit(final DistContinuous wrappedDistribution, final DurationUnit unit)
        {
            super(wrappedDistribution);
            this.unit = unit;
        }

        /** {@inheritDoc} */
        @Override
        public final FloatDuration draw()
        {
            return new FloatDuration((float) super.wrappedDistribution.draw(), this.unit);
        }
    }

    /** Easy access class DistContinuousTime.CalendarDouble. */
    public static class CalendarDouble extends DistContinuousTime<Duration>
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /** the time unit. */
        private final DurationUnit unit;

        /**
         * @param wrappedDistribution the wrapped continuous distribution
         * @param unit the unit for the parameters (and drawn values) of the wrapped distribution
         */
        public CalendarDouble(final DistContinuous wrappedDistribution, final DurationUnit unit)
        {
            super(wrappedDistribution);
            this.unit = unit;
        }

        /** {@inheritDoc} */
        @Override
        public final Duration draw()
        {
            return new Duration(super.wrappedDistribution.draw(), this.unit);
        }
    }

    /** Easy access class DistContinuousTime.CalendarFloat. */
    public static class CalendarFloat extends DistContinuousTime<FloatDuration>
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /** the time unit. */
        private final DurationUnit unit;

        /**
         * @param wrappedDistribution the wrapped continuous distribution
         * @param unit the unit for the parameters (and drawn values) of the wrapped distribution
         */
        public CalendarFloat(final DistContinuous wrappedDistribution, final DurationUnit unit)
        {
            super(wrappedDistribution);
            this.unit = unit;
        }

        /** {@inheritDoc} */
        @Override
        public final FloatDuration draw()
        {
            return new FloatDuration((float) super.wrappedDistribution.draw(), this.unit);
        }
    }

    /** Easy access class DistContinuousTime.CalendarLong. */
    public static class CalendarLong extends DistContinuousTime<Long>
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /**
         * @param wrappedDistribution the wrapped continuous distribution
         */
        public CalendarLong(final DistContinuous wrappedDistribution)
        {
            super(wrappedDistribution);
        }

        /** {@inheritDoc} */
        @Override
        public final Long draw()
        {
            return (long) super.wrappedDistribution.draw();
        }
    }

}
