package nl.tudelft.simulation.dsol.swing.introspection.gui;

import javax.swing.table.TableModel;

import nl.tudelft.simulation.introspection.Introspector;
import nl.tudelft.simulation.introspection.Property;

/**
 * Defines the minimum contract for a TableModel providing additional introspection services. The contract is used to
 * facilitate communication between an {see ObjectJTable}and an introspecting TableModel, especially to allow an
 * ObjectJTable to create additional ObjectJTable instances.
 * <p>
 * Copyright (c) 2002-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. See for project information <a href="https://simulation.tudelft.nl/" target="_blank">
 * https://simulation.tudelft.nl</a>. The DSOL project is distributed under a three-clause BSD-style license, which can
 * be found at <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs</a>.
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>.
 * @author Niels Lang.
 * @since 1.5
 */
public interface IntrospectingTableModelInterface extends TableModel
{
    /**
     * Returns the Property instance generated by this IntrospectingTableModelInterface for the property 'propertyName'.
     * @param propertyName String; The name of the Property to be returned
     * @return The Property corresponding to 'propertyName', null if the property could not be found.
     */
    Property getProperty(String propertyName);

    /**
     * @return The introspector instance used by this introspecting TableModel.
     */
    Introspector getIntrospector();

    /**
     * gets the class of the the object at row,column.
     * @param rowIndex int; the rowNumber
     * @param columnIndex int; the columnNumber
     * @return The type of the value in cell 'rowIndex', 'columnIndex'
     */
    Class<?> getTypeAt(int rowIndex, int columnIndex);

    /**
     * Returns a reference to this model's modelManager, {see ModelManager}.
     * @return the model manager
     */
    ModelManager getModelManager();
}
