package nl.tudelft.simulation.dsol.animation.D2;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.ImageObserver;
import java.rmi.RemoteException;

import javax.naming.NamingException;

import nl.tudelft.simulation.dsol.animation.Locatable;

/**
 * The Renderable2D interface defines the basic interface for 2d animation. This is a hard-to-use interface. It is implemented
 * by the easy-to-use Renderable2D class.
 * <p>
 * Copyright (c) 2002-2019 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @param <T> the Locatable class of the source that indicates the location of the Renderable on the screen
 */
public interface Renderable2DInterface<T extends Locatable>
{
    /**
     * paints the object on a 2D graphics object.
     * @param graphics Graphics2D; the graphics object
     * @param extent Rectangle2D; the extent of the panel
     * @param screenSize Dimension; the screen of the panel
     * @param observer ImageObserver; the observer of the renderableInterface
     */
    void paint(final Graphics2D graphics, final Rectangle2D extent, final Dimension screenSize, final ImageObserver observer);

    /**
     * gets the source of this renderable.
     * @return Locatable the source
     */
    T getSource();

    /**
     * does the shape contain the point?
     * @param pointWorldCoordinates Point2D; the point in world coordinates. Default implementation is to intersect the 3D
     *            bounds on location.z and to return the bounds2D of this intersect.
     * @param extent Rectangle2D; the extent of the panel.
     * @param screenSize Dimension; the screen of the panel.
     * @return whether the point is in the shape
     */
    boolean contains(Point2D pointWorldCoordinates, final Rectangle2D extent, Dimension screenSize);

    /**
     * destroys this editable. How to do this must be implemented by the modeler.
     * @throws RemoteException RemoteException
     * @throws NamingException NamingException
     */
    void destroy() throws RemoteException, NamingException;

    /**
     * A Utility helper class for transforming between screen coordinates and world coordinates. <br>
     * <p>
     * Copyright (c) 2003-2019 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved.
     * See for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>.
     * The DSOL project is distributed under a three-clause BSD-style license, which can be found at
     * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
     * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
     * </p>
     * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
     */
    class Util
    {
        /**
         * constructs a new Util.
         */
        protected Util()
        {
            // constructs a new transform.
        }

        /**
         * returns the scale of a screen compared to an extent. The scale can only be computed if the xScale and yScale are
         * equal. If this is not the case, Double.NaN is returned. In order to overcome estimation errors, this equality is
         * computed with Math.abs(yScale-xScale) &lt; 0.005 * xScale. If the height or the width of the screen are &lt; 0
         * Double.NaN is returned.
         * @param extent Rectangle2D; the extent
         * @param screen Dimension; the screen
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
         * computes the visible extent.
         * @param extent Rectangle2D; the extent
         * @param screen Dimension; the screen
         * @return a new extent or null if parameters are null or screen is invalid (&lt; 0)
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
            if (xScale < yScale)
            {
                result.setRect(result.getCenterX() - 0.5 * yScale * screen.getWidth(), result.getY(),
                        yScale * screen.getWidth(), result.getHeight());
            }
            else
            {
                result.setRect(result.getX(), result.getCenterY() - 0.5 * xScale * screen.getHeight(), result.getWidth(),
                        xScale * screen.getHeight());
            }
            return result;
        }

        /**
         * returns the frame xy-coordinates of a point in world coordinates. If parameters are invalid (i.e. screen.size &lt; 0)
         * a null value is returned. If parameter combinations (i.e !extent.contains(point)) are invalid a null value is
         * returned.
         * @param worldCoordinates Point2D; the world coordinates
         * @param extent Rectangle2D; the extent of this
         * @param screen Dimension; the screen
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
         * returns the frame xy-coordinates of a point in screen coordinates. If parameters are invalid (i.e. screen.size &lt;
         * 0) a null value is returned. If parameter combinations (i.e !screen.contains(point)) are invalid a null value is
         * returned.
         * @param screenCoordinates Point2D; the screen coordinates
         * @param extent Rectangle2D; the extent of this
         * @param screen Dimension; the screen
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
