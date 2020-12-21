package nl.tudelft.simulation.dsol.tutorial.section44;

import java.rmi.RemoteException;

import javax.naming.NamingException;

import org.djutils.draw.point.DirectedPoint3d;

import nl.tudelft.simulation.dsol.simulators.DESSSimulatorInterface;
import nl.tudelft.simulation.jstats.streams.StreamInterface;

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
     * @param simulator DESSSimulatorInterface.TimeDouble; the simulator
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
            this.simulator.getLogger().always().error(exception);
        }
    }

    /** {@inheritDoc} */
    @Override
    public DirectedPoint3d getLocation() throws RemoteException
    {
        double distance = this.positioner.y(this.simulator.getSimulatorTime())[0];
        double x = Math.cos(this.rotZ) * distance + this.origin.getX();
        double y = Math.sin(this.rotZ) * distance + this.origin.getY();
        if (Math.abs(x - this.origin.getX()) > Math.abs(this.destination.getX() - this.origin.getX())
                || Math.abs(y - this.origin.getY()) > Math.abs(this.destination.getY() - this.origin.getY()))
        {
            this.next();
        }
        return new DirectedPoint3d(x, y, this.rotZ);
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
        this.destination = new DirectedPoint3d(-100 + stream.nextInt(0, 200), -100 + stream.nextInt(0, 200), 0);
        this.rotZ = (this.destination.getY() - this.origin.getY()) / (this.destination.getX() - this.origin.getX());
    }
}
