package nl.tudelft.simulation.dsol.statistics;

import java.util.ArrayList;
import java.util.List;

import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.model.DSOLModel;
import nl.tudelft.simulation.dsol.model.inputparameters.InputParameterMap;
import nl.tudelft.simulation.dsol.model.outputstatistics.OutputStatistic;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;

/**
 * DummyModel acts as an 'empty' DSOL model for the statistics tests.
 * <p>
 * Copyright (c) 2019-2019 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://djunits.org/docs/license.html">DJUNITS License</a>.
 * <p>
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank">Alexander Verbraeck</a>
 */
public class DummyModel implements DSOLModel.TimeDouble<DEVSSimulatorInterface.TimeDouble>
{
    /** */
    private static final long serialVersionUID = 1L;

    /** the simulator. */
    private final DEVSSimulatorInterface.TimeDouble simulator;

    /**
     * @param simulator the simulator
     */
    public DummyModel(final DEVSSimulatorInterface.TimeDouble simulator)
    {
        this.simulator = simulator;
    }

    /** {@inheritDoc} */
    @Override
    public void constructModel() throws SimRuntimeException
    {
        //
    }

    /** {@inheritDoc} */
    @Override
    public final DEVSSimulatorInterface.TimeDouble getSimulator()
    {
        return this.simulator;
    }

    /** {@inheritDoc} */
    @Override
    public final InputParameterMap getInputParameterMap()
    {
        return new InputParameterMap("roor", "root", "root", 1.0);
    }

    /** {@inheritDoc} */
        @Override
    public final List<OutputStatistic<?>> getOutputStatistics()
    {
        return new ArrayList<>();
    }
}
