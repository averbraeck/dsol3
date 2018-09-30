package nl.tudelft.simulation.dsol.tutorial.section41;

import java.rmi.RemoteException;

import nl.tudelft.simulation.dsol.gui.swing.DSOLPanel;
import nl.tudelft.simulation.dsol.gui.swing.TablePanel;
import nl.tudelft.simulation.dsol.logger.SimLogger;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;
import nl.tudelft.simulation.dsol.statistics.Persistent;
import nl.tudelft.simulation.dsol.statistics.Tally;
import nl.tudelft.simulation.dsol.statistics.charts.BoxAndWhiskerChart;
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
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class MM1Queue41Panel extends DSOLPanel<Double, Double, SimTimeDouble>
{
    /** */
    private static final long serialVersionUID = 1L;

    /**
     * @param model the model
     * @param simulator the simulator
     */
    public MM1Queue41Panel(final MM1Queue41Model model, final DEVSSimulatorInterface.TimeDouble simulator)
    {
        super(model, simulator);
        addTabs(model);
    }

    /**
     * add a number of charts for the demo.
     * @param model the model from which to take the statistics
     */
    @SuppressWarnings("static-access")
    protected final void addTabs(final MM1Queue41Model model)
    {
        TablePanel charts = new TablePanel(3, 3);
        super.tabbedPane.addTab("statistics", charts);
        super.tabbedPane.setSelectedIndex(1);

        try
        {
            XYChart dN = new XYChart(this.simulator, "dN mean");
            dN.add("dN mean", model.dN, Tally.TIMED_SAMPLE_MEAN_EVENT);
            charts.setCell(dN.getSwingPanel(), 0, 0);

            XYChart qN = new XYChart(this.simulator, "qN mean");
            qN.add("qN mean", model.qN, Tally.TIMED_SAMPLE_MEAN_EVENT);
            charts.setCell(qN.getSwingPanel(), 1, 0);

            XYChart utilization = new XYChart(this.simulator, "utilization");
            utilization.add("utilization", model.uN, Persistent.VALUE_EVENT);
            charts.setCell(utilization.getSwingPanel(), 2, 0);

            XYChart meanUtilization = new XYChart(this.simulator, "mean utilization");
            meanUtilization.add("mean utilization", model.uN, Persistent.TIMED_SAMPLE_MEAN_EVENT);
            charts.setCell(meanUtilization.getSwingPanel(), 2, 1);

            // Charts
            BoxAndWhiskerChart bwdN = new BoxAndWhiskerChart(this.simulator, "d(n) chart");
            bwdN.add(model.dN);
            charts.setCell(bwdN.getSwingPanel(), 0, 1);

            BoxAndWhiskerChart bwqN = new BoxAndWhiskerChart(this.simulator, "q(n) chart");
            bwqN.add(model.qN);
            charts.setCell(bwqN.getSwingPanel(), 1, 1);

            charts.setCell(model.dN.getSwingPanel(), 0, 2);
            charts.setCell(model.qN.getSwingPanel(), 1, 2);
            charts.setCell(model.uN.getSwingPanel(), 2, 2);
        }
        catch (RemoteException exception)
        {
            SimLogger.always().error(exception);
        }
    }

}
