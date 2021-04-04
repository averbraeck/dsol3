package nl.tudelft.simulation.dsol.swing.gui.test;

import java.rmi.RemoteException;

import javax.naming.NamingException;

import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.experiment.Replication;
import nl.tudelft.simulation.dsol.experiment.ReplicationMode;
import nl.tudelft.simulation.dsol.model.DSOLModel;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulator;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;

/**
 * MM1Application.java.
 * <p>
 * Copyright (c) 2020-2021 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/dsol/manual/" target="_blank">DSOL Manual</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">DSOL License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class MM1Application
{
    /**
     * M/M/1 queueing application.
     * @throws SimRuntimeException on simulation error
     * @throws RemoteException on remote error
     * @throws NamingException on naming/animation error
     */
    protected MM1Application() throws SimRuntimeException, RemoteException, NamingException
    {
        DEVSSimulator.TimeDouble simulator = new DEVSSimulator.TimeDouble("MM1SwingApplication.Simulator");
        DSOLModel.TimeDouble<DEVSSimulatorInterface.TimeDouble> model = new MM1Model(simulator);
        Replication.TimeDouble<DEVSSimulatorInterface.TimeDouble> replication =
                Replication.TimeDouble.create("rep1", 0.0, 0.0, 1000.0, model);
        simulator.initialize(replication);
        simulator.start();
    }

    /**
     * @param args String[]; the arguments (not used, should be empty)
     * @throws SimRuntimeException on simulation error
     * @throws RemoteException on remote error
     * @throws NamingException on naming/animation error
     */
    public static void main(final String[] args) throws SimRuntimeException, RemoteException, NamingException
    {
        new MM1Application();
    }

}
