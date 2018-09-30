package nl.tudelft.simulation.xml;

import nl.tudelft.simulation.dsol.DSOLModel;
import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;

/**
 * A DummyTestModel
 * <p>
 * Copyright (c) 2002-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. See for project information <a href="https://simulation.tudelft.nl/" target="_blank">
 * https://simulation.tudelft.nl</a>. The DSOL project is distributed under a three-clause BSD-style license, which can
 * be found at <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @since 1.5
 */
public class DummyModel implements DSOLModel
{
    /**
     * constructs a new DummyModel.
     */
    public DummyModel()
    {
        super();
    }

    /** {@inheritDoc} */
    @Override
    public void constructModel(final SimulatorInterface simulator)
    {
        // We have constructed a model.
    }

    public SimulatorInterface getSimulator()
    {
        return null;
    }
}
