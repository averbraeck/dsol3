/*
 * @(#) ContextUtil.java Oct 26, 2003 Copyright (c) 2002-2005 Delft University
 * of Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. This software is proprietary information of Delft University of
 * Technology 
 */
package nl.tudelft.simulation.naming.context;

import java.util.Enumeration;

import javax.naming.Binding;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.Name;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;

import nl.tudelft.simulation.logger.Logger;

/**
 * ContextUtility class
 * <p>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl"> www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @version 1.2 2004-03-24
 * @since 1.5
 */
public class ContextUtil
{
    /**
     * constructs a new ContextUtil
     */
    protected ContextUtil()
    {
        super();
    }

    /**
     * resolves the name of an object under which it is accessible in the context
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
     * resolves the key under which an object is stored in the context.
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
     * unbinds an object from the context
     * @param object the object to be removed.
     */
    public static void unbindFromContext(Object object)
    {
        try
        {
            InitialContext context = new InitialContext();
            String key = ContextUtil.resolveKey(object, context, "/");
            context.unbind(key);
        }
        catch (NamingException namingException)
        {
            Logger.warning(ContextUtil.class, "unbindFromContext", namingException);
        }
    }

    /**
     * binds an object to the context based on its toString() method.
     * @param context the context
     * @param object the object
     */
    public static void bind(final Context context, final Object object)
    {
        try
        {
            context.bind(object.toString(), object);
        }
        catch (NamingException namingException)
        {
            Logger.warning(ContextUtil.class, "bind", namingException);
        }
    }

    /**
     * creates a subContext
     * @param root the root of the context
     * @param element the element to add
     * @return the new root
     */
    private static Context createSubContext(final Context root, final String element)
    {
        try
        {
            return (Context) root.lookup(element);
        }
        catch (Exception exception)
        {
            try
            {
                return root.createSubcontext(element);
            }
            catch (NamingException namingException)
            {
                Logger.warning(ContextUtil.class, "createSubContext", namingException);
            }
            return root;
        }
    }

    /**
     * resolves the context with relative name on root. If the context does not exist it is created.
     * @param root the root context
     * @param name the name
     * @return the context
     */
    public static Context lookup(Context root, final String name)
    {
        try
        {
            Name parsedName = root.getNameParser(name).parse(name);

            // We take the first one and see if we can build this one.
            Enumeration<String> elements = parsedName.getAll();

            while (elements.hasMoreElements())
            {
                String element = elements.nextElement();
                if (element.length() > 0)
                {
                    root = ContextUtil.createSubContext(root, element);
                }
            }
            return root;
        }
        catch (NamingException namingException)
        {
            Logger.warning(ContextUtil.class, "unbindFromContext", namingException);
            return null;
        }
    }
}