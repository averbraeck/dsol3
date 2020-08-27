package nl.tudelft.simulation.language.d3;

import java.awt.geom.Rectangle2D;

import org.scijava.java3d.Bounds;
import org.scijava.java3d.Transform3D;
import org.scijava.vecmath.Point3d;
import org.scijava.vecmath.Vector3d;

/**
 * A Bounds utility class to help with finding intersections between bounds, to make transformations, and to see if a point lies
 * within a bounds. The static methods can help in animation to see whether a shape needs to be drawn on the screen
 * (3D-viewport) or not.
 * <p>
 * Copyright (c) 2002-2020 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
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
     * Computes the intersect of the bounding box of the provided bounds with the plane spanned by the zValue. Note that the
     * bounds are <b>relative to the center</b> and provided without the translation and rotation of the center directed point!
     * Usually the center is in the bounds, but that is not necessary. The center is in many occasions the Location of an
     * animated object, and the bounds indicate the outer values of its animation relative to (0,0,0). When the bounding box has
     * no values on the given z-height, null is returned. As an example: suppose the bounding box has corner coordinates
     * (-1,-1,-1) and (1,1,1) (all relative to 0,0,0), and the current center location is (0,0,0) and we ask the zIntersect for
     * z=0, we get a rectangle [(-1,-1), (1,1)]. When we ask the zInterzect for z=2 and center point (2,2,0) we get null (no
     * bounds on height z=2). When we ask the zIntersect for z=0 and point (2,2,0) we get [(1,1), (3,3)] -- the box is
     * translated with dx=2 and dy=2. When we ask the zIntersect for z=0 and point (2,2,2) we get null as we 'lifted' the
     * bounding box above the z-value. When center has a direction, the bounds is first rotated around the center after which
     * the translation takes place and the bounds on the z-height are calculated.
     * @param bounds Bounds; the bounds for which the intersection needs to be calculated. The Bounds are <b>relative to the
     *            center</b> that is provided
     * @param center DirectedPoint; the point relative to which the bounds need to be calculated
     * @param zValue double; the zValue as the 'height' for which the bounds intersection is calculated
     * @return Rectangle2D the resulting rectangle of the intersection
     */
    public static Rectangle2D zIntersect(final DirectedPoint center, final Bounds bounds, final double zValue)
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
     * Rotates and translates a bound relative to a directed point. Often this point will be the given center point for the
     * animation.
     * @param point DirectedPoint; the point relative to which the bounds need to be transformed
     * @param bounds Bounds; the bounds that need to be rotated and translated
     * @return the bounds after rotation and translation
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
     * Check whether a point is in the bounds, after transforming the bounds relative to the center point (in animation that is
     * the location). Usually the center is in the bounds, but that is not necessary. The center is in many occasions the
     * Location of an animated object, and the bounds indicate the outer values of its animation without translation and
     * rotation (as if center is 0,0,0) and has no direction (rotX, rotY and rotZ are 0.0).
     * @param center DirectedPoint; the 'center' of the bounds.
     * @param bounds Bounds; the bounds relative to 0,0,0
     * @param point Point3d; the point that might be in or out of the bounds after they have been rotated and translated
     *            relative to the center.
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
        return (point.x >= lower.x && point.x <= upper.x && point.y >= lower.y && point.y <= upper.y && point.z >= lower.z
                && point.z <= upper.z);
    }
}
