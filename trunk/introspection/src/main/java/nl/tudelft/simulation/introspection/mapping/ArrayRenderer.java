/*
 * Created on Aug 26, 2004 @ Erasmus University Rotterdam Copyright (c) Delft
 * University of Technology
 */
package nl.tudelft.simulation.introspection.mapping;

import java.awt.Component;
import java.lang.reflect.Array;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * Renders array values in a human-readable manner.
 * @author <a href="http://web.eur.nl/fbk/dep/dep1/Introduction/Staff/People/Lang">Niels Lang </a><a
 * @since 1.5
 */
public class ArrayRenderer extends DefaultTableCellRenderer
{
    /**
     * A LABEL is returned, preventing users from editing the array contents directly.
     * @see javax.swing.table.DefaultTableCellRenderer#getTableCellRendererComponent(javax.swing.JTable,
     *      java.lang.Object, boolean, boolean, int, int)
     */
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column)
    {
        Class clasz = value.getClass().getComponentType();
        String text = "[] of " + getShortName(clasz) + ": ";
        try
        {
            for (int i = 0; i < Array.getLength(value); i++)
                text += Array.get(value, i) + "; ";
        }
        catch (Exception e)
        {
            // Unfortunately, we now have nothing more than:
            text += "?";
        }
        return new JLabel(text);
    }

    private static String getShortName(Class clasz)
    {
        String name = clasz.getName();
        return name.substring(name.lastIndexOf('.') + 1);
    }
}
