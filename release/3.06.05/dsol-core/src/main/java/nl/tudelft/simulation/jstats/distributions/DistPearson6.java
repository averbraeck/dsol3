package nl.tudelft.simulation.jstats.distributions;

import cern.jet.stat.Gamma;
import nl.tudelft.simulation.jstats.streams.StreamInterface;

/**
 * The Pearson6 distribution. For more information on this distribution see
 * <a href="https://mathworld.wolfram.com/Pearson6Distribution.html"> https://mathworld.wolfram.com/Pearson6Distribution.html </a>
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
public class DistPearson6 extends DistContinuous
{
    /** */
    private static final long serialVersionUID = 1L;

    /** dist1 is the first gamma distribution. */
    private final DistGamma dist1;

    /** dist2 is the second gamma distribution. */
    private final DistGamma dist2;

    /** alpha1 is the first shape parameter. */
    private final double alpha1;

    /** alpha2 is the second shape parameter. */
    private final double alpha2;

    /** beta is the scale parameter. */
    private final double beta;

    /**
     * constructs a new Pearson6 distribution.
     * @param stream StreamInterface; the random number stream
     * @param alpha1 double; the first shape parameter
     * @param alpha2 double; the second shape parameter
     * @param beta double; the scale parameter
     */
    public DistPearson6(final StreamInterface stream, final double alpha1, final double alpha2, final double beta)
    {
        super(stream);
        if ((alpha1 > 0.0) && (alpha2 > 0.0) && (beta > 0.0))
        {
            this.alpha1 = alpha1;
            this.alpha2 = alpha2;
            this.beta = beta;
        }
        else
        {
            throw new IllegalArgumentException("Error alpha1 <= 0.0 or alpha2 <= 0.0 or beta <= 0.0");
        }
        this.dist1 = new DistGamma(super.stream, this.alpha1, this.beta);
        this.dist2 = new DistGamma(super.stream, this.alpha2, this.beta);
    }

    /** {@inheritDoc} */
    @Override
    public double draw()
    {
        // according to Law and Kelton, Simulation Modeling and Analysis, 1991, page 494
        return this.dist1.draw() / this.dist2.draw();
    }

    /** {@inheritDoc} */
    @Override
    public double getProbabilityDensity(final double x)
    {
        if (x > 0)
        {
            return Math.pow(x / this.beta, this.alpha1 - 1) / (this.beta * Gamma.beta(this.alpha1, this.alpha2)
                    * Math.pow(1 + (x / this.beta), (this.alpha1 + this.alpha2)));
        }
        return 0;
    }

    /**
     * @return alpha1
     */
    public final double getAlpha1()
    {
        return this.alpha1;
    }

    /**
     * @return alpha2
     */
    public final double getAlpha2()
    {
        return this.alpha2;
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
        return "Pesrson6(" + this.alpha1 + "," + this.alpha2 + "," + this.beta + ")";
    }
}
