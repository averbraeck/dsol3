package nl.tudelft.simulation.introspection;

/**
 * The introspector provides introspection services, i.e. property discovery and manipulation, for any object.
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
