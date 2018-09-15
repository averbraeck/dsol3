package nl.tudelft.simulation.introspection.fields;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nl.tudelft.simulation.introspection.Introspector;
import nl.tudelft.simulation.introspection.Property;

/**
 * The IntrospectionField introspector provides a field manipulation implementation of the introspection interfaces. Its
 * behavior adhers to the following:
 * <ul>
 * <li>Properties are discovered by searching for an object's fields (visibility neutral)</li>
 * <li>Property value are manipulated by setting field values (visibility neutral)</li>
 * </ul>
 * <p>
 * copyright (c) 2002-2018  <a href="https://simulation.tudelft.nl">Delft University of Technology</a>. <br>
 * BSD-style license. See <a href="https://simulation.tudelft.nl/dsol/3.0/license.html">DSOL License</a>. <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs</a>.
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>.
 * @author Niels Lang.
 * @since 1.5
 */

public class FieldIntrospector implements Introspector
{
    /** useDeepIntrospection. */
    private boolean useDeepIntrospection = true;

    /**
     * constructs a new FieldIntrospector.
     */
    public FieldIntrospector()
    {
        this(false);
    }

    /**
     * constructs a new FieldIntrospector.
     * @param useDeepIntrospection whether to use deep introspection
     */
    public FieldIntrospector(final boolean useDeepIntrospection)
    {
        this.useDeepIntrospection = useDeepIntrospection;
    }

    /** {@inheritDoc} */
    @Override
    public Property[] getProperties(final Object introspected)
    {
        Set<Property> props = new HashSet<Property>();
        try
        {
            Field[] fields = collectFields(introspected.getClass());
            for (int i = 0; i < fields.length; i++)
            {
                props.add(new FieldProperty(introspected, fields[i]));
            }
        }
        catch (Exception e)
        {
            throw new IllegalArgumentException(this + " - getProperties", e);
        }
        return props.toArray(new Property[props.size()]);
    }

    /**
     * Collect the fields for the given class, taking the preference for deep introspection into account.
     * @param clasz the class to use
     * @return Field[] the fields
     */
    private Field[] collectFields(final Class<?> clasz)
    {
        List<Field> fields = new ArrayList<Field>(10);
        this.addFields(fields, clasz, this.useDeepIntrospection);
        return fields.toArray(new Field[fields.size()]);
    }

    /**
     * Add fields of 'clasz' to the fieldList. Optionally iterate over the class hierarchy.
     * @param fieldList the fieldList
     * @param clasz the class
     * @param iterate whether to iterate
     */
    private void addFields(final List<Field> fieldList, final Class<?> clasz, final boolean iterate)
    {
        fieldList.addAll(Arrays.asList(clasz.getDeclaredFields()));
        if (iterate && clasz.getSuperclass() != null)
        {
            addFields(fieldList, clasz.getSuperclass(), iterate);
        }
    }

    /** {@inheritDoc} */
    @Override
    public Property getProperty(final Object introspected, final String property)
    {
        try
        {
            Field[] fields = collectFields(introspected.getClass());
            for (int i = 0; i < fields.length; i++)
            {
                if (fields[i].getName().equals(property))
                {
                    return new FieldProperty(introspected, fields[i]);
                }
            }
        }
        catch (Exception e)
        {
            throw new IllegalArgumentException(this + " - getProperty", e);
        }
        throw new IllegalArgumentException("Property '" + property + "' not found for " + introspected);
    }

    /** {@inheritDoc} */
    @Override
    public String[] getPropertyNames(final Object introspected)
    {
        Set<String> props = new HashSet<String>();
        try
        {
            Field[] fields = collectFields(introspected.getClass());
            for (int i = 0; i < fields.length; i++)
            {
                props.add(fields[i].getName());
            }
        }
        catch (Exception e)
        {
            throw new IllegalArgumentException(this + " - getPropertyNames", e);
        }
        return props.toArray(new String[props.size()]);
    }
}
