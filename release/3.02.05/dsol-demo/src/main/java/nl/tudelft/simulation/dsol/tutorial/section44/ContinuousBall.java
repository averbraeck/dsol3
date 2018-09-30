package nl.tudelft.simulation.dsol.tutorial.section44;

import java.awt.geom.Point2D;
import java.rmi.RemoteException;

import javax.naming.NamingException;

import nl.tudelft.simulation.dsol.logger.SimLogger;
import nl.tudelft.simulation.dsol.simulators.DESSSimulatorInterface;
import nl.tudelft.simulation.jstats.streams.StreamInterface;
import nl.tudelft.simulation.language.d3.DirectedPoint;

/**
 * An extension of Ball.
 * @author peter
 */
public class ContinuousBall extends Ball
{

    /** the positioner. */
    private Positioner positioner = null;

    /** the simulator to use. */
    private DESSSimulatorInterface.TimeDouble simulator = null;

    /**
     * constructs a new Ball.
     * @param simulator the simulator
     * @throws RemoteException on network exception
     * @throws NamingException on animation error
     */
    public ContinuousBall(final DESSSimulatorInterface.TimeDouble simulator) throws RemoteException, NamingException
    {
        super();
        this.simulator = simulator;
        this.positioner = new Positioner(simulator);
        new BallAnimation2D(this, simulator);
        new BallAnimation3D(this, simulator);
        try
        {
            this.next();
        }
        catch (RemoteException exception)
        {
            SimLogger.always().error(exception);
        }
    }

    /** {@inheritDoc} */
    @Override
    public DirectedPoint getLocation() throws RemoteException
    {
        double distance = this.positioner.y(this.simulator.getSimulatorTime())[0];
        double x = Math.cos(this.rotZ) * distance + this.origin.x;
        double y = Math.sin(this.rotZ) * distance + this.origin.y;
        if (Math.abs(x - this.origin.x) > Math.abs(this.destination.x - this.origin.x)
                || Math.abs(y - this.origin.y) > Math.abs(this.destination.y - this.origin.y))
        {
            this.next();
        }
        return new DirectedPoint(new Point2D.Double(x, y), this.rotZ);
    }

    /**
     * next move
     * @throws RemoteException on network failure
     */
    public void next() throws RemoteException
    {
        StreamInterface stream = this.simulator.getReplication().getStream("default");
        this.origin = this.destination;
        this.positioner.setValue(0);
        this.destination = new DirectedPoint(-100 + stream.nextInt(0, 200), -100 + stream.nextInt(0, 200), 0);
        this.rotZ = (this.destination.y - this.origin.y) / (this.destination.x - this.origin.x);
    }
}
