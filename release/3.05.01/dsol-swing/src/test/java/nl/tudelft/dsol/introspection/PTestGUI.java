
package nl.tudelft.dsol.introspection;

import org.djutils.logger.CategoryLogger;
import org.pmw.tinylog.Level;

import nl.tudelft.dsol.introspection.beans.GUIBean;
import nl.tudelft.simulation.dsol.swing.introspection.gui.IntrospectionDialog;
import nl.tudelft.simulation.dsol.swing.introspection.gui.ObjectTableModel;
import nl.tudelft.simulation.introspection.beans.BeanIntrospector;

/**
 * An interactive test program for the introspection GUI package.
 * <p>
 * Copyright (c) 2002-2021 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="http://web.eur.nl/fbk/dep/dep1/Introduction/Staff/People/Lang">Niels Lang
 *         </a><a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @since 1.5
 */
public final class PTestGUI
{
    /**
     * constructs a new PTestGUI.
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
        CategoryLogger.setAllLogLevel(Level.TRACE);
        Object introspected = new GUIBean();
        // new IntrospectionDialog("Test Field introspector", new ObjectTableModel(introspected, new FieldIntrospector()));
        new IntrospectionDialog("Test Bean introspector", new ObjectTableModel(introspected, new BeanIntrospector()));
    }
}
