package nl.tudelft.simulation.introspection.beans;

import java.beans.BeanInfo;
import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.Set;

import nl.tudelft.simulation.introspection.Introspector;
import nl.tudelft.simulation.introspection.Property;

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
 * copyright (c) 2002-2018  <a href="https://simulation.tudelft.nl">Delft University of Technology</a>. <br>
 * BSD-style license. See <a href="https://simulation.tudelft.nl/dsol/3.0/license.html">DSOL License</a>. <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs</a>.
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>.
 * @author Niels Lang.
 * @since 1.5
 */
public class BeanIntrospector implements Introspector
{
    /** {@inheritDoc} */
    @Override
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
            throw new IllegalArgumentException(this + " - getProperties", e);
        }
        return props.toArray(new Property[props.size()]);
    }

    /** {@inheritDoc} */
    @Override
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
            BeanInfo info = java.beans.Introspector.getBeanInfo(introspected.getClass());
            PropertyDescriptor[] descrips = info.getPropertyDescriptors();
            for (int i = 0; i < descrips.length; i++)
            {
                props.add(descrips[i].getName());
            }
        }
        catch (Exception e)
        {
            throw new IllegalArgumentException(this + " - getPropertyNames", e);
        }
        return props.toArray(new String[props.size()]);
    }
}
