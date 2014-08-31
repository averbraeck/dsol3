package nl.tudelft.simulation.introspection.sortable;

/**
 * The SortDefinition.
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
public class SortDefinition implements Sortable.Definition
{
    /** the fieldId. */
    private int fieldID;

    /** whether sorting should occur ascending. */
    private boolean ascending;

    /**
     * constructs a new SortDefinition.
     * @param fieldID the fieldID
     * @param ascending whether sorting should occur ascending
     */
    public SortDefinition(final int fieldID, final boolean ascending)
    {
        this.fieldID = fieldID;
        this.ascending = ascending;
    }

    /** {@inheritDoc} */
    public int getFieldID()
    {
        return this.fieldID;
    }

    /** {@inheritDoc} */
    public boolean isAcendingSort()
    {
        return this.ascending;
    }

    /** {@inheritDoc} */
    public void setAscending(final boolean ascending)
    {
        this.ascending = ascending;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return "Sort definition, fieldID: " + this.fieldID + ", ascending: " + this.ascending;
    }
}
