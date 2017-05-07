package nl.tudelft.simulation.examples.dsol.animation.continuous;

import java.rmi.RemoteException;

import javax.naming.NamingException;

import nl.tudelft.simulation.dsol.simulators.DESSSimulatorInterface;
import nl.tudelft.simulation.examples.dsol.animation.BallAnimation;
import nl.tudelft.simulation.jstats.streams.StreamInterface;
import nl.tudelft.simulation.language.d3.CartesianPoint;
import nl.tudelft.simulation.language.d3.DirectedPoint;

/**
 * @author peter
 */
public class Ball extends nl.tudelft.simulation.examples.dsol.animation.Ball
{
    /** the positioner. */
    private Positioner positioner = null;

    /** the origin. */
    private CartesianPoint origin = new CartesianPoint(0, 0, 0);

    /** the destination. */
    private CartesianPoint destination = new CartesianPoint(0, 0, 0);

    /** the rotation. */
    private double angle = 0.0;

    /** the simulator to use. */
    private DESSSimulatorInterface.TimeDouble simulator = null;

    /**
     * constructs a new Ball.
     * @param simulator the simulator
     * @throws RemoteException on network exception
     * @throws NamingException on animation error
     */
    public Ball(final DESSSimulatorInterface.TimeDouble simulator) throws RemoteException, NamingException
    {
        super();
        this.simulator = simulator;
        this.positioner = new Positioner(simulator);
        new BallAnimation(this, simulator);
        try
        {
            this.next();
        }
        catch (RemoteException exception)
        {
            exception.printStackTrace();
        }
    }

    /** {@inheritDoc} */
    @Override
    public DirectedPoint getLocation() throws RemoteException
    {
        double x = Math.cos(this.angle) * this.positioner.y(this.simulator.getSimulatorTime().get())[1] + this.origin.x;
        double y = Math.sin(this.angle) * this.positioner.y(this.simulator.getSimulatorTime().get())[1] + this.origin.y;
        if (Math.abs(x - this.origin.x) > Math.abs(this.destination.x - this.origin.x)
                || Math.abs(y - this.origin.y) > Math.abs(this.destination.y - this.origin.y))
        {
            this.next();
        }
        return new DirectedPoint(x, y, 0, 0.0, 0.0, this.theta);
    }

    /**
     * next move.
     * @throws RemoteException on network failure
     */
    public void next() throws RemoteException
    {
        StreamInterface stream = this.simulator.getReplication().getStream("default");
        this.origin = this.destination;
        this.positioner.setValue(0);
        this.destination = new CartesianPoint(-100 + stream.nextInt(0, 200), -100 + stream.nextInt(0, 200), 0);
        this.angle = (this.destination.y - this.origin.y) / (this.destination.x - this.origin.x);
    }
}
