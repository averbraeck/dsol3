/*
 * @(#)PTestGui.java April 15, 2004 Copyright (c) 2002-2005-2004 Delft
 * University of Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All
 * rights reserved. This software is proprietary information of Delft University
 * of Technology 
 */

package nl.tudelft.dsol.introspection;

import nl.tudelft.dsol.introspection.beans.GUIBean;
import nl.tudelft.simulation.introspection.beans.BeanIntrospector;
import nl.tudelft.simulation.introspection.fields.FieldIntrospector;
import nl.tudelft.simulation.introspection.gui.IntroSpectionDialog;
import nl.tudelft.simulation.introspection.gui.ObjectTableModel;

/**
 * A test program for the GUI package.
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
public final class PTestGUI
{
    /**
     * constructs a new PTestGUI
     */
    private PTestGUI()
    {
        super();
        // unreachable code
    }

    /**
     * executes the PTestSorter
     * @param args the command-line arguments
     */
    public static void main(final String[] args)
    {
        Object introspected = new GUIBean();
        new IntroSpectionDialog("Test IntrospectionField introspector", new ObjectTableModel(introspected,
                new FieldIntrospector()));
        new IntroSpectionDialog("Test Bean introspector", new ObjectTableModel(introspected, new BeanIntrospector()));
    }
}