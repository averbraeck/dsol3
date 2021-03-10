package nl.tudelft.simulation.jstats.distributions;

import nl.tudelft.simulation.jstats.streams.StreamInterface;

/**
 * The Weibull distribution. For more information on this distribution see
 * <a href="https://mathworld.wolfram.com/WeibullDistribution.html"> https://mathworld.wolfram.com/WeibullDistribution.html </a>
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
public class DistWeibull extends DistContinuous
{
    /** */
    private static final long serialVersionUID = 1L;

    /** alpha is the alpha parameter. */
    private final double alpha;

    /** beta is the beta parameter. */
    private final double beta;

    /**
     * constructs a new Weibull distribution.
     * @param stream StreamInterface; the random number stream
     * @param alpha double; (shape)
     * @param beta double; (scale)
     */
    public DistWeibull(final StreamInterface stream, final double alpha, final double beta)
    {
        super(stream);
        if ((alpha > 0.0) && (beta > 0.0))
        {
            this.alpha = alpha;
            this.beta = beta;
        }
        else
        {
            throw new IllegalArgumentException("Error Weibull - alpha <= 0.0 or beta <= 0.0");
        }
    }

    /** {@inheritDoc} */
    @Override
    public double draw()
    {
        return this.beta * Math.pow(-Math.log(this.stream.nextDouble()), 1.0d / this.alpha);
    }

    /** {@inheritDoc} */
    @Override
    public double getProbabilityDensity(final double x)
    {
        if (x > 0)
        {
            return this.alpha * Math.pow(this.beta, -this.alpha) * Math.pow(x, this.alpha - 1)
                    * Math.exp(Math.pow(-1 * (x / this.beta), this.alpha));
        }
        return 0.0;
    }

    /**
     * @return alpha
     */
    public final double getAlpha()
    {
        return this.alpha;
    }

    /**
     * @return beta
     */
    public final double getBeta()
    {
        return this.beta;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return "Weibull(" + this.alpha + "," + this.beta + ")";
    }
}
