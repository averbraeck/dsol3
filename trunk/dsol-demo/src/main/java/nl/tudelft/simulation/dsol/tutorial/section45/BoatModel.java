package nl.tudelft.simulation.dsol.tutorial.section45;

import java.rmi.RemoteException;

import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.experiment.ExperimentalFrame;
import nl.tudelft.simulation.dsol.logger.SimLogger;
import nl.tudelft.simulation.dsol.model.AbstractDSOLModel;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulator;
import nl.tudelft.simulation.language.io.URLResource;
import nl.tudelft.simulation.xml.dsol.ExperimentParser;

/**
 * A BoatModel.
 * <p>
 * Copyright (c) 2002-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 */
public class BoatModel extends AbstractDSOLModel.TimeDouble<DEVSSimulator.TimeDouble>
{
    /** The default serial version UID for serializable classes. */
    private static final long serialVersionUID = 1L;

    /**
     * constructs a new BoatModel.
     * @param simulator the simulator
     */
    public BoatModel(final DEVSSimulator.TimeDouble simulator)
    {
        super(simulator);
    }

    /** {@inheritDoc} */
    @Override
    public void constructModel() throws SimRuntimeException
    {
        Port port = new Port(this.simulator);

        // We schedule boat creation
        this.scheduleBoatArrival(0, port);
        this.scheduleBoatArrival(1, port);
        this.scheduleBoatArrival(15, port);
    }

    /**
     * schedules the creation of a boat.
     * @param time double; the time when the boat should arrive
     * @param port Port; the port
     * @throws RemoteException on network failuer
     * @throws SimRuntimeException on simulation exception
     */
    private void scheduleBoatArrival(final double time, final Port port) throws SimRuntimeException
    {
        this.simulator.scheduleEventAbs(time, this, Boat.class, "<init>", null);
    }

    /**
     * commandline executes the model.
     * @param args String[]; the arguments to the commandline
     */
    public static void main(final String[] args)
    {
        try
        {
            ExperimentalFrame experimentalFrame =
                    ExperimentParser.parseExperimentalFrame(URLResource.getResource("/section45.xml"));
            experimentalFrame.start();
        }
        catch (Exception exception)
        {
            SimLogger.always().error(exception);
        }
    }
}
