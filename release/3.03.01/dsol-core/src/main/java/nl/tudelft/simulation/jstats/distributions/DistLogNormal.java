package nl.tudelft.simulation.jstats.distributions;

import nl.tudelft.simulation.jstats.streams.StreamInterface;

/**
 * The LogNormal distribution. For more information on this distribution see
 * <a href="http://mathworld.wolfram.com/LogNormalDistribution.html">
 * http://mathworld.wolfram.com/LogNormalDistribution.html </a>
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
public class DistLogNormal extends DistNormal
{
    /** */
    private static final long serialVersionUID = 1L;

    /**
     * constructs a new Lognormal distribution.
     * @param stream StreamInterface; the random number stream
     * @param mu double; the medium
     * @param sigma double; the standard deviation
     */
    public DistLogNormal(final StreamInterface stream, final double mu, final double sigma)
    {
        super(stream);
        this.mu = mu;
        if (sigma > 0.0)
        {
            this.sigma = sigma;
        }
        else
        {
            throw new IllegalArgumentException("Error DistLogNormal - sigma<=0.0");
        }
    }

    /** {@inheritDoc} */
    @Override
    public double draw()
    {
        double y = this.mu + this.sigma * super.nextGaussian();
        return Math.exp(y);
    }

    /** {@inheritDoc} */
    @Override
    public double probDensity(final double observation)
    {
        if (observation > 0)
        {
            return 1 / (observation * Math.sqrt(2 * Math.PI * Math.pow(this.sigma, 2)))
                    * Math.exp(-1 * Math.pow(Math.log(observation) - this.mu, 2) / (2 * Math.pow(this.sigma, 2)));
        }
        return 0.0;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return "LogNormal(" + this.mu + "," + this.sigma + ")";
    }
}
