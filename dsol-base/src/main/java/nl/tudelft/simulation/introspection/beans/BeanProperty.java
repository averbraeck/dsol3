/*
 * @(#) BeanProperty.java Apr 15, 2004 Copyright (c) 2002-2005 Delft University
 * of Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. This software is proprietary information of Delft University of
 * Technology 
 */
package nl.tudelft.simulation.introspection.beans;

import java.beans.PropertyDescriptor;
import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;
import java.lang.reflect.Method;

import nl.tudelft.simulation.introspection.AbstractProperty;
import nl.tudelft.simulation.introspection.Property;
import nl.tudelft.simulation.logger.Logger;

/**
 * The JavaBean TM implementation of the Property interface. See {see BeanIntrospector}for details.
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

public class BeanProperty extends AbstractProperty implements Property
{
    /** the bean whichs owns the property */
    private Object bean = null;

    /** the propertyDescriptor */
    private PropertyDescriptor descriptor = null;

    /**
     * constructs a new BeanProperty
     * @param bean the bean to introspect
     * @param descriptor the descriptor of the property
     */
    protected BeanProperty(final Object bean, final PropertyDescriptor descriptor)
    {
        this.bean = bean;
        this.descriptor = descriptor;
    }

    /**
     * @see nl.tudelft.simulation.introspection.Property#getName()
     */
    public String getName()
    {
        return this.descriptor.getName();
    }

    /**
     * @see nl.tudelft.simulation.introspection.Property#getType()
     */
    public Class<?> getType()
    {
        return this.descriptor.getPropertyType();
    }

    /**
     * @see nl.tudelft.simulation.introspection.AbstractProperty #setRegularValue(java.lang.Object)
     */
    @Override
    protected void setRegularValue(final Object values)
    {
        Class type = this.descriptor.getPropertyType();
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
            Logger.warning(this, "setRegularValue", throwable);
        }
    }

    /**
     * @see nl.tudelft.simulation.introspection.Property#getValue()
     */
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
            Logger.warning(this, "getValue of " + getName(), exception);
        }
        return result;
    }

    /**
     * @see nl.tudelft.simulation.introspection.Property#getInstance()
     */
    public Object getInstance()
    {
        return this.bean;
    }

    /**
     * @see nl.tudelft.simulation.introspection.Property#isEditable()
     */
    public boolean isEditable()
    {
        return !(this.descriptor.getWriteMethod() == null);
    }
}