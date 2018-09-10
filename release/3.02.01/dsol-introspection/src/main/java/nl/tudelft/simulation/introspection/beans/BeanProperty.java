package nl.tudelft.simulation.introspection.beans;

import java.beans.PropertyDescriptor;
import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;
import java.lang.reflect.Method;

import nl.tudelft.simulation.introspection.AbstractProperty;
import nl.tudelft.simulation.introspection.Property;

/**
 * The JavaBean TM implementation of the Property interface. See {see BeanIntrospector}for details.
 * <p>
 * copyright (c) 2002-2018  <a href="https://simulation.tudelft.nl">Delft University of Technology</a>. <br>
 * BSD-style license. See <a href="https://simulation.tudelft.nl/dsol/3.0/license.html">DSOL License</a>. <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs</a>.
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>.
 * @author Niels Lang.
 * @since 1.5
 */

public class BeanProperty extends AbstractProperty implements Property
{
    /** the bean that owns the property. */
    private Object bean = null;

    /** the propertyDescriptor. */
    private PropertyDescriptor descriptor = null;

    /**
     * constructs a new BeanProperty.
     * @param bean the bean to introspect
     * @param descriptor the descriptor of the property
     */
    protected BeanProperty(final Object bean, final PropertyDescriptor descriptor)
    {
        this.bean = bean;
        this.descriptor = descriptor;
    }

    /** {@inheritDoc} */
    @Override
    public String getName()
    {
        return this.descriptor.getName();
    }

    /** {@inheritDoc} */
    @Override
    public Class<?> getType()
    {
        return this.descriptor.getPropertyType();
    }

    /** {@inheritDoc} */
    @Override
    protected void setRegularValue(final Object values)
    {
        Class<?> type = this.descriptor.getPropertyType();
        PropertyEditor editor = PropertyEditorManager.findEditor(type);
        Object newValue = values;
        if (editor != null)
        {
            if (values instanceof String)
            {
                editor.setAsText((String) values);
            }
            else
            {
                editor.setValue(values);
            }
            newValue = editor.getValue();
        }
        Method writeMethod = this.descriptor.getWriteMethod();
        try
        {
            writeMethod.invoke(this.bean, new Object[]{newValue});
        }
        catch (Throwable throwable)
        {
            throw new IllegalArgumentException(this + " - setRegularValue", throwable);
        }
    }

    /** {@inheritDoc} */
    @Override
    public Object getValue()
    {
        Object result = null;
        Method readMethod = this.descriptor.getReadMethod();
        try
        {
            if (readMethod != null)
            {
                result = readMethod.invoke(this.bean, new Object[0]);
            }
        }
        catch (Exception exception)
        {
            throw new IllegalArgumentException(this + "getValue of " + getName(), exception);
        }
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public Object getInstance()
    {
        return this.bean;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isEditable()
    {
        return !(this.descriptor.getWriteMethod() == null);
    }
}
