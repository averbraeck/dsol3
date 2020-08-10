package nl.tudelft.simulation.naming.context;

import java.util.Enumeration;

import javax.naming.Binding;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.Name;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;

/**
 * ContextUtility class
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
public final class ContextUtil
{
    /**
     * prevent construction of a ContextUtil.
     */
    private ContextUtil()
    {
        super();
    }

    /**
     * resolves the name of an object under which it is accessible in the initial context.
     * @param object the object
     * @return String
     * @throws NamingException whenever the object cannot be found
     */
    public static String resolveKey(final Object object) throws NamingException
    {
        String result = ContextUtil.resolveKey(object, new InitialContext(), "");
        if (result == null)
        {
            throw new NamingException("could not resolve " + object.toString());
        }
        return result;
    }

    /**
     * resolves the key under which an object is stored in the given context.
     * @param object the object which key to resolve.
     * @param context the context.
     * @param name the name of the parent.
     * @return the key
     * @throws NamingException on lookup failure
     */
    private static String resolveKey(final Object object, final Context context, final String name)
            throws NamingException
    {
        NamingEnumeration<Binding> list = context.listBindings(name);
        while (list.hasMore())
        {
            Binding binding = list.next();
            if (binding.getObject() instanceof Context)
            {
                String result = ContextUtil.resolveKey(object, (Context) binding.getObject(), binding.getName());
                if (result != null)
                {
                    return result;
                }
            }
            else if (binding.getObject().equals(object))
            {
                String key = context.getNameInNamespace() + "/" + binding.getName();
                return key;
            }
        }
        return null;
    }

    /**
     * binds an object to the given context based on its toString() method.
     * @param context the context
     * @param object the object
     * @throws NamingException on context failure
     */
    public static void bind(final Context context, final Object object) throws NamingException
    {
        context.bind(object.toString(), object);
    }

    /**
     * binds an object to the initial context based on its toString() method.
     * @param object the object
     * @throws NamingException on context failure
     */
    public static void bind(final Object object) throws NamingException
    {
        bind(new InitialContext(), object);
    }

    /**
     * unbinds an object from the given context.
     * @param context the context
     * @param object the object to be removed.
     * @throws NamingException on context failure
     */
    public static void unbind(final Context context, final Object object) throws NamingException
    {
        String key = ContextUtil.resolveKey(object, context, "/");
        context.unbind(key);
    }

    /**
     * unbinds an object from the initial context.
     * @param object the object to be removed.
     * @throws NamingException on context failure
     */
    public static void unbind(final Object object) throws NamingException
    {
        unbind(new InitialContext(), object);
    }

    /**
     * creates a subContext to the given context.
     * @param root the root of the context
     * @param element the element to add
     * @return the new root
     * @throws NamingException on context failure
     */
    public static Context createSubContext(final Context root, final String element) throws NamingException
    {
        try
        {
            return (Context) root.lookup(element);
        }
        catch (NamingException exception)
        {
            return root.createSubcontext(element);
        }
    }

    /**
     * creates a subContext to the initial context.
     * @param element the element to add
     * @return the new root
     * @throws NamingException on context failure
     */
    public static Context createSubContext(final String element) throws NamingException
    {
        return createSubContext(new InitialContext(), element);
    }

    /**
     * removes a subContext from the given context.
     * @param root the root of the context
     * @param element the element to remove
     * @throws NamingException on context failure
     */
    public static void removeSubContext(final Context root, final String element) throws NamingException
    {
        root.destroySubcontext(element);
    }

    /**
     * removes a subContext from the initial context.
     * @param element the element to remove
     * @throws NamingException on context failure
     */
    public static void removeSubContext(final String element) throws NamingException
    {
        removeSubContext(new InitialContext(), element);
    }

    /**
     * recursively removes a subContext from the initial context and unbinds all its elements.
     * @param name the name to remove, relative to the root of the initial context
     * @throws NamingException on context failure
     */
    public static void destroySubContext(final String name) throws NamingException
    {
        destroySubContext(new InitialContext(), name);
    }

    /**
     * recursively removes a subContext from the given context and unbinds all its elements.
     * @param root the root of the context
     * @param name the name to remove, relative to the given context, e.g. "/animation/2D"
     * @throws NamingException on context failure
     */
    public static void destroySubContext(final Context root, final String name) throws NamingException
    {
        Context subcontext = lookup(root, name);
        NamingEnumeration<Binding> list = subcontext.listBindings("");
        while (list.hasMore())
        {
            Binding binding = list.next();
            if (binding.getObject() instanceof Context)
            {
                destroySubContext(subcontext, binding.getName());
            }
            else
            {
                unbind(subcontext, binding.getObject());
            }
        }
        removeSubContext(root, name);
    }

    /**
     * resolves the context with relative name on the given root context. If the context does not exist it is created.
     * @param root the root context
     * @param name the name
     * @return the context
     * @throws NamingException on context failure
     */
    public static Context lookup(final Context root, final String name) throws NamingException
    {
        Context context = root;
        Name parsedName = context.getNameParser(name).parse(name);

        // We take the first one and see if we can build this one.
        Enumeration<String> elements = parsedName.getAll();

        while (elements.hasMoreElements())
        {
            String element = elements.nextElement();
            if (element.length() > 0)
            {
                context = ContextUtil.createSubContext(context, element);
            }
        }
        return context;
    }

    /**
     * resolves the context with relative name on initial context. If the context does not exist it is created.
     * @param name the name
     * @return the context
     * @throws NamingException on context failure
     */
    public static Context lookup(final String name) throws NamingException
    {
        return lookup(new InitialContext(), name);
    }

}