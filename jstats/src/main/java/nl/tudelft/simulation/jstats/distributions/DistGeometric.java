/*
 * @(#)DistGeometric.java Apr 3, 2003 Copyright (c) 2002-2005 Delft University
 * of Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. This software is proprietary information of Delft University of
 * Technology 
 */
package nl.tudelft.simulation.jstats.distributions;

import nl.tudelft.simulation.jstats.streams.StreamInterface;

/**
 * The Geometric distribution. For more information on this distribution see <a
 * href="http://mathworld.wolfram.com/GeometricDistribution.html">
 * http://mathworld.wolfram.com/GeometricDistribution.html </a>
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
public class DistGeometric extends DistDiscrete
{
    /** p is the p-value of the geometric distribution */
    private double p;

    /** lnp is a helper variable to avoid repetitive calculation */
    private double lnp;

    /**
     * constructs a new geometric distribution
     * 
     * @param stream the numberstream
     * @param p is the p-value
     */
    public DistGeometric(final StreamInterface stream, final double p)
    {
        super(stream);
        if ((p > 0.0) && (p < 1.0))
        {
            this.p = p;
        } else
        {
            throw new IllegalArgumentException("Error Geometric - p<=0 or p>=1");
        }
        this.lnp = Math.log(1.0 - this.p);
    }

    /**
     * @see DistDiscrete#draw()
     */
    @Override
    public long draw()
    {
        double u = this.stream.nextDouble();
        return (long) (Math.floor(Math.log(u) / this.lnp));
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
            return this.p * Math.pow(1 - this.p, observation);
        }
        return 0.0;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return "Geometric(" + this.p + ")";
    }
}