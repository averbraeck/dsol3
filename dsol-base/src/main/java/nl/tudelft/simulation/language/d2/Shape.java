/*
 * @(#) Shape.java Jun 17, 2004 Copyright (c) 2002-2009 Delft University of Technology Jaffalaan 5, 2628 BX
 * Delft, the Netherlands. All rights reserved. The code is published under a BSD-style license.
 */
package nl.tudelft.simulation.language.d2;

import java.awt.geom.Rectangle2D;

/**
 * Shape utilities.
 * <p>
 * Copyright (c) 2002-2009 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved.
 * <p>
 * See for project information <a href="http://www.simulation.tudelft.nl/"> www.simulation.tudelft.nl</a>.
 * <p>
 * The DSOL project is distributed under the following BSD-style license:<br>
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
 * following conditions are met:
 * <ul>
 * <li>Redistributions of source code must retain the above copyright notice, this list of conditions and the following
 * disclaimer.</li>
 * <li>Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the
 * following disclaimer in the documentation and/or other materials provided with the distribution.</li>
 * <li>Neither the name of Delft University of Technology, nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.</li>
 * </ul>
 * This software is provided by the copyright holders and contributors "as is" and any express or implied warranties,
 * including, but not limited to, the implied warranties of merchantability and fitness for a particular purpose are
 * disclaimed. In no event shall the copyright holder or contributors be liable for any direct, indirect, incidental,
 * special, exemplary, or consequential damages (including, but not limited to, procurement of substitute goods or
 * services; loss of use, data, or profits; or business interruption) however caused and on any theory of liability,
 * whether in contract, strict liability, or tort (including negligence or otherwise) arising in any way out of the use
 * of this software, even if advised of the possibility of such damage.
 * @author <a href="http://www.peter-jacobs.com/index.htm">Peter Jacobs </a>
 * @version $Revision: 1.2 $ $Date: 2009/10/21 07:32:42 $
 * @since 1.5
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
        boolean contains =
                (r1.getMinX() <= r1.getMinX() && r1.getMinY() <= r2.getMinY() && r1.getMaxX() >= r2.getMaxX() && r1
                        .getMaxY() >= r2.getMaxY());
        return contains;
    }
}
