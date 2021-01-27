package nl.tudelft.simulation.dsol.animation.D2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.rmi.RemoteException;

import org.djutils.draw.bounds.Bounds2d;
import org.djutils.draw.bounds.Bounds3d;
import org.djutils.draw.point.OrientedPoint3d;
import org.djutils.draw.point.Point2d;
import org.junit.Test;

import nl.tudelft.simulation.dsol.animation.Locatable;
import nl.tudelft.simulation.language.d3.BoundsUtil;

/**
 * This class defines the JUnit test for the D2Test.
 * <p>
 * Copyright (c) 2002-2021 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>,
 *         <a href="mailto:a.verbraeck@tudelft.nl">Alexander Verbraeck </a>
 */
public class D2Test
{
    /**
     * tests the 2D Animation.
     */
    @Test
    public void test2dAnimation()
    {
        Bounds2d extent = new Bounds2d(0, 100, 0, 100);
        Dimension size = new Dimension(100, 100);
        // Let's focus on the scale.
        assertTrue(Renderable2DInterface.Util.getScale(extent, size) == 1.0);
        size.setSize(200, 200);
        assertTrue(Renderable2DInterface.Util.getScale(extent, size) == 0.5);
        extent = new Bounds2d(0, 50, 0, 50);
        assertTrue(Renderable2DInterface.Util.getScale(extent, size) == 0.25);
        // Let's test infinity pointer values..
        // size.setSize(0, 0);
        // assertTrue(Double.isInfinite(Renderable2DInterface.Util.getScale(extent, size)));
        // Let's test invalid screen size
        size.setSize(-1, -1);
        assertTrue(Double.isNaN(Renderable2DInterface.Util.getScale(extent, size)));
        // Let's test wrong ratio values
        // size.setSize(50, 100);
        // assertTrue(Double.isNaN(Renderable2DInterface.Util.getScale(extent, size)));
        size.setSize(100, 100);
        extent = new Bounds2d(0, 100, 0, 100);
        Point2d point = new Point2d(1, 1);
        assertTrue(Renderable2DInterface.Util.getScreenCoordinates(point, extent, size).distance(1, 99) == 0);
        size.setSize(200, 200);
        extent = new Bounds2d(0, 100, 0, 100);
        point = new Point2d(1, 1);
        assertTrue(Renderable2DInterface.Util.getScreenCoordinates(point, extent, size).distance(2, 198) == 0);
        // Invalid screen
        /*
         * size.setSize(-200, -200); extent.setRect(0, 0, 100, 100); point = new Point2D.Double(1, 1);
         * assertNull(Renderable2DInterface.Util.getScreenCoordinates( point, extent, size)); // Invalid ratio size.setSize(200,
         * 100); extent.setRect(0, 0, 100, 100); point = new Point2D.Double(1, 1);
         * assertNull(Renderable2DInterface.Util.getScreenCoordinates( point, extent, size)); // Let's test for null values
         * size.setSize(100, 100); extent.setRect(0, 0, 100, 100); point = new Point2D.Double(1, 1);
         * assertNull(Renderable2DInterface.Util.getScreenCoordinates(null, extent, size));
         * assertNull(Renderable2DInterface.Util.getScreenCoordinates( point, null, size));
         * assertNull(Renderable2DInterface.Util.getScreenCoordinates( point, extent, null)); // point not in extent
         * size.setSize(100, 100); extent.setRect(0, 0, 100, 100); point = new Point2D.Double(-1, 1);
         * assertNull(Renderable2DInterface.Util.getScreenCoordinates( point, extent, size));
         */// ********************* WORLD COORDINATES ASSERTIONS **************//
        size.setSize(100, 100);
        extent = new Bounds2d(0, 100, 0, 100);
        Point2D point2D = new Point2D.Double(1, 1);
        assertTrue(Renderable2DInterface.Util.getWorldCoordinates(point2D, extent, size).distance(new Point2d(1, 99)) == 0);

        size.setSize(200, 200);
        extent = new Bounds2d(0, 100, 0, 100);
        point2D = new Point2D.Double(1, 1);
        assertTrue(Renderable2DInterface.Util.getWorldCoordinates(point2D, extent, size).distance(new Point2d(0.5, 99.5)) == 0);

        // Invalid screen
        /*
         * size.setSize(-200, -200); extent.setRect(0, 0, 100, 100); point = new Point2D.Double(1, 1);
         * assertNull(Renderable2DInterface.Util.getWorldCoordinates(point, extent, size)); // Invalid ratio size.setSize(200,
         * 100); extent.setRect(0, 0, 100, 100); point = new Point2D.Double(1, 1);
         * assertNull(Renderable2DInterface.Util.getWorldCoordinates(point, extent, size)); // Let's test for null values
         * size.setSize(100, 100); extent.setRect(0, 0, 100, 100); point = new Point2D.Double(1, 1);
         * assertNull(Renderable2DInterface.Util.getWorldCoordinates(null, extent, size));
         * assertNull(Renderable2DInterface.Util.getWorldCoordinates(point, null, size));
         * assertNull(Renderable2DInterface.Util.getWorldCoordinates(point, extent, null)); // point not in extent
         * size.setSize(100, 100); extent.setRect(0, 0, 100, 100); point = new Point2D.Double(-1, 1);
         * assertNull(Renderable2DInterface.Util.getWorldCoordinates(point, extent, size));
         */
        // ********************* COMPUTE VISIBLE EXTENT **************//
        size.setSize(1000, 500);
        extent = new Bounds2d(0, 5000, -10, 10);
        extent = Renderable2DInterface.Util.computeVisibleExtent(extent, size);
        assertEquals(5.0, Renderable2DInterface.Util.getScale(extent, size), 1E-6);
        assertEquals(-1250.0, extent.getMinY(), 1E-6);
        assertEquals(2500.0, extent.getDeltaY(), 1E-6);
    }

