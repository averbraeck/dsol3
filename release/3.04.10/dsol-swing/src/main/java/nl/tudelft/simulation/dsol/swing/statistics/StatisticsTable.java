package nl.tudelft.simulation.dsol.swing.statistics;

import java.awt.Container;
import java.rmi.RemoteException;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EtchedBorder;

import nl.tudelft.simulation.dsol.statistics.table.StatisticsTableModel;
import nl.tudelft.simulation.dsol.swing.Swingable;

/**
 * StatisticsTable.java.
 * <p>
 * Copyright (c) 2020-2020 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://opentrafficsim.org/docs/current/license.html">OpenTrafficSim License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 * @author <a href="https://www.transport.citg.tudelft.nl">Wouter Schakel</a>
 */
public class StatisticsTable implements Swingable
{
    /** the statistics table that is represented on the screen. */
    private final StatisticsTableModel table;

    /**
     * Constructor.
     * @param table StatisticsTableModel; the statistics table that is represented on the screen
     */
    public StatisticsTable(final StatisticsTableModel table)
    {
        this.table = table;
    }

    /**
     * represents this statisticsObject as Container.
     * @return Container; the result
     * @throws RemoteException on network failure
     */
    @Override
    public Container getSwingPanel() throws RemoteException
    {
        JTable jTable = new JTable(this.table);
        jTable.setEnabled(false);
        JScrollPane pane = new JScrollPane(jTable);
        pane.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        return pane;
    }
}
