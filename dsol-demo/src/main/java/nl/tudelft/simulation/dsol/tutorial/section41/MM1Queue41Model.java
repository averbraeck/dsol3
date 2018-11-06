package nl.tudelft.simulation.dsol.tutorial.section41;

import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.formalisms.Resource;
import nl.tudelft.simulation.dsol.formalisms.flow.Delay;
import nl.tudelft.simulation.dsol.formalisms.flow.Generator;
import nl.tudelft.simulation.dsol.formalisms.flow.Release;
import nl.tudelft.simulation.dsol.formalisms.flow.Seize;
import nl.tudelft.simulation.dsol.formalisms.flow.StationInterface;
import nl.tudelft.simulation.dsol.formalisms.flow.statistics.Utilization;
import nl.tudelft.simulation.dsol.model.AbstractDSOLModel;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simtime.dist.DistContinuousSimTime;
import nl.tudelft.simulation.dsol.simtime.dist.DistContinuousTime;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulator;
import nl.tudelft.simulation.dsol.statistics.Tally;
import nl.tudelft.simulation.jstats.distributions.DistConstant;
import nl.tudelft.simulation.jstats.distributions.DistDiscreteConstant;
import nl.tudelft.simulation.jstats.distributions.DistExponential;
import nl.tudelft.simulation.jstats.streams.MersenneTwister;
import nl.tudelft.simulation.jstats.streams.StreamInterface;

/**
 * The M/M/1 example as published in Simulation Modeling and Analysis by A.M. Law &amp; W.D. Kelton section 1.4 and 2.4.
 * <p>
 * Copyright (c) 2002-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 */
public class MM1Queue41Model extends AbstractDSOLModel.TimeDouble<DEVSSimulator.TimeDouble>
{
    /**
     * @param simulator the simulator
     */
    public MM1Queue41Model(final DEVSSimulator.TimeDouble simulator)
    {
        super(simulator);
    }

    /** The default serial version UID for serializable classes. */
    private static final long serialVersionUID = 1L;

    /** tally dN. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    Tally<Double, Double, SimTimeDouble> dN;

    /** tally qN. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    Tally<Double, Double, SimTimeDouble> qN;

    /** utilization uN. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    Utilization<Double, Double, SimTimeDouble> uN;

    /** {@inheritDoc} */
    @Override
    public final void constructModel() throws SimRuntimeException
    {
        StreamInterface defaultStream = new MersenneTwister();

        // The Generator
        Generator.TimeDouble generator = new Generator.TimeDouble(this.simulator, Object.class, null);
        generator.setInterval(new DistContinuousTime.TimeDouble(new DistExponential(defaultStream, 1.0)));
        generator.setStartTime(new DistContinuousSimTime.TimeDouble(new DistConstant(defaultStream, 0.0)));
        generator.setBatchSize(new DistDiscreteConstant(defaultStream, 1));
        generator.setMaxNumber(1000);

        // The queue, the resource and the release
        Resource<Double, Double, SimTimeDouble> resource = new Resource<>(this.simulator, 1.0);

        // created a resource
        StationInterface.TimeDouble queue = new Seize.TimeDouble(this.simulator, resource);
        StationInterface.TimeDouble release = new Release.TimeDouble(this.simulator, resource, 1.0);

        // The server
        DistContinuousTime.TimeDouble serviceTime = new DistContinuousTime.TimeDouble(new DistExponential(defaultStream, 0.5));
        StationInterface.TimeDouble server = new Delay.TimeDouble(this.simulator, serviceTime);

        // The flow
        generator.setDestination(queue);
        queue.setDestination(server);
        server.setDestination(release);

        // Statistics
        try
        {
            this.dN = new Tally<>("d(n)", this.simulator, queue, Seize.DELAY_TIME);
            this.qN = new Tally<>("q(n)", this.simulator, queue, Seize.QUEUE_LENGTH_EVENT);
            this.uN = new Utilization<>("u(n)", this.simulator, server);
        }
        catch (Exception exception)
        {
            throw new SimRuntimeException(exception);
        }
    }
}
