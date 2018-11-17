package nl.tudelft.simulation.jstats.distributions.unit;

import org.djunits.unit.AbsoluteLinearUnit;
import org.djunits.unit.Unit;
import org.djunits.value.vdouble.scalar.AbstractDoubleScalar;
import org.djunits.value.vdouble.scalar.DoubleScalar;

import nl.tudelft.simulation.jstats.distributions.Dist;
import nl.tudelft.simulation.jstats.distributions.DistContinuous;

/**
 * DistContinuousUnit is the abstract class defining a distribution for a scalar with a unit. <br>
 * <br>
 * Copyright (c) 2003-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://www.simulation.tudelft.nl/" target="_blank">www.simulation.tudelft.nl</a>. The
 * source code and binary code of this software is proprietary information of Delft University of Technology.
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank">Alexander Verbraeck</a>
 * @param <U> the unit type for the values of the distribution
 * @param <S> the type of scalar to draw
 */
public abstract class DistContinuousUnit<U extends Unit<U>, S extends AbstractDoubleScalar<U, S>> extends Dist
{
    /** */
    private static final long serialVersionUID = 1L;

    /** the wrapped distribution. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected final DistContinuous wrappedDistribution;

    /** the unit for the values of the distribution. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected final U unit;

    /**
     * constructs a new continuous distribution.
     * @param wrappedDistribution DistContinuous; the wrapped continuous distribution
     * @param unit U; the unit for the values of the distribution
     */
    public DistContinuousUnit(final DistContinuous wrappedDistribution, final U unit)
    {
        super(wrappedDistribution.getStream());
        this.wrappedDistribution = wrappedDistribution;
        this.unit = unit;
    }

    /**
     * draws the next stream value according to the probability of this this distribution.
     * @return the next double value drawn.
     */
    public abstract S draw();

    /**
     * returns the probability density value of an observation.
     * @param observation double; the observation.
     * @return double the probability density.
     */
    public final double probDensity(final double observation)
    {
        return this.wrappedDistribution.probDensity(observation);
    }

    /**
     * Distribution of a generic absolute DoubleScalar.
     * @param <AU> Absolute unit
     * @param <RU> Relative unit belonging to the absolute unit
     */
    public static class Abs<AU extends AbsoluteLinearUnit<AU, RU>, RU extends Unit<RU>>
            extends DistContinuousUnit<AU, DoubleScalar.Abs<AU, RU>>
    {
        /** */
        private static final long serialVersionUID = 1L;

        /**
         * Constructs a new continuous distribution that draws generic absolute DoubleScalars.
         * @param wrappedDistribution DistContinuous; the wrapped continuous distribution
         * @param unit AU; the unit for the values of the distribution
         */
        public Abs(final DistContinuous wrappedDistribution, final AU unit)
        {
            super(wrappedDistribution, unit);
        }

        /** {@inheritDoc} */
        @Override
        public DoubleScalar.Abs<AU, RU> draw()
        {
            return new DoubleScalar.Abs<AU, RU>(this.wrappedDistribution.draw(), this.unit);
        }
    }

    /**
     * Distribution of a generic absolute DoubleScalar.
     * @param <RU> Relative unit for the DoubleScalar
     */
    public static class Rel<RU extends Unit<RU>> extends DistContinuousUnit<RU, DoubleScalar.Rel<RU>>
    {
        /** */
        private static final long serialVersionUID = 1L;

        /**
         * Constructs a new continuous distribution that draws generic relative DoubleScalars.
         * @param wrappedDistribution DistContinuous; the wrapped continuous distribution
         * @param unit RU; the unit for the values of the distribution
         */
        public Rel(final DistContinuous wrappedDistribution, final RU unit)
        {
            super(wrappedDistribution, unit);
        }

        /** {@inheritDoc} */
        @Override
        public DoubleScalar.Rel<RU> draw()
        {
            return new DoubleScalar.Rel<RU>(this.wrappedDistribution.draw(), this.unit);
        }
    }

}
