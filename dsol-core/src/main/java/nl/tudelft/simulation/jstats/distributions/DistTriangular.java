package nl.tudelft.simulation.jstats.distributions;

import nl.tudelft.simulation.jstats.streams.StreamInterface;

/**
 * The Triangular distribution. For more information on this distribution see
 * <a href="http://mathworld.wolfram.com/TriangularDistribution.html">
 * http://mathworld.wolfram.com/TriangularDistribution.html </a>
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
public class DistTriangular extends DistContinuous
{
    /** */
    private static final long serialVersionUID = 1L;

    /** a is the minimum. */
    private double a;

    /** b is the mode. */
    private double b;

    /** c is the maximum. */
    private double c;

    /**
     * constructs a new triangular distribution.
     * @param stream StreamInterface; the random number stream
     * @param a double; the minimum
     * @param b double; the mode
     * @param c double; the maximum
     */
    public DistTriangular(final StreamInterface stream, final double a, final double b, final double c)
    {
        super(stream);
        if ((a < b) && (b < c))
        {
            this.a = a;
            this.b = b;
            this.c = c;
        }
        else
        {
            throw new IllegalArgumentException("Error condition for tria: a<b<c");
        }
    }

    /** {@inheritDoc} */
    @Override
    public double draw()
    {
        double u = this.stream.nextDouble();
        if (u <= ((this.b - this.a) / (this.c - this.a)))
        {
            return this.a + Math.sqrt((this.b - this.a) * (this.c - this.a) * u);
        }
        return this.c - Math.sqrt((this.c - this.a) * (this.c - this.b) * (1.0d - u));
    }

    /** {@inheritDoc} */
    @Override
    public double probDensity(final double observation)
    {
        if (observation >= this.a && observation <= this.b)
        {
            return 2 * (observation - this.a) / ((this.c - this.a) * (this.b - this.a));
        }
        if (observation >= this.b && observation <= this.c)
        {
            return 2 * (this.c - observation) / ((this.c - this.a) * (this.c - this.b));
        }
        return 0.0;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return "Triangular(" + this.a + "," + this.b + "," + this.c + ")";
    }
}
