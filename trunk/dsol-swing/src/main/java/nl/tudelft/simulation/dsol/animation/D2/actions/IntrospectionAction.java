package nl.tudelft.simulation.dsol.animation.D2.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import nl.tudelft.simulation.introspection.gui.IntroSpectionDialog;

/**
 * Introspecting an object on the screen.
 * <p>
 * Copyright (c) 2002-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. See for project information <a href="https://simulation.tudelft.nl/" target="_blank">
 * https://simulation.tudelft.nl</a>. The DSOL project is distributed under a three-clause BSD-style license, which can
 * be found at <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="http://www.peter-jacobs.com/index.htm">Peter Jacobs </a>
 * @since 1.5
 */
public class IntrospectionAction extends AbstractAction
{
    /** */
    private static final long serialVersionUID = 20140909L;

    /** the target to introspect */
    private Object target = null;

    /**
     * constructs a new IntrospectionAction
     * @param target the target to introspect
     */
    public IntrospectionAction(final Object target)
    {
        super(target.toString());
        this.target = target;
    }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(final ActionEvent e)
    {
        new IntroSpectionDialog(this.target);
    }
}