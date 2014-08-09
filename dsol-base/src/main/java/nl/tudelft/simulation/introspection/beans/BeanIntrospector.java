/*
 * @(#) BeanIntrospector.java Apr 15, 2004 Copyright (c) 2002-2005 Delft
 * University of Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All
 * rights reserved. This software is proprietary information of Delft University
 * of Technology 
 */
package nl.tudelft.simulation.introspection.beans;

import java.beans.BeanInfo;
import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.Set;

import nl.tudelft.simulation.introspection.Introspector;
import nl.tudelft.simulation.introspection.Property;
import nl.tudelft.simulation.logger.Logger;

/**
 * The Bean introspector provides a simplified JavaBean TM implementation of the introspection interfaces. Its behavior
 * adhers to the following:
 * <ul>
 * <li>Properties are discovered by searching for 'getter' and / or 'setter' methods</li>
 * <li>Property value are manipulated via a property's 'setter' method. If no such method is found, the property cannot
 * be altered</li>
 * <li>Indexed properties are probably not correctly supported.</li>
 * </ul>
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
public class BeanIntrospector implements Introspector
{
    /**
     * @see nl.tudelft.simulation.introspection.Introspector#getProperties(Object)
     */
    public Property[] getProperties(final Object introspected)
    {
        Set<Property> props = new HashSet<Property>();
        try
        {
            BeanInfo info = java.beans.Introspector.getBeanInfo(introspected.getClass());

            PropertyDescriptor[] descrips = info.getPropertyDescriptors();
            for (int i = 0; i < descrips.length; i++)
            {
                props.add(new BeanProperty(introspected, descrips[i]));
            }
        }
        catch (Exception e)
        {
            Logger.warning(this, "getProperties", e);
        }
        return props.toArray(new Property[props.size()]);
    }

    /**
     * @see nl.tudelft.simulation.introspection.Introspector#getProperty(Object, String)
     */
    public Property getProperty(final Object introspected, final String property)
    {
        try
        {
            BeanInfo info = java.beans.Introspector.getBeanInfo(introspected.getClass());
            PropertyDescriptor[] descrips = info.getPropertyDescriptors();
            for (int i = 0; i < descrips.length; i++)
            {
                if (descrips[i].getName().equals(property))
                {
                    return new BeanProperty(introspected, descrips[i]);
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
            BeanInfo info = java.beans.Introspector.getBeanInfo(introspected.getClass());
            PropertyDescriptor[] descrips = info.getPropertyDescriptors();
            for (int i = 0; i < descrips.length; i++)
            {
                props.add(descrips[i].getName());
            }
        }
        catch (Exception e)
        {
            Logger.warning(this, "getPropertyNames", e);
        }
        return props.toArray(new String[props.size()]);
    }
}