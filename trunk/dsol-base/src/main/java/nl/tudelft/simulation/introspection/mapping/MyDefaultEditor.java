package nl.tudelft.simulation.introspection.mapping;

import java.awt.Component;
import java.util.EventObject;

import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.CellEditorListener;
import javax.swing.table.TableCellEditor;

/**
 * The default editor.
 * <p>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @version 1.2 Apr 15, 2004
 * @since 1.5
 */
public class MyDefaultEditor implements TableCellEditor
{
    /** the delegate. */
    private TableCellEditor delegate = new DefaultCellEditor(new JTextField());

    /** {@inheritDoc} */
    public Component getTableCellEditorComponent(final JTable table, final Object value, final boolean isSelected,
            final int row, final int column)
    {
        TableCellEditor editor = table.getDefaultEditor(value.getClass());
        if (!(editor instanceof MyDefaultEditor))
        {
            this.delegate = editor;
        }
        return this.delegate.getTableCellEditorComponent(table, value, isSelected, row, column);
    }

    /** {@inheritDoc} */
    public void cancelCellEditing()
    {
        this.delegate.cancelCellEditing();
    }

    /** {@inheritDoc} */
    public Object getCellEditorValue()
    {
        return this.delegate.getCellEditorValue();
    }

    /** {@inheritDoc} */
    public boolean isCellEditable(final EventObject anEvent)
    {
        return this.delegate.isCellEditable(anEvent);
    }

    /** {@inheritDoc} */
    public boolean shouldSelectCell(final EventObject anEvent)
    {
        return this.delegate.shouldSelectCell(anEvent);
    }

    /** {@inheritDoc} */
    public boolean stopCellEditing()
    {
        return this.delegate.stopCellEditing();
    }

    /** {@inheritDoc} */
    public void addCellEditorListener(final CellEditorListener l)
    {
        this.delegate.addCellEditorListener(l);
    }

    /** {@inheritDoc} */
    public void removeCellEditorListener(final CellEditorListener l)
    {
        this.delegate.removeCellEditorListener(l);
    }
}
