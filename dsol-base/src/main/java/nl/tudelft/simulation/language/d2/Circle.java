package nl.tudelft.simulation.language.d2;

import java.awt.geom.Point2D;

/**
 * The Circle class presents a number of mathematical utility functions for circles. For now, the class only implements
 * static helper methods. No instances of the class should be made.
 * <p>
 * Copyright (c) 2002-2018  Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
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
 * @version Oct 17, 2009
 * @author <a href="http://tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public final class Circle
{
    /**
     * constructs a new Circle.
     */
    private Circle()
    {
        super();
        // unreachable code
    }

    /**
     * Elegant intersection algorithm from http://astronomy.swin.edu.au/~pbourke/geometry/2circle/.
     * @param center0 the center of the first circle
     * @param radius0 the radius of the first circle
     * @param center1 the center of the second circle
     * @param radius1 the radius of the second circle
     * @return the intersection
     */
    public static Point2D[] intersection(final Point2D center0, final double radius0, final Point2D center1,
            final double radius1)
    {
        double distance = center0.distance(center1);
        double x0 = center0.getX();
        double x1 = center1.getX();
        double y0 = center0.getY();
        double y1 = center1.getY();
        Point2D[] result;
        if ((distance > radius0 + radius1) || (distance < Math.abs(radius0 - radius1)))
        {
            return new Point2D.Double[0];
        }
        double a = (radius0 * radius0 - radius1 * radius1 + distance * distance) / (2 * distance);
        double h = Math.sqrt(radius0 * radius0 - a * a);
        double x2 = x0 + ((a / distance) * (x1 - x0));
        double y2 = y0 + ((a / distance) * (y1 - y0));
        if (distance == radius0 + radius1)
        {
            result = new Point2D.Double[1];
            result[0] = new Point2D.Double(x2, y2);
        }
        else
        {
            result = new Point2D.Double[2];
            result[0] = new Point2D.Double(x2 + h * (y1 - y0) / distance, y2 - h * (x1 - x0) / distance);
            result[1] = new Point2D.Double(x2 - h * (y1 - y0) / distance, y2 + h * (x1 - x0) / distance);
        }
        return result;
    }
}
