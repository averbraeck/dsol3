package nl.tudelft.simulation.introspection;

import java.lang.reflect.Array;
import java.util.Collection;

/**
 * A default Property implementation that provides a standard way to handle composite values.
 * <p>
 * copyright (c) 2002-2018  <a href="http://www.simulation.tudelft.nl">Delft University of Technology</a>. <br>
 * BSD-style license. See <a href="http://www.simulation.tudelft.nl/dsol/3.0/license.html">DSOL License</a>. <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs</a>.
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>.
 * @author Niels Lang.
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
    @Override
    public void setValue(final Object value)
    {
        if (!this.isCollection())
        {
            this.setRegularValue(value);
            return;
        }
        if (!(value instanceof Collection))
        {
            throw new IllegalArgumentException(this + " - assign Collection values to composite properties");
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
                    throw new IllegalArgumentException(this + " - setValue: could not empty " + oldValues
                            + "setValue method canceled");
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
    @Override
    public boolean isCollection()
    {
        if (getType().isArray() || Collection.class.isAssignableFrom(getType()))
        {
            return true;
        }
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public Class<?> getComponentType()
    {
        if (!isCollection())
        {
            return null;
        }
        if (getType().isArray())
        {
            return getType().getComponentType();
        }
        Collection<?> value = (Collection<?>) getValue();
        if (value == null || value.size() == 0)
        {
            return null;
        }
        return value.toArray()[0].getClass();
    }
}
