package nl.tudelft.simulation.naming.context;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;

import javax.naming.Binding;
import javax.naming.Name;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.event.EventContext;
import javax.naming.event.NamespaceChangeListener;
import javax.naming.event.NamingEvent;
import javax.naming.event.NamingExceptionEvent;
import javax.swing.tree.DefaultMutableTreeNode;

import nl.tudelft.simulation.event.EventType;
import nl.tudelft.simulation.logger.CategoryLogger;

/**
 * A node in the context.
 * <p>
 * (c) 2002-2018 <a href="https://simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="https://simulation.tudelft.nl"> www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @version 1.5 2004-03-24
 * @since 1.5
 */
public class ContextNode extends DefaultMutableTreeNode implements NamespaceChangeListener
{
    /** The default serial version UID for serializable classes. */
    private static final long serialVersionUID = 1L;

    /** NODE_CHANGED_EVENT */
    public static final EventType NODE_CHANGED_EVENT = new EventType("NODE_CHANGED_EVENT");

    /** the context. */
    private EventContext context = null;

    /** displayClasses the classes to display */
    private Class<?>[] displayClasses = null;

    /** display the fields ?.. */
    private boolean displayFields = false;

    /** the treeModel. */
    private ContextTreeModel treeModel = null;

    /**
     * constructs a new ContextNode.
     * @param treeModel the treeModel
     * @param name the name
     * @param context the context
     * @param displayClasses the classes to display
     * @param displayFields the fields to show
     * @throws NamingException on failure
     */
    public ContextNode(final ContextTreeModel treeModel, final String name, final EventContext context,
            final Class<?>[] displayClasses, final boolean displayFields) throws NamingException
    {
        super(name);
        this.treeModel = treeModel;
        this.context = context;
        this.displayClasses = displayClasses;
        this.displayFields = displayFields;

        NamingEnumeration<Binding> bindings = this.context.listBindings("");
        while (bindings.hasMore())
        {
            Binding binding = bindings.next();
            this.objectAdded(new NamingEvent(this.context, NamingEvent.OBJECT_ADDED, binding, null, null));
        }
        this.context.addNamingListener("", EventContext.OBJECT_SCOPE, this);
        this.context.addNamingListener("", EventContext.SUBTREE_SCOPE, this);
    }

    /**
     * constructs a new ContextNode.
     * @param userObject the userObject
     */
    public ContextNode(final Object userObject)
    {
        super(userObject, true);
    }

    /** {@inheritDoc} */
    @Override
    public void objectAdded(final NamingEvent event)
    {
        Binding item = event.getNewBinding();
        if (item.getObject() instanceof EventContext)
        {
            EventContext eventContext = (EventContext) item.getObject();
            try
            {
                Name name = eventContext.getNameParser("").parse(eventContext.getNameInNamespace());
                this.add(new ContextNode(this.treeModel, name.get(name.size() - 1).toString(), eventContext,
                        this.displayClasses, this.displayFields));
            }
            catch (NamingException exception)
            {
                CategoryLogger.always().warn(exception, "objectAdded");
            }
        }
        else if (this.display(item.getObject()))
        {
            this.contructObject(this, item.getObject());
        }
        this.treeModel.fireTreeStructureChanged(this, this.getPath(), null, null);
    }

    /** {@inheritDoc} */
    @Override
    public void objectRemoved(final NamingEvent event)
    {
        Binding item = event.getOldBinding();
        if (!(item.getObject() instanceof EventContext))
        {
            this.remove(item.getObject());
        }
        this.treeModel.fireTreeStructureChanged(this, this.getPath(), null, null);
    }

    /**
     * removes the child from context
     * @param object the object
     */
    private void remove(final Object object)
    {
        if (!display(object))
        {
            return;
        }
        for (int i = 0; i < this.getChildCount(); i++)
        {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) this.getChildAt(i);
            if (node.getUserObject().equals(object))
            {
                this.remove(i);
                return;
            }
        }
        throw new NullPointerException("Could not find " + object);
    }

    /** {@inheritDoc} */
    @Override
    public void objectRenamed(final NamingEvent event)
    {
        throw new RuntimeException("objectRenamed(" + event.toString() + ") not implemented yet");
    }

    /** {@inheritDoc} */
    @Override
    public void namingExceptionThrown(final NamingExceptionEvent event)
    {
        CategoryLogger.always().warn(event.getException(), "namingExceptionThrown");
    }

    /**
     * display this object.
     * @param object the object
     * @return boolean
     */
    private boolean display(final Object object)
    {
        return this.display(object.getClass());
    }

    /**
     * display this class.
     * @param myClass the class
     * @return boolean
     */
    private boolean display(final Class<?> myClass)
    {
        if (this.displayClasses == null)
        {
            return true;
        }
        if (myClass == null)
        {
            return false;
        }
        for (int i = 0; i < this.displayClasses.length; i++)
        {
            if (this.displayClasses[i].equals(myClass))
            {
                return true;
            }
        }
        Class<?>[] interfaces = myClass.getInterfaces();
        for (int i = 0; i < interfaces.length; i++)
        {
            for (int j = 0; j < this.displayClasses.length; j++)
            {
                if (interfaces[i].equals(this.displayClasses[j]))
                {
                    return true;
                }
            }
        }
        return display(myClass.getSuperclass());
    }

    /**
     * constructs an Object
     * @param root the root element
     * @param object the object
     * @return root
     */
    private DefaultMutableTreeNode contructObject(final DefaultMutableTreeNode root, final Object object)
    {
        if (!this.displayFields)
        {
            root.add(new DefaultMutableTreeNode(object));
        }
        else
        {
            DefaultMutableTreeNode child = new DefaultMutableTreeNode(object);
            try
            {
                BeanInfo beanInfo = Introspector.getBeanInfo(object.getClass());
                PropertyDescriptor[] descriptors = beanInfo.getPropertyDescriptors();
                for (int i = 0; i < descriptors.length; i++)
                {
                    String name = "attr:" + descriptors[i].getName();
                    String value = " value:" + descriptors[i].getReadMethod().invoke(object).toString();
                    child.add(new DefaultMutableTreeNode(name + "  " + value));
                }
                root.add(child);
            }
            catch (Exception exception)
            {
                exception = null;
                // No problem, we just don't show
            }
        }
        return root;
    }
}
