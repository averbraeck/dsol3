/*
 * @(#) Filterinterface.java Oct 26, 2004 Copyright (c) 2002-2005 Delft
 * University of Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All
 * rights reserved. This software is proprietary information of Delft University
 * of Technology 
 */
package nl.tudelft.simulation.language.filters;

import java.io.Serializable;

/**
 * The FilterInterface is a general interface for all filters in DSOL. Filters can be based on xY combinations, class
 * information ,etc. etc. The API of implementing filters will explain what it expects as input.
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
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @version $Revision: 1.1 $ $Date: 2009/10/21 07:32:42 $
 * @since 1.5
 */
public interface FilterInterface extends Serializable
{
    /**
     * a filter defines whether to accept a value in a chart
     * @param entry the entry to filter
     * @return whether to accept this entry
     */
    boolean accept(Object entry);

    /**
     * inverts the filter
     * @param inverted whether to invert the filter
     */
    void setInverted(boolean inverted);

    /**
     * is the filter inverted?
     * @return whether the filter is inverted.
     */
    boolean isInverted();

    /**
     * returns a string representation of the criterium
     * @return the string representing the criterium
     */
    String getCriterium();

    /**
     * adds filter to this filter and returns the composed filter
     * @param filter the filter to add
     * @return the composed filter
     */
    FilterInterface and(FilterInterface filter);

    /**
     * creates a new composite filter which is one or two
     * @param filter the filter to add
     * @return the composed filter
     */
    FilterInterface or(FilterInterface filter);
}