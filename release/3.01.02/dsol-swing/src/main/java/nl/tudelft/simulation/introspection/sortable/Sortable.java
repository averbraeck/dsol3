package nl.tudelft.simulation.introspection.sortable;

/**
 * Defines methods to define, retrieve and perform sorting definitions.
 * <p>
 * (c) copyright 2002-2014 <a href="http://www.simulation.tudelft.nl">Delft University of Technology</a>. <br>
 * BSD-style license. See <a href="http://www.simulation.tudelft.nl/dsol/3.0/license.html">DSOL License</a>. <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs</a>.
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>.
 * @author Niels Lang.
 * @since 1.5
 */
public interface Sortable
{
    /**
     * Defines the sort definition.
     */
    public static interface Definition
    {
        /**
         * @return Returns the field to which this sorting definition applies
         */
        int getFieldID();

        /**
         * Returns whether this definition defines an ascending sort.
         * @return A 'false' return value implies a descending sort definition.
         */
        boolean isAcendingSort();

        /**
         * Allows dynamic definitions.
         * @param ascending whether the sort is ascending
         */
        void setAscending(boolean ascending);
    }

    /**
     * @return Returns the current definitions defined for this Sortable. The sequence of the definitions matches the
     *         sorting sequence, in that a definition will be performed before another definition if having a lower
     *         index.
     */
    Definition[] getDefinitions();

    /**
     * Sets the current definitions defined for this Sortable. The sequence of the definitions matches the sorting
     * sequence, in that a definition will be performed before another definition if having a lower index.
     * @param definitions An array of sort definitions. If multiple definitions for the same field are included, the one
     *            with highest index will be applied.
     */
    void setDefinitions(Definition[] definitions);

    /**
     * Instructs this Sortable to sort based on currently set sorting definitions.
     */
    void sort();
}
