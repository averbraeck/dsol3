package nl.tudelft.simulation.introspection.gui;

import javax.swing.table.TableModel;

import nl.tudelft.simulation.introspection.Introspector;
import nl.tudelft.simulation.introspection.Property;
import nl.tudelft.simulation.introspection.sortable.SortingTableModel;

/**
 * The sortingObjectTableModel. Can act as a delegate for an instance of {see
 * nl.tudelft.simulation.introspection.gui.IntrospectingTableModelInterface}.
 * <p>
 * copyright (c) 2002-2018  <a href="http://www.simulation.tudelft.nl">Delft University of Technology</a>. <br>
 * BSD-style license. See <a href="http://www.simulation.tudelft.nl/dsol/3.0/license.html">DSOL License</a>. <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs</a>.
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>.
 * @author Niels Lang.
 * @since 1.5
 */
public class SortingObjectTableModel extends SortingTableModel implements IntrospectingTableModelInterface
{
    /**
     * constructs a new SortingObjectTableModel.
     * @param source the source of this tableModel
     */
    public SortingObjectTableModel(final TableModel source)
    {
        super(source);
    }

    /** {@inheritDoc} */
    @Override
    public Introspector getIntrospector()
    {
        if (!(this.source instanceof IntrospectingTableModelInterface))
        {
            return null;
        }
        return ((IntrospectingTableModelInterface) this.source).getIntrospector();
    }

    /** {@inheritDoc} */
    @Override
    public Property getProperty(final String propertyName)
    {
        if (!(this.source instanceof IntrospectingTableModelInterface))
        {
            return null;
        }
        return ((IntrospectingTableModelInterface) this.source).getProperty(propertyName);
    }

    /** {@inheritDoc} */
    @Override
    public Class<?> getTypeAt(final int rowIndex, final int columnIndex)
    {
        if (!(this.source instanceof IntrospectingTableModelInterface))
        {
            return null;
        }
        return ((IntrospectingTableModelInterface) this.source).getTypeAt(this.expandedIndex[rowIndex].intValue(),
                columnIndex);
    }

    /** {@inheritDoc} */
    @Override
    public ModelManager getModelManager()
    {
        if (!(this.source instanceof IntrospectingTableModelInterface))
        {
            return null;
        }
        return ((IntrospectingTableModelInterface) this.source).getModelManager();
    }
}
