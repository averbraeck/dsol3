
package nl.tudelft.simulation.introspection.gui;

import javax.swing.table.AbstractTableModel;

import nl.tudelft.simulation.introspection.Introspector;
import nl.tudelft.simulation.introspection.Property;
import nl.tudelft.simulation.introspection.beans.BeanIntrospector;
import nl.tudelft.simulation.logger.Logger;

/**
 * The ObjectTableModel.
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
public class ObjectTableModel extends AbstractTableModel implements IntrospectingTableModelInterface
{
    /** the properties. */
    protected Property[] properties = new Property[0];

    /** the columns. */
    private static String[] columns = {"Property", "+", "Value"};

    /** the expand buttons. */
    private ExpandButton[] buttons;

    /** the introspector. */
    private Introspector introspector = null;

    /** The model manager. */
    private ModelManager manager = new DefaultModelManager();

    /**
     * Creates an ObjectTableModel utilizing a {see nl.tudelft.simulation.introspection.beans.BeanIntrospector}.
     * @param bean The object to be introspected according to the bean property-paradigm.
     */
    public ObjectTableModel(final Object bean)
    {
        this(bean, new BeanIntrospector());
    }

    /**
     * Creates an ObjectTableModel utilizing a custom introspector.
     * @param object The object to be introspected.
     * @param introspector The introspector instance utilized.
     */
    public ObjectTableModel(final Object object, final Introspector introspector)
    {
        this.properties = introspector.getProperties(object);
        this.buttons = new ExpandButton[this.properties.length];
        for (int i = 0; i < this.buttons.length; i++)
        {
            this.buttons[i] = new ExpandButton(this.properties[i], this);
        }
        this.introspector = introspector;
    }

    /** {@inheritDoc} */
    public int getRowCount()
    {
        return this.properties.length;
    }

    /** {@inheritDoc} */
    public int getColumnCount()
    {
        return columns.length;
    }

    /** {@inheritDoc} */
    public Object getValueAt(final int rowIndex, final int columnIndex)
    {
        Property requested = this.properties[rowIndex];
        if (columnIndex == 0)
        {
            return requested.getName();
        }
        if (columnIndex == 1)
        {
            return this.buttons[rowIndex];
        }
        if (columnIndex == 2)
        {
            return requested.getValue();
        }
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public String getColumnName(final int columnIndex)
    {
        return columns[columnIndex];
    }

    /** {@inheritDoc} */
    @Override
    public boolean isCellEditable(final int rowIndex, final int columnIndex)
    {
        if (columnIndex == 1)
        {
            return true;
        }
        if (columnIndex == 2)
        {
            return (this.properties[rowIndex].isEditable() && !this.properties[rowIndex].getType().isArray());
        }
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public void setValueAt(final Object aValue, final int rowIndex, final int columnIndex)
    {
        if ((columnIndex != 2) || (!isCellEditable(rowIndex, columnIndex)))
        {
            return;
        }
        Property requested = this.properties[rowIndex];
        try
        {
            requested.setValue(aValue);
        }
        catch (IllegalArgumentException exception)
        {
            Logger.warning(this, "setValueAt", exception);
        }
    }

    /** {@inheritDoc} */
    @Override
    public Class<?> getColumnClass(final int columnIndex)
    {
        if (columnIndex == 1)
        {
            return ExpandButton.class;
        }
        return Object.class;
    }

    /** {@inheritDoc} */
    public Class getTypeAt(final int rowIndex, final int columnIndex)
    {
        Property requested = this.properties[rowIndex];
        if (columnIndex == 0)
        {
            return String.class;
        }
        if (columnIndex == 1)
        {
            return ExpandButton.class;
        }
        if (columnIndex == 2)
        {
            return requested.getType();
        }
        return null;
    }

    /**
     * @param property the property
     * @return Returns the index of the property in this tablemodel which name matches 'property'.
     */
    protected int getPropertyIndex(final String property)
    {
        for (int i = 0; i < this.properties.length; i++)
        {
            if (this.properties[i].getName().equalsIgnoreCase(property))
            {
                return i;
            }
        }
        return -1;
    }

    /** {@inheritDoc} */
    public Property getProperty(final String propertyName)
    {
        int index = getPropertyIndex(propertyName);
        if (index == -1)
        {
            return null;
        }
        return this.properties[index];
    }

    /** {@inheritDoc} */
    public Introspector getIntrospector()
    {
        return this.introspector;
    }

    /**
     * Sets the modelmanager. By default, a {see DefaultModelManager}is used.
     * @param manager the manager
     */
    public void setModelManager(final ModelManager manager)
    {
        this.manager = manager;
    }

    /**
     * By default, a {see DefaultModelManager}returned.
     * @see nl.tudelft.simulation.introspection.gui.IntrospectingTableModelInterface #getModelManager()
     */
    public ModelManager getModelManager()
    {
        return this.manager;
    }
}
