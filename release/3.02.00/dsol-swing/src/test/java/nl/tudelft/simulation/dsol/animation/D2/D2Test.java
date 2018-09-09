package nl.tudelft.simulation.dsol.animation.D2;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * This class defines the JUnit test for the D2Test <br>
 * (c) 2002-2018 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @version 2.0 21.09.2003 <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>,
 *         <a href="mailto:a.verbraeck@tudelft.nl">Alexander Verbraeck </a>
 */
public class D2Test extends TestCase
{
    /** TEST_METHOD_NAME refers to the name of the test method. */
    public static final String TEST_METHOD_NAME = "test";

    /**
     * constructs a new D2Test.
     */
    public D2Test()
    {
        super(TEST_METHOD_NAME);
    }

    /**
     * tests the 2D Animation
     */
    public void test()
    {
        Rectangle2D extent = new Rectangle2D.Double(0, 0, 100, 100);
        Dimension size = new Dimension(100, 100);
        // Let's focus on the scale.
        Assert.assertTrue(Renderable2DInterface.Util.getScale(extent, size) == 1.0);
        size.setSize(200, 200);
        Assert.assertTrue(Renderable2DInterface.Util.getScale(extent, size) == 0.5);
        extent.setRect(0, 0, 50, 50);
        Assert.assertTrue(Renderable2DInterface.Util.getScale(extent, size) == 0.25);
        // Let's test infinity pointer values..
        // size.setSize(0, 0);
        // Assert.assertTrue(Double.isInfinite(Renderable2DInterface.Util.getScale(extent, size)));
        // Let's test invalid screen size
        size.setSize(-1, -1);
        Assert.assertTrue(Double.isNaN(Renderable2DInterface.Util.getScale(extent, size)));
        // Let's test wrong ratio values
        // size.setSize(50, 100);
        // Assert.assertTrue(Double.isNaN(Renderable2DInterface.Util.getScale(extent, size)));
        size.setSize(100, 100);
        extent.setRect(0, 0, 100, 100);
        Point2D point = new Point2D.Double(1, 1);
        Assert.assertTrue(Renderable2DInterface.Util.getScreenCoordinates(point, extent, size).distance(1, 99) == 0);
        size.setSize(200, 200);
        extent.setRect(0, 0, 100, 100);
        point = new Point2D.Double(1, 1);
        Assert.assertTrue(Renderable2DInterface.Util.getScreenCoordinates(point, extent, size).distance(2, 198) == 0);
        // Invalid screen
        /*
         * size.setSize(-200, -200); extent.setRect(0, 0, 100, 100); point = new Point2D.Double(1, 1);
         * Assert.assertNull(Renderable2DInterface.Util.getScreenCoordinates( point, extent, size)); // Invalid ratio
         * size.setSize(200, 100); extent.setRect(0, 0, 100, 100); point = new Point2D.Double(1, 1);
         * Assert.assertNull(Renderable2DInterface.Util.getScreenCoordinates( point, extent, size)); // Let's test for
         * null values size.setSize(100, 100); extent.setRect(0, 0, 100, 100); point = new Point2D.Double(1, 1);
         * Assert.assertNull(Renderable2DInterface.Util.getScreenCoordinates(null, extent, size));
         * Assert.assertNull(Renderable2DInterface.Util.getScreenCoordinates( point, null, size));
         * Assert.assertNull(Renderable2DInterface.Util.getScreenCoordinates( point, extent, null)); // point not in
         * extent size.setSize(100, 100); extent.setRect(0, 0, 100, 100); point = new Point2D.Double(-1, 1);
         * Assert.assertNull(Renderable2DInterface.Util.getScreenCoordinates( point, extent, size));
         */// ********************* WORLD COORDINATES ASSERTIONS **************//
        size.setSize(100, 100);
        extent.setRect(0, 0, 100, 100);
        point = new Point2D.Double(1, 1);
        Assert.assertTrue(Renderable2DInterface.Util.getWorldCoordinates(point, extent, size).distance(1, 99) == 0);

        size.setSize(200, 200);
        extent.setRect(0, 0, 100, 100);
        point = new Point2D.Double(1, 1);
        Assert.assertTrue(Renderable2DInterface.Util.getWorldCoordinates(point, extent, size).distance(0.5, 99.5) == 0);

        // Invalid screen
        /*
         * size.setSize(-200, -200); extent.setRect(0, 0, 100, 100); point = new Point2D.Double(1, 1);
         * Assert.assertNull(Renderable2DInterface.Util.getWorldCoordinates(point, extent, size)); // Invalid ratio
         * size.setSize(200, 100); extent.setRect(0, 0, 100, 100); point = new Point2D.Double(1, 1);
         * Assert.assertNull(Renderable2DInterface.Util.getWorldCoordinates(point, extent, size)); // Let's test for
         * null values size.setSize(100, 100); extent.setRect(0, 0, 100, 100); point = new Point2D.Double(1, 1);
         * Assert.assertNull(Renderable2DInterface.Util.getWorldCoordinates(null, extent, size));
         * Assert.assertNull(Renderable2DInterface.Util.getWorldCoordinates(point, null, size));
         * Assert.assertNull(Renderable2DInterface.Util.getWorldCoordinates(point, extent, null)); // point not in
         * extent size.setSize(100, 100); extent.setRect(0, 0, 100, 100); point = new Point2D.Double(-1, 1);
         * Assert.assertNull(Renderable2DInterface.Util.getWorldCoordinates(point, extent, size));
         */
        // ********************* COMPUTE VISIBLE EXTENT **************//
        size.setSize(1000, 500);
        extent.setRect(0, -10, 5000, 20);
        extent = Renderable2DInterface.Util.computeVisibleExtent(extent, size);
        Assert.assertEquals(5.0, Renderable2DInterface.Util.getScale(extent, size), 1E-6);
        Assert.assertEquals(-1250.0, extent.getMinY(), 1E-6);
        Assert.assertEquals(2500.0, extent.getHeight(), 1E-6);
    }
}
