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
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft
 * University of Technology </a>, the Netherlands. <br>
 * See for project information <a
 * href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser
 * General Public License (LGPL) </a>, no warranty.
 * 
 * @author <a href="http://www.peter-jacobs.com/index.htm">Peter Jacobs </a>
 * @version 1.2 Apr 15, 2004
 * @since 1.5
 */
public class MyTableCellEditor implements TableCellEditor
{
    /**
     * @see javax.swing.table.TableCellEditor#getTableCellEditorComponent(JTable,
     *      Object, boolean, int, int)
     */
    public Component getTableCellEditorComponent(final JTable table,
            final Object value, final boolean isSelected, final int row,
            final int column)
    {
        JPanel result = new JPanel();
        result.setBackground(Color.YELLOW);
        return result;
    }

    /**
     * @see javax.swing.CellEditor#getCellEditorValue()
     */
    public Object getCellEditorValue()
    {
        return null;
    }

    /**
     * @see javax.swing.CellEditor#isCellEditable(EventObject)
     */
    public boolean isCellEditable(final EventObject anEvent)
    {
        return false;
    }

    /**
     * @see javax.swing.CellEditor#shouldSelectCell(EventObject)
     */
    public boolean shouldSelectCell(final EventObject anEvent)
    {
        return false;
    }

    /**
     * @see javax.swing.CellEditor#stopCellEditing()
     */
    public boolean stopCellEditing()
    {
        return false;
    }

    /**
     * @see javax.swing.CellEditor#cancelCellEditing()
     */
    public void cancelCellEditing()
    {
        // We cannot edit; this method will never be invoked.
    }

    /**
     * @see javax.swing.CellEditor#addCellEditorListener(CellEditorListener)
     */
    public void addCellEditorListener(final CellEditorListener l)
    {
        // Strange, we do not edit
    }

    /**
     * @see javax.swing.CellEditor#removeCellEditorListener(CellEditorListener)
     */
    public void removeCellEditorListener(final CellEditorListener l)
    {
        // Same story
    }
}