package nl.tudelft.simulation.examples.dsol.dess;

import java.rmi.RemoteException;

import nl.tudelft.simulation.dsol.gui.swing.DSOLPanel;
import nl.tudelft.simulation.dsol.gui.swing.TablePanel;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simulators.DESSSimulatorInterface;

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
public class DESSPanel extends DSOLPanel<Double, Double, SimTimeDouble>
{
    /** */
    private static final long serialVersionUID = 1L;

    /**
     * @param model the model
     * @param simulator the simulator
     * @throws RemoteException on error
     */
    public DESSPanel(final DESSModel model, final DESSSimulatorInterface.TimeDouble simulator) throws RemoteException
    {
        super(model, simulator);

        // add a chart for the demo.
        TablePanel charts = new TablePanel(2, 1);
        super.tabbedPane.addTab("statistics", charts);
        super.tabbedPane.setSelectedIndex(1);
        charts.setCell(model.getDistanceChart().getSwingPanel(), 0, 0);
        charts.setCell(model.getDistancePersistent().getSwingPanel(), 1, 0);
    }
}
