package nl.tudelft.simulation.dsol.formalisms.flow;

import static org.junit.Assert.assertEquals;

import java.io.Serializable;

import org.junit.Test;

import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.experiment.SingleReplication;
import nl.tudelft.simulation.dsol.model.AbstractDSOLModel;
import nl.tudelft.simulation.dsol.model.DSOLModel;
import nl.tudelft.simulation.dsol.simtime.dist.DistContinuousSimulationTime;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulator;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;
import nl.tudelft.simulation.jstats.distributions.DistExponential;
import nl.tudelft.simulation.jstats.streams.MersenneTwister;
import nl.tudelft.simulation.jstats.streams.StreamInterface;

/**
 * FlowTest tests the flow objects, such as Station, Seize, Delay, Release.
 * <p>
 * Copyright (c) 2021-2021 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/dsol/manual/" target="_blank">DSOL Manual</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">DSOL License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class FlowTest
{
    /**
     * Test the Delay.
     */
    @Test
    public void delayTest()
    {
        DEVSSimulatorInterface.TimeDouble simulator = new DEVSSimulator.TimeDouble("sim");
        DSOLModel.TimeDouble<DEVSSimulatorInterface.TimeDouble> model = makeModelDouble(simulator);
        SingleReplication.TimeDouble replication = new SingleReplication.TimeDouble("replication", 0.0, 0.0, 100.0);
        simulator.initialize(model, replication);
        StreamInterface stream = new MersenneTwister(10L);
        DistContinuousSimulationTime.TimeDouble delayDistribution =
                new DistContinuousSimulationTime.TimeDouble(new DistExponential(stream, 10.0));
        Delay.TimeDouble delay = new Delay.TimeDouble("delay", simulator, delayDistribution);
        assertEquals("delay", delay.getSourceId());
        assertEquals(simulator, delay.getSimulator());

        Departure.TimeDouble departure = new Departure.TimeDouble("departure", simulator);
        delay.setDestination(departure);
        assertEquals(departure, delay.getDestination());
        int nrEvents = simulator.getEventList().size();
        String object = "abc";
        delay.receiveObject(object);
        assertEquals(nrEvents + 1, simulator.getEventList().size());
    }

    /**
     * @param simulator the simulator
     * @return DSOLModel.TimeDouble
     */
    private DSOLModel.TimeDouble<DEVSSimulatorInterface.TimeDouble> makeModelDouble(
            final DEVSSimulatorInterface.TimeDouble simulator)
    {
        return new AbstractDSOLModel.TimeDouble<DEVSSimulatorInterface.TimeDouble>(simulator)
        {
            /** */
            private static final long serialVersionUID = 1L;

            @Override
            public void constructModel() throws SimRuntimeException
            {
                //
            }

            @Override
            public Serializable getSourceId()
            {
                return "model";
            }
        };
    }
}
