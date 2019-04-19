package nl.tudelft.simulation.dsol.swing.animation.D2.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import nl.tudelft.simulation.dsol.swing.introspection.gui.IntroSpectionDialog;
import nl.tudelft.simulation.introspection.DelegateIntrospection;

/**
 * Introspecting an object on the screen.
 * <p>
 * Copyright (c) 2002-2019 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
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
     * @param target Object; the target to introspect
     */
    public IntrospectionAction(final Object target)
    {
        super(checkDelegation(target));
        this.target = target;
    }

    /**
     * Check for introspection delegation to allow for the right object to be shown on the screen. 
     * @param target Object; the object that is displayed on the screen and might have delegation
     * @return the toString() of the (delegated) object 
     */
    private static String checkDelegation(final Object target)
    {
        Object introspected = target;
        while (introspected instanceof DelegateIntrospection)
        {
            introspected = ((DelegateIntrospection) introspected).getParentIntrospectionObject();
        }
        return introspected.toString();
    }
    
    /** {@inheritDoc} */
    @Override
    public void actionPerformed(final ActionEvent e)
    {
        new IntroSpectionDialog(this.target);
    }
}
