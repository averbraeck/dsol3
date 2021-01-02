package nl.tudelft.simulation.dsol.tutorial.section44;

import org.djutils.draw.bounds.Bounds3d;
import org.djutils.draw.point.DirectedPoint3d;
import org.djutils.draw.point.Point3d;

import nl.tudelft.simulation.dsol.animation.Locatable;

/**
 * A Ball.
 * <p>
 * Copyright (c) 2002-2021 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 */
public abstract class Ball implements Locatable
{
    /** the number of created balls. */
    private static int number = 0;

    /** the radius of the ball. */
    public static final double RADIUS = 5.0;

    /** the name of the ball. */
    private String name = "";

    /** the origin. */
    protected DirectedPoint3d origin = new DirectedPoint3d(0,0,0);

    /** the destination. */
    protected DirectedPoint3d destination = new DirectedPoint3d(0,0,0);

    /** the rotation. */
    protected double rotZ = 0.0;

    /**
     * constructs a new Ball.
     */
    public Ball()
    {
        super();
        this.rotZ = 2 * Math.PI * Math.random();
        Ball.number++;
        this.name = "" + Ball.number;
    }

    /** {@inheritDoc} */
    @Override
    public Bounds3d getBounds()
    {
        return new Bounds3d(-Ball.RADIUS, Ball.RADIUS, -Ball.RADIUS, Ball.RADIUS, -Ball.RADIUS, Ball.RADIUS);
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return this.name;
    }
}
