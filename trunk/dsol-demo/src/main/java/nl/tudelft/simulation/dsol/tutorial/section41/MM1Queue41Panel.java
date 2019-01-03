package nl.tudelft.simulation.dsol.tutorial.section41;

import java.rmi.RemoteException;

import nl.tudelft.simulation.dsol.logger.SimLogger;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulator;
import nl.tudelft.simulation.dsol.statistics.SimPersistent;
import nl.tudelft.simulation.dsol.statistics.SimTally;
import nl.tudelft.simulation.dsol.swing.charts.boxAndWhisker.BoxAndWhiskerChart;
import nl.tudelft.simulation.dsol.swing.charts.xy.XYChart;
import nl.tudelft.simulation.dsol.swing.gui.DSOLPanel;
import nl.tudelft.simulation.dsol.swing.gui.TablePanel;

/**
 * <p>
 * Copyright (c) 2002-2019 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. See for project information <a href="https://simulation.tudelft.nl/" target="_blank">
 * https://simulation.tudelft.nl</a>. The DSOL project is distributed under a three-clause BSD-style license, which can
 * be found at <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class MM1Queue41Panel extends DSOLPanel<Double, Double, SimTimeDouble>
{
    /** */
    private static final long serialVersionUID = 1L;

    /**
     * @param model MM1Queue41Model; the model
     * @param simulator DEVSSimulatorInterface.TimeDouble; the simulator
     */
    public MM1Queue41Panel(final MM1Queue41Model model, final DEVSSimulator.TimeDouble simulator)
    {
        super(model, simulator);
        addTabs(model);
    }

    /**
     * add a number of charts for the demo.
     * @param model MM1Queue41Model; the model from which to take the statistics
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
            dN.add("dN mean", model.dN, SimTally.TIMED_SAMPLE_MEAN_EVENT);
            charts.setCell(dN.getSwingPanel(), 0, 0);

            XYChart qN = new XYChart(this.simulator, "qN mean");
            qN.add("qN mean", model.qN, SimTally.TIMED_SAMPLE_MEAN_EVENT);
            charts.setCell(qN.getSwingPanel(), 1, 0);

            XYChart utilization = new XYChart(this.simulator, "utilization");
            utilization.add("utilization", model.uN, SimPersistent.VALUE_EVENT);
            charts.setCell(utilization.getSwingPanel(), 2, 0);

            XYChart meanUtilization = new XYChart(this.simulator, "mean utilization");
            meanUtilization.add("mean utilization", model.uN, SimPersistent.TIMED_SAMPLE_MEAN_EVENT);
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
