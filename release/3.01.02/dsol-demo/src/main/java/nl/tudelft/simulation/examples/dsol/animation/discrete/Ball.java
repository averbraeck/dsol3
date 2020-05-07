package nl.tudelft.simulation.examples.dsol.animation.discrete;

import java.net.URL;
import java.rmi.RemoteException;

import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.animation.D2.SingleImageRenderable;
import nl.tudelft.simulation.dsol.formalisms.eventscheduling.SimEvent;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;
import nl.tudelft.simulation.jstats.distributions.DistNormal;
import nl.tudelft.simulation.jstats.streams.StreamInterface;
import nl.tudelft.simulation.language.d3.CartesianPoint;
import nl.tudelft.simulation.language.d3.DirectedPoint;
import nl.tudelft.simulation.language.io.URLResource;

/**
 * @author peter
 */
public class Ball extends nl.tudelft.simulation.examples.dsol.animation.Ball
{
    /** the origin. */
    private CartesianPoint origin = new CartesianPoint(0, 0, 0);

    /** the destination. */
    private CartesianPoint destination = new CartesianPoint(0, 0, 0);

    /** the simulator. */
    private DEVSSimulatorInterface.TimeDouble simulator = null;

    /** the start time. */
    private double startTime = Double.NaN;

    /** the stop time. */
    private double stopTime = Double.NaN;

    /**
     * constructs a new Ball.
     * @param simulator the simulator
     * @throws RemoteException on remote failure
     * @throws SimRuntimeException on schedule failure
     */
    public Ball(final DEVSSimulatorInterface.TimeDouble simulator) throws RemoteException, SimRuntimeException
    {
        super();
        this.simulator = simulator;
        URL image = URLResource.getResource("/nl/tudelft/simulation/examples/dsol/animation/images/customer.jpg");
        new SingleImageRenderable(this, simulator, image);
        this.next();
    }

    /**
     * next movement
     * @throws RemoteException on network failure
     * @throws SimRuntimeException on simulation failure
     */
    private void next() throws RemoteException, SimRuntimeException
    {
        StreamInterface stream = this.simulator.getReplication().getStream("default");
        this.origin = this.destination;
        this.destination = new CartesianPoint(-100 + stream.nextInt(0, 200), -100 + stream.nextInt(0, 200), 0);
        this.startTime = this.simulator.getSimulatorTime();
        this.stopTime = this.startTime + Math.abs(new DistNormal(stream, 9, 1.8).draw());
        this.simulator.scheduleEvent(new SimEvent(this.stopTime, this, this, "next", null));
    }

    /** {@inheritDoc} */
    @Override
    public DirectedPoint getLocation() throws RemoteException
    {
        double fraction = (this.simulator.getSimulatorTime() - this.startTime) / (this.stopTime - this.startTime);
        double x = this.origin.x + (this.destination.x - this.origin.x) * fraction;
        double y = this.origin.y + (this.destination.y - this.origin.y) * fraction;
        return new DirectedPoint(x, y, 0, 0.0, 0.0, this.theta);
    }
}