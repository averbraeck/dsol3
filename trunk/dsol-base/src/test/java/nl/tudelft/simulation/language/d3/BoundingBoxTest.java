package nl.tudelft.simulation.language.d3;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.scijava.java3d.BoundingSphere;
import org.scijava.java3d.Bounds;
import org.scijava.vecmath.Point3d;

/**
 * The JUNIT Test for the <code>BoundingBox</code>.
 * <p>
 * Copyright (c) 2002-2020 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @since 1.5
 */
public class BoundingBoxTest
{
    /**
     * Test the BoundingBox.
     */
    @Test
    public void testBoundingBox()
    {
        Point3d p1 = new Point3d();
        Point3d p2 = new Point3d();

        BoundingBox bb1 = new BoundingBox();
        bb1.getLower(p1);
        bb1.getUpper(p2);
        assertEquals(-1.0, p1.x, 0.001);
        assertEquals(-1.0, p1.y, 0.001);
        assertEquals(-1.0, p1.z, 0.001);
        assertEquals(1.0, p2.x, 0.001);
        assertEquals(1.0, p2.y, 0.001);
        assertEquals(1.0, p2.z, 0.001);

        BoundingBox bb2 = new BoundingBox(1.0, 1.0, 1.0);
        bb2.getLower(p1);
        bb2.getUpper(p2);
        assertEquals(-0.5, p1.x, 0.001);
        assertEquals(-0.5, p1.y, 0.001);
        assertEquals(-0.5, p1.z, 0.001);
        assertEquals(0.5, p2.x, 0.001);
        assertEquals(0.5, p2.y, 0.001);
        assertEquals(0.5, p2.z, 0.001);

        BoundingBox bb3 = new BoundingBox(new Point3d(0, 0, 0), new Point3d(1.0, 1.0, 1.0));
        bb3.getLower(p1);
        bb3.getUpper(p2);
        assertEquals(0.0, p1.x, 0.001);
        assertEquals(0.0, p1.y, 0.001);
        assertEquals(0.0, p1.z, 0.001);
        assertEquals(1.0, p2.x, 0.001);
        assertEquals(1.0, p2.y, 0.001);
        assertEquals(1.0, p2.z, 0.001);

        Bounds b1 = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 2.0);
        BoundingBox bb4 = new BoundingBox(b1);
        bb4.getLower(p1);
        bb4.getUpper(p2);
        assertEquals(-2.0, p1.x, 0.001);
        assertEquals(-2.0, p1.y, 0.001);
        assertEquals(-2.0, p1.z, 0.001);
        assertEquals(2.0, p2.x, 0.001);
        assertEquals(2.0, p2.y, 0.001);
        assertEquals(2.0, p2.z, 0.001);

        Bounds b2 = new BoundingBox(new Point3d(0, 0, 0), new Point3d(1.0, 1.0, 1.0));
        Bounds b3 = new BoundingBox(new Point3d(0.5, 0.5, 0.5), new Point3d(-1.0, -1.0, -1.0));
        BoundingBox bb5 = new BoundingBox(new Bounds[] {b2, b3});
        bb5.getLower(p1);
        bb5.getUpper(p2);
        assertEquals(-1.0, p1.x, 0.001);
        assertEquals(-1.0, p1.y, 0.001);
        assertEquals(-1.0, p1.z, 0.001);
        assertEquals(1.0, p2.x, 0.001);
        assertEquals(1.0, p2.y, 0.001);
        assertEquals(1.0, p2.z, 0.001);
    }

}
