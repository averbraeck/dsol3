package nl.tudelft.simulation.dsol.animation.D2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.awt.Dimension;
import java.awt.geom.Point2D;

import org.djutils.draw.bounds.Bounds2d;
import org.djutils.draw.point.Point2d;
import org.junit.Test;

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
     * tests the 2D Animation
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
         * assertNull(Renderable2DInterface.Util.getScreenCoordinates( point, extent, size)); // Invalid ratio
         * size.setSize(200, 100); extent.setRect(0, 0, 100, 100); point = new Point2D.Double(1, 1);
         * assertNull(Renderable2DInterface.Util.getScreenCoordinates( point, extent, size)); // Let's test for null
         * values size.setSize(100, 100); extent.setRect(0, 0, 100, 100); point = new Point2D.Double(1, 1);
         * assertNull(Renderable2DInterface.Util.getScreenCoordinates(null, extent, size));
         * assertNull(Renderable2DInterface.Util.getScreenCoordinates( point, null, size));
         * assertNull(Renderable2DInterface.Util.getScreenCoordinates( point, extent, null)); // point not in extent
         * size.setSize(100, 100); extent.setRect(0, 0, 100, 100); point = new Point2D.Double(-1, 1);
         * assertNull(Renderable2DInterface.Util.getScreenCoordinates( point, extent, size));
         */// ********************* WORLD COORDINATES ASSERTIONS **************//
        size.setSize(100, 100);
        extent = new Bounds2d(0, 100, 0, 100);
        Point2D point2D = new Point2D.Double(1, 1);
        assertTrue(
                Renderable2DInterface.Util.getWorldCoordinates(point2D, extent, size).distance(new Point2d(1, 99)) == 0);

        size.setSize(200, 200);
        extent = new Bounds2d(0, 100, 0, 100);
        point2D = new Point2D.Double(1, 1);
        assertTrue(
                Renderable2DInterface.Util.getWorldCoordinates(point2D, extent, size).distance(new Point2d(0.5, 99.5)) == 0);

        // Invalid screen
        /*
         * size.setSize(-200, -200); extent.setRect(0, 0, 100, 100); point = new Point2D.Double(1, 1);
         * assertNull(Renderable2DInterface.Util.getWorldCoordinates(point, extent, size)); // Invalid ratio
         * size.setSize(200, 100); extent.setRect(0, 0, 100, 100); point = new Point2D.Double(1, 1);
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
}
