/*
 * @(#) CompositeFilter.java Oct 26, 2004 Copyright (c) 2002-2005 Delft
 * University of Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All
 * rights reserved. This software is proprietary information of Delft University
 * of Technology 
 */
package nl.tudelft.simulation.language.filters;

/**
 * The composite filter combines two filters.
 * <p>
 * Copyright (c) 2002-2009 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved.
 * <p>
 * See for project information <a href="http://www.simulation.tudelft.nl/"> www.simulation.tudelft.nl</a>.
 * <p>
 * The DSOL project is distributed under the following BSD-style license:<br>
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
 * following conditions are met:
 * <ul>
 * <li>Redistributions of source code must retain the above copyright notice, this list of conditions and the following
 * disclaimer.</li>
 * <li>Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the
 * following disclaimer in the documentation and/or other materials provided with the distribution.</li>
 * <li>Neither the name of Delft University of Technology, nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.</li>
 * </ul>
 * This software is provided by the copyright holders and contributors "as is" and any express or implied warranties,
 * including, but not limited to, the implied warranties of merchantability and fitness for a particular purpose are
 * disclaimed. In no event shall the copyright holder or contributors be liable for any direct, indirect, incidental,
 * special, exemplary, or consequential damages (including, but not limited to, procurement of substitute goods or
 * services; loss of use, data, or profits; or business interruption) however caused and on any theory of liability,
 * whether in contract, strict liability, or tort (including negligence or otherwise) arising in any way out of the use
 * of this software, even if advised of the possibility of such damage.
 * @author <a href="http://web.eur.nl/fbk/dep/dep1/Introduction/Staff/People/Lang" >Niels Lang </a>
 * @author <a href="http://www.peter-jacobs.com/index.htm">Peter Jacobs </a>
 * @version $Revision: 1.2 $ $Date: 2009/10/21 07:32:42 $
 * @since 1.5
 */
public class CompositeFilter extends AbstractFilter
{
    /** The default serial version UID for serializable classes */
    private static final long serialVersionUID = 1L;

    /** the AND operator */
    public static final short AND = 0;

    /** the OR operator */
    public static final short OR = 1;

    /** the operator of the composite filter */
    private short operator = -1;

    /** the filters to compose */
    private FilterInterface[] filters = new FilterInterface[2];

    /**
     * constructs a new CompositeFilter
     * @param filter1 the first filter
     * @param filter2 the second filter
     * @param operator the operator (AND or OR)
     */
    public CompositeFilter(final FilterInterface filter1, final FilterInterface filter2, final short operator)
    {
        super();
        if (operator < 0 || operator > 1)
        {
            throw new IllegalArgumentException("unknown operator");
        }
        this.filters[0] = filter1;
        this.filters[1] = filter2;
        this.operator = operator;
    }

    /**
     * @see nl.tudelft.simulation.language.filters.AbstractFilter#filter(java.lang.Object)
     */
    @Override
    protected boolean filter(final Object entry)
    {
        if (this.operator == CompositeFilter.AND)
        {
            return this.filters[0].accept(entry) && this.filters[1].accept(entry);
        }
        return this.filters[0].accept(entry) || this.filters[1].accept(entry);
    }

    /**
     * Converts the operator of this filter into a human readable string.
     * @return the operator in human readable string
     */
    protected String operatorToString()
    {
        if (this.operator == AND)
        {
            return "AND";
        }
        return "OR";
    }

    /**
     * @see nl.tudelft.simulation.language.filters.AbstractFilter#getCriterium()
     */
    @Override
    public String getCriterium()
    {
        return "composed[" + this.filters[0].getCriterium() + ";" + operatorToString() + ";"
                + this.filters[1].getCriterium() + "]";
    }
}
