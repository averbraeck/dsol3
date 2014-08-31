package nl.tudelft.simulation.introspection.gui;

import javax.swing.table.TableModel;

import nl.tudelft.simulation.introspection.Introspector;
import nl.tudelft.simulation.introspection.Property;
import nl.tudelft.simulation.introspection.sortable.SortingTableModel;

/**
 * The sortingObjectTableModel. Can act as a delegate for an instance of {see
 * nl.tudelft.simulation.introspection.gui.IntrospectingTableModelInterface}.
 * <p>
 * (c) copyright 2002-2005-2004 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="http://web.eur.nl/fbk/dep/dep1/Introduction/Staff/People/Lang">Niels Lang </a><a
 *         href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @version 1.1 Apr 15, 2004
 * @since 1.5
 */
public class SortingObjectTableModel extends SortingTableModel implements IntrospectingTableModelInterface
{
    /**
     * constructs a new SortingObjectTableModel
     * @param source the source of this tableModel
     */
    public SortingObjectTableModel(final TableModel source)
    {
        super(source);
    }

    /** {@inheritDoc} */
    public Introspector getIntrospector()
    {
        if (!(this.source instanceof IntrospectingTableModelInterface))
        {
            return null;
        }
        return ((IntrospectingTableModelInterface) this.source).getIntrospector();
    }

    /** {@inheritDoc} */
    public Property getProperty(final String propertyName)
    {
        if (!(this.source instanceof IntrospectingTableModelInterface))
        {
            return null;
        }
        return ((IntrospectingTableModelInterface) this.source).getProperty(propertyName);
    }

    /** {@inheritDoc} */
    public Class getTypeAt(final int rowIndex, final int columnIndex)
    {
        if (!(this.source instanceof IntrospectingTableModelInterface))
        {
            return null;
        }
        return ((IntrospectingTableModelInterface) this.source).getTypeAt(this.expandedIndex[rowIndex].intValue(),
                columnIndex);
    }

    /** {@inheritDoc} */
    public ModelManager getModelManager()
    {
        if (!(this.source instanceof IntrospectingTableModelInterface))
        {
            return null;
        }
        return ((IntrospectingTableModelInterface) this.source).getModelManager();
    }
}
