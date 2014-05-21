/*
 * Created on May 8, 2004
 */
package nl.tudelft.simulation.introspection.gui;

/**
 * Manages the object model classes for an object model. Allows object models to instantiate appropriate new object
 * models.
 * @author Niels Lang
 */
public interface ModelManager
{
    /**
     * @return Returns the class of the default collection object table model
     */
    Class getDefaultCollectionObjectTableModel();

    /**
     * @return Returns the class of the default object table model
     */
    Class getDefaultObjectTableModel();

    /**
     * sets the class of the default collection object table model
     * @param defaultCollectionObjectTableModel the class
     */
    void setDefaultCollectionObjectTableModel(Class defaultCollectionObjectTableModel);

    /**
     * sets the class of the default object table model
     * @param defaultObjectTableModel the class
     */
    void setDefaultObjectTableModel(Class defaultObjectTableModel);
}