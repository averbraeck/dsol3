package nl.tudelft.simulation.dsol.tutorial.section44;

import javax.media.j3d.BoundingSphere;
import javax.media.j3d.Bounds;
import javax.vecmath.Point3d;

import nl.tudelft.simulation.dsol.animation.Locatable;
import nl.tudelft.simulation.language.d3.DirectedPoint;

/**
 * A Ball <br>
 * copyright (c) 2002-2018 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @version 1.0 Mar 3, 2004 <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 */
public abstract class Ball implements Locatable
{
    /** the number of created balls. */
    private static int number = 0;

    /** the radius of the ball. */
    public static final double RADIUS = 5.0;

    /** the name of the ball. */
    private String name = "";

    /** the origin. */
    protected DirectedPoint origin = new DirectedPoint();

    /** the destination. */
    protected DirectedPoint destination = new DirectedPoint();

    /** the rotation. */
    protected double rotZ = 0.0;

    /**
     * constructs a new Ball.
     */
    public Ball()
    {
        super();
        this.rotZ = 2 * Math.PI * Math.random();
        Ball.number++;
        this.name = "" + Ball.number;
    }

    /** {@inheritDoc} */
    @Override
    public Bounds getBounds()
    {
        return new BoundingSphere(new Point3d(0, 0, 0), Ball.RADIUS);
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return this.name;
    }
}
