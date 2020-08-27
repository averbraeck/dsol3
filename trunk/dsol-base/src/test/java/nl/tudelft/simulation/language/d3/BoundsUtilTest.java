package nl.tudelft.simulation.language.d3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.awt.geom.Rectangle2D;

import org.junit.Test;
import org.scijava.java3d.BoundingSphere;
import org.scijava.java3d.Bounds;
import org.scijava.vecmath.Point3d;

/**
 * BoundsUtilTest.java.
 * <p>
 * Copyright (c) 2019-2020 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
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
        BoundingBox box000 = new BoundingBox(new Point3d(-2, -2, -2), new Point3d(2, 2, 2));
        DirectedPoint location = new DirectedPoint(4, 4, 4);
        // the box is around (4,4,4), so its real coordinates are (2,2,2, 6,6,6).
        assertNull(BoundsUtil.zIntersect(location, box000, 1));
        assertNull(BoundsUtil.zIntersect(location, box000, 7));
        testRect(BoundsUtil.zIntersect(location, box000, 4), 2, 2, 6, 6);

        BoundingSphere sphere = new BoundingSphere(new Point3d(0, 0, 0), 2.0);
        Rectangle2D rsphere0 = BoundsUtil.zIntersect(new DirectedPoint(0, 0, 0), sphere, 0);
        testRect(rsphere0, -2, -2, 2, 2);
        Rectangle2D rsphere2 = BoundsUtil.zIntersect(new DirectedPoint(0, 0, 0), sphere, 2.0);
        testRect(rsphere2, -2, -2, 2, 2);
    }

    /**
     * Test the bounds of the rectangle.
     * @param rect the rectangle to test
     * @param minx expected minx
     * @param miny expected miny
     * @param maxx expected maxx
     * @param maxy expected maxy
     */
    private void testRect(final Rectangle2D rect, final double minx, final double miny, final double maxx, final double maxy)
    {
        assertEquals(minx, rect.getMinX(), 0.001);
        assertEquals(miny, rect.getMinY(), 0.001);
        assertEquals(maxx, rect.getMaxX(), 0.001);
        assertEquals(maxy, rect.getMaxY(), 0.001);
    }

    /**
     * test transform method.
     */
    @Test
    public void testTransform()
    {
        BoundingSphere sphere = new BoundingSphere(new Point3d(0, 0, 0), 2.0);
        Bounds sp2 = BoundsUtil.transform(sphere, new DirectedPoint(0, 0, 0));
        assertEquals(sphere, sp2);
        Bounds sp3 = BoundsUtil.transform(sphere, new DirectedPoint(1, 1, 0));
        assertNotEquals(sphere, sp3);
    }

    /**
     * test contains method.
     */
    @Test
    public void testContains()
    {
        BoundingBox bounds = new BoundingBox(new Point3d(-2, -2, -2), new Point3d(2, 2, 2));
        DirectedPoint center = new DirectedPoint(4, 4, 4);
        // the box is around (4,4,4), so its real coordinates are (2,2,2, 6,6,6).
        assertFalse(BoundsUtil.contains(center, bounds, new Point3d(0, 0, 0)));
        assertTrue(BoundsUtil.contains(center, bounds, new Point3d(4, 4, 4)));
        assertTrue(BoundsUtil.contains(center, bounds, new Point3d(2, 2, 2)));
        assertTrue(BoundsUtil.contains(center, bounds, new Point3d(6, 6, 6)));
    }
}
