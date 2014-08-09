/*
 * @(#) FileContext.java Oct 23, 2003 Copyright (c) 2002-2005 Delft University
 * of Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. This software is proprietary information of Delft University of
 * Technology 
 */
package nl.tudelft.simulation.naming;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.Hashtable;

import javax.naming.Binding;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NameClassPair;
import javax.naming.NameParser;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.event.EventContext;
import javax.naming.event.NamingListener;

import nl.tudelft.simulation.naming.listener.ContextListenerInterface;
import nl.tudelft.simulation.naming.listener.RemoteContextListener;

/**
 * The FileContext as implementation of the Context interface.
 * <p>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl"> www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="http://www.peter-jacobs.com">Peter Jacobs </a>
 * @version 1.1 2004-03-24
 * @since 1.5
 */
public class RemoteContextClient implements EventContext, Serializable
{
    /** The default serial version UID for serializable classes */
    private static final long serialVersionUID = 1L;

    /** the remoteContext on which all calls are passed */
    private RemoteContextInterface remoteContext = null;

    /**
     * constructs a new RemoteContextClient
     * @param remoteContext the remoteContext on which all calls are passed.
     */
    public RemoteContextClient(final RemoteContextInterface remoteContext)
    {
        super();
        this.remoteContext = remoteContext;
    }

    /**
     * @see javax.naming.event.EventContext#addNamingListener(javax.naming.Name, int, javax.naming.event.NamingListener)
     */
    public void addNamingListener(Name target, int scope, NamingListener l) throws NamingException
    {
        try
        {
            this.remoteContext
                    .addNamingListener(target, scope, new RemoteContextListener((ContextListenerInterface) l));
        }
        catch (RemoteException remoteException)
        {
            throw new NamingException(remoteException.getMessage());
        }
    }

    /**
     * @see javax.naming.event.EventContext #addNamingListener(java.lang.String, int, javax.naming.event.NamingListener)
     */
    public void addNamingListener(String target, int scope, NamingListener l) throws NamingException
    {
        try
        {
            this.remoteContext
                    .addNamingListener(target, scope, new RemoteContextListener((ContextListenerInterface) l));
        }
        catch (RemoteException remoteException)
        {
            throw new NamingException(remoteException.getMessage());
        }
    }

    /**
     * @see javax.naming.event.EventContext #removeNamingListener(javax.naming.event.NamingListener)
     */
    public void removeNamingListener(NamingListener l) throws NamingException
    {
        try
        {
            this.remoteContext.removeNamingListener(new RemoteContextListener((ContextListenerInterface) l));
        }
        catch (RemoteException remoteException)
        {
            throw new NamingException(remoteException.getMessage());
        }
    }

    /**
     * @see javax.naming.event.EventContext#targetMustExist()
     */
    public boolean targetMustExist() throws NamingException
    {
        try
        {
            return this.remoteContext.targetMustExist();
        }
        catch (RemoteException remoteException)
        {
            throw new NamingException(remoteException.getMessage());
        }
    }

    /**
     * @see javax.naming.Context#addToEnvironment(java.lang.String, java.lang.Object)
     */
    public Object addToEnvironment(String propName, Object propVal) throws NamingException
    {
        try
        {
            return this.remoteContext.addToEnvironment(propName, propVal);
        }
        catch (RemoteException remoteException)
        {
            throw new NamingException(remoteException.getMessage());
        }
    }

    /**
     * @see javax.naming.Context#bind(javax.naming.Name, java.lang.Object)
     */
    public void bind(Name name, Object obj) throws NamingException
    {
        try
        {
            this.remoteContext.bind(name, obj);
        }
        catch (RemoteException remoteException)
        {
            throw new NamingException(remoteException.getMessage());
        }
    }

    /**
     * @see javax.naming.Context#bind(java.lang.String, java.lang.Object)
     */
    public void bind(String name, Object obj) throws NamingException
    {
        try
        {
            this.remoteContext.bind(name, obj);
        }
        catch (RemoteException remoteException)
        {
            throw new NamingException(remoteException.getMessage());
        }
    }

    /**
     * @see javax.naming.Context#close()
     */
    public void close() throws NamingException
    {
        try
        {
            this.remoteContext.close();
        }
        catch (RemoteException remoteException)
        {
            throw new NamingException(remoteException.getMessage());
        }
    }

