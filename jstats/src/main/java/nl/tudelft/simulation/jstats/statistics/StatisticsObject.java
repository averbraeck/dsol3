/*
 * @(#) StatisticsObject.java Sep 21, 2003 Copyright (c) 2002-2005 Delft
 * University of Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All
 * rights reserved. This software is proprietary information of Delft University
 * of Technology 
 */
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
import nl.tudelft.simulation.language.filters.FilterInterface;
import nl.tudelft.simulation.language.filters.ZeroFilter;

/**
 * The StatisticsObject class defines a statistics object. This abstract class
 * is used to create general table representations for the Counter, the Tally
 * and the Persistent.
 * <p>
 * (c) copyright 2002-2005-2004 <a href="http://www.simulation.tudelft.nl">Delft
 * University of Technology </a>, the Netherlands. <br>
 * See for project information <a
 * href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser
 * General Public License (LGPL) </a>, no warranty.
 * 
 * @author <a href="http://www.peter-jacobs.com">Peter Jacobs </a>
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:38:40 $
 * @since 1.5
 */

public abstract class StatisticsObject extends EventProducer implements
        Swingable, EventListenerInterface, Serializable
{
    /** the filter applied to the Counter */
    protected FilterInterface filter = new ZeroFilter();

    /**
     * constructs a new StatisticsObject
     */
    public StatisticsObject()
    {
        super();
    }

    /**
     * represents the statistics object as Table.
     * 
     * @return TableModel the result
     * @throws RemoteException on network failure
     */
    public abstract TableModel getTable() throws RemoteException;

    /**
     * represents this statisticsObject as Container.
     * 
     * @return Container the result
     * @throws RemoteException on network failure
     */
    public Container getSwingPanel() throws RemoteException
    {
        JTable table = new JTable(this.getTable());
        table.setEnabled(false);
        return new JScrollPane(table);
    }
}