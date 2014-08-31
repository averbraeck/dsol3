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
 * <p />
 * (c) copyright 2002-2014 <a href="http://www.simulation.tudelft.nl">Delft University of Technology</a>. <br />
 * BSD-style license. See <a href="http://www.simulation.tudelft.nl/dsol/3.0/license.html">DSOL License</a>. <br />
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs</a>.
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>.
 * @author Niels Lang.
 * @since 1.5
 */
public class IntroSpectionDialog extends JDialog
{
    /** */
    private static final long serialVersionUID = 20140831L;

    /** the table, set during initialization. */
    private JTable table;

    /** the parent window, set during initialization. */
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
     * @param newTable the table to display
     */
    private void init(final String title, final JTable newTable)
    {
        this.table = newTable;
        this.setModal(false);
        this.setTitle(title);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.getContentPane().setLayout(new BorderLayout());
        JScrollPane pane =
                new JScrollPane(newTable, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                        ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        this.getContentPane().add(pane, BorderLayout.CENTER);
        if (newTable instanceof ObjectJTableInterface)
        {
            if (((ObjectJTableInterface) newTable).getIntrospectingTableModel() instanceof DynamicTableModel)
            {
                DynamicTableModel model =
                        (DynamicTableModel) ((ObjectJTableInterface) newTable).getIntrospectingTableModel();
                this.getContentPane().add(new ButtonPanel(model, newTable), BorderLayout.SOUTH);
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
        /** */
        private static final long serialVersionUID = 20140831L;

        /** model. */
        private DynamicTableModel model;

        /** the viewer. */
        private JTable viewer;

        /**
         * Constructs a new ButtonPanel.
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
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    ButtonPanel.this.addRow();
                    formatDialog();
                }
            });
            delButton.addActionListener(new ActionListener()
            {
                @Override
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
