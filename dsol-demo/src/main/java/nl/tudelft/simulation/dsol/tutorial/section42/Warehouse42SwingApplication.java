package nl.tudelft.simulation.dsol.tutorial.section42;

import java.rmi.RemoteException;

import javax.naming.NamingException;

import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.experiment.Replication;
import nl.tudelft.simulation.dsol.experiment.ReplicationMode;
import nl.tudelft.simulation.dsol.gui.swing.DSOLApplication;
import nl.tudelft.simulation.dsol.gui.swing.DSOLPanel;
import nl.tudelft.simulation.dsol.gui.swing.TablePanel;
import nl.tudelft.simulation.dsol.logger.SimLogger;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulator;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;
import nl.tudelft.simulation.dsol.statistics.charts.XYChart;

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
public class Warehouse42SwingApplication extends DSOLApplication
{
    /**
     * @param title String; the title
     * @param panel DSOLPanel&lt;Double,Double,SimTimeDouble&gt;; the panel
     */
    public Warehouse42SwingApplication(final String title, final DSOLPanel<Double, Double, SimTimeDouble> panel)
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
     * @throws InterruptedException on error
     */
    public static void main(final String[] args)
            throws SimRuntimeException, RemoteException, NamingException, InterruptedException
    {
        Warehouse42Model model = new Warehouse42Model();
        DEVSSimulatorInterface.TimeDouble simulator = new DEVSSimulator.TimeDouble();
        Replication<Double, Double, SimTimeDouble> replication =
                new Replication<>("rep1", new SimTimeDouble(0.0), 0.0, 5 * 24.0, model);
        simulator.initialize(replication, ReplicationMode.TERMINATING);
        new Warehouse42SwingApplication("MM1 Queue model", new Warehouse42Panel(model, simulator));
    }

    /**
     * <p>
     * copyright (c) 2002-2018 <a href="https://simulation.tudelft.nl">Delft University of Technology</a>. <br>
     * BSD-style license. See <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank"> DSOL
     * License</a>. <br>
     * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
     */
    protected static class Warehouse42Panel extends DSOLPanel<Double, Double, SimTimeDouble>
    {
        /** */
        private static final long serialVersionUID = 1L;

        /**
         * @param model Warehouse42Model; the model
         * @param simulator DEVSSimulatorInterface.TimeDouble; the simulator
         */
        Warehouse42Panel(final Warehouse42Model model, final DEVSSimulatorInterface.TimeDouble simulator)
        {
            super(model, simulator);
            addTabs(model);
        }

        /**
         * add a number of charts for the demo.
         * @param model Warehouse42Model; the model from which to take the statistics
         */
        protected final void addTabs(final Warehouse42Model model)
        {
            TablePanel charts = new TablePanel(2, 1);
            super.tabbedPane.addTab("statistics", charts);
            super.tabbedPane.setSelectedIndex(1);

            try
            {
                XYChart chart = new XYChart(this.simulator, "Inventory Levels");
                chart.add(model.inventory);
                chart.add(model.backlog);
                charts.setCell(chart.getSwingPanel(), 0, 0);

                charts.setCell(model.orderingCosts.getSwingPanel(), 1, 0);
            }
            catch (RemoteException exception)
            {
                SimLogger.always().error(exception);
            }
        }
    }
}
