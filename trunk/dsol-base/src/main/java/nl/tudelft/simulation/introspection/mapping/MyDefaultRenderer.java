package nl.tudelft.simulation.introspection.mapping;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

/**
 * @author (c) 2003 <a href="http://www.tudelft.nl">Delft University of Technology </a>, Delft, the Netherlands <br>
 *         <a href="http://www.tbm.tudelft.nl">Faculty of Technology, Policy and Management </a> <br>
 *         <a href="http://www.sk.tbm.tudelft.nl">Department of System Engineering </a> <br>
 *         Main researcher : <a href="http://www.tbm.tudelft.nl/webstaf/alexandv/">Dr. Ir. A. Verbraeck <a/><br>
 *         Assistant researchers <a href="http://www.peter-jacobs.com">Ir. P.H.M. Jacobs </a> and <a
 *         href="http://www.tbm.tudelft.nl/webstaf/nielsl">Ir. N.A. Lang </a>
 */
public class MyDefaultRenderer extends DefaultTableCellRenderer
{
    /**
     * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(JTable, Object, boolean, boolean, int,
     *      int)
     */
    @Override
    public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean isSelected,
            final boolean hasFocus, final int row, final int column)
    {
        TableCellRenderer renderer = table.getDefaultRenderer(Object.class);
        if (value != null)
        {
            renderer = table.getDefaultRenderer(value.getClass());
        }
        if (renderer instanceof MyDefaultRenderer)
        {
            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        }
        return renderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    }
}