package nl.tudelft.simulation.introspection.fields;

import java.lang.reflect.Field;

import nl.tudelft.simulation.introspection.AbstractProperty;
import nl.tudelft.simulation.introspection.Property;
import nl.tudelft.simulation.logger.Logger;

/**
 * The field implementation of the Property interface. See for details.
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
    public String getName()
    {
        return this.descriptor.getName();
    }

    /** {@inheritDoc} */
    public Class<?> getType()
    {
        return this.descriptor.getType();
    }

    /** {@inheritDoc} */
    @Override
    public void setRegularValue(final Object value)
    {
        Class type = this.descriptor.getType();
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
                Logger.warning(this, "setRegularValue", exception);
            }
        }
    }

    /** {@inheritDoc} */
    public Object getValue()
    {
        try
        {
            return this.descriptor.get(this.owner);
        }
        catch (Exception exception)
        {
            Logger.warning(this, "getValue", exception);
        }
        return null;
    }

    /** {@inheritDoc} */
    public Object getInstance()
    {
        return this.owner;
    }

    /** {@inheritDoc} */
    public boolean isEditable()
    {
        return this.editable;
    }
}
