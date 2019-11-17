package nl.tudelft.simulation.dsol.serialize;

import java.rmi.MarshalledObject;
import java.rmi.RemoteException;

import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.experiment.ExperimentalFrame;
import nl.tudelft.simulation.dsol.formalisms.process.ExperimentalFrameUtil;
import nl.tudelft.simulation.dsol.logger.SimLogger;
import nl.tudelft.simulation.dsol.model.AbstractDSOLModel;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulator;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;

/**
 * The histogram specifies a histogram chart for the DSOL framework.
 * <p>
 * Copyright (c) 2002-2019 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs"> Peter Jacobs </a>
 * @since 1.5
 */
public class Model extends AbstractDSOLModel.TimeDouble<DEVSSimulatorInterface.TimeDouble>
{
    /** The default serial version UID for serializable classes. */
    private static final long serialVersionUID = 1L;

    /**
     * constructs a new Model.
     * @param simulator the simulator
     */
    public Model(final DEVSSimulatorInterface.TimeDouble simulator)
    {
        super(simulator);
    }

    /** {@inheritDoc} */
    @Override
    public void constructModel() throws SimRuntimeException
    {
        getSimulator().scheduleEventAbs(new SimTimeDouble(10.0), this, this, "pause", null);
    }

    /**
     * pauses the model.
     * @throws SimRuntimeException on error
     * @throws RemoteException on error
     */
    protected void pause() throws SimRuntimeException, RemoteException
    {
        getSimulator().stop();
        try
        {
            @SuppressWarnings({"rawtypes", "unchecked"})
            MarshalledObject serializedModel = new MarshalledObject(this);
            Model mySelf = (Model) serializedModel.get();
            mySelf.getSimulator().start();
        }
        catch (Exception exception)
        {
            SimLogger.always().warn(exception, "pause");
        }
    }

    /**
     * executes the Model.
     * @param args the command line arguments
     */
    public static void main(final String[] args)
    {
        DEVSSimulator.TimeDouble simulator = new DEVSSimulator.TimeDouble();
        ExperimentalFrame experimentalFrame = ExperimentalFrameUtil.createExperimentalFrame(simulator, new Model(simulator));
        experimentalFrame.start();
    }
}
