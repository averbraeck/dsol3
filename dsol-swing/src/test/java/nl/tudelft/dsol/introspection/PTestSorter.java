package nl.tudelft.dsol.introspection;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;

import nl.tudelft.simulation.introspection.gui.SortingObjectTableModel;
import nl.tudelft.simulation.introspection.sortable.SortDefinition;
import nl.tudelft.simulation.introspection.sortable.Sortable;
import nl.tudelft.simulation.introspection.sortable.SortingTableHeader;

/**
 * A test program for the sortable table model.
 * <p>
 * (c) copyright 2002-2005-2004 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="http://web.eur.nl/fbk/dep/dep1/Introduction/Staff/People/Lang">Niels Lang
 *         </a><a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @version 1.1 Apr 15, 2004
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
                new Object[][]{{"fruit", "apple", new Integer(200), "1"}, {"car", "BMW 3", new Integer(1980), "2"},
                        {"nation", "America", new Integer(1776), "3"}, {"nation", "Germany", new Integer(1024), "4"},
                        {"nation", "England", new Integer(500), "5"}, {"nation", "Scotland", new Integer(1666), "6"},
                        {"nation", "Russia", new Integer(200), "7"}, {"nation", "France", new Integer(1789), "8"},
                        {"nation", "Belgium", new Integer(1820), "9"}, {"nation", "Pakistan", new Integer(1960), "10"},
                        {"nation", "Israel", new Integer(1945), "11"}, {"nation", "Palestina", new Integer(2004), "12"},
                        {"nation", "Iraq", new Integer(1300), "13"}, {"nation", "China", new Integer(-2000), "14"},
                        {"nation", "Peru", new Integer(-3000), "15"}, {"nation", "Nigeria", new Integer(-2000), "16"}},
                new String[]{"category", "instance", "date", "entry"});
        TableModel sorted = new SortingObjectTableModel(unsorted);

        ((Sortable) sorted).setDefinitions(new SortDefinition[]{new SortDefinition(0, true),
                new SortDefinition(2, false), new SortDefinition(1, false)});
        ((Sortable) sorted).sort();

        JFrame test = new JFrame("Test sorter");
        test.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        test.getContentPane().setLayout(new BorderLayout());

        JTable table = new JTable(sorted);
        JTableHeader header = new SortingTableHeader(new SortDefinition[]{new SortDefinition(0, true)});
        header.setColumnModel(table.getColumnModel());
        table.setTableHeader(header);
        JScrollPane scroller = new JScrollPane(table, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        test.getContentPane().add(scroller, BorderLayout.CENTER);
        test.pack();
        test.setVisible(true);
    }
}
