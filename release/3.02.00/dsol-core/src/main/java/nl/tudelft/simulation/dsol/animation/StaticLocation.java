package nl.tudelft.simulation.dsol.animation;

import java.awt.geom.Point2D;

import javax.media.j3d.Bounds;

import nl.tudelft.simulation.language.d3.DirectedPoint;

/**
 * A StaticLocation <br>
 * (c) 2002-2018 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:36:45 $
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 */
public class StaticLocation extends DirectedPoint implements Locatable
{
    /** The default serial version UID for serializable classes. */
    private static final long serialVersionUID = 1L;

    /** the bounds. */
    private Bounds bounds = null;

    /**
     * constructs a new StaticLocation.
     * @param x the x location
     * @param y the y location
     * @param z the z location
     * @param theta theta
     * @param phi phi
     * @param rho rho
     * @param bounds the bounds
     */
    public StaticLocation(final double x, final double y, final double z, final double theta, final double phi,
            final double rho, final Bounds bounds)
    {
        super(x, y, z, theta, phi, rho);
        this.bounds = bounds;
    }

    /**
     * constructs a new StaticLocation.
     * @param point2D the point2d
     * @param rotZ the rotation in the xy plane
     * @param bounds the bounds
     */
    public StaticLocation(final Point2D point2D, final double rotZ, final Bounds bounds)
    {
        super(point2D, rotZ);
        this.bounds = bounds;
    }

    /**
     * constructs a new StaticLocation.
     * @param location the location
     * @param bounds the bounds
     */
    public StaticLocation(final DirectedPoint location, final Bounds bounds)
    {
        super(location);
        this.bounds = bounds;
    }

    /** {@inheritDoc} */
    @Override
    public final DirectedPoint getLocation()
    {
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public final Bounds getBounds()
    {
        return this.bounds;
    }

}
