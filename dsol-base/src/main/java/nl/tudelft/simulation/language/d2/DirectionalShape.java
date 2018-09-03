package nl.tudelft.simulation.language.d2;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Point2d;

/**
 * DirectionalShape is used to create a shape out of vertices and find out whether a certain point is inside or outside
 * of the shape.
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
 * @version $Revision: 1.2 $ $Date: 2009/10/21 07:32:42 $
 * @author <a href="mailto:royc@tbm.tudelft.nl">Roy Chin </a>
 */
public class DirectionalShape implements Serializable
{
    /** the default serialVersionUId. */
    private static final long serialVersionUID = 1L;

    /** points that span up the shape. */
    private List<Point2d> points = new ArrayList<Point2d>();

    /** lines that connect the points. */
    private List<DirectionalLine> lines = new ArrayList<DirectionalLine>();

    /** the default last side. */
    public static final int DEFAULT_LAST_SIDE = -10;

    /**
     * constructs a new directional line.
     */
    public DirectionalShape()
    {
        super();
    }

    /**
     * add a point to the shape.
     * @param x X coordinate
     * @param y Y coordinate
     */
    public void addPoint(final double x, final double y)
    {
        this.points.add(new Point2d(x, y));
    }

    /**
     * determine the line segments between the points.
     */
    public void determineSegments()
    {
        // First clear possible previous segments
        this.lines.clear();
        // All segments but the closing segment
        for (int i = 0; i < this.points.size() - 1; i++)
        {
            double x1 = this.points.get(i).x;
            double y1 = this.points.get(i).y;
            double x2 = this.points.get(i + 1).x;
            double y2 = this.points.get(i + 1).y;
            DirectionalLine line = new DirectionalLine(x1, y1, x2, y2);
            this.lines.add(line);
        }
        // The closing segment
        double x1 = this.points.get(this.points.size() - 1).x;
        double y1 = this.points.get(this.points.size() - 1).y;
        double x2 = this.points.get(0).x;
        double y2 = this.points.get(0).y;
        DirectionalLine line = new DirectionalLine(x1, y1, x2, y2);
        this.lines.add(line);
    }

    /**
     * determine whether a point (x,y) is inside this shape or not.
     * @param x X coordinate
     * @param y Y coordinate
     * @return True if (x,y) is inside this shape
     */
    public boolean getInside(final double x, final double y)
    {
        boolean result = true;
        // Note -10 is just an arbitrary number that is not
        // being used as one of the constants in DirectionalLine
        int lastSide = DEFAULT_LAST_SIDE;
        for (DirectionalLine line : this.lines)
        {
            int side = line.getSideThin(x, y);
            if (lastSide != DEFAULT_LAST_SIDE)
            {
                if (side != lastSide)
                {
                    result = false;
                }
            }
            lastSide = side;
        }
        return result;
    }
}
