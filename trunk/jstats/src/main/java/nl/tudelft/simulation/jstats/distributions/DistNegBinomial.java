/*
 * @(#)DistNegBinomial.java Apr 3, 2003 Copyright (c) 2002-2005 Delft University
 * of Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. This software is proprietary information of Delft University of
 * Technology 
 */
package nl.tudelft.simulation.jstats.distributions;

import nl.tudelft.simulation.jstats.math.ProbMath;
import nl.tudelft.simulation.jstats.streams.StreamInterface;

/**
 * The NegBinomial distribution. For more information on this distribution see
 * <a href="http://mathworld.wolfram.com/NegativeBinomialDistribution.html">
 * http://mathworld.wolfram.com/NegativeBinomialDistribution.html </a>
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
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:38:39 $
 * @since 1.5
 */
public class DistNegBinomial extends DistDiscrete
{
    /** n independent geometric trials with probability p */
    private long n;

    /** p is the propbability */
    private double p;

    /** lnp is a helper variable to avoid repetitive calculation */
    private double lnp;

    /**
     * constructs a new negative benomial distribution
     * 
     * @param stream the numberstream
     * @param n reflect the independent geometric trials with probability p
     * @param p is the propbability
     */
    public DistNegBinomial(final StreamInterface stream, final long n,
            final double p)
    {
        super(stream);
        if ((n > 0) && (p > 0) && (p < 1))
        {
            this.n = n;
            this.p = p;
        } else
        {
            throw new IllegalArgumentException(
                    "Error NegBinomial - n<=0 or p<=0.0 or p>=1.0");
        }
        this.lnp = Math.log(1.0 - this.p);
    }

    /**
     * @see DistDiscrete#draw()
     */
    @Override
    public long draw()
    {
        long x = 0;
        for (long i = 0; i < this.n; i++)
        {
            double u = this.stream.nextDouble();
            x = x + (long) (Math.floor(Math.log(u) / this.lnp));
        }
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
            return ProbMath.permutations((int) this.n + observation - 1,
                    observation)
                    * Math.pow(this.p, this.n)
                    * Math.pow(1 - this.p, observation);
        }
        return 0.0;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return "NegBinomial(" + this.n + "," + this.p + ")";
    }
}