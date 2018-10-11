package nl.tudelft.simulation.language.d2;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Point2d;

/**
 * DirectionalShape is used to create a shape out of vertices and find out whether a certain point is inside or outside
 * of the shape.
 * <p>
 * Copyright (c) 2003-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. See for project information <a href="https://simulation.tudelft.nl/" target="_blank">
 * https://simulation.tudelft.nl</a>. The DSOL project is distributed under a three-clause BSD-style license, which can
 * be found at <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
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
     * @param x double; X coordinate
     * @param y double; Y coordinate
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
     * @param x double; X coordinate
     * @param y double; Y coordinate
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
