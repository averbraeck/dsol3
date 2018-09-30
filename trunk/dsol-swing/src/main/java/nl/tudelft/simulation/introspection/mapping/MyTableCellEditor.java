package nl.tudelft.simulation.introspection.mapping;

import java.awt.Color;
import java.awt.Component;
import java.util.EventObject;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.event.CellEditorListener;
import javax.swing.table.TableCellEditor;

/**
 * An editor for TableObjects.
 * <p>
 * copyright (c) 2002-2018  <a href="https://simulation.tudelft.nl">Delft University of Technology</a>. <br>
 * BSD-style license. See <a href="https://simulation.tudelft.nl/dsol/3.0/license.html">DSOL License</a>. <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs</a>.
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>.
 * @author Niels Lang.
 * @since 1.5
 */
public class MyTableCellEditor implements TableCellEditor
{
    /** {@inheritDoc} */
    @Override
    public Component getTableCellEditorComponent(final JTable table, final Object value, final boolean isSelected,
            final int row, final int column)
    {
        JPanel result = new JPanel();
        result.setBackground(Color.YELLOW);
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public Object getCellEditorValue()
    {
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isCellEditable(final EventObject anEvent)
    {
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public boolean shouldSelectCell(final EventObject anEvent)
    {
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public boolean stopCellEditing()
    {
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public void cancelCellEditing()
    {
        // We cannot edit; this method will never be invoked.
    }

    /** {@inheritDoc} */
    @Override
    public void addCellEditorListener(final CellEditorListener l)
    {
        // Strange, we do not edit
    }

    /** {@inheritDoc} */
    @Override
    public void removeCellEditorListener(final CellEditorListener l)
    {
        // Same story
    }
}
