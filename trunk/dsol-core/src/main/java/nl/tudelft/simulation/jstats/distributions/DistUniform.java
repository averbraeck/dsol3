package nl.tudelft.simulation.jstats.distributions;

import nl.tudelft.simulation.jstats.streams.StreamInterface;

/**
 * The Uniform distribution. For more information on this distribution see
 * <a href="http://mathworld.wolfram.com/UniformDistribution.html"> http://mathworld.wolfram.com/UniformDistribution.html </a>
 * <p>
 * Copyright (c) 2002-2019 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class DistUniform extends DistContinuous
{
    /** */
    private static final long serialVersionUID = 1L;

    /** a is the minimum. */
    private double a;

    /** b is the maximum. */
    private double b;

    /**
     * constructs a new uniform distribution. a and b are real numbers with a less than b. a is a location parameter, b-a is a
     * scale parameter.
     * @param stream StreamInterface; the random number stream
     * @param a double; the minimum value
     * @param b double; the maximum value
     */
    public DistUniform(final StreamInterface stream, final double a, final double b)
    {
        super(stream);
        this.a = a;
        if (b > a)
        {
            this.b = b;
        }
        else
        {
            throw new IllegalArgumentException("Error Uniform - a >= b");
        }
    }

    /** {@inheritDoc} */
    @Override
    public double draw()
    {
        return this.a + (this.b - this.a) * this.stream.nextDouble();
    }

    /** {@inheritDoc} */
    @Override
    public double probDensity(final double observation)
    {
        if (observation >= this.a && observation <= this.b)
        {
            return 1.0 / (this.b - this.a);
        }
        return 0.0;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return "Uniform(" + this.a + "," + this.b + ")";
    }
}
