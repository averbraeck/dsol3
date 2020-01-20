package nl.tudelft.simulation.naming.context.tree;

import javax.naming.NamingException;
import javax.naming.event.EventContext;
import javax.swing.tree.DefaultTreeModel;

/**
 * The ContextTreeModel defines the inner structure of the context.
 * <p>
 * Copyright (c) 2002-2020 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class ContextTreeModel extends DefaultTreeModel
{
    /** The default serial version UID for serializable classes. */
    private static final long serialVersionUID = 1L;

    /**
     * constructs a new ContextTreeModel.
     * @param context EventContext; the context
     * @throws NamingException on failure
     */
    public ContextTreeModel(final EventContext context) throws NamingException
    {
        this(context, null, true);
    }

    /**
     * constructs a new ContextTreeModel.
     * @param context EventContext; the context
     * @param displayClasses Class&lt;?&gt;[]; the set of classes to display as children
     * @param displayFields boolean; should we display them?
     * @throws NamingException on failure
     */
    public ContextTreeModel(final EventContext context, final Class<?>[] displayClasses, final boolean displayFields)
            throws NamingException
    {
        super(null);
        this.setRoot(new ContextNode(this, "/", context, displayClasses, displayFields));
    }

    /** {@inheritDoc} */
    @Override
    protected void fireTreeStructureChanged(final Object source, final Object[] path, final int[] childIndices,
            final Object[] children)
    {
        super.fireTreeStructureChanged(source, path, childIndices, children);
    }

}
