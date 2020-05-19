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
 * copyright (c) 2002-2018  <a href="https://simulation.tudelft.nl">Delft University of Technology</a>. <br>
 * BSD-style license. See <a href="https://simulation.tudelft.nl/dsol/3.0/license.html">DSOL License</a>. <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs</a>.
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>.
 * @author Niels Lang.
 * @since 1.5
 */
public class MyDefaultEditor implements TableCellEditor
{
    /** the delegate. */
    private TableCellEditor delegate = new DefaultCellEditor(new JTextField());

    /** {@inheritDoc} */
    @Override
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
    @Override
    public void cancelCellEditing()
    {
        this.delegate.cancelCellEditing();
    }

    /** {@inheritDoc} */
    @Override
    public Object getCellEditorValue()
    {
        return this.delegate.getCellEditorValue();
    }

    /** {@inheritDoc} */
    @Override
    public boolean isCellEditable(final EventObject anEvent)
    {
        return this.delegate.isCellEditable(anEvent);
    }

    /** {@inheritDoc} */
    @Override
    public boolean shouldSelectCell(final EventObject anEvent)
    {
        return this.delegate.shouldSelectCell(anEvent);
    }

    /** {@inheritDoc} */
    @Override
    public boolean stopCellEditing()
    {
        return this.delegate.stopCellEditing();
    }

    /** {@inheritDoc} */
    @Override
    public void addCellEditorListener(final CellEditorListener l)
    {
        this.delegate.addCellEditorListener(l);
    }

    /** {@inheritDoc} */
    @Override
    public void removeCellEditorListener(final CellEditorListener l)
    {
        this.delegate.removeCellEditorListener(l);
    }
}