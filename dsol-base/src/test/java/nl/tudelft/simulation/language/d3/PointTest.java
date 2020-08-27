package nl.tudelft.simulation.language.d3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.awt.geom.Point2D;

import org.junit.Test;
import org.scijava.vecmath.Point3d;
import org.scijava.vecmath.Point3f;
import org.scijava.vecmath.Tuple3d;
import org.scijava.vecmath.Tuple3f;

/**
 * PointTest.java.
 * <p>
 * Copyright (c) 2019-2020 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djunits.org/docs/license.html">DJUNITS License</a>.
 * <p>
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank">Alexander Verbraeck</a>
 */
public class PointTest
{
    /**
     * Test the DirectedPoint (and all constructors of the CartesianPoint).
     */
    @Test
    public void testDirectedPoint()
    {
        DirectedPoint p1 = new DirectedPoint();
        testDP(p1, 0, 0, 0, 0, 0, 0);
        p1 = new DirectedPoint(1, 2, 3);
        testDP(p1, 1, 2, 3, 0, 0, 0);
        p1 = new DirectedPoint(4, 5, 6, .2, .3, .4);
        testDP(p1, 4, 5, 6, .2, .3, .4);
        assertTrue(p1.equals(p1.clone()));
        DirectedPoint p2 = new DirectedPoint(p1);
        testDP(p2, 4, 5, 6, .2, .3, .4);
        assertEquals(p1, p2);
        assertEquals(p1.hashCode(), p2.hashCode());
        p1 = new DirectedPoint(new double[] {2, 4, 6});
        testDP(p1, 2, 4, 6, 0, 0, 0);
        p1 = new DirectedPoint(new Point2D.Double(3.5, 7.0));
        testDP(p1, 3.5, 7.0, 0, 0, 0, 0);
        p1 = new DirectedPoint(new Point2D.Double(3.5, 7.0), 0.707);
        testDP(p1, 3.5, 7.0, 0, 0, 0, 0.707);
        p1.setX(10.0);
        p1.setY(11.0);
        p1.setZ(12.0);
        p1.setRotX(0.1);
        p1.setRotY(0.2);
        p1.setRotZ(0.3);
        testDP(p1, 10, 11, 12, .1, .2, .3);
        p1 = new DirectedPoint(new SphericalPoint(1.0, 0.0, 0.0));
        testDP(p1, 0, 0, 1, 0, 0, 0);
        p1 = new DirectedPoint(new Point3d(5, 6, 7));
        testDP(p1, 5, 6, 7, 0, 0, 0);
        p1 = new DirectedPoint(new Point3f(3f, 4f, 5f));
        testDP(p1, 3, 4, 5, 0, 0, 0);
        p1 = new DirectedPoint((Tuple3d) new Point3d(5, 6, 7));
        testDP(p1, 5, 6, 7, 0, 0, 0);
        p1 = new DirectedPoint((Tuple3f) new Point3f(3f, 4f, 5f));
        testDP(p1, 3, 4, 5, 0, 0, 0);
        assertTrue(p1.equals(p1));
        assertFalse(p1.equals(p2));
        assertFalse(p1.equals(new Point3d(3, 4, 5)));
        assertFalse(p1.equals(new DirectedPoint(3, 4, 5, 0.1, 0, 0)));
        assertFalse(p1.equals(new DirectedPoint(3, 4, 5, 0, 0.1, 0)));
        assertFalse(p1.equals(new DirectedPoint(3, 4, 5, 0, 0, 0.1)));
        p1 = new DirectedPoint(123, 456, 789, 0.135, 0.246, 0.357);
        assertTrue(p1.toString().contains("123.0"));
        assertTrue(p1.toString().contains("456.0"));
        assertTrue(p1.toString().contains("789.0"));
        assertTrue(p1.toString().contains("RotX=0.135"));
        assertTrue(p1.toString().contains("RotY=0.246"));
        assertTrue(p1.toString().contains("RotZ=0.357"));
    }

    /**
     * @param p the point to check
     * @param x double; expected x
     * @param y double; expected y
     * @param z double; expected z
     * @param rX double; expected rotX
     * @param rY double; expected rotY
     * @param rZ double; expected rotZ
     */
    private void testDP(DirectedPoint p, double x, double y, double z, double rX, double rY, double rZ)
    {
        assertNotNull(p);
        assertEquals(x, p.x, 0.0001);
        assertEquals(x, p.getX(), 0.0001);
        assertEquals(y, p.y, 0.0001);
        assertEquals(y, p.getY(), 0.0001);
        assertEquals(z, p.z, 0.0001);
        assertEquals(z, p.getZ(), 0.0001);
        assertEquals(rX, p.getRotX(), 0.0001);
        assertEquals(rY, p.getRotY(), 0.0001);
        assertEquals(rZ, p.getRotZ(), 0.0001);
    }

    /**
     * Test the CartesianPoint and the SphericalPoint.
     */
    @SuppressWarnings("unlikely-arg-type")
    @Test
    public void testCartesianSphericalPoint()
    {
        CartesianPoint cp = new CartesianPoint(1.0, 2.0, 3.0);
        assertTrue(cp.equals(cp));
        assertEquals(1.0, cp.to2D().getX(), 0.001);
        assertEquals(2.0, cp.to2D().getY(), 0.001);
        cp = new CartesianPoint(1, 1, 1);
        SphericalPoint sp = cp.toSphericalPoint();
        assertEquals(Math.sqrt(3), sp.getRadius(), 0.001);
        // theta = arctan y/x
        assertEquals(Math.atan(1.0), sp.getTheta(), 0.001);
        // phi = arccos z/r
        assertEquals(Math.acos(1.0 / Math.sqrt(3)), sp.getPhi(), 0.001);
        assertTrue(sp.equals(sp));
        assertFalse(sp.equals(cp));
        assertFalse(cp.equals(sp));
        CartesianPoint cp1 = cp.toSphericalPoint().toCartesianPoint();
        assertEquals(cp.x, cp1.x, 0.001);
        assertEquals(cp.y, cp1.y, 0.001);
        assertEquals(cp.z, cp1.z, 0.001);
        SphericalPoint sp1 = sp.toCartesianPoint().toSphericalPoint();
        assertEquals(sp.getRadius(), sp1.getRadius(), 0.001);
        assertEquals(sp.getTheta(), sp1.getTheta(), 0.001);
        assertEquals(sp.getPhi(), sp1.getPhi(), 0.001);
        sp = new SphericalPoint(2.5, 0.2, 1.2);
        assertTrue(sp.equals(new SphericalPoint(2.5, 0.2, 1.2)));
        assertEquals(sp.hashCode(), new SphericalPoint(2.5, 0.2, 1.2).hashCode());
        assertFalse(sp.equals(new SphericalPoint(2.0, 0.2, 1.2)));
        assertFalse(sp.equals(new SphericalPoint(2.5, 0.25, 1.2)));
        assertFalse(sp.equals(new SphericalPoint(2.5, 0.2, 1.25)));
        assertFalse(sp.equals(null));
        assertTrue(sp.toString().contains("radius=2.5"));
        assertTrue(sp.toString().contains("theta=0.2"));
        assertTrue(sp.toString().contains("phi=1.2"));
    }
}
