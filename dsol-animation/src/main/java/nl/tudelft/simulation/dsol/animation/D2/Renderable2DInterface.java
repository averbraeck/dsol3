package nl.tudelft.simulation.dsol.animation.D2;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.image.ImageObserver;
import java.io.Serializable;
import java.rmi.RemoteException;

import javax.naming.NamingException;

import org.djutils.draw.bounds.Bounds2d;
import org.djutils.draw.point.Point;
import org.djutils.draw.point.Point2d;

import nl.tudelft.simulation.dsol.animation.Locatable;
import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;

/**
 * The Renderable2D interface defines the basic interface for 2d animation. This is a hard-to-use interface. It is implemented
 * by the easy-to-use Renderable2D class.
 * <p>
 * Copyright (c) 2002-2021 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @param <L> the Locatable class of the source that indicates the location of the Renderable on the screen
 */
public interface Renderable2DInterface<L extends Locatable> extends Serializable
{
    /**
     * paints the object on a 2D graphics object.
     * @param graphics Graphics2D; the graphics object
     * @param extent Bounds2d; the extent of the panel
     * @param screenSize Dimension; the screen of the panel
     * @param observer ImageObserver; the observer of the renderableInterface
     */
    void paint(final Graphics2D graphics, final Bounds2d extent, final Dimension screenSize, final ImageObserver observer);

    /**
     * gets the source of this renderable.
     * @return Locatable the source
     */
    L getSource();

    /**
     * does the shape contain the point?
     * @param pointWorldCoordinates Point2d; the point in world coordinates. Default implementation is to intersect the 3D
     *            bounds on location.z and to return the bounds2D of this intersect.
     * @param extent Bounds2d; the extent of the panel.
     * @return whether the point is in the shape
     */
    boolean contains(Point2d pointWorldCoordinates, final Bounds2d extent);

    /**
     * destroys this editable. How to do this must be implemented by the modeler.
     * @param simulator SimulatorInterface&lt;?,?,?&gt;; the simulator used for unbinding the object from the context
     * @throws RemoteException RemoteException
     * @throws NamingException NamingException
     */
    void destroy(SimulatorInterface<?, ?, ?> simulator) throws RemoteException, NamingException;

    /**
     * A Utility helper class for transforming between screen coordinates and world coordinates. <br>
     * <p>
     * Copyright (c) 2003-2021 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved.
     * See for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>.
     * The DSOL project is distributed under a three-clause BSD-style license, which can be found at
     * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
     * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
     * </p>
     * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
     */
    class Util
    {
        /** Utility class - no constructor. */
        private Util()
        {
            // Utility class
        }

        /**
         * returns the scale of a screen compared to an extent. The scale can only be computed if the xScale and yScale are
         * equal. If this is not the case, Double.NaN is returned. In order to overcome estimation errors, this equality is
         * computed with Math.abs(yScale-xScale) &lt; 0.005 * xScale. If the height or the width of the screen are &lt; 0
         * Double.NaN is returned.
         * @param extent Rectangle2D; the extent of this animation
         * @param screen Dimension; the screen dimensions
         * @return double the scale. Can return Double.NaN
         */
        public static double getScale(final Bounds2d extent, final Dimension screen)
        {
            if (screen.getHeight() <= 0 || screen.getWidth() <= 0)
            {
                return Double.NaN;
            }
            return extent.getDeltaX() / screen.getWidth();
        }

        /**
         * computes the visible extent.
         * @param extent Rectangle2D; the extent
         * @param screen Dimension; the screen
         * @return a new extent or null if parameters are null or screen is invalid (width / height &lt;= 0)
         */
        public static Bounds2d computeVisibleExtent(final Bounds2d extent, final Dimension screen)
        {
            if (extent == null || screen == null || screen.getHeight() <= 0 || screen.getWidth() <= 0)
            {
                return null;
            }
            double xScale = extent.getDeltaX() / screen.getWidth();
            double yScale = extent.getDeltaY() / screen.getHeight();
            Bounds2d result;
            if (xScale < yScale)
            {
                result = new Bounds2d(extent.midPoint().getX() - 0.5 * yScale * screen.getWidth(),
                        extent.midPoint().getX() + 0.5 * yScale * screen.getWidth(), extent.getMinY(), extent.getMaxY());
            }
            else
            {
                result = new Bounds2d(extent.getMinX(), extent.getMaxX(),
                        extent.midPoint().getY() - 0.5 * xScale * screen.getHeight(),
                        extent.midPoint().getY() + 0.5 * xScale * screen.getHeight());
            }
            return result;
        }

        /**
         * returns the frame xy-coordinates of a point in world coordinates. If parameters are invalid (i.e. screen.size &lt;=
         * 0) a null value is returned. If parameter combinations (i.e !extent.contains(point)) are invalid a null value is
         * returned.
         * @param worldCoordinates Point&lt;?, ?&gt;; the world coordinates
         * @param extent Bounds2d; the extent of this animation
         * @param screen Dimension; the screen dimentsions
         * @return Point2D (x,y) on screen. Can be null.
         */
        public static Point2D getScreenCoordinates(final Point<?, ?> worldCoordinates, final Bounds2d extent,
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
         * @param extent Bounds2d; the extent of this animation
         * @param screen Dimension; the screen dimensions
         * @return Point2d (x,y) in the 2D or 3D world
         */
        public static Point2d getWorldCoordinates(final Point2D screenCoordinates, final Bounds2d extent,
                final Dimension screen)
        {
            double scale = Renderable2DInterface.Util.getScale(extent, screen);
            double x = (screenCoordinates.getX()) * scale + extent.getMinX();
            double y = ((screen.getHeight() - screenCoordinates.getY())) * scale + extent.getMinY();
            return new Point2d(x, y);
        }
    }
}
