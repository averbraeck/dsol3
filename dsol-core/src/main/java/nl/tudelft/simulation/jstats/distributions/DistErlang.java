package nl.tudelft.simulation.jstats.distributions;

import nl.tudelft.simulation.jstats.math.ProbMath;
import nl.tudelft.simulation.jstats.streams.StreamInterface;

/**
 * The Erlang distribution. For more information on this distribution see
 * <a href="http://mathworld.wolfram.com/ErlangDistribution.html"> http://mathworld.wolfram.com/ErlangDistribution.html </a>
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
public class DistErlang extends DistContinuous
{
    /** */
    private static final long serialVersionUID = 1L;

    /** k is the k-value of the Erlang distribution. */
    private final int k;

    /** lambda is the rate of change value of the Erlang distribution. */
    private final double lambda;

    /** lambdak is the mean value of the Erlang distribution. */
    private final double lambdak;

    /** distGamma is the underlying gamma distribution. */
    private final DistGamma distGamma;

    /** GAMMABORDER is the borber above which we use a gamma function and below repeated drawing. */
    private static final short GAMMABORDER = 10;

    /**
     * Construct a new Erlang distribution.
     * @param stream StreamInterface; the random number stream
     * @param k int; the k-parameter
     * @param lambda double; the rate of change parameter
     */
    public DistErlang(final StreamInterface stream, final int k, final double lambda)
    {
        super(stream);
        if ((k > 0) && (lambda > 0.0))
        {
            this.k = k;
            this.lambda = lambda;
        }
        else
        {
            throw new IllegalArgumentException("Error Erlang - k <= 0 or beta <= 0");
        }
        if (this.k <= DistErlang.GAMMABORDER)
        {
            this.lambdak = -this.lambda / this.k;
            this.distGamma = null;
        }
        else
        {
            this.distGamma = new DistGamma(stream, this.k, this.lambda);
            this.lambdak = Double.NaN;
        }
    }

    /** {@inheritDoc} */
    @Override
    public double draw()
    {
        if (this.k <= DistErlang.GAMMABORDER)
        {
            // according to Law and Kelton, Simulation Modeling and Analysis
            // repeated drawing and composition is usually faster for k<=10
            double product = 1.0;
            for (int i = 1; i <= this.k; i++)
            {
                product = product * this.stream.nextDouble();
            }
            return this.lambdak * Math.log(product);
        }
        // and using the gamma distribution is faster for k>10
        return this.distGamma.draw();
    }

    /** {@inheritDoc} */
    @Override
    public double getProbabilityDensity(final double x)
    {
        if (x < 0)
        {
            return 0;
        }
        return this.lambda * Math.exp(-this.lambda * x) * Math.pow(this.lambda * x, this.k - 1) / ProbMath.faculty(this.k - 1);
    }

    /**
     * @return k
     */
    public final int getK()
    {
        return this.k;
    }

    /**
     * @return beta
     */
    public final double getLambda()
    {
        return this.lambda;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return "Erlang(" + this.k + "," + this.lambda + ")";
    }
}
