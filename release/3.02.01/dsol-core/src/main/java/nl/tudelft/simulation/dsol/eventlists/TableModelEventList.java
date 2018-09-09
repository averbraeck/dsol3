package nl.tudelft.simulation.dsol.eventlists;

import java.util.Collection;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import nl.tudelft.simulation.dsol.formalisms.eventscheduling.SimEvent;
import nl.tudelft.simulation.dsol.formalisms.eventscheduling.SimEventInterface;
import nl.tudelft.simulation.dsol.simtime.SimTime;
import nl.tudelft.simulation.event.EventType;

/**
 * A TableModel implementation of an eventlist is an extionsion of the eventlist which upholds its own TableModel. This
 * implementation is used to graphically display the events in the tree.
 * <p>
 * (c) 2002-2018 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl"> www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:36:45 $
 * @param <T> the type of simulation time, e.g. SimTimeCalendarLong or SimTimeDouble or SimTimeDoubleUnit.
 * @since 1.5
 */
public class TableModelEventList<T extends SimTime<?, ?, T>> extends RedBlackTree<T>
{
    /** The default serial version UID for serializable classes. */
    private static final long serialVersionUID = 1L;

    /** The EVENTLIST_CHANGED_EVENT. */
    public static final EventType EVENTLIST_CHANCED_EVENT = null;

    /** The tableHeader. */
    public static final String[] HEADER = {"Time", "Source", "Target", "Method"};

    /** the tableModel. */
    private DefaultTableModel tableModel = new DefaultTableModel(HEADER, 0);

    /** show the package information in the tableModel. */
    private boolean showPackage = false;

    /** the logger./ */
    private static Logger logger = LogManager.getLogger(TableModelEventList.class);

    /**
     * constructs a new TableModelEventList.
     * @param origin the origin
     */
    public TableModelEventList(final EventListInterface<T> origin)
    {
        super();
        synchronized (origin)
        {
            // TODO make a copy of all the events for this event list?
            this.addAll(origin);
        }
    }

    /**
     * returns the TreeMapEventList.
     * @return EventListenerInterface
     */
    public final synchronized EventListInterface<T> getOrigin()
    {
        RedBlackTree<T> result = new RedBlackTree<T>();
        // TODO make a copy first?
        result.addAll(this);
        return result;
    }

    /**
     * returns the tableModel.
     * @return TableModel result
     */
    public final TableModel getTableModel()
    {
        return this.tableModel;
    }

    /**
     * update the tableModel.
     */
    private void updateTableModel()
    {
        try
        {
            this.tableModel.setRowCount(0);
            for (SimEventInterface<T> simEventInterface : this)
            {
                if (simEventInterface instanceof SimEvent)
                {
                    Object[] row = new Object[4];
                    SimEvent<T> event = (SimEvent<T>) simEventInterface;
                    row[0] = event.getAbsoluteExecutionTime().toString();
                    row[1] = this.formatObject(event.getSource().toString());
                    row[2] = this.formatObject(event.getTarget().toString());
                    row[3] = this.formatObject(event.getMethod().toString());
                    this.tableModel.addRow(row);
                }
            }

        }
        catch (Exception exception)
        {
            logger.warn("updateTableModel", exception);
        }
    }

    /**
     * formats a label representing an object.
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
     * sets the showPackage.
     * @param showPackage The showPackage to set.
     */
    public final void setShowPackage(final boolean showPackage)
    {
        this.showPackage = showPackage;
    }

    /** {@inheritDoc} */
    @Override
    public final synchronized boolean addAll(final Collection<? extends SimEventInterface<T>> collection)
    {
        synchronized (this.tableModel)
        {
            boolean result = super.addAll(collection);
            this.updateTableModel();
            return result;
        }
    }

    /** {@inheritDoc} */
    @Override
    public final synchronized void clear()
    {
        synchronized (this.tableModel)
        {
            super.clear();
            this.updateTableModel();
        }
    }

    /** {@inheritDoc} */
    @Override
    public final synchronized boolean removeAll(final Collection<?> collection)
    {
        synchronized (this.tableModel)
        {
            boolean result = super.removeAll(collection);
            this.updateTableModel();
            return result;
        }
    }

    /** {@inheritDoc} */
    @Override
    public final synchronized SimEventInterface<T> removeFirst()
    {
        synchronized (this.tableModel)
        {
            SimEventInterface<T> result = super.removeFirst();
            this.updateTableModel();
            return result;
        }
    }

    /** {@inheritDoc} */
    @Override
    public final synchronized SimEventInterface<T> removeLast()
    {
        synchronized (this.tableModel)
        {
            SimEventInterface<T> result = super.removeLast();
            this.updateTableModel();
            return result;
        }
    }
}
