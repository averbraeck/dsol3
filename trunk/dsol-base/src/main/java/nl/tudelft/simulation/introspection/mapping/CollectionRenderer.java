/*
 * Created on Aug 26, 2004 @ Erasmus University Rotterdam Copyright (c) Delft
 * University of Technology
 */
package nl.tudelft.simulation.introspection.mapping;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * Renders Collection values in a human-readable manner.
 * @author <a href="http://web.eur.nl/fbk/dep/dep1/Introduction/Staff/People/Lang">Niels Lang </a><a
 * @since 1.5
 */
public class CollectionRenderer extends DefaultTableCellRenderer
{
    /** {@inheritDoc} */
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column)
    {
        List<Object> coll = new ArrayList<Object>((Collection<?>) value);
        String content = "Collection of ";
        if (coll.size() > 0)
            content += getShortName(coll.get(0).getClass());
        else
            content += "?";
        content += ": ";
        for (int i = 0; i < coll.size(); i++)
            content += coll.get(i).toString() + "; ";
        return new JLabel(content);
    }

    /**
     * Returns the short name of a class
     * @param clasz the class
     * @return the short name
     */
    private static String getShortName(Class clasz)
    {
        String name = clasz.getName();
        return name.substring(name.lastIndexOf('.') + 1);
    }
}
