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
 * Copyright (c) 2002-2009 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved.
 * <p>
 * See for project information <a href="http://www.simulation.tudelft.nl/"> www.simulation.tudelft.nl</a>.
 * <p>
 * The DSOL project is distributed under the following BSD-style license:<br>
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
 * following conditions are met:
 * <ul>
 * <li>Redistributions of source code must retain the above copyright notice, this list of conditions and the following
 * disclaimer.</li>
 * <li>Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the
 * following disclaimer in the documentation and/or other materials provided with the distribution.</li>
 * <li>Neither the name of Delft University of Technology, nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.</li>
 * </ul>
 * This software is provided by the copyright holders and contributors "as is" and any express or implied warranties,
 * including, but not limited to, the implied warranties of merchantability and fitness for a particular purpose are
 * disclaimed. In no event shall the copyright holder or contributors be liable for any direct, indirect, incidental,
 * special, exemplary, or consequential damages (including, but not limited to, procurement of substitute goods or
 * services; loss of use, data, or profits; or business interruption) however caused and on any theory of liability,
 * whether in contract, strict liability, or tort (including negligence or otherwise) arising in any way out of the use
 * of this software, even if advised of the possibility of such damage.
 * @version $Revision: 1.2 $ $Date: 2009/10/21 07:32:42 $
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
     * @param bounds the bounds
     * @param center the
     * @param zValue the zValue
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
     * @param point the point
     * @param bounds the bounds
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
     * @param center the center of the bounds
     * @param bounds the bounds
     * @param point the point
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
