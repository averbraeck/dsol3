package nl.tudelft.simulation.introspection.mapping;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractCellEditor;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

/**
 * Starts up a default {see javax.swing.JColorChooser}panel to edit the color value.
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
public class MyColorEditor extends AbstractCellEditor implements TableCellEditor
{
    /** the value. */
    protected Color value;

    /** the cellPanel. */
    protected JPanel cellPanel = new JPanel();

    /**
     * The OK listener
     */
    private class OKListener implements ActionListener
    {
        /** the color chooser. */
        private JColorChooser chooser;

        /**
         * constructs a new OKListener.
         * @param chooser the color chooser.
         */
        public OKListener(JColorChooser chooser)
        {
            this.chooser = chooser;
        }

        /** {@inheritDoc} */
        public synchronized void actionPerformed(ActionEvent event)
        {
            MyColorEditor.this.value = this.chooser.getColor();
            MyColorEditor.this.stopCellEditing();
            MyColorEditor.this.cellPanel.setBackground(MyColorEditor.this.value.darker());
            MyColorEditor.this.cellPanel.paintImmediately(MyColorEditor.this.cellPanel.getBounds());
        }
    }

    /**
     * The CancelListener
     */
    private class CancelListener implements ActionListener
    {
        /** {@inheritDoc} */
        public void actionPerformed(ActionEvent e)
        {
            MyColorEditor.this.cancelCellEditing();
        }
    }

    /** {@inheritDoc} */
    public Object getCellEditorValue()
    {
        return this.value;
    }

    /** {@inheritDoc} */
    public Component getTableCellEditorComponent(final JTable table, final Object value, final boolean isSelected,
            final int row, final int column)
    {
        this.cellPanel.setBackground(((Color) value).darker());
        this.value = (Color) value;
        JColorChooser chooser = new JColorChooser((Color) value);
        JDialog dialog =
                JColorChooser.createDialog(table, "Color selection", false, chooser, new OKListener(chooser),
                        new CancelListener());
        dialog.setVisible(true);
        return this.cellPanel;
    }
}
