package nl.tudelft.simulation.jstats.distributions;

import org.djutils.exceptions.Throw;

import nl.tudelft.simulation.jstats.streams.StreamInterface;

/**
 * The Triangular distribution. For more information on this distribution see
 * <a href="http://mathworld.wolfram.com/TriangularDistribution.html"> http://mathworld.wolfram.com/TriangularDistribution.html
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
public class DistTriangular extends DistContinuous
{
    /** */
    private static final long serialVersionUID = 1L;

    /** the minimum. */
    private final double min;

    /** the mode. */
    private final double mode;

    /** the maximum. */
    private final double max;

    /**
     * constructs a new triangular distribution.
     * @param stream StreamInterface; the random number stream
     * @param min double; the minimum
     * @param mode double; the mode
     * @param max double; the maximum
     */
    public DistTriangular(final StreamInterface stream, final double min, final double mode, final double max)
    {
        super(stream);
        Throw.when(mode < min, IllegalArgumentException.class, "Triangular distribution, mode < min");
        Throw.when(mode > max, IllegalArgumentException.class, "Triangular distribution, mode > max");
        Throw.when(min == max, IllegalArgumentException.class, "Triangular distribution, min == max");
        this.min = min;
        this.mode = mode;
        this.max = max;
    }

    /** {@inheritDoc} */
    @Override
    public double draw()
    {
        double u = this.stream.nextDouble();
        if (u <= ((this.mode - this.min) / (this.max - this.min)))
        {
            return this.min + Math.sqrt((this.mode - this.min) * (this.max - this.min) * u);
        }
        return this.max - Math.sqrt((this.max - this.min) * (this.max - this.mode) * (1.0d - u));
    }

    /** {@inheritDoc} */
    @Override
    public double getProbabilityDensity(final double x)
    {
        if (x >= this.min && x <= this.mode)
        {
            return 2 * (x - this.min) / ((this.max - this.min) * (this.mode - this.min));
        }
        if (x >= this.mode && x <= this.max)
        {
            return 2 * (this.max - x) / ((this.max - this.min) * (this.max - this.mode));
        }
        return 0.0;
    }

    /**
     * @return min
     */
    public final double getMin()
    {
        return this.min;
    }

    /**
     * @return mode
     */
    public final double getMode()
    {
        return this.mode;
    }

    /**
     * @return max
     */
    public final double getMax()
    {
        return this.max;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return "Triangular(" + this.min + "," + this.mode + "," + this.max + ")";
    }
}
