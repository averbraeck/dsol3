/*
 * @(#) ModulusFilter.java Oct 26, 2004 Copyright (c) 2004 Delft University of
 * Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved.
 * This software is proprietary information of Delft University of Technology
 * 
 */
package nl.tudelft.simulation.jstats.filters;

import nl.tudelft.simulation.language.filters.AbstractFilter;

/**
 * The modulus filter only accepts
 * <p>
 * (c) copyright 2004 <a href="http://www.simulation.tudelft.nl/dsol/">Delft
 * University of Technology </a>, the Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl/dsol/">
 * www.simulation.tudelft.nl/dsol </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser
 * General Public License (LGPL) </a>, no warranty.
 * 
 * @author <a
 *         href="http://web.eur.nl/fbk/dep/dep1/Introduction/Staff/People/Lang">Niels
 *         Lang </a><a href="http://www.peter-jacobs.com/index.htm">Peter
 *         Jacobs </a>
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:38:41 $
 * @since 1.5
 */
public class SnippetFilter extends AbstractFilter
{
    /** the snippet representing the xRange for this filter */
    private double snippet = Double.NaN;

    /** the amount of points already accepted */
    private double lastAcceptedXValue = -Double.MAX_VALUE;

    /**
     * constructs a new SnippetFilter. A snippet filter test only accepts one
     * entry per snippet value. A snippet is a range in x-value.
     * 
     * @param snippet the snippet to use. A snippet is a range in the x-value.
     */
    public SnippetFilter(final double snippet)
    {
        super();
        if (snippet <= 0.0)
        {
            throw new IllegalArgumentException("snippet should be >0.0");
        }
        this.snippet = snippet;
    }

    /**
     * @see nl.tudelft.simulation.language.filters.AbstractFilter
     *      #filter(java.lang.Object)
     */
    @Override
    public boolean filter(final Object entry)
    {
        if (!(entry instanceof double[]) || ((double[]) entry).length != 2)
        {
            throw new IllegalArgumentException(
                    "entry should be instance of double[2] representing x,y");
        }
        double[] value = (double[]) entry;
        if ((value[0] - this.lastAcceptedXValue) >= this.snippet)
        {
            this.lastAcceptedXValue = value[0];
            return true;
        }
        return false;
    }

    /**
     * @see nl.tudelft.simulation.language.filters.FilterInterface#getCriterium()
     */
    @Override
    public String getCriterium()
    {
        return "accepts one entry per " + this.snippet + " xRange value";
    }
}