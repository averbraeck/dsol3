package nl.tudelft.simulation.jstats.distributions;

import nl.tudelft.simulation.jstats.streams.StreamInterface;

/**
 * The Geometric distribution. For more information on this distribution see
 * <a href="https://mathworld.wolfram.com/GeometricDistribution.html"> https://mathworld.wolfram.com/GeometricDistribution.html
 * </a>
 * <p>
 * Copyright (c) 2002-2021 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class DistGeometric extends DistDiscrete
{
    /** */
    private static final long serialVersionUID = 1L;

    /** p is the p-value of the geometric distribution. */
    private final double p;

    /** lnp is a helper variable to avoid repetitive calculation. */
    private final double lnp;

    /**
     * constructs a new geometric distribution.
     * @param stream StreamInterface; the random number stream
     * @param p double; is the p-value
     */
    public DistGeometric(final StreamInterface stream, final double p)
    {
        super(stream);
        if ((p > 0.0) && (p < 1.0))
        {
            this.p = p;
        }
        else
        {
            throw new IllegalArgumentException("Error Geometric - p<=0 or p>=1");
        }
        this.lnp = Math.log(1.0 - this.p);
    }

    /** {@inheritDoc} */
    @Override
    public long draw()
    {
        double u = this.stream.nextDouble();
        return (long) (Math.floor(Math.log(u) / this.lnp));
    }

    /** {@inheritDoc} */
    @Override
    public double probability(final int observation)
    {
        if (observation >= 0)
        {
            return this.p * Math.pow(1 - this.p, observation);
        }
        return 0.0;
    }

    /**
     * @return p
     */
    public final double getP()
    {
        return this.p;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return "Geometric(" + this.p + ")";
    }
}
