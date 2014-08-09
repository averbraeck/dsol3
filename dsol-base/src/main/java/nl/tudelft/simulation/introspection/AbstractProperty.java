/*
 * @(#)AbstractProperty.java April 15, 2004 Copyright (c) 2002-2005-2004 Delft
 * University of Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All
 * rights reserved. This software is proprietary information of Delft University
 * of Technology 
 */
package nl.tudelft.simulation.introspection;

import java.lang.reflect.Array;
import java.util.Collection;

import nl.tudelft.simulation.logger.Logger;

/**
 * A default Property implementation that provides a standard way to handle composite values.
 * <p>
 * (c) copyright 2002-2005-2004 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="http://web.eur.nl/fbk/dep/dep1/Introduction/Staff/People/Lang">Niels Lang </a><a
 *         href="http://www.peter-jacobs.com/index.htm">Peter Jacobs </a>
 * @version 1.1 Apr 15, 2004
 * @since 1.5
 */
public abstract class AbstractProperty implements Property
{
    /**
     * Basic 'setValue' implementation. It is checked whether this property contains a composite value. If so, the
     * composite value of this property is updated. Composite values are expected to be supplied as a {see
     * java.util.Collection}. If needed, array conversion takes place. If the property is not composite, the
     * value-setting is delegated to the 'setRegularValue' method.
     * @see nl.tudelft.simulation.introspection.Property#setValue(java.lang.Object)
     */
    @SuppressWarnings("unchecked")
    public void setValue(final Object value)
    {
        if (!this.isCollection())
        {
            this.setRegularValue(value);
            return;
        }
        if (!(value instanceof Collection))
        {
            throw new IllegalArgumentException(this + "assign Collection values to composite properties");
        }
        if (this.getType().isArray())
        {
            Object[] array = (Object[]) Array.newInstance(getType().getComponentType(), 0);
            this.setRegularValue(((Collection<?>) value).toArray(array));
        }
        else
        {
            synchronized (this.getInstance())
            {
                Collection<Object> oldValues = (Collection<Object>) getValue();
                try
                {
                    oldValues.clear();
                    oldValues.addAll((Collection<Object>) value);
                }
                catch (UnsupportedOperationException e)
                {
                    Logger.warning(this, "setValue", "could not empty " + oldValues + "setValue method canceled");
                }
            }
        }

    }

    /**
     * Method used to set a regular (i.e. not-composite) property value.
     * @param value the new value
     */
    protected abstract void setRegularValue(final Object value);

    /**
     * Returns true when the contained value is either an array, or an instance of {see java.util.Collection}, i.e. is a
     * property with composite value.
     * @see nl.tudelft.simulation.introspection.Property#isCollection()
     */
    public boolean isCollection()
    {
        if (getType().isArray() || Collection.class.isAssignableFrom(getType()))
        {
            return true;
        }
        return false;
    }

    /**
     * @see nl.tudelft.simulation.introspection.Property#getComponentType()
     */
    public Class getComponentType()
    {
        if (!isCollection())
        {
            return null;
        }
        if (getType().isArray())
        {
            return getType().getComponentType();
        }
        Collection value = (Collection) getValue();
        if (value == null || value.size() == 0)
        {
            return null;
        }
        return value.toArray()[0].getClass();
    }
}