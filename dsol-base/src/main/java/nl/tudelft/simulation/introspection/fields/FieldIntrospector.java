/*
 * @(#) DSOLIntrospector.java Apr 15, 2004 Copyright (c) 2002-2005 Delft
 * University of Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All
 * rights reserved. This software is proprietary information of Delft University
 * of Technology 
 */
package nl.tudelft.simulation.introspection.fields;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nl.tudelft.simulation.introspection.Introspector;
import nl.tudelft.simulation.introspection.Property;
import nl.tudelft.simulation.logger.Logger;

/**
 * The IntrospectionField introspector provides a field manipulation implementation of the introspection interfaces. Its
 * behavior adhers to the following:
 * <ul>
 * <li>Properties are discovered by searching for an object's fields (visibility neutral)</li>
 * <li>Property value are manipulated by setting field values (visibility neutral)</li>
 * </ul>
 * <p>
 * During construction, one can choose for either deep introspection (i.e. including fields from all superclasses), or
 * regular introspection (inlude declared fields only). (c) copyright 2002-2005-2004 <a
 * href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="http://web.eur.nl/fbk/dep/dep1/Introduction/Staff/People/Lang">Niels Lang </a><a
 *         href="http://www.peter-jacobs.com/index.htm">Peter Jacobs </a>
 * @version 1.1 Apr 15, 2004
 * @since 1.5
 */

public class FieldIntrospector implements Introspector
{
    /** useDeepIntrospection */
    private boolean useDeepIntrospection = true;

    /**
     * constructs a new FieldIntrospector
     */
    public FieldIntrospector()
    {
        this(false);
    }

    /**
     * constructs a new FieldIntrospector
     * @param useDeepIntrospection whether to use deep introspection
     */
    public FieldIntrospector(final boolean useDeepIntrospection)
    {
        this.useDeepIntrospection = useDeepIntrospection;
    }

    /**
     * @see nl.tudelft.simulation.introspection.Introspector#getProperties(Object)
     */
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
            Logger.warning(this, "getProperties", e);
        }
        return props.toArray(new Property[props.size()]);
    }

    /**
     * Collect the fields for the given class, taking the preference for deep introspection into account.
     * @param clasz the class to use
     * @return Field[] the fields
     */
    private Field[] collectFields(final Class clasz)
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

    /**
     * @see nl.tudelft.simulation.introspection.Introspector#getProperty(Object, String)
     */
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
            Logger.warning(this, "getProperty", e);
        }
        throw new IllegalArgumentException("Property '" + property + "' not found for " + introspected);
    }

    /**
     * @see nl.tudelft.simulation.introspection.Introspector#getPropertyNames(Object)
     */
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
            Logger.warning(this, "getPropertyNames", e);
        }
        return props.toArray(new String[props.size()]);
    }
}