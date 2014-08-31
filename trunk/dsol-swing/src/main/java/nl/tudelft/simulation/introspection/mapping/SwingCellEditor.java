package nl.tudelft.simulation.introspection.mapping;

import java.awt.Component;

import javax.swing.AbstractCellEditor;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

/**
 * An editor for SwingComponents.
 * <p />
 * (c) copyright 2002-2014 <a href="http://www.simulation.tudelft.nl">Delft University of Technology</a>. <br />
 * BSD-style license. See <a href="http://www.simulation.tudelft.nl/dsol/3.0/license.html">DSOL License</a>. <br />
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs</a>.
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>.
 * @author Niels Lang.
 * @since 1.5
 */
public class SwingCellEditor extends AbstractCellEditor implements TableCellEditor
{
    /** */
    private static final long serialVersionUID = 20140831L;

    /** the value to edit. */
    private JComponent component = new JPanel();

    /** {@inheritDoc} */
    @Override
    public Component getTableCellEditorComponent(final JTable table, final Object value, final boolean isSelected,
            final int row, final int column)
    {
        this.component = (JComponent) value;
        return (JComponent) value;
    }

    /** {@inheritDoc} */
    @Override
    public Object getCellEditorValue()
    {
        return this.component;
    }
}
