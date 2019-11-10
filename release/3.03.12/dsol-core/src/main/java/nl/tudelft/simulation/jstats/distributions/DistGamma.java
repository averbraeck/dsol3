package nl.tudelft.simulation.jstats.distributions;

import cern.jet.stat.Gamma;
import nl.tudelft.simulation.jstats.streams.StreamInterface;

/**
 * The Gamma distribution. For more information on this distribution see
 * <a href="http://mathworld.wolfram.com/GammaDistribution.html"> http://mathworld.wolfram.com/GammaDistribution.html </a>
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
public class DistGamma extends DistContinuous
{
    /** */
    private static final long serialVersionUID = 1L;

    /** alpha is the alpha parameter of the distribution. */
    private final double alpha;

    /** beta is the beta parameter of the distribution. */
    private final double beta;

    /**
     * constructs a new gamma distribution. The gamma distribution represents the time to complete some task, e.g. customer
     * service or machine repair
     * @param stream StreamInterface; the random number stream
     * @param alpha double; is the shape parameter alpha &gt; 0
     * @param beta double; is the scale parameter beta &gt; 0
     */
    public DistGamma(final StreamInterface stream, final double alpha, final double beta)
    {
        super(stream);
        if ((alpha > 0.0) && (beta > 0.0))
        {
            this.alpha = alpha;
            this.beta = beta;
        }
        else
        {
            throw new IllegalArgumentException("Error Gamma - alpha <= 0.0 or beta <= 0.0");
        }
    }

    /** {@inheritDoc} */
    @Override
    public double draw()
    {
        // according to Law and Kelton, Simulation Modeling and Analysis, 1991
        // pages 488-489
        if (this.alpha < 1.0)
        {
            double b = (Math.E + this.alpha) / Math.E;
            long counter = 0;
            while (counter < 1000)
            {
                // step 1.
                double p = b * this.stream.nextDouble();
                if (p <= 1.0d)
                {
                    // step 2.
                    double y = Math.pow(p, 1.0d / this.alpha);
                    double u2 = this.stream.nextDouble();
                    if (u2 <= Math.exp(-y))
                    {
                        return this.beta * y;
                    }
                }
                else
                {
                    // step 3.
                    double y = -Math.log((b - p) / this.alpha);
                    double u2 = this.stream.nextDouble();
                    if (u2 <= Math.pow(y, this.alpha - 1.0d))
                    {
                        return this.beta * y;
                    }
                }
            }
            new IllegalArgumentException("1000 tries for alpha<1.0");
            return 1.0d;
        }
        else if (this.alpha > 1.0)
        {
            // according to Law and Kelton, Simulation Modeling and
            // Analysis, 1991, pages 488-489
            double a = 1.0d / Math.sqrt(2.0d * this.alpha - 1.0d);
            double b = this.alpha - Math.log(4.0d);
            double q = this.alpha + (1.0d / a);
            double theta = 4.5d;
            double d = 1.0d + Math.log(theta);
            long counter = 0;
            while (counter < 1000)
            {
                // step 1.
                double u1 = this.stream.nextDouble();
                double u2 = this.stream.nextDouble();
                // step 2.
                double v = a * Math.log(u1 / (1.0d - u1));
                double y = this.alpha * Math.exp(v);
                double z = u1 * u1 * u2;
                double w = b + q * v - y;
                // step 3.
                if ((w + d - theta * z) >= 0.0d)
                {
                    return this.beta * y;
                }
                // step 4.
                if (w > Math.log(z))
                {
                    return this.beta * y;
                }
            }
            new IllegalArgumentException("1000 tries for alpha>1.0");
            return 1.0d;
        }
        else
        // alpha == 1.0
        {
            // Gamma(1.0, beta) ~ exponential with mean = beta
            return -this.beta * Math.log(this.stream.nextDouble());
        }
    }

    /** {@inheritDoc} */
    @Override
    public double probDensity(final double observation)
    {
        if (observation <= 0)
        {
            return 0.0;
        }
        return (Math.pow(this.beta, -this.alpha) * Math.pow(observation, this.alpha - 1)
                * Math.exp(-1 * observation / this.beta)) / Gamma.gamma(this.alpha);
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
        return "Gamma(" + this.alpha + "," + this.beta + ")";
    }
}
