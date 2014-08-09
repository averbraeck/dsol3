package nl.tudelft.simulation.introspection.mapping;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 * The color renderer
 * <p>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="http://www.peter-jacobs.com/index.htm">Peter Jacobs </a>
 * @version 1.2 Apr 15, 2004
 * @since 1.5
 */
public class MyColorRenderer implements TableCellRenderer
{
    /**
     * @see javax.swing.table.TableCellRenderer #getTableCellRendererComponent(javax.swing.JTable, java.lang.Object,
     *      boolean, boolean, int, int)
     */
    public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean isSelected,
            final boolean hasFocus, final int row, final int column)
    {
        JPanel result = new JPanel();
        Color color = (Color) value;
        if (isSelected)
        {
            color = color.darker();
        }
        if (hasFocus)
        {
            result.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        }
        result.setBackground(color);
        return result;
    }
}