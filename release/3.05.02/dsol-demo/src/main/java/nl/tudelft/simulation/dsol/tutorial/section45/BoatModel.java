package nl.tudelft.simulation.dsol.tutorial.section45;

import java.io.Serializable;
import java.rmi.RemoteException;

import javax.naming.NamingException;

import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.experiment.Replication;
import nl.tudelft.simulation.dsol.experiment.ReplicationMode;
import nl.tudelft.simulation.dsol.model.AbstractDSOLModel;
import nl.tudelft.simulation.dsol.model.DSOLModel;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulator;

/**
 * A BoatModel.
 * <p>
 * Copyright (c) 2002-2021 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
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
     * @param simulator DEVSSimulator.TimeDouble; the simulator
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
        this.simulator.scheduleEventAbs(time, this, Boat.class, "<init>", new Object[] {this.simulator, port});
    }

    /**
     * command line executes the model.
     * @param args String[]; the arguments to the command line
     * @throws NamingException on Context error
     * @throws SimRuntimeException on simulation model construction error
     */
    public static void main(final String[] args) throws NamingException, SimRuntimeException
    {
        DEVSSimulator.TimeDouble simulator = new DEVSSimulator.TimeDouble("BoatModel");
        DSOLModel.TimeDouble<DEVSSimulator.TimeDouble> model = new BoatModel(simulator);
        Replication.TimeDouble<DEVSSimulator.TimeDouble> replication =
                Replication.TimeDouble.create("rep1", 0.0, 0.0, 100.0, model);
        simulator.initialize(replication, ReplicationMode.TERMINATING);
        simulator.start();
    }

    /** {@inheritDoc} */
    @Override
    public Serializable getSourceId()
    {
        return "BoatModel";
    }
}
