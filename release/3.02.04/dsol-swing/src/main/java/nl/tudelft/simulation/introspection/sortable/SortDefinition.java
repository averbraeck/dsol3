package nl.tudelft.simulation.introspection.sortable;

/**
 * The SortDefinition.
 * <p>
 * copyright (c) 2002-2018  <a href="https://simulation.tudelft.nl">Delft University of Technology</a>. <br>
 * BSD-style license. See <a href="https://simulation.tudelft.nl/dsol/3.0/license.html">DSOL License</a>. <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs</a>.
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>.
 * @author Niels Lang.
 * @since 1.5
 */
public class SortDefinition implements Sortable.Definition
{
    /** the fieldId. */
    private final int fieldID;

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
    @Override
    public int getFieldID()
    {
        return this.fieldID;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isAcendingSort()
    {
        return this.ascending;
    }

    /** {@inheritDoc} */
    @Override
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
