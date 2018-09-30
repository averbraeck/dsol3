package nl.tudelft.simulation.jstats.distributions;

import nl.tudelft.simulation.jstats.math.ProbMath;
import nl.tudelft.simulation.jstats.streams.StreamInterface;

/**
 * The Binomial distribution. For more information on this distribution see
 * <a href="http://mathworld.wolfram.com/BinomialDistribution.html">
 * http://mathworld.wolfram.com/BinomialDistribution.html </a>
 * <p>
 * Copyright (c) 2002-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. See for project information <a href="https://simulation.tudelft.nl/">https://simulation.tudelft.nl</a>. The
 * DSOL project is distributed under a three-clause BSD-style license, which can be found at <a href=
 * "https://simulation.tudelft.nl/dsol/3.0/license.html">https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class DistBinomial extends DistDiscrete
{
    /** */
    private static final long serialVersionUID = 1L;

    /** n is the n-parameter of the Binomial distribution. */
    private long n;

    /** p is the p-value of the binomial distribution. */
    private double p;

    /**
     * constructs a Binomial distribution. Number of successes in t independent Bernoulli trials with probability p of
     * sucess on each trial.
     * @param stream the random number stream
     * @param n is the n-parameter of the Binomial distribution
     * @param p is the p-parameter of the Binomial distribution
     */
    public DistBinomial(final StreamInterface stream, final long n, final double p)
    {
        super(stream);
        if ((n > 0) && (p > 0) && (p < 1))
        {
            this.n = n;
            this.p = p;
        }
        else
        {
            throw new IllegalArgumentException("Error Binomial - n<=0 or p<=0.0 or p>=1.0");
        }
    }

    /** {@inheritDoc} */
    @Override
    public long draw()
    {
        long x = 0;
        for (long i = 0; i < this.n; i++)
        {
            if (this.stream.nextDouble() <= this.p)
            {
                x++;
            }
        }
        return x;
    }

    /** {@inheritDoc} */
    @Override
    public double probability(final int observation)
    {
        if (observation <= this.n && observation >= 0)
        {
            return ProbMath.permutations((int) this.n, observation) * Math.pow(this.p, observation)
                    * Math.pow(1 - this.p, this.n - observation);
        }
        return 0.0;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return "Binomial(" + this.n + "," + this.p + ")";
    }
}
