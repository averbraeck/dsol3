package nl.tudelft.simulation.jstats.statistics;

import java.awt.Container;
import java.io.Serializable;
import java.rmi.RemoteException;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableModel;

import nl.tudelft.simulation.event.EventListenerInterface;
import nl.tudelft.simulation.event.EventProducer;
import nl.tudelft.simulation.jstats.Swingable;

/**
 * The StatisticsObject class defines a statistics object. This abstract class is used to create general table representations
 * for the Counter, the Tally and the Persistent.
 * <p>
 * Copyright (c) 2002-2019 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank"> Alexander Verbraeck</a>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 */

public abstract class StatisticsObject extends EventProducer implements Swingable, EventListenerInterface, Serializable
{
    /** */
    private static final long serialVersionUID = 20140804L;

    /**
     * represents the statistics object as Table.
     * @return TableModel the result
     * @throws RemoteException on network failure
     */
    public abstract TableModel getTable() throws RemoteException;

    /**
     * represents this statisticsObject as Container.
     * @return Container; the result
     * @throws RemoteException on network failure
     */
    @Override
    public Container getSwingPanel() throws RemoteException
    {
        JTable table = new JTable(this.getTable());
        table.setEnabled(false);
        return new JScrollPane(table);
    }
}
