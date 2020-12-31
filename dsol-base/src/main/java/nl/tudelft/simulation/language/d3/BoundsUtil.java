package nl.tudelft.simulation.language.d3;

import java.awt.geom.Rectangle2D;

import org.djutils.draw.Transform2d;
import org.djutils.draw.Transform3d;
import org.djutils.draw.bounds.Bounds;
import org.djutils.draw.bounds.Bounds2d;
import org.djutils.draw.bounds.Bounds3d;
import org.djutils.draw.point.DirectedPoint2d;
import org.djutils.draw.point.DirectedPoint3d;
import org.djutils.draw.point.Point;
import org.djutils.draw.point.Point2d;
import org.djutils.draw.point.Point3d;

/**
 * A Bounds3d utility class to help with finding intersections between bounds, to make transformations, and to see if a point
 * lies within a bounds. The static methods can help in animation to see whether a shape needs to be drawn on the screen
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
     * @param bounds Bounds; the bounds for which the intersection needs to be calculated. The Bounds3d are <b>relative to the
     *            center</b> that is provided
     * @param center DirectedPoint3d; the point relative to which the bounds need to be calculated
     * @param zValue double; the zValue as the 'height' for which the bounds intersection is calculated
     * @return Rectangle2D the resulting rectangle of the intersection
     */
    public static Rectangle2D zIntersect(final Point<?, ?> center, final Bounds<?, ?> bounds, final double zValue)
    {
        if (center instanceof DirectedPoint3d && bounds instanceof Bounds3d)
        {
            DirectedPoint3d center3d = (DirectedPoint3d) center;
            Transform3d transform = new Transform3d();
            transform.rotZ(center3d.getDirZ());
            transform.rotY(center3d.getDirY());
            transform.rotX(center3d.getDirX());
            transform.translate(center3d);
            Bounds3d box = transform.transform((Bounds3d) bounds);
            Point3d lower = new Point3d(box.getMinX(), box.getMinY(), zValue);
            if (!box.covers(lower))
            {
                return null;
            }
            return new Rectangle2D.Double(box.getMinX(), box.getMinY(), box.getDeltaX(), box.getDeltaY());
        }
        else if (center instanceof Point3d && bounds instanceof Bounds3d)
        {
            Point3d center3d = (Point3d) center;
            Transform3d transform = new Transform3d();
            transform.translate(center3d);
            Bounds3d box = transform.transform((Bounds3d) bounds);
            Point3d lower = new Point3d(box.getMinX(), box.getMinY(), zValue);
            if (!box.covers(lower))
            {
                return null;
            }
            return new Rectangle2D.Double(box.getMinX(), box.getMinY(), box.getDeltaX(), box.getDeltaY());
        }
        else if (center instanceof DirectedPoint2d)
        {
            DirectedPoint2d center2d = (DirectedPoint2d) center;
            Transform2d transform = new Transform2d();
            transform.rotation(center2d.getDirZ());
            transform.translate(center2d);
            Bounds2d box = transform.transform((Bounds2d) bounds);
            Point2d lower = new Point2d(box.getMinX(), box.getMinY());
            if (!box.covers(lower))
            {
                return null;
            }
            return new Rectangle2D.Double(box.getMinX(), box.getMinY(), box.getDeltaX(), box.getDeltaY());
        }
        Point2d center2d = (Point2d) center;
        Transform2d transform = new Transform2d();
        transform.translate(center2d);
        Bounds2d box = transform.transform((Bounds2d) bounds);
        Point2d lower = new Point2d(box.getMinX(), box.getMinY());
        if (!box.covers(lower))
        {
            return null;
        }
        return new Rectangle2D.Double(box.getMinX(), box.getMinY(), box.getDeltaX(), box.getDeltaY());
    }

    /**
     * Rotates and translates a bound relative to a directed point. Often this point will be the given center point for the
     * animation.
     * @param point DirectedPoint3d; the point relative to which the bounds need to be transformed
     * @param bounds Bounds; the bounds that need to be rotated and translated
     * @return the bounds after rotation and translation
     */
    public static Bounds3d transform(final Bounds3d bounds, final DirectedPoint3d point)
    {
        Transform3d transform = new Transform3d();
        transform.rotZ(point.getDirZ());
        transform.rotY(point.getDirY());
        transform.rotX(point.getDirX());
        transform.translate(point);
        return transform.transform(bounds);
    }

    /**
     * Check whether a point is in the bounds, after transforming the bounds relative to the center point (in animation that is
     * the location). Usually the center is in the bounds, but that is not necessary. The center is in many occasions the
     * Location of an animated object, and the bounds indicate the outer values of its animation without translation and
     * rotation (as if center is 0,0,0) and has no direction (rotX, rotY and rotZ are 0.0).
     * @param center DirectedPoint3d; the 'center' of the bounds.
     * @param bounds Bounds; the bounds relative to 0,0,0
     * @param point Point3d; the point that might be in or out of the bounds after they have been rotated and translated
     *            relative to the center.
     * @return whether or not the point is in the bounds
     */
    public static boolean contains(final DirectedPoint3d center, final Bounds3d bounds, final Point3d point)
    {
        Transform3d transform = new Transform3d();
        transform.rotZ(center.getDirZ());
        transform.rotY(center.getDirY());
        transform.rotX(center.getDirX());
        transform.translate(center);
        Bounds3d box = transform.transform(bounds);
        return box.covers(point);
    }
}
