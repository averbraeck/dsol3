package nl.tudelft.simulation.jstats.distributions;

import nl.tudelft.simulation.jstats.streams.StreamInterface;

/**
 * The Bernouilli distribution. For more information on this distribution see
 * <a href="https://mathworld.wolfram.com/BernouilliDistribution.html"> https://mathworld.wolfram.com/BernouilliDistribution.html
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
public class DistBernoulli extends DistDiscrete
{
    /** */
    private static final long serialVersionUID = 1L;

    /** p is the p-value of the Bernouilli distribution. */
    private final double p;

    /**
     * constructs a new Bernoulli distribution. Random occurrence with two possible outcomes; used to generate other discrete
     * random variates.
     * @param stream StreamInterface; is the stream
     * @param p double; the p-value of a Bernoulli distribution
     */
    public DistBernoulli(final StreamInterface stream, final double p)
    {
        super(stream);
        if ((p >= 0.0) && (p <= 1.0))
        {
            this.p = p;
        }
        else
        {
            throw new IllegalArgumentException("Error Exponential - p<0 or p>1 (p=" + p + ")");
        }
    }

    /**
     * draws the next value from the Bernoulli distribution. More information on this distribution can be found at <br>
     * <a href="https://mathworld.wolfram.com/BernoulliDistribution.html">https://mathworld.wolfram.com/
     * BernoulliDistribution.html</a>.
     * @return the next value {0,1}.
     */
    @Override
    public long draw()
    {
        if (this.stream.nextDouble() <= this.p)
        {
            return 1L;
        }
        return 0L;
    }

    /** {@inheritDoc} */
    @Override
    public double probability(final int observation)
    {
        if (observation == 0)
        {
            return 1 - this.p;
        }
        if (observation == 1)
        {
            return this.p;
        }
        return 0;
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
        return "Bernoulli(" + this.p + ")";
    }
}
