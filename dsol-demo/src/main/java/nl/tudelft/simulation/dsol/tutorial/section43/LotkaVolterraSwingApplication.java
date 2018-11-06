package nl.tudelft.simulation.dsol.tutorial.section43;

import java.rmi.RemoteException;

import javax.naming.NamingException;

import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.experiment.Replication;
import nl.tudelft.simulation.dsol.experiment.ReplicationMode;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simulators.DESSSimulator;
import nl.tudelft.simulation.dsol.swing.gui.DSOLApplication;
import nl.tudelft.simulation.dsol.swing.gui.DSOLPanel;
import nl.tudelft.simulation.dsol.swing.gui.TablePanel;

/**
 * <p>
 * Copyright (c) 2002-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class LotkaVolterraSwingApplication extends DSOLApplication
{
    /**
     * @param title String; the title
     * @param panel DSOLPanel&lt;Double,Double,SimTimeDouble&gt;; the panel
     */
    public LotkaVolterraSwingApplication(final String title, final DSOLPanel<Double, Double, SimTimeDouble> panel)
    {
        super(title, panel);
    }

    /** */
    private static final long serialVersionUID = 1L;

    /**
     * @param args String[]; arguments, expected to be empty
     * @throws SimRuntimeException on error
     * @throws RemoteException on error
     * @throws NamingException on error
     */
    public static void main(final String[] args) throws SimRuntimeException, RemoteException, NamingException
    {
        DESSSimulator.TimeDouble simulator = new DESSSimulator.TimeDouble(0.01);
        PredatorPrey model = new PredatorPrey(simulator);
        Replication.TimeDouble<DESSSimulator.TimeDouble> replication =
                Replication.TimeDouble.create("rep1", 0.0, 0.0, 100.0, model);
        simulator.initialize(replication, ReplicationMode.TERMINATING);
        new LotkaVolterraSwingApplication("DESS model", new LotkaVolterraPanel(model, simulator));
    }

    /** The panel. */
    private static class LotkaVolterraPanel extends DSOLPanel<Double, Double, SimTimeDouble>
    {
        /** */
        private static final long serialVersionUID = 1L;

        /**
         * @param model PredatorPrey; the model
         * @param simulator DESSSimulatorInterface.TimeDouble; the simulator
         */
        LotkaVolterraPanel(final PredatorPrey model, final DESSSimulator.TimeDouble simulator)
        {
            super(model, simulator);

            // add a chart for the demo.
            TablePanel charts = new TablePanel(1, 1);
            super.tabbedPane.addTab("statistics", charts);
            super.tabbedPane.setSelectedIndex(1);
            charts.setCell(model.getChart().getSwingPanel(), 0, 0);
        }
    }
}
