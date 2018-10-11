package nl.tudelft.simulation.examples.dsol.animation3d;

import java.rmi.RemoteException;

import javax.naming.NamingException;

import nl.tudelft.simulation.dsol.DSOLModel;
import nl.tudelft.simulation.dsol.logger.SimLogger;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simulators.DESSSimulatorInterface;
import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;
import nl.tudelft.simulation.language.d3.DirectedPoint;

/**
 * BallModel3D, the ball example in 3D.
 * <p>
 * Copyright (c) 2003-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. See for project information <a href="https://simulation.tudelft.nl/" target="_blank">
 * https://simulation.tudelft.nl</a>. The DSOL project is distributed under a three-clause BSD-style license, which can
 * be found at <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="http://www.tbm.tudelft.nl/webstaf/royc/index.htm">Roy Chin </a>
 */
public class BallModel3D implements DSOLModel<Double, Double, SimTimeDouble>
{
    /** The default serial version UID for serializable classes. */
    private static final long serialVersionUID = 1L;

    /** the simulator. */
    private SimulatorInterface.TimeDouble simulator;

    /**
     * Constructs new BallModel3D.
     */
    public BallModel3D()
    {
        super();
    }

    /** {@inheritDoc} */
    @Override
    public void constructModel(final SimulatorInterface<Double, Double, SimTimeDouble> pSimulator)
    {
        this.simulator = (SimulatorInterface.TimeDouble) pSimulator;
        System.out.println("*** Ball 3D Model ***");
        for (int i = 0; i < 10; i++)
        {
            try
            {
                new Ball3D((DESSSimulatorInterface.TimeDouble) pSimulator);
            }
            catch (NamingException | RemoteException exception)
            {
                SimLogger.always().error(exception);
            }
        }
        new World(new DirectedPoint(0, 0, -5.5), this.simulator);
    }

    /** {@inheritDoc} */
    @Override
    public SimulatorInterface.TimeDouble getSimulator()
    {
        return this.simulator;
    }
}
