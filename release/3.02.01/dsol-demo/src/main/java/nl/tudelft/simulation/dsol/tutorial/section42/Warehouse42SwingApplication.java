package nl.tudelft.simulation.dsol.tutorial.section42;

import java.rmi.RemoteException;

import javax.naming.NamingException;

import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.experiment.Replication;
import nl.tudelft.simulation.dsol.experiment.ReplicationMode;
import nl.tudelft.simulation.dsol.gui.swing.DSOLApplication;
import nl.tudelft.simulation.dsol.gui.swing.DSOLPanel;
import nl.tudelft.simulation.dsol.gui.swing.TablePanel;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulator;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;
import nl.tudelft.simulation.dsol.statistics.charts.XYChart;

/**
 * <p>
 * Copyright (c) 2002-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved.
 * <p>
 * See for project information <a href="https://simulation.tudelft.nl/"> www.simulation.tudelft.nl</a>.
 * <p>
 * The DSOL project is distributed under the following BSD-style license:<br>
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
 * following conditions are met:
 * <ul>
 * <li>Redistributions of source code must retain the above copyright notice, this list of conditions and the following
 * disclaimer.</li>
 * <li>Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the
 * following disclaimer in the documentation and/or other materials provided with the distribution.</li>
 * <li>Neither the name of Delft University of Technology, nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.</li>
 * </ul>
 * This software is provided by the copyright holders and contributors "as is" and any express or implied warranties,
 * including, but not limited to, the implied warranties of merchantability and fitness for a particular purpose are
 * disclaimed. In no event shall the copyright holder or contributors be liable for any direct, indirect, incidental,
 * special, exemplary, or consequential damages (including, but not limited to, procurement of substitute goods or
 * services; loss of use, data, or profits; or business interruption) however caused and on any theory of liability,
 * whether in contract, strict liability, or tort (including negligence or otherwise) arising in any way out of the use
 * of this software, even if advised of the possibility of such damage.
 * @version Aug 15, 2014 <br>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class Warehouse42SwingApplication extends DSOLApplication
{
    /**
     * @param title the title
     * @param panel the panel
     */
    public Warehouse42SwingApplication(final String title, final DSOLPanel<Double, Double, SimTimeDouble> panel)
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
        Warehouse42Model model = new Warehouse42Model();
        DEVSSimulatorInterface.TimeDouble simulator = new DEVSSimulator.TimeDouble();
        Replication<Double, Double, SimTimeDouble> replication =
                new Replication<>("rep1", new SimTimeDouble(0.0), 0.0, 5 * 24.0, model);
        simulator.initialize(replication, ReplicationMode.TERMINATING);
        new Warehouse42SwingApplication("MM1 Queue model", new Boat42Panel(model, simulator));
    }

    /**
     * <p>
     * copyright (c) 2002-2018  <a href="https://simulation.tudelft.nl">Delft University of Technology</a>. <br>
     * BSD-style license. See <a href="https://simulation.tudelft.nl/dsol/3.0/license.html">DSOL License</a>. <br>
     * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
     * @version Oct 10, 2016
     */
    protected static class Boat42Panel extends DSOLPanel<Double, Double, SimTimeDouble>
    {
        /** */
        private static final long serialVersionUID = 1L;

        /**
         * @param model the model
         * @param simulator the simulator
         */
        Boat42Panel(final Warehouse42Model model, final DEVSSimulatorInterface.TimeDouble simulator)
        {
            super(model, simulator);
            addTabs(model);
        }

        /**
         * add a number of charts for the demo.
         * @param model the model from which to take the statistics
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
                exception.printStackTrace();
            }
        }
    }
}
