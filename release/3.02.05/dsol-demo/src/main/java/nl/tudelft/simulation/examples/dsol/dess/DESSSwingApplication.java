package nl.tudelft.simulation.examples.dsol.dess;

import java.rmi.RemoteException;

import javax.naming.NamingException;

import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.experiment.Replication;
import nl.tudelft.simulation.dsol.experiment.ReplicationMode;
import nl.tudelft.simulation.dsol.gui.swing.DSOLApplication;
import nl.tudelft.simulation.dsol.gui.swing.DSOLPanel;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simulators.DESSSimulator;

/**
 * <p>
 * Copyright (c) 2002-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. See for project information <a href="https://simulation.tudelft.nl/" target="_blank">
 * https://simulation.tudelft.nl</a>. The DSOL project is distributed under a three-clause BSD-style license, which can
 * be found at <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class DESSSwingApplication extends DSOLApplication
{
    /**
     * @param title the title
     * @param panel the panel
     */
    public DESSSwingApplication(final String title, final DSOLPanel<Double, Double, SimTimeDouble> panel)
    {
        super(title, panel);
    }

    /** */
    private static final long serialVersionUID = 1L;

    /**
     * @param args arguments, expected to be empty
     * @throws SimRuntimeException on error
     * @throws RemoteException on error
     * @throws NamingException on error
     */
    public static void main(final String[] args) throws SimRuntimeException, RemoteException, NamingException
    {
        DESSModel model = new DESSModel();
        DESSSimulator.TimeDouble simulator = new DESSSimulator.TimeDouble(0.1);
        Replication<Double, Double, SimTimeDouble> replication =
                new Replication<>("rep1", new SimTimeDouble(0.0), 0.0, 100.0, model);
        simulator.initialize(replication, ReplicationMode.TERMINATING);
        new DESSSwingApplication("DESS model", new DESSPanel(model, simulator));
    }

}
