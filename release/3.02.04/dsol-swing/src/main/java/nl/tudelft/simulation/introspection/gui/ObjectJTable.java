package nl.tudelft.simulation.introspection.gui;

import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.util.Collection;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

import nl.tudelft.simulation.introspection.mapping.CellPresentationConfiguration;
import nl.tudelft.simulation.introspection.mapping.DefaultConfiguration;
import nl.tudelft.simulation.introspection.sortable.SortDefinition;
import nl.tudelft.simulation.introspection.sortable.SortingTableHeader;

/**
 * * A customization of a standard JTable to allow the display of an introspected object. The behaviour of the
 * ObjectJTable depends on the contained TableModel. {see ObjectTableModel}provides a view of the properties and values
 * of a single introspected object. {see CollectionTableModel}provides a view on a collection of instances: usually the
 * value of a composite property.
 * <p>
 * A configuration mechanism is implemented to load the editors and renders to be used by this JTable. See {see
 * #setConfig(nl.tudelft.simulation.introspection.mapping.CellPresentationConfiguration)} for details.
 * <p>
 * copyright (c) 2002-2018  <a href="https://simulation.tudelft.nl">Delft University of Technology</a>. <br>
 * BSD-style license. See <a href="https://simulation.tudelft.nl/dsol/3.0/license.html">DSOL License</a>. <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs</a>.
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>.
 * @author Niels Lang.
 * @since 1.5
 */
public class ObjectJTable extends JTable implements ObjectJTableInterface, ICellPresentationConfigProvider
{
    /** */
    private static final long serialVersionUID = 20140831L;

    /** the updateTimer. */
    private static UpdateTimer updateTimer = new UpdateTimer(100L);

    /** hasShown? */
    protected boolean hasShown = false;

    /** the introspectionTableModel. */
    private IntrospectingTableModelInterface introspectionTableModel;

    /**
     * The configuration used to assign renderers and editors to cells.
     */
    private final CellPresentationConfiguration CONFIG;

    /**
     * constructs a new ObjectJTable.
     * @param dm the defaultTableModel
     */
    public ObjectJTable(final IntrospectingTableModelInterface dm)
    {
        this(dm, DefaultConfiguration.getDefaultConfiguration());
    }

    /**
     * constructs a new ObjectJTable.
     * @param dm the defaultTableModel
     * @param config the CellPresentationConfiguration
     */
    public ObjectJTable(final IntrospectingTableModelInterface dm, final CellPresentationConfiguration config)
    {
        super(new SortingObjectTableModel(dm));
        this.CONFIG = config;
        init(dm);
    }

    /**
     * Constructor for ObjectJTable.
     * @param dm the defaultTableModel
     * @param cm the tableColumnModel
     */
    public ObjectJTable(final IntrospectingTableModelInterface dm, final TableColumnModel cm)
    {
        super(new SortingObjectTableModel(dm), cm);
        this.CONFIG = DefaultConfiguration.getDefaultConfiguration();
        init(dm);
    }

    /**
     * Constructor for ObjectJTable.
     * @param dm the defaultTableModel
     * @param cm the tableColumnModel
     * @param sm the listSelectionModel
     */
    public ObjectJTable(final IntrospectingTableModelInterface dm, final TableColumnModel cm,
            final ListSelectionModel sm)
    {
        super(new SortingObjectTableModel(dm), cm, sm);
        this.CONFIG = DefaultConfiguration.getDefaultConfiguration();
        init(dm);
    }

    /** {@inheritDoc} */
    @Override
    public CellPresentationConfiguration getCellPresentationConfiguration()
    {
        return this.CONFIG;
    }

    /**
     * initializes the objectJTable.
     * @param model the model
     */
    private void init(IntrospectingTableModelInterface model)
    {
        this.introspectionTableModel = model;
        initConfig();
        setPreferredScrollableViewportSize(this.getPreferredSize());
        JTableHeader header = new SortingTableHeader(new SortDefinition[]{new SortDefinition(0, true)});
        this.setTableHeader(header);
        header.setColumnModel(this.getColumnModel());
        ObjectJTable.updateTimer.add(this);
    }

    /**
     * Enables the installation of a special renderer for arrays and Collections.
     * @see javax.swing.JTable#getDefaultRenderer(java.lang.Class)
     */
    @Override
    public TableCellRenderer getDefaultRenderer(Class<?> columnClass)
    {
        if (columnClass.isArray())
            return super.getDefaultRenderer(Object[].class);
        if (Collection.class.isAssignableFrom(columnClass))
            return super.getDefaultRenderer(Collection.class);
        return super.getDefaultRenderer(columnClass);
    }

    /**
     * the ParentListener.
     */
    protected class ParentListener implements HierarchyListener
    {
        /** {@inheritDoc} */
        @Override
        public void hierarchyChanged(final HierarchyEvent e)
        {
            if (e.getChangeFlags() == HierarchyEvent.DISPLAYABILITY_CHANGED)
            {
                if (!ObjectJTable.this.hasShown && isDisplayable())
                {
                    ObjectJTable.this.hasShown = true;
                    return;
                }
                if (ObjectJTable.this.hasShown && !isDisplayable())
                {
                    ObjectJTable.this.getModel().removeTableModelListener(ObjectJTable.this);
                }
            }
        }
    }

    /**
     * Initializes the configuration, by propagating its settings to the table's set of default renderers/editors.
     */
    private void initConfig()
    {
        addHierarchyListener(new ParentListener());
        Class<?>[][] renderers = this.CONFIG.getRenderers();
        Class<?>[][] editors = this.CONFIG.getEditors();
        try
        {
            for (int i = 0; i < renderers.length; i++)
            {
                this.setDefaultRenderer(renderers[i][0], (TableCellRenderer) renderers[i][1].newInstance());
            }
            for (int i = 0; i < editors.length; i++)
            {
                this.setDefaultEditor(editors[i][0], (TableCellEditor) editors[i][1].newInstance());
            }
        }
        catch (Exception e)
        {
            throw new IllegalArgumentException("Configuration " + this.CONFIG + "failed, "
                    + "probably invalid classes.");
        }
        this.getColumn(getColumnName(0)).setPreferredWidth(70);
        this.getColumn(getColumnName(1)).setMaxWidth(25);
        this.getColumn(getColumnName(2)).setPreferredWidth(450);
        this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    }

    /** {@inheritDoc} */
    @Override
    public IntrospectingTableModelInterface getIntrospectingTableModel()
    {
        return this.introspectionTableModel;
    }
}
