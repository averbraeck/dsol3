package nl.tudelft.simulation.dsol.tutorial.section45;

import java.rmi.RemoteException;

import nl.tudelft.simulation.dsol.DSOLModel;
import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.experiment.ExperimentalFrame;
import nl.tudelft.simulation.dsol.logger.SimLogger;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;
import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;
import nl.tudelft.simulation.language.io.URLResource;
import nl.tudelft.simulation.xml.dsol.ExperimentParser;

/**
 * A BoatModel.
 * <p>
 * Copyright (c) 2002-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. See for project information <a href="https://simulation.tudelft.nl/" target="_blank">
 * https://simulation.tudelft.nl</a>. The DSOL project is distributed under a three-clause BSD-style license, which can
 * be found at <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 */
public class BoatModel implements DSOLModel<Double, Double, SimTimeDouble>
{
    /** The default serial version UID for serializable classes. */
    private static final long serialVersionUID = 1L;

    /** the simulator. */
    private SimulatorInterface.TimeDouble simulator;

    /**
     * constructs a new BoatModel.
     */
    public BoatModel()
    {
        super();
    }

    /** {@inheritDoc} */
    @Override
    public void constructModel(final SimulatorInterface<Double, Double, SimTimeDouble> pSimulator)
            throws SimRuntimeException
    {
        this.simulator = (SimulatorInterface.TimeDouble) pSimulator;
        DEVSSimulatorInterface.TimeDouble devsSimulator = (DEVSSimulatorInterface.TimeDouble) pSimulator;
        Port port = new Port(devsSimulator);

        // We schedule boat creation
        this.scheduleBoatArrival(0, devsSimulator, port);
        this.scheduleBoatArrival(1, devsSimulator, port);
        this.scheduleBoatArrival(15, devsSimulator, port);
    }

    /**
     * schedules the creation of a boat.
     * @param time the time when the boat should arrive
     * @param pSimulator the simulator on which we schedule
     * @param port the port
     * @throws RemoteException on network failuer
     * @throws SimRuntimeException on simulation exception
     */
    private void scheduleBoatArrival(final double time, final DEVSSimulatorInterface.TimeDouble pSimulator,
            final Port port) throws SimRuntimeException
    {
        pSimulator.scheduleEventAbs(time, this, Boat.class, "<init>", new Object[]{pSimulator, port});
    }

    /**
     * @return the simulator
     */
    @Override
    public SimulatorInterface.TimeDouble getSimulator()
    {
        return this.simulator;
    }

    /**
     * commandline executes the model.
     * @param args the arguments to the commandline
     */
    public static void main(final String[] args)
    {
        try
        {
            ExperimentalFrame.TimeDouble experimentalFrame = (ExperimentalFrame.TimeDouble) ExperimentParser
                    .parseExperimentalFrame(URLResource.getResource("/section45.xml"));
            experimentalFrame.start();
        }
        catch (Exception exception)
        {
            SimLogger.always().error(exception);
        }
    }
}