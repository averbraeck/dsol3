package nl.tudelft.simulation.dsol.animation.interpolation;

import nl.tudelft.simulation.language.d3.DirectedPoint;

/**
 * A LinearInterpolation.
 * <p>
 * Copyright (c) 2002-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. See for project information <a href="https://simulation.tudelft.nl/" target="_blank">
 * https://simulation.tudelft.nl</a>. The DSOL project is distributed under a three-clause BSD-style license, which can
 * be found at <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 */
public class LinearInterpolation implements InterpolationInterface
{
    /** the start time. */
    protected double startTime = Double.NaN;

    /** the end time. */
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
     * constructs a new LinearInterpolation.
     * @param startTime double; the startingTime
     * @param endTime double; the endTime
     * @param origin DirectedPoint; the origin
     * @param destination DirectedPoint; the destination
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

    /** {@inheritDoc} */
    @Override
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
