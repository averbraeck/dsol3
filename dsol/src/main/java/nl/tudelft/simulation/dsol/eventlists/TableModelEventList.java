/*
 * @(#) TableModelEventList.java Nov 7, 2003 Copyright (c) 2002-2005 Delft
 * University of Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All
 * rights reserved. This software is proprietary information of Delft University
 * of Technology 
 */
package nl.tudelft.simulation.dsol.eventlists;

import java.util.Arrays;
import java.util.Collection;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import nl.tudelft.simulation.dsol.formalisms.eventscheduling.SimEvent;
import nl.tudelft.simulation.dsol.formalisms.eventscheduling.SimEventInterface;
import nl.tudelft.simulation.event.EventType;
import nl.tudelft.simulation.logger.Logger;

/**
 * A TableModel implementation of an eventlist is an extionsion of the eventlist which upholds its own TableModel. This
 * implementation is used to graphically display the events in the tree.
 * <p>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl"> www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="http://www.peter-jacobs.com">Peter Jacobs </a>
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:36:45 $
 * @since 1.5
 */
public class TableModelEventList extends RedBlackTree
{
    /** The default serial version UID for serializable classes */
    private static final long serialVersionUID = 1L;

    /** The EVENTLIST_CHANGED_EVENT */
    public static final EventType EVENTLIST_CHANCED_EVENT = null;

    /** The tableHeader */
    public static final String[] HEADER = {"Time", "Source", "Target", "Method"};

    /**
     * the tableModel
     */
    private DefaultTableModel tableModel = new DefaultTableModel(HEADER, 0);

    /**
     * show the package information in the tableModel
     */
    private boolean showPackage = false;

    /**
     * constructs a new TableModelEventList
     * @param origin the origin
     */
    public TableModelEventList(final EventListInterface origin)
    {
        super();
        synchronized (origin)
        {
            Collection<? extends SimEventInterface> collection =
                    Arrays.asList(origin.toArray(new SimEventInterface[origin.size()]));
            this.addAll(collection);
        }
    }

    /**
     * returns the TreeMapEventList
     * @return EventListenerInterface
     */
    public synchronized EventListInterface getOrigin()
    {
        RedBlackTree result = new RedBlackTree();
        result.addAll(Arrays.asList(this.toArray(new SimEventInterface[this.size()])));
        return result;
    }

    /**
     * returns the tableModel
     * @return TableModel resutl
     */
    public TableModel getTableModel()
    {
        return this.tableModel;
    }

    /**
     * update the tableModel
     */
    private void updateTableModel()
    {
        try
        {
            this.tableModel.setRowCount(0);
            for (SimEventInterface simEventInterface : this)
            {
                if (simEventInterface instanceof SimEvent)
                {
                    Object[] row = new Object[4];
                    SimEvent event = (SimEvent) simEventInterface;
                    row[0] = new Double(event.getAbsoluteExecutionTime());
                    row[1] = this.formatObject(event.getSource().toString());
                    row[2] = this.formatObject(event.getTarget().toString());
                    row[3] = this.formatObject(event.getMethod().toString());
                    this.tableModel.addRow(row);
                }
            }

        }
        catch (Exception exception)
        {
            Logger.warning(this, "updateTableModel", exception);
        }
    }

    /**
     * formats a label representing an object
     * @param label the label to format.
     * @return String the label
     */
    private String formatObject(final String label)
    {
        if (label == null)
        {
            return "null";
        }
        if (this.showPackage)
        {
            return label;
        }
        return label.substring(label.lastIndexOf(".") + 1);
    }

    /**
     * sets the showPackage
     * @param showPackage The showPackage to set.
     */
    public void setShowPackage(final boolean showPackage)
    {
        this.showPackage = showPackage;
    }

    /**
     * @see nl.tudelft.simulation.dsol.eventlists.EventListInterface
     *      #add(nl.tudelft.simulation.dsol.formalisms.eventscheduling.SimEventInterface)
     */
    @Override
    public synchronized boolean add(final SimEventInterface value)
    {
        synchronized (this.tableModel)
        {
            boolean result = super.add(value);
            this.updateTableModel();
            return result;
        }
    }

    /**
     * @see nl.tudelft.simulation.dsol.eventlists.EventListInterface #addAll(java.util.Collection)
     */
    @Override
    public synchronized boolean addAll(final Collection<? extends SimEventInterface> collection)
    {
        synchronized (this.tableModel)
        {
            boolean result = super.addAll(collection);
            this.updateTableModel();
            return result;
        }
    }

    /**
     * @see nl.tudelft.simulation.dsol.eventlists.EventListInterface #clear()
     */
    @Override
    public synchronized void clear()
    {
        synchronized (this.tableModel)
        {
            super.clear();
            this.updateTableModel();
        }
    }

    /**
     * @see nl.tudelft.simulation.dsol.eventlists.EventListInterface
     *      #remove(nl.tudelft.simulation.dsol.formalisms.eventscheduling.SimEventInterface)
     */
    @Override
    public synchronized boolean remove(final Object value)
    {
        synchronized (this.tableModel)
        {
            boolean result = super.remove(value);
            this.updateTableModel();
            return result;
        }
    }

    /**
     * @see nl.tudelft.simulation.dsol.eventlists.EventListInterface #removeAll(java.util.Collection)
     */
    @Override
    public synchronized boolean removeAll(final Collection<?> collection)
    {
        synchronized (this.tableModel)
        {
            boolean result = super.removeAll(collection);
            this.updateTableModel();
            return result;
        }
    }

    /**
     * @see nl.tudelft.simulation.dsol.eventlists.EventListInterface#removeFirst()
     */
    @Override
    public synchronized SimEventInterface removeFirst()
    {
        synchronized (this.tableModel)
        {
            SimEventInterface result = super.removeFirst();
            this.updateTableModel();
            return result;
        }
    }

    /**
     * @see nl.tudelft.simulation.dsol.eventlists.EventListInterface#removeLast()
     */
    @Override
    public synchronized SimEventInterface removeLast()
    {
        synchronized (this.tableModel)
        {
            SimEventInterface result = super.removeLast();
            this.updateTableModel();
            return result;
        }
    }
}