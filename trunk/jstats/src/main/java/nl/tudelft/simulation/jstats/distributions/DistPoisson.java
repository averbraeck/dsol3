/*
 * @(#)DistPoisson.java Apr 3, 2003 Copyright (c) 2002-2005 Delft University of
 * Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved.
 * This software is proprietary information of Delft University of Technology
 * 
 */
package nl.tudelft.simulation.jstats.distributions;

import nl.tudelft.simulation.jstats.math.ProbMath;
import nl.tudelft.simulation.jstats.streams.StreamInterface;

/**
 * The Poisson distribution. For more information on this distribution see <a
 * href="http://mathworld.wolfram.com/PoissonDistribution.html">
 * http://mathworld.wolfram.com/PoissonDistribution.html </a>
 * <p>
 * (c) copyright 2002-2004 <a href="http://www.simulation.tudelft.nl">Delft
 * University of Technology </a>, the Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">
 * www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser
 * General Public License (LGPL) </a>, no warranty.
 * 
 * @author <a href="mailto:a.verbraeck@tudelft.nl">
 *         Alexander Verbraeck </a> <br>
 *         <a href="http://www.peter-jacobs.com/index.htm"> Peter Jacobs </a>
 * @version 1.8 2004-03-22
 * @since 1.5
 */
public class DistPoisson extends DistDiscrete
{
    /** lambda is the lambda parameter */
    private double lambda;

    /** expl is a helper variable */
    private double expl;

    /**
     * constructs a new poisson distribution
     * 
     * @param stream the numberstream
     * @param lambda the lambda parameter
     */
    public DistPoisson(final StreamInterface stream, final double lambda)
    {
        super(stream);
        if (lambda > 0.0)
        {
            this.lambda = lambda;
        } else
        {
            throw new IllegalArgumentException("Error Poisson - lambda<=0");
        }
        this.expl = Math.exp(-this.lambda);
    }

    /**
     * @see DistDiscrete#draw()
     */
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
        } while (s > this.expl);
        return x;
    }

    /**
     * @see nl.tudelft.simulation.jstats.distributions.DistDiscrete
     *      #probability(int)
     */
    @Override
    public double probability(final int observation)
    {
        if (observation >= 0)
        {
            return (Math.exp(-this.lambda) * Math.pow(this.lambda, observation))
                    / ProbMath.faculty(observation);
        }
        return 0;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return "Poisson(" + this.lambda + ")";
    }
}