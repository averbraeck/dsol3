package nl.tudelft.simulation.jstats.distributions.unit;

import org.djunits.unit.Unit;
import org.djunits.value.vdouble.scalar.base.AbstractDoubleScalar;
import org.djunits.value.vdouble.scalar.base.DoubleScalar;

import nl.tudelft.simulation.jstats.distributions.Dist;
import nl.tudelft.simulation.jstats.distributions.DistContinuous;

/**
 * DistContinuousUnit is the abstract class defining a distribution for a scalar with a unit. <br>
 * <br>
 * Copyright (c) 2003-2021 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://www.simulation.tudelft.nl/" target="_blank">www.simulation.tudelft.nl</a>. The
 * source code and binary code of this software is proprietary information of Delft University of Technology.
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank">Alexander Verbraeck</a>
 * @param <U> the unit type for the values of the distribution
 * @param <S> the type of scalar to draw
 */
public class DistContinuousUnit<U extends Unit<U>, S extends AbstractDoubleScalar<U, S>> extends Dist
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
    public S draw()
    {
        return DoubleScalar.instantiate(this.wrappedDistribution.draw(), this.unit);
    }

    /**
     * returns the probability density for a value x.
     * @param x double; the value for which to calculate the probability density.
     * @return double; the probability density for value x
     */
    public final double probDensity(final double x)
    {
        return this.wrappedDistribution.getProbabilityDensity(x);
    }
}
