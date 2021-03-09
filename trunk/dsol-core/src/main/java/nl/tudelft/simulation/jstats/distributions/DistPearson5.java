package nl.tudelft.simulation.jstats.distributions;

import cern.jet.stat.Gamma;
import nl.tudelft.simulation.jstats.streams.StreamInterface;

/**
 * The Pearson5 distribution. For more information on this distribution see
 * <a href="https://mathworld.wolfram.com/Pearson5Distribution.html"> https://mathworld.wolfram.com/Pearson5Distribution.html </a>
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
public class DistPearson5 extends DistContinuous
{
    /** */
    private static final long serialVersionUID = 1L;

    /** dist is the gamma distribution. */
    private final DistGamma dist;

    /** alpha is the alpha parameter of the distribution. */
    private final double alpha;

    /** beta is the beta parameter of the distribution. */
    private final double beta;

    /**
     * constructs a new Pearson5 distribution.
     * @param stream StreamInterface; the random number stream
     * @param alpha double; the scale parameter
     * @param beta double; the shape parameter
     */
    public DistPearson5(final StreamInterface stream, final double alpha, final double beta)
    {
        super(stream);
        if ((alpha > 0.0) && (beta > 0.0))
        {
            this.alpha = alpha;
            this.beta = beta;
        }
        else
        {
            throw new IllegalArgumentException("Error alpha <= 0.0 or beta <= 0.0");
        }
        this.dist = new DistGamma(stream, this.alpha, 1.0d / this.beta);
    }

    /** {@inheritDoc} */
    @Override
    public double draw()
    {
        // according to Law and Kelton, Simulation Modeling and Analysis, 1991
        // pages 492-493
        return 1.0d / this.dist.draw();
    }

    /** {@inheritDoc} */
    @Override
    public double getProbabilityDensity(final double x)
    {
        if (x > 0)
        {
            return (Math.pow(x, -1 * (this.alpha + 1)) * Math.exp(-this.beta / x))
                    / (Math.pow(this.beta, -this.alpha) * Gamma.gamma(this.alpha));
        }
        return 0;
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
        return "Pearson5(" + this.alpha + "," + this.beta + ")";
    }
}
