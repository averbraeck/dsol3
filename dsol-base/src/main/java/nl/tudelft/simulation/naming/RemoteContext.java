/*
 * @(#) RemoteContext.java Apr 7, 2004 Copyright (c) 2002-2005 Delft University
 * of Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. This software is proprietary information of Delft University of
 * Technology 
 */
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
 * <p>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @version 1.1 Apr 7, 2004
 * @since 1.5
 */
public class RemoteContext extends UnicastRemoteObject implements RemoteContextInterface
{
    /** The default serial version UID for serializable classes */
    private static final long serialVersionUID = 1L;

    /** the underlying eventcontext */
    private EventContext eventContext = null;

    /** the listeners */
    private Map<RemoteContextListenerInterface, RemoteContextListenerClient> listeners = Collections
            .synchronizedMap(new HashMap<RemoteContextListenerInterface, RemoteContextListenerClient>());

    /**
     * constructs a new RemoteContext
     * @param eventContext the underlying context
     * @throws RemoteException on network failure
     */
    public RemoteContext(final EventContext eventContext) throws RemoteException
    {
        super();
        this.eventContext = eventContext;
    }

    /**
     * @see nl.tudelft.simulation.naming.RemoteContextInterface#addNamingListener(javax.naming.Name, int,
     *      nl.tudelft.simulation.naming.listener.RemoteContextListenerInterface)
     */
    public void addNamingListener(Name target, int scope, RemoteContextListenerInterface l) throws NamingException
    {
        RemoteContextListenerClient client = new RemoteContextListenerClient(l);
        this.listeners.put(l, client);
        this.eventContext.addNamingListener(target, scope, client);
    }

    /**
     * @see nl.tudelft.simulation.naming.RemoteContextInterface#addNamingListener(java.lang.String, int,
     *      nl.tudelft.simulation.naming.listener.RemoteContextListenerInterface)
     */
    public void addNamingListener(String target, int scope, RemoteContextListenerInterface l) throws NamingException
    {
        RemoteContextListenerClient client = new RemoteContextListenerClient(l);
        this.listeners.put(l, client);
        this.eventContext.addNamingListener(target, scope, client);
    }

    /**
     * @see nl.tudelft.simulation.naming.RemoteContextInterface#removeNamingListener(nl.tudelft.simulation.naming.listener.RemoteContextListenerInterface)
     */
    public void removeNamingListener(RemoteContextListenerInterface l) throws NamingException
    {
        this.eventContext.removeNamingListener(this.listeners.remove(l));
    }

    /**
     * @see nl.tudelft.simulation.naming.RemoteContextInterface#targetMustExist()
     */
    public boolean targetMustExist() throws NamingException
    {
        return this.eventContext.targetMustExist();
    }

    /**
     * @see nl.tudelft.simulation.naming.RemoteContextInterface#lookup(javax.naming.Name)
     */
    public Object lookup(Name name) throws NamingException
    {
        return this.eventContext.lookup(name);
    }

    /**
     * @see nl.tudelft.simulation.naming.RemoteContextInterface#lookup(java.lang.String)
     */
    public Object lookup(String name) throws NamingException
    {
        return this.eventContext.lookup(name);
    }

    /**
     * @see nl.tudelft.simulation.naming.RemoteContextInterface#bind(javax.naming.Name, java.lang.Object)
     */
    public void bind(Name name, Object obj) throws NamingException
    {
        this.eventContext.bind(name, obj);
    }

    /**
     * @see nl.tudelft.simulation.naming.RemoteContextInterface#bind(java.lang.String, java.lang.Object)
     */
    public void bind(String name, Object obj) throws NamingException
    {
        this.eventContext.bind(name, obj);
    }

    /**
     * @see nl.tudelft.simulation.naming.RemoteContextInterface#rebind(javax.naming.Name, java.lang.Object)
     */
    public void rebind(Name name, Object obj) throws NamingException
    {
        this.eventContext.rebind(name, obj);
    }

    /**
     * @see nl.tudelft.simulation.naming.RemoteContextInterface#rebind(java.lang.String, java.lang.Object)
     */
    public void rebind(String name, Object obj) throws NamingException
    {
        this.eventContext.rebind(name, obj);
    }

    /**
     * @see nl.tudelft.simulation.naming.RemoteContextInterface#unbind(javax.naming.Name)
     */
    public void unbind(Name name) throws NamingException
    {
        this.eventContext.unbind(name);
    }

    /**
     * @see nl.tudelft.simulation.naming.RemoteContextInterface#unbind(java.lang.String)
     */
    public void unbind(String name) throws NamingException
    {
        this.eventContext.unbind(name);
    }

    /**
     * @see nl.tudelft.simulation.naming.RemoteContextInterface#rename(javax.naming.Name, javax.naming.Name)
     */
    public void rename(Name oldName, Name newName) throws NamingException
    {
        this.eventContext.rename(oldName, newName);
    }

