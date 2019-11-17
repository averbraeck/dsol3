package nl.tudelft.simulation.examples.dsol.animation;

import java.rmi.RemoteException;

import javax.media.j3d.BoundingSphere;
import javax.media.j3d.Bounds;
import javax.vecmath.Point3d;

import nl.tudelft.simulation.dsol.animation.Locatable;

/**
 * A Ball.
 * <p>
 * Copyright (c) 2003-2019 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://simulation.tudelft.nl/people/jacobs.html">Peter Jacobs </a>
 */
public abstract class Ball implements Locatable
{
    /** the number of created balls. */
    private static int number = 0;

    /** the radius of the ball. */
    public static final double RADIUS = 5.0;

    /** the angle of the ball. */
    protected double theta = 0.0;

    /** the name of the ball. */
    private String name = "";

    /**
     * constructs a new Ball.
     */
    public Ball()
    {
        super();
        this.theta = 2 * Math.PI * Math.random();
        Ball.number++;
        this.name = "" + Ball.number;
    }

    /** {@inheritDoc} */
    @Override
    public Bounds getBounds() throws RemoteException
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
