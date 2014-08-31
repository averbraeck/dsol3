package nl.tudelft.simulation.introspection.gui;

import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Constructor;

import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.SwingUtilities;

import nl.tudelft.simulation.introspection.Introspector;
import nl.tudelft.simulation.introspection.Property;
import nl.tudelft.simulation.introspection.mapping.CellPresentationConfiguration;
import nl.tudelft.simulation.introspection.mapping.DefaultConfiguration;
import nl.tudelft.simulation.logger.Logger;

/**
 * A table-element that spawns an introspection dialog for a property. In the new dialog, the property has become the
 * introspected object.
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
public class ExpandButton extends JButton
{
    /** the JTable in which this button is actually displayed. */
    private JTable myTable;

    /** the property */
    private final Property PROPERTY;

    /** the model. */
    private final IntrospectingTableModelInterface MODEL;

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
            /** {@inheritDoc} */
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
            Logger.warning(this, "instantiate", " could not instantiate parent tablemodel, using default. Exception:"
                    + exception.getMessage());
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
