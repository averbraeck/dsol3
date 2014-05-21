/*
 * @(#) SortDefinition.java Apr 14, 2004 Copyright (c) 2002-2005 Delft
 * University of Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All
 * rights reserved. This software is proprietary information of Delft University
 * of Technology 
 */
package nl.tudelft.simulation.introspection.sortable;

/**
 * The SortDefinition.
 * <p>
 * (c) copyright 2002-2005-2004 <a href="http://www.simulation.tudelft.nl">Delft
 * University of Technology </a>, the Netherlands. <br>
 * See for project information <a
 * href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser
 * General Public License (LGPL) </a>, no warranty.
 * 
 * @author <a
 *         href="http://web.eur.nl/fbk/dep/dep1/Introduction/Staff/People/Lang">Niels
 *         Lang </a><a href="http://www.peter-jacobs.com/index.htm">Peter
 *         Jacobs </a>
 * @version 1.1 Apr 15, 2004
 * @since 1.5
 */
public class SortDefinition implements Sortable.Definition
{
    /** the fieldID */
    private int fieldID;

    /** whether sorting should occur ascending */
    private boolean ascending;

    /**
     * constructs a new SortDefinition
     * 
     * @param fieldID the fieldID
     * @param ascending whether sorting should occur ascending
     */
    public SortDefinition(final int fieldID, final boolean ascending)
    {
        this.fieldID = fieldID;
        this.ascending = ascending;
    }

    /**
     * @see nl.tudelft.simulation.introspection.sortable.Sortable.Definition#getFieldID()
     */
    public int getFieldID()
    {
        return this.fieldID;
    }

    /**
     * @see nl.tudelft.simulation.introspection.sortable.Sortable.Definition
     *      #isAcendingSort()
     */
    public boolean isAcendingSort()
    {
        return this.ascending;
    }

    /**
     * @see nl.tudelft.simulation.introspection.sortable.Sortable.Definition
     *      #setAscending(boolean)
     */
    public void setAscending(final boolean ascending)
    {
        this.ascending = ascending;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return "Sort definition, fieldID: " + this.fieldID + ", ascending: "
                + this.ascending;
    }
}