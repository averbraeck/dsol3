package nl.tudelft.simulation.introspection.fields;

import java.lang.reflect.Field;

import nl.tudelft.simulation.introspection.AbstractProperty;
import nl.tudelft.simulation.introspection.Property;

/**
 * The field implementation of the Property interface. See for details.
 * <p>
 * copyright (c) 2002-2018  <a href="http://www.simulation.tudelft.nl">Delft University of Technology</a>. <br>
 * BSD-style license. See <a href="http://www.simulation.tudelft.nl/dsol/3.0/license.html">DSOL License</a>. <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs</a>.
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>.
 * @author Niels Lang.
 * @since 1.5
 */
public class FieldProperty extends AbstractProperty implements Property
{
    /** the owner of the fieldProperty */
    private Object owner = null;

    /** the descriptor of the field. */
    private Field descriptor = null;

    /** is the property editable. */
    private boolean editable = false;

    /**
     * constructs a new FieldProperty.
     * @param owner its owner
     * @param descriptor the descriptor
     * @param editable is the property editable
     */
    public FieldProperty(final Object owner, final Field descriptor, final boolean editable)
    {
        // Check whether descriptor is valid for owner should be conducted here
        this.owner = owner;
        this.descriptor = descriptor;
        this.descriptor.setAccessible(true);
        this.editable = editable;
    }

    /**
     * constructs a new FieldProperty.
     * @param owner its owner
     * @param descriptor the descriptor
     */
    public FieldProperty(final Object owner, final Field descriptor)
    {
        this(owner, descriptor, true);
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
        return this.descriptor.getType();
    }

    /** {@inheritDoc} */
    @Override
    public void setRegularValue(final Object value)
    {
        Class<?> type = this.descriptor.getType();
        if (!type.isInstance(value) || !this.editable)
        {
            throw new IllegalArgumentException("Cannot assign " + value + " to " + this.owner + ", " + this.descriptor);
        }
        synchronized (this.owner)
        {
            try
            {
                this.descriptor.set(this.owner, value);
            }
            catch (Exception exception)
            {
                throw new IllegalArgumentException(this + " - setRegularValue", exception);
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    public Object getValue()
    {
        try
        {
            return this.descriptor.get(this.owner);
        }
        catch (Exception exception)
        {
            throw new IllegalArgumentException(this + " - getValue", exception);
        }
    }

    /** {@inheritDoc} */
    @Override
    public Object getInstance()
    {
        return this.owner;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isEditable()
    {
        return this.editable;
    }
}