    /**
     * @see nl.tudelft.simulation.naming.RemoteContextInterface#rename(java.lang.String, java.lang.String)
     */
    public void rename(String oldName, String newName) throws NamingException
    {
        this.eventContext.rename(oldName, newName);
    }

    /**
     * @see nl.tudelft.simulation.naming.RemoteContextInterface#list(javax.naming.Name)
     */
    public NamingEnumeration<NameClassPair> list(Name name) throws NamingException
    {
        return this.eventContext.list(name);
    }

    /**
     * @see nl.tudelft.simulation.naming.RemoteContextInterface#list(java.lang.String)
     */
    public NamingEnumeration<NameClassPair> list(String name) throws NamingException
    {
        return this.eventContext.list(name);
    }

    /**
     * @see nl.tudelft.simulation.naming.RemoteContextInterface#listBindings(javax.naming.Name)
     */
    public NamingEnumeration<Binding> listBindings(Name name) throws NamingException
    {
        return this.eventContext.listBindings(name);
    }

    /**
     * @see nl.tudelft.simulation.naming.RemoteContextInterface#listBindings(java.lang.String)
     */
    public NamingEnumeration<Binding> listBindings(String name) throws NamingException
    {
        return this.eventContext.listBindings(name);
    }

    /**
     * @see nl.tudelft.simulation.naming.RemoteContextInterface#destroySubcontext(javax.naming.Name)
     */
    public void destroySubcontext(Name name) throws NamingException
    {
        this.eventContext.destroySubcontext(name);
    }

    /**
     * @see nl.tudelft.simulation.naming.RemoteContextInterface#destroySubcontext(java.lang.String)
     */
    public void destroySubcontext(String name) throws NamingException
    {
        this.eventContext.destroySubcontext(name);
    }

    /**
     * @see nl.tudelft.simulation.naming.RemoteContextInterface#createSubcontext(javax.naming.Name)
     */
    public RemoteContextInterface createSubcontext(Name name) throws NamingException, RemoteException
    {
        EventContext child = (EventContext) this.eventContext.createSubcontext(name);
        return new RemoteContext(child);
    }

    /**
     * @see nl.tudelft.simulation.naming.RemoteContextInterface#createSubcontext(java.lang.String)
     */
    public RemoteContextInterface createSubcontext(String name) throws NamingException, RemoteException
    {
        EventContext child = (EventContext) this.eventContext.createSubcontext(name);
        return new RemoteContext(child);
    }

    /**
     * @see nl.tudelft.simulation.naming.RemoteContextInterface#lookupLink(javax.naming.Name)
     */
    public Object lookupLink(Name name) throws NamingException
    {
        return this.eventContext.lookupLink(name);
    }

    /**
     * @see nl.tudelft.simulation.naming.RemoteContextInterface#lookupLink(java.lang.String)
     */
    public Object lookupLink(String name) throws NamingException
    {
        return this.eventContext.lookupLink(name);
    }

    /**
     * @see nl.tudelft.simulation.naming.RemoteContextInterface#getNameParser(javax.naming.Name)
     */
    public NameParser getNameParser(Name name) throws NamingException
    {
        return this.eventContext.getNameParser(name);
    }

    /**
     * @see nl.tudelft.simulation.naming.RemoteContextInterface#getNameParser(java.lang.String)
     */
    public NameParser getNameParser(String name) throws NamingException
    {
        return this.eventContext.getNameParser(name);
    }

    /**
     * @see nl.tudelft.simulation.naming.RemoteContextInterface#composeName(javax.naming.Name, javax.naming.Name)
     */
    public Name composeName(Name name, Name prefix) throws NamingException
    {
        return this.eventContext.composeName(name, prefix);
    }

    /**
     * @see nl.tudelft.simulation.naming.RemoteContextInterface#composeName(java.lang.String, java.lang.String)
     */
    public String composeName(String name, String prefix) throws NamingException
    {
        return this.eventContext.composeName(name, prefix);
    }

    /**
     * @see nl.tudelft.simulation.naming.RemoteContextInterface#addToEnvironment(java.lang.String, java.lang.Object)
     */
    public Object addToEnvironment(String propName, Object propVal) throws NamingException
    {
        return this.eventContext.addToEnvironment(propName, propVal);
    }

    /**
     * @see nl.tudelft.simulation.naming.RemoteContextInterface#removeFromEnvironment(java.lang.String)
     */
    public Object removeFromEnvironment(String propName) throws NamingException
    {
        return this.eventContext.removeFromEnvironment(propName);
    }

    /**
     * @see nl.tudelft.simulation.naming.RemoteContextInterface#getEnvironment()
     */
    public Hashtable<?, ?> getEnvironment() throws NamingException
    {
        return this.eventContext.getEnvironment();
    }

    /**
     * @see nl.tudelft.simulation.naming.RemoteContextInterface#close()
     */
    public void close() throws NamingException
    {
        this.eventContext.close();
    }

    /**
     * @see nl.tudelft.simulation.naming.RemoteContextInterface#getNameInNamespace()
     */
    public String getNameInNamespace() throws NamingException
    {
        return this.eventContext.getNameInNamespace();
    }
}