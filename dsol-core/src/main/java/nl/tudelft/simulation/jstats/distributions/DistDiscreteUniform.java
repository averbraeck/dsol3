package nl.tudelft.simulation.jstats.distributions;

import nl.tudelft.simulation.jstats.streams.StreamInterface;

/**
 * The discrete Uniform distribution. For more information on this distribution see
 * <a href="http://mathworld.wolfram.com/UnifomrDistribution.html">
 * http://mathworld.wolfram.com/UniformDistribution.html </a>
 * <p>
 * Copyright (c) 2002-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. See for project information <a href="https://simulation.tudelft.nl/" target="_blank">
 * https://simulation.tudelft.nl</a>. The DSOL project is distributed under a three-clause BSD-style license, which can
 * be found at <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class DistDiscreteUniform extends DistDiscrete
{
    /** */
    private static final long serialVersionUID = 1L;

    /** min is the minimum value of this distribution. */
    private long min;

    /** max is the maximum value of this distribution. */
    private long max;

    /**
     * constructs a new uniform distribution. Random occurence with several possible outcomes, each of which is equally
     * likely.
     * @param stream StreamInterface; the random number stream
     * @param min long; the minimal value
     * @param max long; the maximum value
     */
    public DistDiscreteUniform(final StreamInterface stream, final long min, final long max)
    {
        super(stream);
        this.min = min;
        if (max >= this.min)
        {
            this.max = max;
        }
        else
        {
            throw new IllegalArgumentException("Error Discrete Uniform - min >= max");
        }
    }

    /** {@inheritDoc} */
    @Override
    public long draw()
    {
        return this.stream.nextInt((int) this.min, (int) this.max);
    }

    /** {@inheritDoc} */
    @Override
    public double probability(final int observation)
    {
        if (observation > this.min && observation < this.max)
        {
            return 1 / ((double) this.max - this.min + 1);
        }
        return 0.0;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return "DiscreteUniform(" + this.min + "," + this.max + ")";
    }
}
