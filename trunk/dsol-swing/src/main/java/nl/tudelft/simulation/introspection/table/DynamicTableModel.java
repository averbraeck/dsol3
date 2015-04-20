package nl.tudelft.simulation.introspection.table;

import javax.swing.table.TableModel;

/**
 * An interface that defines methods for adding and deleting rows from a tablemodel.
 * <p>
 * (c) copyright 2002-2014 <a href="http://www.simulation.tudelft.nl">Delft University of Technology</a>. <br>
 * BSD-style license. See <a href="http://www.simulation.tudelft.nl/dsol/3.0/license.html">DSOL License</a>. <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs</a>.
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>.
 * @author Niels Lang.
 * @since 1.5
 */
public interface DynamicTableModel extends TableModel
{
    /**
     * Deletes a specific row from the TableModel.
     * @param index The (TableModel) index of the row to be deleted
     */
    void deleteRow(int index);

    /**
     * Deletes a specific set of rows from the TableModel.
     * @param indices The (TableModel) indices of the rows to be deleted
     */
    void deleteRows(int[] indices);

    /**
     * Creates a new row at the end of the TableModel.
     */
    void createRow();

    /**
     * Creates a number of new rows at the end of the TableModel
     * @param amount The number of rows to be created.
     */
    void createRows(int amount);

    /**
     * @return whether or not the rows in this model can be edited. If false, calls to create and delete methods will
     *         have no final result.
     */
    public boolean isRowEditable();
}