    /**
     * Test the zIntersect and bounds transformation.
     * @throws RemoteException on network error
     */
    @Test
    public void testZIntersect() throws RemoteException
    {
        class L implements Locatable
        {
            private double x, y, dirZ;

            L(final double x, final double y, final double dirZ)
            {
                this.x = x;
                this.y = y;
                this.dirZ = dirZ;
            }

            @Override
            public OrientedPoint3d getLocation() throws RemoteException
            {
                return new OrientedPoint3d(this.x, this.y, 0, 0, 0, dirZ);
            }

            @Override
            public Bounds3d getBounds() throws RemoteException
            {
                return new Bounds3d(-4, 4, -4, 4, -4, 4);
            }

        }

        L l = new L(0.0, 0.0, 0.0);
        Bounds3d b = BoundsUtil.transform(l.getBounds(), l.getLocation());
        assertEquals(-4, b.getMinX(), 0.001);
        assertEquals(+4, b.getMaxX(), 0.001);
        assertEquals(-4, b.getMinY(), 0.001);
        assertEquals(+4, b.getMaxY(), 0.001);
        assertEquals(-4, b.getMinZ(), 0.001);
        assertEquals(+4, b.getMaxZ(), 0.001);

        l = new L(20.0, 10.0, 0.0);
        b = BoundsUtil.transform(l.getBounds(), l.getLocation());
        assertEquals(20 - 4, b.getMinX(), 0.001);
        assertEquals(20 + 4, b.getMaxX(), 0.001);
        assertEquals(10 - 4, b.getMinY(), 0.001);
        assertEquals(10 + 4, b.getMaxY(), 0.001);
        assertEquals(-4, b.getMinZ(), 0.001);
        assertEquals(+4, b.getMaxZ(), 0.001);

        l = new L(20.0, 10.0, Math.toRadians(90.0));
        b = BoundsUtil.transform(l.getBounds(), l.getLocation());
        assertEquals(20 - 4, b.getMinX(), 0.001);
        assertEquals(20 + 4, b.getMaxX(), 0.001);
        assertEquals(10 - 4, b.getMinY(), 0.001);
        assertEquals(10 + 4, b.getMaxY(), 0.001);
        assertEquals(-4, b.getMinZ(), 0.001);
        assertEquals(+4, b.getMaxZ(), 0.001);

        l = new L(0.0, 0.0, Math.toRadians(45.0));
        double d = 4.0 * Math.sqrt(2.0);
        b = BoundsUtil.transform(l.getBounds(), l.getLocation());
        assertEquals(-d, b.getMinX(), 0.001);
        assertEquals(d, b.getMaxX(), 0.001);
        assertEquals(-d, b.getMinY(), 0.001);
        assertEquals(d, b.getMaxY(), 0.001);
        assertEquals(-4, b.getMinZ(), 0.001);

        l = new L(20.0, 10.0, Math.toRadians(45.0));
        d = 4.0 * Math.sqrt(2.0);
        b = BoundsUtil.transform(l.getBounds(), l.getLocation());
        assertEquals(20 - d, b.getMinX(), 0.001);
        assertEquals(20 + d, b.getMaxX(), 0.001);
        assertEquals(10 - d, b.getMinY(), 0.001);
        assertEquals(10 + d, b.getMaxY(), 0.001);
        assertEquals(-4, b.getMinZ(), 0.001);
        assertEquals(+4, b.getMaxZ(), 0.001);
    }
}
