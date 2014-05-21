/*
 * @(#)DistUniform.java Apr 3, 2003 Copyright (c) 2002-2005 Delft University of
 * Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved.
 * This software is proprietary information of Delft University of Technology
 * 
 */
package nl.tudelft.simulation.jstats.distributions;

import nl.tudelft.simulation.jstats.streams.StreamInterface;

/**
 * The Uniform distribution. For more information on this distribution see <a
 * href="http://mathworld.wolfram.com/UniformDistribution.html"> http://mathworld.wolfram.com/UniformDistribution.html
 * </a>
 * <p>
 * (c) copyright 2002-2004 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl"> www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="mailto:a.verbraeck@tudelft.nl"> Alexander Verbraeck </a> <br>
 *         <a href="http://www.peter-jacobs.com/index.htm"> Peter Jacobs </a>
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:38:39 $
 * @since 1.5
 */
public class DistUniform extends DistContinuous
{
    /** a is the minimum */
    private double a;

    /** b is the maximum */
    private double b;

    /**
     * constructs a new uniform distribution. a and b are real numbers with a less than b. a is a location parameter,
     * b-a is a scale parameter.
     * @param stream the numberstream
     * @param a the minimum value
     * @param b the maximum value
     */
    public DistUniform(final StreamInterface stream, final double a, final double b)
    {
        super(stream);
        this.a = a;
        if (b > a)
        {
            this.b = b;
        }
        else
        {
            throw new IllegalArgumentException("Error Uniform - a >= b");
        }
    }

    /**
     * @see DistContinuous#draw()
     */
    @Override
    public double draw()
    {
        return this.a + (this.b - this.a) * this.stream.nextDouble();
    }

    /**
     * @see nl.tudelft.simulation.jstats.distributions.DistContinuous #probDensity(double)
     */
    @Override
    public double probDensity(final double observation)
    {
        if (observation >= this.a && observation <= this.b)
        {
            return 1.0 / (this.b - this.a);
        }
        return 0.0;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return "Uniform(" + this.a + "," + this.b + ")";
    }
}