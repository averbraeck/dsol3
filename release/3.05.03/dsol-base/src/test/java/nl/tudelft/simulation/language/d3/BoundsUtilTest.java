package nl.tudelft.simulation.language.d3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.djutils.draw.Transform3d;
import org.djutils.draw.bounds.Bounds2d;
import org.djutils.draw.bounds.Bounds3d;
import org.djutils.draw.point.OrientedPoint3d;
import org.djutils.draw.point.Point3d;
import org.junit.Test;

/**
 * BoundsUtilTest.java.
 * <p>
 * Copyright (c) 2019-2021 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djunits.org/docs/license.html">DJUNITS License</a>.
 * <p>
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank">Alexander Verbraeck</a>
 */
public class BoundsUtilTest
{
    /**
     * test zIntersect method.
     */
    @Test
    public void testZIntersect()
    {
        Bounds3d box000 = new Bounds3d(new Point3d[] {new Point3d(-2, -2, -2), new Point3d(2, 2, 2)});
        OrientedPoint3d location = new OrientedPoint3d(4, 4, 4);
        // the box is around (4,4,4), so its real coordinates are (2,2,2, 6,6,6).
        assertNull(BoundsUtil.zIntersect(location, box000, 1));
        assertNull(BoundsUtil.zIntersect(location, box000, 7));
        testRect(BoundsUtil.zIntersect(location, box000, 4), 2, 2, 6, 6);
    }

    /**
     * test transform method.
     */
    @Test
    public void testTransform()
    {
        Bounds3d box000 = new Bounds3d(new Point3d[] {new Point3d(-2, -2, -2), new Point3d(2, 2, 2)});
        OrientedPoint3d location = new OrientedPoint3d(4, 4, 4);
        // rotate 45 degrees around z-axis w.r.t. (0,0,0) ->
        // bounding box becomes (-2.sqrt(2), -2.sqrt(2), -2), (2.sqrt(2), 2.sqrt(2), 2)
        // the box is around (4,4,4), so its real coordinates are (4-2.sqrt(2), 4-2.sqrt(2), 2), (4+2.sqrt(2), 4+2.sqrt(2), 6).
        Transform3d tr45 = new Transform3d();
        tr45.rotZ(Math.toRadians(45.0));
        Bounds3d box45 = tr45.transform(box000);
        Bounds3d box = BoundsUtil.transform(box45, location);
        double s2 = 2.0 * Math.sqrt(2.0);
        testRect(box.project(), 4.0 - s2, 4.0 - s2, 4.0 + s2, 4.0 + s2);
        assertEquals(2.0, box.getMinZ(), 0.001);
        assertEquals(6.0, box.getMaxZ(), 0.001);
    }

    /**
     * Test the bounds of the rectangle.
     * @param rect the rectangle to test
     * @param minx expected minx
     * @param miny expected miny
     * @param maxx expected maxx
     * @param maxy expected maxy
     */
    private void testRect(final Bounds2d rect, final double minx, final double miny, final double maxx, final double maxy)
    {
        assertEquals(minx, rect.getMinX(), 0.001);
        assertEquals(miny, rect.getMinY(), 0.001);
        assertEquals(maxx, rect.getMaxX(), 0.001);
        assertEquals(maxy, rect.getMaxY(), 0.001);
    }

    /**
     * test contains method.
     */
    @Test
    public void testContains()
    {
        Bounds3d bounds = new Bounds3d(new Point3d[] {new Point3d(-2, -2, -2), new Point3d(2, 2, 2)});
        OrientedPoint3d center = new OrientedPoint3d(4, 4, 4);
        // the box is around (4,4,4), so its real coordinates are (2,2,2, 6,6,6).
        assertFalse(BoundsUtil.contains(center, bounds, new Point3d(0, 0, 0)));
        assertTrue(BoundsUtil.contains(center, bounds, new Point3d(4, 4, 4)));
        assertTrue(BoundsUtil.contains(center, bounds, new Point3d(2, 2, 2)));
        assertTrue(BoundsUtil.contains(center, bounds, new Point3d(6, 6, 6)));
    }
}
