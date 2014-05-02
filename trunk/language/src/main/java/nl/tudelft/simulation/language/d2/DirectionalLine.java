/*
 * @(#) DirectionalLine.java 23-jul-2004 Copyright (c) 2002-2005 Delft University of Technology Jaffalaan 5,
 * 2628 BX Delft, the Netherlands. All rights reserved. This software is proprietary information of Delft
 * University of Technology 
 */
package nl.tudelft.simulation.language.d2;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

/**
 * A directional line with normal vector. Based on the BSPLine-example from the
 * book Developing games in Java from David Brackeen. DirectionalLine.java
 * <p>
 * Copyright (c) 2002-2009 Delft University of Technology, Jaffalaan 5, 2628 BX
 * Delft, the Netherlands. All rights reserved.
 * <p>
 * See for project information <a href="http://www.simulation.tudelft.nl/">
 * www.simulation.tudelft.nl</a>.
 * <p>
 * The DSOL project is distributed under the following BSD-style license:<br>
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * <ul>
 * <li>Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.</li>
 * <li>Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.</li>
 * <li>Neither the name of Delft University of Technology, nor the names of its
 * contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.</li>
 * </ul>
 * This software is provided by the copyright holders and contributors "as is"
 * and any express or implied warranties, including, but not limited to, the
 * implied warranties of merchantability and fitness for a particular purpose
 * are disclaimed. In no event shall the copyright holder or contributors be
 * liable for any direct, indirect, incidental, special, exemplary, or
 * consequential damages (including, but not limited to, procurement of
 * substitute goods or services; loss of use, data, or profits; or business
 * interruption) however caused and on any theory of liability, whether in
 * contract, strict liability, or tort (including negligence or otherwise)
 * arising in any way out of the use of this software, even if advised of the
 * possibility of such damage.
 * 
 * @version $Revision: 1.2 $ $Date: 2009/10/21 07:32:42 $
 * @author <a href="mailto:royc@tbm.tudelft.nl">Roy Chin </a>
 */
public class DirectionalLine extends Line2D.Double
{
    /** the default serialVersionUID */
    private static final long serialVersionUID = 1L;

    /** point at the back of the line. */
    public static final int BACKSIDE = -1;

    /** point collinear with the line. */
    public static final int COLLINEAR = 0;

    /** point in front of the line. */
    public static final int FRONTSIDE = 1;

    /** other line is spanning this line. */
    public static final int SPANNING = 2;

    /** the thickness of the line. */
    private double lineThickness = 1;

    /** x coordinate of the line normal. */
    private double normalX;

    /** y coordinate of the line normal. */
    private double normalY;

    /**
     * Creates a new DirectionalLine based on the specified coordinates.
     * 
     * @param x1 Coordinate x1
     * @param y1 Coordinate y1
     * @param x2 Coordinate x2
     * @param y2 Coordinate y2
     */
    public DirectionalLine(final double x1, final double y1, final double x2,
            final double y2)
    {
        this.setLine(x1, y1, x2, y2);
    }

    /**
     * Calculates the normal to this line.
     */
    public void calcNormal()
    {
        this.normalX = this.y2 - this.y1;
        this.normalY = this.x1 - this.x2;
    }

    /**
     * Normalizes the normal of this line (make the normal's length 1).
     */
    public void normalize()
    {
        double length = Math.sqrt(this.normalX * this.normalX + this.normalY
                * this.normalY);
        this.normalX /= length;
        this.normalY /= length;
    }

    /**
     * Set the line using floats.
     * 
     * @param x1 x1 coordinate
     * @param y1 y1 coodinate
     * @param x2 x2 coordinate
     * @param y2 y2 coordinate
     */
    public void setLine(final float x1, final float y1, final float x2,
            final float y2)
    {
        super.setLine(x1, y1, x2, y2);
        this.calcNormal();
    }

    /**
     * @see java.awt.geom.Line2D#setLine(double, double, double, double)
     */
    @Override
    public void setLine(final double x1, final double y1, final double x2,
            final double y2)
    {
        super.setLine(x1, y1, x2, y2);
        this.calcNormal();
    }

    /**
     * Flips this line so that the end points are reversed (in other words,
     * (x1,y1) becomes (x2,y2) and vice versa) and the normal is changed to
     * point the opposite direction.
     */
    public void flip()
    {
        double tx = this.x1;
        double ty = this.y1;
        this.x1 = this.x2;
        this.y1 = this.y2;
        this.x2 = tx;
        this.y2 = ty;
        this.normalX = -this.normalX;
        this.normalY = -this.normalY;
    }

    /**
     * Returns true if the endpoints of this line match the endpoints of the
     * specified line. Ignores normal and height values.
     * 
     * @param line another line
     * @return true if this line's coordinates are equal to the other line's
     *         coordinates
     */
    public boolean equalsCoordinates(final DirectionalLine line)
    {
        return (this.x1 == line.x1 && this.x2 == line.x2 && this.y1 == line.y1 && this.y2 == line.y2);
    }

    /**
     * Returns true if the endpoints of this line match the endpoints of the
     * specified line, ignoring endpoint order (if the first point of this line
     * is equal to the second point of the specified line, and vice versa,
     * returns true). Ignores normal and height values.
     * 
     * @param line another line
     * @return true if coordinates match independent of the order
     */
    public boolean equalsCoordinatesIgnoreOrder(final DirectionalLine line)
    {
        return equalsCoordinates(line)
                || ((this.x1 == line.x2 && this.x2 == line.x1
                        && this.y1 == line.y2 && this.y2 == line.y1));
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return "(" + this.x1 + ", " + this.y1 + ")->" + "(" + this.x2 + ","
                + this.y2 + ")";
    }

