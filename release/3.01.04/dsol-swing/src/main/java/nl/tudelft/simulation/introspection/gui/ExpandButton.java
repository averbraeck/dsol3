package nl.tudelft.simulation.introspection.gui;

import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Constructor;

import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.SwingUtilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import nl.tudelft.simulation.introspection.Introspector;
import nl.tudelft.simulation.introspection.Property;
import nl.tudelft.simulation.introspection.mapping.CellPresentationConfiguration;
import nl.tudelft.simulation.introspection.mapping.DefaultConfiguration;

/**
 * A table-element that spawns an introspection dialog for a property. In the new dialog, the property has become the
 * introspected object.
 * <p>
 * (c) copyright 2002-2014 <a href="http://www.simulation.tudelft.nl">Delft University of Technology</a>. <br>
 * BSD-style license. See <a href="http://www.simulation.tudelft.nl/dsol/3.0/license.html">DSOL License</a>. <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs</a>.
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>.
 * @author Niels Lang.
 * @since 1.5
 */
public class ExpandButton extends JButton
{
    /** */
    private static final long serialVersionUID = 20140831L;

    /** the JTable in which this button is actually displayed. */
    private JTable myTable;

    /** the property */
    private final Property PROPERTY;

    /** the model. */
    private final IntrospectingTableModelInterface MODEL;
    
    /** the logger. */
    private static Logger logger = LogManager.getLogger(ExpandButton.class);

    /**
     * constructs a new ExpandButton.
     * @param property the property
     * @param model the model
     */
    public ExpandButton(final Property property, final IntrospectingTableModelInterface model)
    {
        super("+");
        this.setMargin(new Insets(0, 0, 0, 0));
        this.PROPERTY = property;
        this.MODEL = model;
        this.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                showTable();
            }
        });
    }

    /**
     * Sets the JTable in which this button is actually displayed. The reference is used to facilitate dialog creation.
     * @param table the table
     */
    public void setMyJTable(final JTable table)
    {
        this.myTable = table;
    }

    /**
     * Shows a new table introspecing the property.
     */
    public void showTable()
    {
        if (this.PROPERTY.getValue() == null)
        {
            return;
        }
        if (this.myTable != null)
        {
            Window parentWindow = SwingUtilities.getWindowAncestor(this);
            new IntroSpectionDialog(parentWindow, this.PROPERTY.getName() + ", " + this.PROPERTY.getValue(),
                    instantiateTable());
        }
        else
        {
            new IntroSpectionDialog(this.PROPERTY.getName() + ", " + this.PROPERTY.getValue(), instantiateTable());
        }
    }

    /**
     * instantiates a JTable with an object model of the property.
     * @return the JTable
     */
    private JTable instantiateTable()
    {
        IntrospectingTableModelInterface newModel = null;
        ModelManager manager = this.MODEL.getModelManager();
        Introspector introspector = this.MODEL.getIntrospector();
        try
        {
            Class<?> modelClass = null;
            if (this.PROPERTY.isCollection())
            {
                modelClass = manager.getDefaultCollectionObjectTableModel();
                Constructor<?> c = modelClass.getConstructor(new Class[]{Property.class, Introspector.class});
                newModel = (IntrospectingTableModelInterface) c.newInstance(new Object[]{this.PROPERTY, introspector});
            }
            else
            {
                modelClass = manager.getDefaultObjectTableModel();
                Constructor<?> c = modelClass.getConstructor(new Class[]{Object.class, Introspector.class});
                newModel =
                        (IntrospectingTableModelInterface) c.newInstance(new Object[]{this.PROPERTY.getValue(),
                                introspector});
            }
        }
        catch (Exception exception)
        {
            logger.warn("instantiate: could not instantiate parent tablemodel, using default", exception);
            if (this.PROPERTY.isCollection())
            {
                newModel = new CollectionTableModel(this.PROPERTY);
            }
            else
            {
                newModel = new ObjectTableModel(this.PROPERTY.getValue());
            }
        }
        // Propagate CellPresentation configuration.
        CellPresentationConfiguration config = DefaultConfiguration.getDefaultConfiguration();
        if (this.myTable instanceof ICellPresentationConfigProvider)
            config = ((ICellPresentationConfigProvider) this.myTable).getCellPresentationConfiguration();
        JTable result = new ObjectJTable(newModel, config);
        // Propagate model settings
        newModel.getModelManager().setDefaultCollectionObjectTableModel(manager.getDefaultCollectionObjectTableModel());
        newModel.getModelManager().setDefaultObjectTableModel(manager.getDefaultObjectTableModel());
        return result;
    }
}
