package nl.tudelft.simulation.dsol.animation;

import java.awt.geom.Point2D;

import javax.media.j3d.Bounds;

import nl.tudelft.simulation.language.d3.DirectedPoint;

/**
 * A StaticLocation <br>
 * <p>
 * Copyright (c) 2002-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. See for project information <a href="https://simulation.tudelft.nl/" target="_blank">
 * https://simulation.tudelft.nl</a>. The DSOL project is distributed under a three-clause BSD-style license, which can
 * be found at <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 */
public class StaticLocation extends DirectedPoint implements Locatable
{
    /** The default serial version UID for serializable classes. */
    private static final long serialVersionUID = 1L;

    /** the bounds. */
    private Bounds bounds = null;

    /**
     * constructs a new StaticLocation.
     * @param x double; the x location
     * @param y double; the y location
     * @param z double; the z location
     * @param theta double; theta
     * @param phi double; phi
     * @param rho double; rho
     * @param bounds Bounds; the bounds
     */
    public StaticLocation(final double x, final double y, final double z, final double theta, final double phi,
            final double rho, final Bounds bounds)
    {
        super(x, y, z, theta, phi, rho);
        this.bounds = bounds;
    }

    /**
     * constructs a new StaticLocation.
     * @param point2D Point2D; the point2d
     * @param rotZ double; the rotation in the xy plane
     * @param bounds Bounds; the bounds
     */
    public StaticLocation(final Point2D point2D, final double rotZ, final Bounds bounds)
    {
        super(point2D, rotZ);
        this.bounds = bounds;
    }

    /**
     * constructs a new StaticLocation.
     * @param location DirectedPoint; the location
     * @param bounds Bounds; the bounds
     */
    public StaticLocation(final DirectedPoint location, final Bounds bounds)
    {
        super(location);
        this.bounds = bounds;
    }

    /** {@inheritDoc} */
    @Override
    public final DirectedPoint getLocation()
    {
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public final Bounds getBounds()
    {
        return this.bounds;
    }

}
