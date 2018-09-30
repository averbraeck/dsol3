package nl.tudelft.simulation.language.d2;

import java.awt.geom.Point2D;

/**
 * The Angle class presents a number of mathematical utility functions on the angle. For now, the class only implements
 * static helper methods. No instances of the class should be made now.
 * <p>
 * Copyright (c) 2003-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. See for project information <a href="https://simulation.tudelft.nl/">https://simulation.tudelft.nl</a>. The
 * DSOL project is distributed under a three-clause BSD-style license, which can be found at <a href=
 * "https://simulation.tudelft.nl/dsol/3.0/license.html">https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="http://tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public final class Angle
{
    /**
     * constructs a new Angle.
     */
    private Angle()
    {
        super();
        // unreachable code
    }

    /**
     * Normalize an angle between 0 and 2*pi.
     * @param angle the angle to normalize
     * @return normalized angle
     */
    public static double normalize2Pi(final double angle)
    {
        double result = angle + 2.0d * Math.PI;
        double times = Math.floor(result / (2.0d * Math.PI));
        result -= times * 2.0d * Math.PI;
        return result;
    }

    /**
     * Normalize an angle between -pi and +pi.
     * @param angle the angle to normalize
     * @return normalized angle
     */
    public static double normalizePi(final double angle)
    {
        double result = angle + 2.0d * Math.PI;
        double times = Math.floor((result + Math.PI) / (2.0d * Math.PI));
        result -= times * 2.0d * Math.PI;
        return result;
    }

    /**
     * Return the 2-pi normalized angle when making an arc from p0 to p1.
     * @param p0 first point
     * @param p1 second point
     * @return the normalized angle
     */
    public static double angle(final Point2D p0, final Point2D p1)
    {
        return normalize2Pi(Math.atan2(p1.getY() - p0.getY(), p1.getX() - p0.getX()));
    }
}
