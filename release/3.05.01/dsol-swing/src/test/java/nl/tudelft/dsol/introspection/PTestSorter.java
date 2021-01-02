package nl.tudelft.dsol.introspection;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;

import nl.tudelft.simulation.dsol.swing.introspection.gui.SortingObjectTableModel;
import nl.tudelft.simulation.dsol.swing.introspection.sortable.SortDefinition;
import nl.tudelft.simulation.dsol.swing.introspection.sortable.Sortable;
import nl.tudelft.simulation.dsol.swing.introspection.sortable.SortingTableHeader;

/**
 * A test program for the sortable table model.
 * <p>
 * (c) 2002-2019-2004 <a href="https://simulation.tudelft.nl">Delft University of Technology </a>, the Netherlands. <br>
 * See for project information <a href="https://simulation.tudelft.nl">www.simulation.tudelft.nl </a>.
 * <p>
 * Copyright (c) 2002-2021 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="http://web.eur.nl/fbk/dep/dep1/Introduction/Staff/People/Lang">Niels Lang
 *         </a><a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @since 1.5
 */
public final class PTestSorter
{
    /**
     * constructs a new PTestSorter.
     */
    private PTestSorter()
    {
        super();
        // unreachable code
    }

    /**
     * executes the PTestSorter
     * @param args the command-line arguments
     */
    public static void main(final String[] args)
    {
        TableModel unsorted = new DefaultTableModel(
                new Object[][] {{"fruit", "apple", Integer.valueOf(200), "1"}, {"car", "BMW 3", Integer.valueOf(1980), "2"},
                        {"nation", "America", Integer.valueOf(1776), "3"}, {"nation", "Germany", Integer.valueOf(1024), "4"},
                        {"nation", "England", Integer.valueOf(500), "5"}, {"nation", "Scotland", Integer.valueOf(1666), "6"},
                        {"nation", "Russia", Integer.valueOf(200), "7"}, {"nation", "France", Integer.valueOf(1789), "8"},
                        {"nation", "Belgium", Integer.valueOf(1820), "9"}, {"nation", "Pakistan", Integer.valueOf(1960), "10"},
                        {"nation", "Israel", Integer.valueOf(1945), "11"}, {"nation", "Palestina", Integer.valueOf(2004), "12"},
                        {"nation", "Iraq", Integer.valueOf(1300), "13"}, {"nation", "China", Integer.valueOf(-2000), "14"},
                        {"nation", "Peru", Integer.valueOf(-3000), "15"}, {"nation", "Nigeria", Integer.valueOf(-2000), "16"}},
                new String[] {"category", "instance", "date", "entry"});
        TableModel sorted = new SortingObjectTableModel(unsorted);

        ((Sortable) sorted).setDefinitions(
                new SortDefinition[] {new SortDefinition(0, true), new SortDefinition(2, false), new SortDefinition(1, false)});
        ((Sortable) sorted).sort();

        JFrame test = new JFrame("Test sorter");
        test.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        test.getContentPane().setLayout(new BorderLayout());

        JTable table = new JTable(sorted);
        JTableHeader header = new SortingTableHeader(new SortDefinition[] {new SortDefinition(0, true)});
        header.setColumnModel(table.getColumnModel());
        table.setTableHeader(header);
        JScrollPane scroller = new JScrollPane(table, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        test.getContentPane().add(scroller, BorderLayout.CENTER);
        test.pack();
        test.setVisible(true);
    }
}
