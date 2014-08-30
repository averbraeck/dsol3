/*
 * @(#)Property.java April 14, 2004 Copyright (c) 2002-2005-2004 Delft
 * University of Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All
 * rights reserved. This software is proprietary information of Delft University
 * of Technology 
 */
package nl.tudelft.simulation.introspection;

/**
 * A property defines a characteristic of an object. It has a name, a type and provides methods to view and alter its
 * value. Different introspection implementation may provide different definitions for what exactly are regarded to be
 * the 'properties' of an object.
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
public interface Property
{
    /**
     * Retrieves the name of the property.
     * @return The name of the property
     */
    String getName();

    /**
     * Returns the type of this property's value.
     * @return A {see java.lang.Class}instance denoting the type of this property.
     */
    Class<?> getType();

    /**
     * Returns whether the value of this property may be altered.
     * @return 'True', when this property's value can be altered, 'false' otherwise.
     */
    boolean isEditable();

    /**
     * Set the value of this property. However, if isEditable() returns 'false', the value of this property will not be
     * altered. Composite property values (i.e. {see java.util.Collection}or arrays) should be provided as an instance
     * of {see java.util.Collection}.
     * @param value The new value of this property.
     */
    void setValue(Object value);

    /**
     * Returns the current value of this property.
     * @return The current value of this property.
     */
    Object getValue();

    /**
     * Retrieves the introspected object, which contains this Property.
     * @return the instance
     */
    Object getInstance();

    /**
     * Returns whether the contained value is a collection (i.e. is a composite value). The definition whether a value
     * is considered composite depends on the property paradigm used by this Property.
     * @return true, if the contained value is a collection, false otherwise.
     */
    boolean isCollection();

    /**
     * Returns the type of the collection components contained in this Property.
     * @return The type of the collection components contained in this Property. Returns null when isCollection() returns
     *         false, or when the component type could not be determined by this Property.
     */
    Class getComponentType();
}