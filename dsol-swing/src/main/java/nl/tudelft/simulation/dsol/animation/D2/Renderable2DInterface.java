package nl.tudelft.simulation.dsol.animation.D2;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.ImageObserver;
import java.rmi.RemoteException;

import javax.naming.NamingException;

import nl.tudelft.simulation.dsol.animation.LocatableInterface;

/**
 * The Renderable2D interface defines the basic interface for 2d animation. This is a hard-to-use interface. It is
 * implemented by the easy-to-use Renderable2D class <br>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl"> Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl"> www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @version $Revision: 1.1 $ $Date: 2010/08/10 11:37:21 $
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 */

public interface Renderable2DInterface
{
    /**
     * paints the object on a 2D graphics object
     * @param graphics the graphics object
     * @param extent the extent of the panel
     * @param screen the screen of the panel
     * @param observer the observer of the renderableInterface
     */
    void paint(final Graphics2D graphics, final Rectangle2D extent, final Dimension screen, final ImageObserver observer);

    /**
     * gets the source of this renderable
     * @return Locatable the source
     */
    LocatableInterface getSource();

    /**
     * does the shape contain the point
     * @param pointWorldCoordinates the point in world coordinates. Default implementation is to intersect the 3D bounds
     *            on location.z and to return the bounds2D of this intersect.
     * @param extent the extent of the panel.
     * @param screen the screen of the panel.
     * @return whether the point is in the shape
     */
    boolean contains(Point2D pointWorldCoordinates, final Rectangle2D extent, Dimension screen);

    /**
     * destroys this editable. How to do this must be implemented by the modeler
     * @throws RemoteException RemoteException
     * @throws NamingException
     */
    void destroy() throws RemoteException, NamingException;

    /**
     * A Util <br>
     * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
     * Netherlands. <br>
     * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
     * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
     * warranty.
     * @version 1.0 Mar 1, 2004 <br>
     * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
     */
    static class Util
    {

        /**
         * constructs a new Util.
         */
        protected Util()
        {
            // constructs a new transform.
        }

        /**
         * returns the scale of a screen compared to an extent. The scale can only be computed if the xScale and yScale
         * are equal. If this is not the case, Double.NaN is returned. In order to overcome estimation errors, this
         * equality is computed with Math.abs(yScale-xScale) <0.005xScale. If the height or the width of the screen are
         * <0 Double.NaN is returned.
         * @param extent the extent
         * @param screen the screen
         * @return double the scale. Can return Double.NaN
         */
        public static double getScale(final Rectangle2D extent, final Dimension screen)
        {
            if (screen.getHeight() <= 0 || screen.getWidth() <= 0)
            {
                return Double.NaN;
            }
            return extent.getWidth() / screen.getWidth();
        }

        /**
         * computes the visible extent
         * @param extent the extent
         * @param screen the screen
         * @return a new extent or null if parameters are null or screen is invalid ( <0)
         */
        public static Rectangle2D computeVisibleExtent(final Rectangle2D extent, final Dimension screen)
        {
            if (extent == null || screen == null || screen.getHeight() <= 0 || screen.getWidth() <= 0)
            {
                return null;
            }
            double xScale = extent.getWidth() / screen.getWidth();
            double yScale = extent.getHeight() / screen.getHeight();
            Rectangle2D result = (Rectangle2D) extent.clone();
            if (xScale >= yScale)
            {
                result.setRect(result.getCenterX() - 0.5 * yScale * screen.getWidth(), result.getY(),
                        yScale * screen.getWidth(), result.getHeight());
            }
            else
            {
                result.setRect(result.getX(), result.getCenterY() - 0.5 * xScale * screen.getHeight(),
                        result.getWidth(), xScale * screen.getHeight());
            }
            return result;
        }

        /**
         * returns the frame xy-coordinates of a point in world coordinates. If parameters are invalid (i.e. screen.size
         * <0) a null value is returned. If parameter combinations (i.e !extent.contains(point)) are invalid a null
         * value is returned.
         * @param worldCoordinates the world coordinates
         * @param extent the extent of this
         * @param screen the screen
         * @return Point2D (x,y) on screen. Can be null
         */
        public static Point2D getScreenCoordinates(final Point2D worldCoordinates, final Rectangle2D extent,
                final Dimension screen)
        {
            double scale = 1.0 / Renderable2DInterface.Util.getScale(extent, screen);
            double x = (worldCoordinates.getX() - extent.getMinX()) * scale;
            double y = screen.getHeight() - (worldCoordinates.getY() - extent.getMinY()) * scale;
            return new Point2D.Double(x, y);
        }

        /**
         * returns the frame xy-coordinates of a point in screen coordinates. If parameters are invalid (i.e.
         * screen.size <0) a null value is returned. If parameter combinations (i.e !screen.contains(point)) are invalid
         * a null value is returned.
         * @param screenCoordinates the screen coordinates
         * @param extent the extent of this
         * @param screen the screen
         * @return Point2D (x,y) on screen
         */
        public static Point2D getWorldCoordinates(final Point2D screenCoordinates, final Rectangle2D extent,
                final Dimension screen)
        {
            double scale = Renderable2DInterface.Util.getScale(extent, screen);
            double x = (screenCoordinates.getX()) * scale + extent.getMinX();
            double y = ((screen.getHeight() - screenCoordinates.getY())) * scale + extent.getMinY();
            return new Point2D.Double(x, y);
        }
    }
}
