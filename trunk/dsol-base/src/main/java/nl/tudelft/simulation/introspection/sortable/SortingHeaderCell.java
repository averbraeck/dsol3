package nl.tudelft.simulation.introspection.sortable;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * The sorting header cell.
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
public class SortingHeaderCell extends DefaultTableCellRenderer
{
    /** SORT_NONE means that sorting is off. */
    public static final int SORT_NONE = 0;

    /** SORT_NONE means that sorting is ascending. */
    public static final int SORT_ASCENDING = 1;

    /** SORT_NONE means that sorting is descending. */
    public static final int SORT_DESCENDING = 2;

    /** counts the number of created instances. */
    protected static int instanceCounter = 0;

    /** the sortMode. */
    private int sortMode = SortingHeaderCell.SORT_NONE;

    /** the id of the instance. */
    private int id = SortingHeaderCell.instanceCounter;

    /**
     * constructs a new SortingHeaderCell.
     * @param sort the sort mode (none, ascending, descending).
     */
    public SortingHeaderCell(final int sort)
    {
        this.sortMode = sort;
        this.setHorizontalAlignment((int) Component.CENTER_ALIGNMENT);
        this.setBackground(Color.LIGHT_GRAY);
        this.id = SortingHeaderCell.instanceCounter++;
    }

    /**
     * constructs a new SortingHeaderCell without sorting.
     */
    public SortingHeaderCell()
    {
        this(SORT_NONE);
    }

    /**
     * changes the sort modus.
     */
    public void changeSort()
    {
        this.sortMode = (this.sortMode + 1) % 3;
        this.repaint();
    }

    /**
     * sets the sort mode
     * @param sort the new mode
     */
    public void setSort(final int sort)
    {
        this.sortMode = sort;
    }

    /**
     * @return returns the sort mode
     */
    public int getSort()
    {
        return this.sortMode;
    }

    /**
     * is the header cell sorted?
     * @return whether the header cell is sorted
     */
    public boolean isSorted()
    {
        return (this.sortMode != SORT_NONE);
    }

    /**
     * is the header cell ascendingly sorted?
     * @return whether the header cell is ascendingly sorted.
     */
    public boolean isAscendingSorted()
    {
        return this.sortMode == SORT_ASCENDING;
    }

    /** {@inheritDoc} */
    @Override
    public void paintComponent(final Graphics g)
    {
        super.paintComponent(g);
        g.setColor(Color.GRAY);
        int width = this.getWidth();
        int height = this.getHeight();
        if (this.sortMode == SortingHeaderCell.SORT_DESCENDING)
        {
            g.fillPolygon(new int[]{width - 14, width - 9, width - 4}, new int[]{4, height - 4, 4}, 3);
        }
        else if (this.sortMode == SortingHeaderCell.SORT_ASCENDING)
        {
            g.fillPolygon(new int[]{width - 14, width - 9, width - 4}, new int[]{height - 4, 4, height - 4}, 3);
        }
    }

    /** {@inheritDoc} */
    @Override
    protected void setValue(final Object value)
    {
        super.setValue(value);
        this.setBorder(BorderFactory.createRaisedBevelBorder());
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return "SortingHeaderRenderer: " + this.id;
    }
}
