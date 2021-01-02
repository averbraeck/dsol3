package nl.tudelft.simulation.jstats.distributions;

import nl.tudelft.simulation.jstats.streams.MersenneTwister;
import nl.tudelft.simulation.jstats.streams.StreamInterface;

/**
 * The LogNormal distribution. For more information on this distribution see
 * <a href="http://mathworld.wolfram.com/LogNormalDistribution.html"> http://mathworld.wolfram.com/LogNormalDistribution.html
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
public class DistLogNormal extends DistNormal
{
    /** */
    private static final long serialVersionUID = 1L;

    /** the constant in the lognormal calculation: SQRT(2 * pi * sigma^2). */
    private final double c2pisigma2;

    /** the constant in the lognormal calculation: 2 * sigma^2. */
    private final double csigma2;

    /**
     * Construct a new Lognormal distribution.
     * @param stream StreamInterface; the random number stream
     * @param mu double; the medium
     * @param sigma double; the standard deviation
     */
    public DistLogNormal(final StreamInterface stream, final double mu, final double sigma)
    {
        super(stream, mu, sigma);
        this.csigma2 = 2.0 * this.sigma * this.sigma;
        this.c2pisigma2 = Math.sqrt(Math.PI * this.csigma2);
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
    public double getProbabilityDensity(final double x)
    {
        if (x > 0.0)
        {
            double xminmu = Math.log(x) - this.mu;
            return Math.exp(-1 * xminmu * xminmu / this.csigma2) / (x * this.c2pisigma2);
        }
        return 0.0;
    }

    /** {@inheritDoc} */
    @Override
    public double getCumulativeProbability(final double x)
    {
        if (x <= 0.0)
        {
            return 0.0;
        }
        return super.getCumulativeProbability(Math.log(x) / this.sigma);
    }
    
    /** {@inheritDoc} */
    @Override
    public double getInverseCumulativeProbability(final double cumulativeProbability)
    {
        return Math.exp(super.getInverseCumulativeProbability(cumulativeProbability));
    }
    
    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return "LogNormal(" + this.mu + "," + this.sigma + ")";
    }
    
    /**
     * Test of the lognormal distribution.
     * @param args empty
     */
    public static void main(final String[] args)
    {
        StreamInterface stream = new MersenneTwister(2);
        DistLogNormal ln51 = new DistLogNormal(stream, Math.log(5.0), Math.log(2.0));
        for (double x = 0.0; x <= 15.0; x += 0.01)
        {
            System.out.println(x + "\t" + ln51.getProbabilityDensity(x) + "\t" + ln51.getCumulativeProbability(x));
        }
        
        for (int i = 0; i < 100000; i++)
        {
            // System.out.println(ln51.draw());
        }
    }
}
