package nl.tudelft.simulation.naming.context;

import java.io.Serializable;
import java.net.URL;
import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.naming.NameNotFoundException;
import javax.naming.NamingException;

import org.djutils.event.EventListenerInterface;
import org.djutils.event.EventType;
import org.djutils.event.ref.ReferenceType;
import org.djutils.event.remote.RemoteEventProducerInterface;
import org.djutils.exceptions.Throw;
import org.djutils.rmi.RMIObject;

import nl.tudelft.simulation.naming.context.util.ContextUtil;
import nl.tudelft.simulation.naming.listener.RemoteContextListenerClient;
import nl.tudelft.simulation.naming.listener.RemoteContextListenerInterface;

/**
 * Context that exists on another computer.
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
public class RemoteContext extends RMIObject implements RemoteContextInterface, RemoteEventProducerInterface
{
    /** The default serial version UID for serializable classes. */
    private static final long serialVersionUID = 1L;

    /** the underlying event context. */
    private ContextInterface embeddedContext = null;

    /** the listeners. */
    private Map<RemoteContextListenerInterface, RemoteContextListenerClient> listeners =
            Collections.synchronizedMap(new HashMap<RemoteContextListenerInterface, RemoteContextListenerClient>());

    /**
     * Constructs a new RemoteContext. Register the new context in the RMI registry. When the RMI registry does not exist yet,
     * it will be created, but <b>only</b> on the local host. Remote creation of a registry on another computer is not possible.
     * Any attempt to do so will cause an AccessException to be fired.
     * @param host String; the host where the RMI registry resides or will be created. Creation is only possible on localhost.
     * @param port int; the port where the RMI registry can be found or will be created
     * @param bindingKey the key under which this context will be bound in the RMI registry
     * @param embeddedContext ContextInterface; the underlying context
     * @throws RemoteException when there is a problem with the RMI registry
     * @throws AlreadyBoundException when there is already another object bound to the bindingKey
     * @throws NullPointerException when host, path, or bindingKey is null
     * @throws IllegalArgumentException when port &lt; 0 or port &gt; 65535
     * @throws AccessException when there is an attempt to create a registry on a remote host
     */
    public RemoteContext(final String host, final int port, final String bindingKey, final ContextInterface embeddedContext)
            throws RemoteException, AlreadyBoundException
    {
        super(host, port, bindingKey);
        Throw.whenNull(embeddedContext, "embedded context cannot be null");
        this.embeddedContext = embeddedContext;
    }

    /**
     * Constructs a new RemoteContext. Register the new context in the RMI registry. When the host has not been specified in the
     * URL, 127.0.0.1 will be used. When the port has not been specified in the URL, the default RMI port 1099 will be used.
     * When the RMI registry does not exist yet, it will be created, but <b>only</b> on the local host. Remote creation of a
     * registry on another computer is not possible. Any attempt to do so will cause an AccessException to be fired.
     * @param registryURL URL; the URL of the registry, e.g., "http://localhost:1099" or "http://130.161.185.14:28452"
     * @param bindingKey String; the key under which this context will be bound in the RMI registry
     * @param embeddedContext ContextInterface; the underlying context
     * @throws RemoteException when there is a problem with the RMI registry
     * @throws AlreadyBoundException when there is already another object bound to the bindingKey
     * @throws NullPointerException when registryURL, bindingKey, or embeddedContext is null
     * @throws AccessException when there is an attempt to create a registry on a remote host
     */
    public RemoteContext(final URL registryURL, final ContextInterface embeddedContext)
            throws RemoteException, AlreadyBoundException
    {
        super(registryURL, registryURL.getPath());
        Throw.whenNull(embeddedContext, "embedded context cannot be null");
        this.embeddedContext = embeddedContext;
    }

    /** {@inheritDoc} */
    @Override
    public Serializable getSourceId() throws RemoteException
    {
        return getAtomicName();
    }
    
    /** {@inheritDoc} */
    @Override
    public String getAtomicName() throws RemoteException
    {
        return this.embeddedContext.getAtomicName();
    }

    /** {@inheritDoc} */
    @Override
    public ContextInterface getParent() throws RemoteException
    {
        return this.embeddedContext.getParent();
    }

    /** {@inheritDoc} */
    @Override
    public ContextInterface getRootContext() throws RemoteException
    {
        return this.embeddedContext.getRootContext();
    }

    /** {@inheritDoc} */
    @Override
    public Object get(final String name) throws NamingException, RemoteException
    {
        return this.embeddedContext.get(name);
    }

    /** {@inheritDoc} */
    @Override
    public Object getObject(final String key) throws NamingException, RemoteException
    {
        return this.embeddedContext.getObject(key);
    }

    /** {@inheritDoc} */
    @Override
    public boolean exists(final String name) throws NamingException, RemoteException
    {
        return this.embeddedContext.exists(name);
    }

    /** {@inheritDoc} */
    @Override
    public boolean hasKey(final String key) throws NamingException, RemoteException
    {
        return this.embeddedContext.hasKey(key);
    }

    /** {@inheritDoc} */
    @Override
    public boolean hasObject(final Object object) throws RemoteException
    {
        return this.embeddedContext.hasObject(object);
    }

    /** {@inheritDoc} */
    @Override
    public boolean isEmpty() throws RemoteException
    {
        return this.embeddedContext.isEmpty();
    }

    /** {@inheritDoc} */
    @Override
    public void bind(final String name, final Object object) throws NamingException, RemoteException
    {
        this.embeddedContext.bind(name, object);
    }

    /** {@inheritDoc} */
    @Override
    public void bindObject(final String key, final Object object) throws NamingException, RemoteException
    {
        this.embeddedContext.bindObject(key, object);
    }

    /** {@inheritDoc} */
    @Override
    public void bindObject(final Object object) throws NamingException, RemoteException
    {
        this.embeddedContext.bindObject(object);
    }

    /** {@inheritDoc} */
    @Override
    public void unbind(final String name) throws NamingException, RemoteException
    {
        this.embeddedContext.unbind(name);
    }

    /** {@inheritDoc} */
    @Override
    public void unbindObject(final String key) throws NamingException, RemoteException
    {
        this.embeddedContext.unbindObject(key);
    }

    /** {@inheritDoc} */
    @Override
    public void rebind(final String name, final Object object) throws NamingException, RemoteException
    {
        this.embeddedContext.rebind(name, object);
    }

    /** {@inheritDoc} */
    @Override
    public void rebindObject(final String key, final Object object) throws NamingException, RemoteException
    {
        this.embeddedContext.rebindObject(key, object);
    }

    /** {@inheritDoc} */
    @Override
    public void rename(final String oldName, final String newName) throws NamingException, RemoteException
    {
        this.embeddedContext.rename(oldName, newName);
    }

    /** {@inheritDoc} */
    @Override
    public ContextInterface createSubcontext(final String name) throws NamingException, RemoteException
    {
        return this.embeddedContext.createSubcontext(name);
    }

    /** {@inheritDoc} */
    @Override
    public void destroySubcontext(final String name) throws NamingException, RemoteException
    {
        this.embeddedContext.destroySubcontext(name);
    }

    /** {@inheritDoc} */
    @Override
    public Set<String> keySet() throws RemoteException
    {
        return new LinkedHashSet<String>(this.embeddedContext.keySet());
    }

    /** {@inheritDoc} */
    @Override
    public Collection<Object> values() throws RemoteException
    {
        return new LinkedHashSet<Object>(this.embeddedContext.values());
    }

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> bindings() throws RemoteException
    {
        return this.embeddedContext.bindings();
    }

    /** {@inheritDoc} */
    @Override
    public void checkCircular(final Object newObject) throws NamingException, RemoteException
    {
        this.embeddedContext.checkCircular(newObject);
    }

    /** {@inheritDoc} */
    @Override
    public void close() throws NamingException, RemoteException
    {
        // TODO: see if connection needs to be closed
        this.embeddedContext.close();
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        if (this.embeddedContext != null)
            return this.embeddedContext.toString();
        return "RemoteContext[null]";
    }

    /** {@inheritDoc} */
    @Override
    public String toString(final boolean verbose) throws RemoteException
    {
        if (!verbose)
        {
            return "RemoteContext[" + getAtomicName() + "]";
        }
        return ContextUtil.toText(this);
    }
    /* ***************************************************************************************************************** */

    /** {@inheritDoc} */
    @Override
    public void fireObjectChangedEvent(Object object)
            throws NameNotFoundException, NullPointerException, NamingException, RemoteException
    {
    }

    /** {@inheritDoc} */
    @Override
    public void fireObjectChangedEvent(String key)
            throws NameNotFoundException, NullPointerException, NamingException, RemoteException
    {
    }

    /** {@inheritDoc} */
    @Override
    public boolean addListener(EventListenerInterface listener, EventType eventType) throws RemoteException
    {
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public boolean addListener(EventListenerInterface listener, EventType eventType, ReferenceType referenceType)
            throws RemoteException
    {
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public boolean addListener(EventListenerInterface listener, EventType eventType, int position) throws RemoteException
    {
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public boolean addListener(EventListenerInterface listener, EventType eventType, int position, ReferenceType referenceType)
            throws RemoteException
    {
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public boolean removeListener(EventListenerInterface listener, EventType eventType) throws RemoteException
    {
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public boolean hasListeners() throws RemoteException
    {
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public int numberOfListeners(EventType eventType) throws RemoteException
    {
        return 0;
    }

    /** {@inheritDoc} */
    @Override
    public Set<EventType> getEventTypesWithListeners() throws RemoteException
    {
        return null;
    }

}
