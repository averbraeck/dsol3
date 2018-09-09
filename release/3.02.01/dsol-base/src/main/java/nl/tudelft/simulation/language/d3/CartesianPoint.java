package nl.tudelft.simulation.language.d3;

import java.awt.geom.Point2D;

import javax.vecmath.Point3f;
import javax.vecmath.Tuple3d;
import javax.vecmath.Tuple3f;

/**
 * The Point3D class with utilities to convert to point2D where the z-axis is neglected.
 * <p>
 * Copyright (c) 2002-2018  Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved.
 * <p>
 * See for project information <a href="https://simulation.tudelft.nl/"> www.simulation.tudelft.nl</a>.
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
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 */
public class CartesianPoint extends javax.vecmath.Point3d
{
    /** The default serial version UID for serializable classes. */
    private static final long serialVersionUID = 1L;

    /**
     * constructs a new CartesianPoint.
     * @param x x
     * @param y y
     * @param z z
     */
    public CartesianPoint(final double x, final double y, final double z)
    {
        super(x, y, z);
    }

    /**
     * constructs a new CartesianPoint.
     * @param xyz x,y,z
     */
    public CartesianPoint(final double[] xyz)
    {
        super(xyz);
    }

    /**
     * constructs a new CartesianPoint.
     * @param point point3d
     */
    public CartesianPoint(final javax.vecmath.Point3d point)
    {
        super(point);
    }

    /**
     * constructs a new CartesianPoint.
     * @param point point3d
     */
    public CartesianPoint(final Point3f point)
    {
        super(point);
    }

    /**
     * constructs a new CartesianPoint.
     * @param tuple tuple
     */
    public CartesianPoint(final Tuple3f tuple)
    {
        super(tuple);
    }

    /**
     * constructs a new CartesianPoint.
     * @param tuple point3d
     */
    public CartesianPoint(final Tuple3d tuple)
    {
        super(tuple);
    }

    /**
     * constructs a new CartesianPoint.
     * @param point2D a 2D point
     */
    public CartesianPoint(final Point2D point2D)
    {
        this(point2D.getX(), point2D.getY(), 0);
    }

    /**
     * constructs a new CartesianPoint.
     */
    public CartesianPoint()
    {
        super();
    }

    /**
     * returns the 2D representation of the point.
     * @return Point2D the result
     */
    public final Point2D to2D()
    {
        return new Point2D.Double(this.x, this.y);
    }

    /**
     * converts the point to a sperical point.
     * @return the spherical point
     */
    public final SphericalPoint toCartesianPoint()
    {
        return CartesianPoint.toSphericalPoint(this);
    }

    /**
     * converts a cartesian point to a sperical point.
     * @param point the cartesian point
     * @return the spherical point
     */
    public static SphericalPoint toSphericalPoint(final CartesianPoint point)
    {
        double rho = Math.sqrt(Math.pow(point.x, 2) + Math.pow(point.y, 2) + Math.pow(point.z, 2));
        double s = Math.sqrt(Math.pow(point.x, 2) + Math.pow(point.y, 2));
        double phi = Math.acos(point.z / rho);
        double theta = Math.asin(point.y / s);
        if (point.x >= 0)
        {
            theta = Math.PI - theta;
        }
        return new SphericalPoint(phi, rho, theta);
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("checkstyle:designforextension")
    public String toString()
    {
        return "CartesianPoint [x=" + this.x + ", y=" + this.y + ", z=" + this.z + "]";
    }
    
}
