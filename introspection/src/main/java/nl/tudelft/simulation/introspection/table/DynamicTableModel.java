/*
 * @(#)DynamicTableModel April 14, 2004 Copyright (c) 2002-2005-2004 Delft
 * University of Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All
 * rights reserved. This software is proprietary information of Delft University
 * of Technology 
 */
package nl.tudelft.simulation.introspection.table;

import javax.swing.table.TableModel;

/**
 * An interface that defines methods for adding and deleting rows from a tablemodel.
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