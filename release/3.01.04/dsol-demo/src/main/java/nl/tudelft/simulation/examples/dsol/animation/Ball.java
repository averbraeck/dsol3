package nl.tudelft.simulation.examples.dsol.animation;

import java.rmi.RemoteException;

import javax.media.j3d.BoundingSphere;
import javax.media.j3d.Bounds;
import javax.vecmath.Point3d;

import nl.tudelft.simulation.dsol.animation.Locatable;

/**
 * A Ball <br>
 * (c) copyright 2003 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/gpl.html">General Public License (GPL) </a>, no warranty <br>
 * @version 1.0 Mar 3, 2004 <br>
 * @author <a href="http://www.simulation.tudelft.nl/people/jacobs.html">Peter Jacobs </a>
 */
public abstract class Ball implements Locatable
{
    /** the number of created balls. */
    private static int number = 0;

    /** the radius of the ball. */
    public static final double RADIUS = 5.0;

    /** the angle of the ball. */
    protected double theta = 0.0;

    /** the name of the ball. */
    private String name = "";

    /**
     * constructs a new Ball.
     */
    public Ball()
    {
        super();
        this.theta = 2 * Math.PI * Math.random();
        Ball.number++;
        this.name = "" + Ball.number;
    }

    /** {@inheritDoc} */
    @Override
    public Bounds getBounds() throws RemoteException
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
