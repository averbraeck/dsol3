package nl.tudelft.simulation.introspection.mapping;

import java.awt.Component;

import javax.swing.AbstractCellEditor;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import nl.tudelft.simulation.dsol.logger.SimLogger;
import nl.tudelft.simulation.introspection.gui.ExpandButton;

/**
 * Implements the pop-up behaviour of the {see nl.tudelft.simulation.introspection.gui.ExpandButton}.
 * <p>
 * copyright (c) 2002-2018 <a href="https://simulation.tudelft.nl">Delft University of Technology</a>. <br>
 * BSD-style license. See <a href="https://simulation.tudelft.nl/dsol/3.0/license.html">DSOL License</a>. <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs</a>.
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>.
 * @author Niels Lang.
 * @since 1.5
 */
public class ExpandButtonEditor extends AbstractCellEditor implements TableCellEditor
{
    /** */
    private static final long serialVersionUID = 20140831L;

    /** the value. */
    private JComponent component;

    /** {@inheritDoc} */
    @Override
    public Component getTableCellEditorComponent(final JTable table, final Object value, final boolean isSelected,
            final int row, final int column)
    {
        if (value instanceof ExpandButton)
        {
            ((ExpandButton) value).setMyJTable(table);
        }
        else
        {
            SimLogger.always().warn("getTableCellEditorComponent: Expected value to be an ExpandButton, but found: {}", value);
        }
        return (Component) value;
    }

    /** {@inheritDoc} */
    @Override
    public Object getCellEditorValue()
    {
        return this.component;
    }
}
