package nl.tudelft.simulation.jstats.statistics;

import javax.swing.table.DefaultTableModel;

import nl.tudelft.simulation.event.EventInterface;
import nl.tudelft.simulation.event.EventListenerInterface;
import nl.tudelft.simulation.event.EventType;

/**
 * The StatisticsTableModel class defines the tableModel used by the statistics objects.
 * <p>
 * (c) 2002-2018-2004 <a href="https://simulation.tudelft.nl">Delft University of Technology </a>, the Netherlands. <br>
 * See for project information <a href="https://simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:38:40 $
 * @since 1.5
 */
public class StatisticsTableModel extends DefaultTableModel implements EventListenerInterface
{
    /** eventTypes represent the eventTypes corresponding to the colmumns. */
    private EventType[] eventTypes = null;

    /**
     * constructs a new StatisticsTableModel.
     * @param columnNames the names of the columns
     * @param eventTypes the eventTypes representing the column
     * @param rows the number of rows
     */
    public StatisticsTableModel(final Object[] columnNames, final EventType[] eventTypes, final int rows)
    {
        super(columnNames, rows);
        if (rows != eventTypes.length)
        {
            throw new IllegalArgumentException("eventTypes.length!=rows");
        }
        this.eventTypes = eventTypes;
    }

    /** {@inheritDoc} */
    @Override
    public void notify(final EventInterface event)
    {
        for (int i = 0; i < this.eventTypes.length; i++)
        {
            if (event.getType().equals(this.eventTypes[i]))
            {
                this.setValueAt(event.getContent(), i, 1);
            }
        }
    }
}
