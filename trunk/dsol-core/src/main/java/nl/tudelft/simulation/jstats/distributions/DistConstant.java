/*
 * @(#)DistConstant.java Apr 3, 2003 Copyright (c) 2002-2005 Delft University of
 * Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved.
 * This software is proprietary information of Delft University of Technology
 * 
 */
package nl.tudelft.simulation.jstats.distributions;

import nl.tudelft.simulation.jstats.streams.StreamInterface;

/**
 * The Constant distribution. For more information on this distribution see <a
 * href="http://mathworld.wolfram.com/ContinuousDistribution.html">
 * http://mathworld.wolfram.com/ContinuousDistribution.html </a>
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
public class DistConstant extends DistContinuous
{
    /** value is the value of the constant distribution */
    private double value;

    /**
     * constructs a new constant distribution
     * @param stream the numberstream
     * @param value the value
     */
    public DistConstant(final StreamInterface stream, final double value)
    {
        super(stream);
        this.value = value;
    }

    /**
     * @see DistContinuous#draw()
     */
    @Override
    public double draw()
    {
        this.stream.nextDouble();
        return this.value;
    }

    /**
     * @see nl.tudelft.simulation.jstats.distributions.DistContinuous #probDensity(double)
     */
    @Override
    public double probDensity(final double observation)
    {
        if (observation == this.value)
        {
            return 1.0;
        }
        return 0.0;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return "Constant(" + this.value + ")";
    }
}