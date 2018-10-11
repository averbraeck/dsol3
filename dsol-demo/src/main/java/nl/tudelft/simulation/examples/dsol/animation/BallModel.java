package nl.tudelft.simulation.examples.dsol.animation;

import java.rmi.RemoteException;

import nl.tudelft.simulation.dsol.DSOLModel;
import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.logger.SimLogger;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;
import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;

/**
 * <p>
 * Copyright (c) 2002-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. See for project information <a href="https://simulation.tudelft.nl/" target="_blank">
 * https://simulation.tudelft.nl</a>. The DSOL project is distributed under a three-clause BSD-style license, which can
 * be found at <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 */
public class BallModel implements DSOLModel.TimeDouble
{
    /** The default serial version UID for serializable classes. */
    private static final long serialVersionUID = 1L;

    /** the simulator. */
    private DEVSSimulatorInterface.TimeDouble simulator;

    /**
     * constructs a new BallModel.
     */
    public BallModel()
    {
        super();
    }

    /** {@inheritDoc} */
    @Override
    public void constructModel(final SimulatorInterface<Double, Double, SimTimeDouble> sim) throws SimRuntimeException
    {
        this.simulator = (DEVSSimulatorInterface.TimeDouble) sim;
        for (int i = 0; i < 10; i++)
        {
            try
            {
                new DiscreteBall(this.simulator);
            }
            catch (RemoteException exception)
            {
                SimLogger.always().error(exception);
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    public SimulatorInterface.TimeDouble getSimulator()
    {
        return this.simulator;
    }
}
