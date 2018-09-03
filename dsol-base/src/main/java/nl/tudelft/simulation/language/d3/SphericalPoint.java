package nl.tudelft.simulation.language.d3;

/**
 * A sphericalpoint as defined in <a href="http://mathworld.wolfram.com/SphericalCoordinates.html">
 * http://mathworld.wolfram.com/SphericalCoordinates.html </a>.
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
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @version $Revision: 1.2 $ $Date: 2009/10/21 07:32:42 $
 * @since 1.5
 */
public class SphericalPoint
{
    /** radius. */
    private double radius = 0.0;

    /** phi. */
    private double phi = 0.0;

    /** theta. */
    private double theta = 0.0;

    /**
     * constructs a new SphericalPoint.
     * @param phi phi
     * @param radius radius
     * @param theta theta
     */
    public SphericalPoint(final double radius, final double phi, final double theta)
    {
        super();
        this.phi = phi;
        this.radius = radius;
        this.theta = theta;
    }

    /**
     * @return phi
     */
    public final double getPhi()
    {
        return this.phi;
    }

    /**
     * @return radius
     */
    public final double getRadius()
    {
        return this.radius;
    }

    /**
     * @return theta
     */
    public final double getTheta()
    {
        return this.theta;
    }

    /**
     * converts a sphericalpoint to a cartesian point.
     * @return the cartesian point
     */
    public final CartesianPoint toCartesianPoint()
    {
        return SphericalPoint.toCartesianPoint(this);
    }

    /**
     * converts a sphericalpoint to a cartesian point.
     * @param point the sphericalpoint
     * @return the cartesian point
     */
    public static CartesianPoint toCartesianPoint(final SphericalPoint point)
    {
        double x = point.radius * Math.sin(point.phi) * Math.cos(point.theta);
        double y = point.radius * Math.sin(point.phi) * Math.sin(point.theta);
        double z = point.radius * Math.cos(point.phi);
        return new CartesianPoint(x, y, z);
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("checkstyle:designforextension")
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(this.phi);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(this.radius);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(this.theta);
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
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SphericalPoint other = (SphericalPoint) obj;
        if (Double.doubleToLongBits(this.phi) != Double.doubleToLongBits(other.phi))
            return false;
        if (Double.doubleToLongBits(this.radius) != Double.doubleToLongBits(other.radius))
            return false;
        if (Double.doubleToLongBits(this.theta) != Double.doubleToLongBits(other.theta))
            return false;
        return true;
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("checkstyle:designforextension")
    public String toString()
    {
        return "SphericalPoint [radius=" + this.radius + ", phi=" + this.phi + ", theta=" + this.theta + "]";
    }

}