    /**
     * @see javax.naming.Context#composeName(javax.naming.Name, javax.naming.Name)
     */
    public Name composeName(Name name, Name prefix) throws NamingException
    {
        try
        {
            return this.remoteContext.composeName(name, prefix);
        }
        catch (RemoteException remoteException)
        {
            throw new NamingException(remoteException.getMessage());
        }
    }

    /**
     * @see javax.naming.Context#composeName(java.lang.String, java.lang.String)
     */
    public String composeName(String name, String prefix) throws NamingException
    {
        try
        {
            return this.remoteContext.composeName(name, prefix);
        }
        catch (RemoteException remoteException)
        {
            throw new NamingException(remoteException.getMessage());
        }
    }

    /**
     * @see javax.naming.Context#createSubcontext(javax.naming.Name)
     */
    public Context createSubcontext(Name name) throws NamingException
    {
        try
        {
            return new RemoteContextClient(this.remoteContext.createSubcontext(name));
        }
        catch (RemoteException remoteException)
        {
            throw new NamingException(remoteException.getMessage());
        }

    }

    /**
     * @see javax.naming.Context#createSubcontext(java.lang.String)
     */
    public Context createSubcontext(String name) throws NamingException
    {
        try
        {
            return new RemoteContextClient(this.remoteContext.createSubcontext(name));
        }
        catch (RemoteException remoteException)
        {
            throw new NamingException(remoteException.getMessage());
        }
    }

    /**
     * @see javax.naming.Context#destroySubcontext(javax.naming.Name)
     */
    public void destroySubcontext(Name name) throws NamingException
    {
        try
        {
            this.remoteContext.destroySubcontext(name);
        }
        catch (RemoteException remoteException)
        {
            throw new NamingException(remoteException.getMessage());
        }
    }

    /**
     * @see javax.naming.Context#destroySubcontext(java.lang.String)
     */
    public void destroySubcontext(String name) throws NamingException
    {
        try
        {
            this.remoteContext.destroySubcontext(name);
        }
        catch (RemoteException remoteException)
        {
            throw new NamingException(remoteException.getMessage());
        }
    }

    /**
     * @see javax.naming.Context#getEnvironment()
     */
    public Hashtable<?, ?> getEnvironment() throws NamingException
    {
        try
        {
            return this.remoteContext.getEnvironment();
        }
        catch (RemoteException remoteException)
        {
            throw new NamingException(remoteException.getMessage());
        }

    }

    /**
     * @see javax.naming.Context#getNameInNamespace()
     */
    public String getNameInNamespace() throws NamingException
    {
        try
        {
            return this.remoteContext.getNameInNamespace();
        }
        catch (RemoteException remoteException)
        {
            throw new NamingException(remoteException.getMessage());
        }
    }

    /**
     * @see javax.naming.Context#getNameParser(javax.naming.Name)
     */
    public NameParser getNameParser(Name name) throws NamingException
    {
        try
        {
            return this.remoteContext.getNameParser(name);
        }
        catch (RemoteException remoteException)
        {
            throw new NamingException(remoteException.getMessage());
        }

    }

    /**
     * @see javax.naming.Context#getNameParser(java.lang.String)
     */
    public NameParser getNameParser(String name) throws NamingException
    {
        try
        {
            return this.remoteContext.getNameParser(name);
        }
        catch (RemoteException remoteException)
        {
            throw new NamingException(remoteException.getMessage());
        }

    }

    /**
     * @see javax.naming.Context#list(javax.naming.Name)
     */
    public NamingEnumeration<NameClassPair> list(Name name) throws NamingException
    {
        try
        {
            return this.remoteContext.list(name);
        }
        catch (RemoteException remoteException)
        {
            throw new NamingException(remoteException.getMessage());
        }

    }

    /**
     * @see javax.naming.Context#list(java.lang.String)
     */
    public NamingEnumeration<NameClassPair> list(String name) throws NamingException
    {
        try
        {
            return this.remoteContext.list(name);
        }
        catch (RemoteException remoteException)
        {
            throw new NamingException(remoteException.getMessage());
        }

    }

