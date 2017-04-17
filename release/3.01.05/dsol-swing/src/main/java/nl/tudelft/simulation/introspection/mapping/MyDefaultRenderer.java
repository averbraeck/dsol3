package nl.tudelft.simulation.introspection.mapping;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

/**
 * <p>
 * (c) copyright 2002-2014 <a href="http://www.simulation.tudelft.nl">Delft University of Technology</a>. <br>
 * BSD-style license. See <a href="http://www.simulation.tudelft.nl/dsol/3.0/license.html">DSOL License</a>. <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs</a>.
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>.
 * @author Niels Lang.
 */
public class MyDefaultRenderer extends DefaultTableCellRenderer
{
    /** */
    private static final long serialVersionUID = 20140831L;

    /** {@inheritDoc} */
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
