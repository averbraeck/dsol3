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
 * The StatisticsObject class defines a statistics object. This abstract class is used to create general table
 * representations for the Counter, the Tally and the Persistent.
 * <p>
 * (c) copyright 2002-2005-2004 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:38:40 $
 * @since 1.5
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
