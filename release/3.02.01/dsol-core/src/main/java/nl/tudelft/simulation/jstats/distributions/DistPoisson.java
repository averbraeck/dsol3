package nl.tudelft.simulation.jstats.distributions;

import nl.tudelft.simulation.jstats.math.ProbMath;
import nl.tudelft.simulation.jstats.streams.StreamInterface;

/**
 * The Poisson distribution. For more information on this distribution see
 * <a href="http://mathworld.wolfram.com/PoissonDistribution.html">
 * http://mathworld.wolfram.com/PoissonDistribution.html </a>
 * <p>
 * copyright (c) 2002-2018  <a href="https://simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="https://simulation.tudelft.nl"> www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="mailto:a.verbraeck@tudelft.nl"> Alexander Verbraeck </a> <br>
 *         <a href="https://www.linkedin.com/in/peterhmjacobs"> Peter Jacobs </a>
 * @version 1.8 2004-03-22
 * @since 1.5
 */
public class DistPoisson extends DistDiscrete
{
    /** lambda is the lambda parameter. */
    private double lambda;

    /** expl is a helper variable. */
    private double expl;

    /**
     * constructs a new poisson distribution.
     * @param stream the numberstream
     * @param lambda the lambda parameter
     */
    public DistPoisson(final StreamInterface stream, final double lambda)
    {
        super(stream);
        if (lambda > 0.0)
        {
            this.lambda = lambda;
        }
        else
        {
            throw new IllegalArgumentException("Error Poisson - lambda<=0");
        }
        this.expl = Math.exp(-this.lambda);
    }

    /** {@inheritDoc} */
    @Override
    public long draw()
    {
        // Adapted from Fortran program in Shannon, Systems Simulation, 1975,
        // p. 359
        double s = 1.0;
        long x = -1;
        do
        {
            s = s * this.stream.nextDouble();
            x++;
        }
        while (s > this.expl);
        return x;
    }

    /** {@inheritDoc} */
    @Override
    public double probability(final int observation)
    {
        if (observation >= 0)
        {
            return (Math.exp(-this.lambda) * Math.pow(this.lambda, observation)) / ProbMath.faculty(observation);
        }
        return 0;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return "Poisson(" + this.lambda + ")";
    }
}