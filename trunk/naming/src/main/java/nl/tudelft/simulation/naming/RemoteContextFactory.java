/*
 * @(#)JVMContextFactory.java Feb 1, 2003 Copyright (c) 2002-2005 Delft
 * University of Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All
 * rights reserved. This software is proprietary information of Delft University
 * of Technology 
 */
package nl.tudelft.simulation.naming;

import java.net.InetAddress;
import java.net.URL;
import java.rmi.ConnectException;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Hashtable;
import java.util.Iterator;

import javax.naming.Context;
import javax.naming.event.EventContext;
import javax.naming.spi.InitialContextFactory;

import nl.tudelft.simulation.logger.Logger;

/**
 * A factory for RemoteContextClient instances, automatically invoked by JNDI
 * when the correct jndi.properties file has been used.
 * <p>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft
 * University of Technology </a>, the Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">
 * www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser
 * General Public License (LGPL) </a>, no warranty.
 * 
 * @author <a href="http://www.peter-jacobs.com">Peter Jacobs </a>
 * @version 1.1 2004-03-24
 * @since 1.5
 */
public class RemoteContextFactory implements InitialContextFactory
{
    /** context refers to the static RemoteContextClient */
    private static RemoteContextClient context = null;

    /**
     * @see javax.naming.spi.InitialContextFactory #getInitialContext(Hashtable)
     */
    public synchronized Context getInitialContext(
            final Hashtable< ? extends Object, ? extends Object> environment)
    {
        // If the context is already looked up, let's return immediately
        if (RemoteContextFactory.context != null)
        {
            return RemoteContextFactory.context;
        }

        // Let's look for our remote partner
        try
        {
            URL url = new URL(environment.get(Context.PROVIDER_URL).toString());
            Registry registry = LocateRegistry.getRegistry(url.getHost(), url
                    .getPort());

            // If there is no registry, registry!=null, so we have to test the
            // registry
            // to make sure whether there is one or not. We test by requesting a
            // list. This code
            // might be improved.
            try
            {
                registry.list();
            } catch (ConnectException connectException)
            {
                // Since we cannot find the registry, we must perhaps create
                // one..
                // This is only allowed if the host is our localhost. We cannot
                // create a registry on a remote host.
                if (!(url.getHost().equals("localhost")
                        || url.getHost().equals("127.0.0.1")
                        || url.getHost().equals(
                                InetAddress.getLocalHost().getHostName()) || url
                        .getHost().equals(
                                InetAddress.getLocalHost().getHostAddress())))
                {
                    throw new IllegalArgumentException(
                            "cannot create registry on remote host");
                }
                registry = LocateRegistry.createRegistry(url.getPort());
            }
            // We now have a registry. Let's resolve the context object

            RemoteContextInterface remoteContext = null;
            try
            {
                remoteContext = (RemoteContextInterface) registry.lookup(url
                        .getFile());
            } catch (NotBoundException notBoundException)
            {
                // Since we cannot find the context, we must create one..
                // This is done based on the java.naming.wrapped properties in
                // jndi.properties
                Hashtable<Object, Object> wrappedEnvironment = new Hashtable<Object, Object>();
                for (Iterator< ? > iterator = environment.keySet().iterator(); iterator
                        .hasNext();)
                {
                    String key = iterator.next().toString();
                    if (key.startsWith(RemoteContextInterface.WRAPPED_PREFIX))
                    {
                        wrappedEnvironment.put(key.replaceFirst(
                                RemoteContextInterface.WRAPPED_PREFIX,
                                "java.naming"), environment.get(key));
                    }
                }
                if (wrappedEnvironment.isEmpty())
                {
                    // If we do not throw this exception and accept an empty
                    // environment, we'll get in an infinte loop
                    throw new IllegalArgumentException(
                            "no wrapped initial context defined");
                }
                EventContext wrappedContext = new InitialEventContext(
                        wrappedEnvironment);
                remoteContext = new RemoteContext(wrappedContext);
                registry.bind(url.getFile(), remoteContext);
            }
            RemoteContextFactory.context = new RemoteContextClient(
                    remoteContext);
            return RemoteContextFactory.context;
        } catch (Exception exception)
        {
            Logger.warning(this, "getInitialContext", exception);
            return null;
        }
    }
}