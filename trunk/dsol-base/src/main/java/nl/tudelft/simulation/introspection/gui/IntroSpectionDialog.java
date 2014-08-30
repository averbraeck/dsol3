/*
 * @(#) IntroSpectionDialog.java Apr 15, 2004 Copyright (c) 2002-2005 Delft
 * University of Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All
 * rights reserved. This software is proprietary information of Delft University
 * of Technology 
 */

package nl.tudelft.simulation.introspection.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.WindowConstants;

import nl.tudelft.simulation.introspection.table.DynamicTableModel;

/**
 * A GUI element for presentation and manipulation of an introspected object. The dialog is 'powered' by an instance of
 * {see ObjectJTable}. The dialog is positioned to a 'parent' window, or displayed centered if no parent window is
 * available.
 * <p>
 * (c) copyright 2002-2005-2004 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="http://web.eur.nl/fbk/dep/dep1/Introduction/Staff/People/Lang">Niels Lang </a><a
 *         href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @version 1.1 Apr 15, 2004
 * @since 1.5
 */
public class IntroSpectionDialog extends JDialog
{
    /** the table, set during initialization */
    private JTable table;

    /** the parent window, set during initialization */
    private Window parent;

    /**
     * Constructs a new IntroSpectionDialog.
     * @param introspected The introspected object
     */
    public IntroSpectionDialog(final Object introspected)
    {
        this(null, introspected);
    }

    /**
     * Constructs a new IntroSpectionDialog.
     * @param parent The parent window, used for locating the dialog
     * @param introspected The introspected object
     */
    public IntroSpectionDialog(final Window parent, final Object introspected)
    {
        this(parent, introspected.toString(), new ObjectJTable(new ObjectTableModel(introspected)));
    }

    /**
     * Constructs a new IntroSpectionDialog.
     * @param title The title of the frame
     * @param introspected The introspected object
     */
    public IntroSpectionDialog(final Object introspected, final String title)
    {
        this(null, title, new ObjectJTable(new ObjectTableModel(introspected)));
    }

    /**
     * Constructs a new IntroSpectionDialog.
     * @param title The title of the dialog
     * @param content The object table-model containing the data of the introspected object
     */
    public IntroSpectionDialog(final String title, final IntrospectingTableModelInterface content)
    {
        this(null, title, content);
    }

    /**
     * Constructs a new IntroSpectionDialog.
     * @param parent The parent window, used for locating the dialog
     * @param title The title of the dialog
     * @param content The object table-model containing the data of the introspected object
     */
    public IntroSpectionDialog(final Window parent, final String title, final IntrospectingTableModelInterface content)
    {
        this(parent, title, new ObjectJTable(content));
    }

    /**
     * Constructs a new IntroSpectionDialog.
     * @param parent The parent window, used for locating the dialog
     * @param title The title of the dialog
     * @param introspected The introspected object
     */
    public IntroSpectionDialog(final Frame parent, final Object introspected, final String title)
    {
        this(parent, title, new ObjectJTable(new ObjectTableModel(introspected)));
    }

    /**
     * Constructs a new IntroSpectionDialog.
     * @param title The title of the dialog
     * @param content The table displaying the data of the introspected object
     */
    public IntroSpectionDialog(final String title, final JTable content)
    {
        this(null, title, content);
    }

    /**
     * Constructs a new IntroSpectionDialog.
     * @param parent The parent window, used for locating the dialog
     * @param title The title of the dialog
     * @param content The table displaying the data of the introspected object
     */
    public IntroSpectionDialog(final Window parent, final String title, final JTable content)
    {
        super();
        this.parent = parent;
        this.init(title, content);
    }

    /**
     * initializes the dialog
     * @param title the title of the dialog
     * @param table the table to display
     */
    private void init(final String title, final JTable table)
    {
        this.table = table;
        this.setModal(false);
        this.setTitle(title);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.getContentPane().setLayout(new BorderLayout());
        JScrollPane pane =
                new JScrollPane(table, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                        ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        this.getContentPane().add(pane, BorderLayout.CENTER);
        if (table instanceof ObjectJTableInterface)
        {
            if (((ObjectJTableInterface) table).getIntrospectingTableModel() instanceof DynamicTableModel)
            {
                DynamicTableModel model =
                        (DynamicTableModel) ((ObjectJTableInterface) table).getIntrospectingTableModel();
                this.getContentPane().add(new ButtonPanel(model, table), BorderLayout.SOUTH);
            }
        }
        this.pack();
        setRelativeLocation();
        this.setVisible(true);
    }

    /**
     * Reformats this dialog to reflect changes in the table displayed.
     */
    protected void formatDialog()
    {
        // NB PropertyChanges of height are not broadcasted by the dialog!
        // Therefore, a static check is used instead.
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        if (this.table.getPreferredSize().height >= 0.5 * d.height
                || this.table.getPreferredSize().height + getLocation().y >= 0.9 * d.height)
        {
            return;
        }
        this.table.setPreferredScrollableViewportSize(this.table.getPreferredSize());
        pack();
    }

    /**
     * Initializes the location of this dialog relative to its parent window if any.
     */
    protected void setRelativeLocation()
    {
        setLocationRelativeTo(this.parent);
    }

    /**
     * The ButtonPanel adds functionality for adding and removing rows in a table.
     */
    class ButtonPanel extends JPanel
    {
        /** model */
        private DynamicTableModel model;

        /** the viewer */
        private JTable viewer;

        /**
         * Constructs a new ButtonPanel
         * @param model the model to control
         * @param viewer the viewer to control
         */
        public ButtonPanel(final DynamicTableModel model, final JTable viewer)
        {
            this.model = model;
            this.viewer = viewer;
            this.setLayout(new BorderLayout());
            JPanel buttons = new JPanel();
            FlowLayout manager = new FlowLayout();
            manager.setHgap(0);
            manager.setVgap(0);
            buttons.setLayout(manager);
            add(buttons, BorderLayout.CENTER);
            JButton addButton = new JButton("Add row");
            JButton delButton = new JButton("Delete rows");
            if (!model.isRowEditable())
            {
                addButton.setEnabled(false);
                delButton.setEnabled(false);
            }
            buttons.add(addButton);
            buttons.add(delButton);
            addButton.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    ButtonPanel.this.addRow();
                    formatDialog();
                }
            });
            delButton.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    ButtonPanel.this.delRows();
                    formatDialog();
                }
            });
        }

        /**
         * Adds a row
         */
        protected void addRow()
        {
            this.model.createRow();
        }

        /**
         * Deletes the rows currently selected from the table model.
         */
        protected void delRows()
        {
            this.model.deleteRows(this.viewer.getSelectedRows());
        }
    }
}