    /**
     * Gets the side of this line the specified point is on. This method treats
     * the line as 1-unit thick, so points within this 1-unit border are
     * considered collinear. For this to work correctly, the normal of this line
     * must be normalized, either by setting this line to a polygon or by
     * calling normalize(). Returns either FRONTSIDE, BACKSIDE, or COLLINEAR.
     * 
     * @param x coordinate x
     * @param y coordinate y
     * @return the side
     */
    public int getSideThick(final double x, final double y)
    {
        double normalX2 = this.normalX * this.lineThickness;
        double normalY2 = this.normalY * this.lineThickness;

        int frontSide = getSideThin(x - normalX2 / 2, y - normalY2 / 2);
        if (frontSide == DirectionalLine.FRONTSIDE)
        {
            return FRONTSIDE;
        } else if (frontSide == DirectionalLine.BACKSIDE)
        {
            int backSide = getSideThin(x + normalX2 / 2, y + normalY2 / 2);
            if (backSide == DirectionalLine.BACKSIDE)
            {
                return DirectionalLine.BACKSIDE;
            }
        }
        return DirectionalLine.COLLINEAR;
    }

    /**
     * Gets the side of this line the specified point is on. Because of doubling
     * point inaccuracy, a collinear line will be rare. For this to work
     * correctly, the normal of this line must be normalized, either by setting
     * this line to a polygon or by calling normalize(). Returns either
     * FRONTSIDE, BACKSIDE, or COLLINEAR.
     * 
     * @param x coordinate x
     * @param y coordinate y
     * @return the side
     */
    public int getSideThin(final double x, final double y)
    {
        // dot product between vector to the point and the normal
        double side = (x - this.x1) * this.normalX + (y - this.y1)
                * this.normalY;
        if (side < 0)
        {
            return DirectionalLine.BACKSIDE;
        } else if (side > 0)
        {
            return DirectionalLine.FRONTSIDE;
        } else
        {
            return DirectionalLine.COLLINEAR;
        }
    }

    /**
     * Gets the side of this line that the specified line segment is on. Returns
     * either FRONT, BACK, COLINEAR, or SPANNING.
     * 
     * @param line line segment
     * @return the side
     */
    public int getSide(final Line2D.Double line)
    {
        if (this.x1 == line.x1 && this.x2 == line.x2 && this.y1 == line.y1
                && this.y2 == line.y2)
        {
            return DirectionalLine.COLLINEAR;
        }
        int p1Side = getSideThick(line.x1, line.y1);
        int p2Side = getSideThick(line.x2, line.y2);
        if (p1Side == p2Side)
        {
            return p1Side;
        } else if (p1Side == DirectionalLine.COLLINEAR)
        {
            return p2Side;
        } else if (p2Side == DirectionalLine.COLLINEAR)
        {
            return p1Side;
        } else
        {
            return DirectionalLine.SPANNING;
        }
    }

    /**
     * Returns the fraction of intersection along this line. Returns a value
     * from 0 to 1 if the segments intersect. For example, a return value of 0
     * means the intersection occurs at point (x1, y1), 1 means the intersection
     * occurs at point (x2, y2), and .5 mean the intersection occurs halfway
     * between the two endpoints of this line. Returns -1 if the lines are
     * parallel.
     * 
     * @param line a line
     * @return the intersection
     */
    public double getIntersection(final Line2D.Double line)
    {
        // The intersection point I, of two vectors, A1->A2 and
        // B1->B2, is:
        // I = A1 + Ua * (A2 - A1)
        // I = B1 + Ub * (B2 - B1)
        //
        // Solving for Ua gives us the following formula.
        // Ua is returned.
        double denominator = (line.y2 - line.y1) * (this.x2 - this.x1)
                - (line.x2 - line.x1) * (this.y2 - this.y1);

        // check if the two lines are parallel
        if (denominator == 0)
        {
            return -1;
        }

        double numerator = (line.x2 - line.x1) * (this.y1 - line.y1)
                - (line.y2 - line.y1) * (this.x1 - line.x1);

        return numerator / denominator;
    }

    /**
     * Returns the intersection point of this line with the specified line.
     * 
     * @param line a line
     * @return intersection point
     */
    public Point2D.Double getIntersectionPoint(final Line2D.Double line)
    {
        double fraction = getIntersection(line);
        Point2D.Double intersection = new Point2D.Double();
        intersection.setLocation(this.x1 + fraction * (this.x2 - this.x1),
                this.y1 + fraction * (this.y2 - this.y1));
        return intersection;
    }

    /**
     * Gets the thickness of the line.
     * 
     * @return returns the lineThickness
     */
    public double getLineThickness()
    {
        return this.lineThickness;
    }

    /**
     * Sets the thickness of the line.
     * 
     * @param lineThickness the lineThickness to set
     */
    public void setLineThickness(final double lineThickness)
    {
        this.lineThickness = lineThickness;
    }

    /**
     * @return returns the normalX
     */
    public double getNormalx()
    {
        return this.normalX;
    }

    /**
     * @return returns the normalY
     */
    public double getNormaly()
    {
        return this.normalY;
    }
}
