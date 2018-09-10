package nl.tudelft.simulation.introspection.gui;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import nl.tudelft.simulation.introspection.AbstractProperty;
import nl.tudelft.simulation.introspection.Introspector;
import nl.tudelft.simulation.introspection.Property;
import nl.tudelft.simulation.introspection.beans.BeanIntrospector;
import nl.tudelft.simulation.introspection.table.DynamicTableModel;

/**
 * A tablemodel used to manage and present the instances of a composite property.
 * <p>
 * copyright (c) 2002-2018  <a href="https://simulation.tudelft.nl">Delft University of Technology</a>. <br>
 * BSD-style license. See <a href="https://simulation.tudelft.nl/dsol/3.0/license.html">DSOL License</a>. <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs</a>.
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>.
 * @author Niels Lang.
 * @since 1.5
 */
public class CollectionTableModel extends AbstractTableModel implements IntrospectingTableModelInterface,
        DynamicTableModel
{
    /** */
    private static final long serialVersionUID = 20140831L;

    /** the instances of the collection. */
    protected Map<Integer, Object> instances = Collections.synchronizedMap(new HashMap<Integer, Object>(20));

    /** the keys identifying specific instances. */
    protected List<Integer> keys = Collections.synchronizedList(new ArrayList<Integer>(20));

    /** the componentType. */
    private Class<?> componentType = null;

    /** the COLUMNS of this tabbleModel. */
    private static final String[] COLUMNS = {"#", "+", "Instance"};

    /** the expand button. */
    private List<ExpandButton> buttons = Collections.synchronizedList(new ArrayList<ExpandButton>(20));

    /** the parentProperty */
    private Property parentProperty;

    /** the introspector. */
    private Introspector introspector;

    /** The model manager. */
    private ModelManager manager = new DefaultModelManager();

    /** The highest key currently allocated. */
    private int maxKey = 0;
    
    /** the logger. */
    private static Logger logger = LogManager.getLogger(CollectionTableModel.class);

    /**
     * constructs a new CollectionTableModel.
     * @param parentProperty the parentPropert
     */
    public CollectionTableModel(final Property parentProperty)
    {
        this(parentProperty, new BeanIntrospector());
    }

    /**
     * constructs a new CollectionTableModel.
     * @param parentProperty the parentProperty
     * @param introspector the introspector to use
     */
    public CollectionTableModel(final Property parentProperty, final Introspector introspector)
    {
        Object values;
        try
        {
            values = parentProperty.getValue();
        }
        catch (Exception e)
        {
            values = new String("-");
        }
        if (values.getClass().isArray())
        {
            for (int i = 0; i < Array.getLength(values); i++)
            {
                addValue(Array.get(values, i));
            }
        }
        if (values instanceof Collection)
        {
            for (Iterator<?> i = ((Collection<?>) values).iterator(); i.hasNext();)
            {
                addValue(i.next());
            }
        }
        this.parentProperty = parentProperty;
        this.introspector = introspector;
        // Initialize buttons
        for (int i = 0; i < this.instances.size(); i++)
        {
            this.buttons.add(new ExpandButton(getProperty(i), this));
        }
    }

    /**
     * Adds a new value to the managed composite property.
     * @param value the value to add
     */
    private void addValue(final Object value)
    {
        Integer nextKey = new Integer(this.maxKey++);
        this.keys.add(nextKey);
        this.instances.put(nextKey, value);
    }

    /** {@inheritDoc} */
    @Override
    public int getRowCount()
    {
        return this.instances.size();
    }

    /** {@inheritDoc} */
    @Override
    public int getColumnCount()
    {
        return CollectionTableModel.COLUMNS.length;
    }

    /** {@inheritDoc} */
    @Override
    public Object getValueAt(final int rowIndex, final int columnIndex)
    {
        if (columnIndex == 0)
        {
            return new Integer(rowIndex);
        }
        if (columnIndex == 1)
        {
            return this.buttons.get(rowIndex);
        }
        if (columnIndex == 2)
        {
            return this.instances.get(this.keys.get(rowIndex));
        }
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public String getColumnName(final int columnIndex)
    {
        return CollectionTableModel.COLUMNS[columnIndex];
    }

    /** {@inheritDoc} */
    @Override
    public boolean isCellEditable(final int rowIndex, final int columnIndex)
    {
        if (columnIndex == 1 || columnIndex == 2)
        {
            return true;
        }
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public void setValueAt(final Object aValue, final int rowIndex, final int columnIndex)
    {
        if (columnIndex == 2)
        {
            Integer key = this.keys.get(rowIndex);
            this.instances.put(key, aValue);
        }
        this.update();
    }

    /**
     * updates the tableModel
     */
    private void update()
    {
        // Generate a List reflecting changes
        List<Object> newValue = new ArrayList<Object>(this.keys.size());
        for (int i = 0; i < this.keys.size(); i++)
        {
            newValue.add(this.instances.get(this.keys.get(i)));
        }
        this.parentProperty.setValue(newValue);
        this.fireTableDataChanged();
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

    /**
     * The collection table model labels all properties according to their rowIndex. Only these labels are expected to
     * be requested here.
     * @see nl.tudelft.simulation.introspection.gui.IntrospectingTableModelInterface #getProperty(java.lang.String)
     */
    @Override
    public Property getProperty(final String propertyName)
    {
        int index = Integer.parseInt(propertyName);
        return getProperty(index);
    }

    /**
     * @param index the index of the property
     * @return the Property
     */
    protected Property getProperty(final int index)
    {
        return new CollectionProperty(this.keys.get(index), this.parentProperty.getName());
    }

    /**
     * The CollectionProperty
     */
    class CollectionProperty extends AbstractProperty implements Property
    {
        /** the key of this property */
        private final Integer key;

        /** the name. */
        private final String name;

        /**
         * This implementation is NOT thread-safe. When multiple users will edit the parent at the same time, errors are
         * expected.
         * @param key the key
         * @param name the name
         */
        public CollectionProperty(Integer key, String name)
        {
            this.key = key;
            this.name = name;
        }

        /** {@inheritDoc} */
        @Override
        public Object getInstance()
        {
            return CollectionTableModel.this.instances.values();
        }

        /** {@inheritDoc} */
        @Override
        public String getName()
        {
            return this.name + "[" + CollectionTableModel.this.keys.indexOf(this.key) + "]";
        }

        /** {@inheritDoc} */
        @Override
        public Class<?> getType()
        {
            return CollectionTableModel.this.instances.get(this.key).getClass();
        }

        /** {@inheritDoc} */
        @Override
        public Object getValue()
        {
            try
            {
                return CollectionTableModel.this.instances.get(this.key);
            }
            catch (Exception e)
            {
                return new String("-");
            }
        }

        /** {@inheritDoc} */
        @Override
        public boolean isEditable()
        {
            return true;
        }

        /** {@inheritDoc} */
        @Override
        protected void setRegularValue(final Object value)
        {
            throw new IllegalArgumentException(this + " is only supposed to be" + " set to composite values."
                    + "A program is not supposed to arrive here.");
        }

        /** {@inheritDoc} */
        @Override
        public String toString()
        {
            return "Coll.Prop, key:" + this.key;
        }
    }

    /** {@inheritDoc} */
    @Override
    public void createRow()
    {
        if (this.componentType == null)
        {
            this.componentType = this.parentProperty.getComponentType();
            if (this.componentType == null)
            {
                return;
            }
        }
        try
        {
            Constructor<?> instanceConstructor = this.componentType.getConstructor(new Class[0]);
            Object instance = instanceConstructor.newInstance(new Object[0]);
            addValue(instance);
            this.buttons.add(new ExpandButton(getProperty(this.instances.size() - 1), this));
            update();
        }
        catch (Exception e)
        {
            logger.warn("createRow: Could not instantiate new instance: ", e);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void createRows(final int amount)
    {
        for (int i = 0; i < amount; i++)
        {
            this.createRow();
        }
    }

    /** {@inheritDoc} */
    @Override
    public void deleteRow(final int index)
    {
        Integer deletionKey = this.keys.get(index);
        this.instances.remove(deletionKey);
        this.keys.remove(index);
        this.buttons.remove(index);
        update();
    }

    /** {@inheritDoc} */
    @Override
    public synchronized void deleteRows(final int[] indices)
    {
        Arrays.sort(indices);
        for (int i = indices.length - 1; i >= 0; i--)
        {
            deleteRow(indices[i]);
        }
    }

    /** {@inheritDoc} */
    @Override
    public Introspector getIntrospector()
    {
        return this.introspector;
    }

    /** {@inheritDoc} */
    @Override
    public Class<?> getTypeAt(final int rowIndex, final int columnIndex)
    {
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
            return this.instances.get(this.keys.get(rowIndex)).getClass();
        }
        return null;
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
     * @return the Manager
     */
    @Override
    public ModelManager getModelManager()
    {
        return this.manager;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isRowEditable()
    {
        return this.parentProperty.isEditable();
    }
}
