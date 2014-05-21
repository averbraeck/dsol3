/*
 * @(#) SortingTableHeader.java April 15, 2004 Copyright (c) 2002-2005-2004
 * Delft University of Technology Jaffalaan 5, 2628 BX Delft, the Netherlands.
 * All rights reserved. This software is proprietary information of Delft
 * University of Technology The code is published under the Lesser General
 * Public License
 */
package nl.tudelft.simulation.introspection.sortable;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

/**
 * The sortingTableHeader class.
 * <p>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft
 * University of Technology </a>, the Netherlands. <br>
 * See for project information <a
 * href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser
 * General Public License (LGPL) </a>, no warranty.
 * 
 * @author <a href="http://www.peter-jacobs.com/index.htm">Peter Jacobs </a>
 * @version 1.2 Apr 14, 2004
 * @since 1.5
 */
public class SortingTableHeader extends JTableHeader
{
    /** the definitions of the tableHeader */
    protected List<Sortable.Definition> definitions = new ArrayList<Sortable.Definition>(
            5);

    /**
     * constructs a new SortingTableHeader
     * 
     * @param defaultSorting the defaultSorting definitions
     */
    public SortingTableHeader(final Sortable.Definition[] defaultSorting)
    {
        this.definitions.addAll(Arrays.asList(defaultSorting));
        this.init();
    }

    /**
     * initializes the SortingTableHeader
     */
    private void init()
    {
        this.addMouseListener(new MouseAdapter()
        {
            /**
             * @see java.awt.event.MouseAdapter#mouseClicked(MouseEvent)
             */
            @Override
            public void mouseClicked(final MouseEvent e)
            {
                int column = columnAtPoint(e.getPoint());
                TableColumn col = getColumnModel().getColumn(column);
                SortingHeaderCell header = (SortingHeaderCell) col
                        .getHeaderRenderer();
                header.changeSort();
                if (!e.isShiftDown())
                {
                    SortingTableHeader.this.definitions.clear();
                    SortingTableHeader.this.clearHeaders(header);
                    if (header.isSorted())
                        SortingTableHeader.this.definitions
                                .add(new SortDefinition(column, header
                                        .isAscendingSorted()));
                } else
                {
                    Sortable.Definition def = SortingTableHeader.this
                            .getMatchingDef(column);
                    if (def == null)
                        SortingTableHeader.this.definitions
                                .add(new SortDefinition(column, header
                                        .isAscendingSorted()));
                    else
                    {
                        if (!header.isSorted())
                            SortingTableHeader.this.definitions.remove(def);
                        else
                            def.setAscending(header.isAscendingSorted());
                    }
                }
                SortingTableHeader.this.propagateSort();
            }

        });
    }

    /**
     * gets the matching definition
     * 
     * @param column the columnNumber
     * @return the definition
     */
    protected Sortable.Definition getMatchingDef(final int column)
    {
        for (Sortable.Definition def : this.definitions)
        {
            if (def.getFieldID() == column)
            {
                return def;
            }
        }
        return null;
    }

    /**
     * clears the header
     * 
     * @param butThisOne all but this one
     */
    protected void clearHeaders(final SortingHeaderCell butThisOne)
    {
        for (int i = 0; i < getColumnModel().getColumnCount(); i++)
        {
            SortingHeaderCell current = (SortingHeaderCell) getColumnModel()
                    .getColumn(i).getHeaderRenderer();
            if (!current.equals(butThisOne))
            {
                current.setSort(SortingHeaderCell.SORT_NONE);
            }
        }
    }

    /**
     * propagates the sort
     */
    protected void propagateSort()
    {
        TableModel model = this.getTable().getModel();
        if (model instanceof Sortable)
        {
            ((Sortable) model).setDefinitions(this.definitions
                    .toArray(new SortDefinition[0]));
            ((Sortable) model).sort();
        }
    }

    /**
     * @see javax.swing.table.JTableHeader#setColumnModel(TableColumnModel)
     */
    @Override
    public void setColumnModel(final TableColumnModel columnModel)
    {
        super.setColumnModel(columnModel);
        for (int i = 0; i < this.getColumnModel().getColumnCount(); i++)
        {
            int sort = getSort(i);
            this.getColumnModel().getColumn(i).setHeaderRenderer(
                    new SortingHeaderCell(sort));
        }
    }

    /**
     * gets the sorting of the column
     * 
     * @param column the column number
     * @return the value
     */
    private int getSort(final int column)
    {
        for (Sortable.Definition def : this.definitions)
        {
            if (def.getFieldID() == column)
            {
                if (def.isAcendingSort())
                {
                    return SortingHeaderCell.SORT_ASCENDING;
                }
                return SortingHeaderCell.SORT_DESCENDING;
            }
        }
        return SortingHeaderCell.SORT_NONE;
    }

    /**
     * @see javax.swing.table.JTableHeader#setTable(JTable)
     */
    @Override
    public void setTable(final JTable table)
    {
        super.setTable(table);
        this.propagateSort();
    }
}