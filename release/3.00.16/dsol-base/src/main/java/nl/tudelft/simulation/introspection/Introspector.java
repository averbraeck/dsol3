package nl.tudelft.simulation.introspection;

/**
 * The introspector provides introspection services, i.e. property discovery and manipulation, for any object.
 * <p>
 * (c) copyright 2002-2014 <a href="http://www.simulation.tudelft.nl">Delft University of Technology</a>. <br>
 * BSD-style license. See <a href="http://www.simulation.tudelft.nl/dsol/3.0/license.html">DSOL License</a>. <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs</a>.
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>.
 * @author Niels Lang.
 * @since 1.5
 */
public interface Introspector
{
    /**
     * @param introspected the introspected object
     * @return Retrieves properties of the introspected object. The properties' values can themselves be introspectable.
     *         An empty array is returned if no introspected object has been set.
     */
    Property[] getProperties(Object introspected);

    /**
     * Retrieves the names of the properties of the introspected object.
     * @param introspected The introspected object.
     * @return An unordered array of the introspected object's property names.
     */
    String[] getPropertyNames(Object introspected);

    /**
     * Retrieves the {see Property}with a given name from an introspected object.
     * @param introspected The introspected object.
     * @param property The name of the property to be retrieved
     * @return A {see Property}instance for the given object and property name.
     */
    Property getProperty(Object introspected, String property);
}
