package nl.tudelft.simulation.jstats.distributions;

import nl.tudelft.simulation.jstats.math.ProbMath;
import nl.tudelft.simulation.jstats.streams.StreamInterface;

/**
 * The NegBinomial distribution. For more information on this distribution see
 * <a href="http://mathworld.wolfram.com/NegativeBinomialDistribution.html">
 * http://mathworld.wolfram.com/NegativeBinomialDistribution.html </a>
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
public class DistNegBinomial extends DistDiscrete
{
    /** */
    private static final long serialVersionUID = 1L;

    /** n independent geometric trials with probability p. */
    private long n;

    /** p is the probability. */
    private double p;

    /** lnp is a helper variable to avoid repetitive calculation. */
    private double lnp;

    /**
     * constructs a new negative binomial distribution.
     * @param stream StreamInterface; the random number stream
     * @param n long; reflect the independent geometric trials with probability p
     * @param p double; is the probability
     */
    public DistNegBinomial(final StreamInterface stream, final long n, final double p)
    {
        super(stream);
        if ((n > 0) && (p > 0) && (p < 1))
        {
            this.n = n;
            this.p = p;
        }
        else
        {
            throw new IllegalArgumentException("Error NegBinomial - n<=0 or p<=0.0 or p>=1.0");
        }
        this.lnp = Math.log(1.0 - this.p);
    }

    /** {@inheritDoc} */
    @Override
    public long draw()
    {
        long x = 0;
        for (long i = 0; i < this.n; i++)
        {
            double u = this.stream.nextDouble();
            x = x + (long) (Math.floor(Math.log(u) / this.lnp));
        }
        return x;
    }

    /** {@inheritDoc} */
    @Override
    public double probability(final int observation)
    {
        if (observation >= 0)
        {
            return ProbMath.permutations((int) this.n + observation - 1, observation) * Math.pow(this.p, this.n)
                    * Math.pow(1 - this.p, observation);
        }
        return 0.0;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return "NegBinomial(" + this.n + "," + this.p + ")";
    }
}
