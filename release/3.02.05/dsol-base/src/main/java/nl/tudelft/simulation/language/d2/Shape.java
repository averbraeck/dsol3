package nl.tudelft.simulation.language.d2;

import java.awt.geom.Rectangle2D;

/**
 * Shape utilities.
 * <p>
 * Copyright (c) 2002-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. See for project information <a href="https://simulation.tudelft.nl/" target="_blank">
 * https://simulation.tudelft.nl</a>. The DSOL project is distributed under a three-clause BSD-style license, which can
 * be found at <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 */
public final class Shape
{
    /**
     * constructs a new Shape.
     */
    private Shape()
    {
        super();
    }

    /**
     * overlaps extent and shape. Overlap = Intersect + Contain
     * @param extent the extent
     * @param shape the shape
     * @return whether extent overlaps shape
     */
    public static boolean overlaps(final Rectangle2D extent, final Rectangle2D shape)
    {
        if (extent.getMaxX() < shape.getMinX())
        {
            return false;
        }
        if (extent.getMaxY() < shape.getMinY())
        {
            return false;
        }
        if (extent.getMinX() > shape.getMaxX())
        {
            return false;
        }
        if (extent.getMinY() > shape.getMaxY())
        {
            return false;
        }
        return true;
    }

    /**
     * @param r1 the first rectangle
     * @param r2 the second rectangle
     * @return whether r1 intersects r2
     */
    public static boolean intersects(final Rectangle2D r1, final Rectangle2D r2)
    {
        return !Shape.contains(r1, r2) && Shape.intersects(r1, r2);
    }

    /**
     * is r1 completely in r2.
     * @param r1 the first rectangle
     * @param r2 the second rectangle
     * @return whether r1 in r2
     */
    public static boolean contains(final Rectangle2D r1, final Rectangle2D r2)
    {
        boolean contains = (r1.getMinX() <= r1.getMinX() && r1.getMinY() <= r2.getMinY() && r1.getMaxX() >= r2.getMaxX()
                && r1.getMaxY() >= r2.getMaxY());
        return contains;
    }
}
