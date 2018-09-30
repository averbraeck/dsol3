/*
 * Created on May 8, 2004
 */
package nl.tudelft.simulation.introspection.gui;

/**
 * Manages the object model classes for an object model. Allows object models to instantiate appropriate new object
 * models.
 * <p>
 * copyright (c) 2002-2018  <a href="https://simulation.tudelft.nl">Delft University of Technology</a>. <br>
 * BSD-style license. See <a href="https://simulation.tudelft.nl/dsol/3.0/license.html">DSOL License</a>. <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs</a>.
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>.
 * @author Niels Lang.
 */
public interface ModelManager
{
    /**
     * @return Returns the class of the default collection object table model
     */
    Class<?> getDefaultCollectionObjectTableModel();

    /**
     * @return Returns the class of the default object table model
     */
    Class<?> getDefaultObjectTableModel();

    /**
     * sets the class of the default collection object table model.
     * @param defaultCollectionObjectTableModel the class
     */
    void setDefaultCollectionObjectTableModel(Class<?> defaultCollectionObjectTableModel);

    /**
     * sets the class of the default object table model.
     * @param defaultObjectTableModel the class
     */
    void setDefaultObjectTableModel(Class<?> defaultObjectTableModel);
}
