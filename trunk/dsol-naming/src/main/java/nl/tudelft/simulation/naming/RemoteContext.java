package nl.tudelft.simulation.naming;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.naming.Binding;
import javax.naming.Name;
import javax.naming.NameClassPair;
import javax.naming.NameParser;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.event.EventContext;

import nl.tudelft.simulation.naming.listener.RemoteContextListenerClient;
import nl.tudelft.simulation.naming.listener.RemoteContextListenerInterface;

/**
 * Context that exists on another computer.
 * <p>
 * Copyright (c) 2002-2019 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. See for project information <a href="https://simulation.tudelft.nl/" target="_blank">
 * https://simulation.tudelft.nl</a>. The DSOL project is distributed under a three-clause BSD-style license, which can
 * be found at <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class RemoteContext extends UnicastRemoteObject implements RemoteContextInterface
{
    /** The default serial version UID for serializable classes. */
    private static final long serialVersionUID = 1L;

    /** the underlying eventcontext. */
    private EventContext eventContext = null;

    /** the listeners. */
    private Map<RemoteContextListenerInterface, RemoteContextListenerClient> listeners =
            Collections.synchronizedMap(new HashMap<RemoteContextListenerInterface, RemoteContextListenerClient>());

    /**
     * constructs a new RemoteContext.
     * @param eventContext EventContext; the underlying context
     * @throws RemoteException on network failure
     */
    public RemoteContext(final EventContext eventContext) throws RemoteException
    {
        super();
        this.eventContext = eventContext;
    }

    /** {@inheritDoc} */
    @Override
    public void addNamingListener(Name target, int scope, RemoteContextListenerInterface l) throws NamingException
    {
        RemoteContextListenerClient client = new RemoteContextListenerClient(l);
        this.listeners.put(l, client);
        this.eventContext.addNamingListener(target, scope, client);
    }

    /** {@inheritDoc} */
    @Override
    public void addNamingListener(String target, int scope, RemoteContextListenerInterface l) throws NamingException
    {
        RemoteContextListenerClient client = new RemoteContextListenerClient(l);
        this.listeners.put(l, client);
        this.eventContext.addNamingListener(target, scope, client);
    }

    /** {@inheritDoc} */
    @Override
    public void removeNamingListener(RemoteContextListenerInterface l) throws NamingException
    {
        this.eventContext.removeNamingListener(this.listeners.remove(l));
    }

    /** {@inheritDoc} */
    @Override
    public boolean targetMustExist() throws NamingException
    {
        return this.eventContext.targetMustExist();
    }

    /** {@inheritDoc} */
    @Override
    public Object lookup(Name name) throws NamingException
    {
        return this.eventContext.lookup(name);
    }

    /** {@inheritDoc} */
    @Override
    public Object lookup(String name) throws NamingException
    {
        return this.eventContext.lookup(name);
    }

    /** {@inheritDoc} */
    @Override
    public void bind(Name name, Object obj) throws NamingException
    {
        this.eventContext.bind(name, obj);
    }

    /** {@inheritDoc} */
    @Override
    public void bind(String name, Object obj) throws NamingException
    {
        this.eventContext.bind(name, obj);
    }

    /** {@inheritDoc} */
    @Override
    public void rebind(Name name, Object obj) throws NamingException
    {
        this.eventContext.rebind(name, obj);
    }

    /** {@inheritDoc} */
    @Override
    public void rebind(String name, Object obj) throws NamingException
    {
        this.eventContext.rebind(name, obj);
    }

    /** {@inheritDoc} */
    @Override
    public void unbind(Name name) throws NamingException
    {
        this.eventContext.unbind(name);
    }

    /** {@inheritDoc} */
    @Override
    public void unbind(String name) throws NamingException
    {
        this.eventContext.unbind(name);
    }

    /** {@inheritDoc} */
    @Override
    public void rename(Name oldName, Name newName) throws NamingException
    {
        this.eventContext.rename(oldName, newName);
    }

    /** {@inheritDoc} */
    @Override
    public void rename(String oldName, String newName) throws NamingException
    {
        this.eventContext.rename(oldName, newName);
    }

    /** {@inheritDoc} */
    @Override
    public NamingEnumeration<NameClassPair> list(Name name) throws NamingException
    {
        return this.eventContext.list(name);
    }

    /** {@inheritDoc} */
    @Override
    public NamingEnumeration<NameClassPair> list(String name) throws NamingException
    {
        return this.eventContext.list(name);
    }

    /** {@inheritDoc} */
    @Override
    public NamingEnumeration<Binding> listBindings(Name name) throws NamingException
    {
        return this.eventContext.listBindings(name);
    }

    /** {@inheritDoc} */
    @Override
    public NamingEnumeration<Binding> listBindings(String name) throws NamingException
    {
        return this.eventContext.listBindings(name);
    }

    /** {@inheritDoc} */
    @Override
    public void destroySubcontext(Name name) throws NamingException
    {
        this.eventContext.destroySubcontext(name);
    }

    /** {@inheritDoc} */
    @Override
    public void destroySubcontext(String name) throws NamingException
    {
        this.eventContext.destroySubcontext(name);
    }

    /** {@inheritDoc} */
    @Override
    public RemoteContextInterface createSubcontext(Name name) throws NamingException, RemoteException
    {
        EventContext child = (EventContext) this.eventContext.createSubcontext(name);
        return new RemoteContext(child);
    }

    /** {@inheritDoc} */
    @Override
    public RemoteContextInterface createSubcontext(String name) throws NamingException, RemoteException
    {
        EventContext child = (EventContext) this.eventContext.createSubcontext(name);
        return new RemoteContext(child);
    }

    /** {@inheritDoc} */
    @Override
    public Object lookupLink(Name name) throws NamingException
    {
        return this.eventContext.lookupLink(name);
    }

    /** {@inheritDoc} */
    @Override
    public Object lookupLink(String name) throws NamingException
    {
        return this.eventContext.lookupLink(name);
    }

    /** {@inheritDoc} */
    @Override
    public NameParser getNameParser(Name name) throws NamingException
    {
        return this.eventContext.getNameParser(name);
    }

    /** {@inheritDoc} */
    @Override
    public NameParser getNameParser(String name) throws NamingException
    {
        return this.eventContext.getNameParser(name);
    }

    /** {@inheritDoc} */
    @Override
    public Name composeName(Name name, Name prefix) throws NamingException
    {
        return this.eventContext.composeName(name, prefix);
    }

    /** {@inheritDoc} */
    @Override
    public String composeName(String name, String prefix) throws NamingException
    {
        return this.eventContext.composeName(name, prefix);
    }

    /** {@inheritDoc} */
    @Override
    public Object addToEnvironment(String propName, Object propVal) throws NamingException
    {
        return this.eventContext.addToEnvironment(propName, propVal);
    }

    /** {@inheritDoc} */
    @Override
    public Object removeFromEnvironment(String propName) throws NamingException
    {
        return this.eventContext.removeFromEnvironment(propName);
    }

    /** {@inheritDoc} */
    @Override
    public Hashtable<?, ?> getEnvironment() throws NamingException
    {
        return this.eventContext.getEnvironment();
    }

    /** {@inheritDoc} */
    @Override
    public void close() throws NamingException
    {
        this.eventContext.close();
    }

    /** {@inheritDoc} */
    @Override
    public String getNameInNamespace() throws NamingException
    {
        return this.eventContext.getNameInNamespace();
    }
}
