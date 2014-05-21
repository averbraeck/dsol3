/*
 * @(#)AbstractSupressIntrospector.java April 15, 2004 Copyright (c)
 * 2002-2005-2004 Delft University of Technology Jaffalaan 5, 2628 BX Delft, the
 * Netherlands. All rights reserved. This software is proprietary information of
 * Delft University of Technology The code is published under the Lesser General
 * Public License
 */
package nl.tudelft.simulation.introspection;

import java.util.ArrayList;
import java.util.List;

/**
 * The AbstractSupressIntrospector.
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
public abstract class AbstractSuppressIntrospector implements Introspector
{
    /** the parent introspector */
    protected Introspector parent;

    /**
     * Constructor for AbstractSuppressIntrospector.
     * @param parent the parent introspector
     */
    public AbstractSuppressIntrospector(final Introspector parent)
    {
        super();
        this.parent = parent;
    }

    /**
     * @see nl.tudelft.simulation.introspection.Introspector#getProperties(Object)
     */
    public Property[] getProperties(final Object arg0)
    {
        Property[] original = this.parent.getProperties(arg0);
        List<Property> result = new ArrayList<Property>();
        for (int i = 0; i < original.length; i++)
        {
            if (!this.suppress(original[i].getType()) && !this.suppress(original[i].getName()))
            {
                result.add(original[i]);
            }
        }
        return result.toArray(new Property[0]);
    }

    /**
     * @see nl.tudelft.simulation.introspection.Introspector#getPropertyNames(Object)
     */
    public String[] getPropertyNames(final Object arg0)
    {
        Property[] properties = this.getProperties(arg0);
        String[] result = new String[properties.length];
        for (int i = 0; i < properties.length; i++)
        {
            result[i] = properties[i].getName();
        }
        return result;
    }

    /**
     * @see nl.tudelft.simulation.introspection.Introspector#getProperty(Object, String)
     */
    public Property getProperty(final Object arg0, final String arg1)
    {
        Property[] properties = this.getProperties(arg0);
        for (int i = 0; i < properties.length; i++)
        {
            if (properties[i].getName().equals(arg1))
            {
                return properties[i];
            }
        }
        return null;
    }

    /**
     * Method suppress.
     * @param type the type of tyhe class
     * @return boolean whether to supress
     */
    protected boolean suppress(final Class type)
    {
        if (type.equals(Class.class))
        {
            return true;
        }
        return false;
    }

    /**
     * Method suppress.
     * @param propertyName the propertyName
     * @return whether to supress
     */
    protected abstract boolean suppress(final String propertyName);
}