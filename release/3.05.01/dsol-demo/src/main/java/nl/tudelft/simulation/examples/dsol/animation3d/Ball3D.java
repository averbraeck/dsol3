package nl.tudelft.simulation.examples.dsol.animation3d;

import java.rmi.RemoteException;

import javax.naming.NamingException;

import org.djutils.draw.point.DirectedPoint3d;

import nl.tudelft.simulation.dsol.simulators.DESSSimulatorInterface;
import nl.tudelft.simulation.jstats.streams.StreamInterface;
import nl.tudelft.simulation.language.d3.CartesianPoint;

/**
 * An extension of Ball.
 * <p>
 * Copyright (c) 2003-2021 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author peter
 */
public class Ball3D extends Ball
{
    /** the positioner. */
    private Positioner3D positioner = null;

    /** the origin. */
    private CartesianPoint origin = new CartesianPoint(0,0,0);

    /** the destination. */
    private CartesianPoint destination = new CartesianPoint(0,0,0);

    /** the rotation. */
    private double angle = 0.0;

    /** the simulator to use. */
    private DESSSimulatorInterface.TimeDouble simulator = null;

    /**
     * constructs a new Ball.
     * @param simulator DESSSimulatorInterface.TimeDouble; the simulator
     * @throws RemoteException on network exception
     * @throws NamingException on error
     */
    public Ball3D(final DESSSimulatorInterface.TimeDouble simulator) throws RemoteException, NamingException
    {
        super();
        this.simulator = simulator;
        this.positioner = new Positioner3D(simulator);
        new BallAnimation(this, simulator);
        new BallAnimation3D(this, simulator);
        try
        {
            this.next();
        }
        catch (RemoteException exception)
        {
            simulator.getLogger().always().error(exception);
        }
    }

    /** {@inheritDoc} */
    @Override
    public DirectedPoint3d getLocation() throws RemoteException
    {
        double x = Math.cos(this.angle) * this.positioner.y(this.simulator.getSimulatorTime())[0] + this.origin.getX();
        double y = Math.sin(this.angle) * this.positioner.y(this.simulator.getSimulatorTime())[0] + this.origin.getY();
        if (Math.abs(x - this.origin.getX()) > Math.abs(this.destination.getX() - this.origin.getX())
                || Math.abs(y - this.origin.getY()) > Math.abs(this.destination.getY() - this.origin.getY()))
        {
            this.next();
        }
        return new DirectedPoint3d(x, y, 0.0, this.theta, 0.0, 0.0);
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
        this.angle = (this.destination.getY() - this.origin.getY()) / (this.destination.getX() - this.origin.getX());
    }

}