    /**
     * @see javax.naming.Context#listBindings(javax.naming.Name)
     */
    public NamingEnumeration<Binding> listBindings(Name name) throws NamingException
    {
        try
        {
            return this.remoteContext.listBindings(name);
        }
        catch (RemoteException remoteException)
        {
            throw new NamingException(remoteException.getMessage());
        }

    }

    /**
     * @see javax.naming.Context#listBindings(java.lang.String)
     */
    public NamingEnumeration<Binding> listBindings(String name) throws NamingException
    {
        try
        {
            return this.remoteContext.listBindings(name);
        }
        catch (RemoteException remoteException)
        {
            throw new NamingException(remoteException.getMessage());
        }

    }

    /**
     * @see javax.naming.Context#lookup(javax.naming.Name)
     */
    public Object lookup(Name name) throws NamingException
    {
        try
        {
            return this.remoteContext.lookup(name);
        }
        catch (RemoteException remoteException)
        {
            throw new NamingException(remoteException.getMessage());
        }

    }

    /**
     * @see javax.naming.Context#lookup(java.lang.String)
     */
    public Object lookup(String name) throws NamingException
    {
        try
        {
            return this.remoteContext.lookup(name);
        }
        catch (RemoteException remoteException)
        {
            throw new NamingException(remoteException.getMessage());
        }

    }

    /**
     * @see javax.naming.Context#lookupLink(javax.naming.Name)
     */
    public Object lookupLink(Name name) throws NamingException
    {
        try
        {
            return this.remoteContext.lookupLink(name);
        }
        catch (RemoteException remoteException)
        {
            throw new NamingException(remoteException.getMessage());
        }

    }

    /**
     * @see javax.naming.Context#lookupLink(java.lang.String)
     */
    public Object lookupLink(String name) throws NamingException
    {
        try
        {
            return this.remoteContext.lookupLink(name);
        }
        catch (RemoteException remoteException)
        {
            throw new NamingException(remoteException.getMessage());
        }

    }

    /**
     * @see javax.naming.Context#rebind(javax.naming.Name, java.lang.Object)
     */
    public void rebind(Name name, Object obj) throws NamingException
    {
        try
        {
            this.remoteContext.rebind(name, obj);
        }
        catch (RemoteException remoteException)
        {
            throw new NamingException(remoteException.getMessage());
        }

    }

    /**
     * @see javax.naming.Context#rebind(java.lang.String, java.lang.Object)
     */
    public void rebind(String name, Object obj) throws NamingException
    {
        try
        {
            this.remoteContext.rebind(name, obj);
        }
        catch (RemoteException remoteException)
        {
            throw new NamingException(remoteException.getMessage());
        }
    }

    /**
     * @see javax.naming.Context#removeFromEnvironment(java.lang.String)
     */
    public Object removeFromEnvironment(String propName) throws NamingException
    {
        try
        {
            return this.remoteContext.removeFromEnvironment(propName);
        }
        catch (RemoteException remoteException)
        {
            throw new NamingException(remoteException.getMessage());
        }

    }

    /**
     * @see javax.naming.Context#rename(javax.naming.Name, javax.naming.Name)
     */
    public void rename(Name oldName, Name newName) throws NamingException
    {
        try
        {
            this.remoteContext.rename(oldName, newName);
        }
        catch (RemoteException remoteException)
        {
            throw new NamingException(remoteException.getMessage());
        }
    }

    /**
     * @see javax.naming.Context#rename(java.lang.String, java.lang.String)
     */
    public void rename(String oldName, String newName) throws NamingException
    {
        try
        {
            this.remoteContext.rename(oldName, newName);
        }
        catch (RemoteException remoteException)
        {
            throw new NamingException(remoteException.getMessage());
        }
    }

    /**
     * @see javax.naming.Context#unbind(javax.naming.Name)
     */
    public void unbind(Name name) throws NamingException
    {
        try
        {
            this.remoteContext.unbind(name);
        }
        catch (RemoteException remoteException)
        {
            throw new NamingException(remoteException.getMessage());
        }

    }

    /**
     * @see javax.naming.Context#unbind(java.lang.String)
     */
    public void unbind(String name) throws NamingException
    {
        try
        {
            this.remoteContext.unbind(name);
        }
        catch (RemoteException remoteException)
        {
            throw new NamingException(remoteException.getMessage());
        }
    }
}