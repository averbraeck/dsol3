package nl.tudelft.simulation.language.d3;

import java.awt.geom.Rectangle2D;

import javax.media.j3d.Bounds;
import javax.media.j3d.Transform3D;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

/**
 * A Bounds utility class to help with finding intersections between bounds, to make transformations, and to see if a
 * point lies witin a bounds. The static methods can help in animation to see whether a shape needs to be drawn on the
 * screen (3D-viewport) or not.
 * <p>
 * Copyright (c) 2002-2019 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. See for project information <a href="https://simulation.tudelft.nl/" target="_blank">
 * https://simulation.tudelft.nl</a>. The DSOL project is distributed under a three-clause BSD-style license, which can
 * be found at <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 */
public final class BoundsUtil
{
    /**
     * constructs a new BoundsUtil.
     */
    private BoundsUtil()
    {
        super();
        // unreachable code
    }

    /**
     * computes the intersect of bounds with the zValue.
     * @param bounds Bounds; the bounds
     * @param center DirectedPoint; the
     * @param zValue double; the zValue
     * @return Rectangle2D the result
     */
    public static Rectangle2D getIntersect(final DirectedPoint center, final Bounds bounds, final double zValue)
    {
        BoundingBox box = new BoundingBox((Bounds) bounds.clone());
        Transform3D transform = new Transform3D();
        transform.rotZ(center.getRotZ());
        transform.rotY(center.getRotY());
        transform.rotX(center.getRotX());
        transform.setTranslation(new Vector3d(new Point3d(center.x, center.y, center.z)));
        box.transform(transform);

        Point3d lower = new Point3d();
        box.getLower(lower);
        lower.set(lower.x, lower.y, zValue);
        if (!box.intersect(lower))
        {
            return null;
        }
        Point3d upper = new Point3d();
        box.getUpper(upper);
        return new Rectangle2D.Double(lower.x, lower.y, (upper.x - lower.x), (upper.y - lower.y));
    }

    /**
     * rotates and translates to a directed point.
     * @param point DirectedPoint; the point
     * @param bounds Bounds; the bounds
     * @return the bounds
     */
    public static Bounds transform(final Bounds bounds, final DirectedPoint point)
    {
        Bounds result = (Bounds) bounds.clone();

        // First we rotate around 0,0,0
        Transform3D transform = new Transform3D();
        transform.rotX(point.getRotX());
        transform.rotY(point.getRotY());
        transform.rotZ(point.getRotZ());
        transform.setTranslation(new Vector3d(point));
        result.transform(transform);
        return result;
    }

    /**
     * @param center DirectedPoint; the center of the bounds
     * @param bounds Bounds; the bounds
     * @param point Point3d; the point
     * @return whether or not the point is in the bounds
     */
    public static boolean contains(final DirectedPoint center, final Bounds bounds, final Point3d point)
    {
        BoundingBox box = new BoundingBox((Bounds) bounds.clone());
        Transform3D transform = new Transform3D();
        transform.rotZ(center.getRotZ());
        transform.rotY(center.getRotY());
        transform.rotX(center.getRotX());
        transform.setTranslation(new Vector3d(center));
        box.transform(transform);
        Point3d lower = new Point3d();
        box.getLower(lower);
        Point3d upper = new Point3d();
        box.getUpper(upper);
        return (point.x >= lower.x && point.x <= upper.x && point.y >= lower.y && point.y <= upper.y
                && point.z >= lower.z && point.z <= upper.z);
    }
}
