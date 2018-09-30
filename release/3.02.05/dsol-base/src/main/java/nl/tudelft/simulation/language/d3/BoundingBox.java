package nl.tudelft.simulation.language.d3;

import java.io.Serializable;

import javax.media.j3d.Bounds;
import javax.vecmath.Point3d;

/**
 * A bounding box.
 * <p>
 * Copyright (c) 2002-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. See for project information <a href="https://simulation.tudelft.nl/" target="_blank">
 * https://simulation.tudelft.nl</a>. The DSOL project is distributed under a three-clause BSD-style license, which can
 * be found at <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 */
public class BoundingBox extends javax.media.j3d.BoundingBox implements Serializable
{
    /** the default serialVersionUId. */
    private static final long serialVersionUID = 1L;

    /**
     * constructs a new BoundingBox.
     */
    public BoundingBox()
    {
        super();
    }

    /**
     * constructs a new BoundingBox around [0;0;0].
     * @param deltaX the deltaX
     * @param deltaY the deltaY
     * @param deltaZ the deltaZ
     */
    public BoundingBox(final double deltaX, final double deltaY, final double deltaZ)
    {
        super(new Point3d(-0.5 * deltaX, -0.5 * deltaY, -0.5 * deltaZ),
                new Point3d(0.5 * deltaX, 0.5 * deltaY, 0.5 * deltaZ));
        this.normalize();
    }

    /**
     * constructs a new BoundingBox.
     * @param arg0 the boundaries
     */
    public BoundingBox(final Bounds arg0)
    {
        super(arg0);
        this.normalize();
    }

    /**
     * constructs a new BoundingBox.
     * @param arg0 the boundaries
     */
    public BoundingBox(final Bounds[] arg0)
    {
        super(arg0);
        this.normalize();
    }

    /**
     * constructs a new BoundingBox.
     * @param arg0 the boundaries
     * @param arg1 the point
     */
    public BoundingBox(final Point3d arg0, final Point3d arg1)
    {
        super(arg0, arg1);
        this.normalize();
    }

    /**
     * normalizes the boundingBox.
     */
    public final void normalize()
    {
        Point3d p1 = new Point3d();
        Point3d p2 = new Point3d();
        this.getLower(p1);
        this.getUpper(p2);
        this.setLower(new Point3d(Math.min(p1.x, p2.x), Math.min(p1.y, p2.y), Math.min(p1.z, p2.z)));
        this.setUpper(new Point3d(Math.max(p1.x, p2.x), Math.max(p1.y, p2.y), Math.max(p1.z, p2.z)));
    }
}
