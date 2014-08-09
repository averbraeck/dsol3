/*
 * @(#) LinearInterpolation.java Mar 4, 2004 Copyright (c) 2002-2005 Delft
 * University of Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All
 * rights reserved. This software is proprietary information of Delft University
 * of Technology 
 */
package nl.tudelft.simulation.dsol.animation.interpolation;

import nl.tudelft.simulation.language.d3.DirectedPoint;

/**
 * A LinearInterpolation <br>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @version $Revision: 1.1 $ $Date: 2010/08/10 11:37:25 $
 * @author <a href="http://www.peter-jacobs.com">Peter Jacobs </a>
 */
public class LinearInterpolation implements InterpolationInterface
{
    /** the start time */
    protected double startTime = Double.NaN;

    /** the end time */
    protected double endTime = Double.NaN;

    /**
     * the origin
     */
    private DirectedPoint origin = null;

    /**
     * the destination
     */
    private DirectedPoint destination = null;

    /**
     * constructs a new LinearInterpolation
     * @param startTime the startingTime
     * @param endTime the endTime
     * @param origin the origin
     * @param destination the destination
     */
    public LinearInterpolation(final double startTime, final double endTime, final DirectedPoint origin,
            final DirectedPoint destination)
    {
        super();
        if (endTime < startTime)
        {
            throw new IllegalArgumentException("endTime < startTime");
        }
        this.startTime = startTime;
        this.endTime = endTime;
        this.origin = (DirectedPoint) origin.clone();
        this.destination = (DirectedPoint) destination.clone();
    }

    /**
     * @see nl.tudelft.simulation.dsol.animation.interpolation.InterpolationInterface #getLocation(double)
     */
    public DirectedPoint getLocation(final double time)
    {
        if (time <= this.startTime)
        {
            return this.origin;
        }
        if (time >= this.endTime)
        {
            return this.destination;
        }
        double fraction = (time - this.startTime) / (this.endTime - this.startTime);
        double x = this.origin.x + (this.destination.x - this.origin.x) * fraction;
        double y = this.origin.y + (this.destination.y - this.origin.y) * fraction;
        double z = this.origin.z + (this.destination.z - this.origin.z) * fraction;
        double rotY = this.origin.getRotY() + (this.destination.getRotY() - this.origin.getRotY()) * fraction;
        double rotZ = this.origin.getRotZ() + (this.destination.getRotZ() - this.origin.getRotZ()) * fraction;
        double rotX = this.origin.getRotX() + (this.destination.getRotX() - this.origin.getRotX()) * fraction;
        return new DirectedPoint(x, y, z, rotX, rotY, rotZ);
    }
}