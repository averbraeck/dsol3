package nl.tudelft.simulation.dsol.tutorial.section44;

import org.scijava.java3d.BoundingSphere;
import org.scijava.java3d.Bounds;
import org.scijava.vecmath.Point3d;

import nl.tudelft.simulation.dsol.animation.Locatable;
import nl.tudelft.simulation.language.d3.DirectedPoint;

/**
 * A Ball.
 * <p>
 * Copyright (c) 2002-2020 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
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
    protected DirectedPoint origin = new DirectedPoint();

    /** the destination. */
    protected DirectedPoint destination = new DirectedPoint();

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
    public Bounds getBounds()
    {
        return new BoundingSphere(new Point3d(0, 0, 0), Ball.RADIUS);
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return this.name;
    }
}
