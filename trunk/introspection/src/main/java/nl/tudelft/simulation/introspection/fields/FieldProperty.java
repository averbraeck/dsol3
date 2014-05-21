/*
 * @(#) FieldProperty.java Apr 15, 2004 Copyright (c) 2002-2005 Delft University
 * of Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. This software is proprietary information of Delft University of
 * Technology 
 */
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
 *         href="http://www.peter-jacobs.com/index.htm">Peter Jacobs </a>
 * @version 1.1 Apr 15, 2004
 * @since 1.5
 */
public class FieldProperty extends AbstractProperty implements Property
{
    /** the owner of the fieldProperty */
    private Object owner = null;

    /** the descriptor of the field */
    private Field descriptor = null;

    /** is the property editable */
    private boolean editable = false;

    /**
     * constructs a new FieldProperty
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
     * constructs a new FieldProperty
     * @param owner its owner
     * @param descriptor the descriptor
     */
    public FieldProperty(final Object owner, final Field descriptor)
    {
        this(owner, descriptor, true);
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
        return this.descriptor.getType();
    }

    /**
     * @see nl.tudelft.simulation.introspection.AbstractProperty #setRegularValue(java.lang.Object)
     */
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

    /**
     * @see nl.tudelft.simulation.introspection.Property#getValue()
     */
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

    /**
     * @see nl.tudelft.simulation.introspection.Property#getInstance()
     */
    public Object getInstance()
    {
        return this.owner;
    }

    /**
     * @see nl.tudelft.simulation.introspection.Property#isEditable()
     */
    public boolean isEditable()
    {
        return this.editable;
    }
}