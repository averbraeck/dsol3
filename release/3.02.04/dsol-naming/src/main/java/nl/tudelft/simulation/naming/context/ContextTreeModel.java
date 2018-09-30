package nl.tudelft.simulation.naming.context;

import javax.naming.NamingException;
import javax.naming.event.EventContext;
import javax.swing.tree.DefaultTreeModel;

/**
 * The ContextTreeModel defines the inner structure of the context.
 * <p>
 * (c) 2002-2018 <a href="https://simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="https://simulation.tudelft.nl"> www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @version 1.2 2004-03-24
 * @since 1.5
 */
public class ContextTreeModel extends DefaultTreeModel
{
    /** The default serial version UID for serializable classes. */
    private static final long serialVersionUID = 1L;

    /**
     * constructs a new ContextTreeModel.
     * @param context the context
     * @throws NamingException on failure
     */
    public ContextTreeModel(final EventContext context) throws NamingException
    {
        this(context, null, true);
    }

    /**
     * constructs a new ContextTreeModel.
     * @param context the context
     * @param displayClasses the set of classes to display as children
     * @param displayFields should we display them?
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
    protected void fireTreeStructureChanged(final Object arg0, final Object[] arg1, final int[] arg2,
            final Object[] arg3)
    {
        super.fireTreeStructureChanged(arg0, arg1, arg2, arg3);
    }

}
