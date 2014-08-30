/*
 * @(#) Sortable.java April 15, 2004 Copyright (c) 2002-2005-2004 Delft
 * University of Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All
 * rights reserved. This software is proprietary information of Delft University
 * of Technology 
 */
package nl.tudelft.simulation.introspection.sortable;

/**
 * Defines methods to define, retrieve and perform sorting definitions.
 * <p>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @version 1.2 Apr 14, 2004
 * @since 1.5
 */
public interface Sortable
{
    /**
     * Defines the sort definition
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
         * Allows dynamic definitions
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