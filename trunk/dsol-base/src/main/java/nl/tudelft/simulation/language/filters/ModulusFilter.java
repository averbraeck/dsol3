/*
 * @(#) ModulusFilter.java Oct 26, 2004 Copyright (c) 2002-2005 Delft University of Technology Jaffalaan 5,
 * 2628 BX Delft, the Netherlands. All rights reserved. This software is proprietary information of Delft
 * University of Technology 
 */
package nl.tudelft.simulation.language.filters;

/**
 * The modulus filter only accepts the nth event where n % given modulus = 0.
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
 * @author <a href="http://www.peter-jacobs.com/index.htm"> Peter Jacobs </a>
 * @version $Revision: 1.2 $ $Date: 2009/10/21 07:32:42 $
 * @since 1.5
 */
public class ModulusFilter extends AbstractFilter
{
    /** The default serial version UID for serializable classes */
    private static final long serialVersionUID = 1L;

    /** the maxPoints to use. */
    private long modulus = -1;

    /** the amount of points already accepted. */
    private long current = -1;

    /**
     * constructs a new ModulusFilter.
     * @param modulus the modulus to use
     */
    public ModulusFilter(final long modulus)
    {
        super();
        this.modulus = modulus;
    }

    /**
     * @see nl.tudelft.simulation.language.filters.AbstractFilter#filter(java.lang.Object)
     */
    @Override
    protected synchronized boolean filter(final Object entry)
    {
        this.current++;
        if (this.current % this.modulus == 0)
        {
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
        return "accepts every " + this.modulus + "nth entry";
    }
}
