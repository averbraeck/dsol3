package nl.tudelft.simulation.naming.context;

import java.util.Enumeration;

import javax.naming.Binding;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.Name;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;

/**
 * Context Utility class for binding, unbinding, lookup, and creation and removal of sub-contexts.
 * <p>
 * Copyright (c) 2002-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. See for project information <a href="https://simulation.tudelft.nl/" target="_blank">
 * https://simulation.tudelft.nl</a>. The DSOL project is distributed under a three-clause BSD-style license, which can
 * be found at <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public final class ContextUtil
{
    /**
     * prevent construction of a ContextUtil.
     */
    private ContextUtil()
    {
        // utility class
    }

    /**
     * resolves the name of an object under which it is accessible in the initial context.
     * @param object Object; the object
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
     * @param object Object; the object which key to resolve.
     * @param context Context; the context.
     * @param name String; the name of the parent.
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
     * @param context Context; the context
     * @param object Object; the object
     * @throws NamingException on context failure
     */
    public static void bind(final Context context, final Object object) throws NamingException
    {
        context.bind(object.toString(), object);
    }

    /**
     * binds an object to the initial context based on its toString() method.
     * @param object Object; the object
     * @throws NamingException on context failure
     */
    public static void bind(final Object object) throws NamingException
    {
        bind(new InitialContext(), object);
    }

    /**
     * unbinds an object from the given context.
     * @param context Context; the context
     * @param object Object; the object to be removed.
     * @throws NamingException on context failure
     */
    public static void unbind(final Context context, final Object object) throws NamingException
    {
        String key = ContextUtil.resolveKey(object, context, "/");
        context.unbind(key);
    }

    /**
     * unbinds an object from the initial context.
     * @param object Object; the object to be removed.
     * @throws NamingException on context failure
     */
    public static void unbind(final Object object) throws NamingException
    {
        unbind(new InitialContext(), object);
    }

    /**
     * creates a subContext to the given context.
     * @param root Context; the root of the context
     * @param element String; the element to add
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
     * @param element String; the element to add
     * @return the new root
     * @throws NamingException on context failure
     */
    public static Context createSubContext(final String element) throws NamingException
    {
        return createSubContext(new InitialContext(), element);
    }

    /**
     * removes a subContext from the given context.
     * @param root Context; the root of the context
     * @param element String; the element to remove
     * @throws NamingException on context failure
     */
    public static void removeSubContext(final Context root, final String element) throws NamingException
    {
        root.destroySubcontext(element);
    }

    /**
     * removes a subContext from the initial context.
     * @param element String; the element to remove
     * @throws NamingException on context failure
     */
    public static void removeSubContext(final String element) throws NamingException
    {
        removeSubContext(new InitialContext(), element);
    }

    /**
     * recursively removes a subContext from the initial context and unbinds all its elements.
     * @param name String; the name to remove, relative to the root of the initial context
     * @throws NamingException on context failure
     */
    public static void destroySubContext(final String name) throws NamingException
    {
        destroySubContext(new InitialContext(), name);
    }

    /**
     * recursively removes a subContext from the given context and unbinds all its elements.
     * @param root Context; the root of the context
     * @param name String; the name to remove, relative to the given context, e.g. "/animation/2D"
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
     * @param root Context; the root context
     * @param name String; the name
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
     * @param name String; the name
     * @return the context
     * @throws NamingException on context failure
     */
    public static Context lookup(final String name) throws NamingException
    {
        return lookup(new InitialContext(), name);
    }

}
