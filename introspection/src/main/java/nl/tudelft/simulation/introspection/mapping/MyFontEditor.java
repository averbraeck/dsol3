package nl.tudelft.simulation.introspection.mapping;

import java.awt.Component;
import java.awt.Dialog;
import java.awt.Font;

import javax.swing.AbstractCellEditor;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.TableCellEditor;

import org.jfree.ui.FontChooserDialog;

/**
 * An editor for fonts.
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
public class MyFontEditor extends AbstractCellEditor implements TableCellEditor
{
    /** the value */
    protected Font value;

    /** the cellPanel */
    protected JLabel cellPanel = new JLabel();

    /**
     * @see javax.swing.CellEditor#getCellEditorValue()
     */
    public Object getCellEditorValue()
    {
        return this.value;
    }

    /**
     * @see javax.swing.table.TableCellEditor#getTableCellEditorComponent(JTable, Object, boolean, int, int)
     */
    public Component getTableCellEditorComponent(final JTable table, final Object value, final boolean isSelected,
            final int row, final int column)
    {
        this.cellPanel.setBackground(this.cellPanel.getBackground().darker());
        this.value = (Font) value;
        MyFontChooserDialog chooser =
                new MyFontChooserDialog((Dialog) SwingUtilities.getWindowAncestor(table), "Font selection", true,
                        (Font) value);

        chooser.addUserListener(new UserListener(chooser));
        chooser.pack();
        chooser.setVisible(true);
        return this.cellPanel;
    }

    /** the listener */
    private class UserListener implements MyFontChooserDialog.UserListenerInterface
    {
        /** the fileChooser */
        private FontChooserDialog chooser;

        /**
         * constructs a new UserListener
         * @param chooser the chooser we listen at
         */
        public UserListener(FontChooserDialog chooser)
        {
            this.chooser = chooser;
        }

        /**
         * @see nl.tudelft.simulation.introspection.mapping.MyFontChooserDialog.UserListenerInterface
         *      #okActionPerformed()
         */
        public void okActionPerformed()
        {
            MyFontEditor.this.value = this.chooser.getSelectedFont();
            MyFontEditor.this.cellPanel.setText("" + MyFontEditor.this.value);
            MyFontEditor.this.cellPanel.paintImmediately(MyFontEditor.this.cellPanel.getBounds());
            MyFontEditor.this.stopCellEditing();
        }

        /**
         * @see nl.tudelft.simulation.introspection.mapping.MyFontChooserDialog.UserListenerInterface
         *      #cancelActionPerformed()
         */
        public void cancelActionPerformed()
        {
            MyFontEditor.this.cancelCellEditing();
        }
    }
}