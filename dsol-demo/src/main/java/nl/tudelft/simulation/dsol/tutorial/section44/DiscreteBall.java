package nl.tudelft.simulation.dsol.tutorial.section44;

import java.awt.geom.Point2D;
import java.net.URL;
import java.rmi.RemoteException;

import javax.naming.NamingException;

import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.animation.D2.SingleImageRenderable;
import nl.tudelft.simulation.dsol.animation.interpolation.InterpolationInterface;
import nl.tudelft.simulation.dsol.animation.interpolation.LinearInterpolation;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;
import nl.tudelft.simulation.jstats.distributions.DistNormal;
import nl.tudelft.simulation.jstats.streams.StreamInterface;
import nl.tudelft.simulation.language.d3.DirectedPoint;
import nl.tudelft.simulation.language.io.URLResource;

/**
 * <p>
 * Copyright (c) 2002-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved.
 * <p>
 * See for project information <a href="https://simulation.tudelft.nl/"> www.simulation.tudelft.nl</a>.
 * <p>
 * @version Aug 15, 2014 <br>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class DiscreteBall extends Ball
{
    /** the simulator. */
    private DEVSSimulatorInterface.TimeDouble simulator = null;

    /** the start time. */
    private double startTime = Double.NaN;

    /** the stop time. */
    private double stopTime = Double.NaN;

    /** the interpolator. */
    private InterpolationInterface interpolator = null;

    /**
     * constructs a new DiscreteBall.
     * @param simulator the simulator
     * @throws SimRuntimeException on schedule failure
     * @throws NamingException on animation error
     * @throws RemoteException on network error for the animation listener
     */
    public DiscreteBall(final DEVSSimulatorInterface.TimeDouble simulator)
            throws SimRuntimeException, NamingException, RemoteException
    {
        super();
        this.simulator = simulator;
        URL image = URLResource.getResource("/nl/tudelft/simulation/dsol/" + "tutorial/section44/images/customer.jpg");
        new SingleImageRenderable(this, simulator, image);
        this.next();
    }

    /**
     * next movement.
     * @throws SimRuntimeException on simulation failure
     */
    private void next() throws SimRuntimeException
    {
        StreamInterface stream = this.simulator.getReplication().getStream("default");
        this.origin = this.destination;
        this.rotZ = 2 * Math.PI * Math.random();
        this.destination = new DirectedPoint(
                new Point2D.Double(-100 + stream.nextInt(0, 200), -100 + stream.nextInt(0, 200)), this.rotZ);
        this.startTime = this.simulator.getSimulatorTime();
        this.stopTime = this.startTime + Math.abs(new DistNormal(stream, 9, 1.8).draw());
        this.interpolator = new LinearInterpolation(this.startTime, this.stopTime, this.origin, this.destination);
        this.simulator.scheduleEventAbs(this.stopTime, this, this, "next", null);
    }

    /** {@inheritDoc} */
    @Override
    public DirectedPoint getLocation() throws RemoteException
    {
        if (this.interpolator != null)
        {
            return this.interpolator.getLocation(this.simulator.getSimulatorTime());
        }
        return this.origin;
    }
}
