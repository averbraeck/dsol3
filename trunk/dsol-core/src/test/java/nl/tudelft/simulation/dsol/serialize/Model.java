package nl.tudelft.simulation.dsol.serialize;

import java.rmi.MarshalledObject;
import java.rmi.RemoteException;

import nl.tudelft.simulation.dsol.DSOLModel;
import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.experiment.ExperimentalFrame;
import nl.tudelft.simulation.dsol.formalisms.process.TestExperimentalFrame;
import nl.tudelft.simulation.dsol.logger.SimLogger;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulator;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;
import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;

/**
 * The histogram specifies a histogram chart for the DSOL framework.
 * <p>
 * Copyright (c) 2002-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. See for project information <a href="https://simulation.tudelft.nl/" target="_blank">
 * https://simulation.tudelft.nl</a>. The DSOL project is distributed under a three-clause BSD-style license, which can
 * be found at <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs"> Peter Jacobs </a>
 * @since 1.5
 */
public class Model implements DSOLModel
{
    /** The default serial version UID for serializable classes. */
    private static final long serialVersionUID = 1L;

    /**
     * the simulator to use.
     */
    private SimulatorInterface simulator = null;

    /**
     * constructs a new Model.
     */
    public Model()
    {
        super();
    }

    /** {@inheritDoc} */
    @Override
    public void constructModel(final SimulatorInterface simulator) throws SimRuntimeException
    {
        this.simulator = simulator;
        DEVSSimulatorInterface devsSimulator = (DEVSSimulatorInterface.TimeDouble) simulator;
        devsSimulator.scheduleEventAbs(new SimTimeDouble(10.0), this, this, "pause", null);
    }

    /**
     * @return the simulator
     */
    public SimulatorInterface getSimulator()
    {
        return this.simulator;
    }

    /**
     * pauses the model
     * @throws SimRuntimeException
     * @throws RemoteException
     */
    protected void pause() throws SimRuntimeException, RemoteException
    {
        this.simulator.stop();
        try
        {
            MarshalledObject serializedModel = new MarshalledObject(this);
            Model mySelf = (Model) serializedModel.get();
            mySelf.simulator.start();
        }
        catch (Exception exception)
        {
            SimLogger.always().warn(exception, "pause");
        }
    }

    /**
     * executes the Model
     * @param args the command line arguments
     */
    public static void main(final String[] args)
    {
        ExperimentalFrame experimentalFrame =
                TestExperimentalFrame.createExperimentalFrame(new DEVSSimulator(), new Model());
        experimentalFrame.start();
    }
}
