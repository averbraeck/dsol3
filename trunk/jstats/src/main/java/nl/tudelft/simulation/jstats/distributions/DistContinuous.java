/*
 * @(#)DistContinuous.java Apr 3, 2003 Copyright (c) 2002-2005 Delft University
 * of Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. This software is proprietary information of Delft University of
 * Technology 
 */
package nl.tudelft.simulation.jstats.distributions;

import nl.tudelft.simulation.jstats.streams.StreamInterface;

/**
 * The Continuous distribution. For more information on this distribution see <a
 * href="http://mathworld.wolfram.com/ContinuousDistribution.html">
 * http://mathworld.wolfram.com/ContinuousDistribution.html </a>
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
public abstract class DistContinuous extends Dist
{
    /**
     * constructs a new continuous distribution
     * 
     * @param stream the stream
     */
    public DistContinuous(final StreamInterface stream)
    {
        super(stream);
    }

    /**
     * draws the next stream value according to the probability of this this
     * distribution.
     * 
     * @return the next double value drawn.
     */
    public abstract double draw();

    /**
     * returns the propbability density value of an observation
     * 
     * @param observation the observation.
     * @return double the probability density.
     */
    public abstract double probDensity(final double observation);
}