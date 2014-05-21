/*
 * @(#)PTestBeans.java April 15, 2004 Copyright (c) 2002-2005-2004 Delft
 * University of Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All
 * rights reserved. This software is proprietary information of Delft University
 * of Technology 
 */
package nl.tudelft.dsol.introspection;

import nl.tudelft.dsol.introspection.beans.SubTestBean2;
import nl.tudelft.simulation.introspection.Property;
import nl.tudelft.simulation.introspection.beans.BeanIntrospector;
import nl.tudelft.simulation.logger.Logger;

/**
 * A test program for the JavaBean introspection implementation.
 * <p>
 * (c) copyright 2002-2005-2004 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="http://web.eur.nl/fbk/dep/dep1/Introduction/Staff/People/Lang" >Niels Lang </a><a
 *         href="http://www.peter-jacobs.com/index.htm">Peter Jacobs </a>
 * @version 1.1 Apr 15, 2004
 * @since 1.5
 */
public final class PTestBeans
{
    /**
     * constructs a new PTestBeans
     */
    private PTestBeans()
    {
        super();
        // unreachable code;
    }

    /**
     * executes the PTestBeans
     * @param args the commandline arguments
     */
    public static void main(final String[] args)
    {
        Property[] props = (new BeanIntrospector()).getProperties(new SubTestBean2());
        for (int i = 0; i < props.length; i++)
        {
            Logger.info(PTestBeans.class, "main", "Prop name: " + props[i].getName());
            Logger.info(PTestBeans.class, "main", "Prop class: " + props[i].getType());
            Logger.info(PTestBeans.class, "main", "Prop value: " + props[i].getValue());
        }
    }
}