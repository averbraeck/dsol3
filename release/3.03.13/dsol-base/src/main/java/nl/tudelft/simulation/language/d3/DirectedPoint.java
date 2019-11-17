package nl.tudelft.simulation.language.d3;

import java.awt.geom.Point2D;

import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Tuple3d;
import javax.vecmath.Tuple3f;

/**
 * The location object.
 * <p>
 * Copyright (c) 2002-2019 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 */
public class DirectedPoint extends CartesianPoint
{
    /** */
    private static final long serialVersionUID = 20191116L;

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
     * @param x double; the x value
     * @param y double; the y value
     * @param z double; the z value
     */
    public DirectedPoint(final double x, final double y, final double z)
    {
        super(x, y, z);
    }

    /**
     * constructs a new DirectedPoint.
     * @param x double; the x value
     * @param y double; the y value
     * @param z double; the z value
     * @param rotX double; rotX
     * @param rotY double; rotY
     * @param rotZ double; rotZ
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
     * @param point2D Point2D; the point
     * @param rotZ double; rotZ
     */
    public DirectedPoint(final Point2D point2D, final double rotZ)
    {
        super(point2D);
        this.rotZ = rotZ;
    }

    /**
     * constructs a new DirectedPoint.
     * @param xyz double[]; the xyx value
     */
    public DirectedPoint(final double[] xyz)
    {
        super(xyz);
    }

    /**
     * constructs a new DirectedPoint.
     * @param cartesianPoint Point3d; the cartesianPoint
     */
    public DirectedPoint(final Point3d cartesianPoint)
    {
        super(cartesianPoint);
    }

    /**
     * constructs a new DirectedPoint.
     * @param sphericalPoint SphericalPoint; the sphericalPoint
     */
    public DirectedPoint(final SphericalPoint sphericalPoint)
    {
        this(sphericalPoint.toCartesianPoint());
    }

    /**
     * constructs a new DirectedPoint.
     * @param location DirectedPoint; the location
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
     * @param point2D Point2D; the point
     */
    public DirectedPoint(final Point2D point2D)
    {
        super(point2D);
    }

    /**
     * constructs a new DirectedPoint.
     * @param point Point3f; the point
     */
    public DirectedPoint(final Point3f point)
    {
        super(point);
    }

    /**
     * constructs a new DirectedPoint.
     * @param tuple Tuple3d; the point
     */
    public DirectedPoint(final Tuple3d tuple)
    {
        super(tuple);
    }

    /**
     * constructs a new DirectedPoint.
     * @param tuple Tuple3f; the point
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
     * @param rotY double; the rotY-value
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
     * @param rotZ double; the rotZ-value
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
     * @param rotX double; rotX-value
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

    /**
     * This extra equals method is necessary because Tuple3d (stupidly) implements equals(Tuple3d) which will 'win' from
     * DirectedPoint.equals(DirectedPoint) because it is more 'specific'. This means that the rotX, rotY and rotZ fields are not
     * checked in equals when two directedPoints are compared with each other.
     * @param obj the directed point to compare with
     * @return whether the two directed points are equal or not
     */
    public boolean equals(final DirectedPoint obj)
    {
        return this.equals((Object) obj);
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(final Tuple3d obj)
    {
        return this.equals((Object) obj);
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
        return "[position=" + super.toString() + ";RotX=" + this.rotX + ";RotY=" + this.rotY + ";RotZ=" + this.rotZ + "]";
    }

}
