package nl.tudelft.simulation.language.d3;

import java.awt.geom.Point2D;

import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Tuple3d;
import javax.vecmath.Tuple3f;

/**
 * The location object.
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
 * @version $Revision: 1.2 $ $Date: 2009/10/21 07:32:42 $
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 */
public class DirectedPoint extends CartesianPoint
{
    /** The default serial version UID for serializable classes. */
    private static final long serialVersionUID = 1L;

    /** rotX is the rotX. */
    private double rotX = 0.0;

    /** rotY is the rotY-value. */
    private double rotY = 0.0;

    /** rotZ is the rotZ-value. */
    private double rotZ = 0.0;

    /**
     * constructs a new DirectedPoint.
     */
    public DirectedPoint()
    {
        super();
    }

    /**
     * constructs a new DirectedPoint.
     * @param x the x value
     * @param y the y value
     * @param z the z value
     */
    public DirectedPoint(final double x, final double y, final double z)
    {
        super(x, y, z);
    }

    /**
     * constructs a new DirectedPoint.
     * @param x the x value
     * @param y the y value
     * @param z the z value
     * @param rotX rotX
     * @param rotY rotY
     * @param rotZ rotZ
     */
    public DirectedPoint(final double x, final double y, final double z, final double rotX, final double rotY,
            final double rotZ)
    {
        super(x, y, z);
        this.rotX = rotX;
        this.rotY = rotY;
        this.rotZ = rotZ;
    }

    /**
     * constructs a new DirectedPoint.
     * @param point2D the point
     * @param rotZ rotZ
     */
    public DirectedPoint(final Point2D point2D, final double rotZ)
    {
        super(point2D);
        this.rotZ = rotZ;
    }

    /**
     * constructs a new DirectedPoint.
     * @param xyz the xyx value
     */
    public DirectedPoint(final double[] xyz)
    {
        super(xyz);
    }

    /**
     * constructs a new DirectedPoint.
     * @param cartesianPoint the cartesianPoint
     */
    public DirectedPoint(final Point3d cartesianPoint)
    {
        super(cartesianPoint);
    }

    /**
     * constructs a new DirectedPoint.
     * @param sphericalPoint the sphericalPoint
     */
    public DirectedPoint(final SphericalPoint sphericalPoint)
    {
        this(sphericalPoint.toCartesianPoint());
    }

    /**
     * constructs a new DirectedPoint.
     * @param location the location
     */
    public DirectedPoint(final DirectedPoint location)
    {
        super(location);
        this.rotY = location.rotY;
        this.rotZ = location.rotZ;
        this.rotX = location.rotX;
    }

    /**
     * constructs a new DirectedPoint.
     * @param point2D the point
     */
    public DirectedPoint(final Point2D point2D)
    {
        super(point2D);
    }

    /**
     * constructs a new DirectedPoint.
     * @param point the point
     */
    public DirectedPoint(final Point3f point)
    {
        super(point);
    }

    /**
     * constructs a new DirectedPoint.
     * @param tuple the point
     */
    public DirectedPoint(final Tuple3d tuple)
    {
        super(tuple);
    }

    /**
     * constructs a new DirectedPoint.
     * @param tuple the point
     */
    public DirectedPoint(final Tuple3f tuple)
    {
        super(tuple);
    }

    /**
     * returns ther rotY-value.
     * @return double
     */
    public final double getRotY()
    {
        return this.rotY;
    }

    /**
     * sets the rotY.
     * @param rotY the rotY-value
     */
    public final void setRotY(final double rotY)
    {
        this.rotY = rotY;
    }

    /**
     * returns the rotZ value.
     * @return double
     */
    public final double getRotZ()
    {
        return this.rotZ;
    }

    /**
     * sets the rotZ value.
     * @param rotZ the rotZ-value
     */
    public final void setRotZ(final double rotZ)
    {
        this.rotZ = rotZ;
    }

    /**
     * returns the rotX value.
     * @return double
     */
    public final double getRotX()
    {
        return this.rotX;
    }

    /**
     * sets the rotX.
     * @param rotX rotX-value
     */
    public final void setRotX(final double rotX)
    {
        this.rotX = rotX;
    }

    /** {@inheritDoc} */
    @Override
    public final Object clone()
    {
        return new DirectedPoint(this.x, this.y, this.z, this.rotX, this.rotY, this.rotZ);
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("checkstyle:designforextension")
    public int hashCode()
    {
        final int prime = 31;
        int result = super.hashCode();
        long temp;
        temp = Double.doubleToLongBits(this.rotX);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(this.rotY);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(this.rotZ);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings({"checkstyle:designforextension", "checkstyle:needbraces"})
    public boolean equals(final Object obj)
    {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        DirectedPoint other = (DirectedPoint) obj;
        if (Double.doubleToLongBits(this.rotX) != Double.doubleToLongBits(other.rotX))
            return false;
        if (Double.doubleToLongBits(this.rotY) != Double.doubleToLongBits(other.rotY))
            return false;
        if (Double.doubleToLongBits(this.rotZ) != Double.doubleToLongBits(other.rotZ))
            return false;
        return true;
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("checkstyle:designforextension")
    public final String toString()
    {
        return "[position=" + super.toString() + ";RotX=" + this.rotX + ";RotY=" + this.rotY + ";RotZ=" + this.rotZ
                + "]";
    }

}