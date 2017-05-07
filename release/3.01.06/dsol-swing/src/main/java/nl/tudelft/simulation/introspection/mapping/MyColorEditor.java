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
 * (c) copyright 2002-2014 <a href="http://www.simulation.tudelft.nl">Delft University of Technology</a>. <br>
 * BSD-style license. See <a href="http://www.simulation.tudelft.nl/dsol/3.0/license.html">DSOL License</a>. <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs</a>.
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>.
 * @author Niels Lang.
 * @since 1.5
 */
public class MyColorEditor extends AbstractCellEditor implements TableCellEditor
{
    /** */
    private static final long serialVersionUID = 20140831L;

    /** the color. */
    protected Color color;

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
        @Override
        public synchronized void actionPerformed(ActionEvent event)
        {
            MyColorEditor.this.color = this.chooser.getColor();
            MyColorEditor.this.stopCellEditing();
            MyColorEditor.this.cellPanel.setBackground(MyColorEditor.this.color.darker());
            MyColorEditor.this.cellPanel.paintImmediately(MyColorEditor.this.cellPanel.getBounds());
        }
    }

    /**
     * The CancelListener.
     */
    protected class CancelListener implements ActionListener
    {
        /** {@inheritDoc} */
        @Override
        public void actionPerformed(ActionEvent e)
        {
            MyColorEditor.this.cancelCellEditing();
        }
    }

    /** {@inheritDoc} */
    @Override
    public Object getCellEditorValue()
    {
        return this.color;
    }

    /** {@inheritDoc} */
    @Override
    public Component getTableCellEditorComponent(final JTable table, final Object value, final boolean isSelected,
            final int row, final int column)
    {
        this.cellPanel.setBackground(((Color) value).darker());
        this.color = (Color) value;
        JColorChooser chooser = new JColorChooser((Color) value);
        JDialog dialog =
                JColorChooser.createDialog(table, "Color selection", false, chooser, new OKListener(chooser),
                        new CancelListener());
        dialog.setVisible(true);
        return this.cellPanel;
    }
}
