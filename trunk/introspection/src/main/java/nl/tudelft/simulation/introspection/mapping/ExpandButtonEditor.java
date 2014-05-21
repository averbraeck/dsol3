package nl.tudelft.simulation.introspection.mapping;

import java.awt.Component;

import javax.swing.AbstractCellEditor;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import nl.tudelft.simulation.introspection.gui.ExpandButton;
import nl.tudelft.simulation.logger.Logger;

/**
 * Implements the pop-up behaviour of the {see
 * nl.tudelft.simulation.introspection.gui.ExpandButton}.
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
public class ExpandButtonEditor extends AbstractCellEditor implements
        TableCellEditor
{
    /** the value */
    private JComponent value;

    /**
     * @see javax.swing.table.TableCellEditor#getTableCellEditorComponent(JTable,
     *      Object, boolean, int, int)
     */
    public Component getTableCellEditorComponent(final JTable table,
            final Object value, final boolean isSelected, final int row,
            final int column)
    {
        if (value instanceof ExpandButton)
        {
            ((ExpandButton) value).setMyJTable(table);
        } else
        {
            Logger
                    .warning(this, "getTableCellEditorComponent",
                            "Expected value to be an ExpandButton, but found: "
                                    + value);
        }
        return (Component) value;
    }

    /**
     * @see javax.swing.CellEditor#getCellEditorValue()
     */
    public Object getCellEditorValue()
    {
        return this.value;
    }
}