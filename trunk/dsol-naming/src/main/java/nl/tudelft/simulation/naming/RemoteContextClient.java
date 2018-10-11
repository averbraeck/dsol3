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
 * The client-side of the RemoteContext.
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
public class RemoteContextClient implements EventContext, Serializable
{
    /** The default serial version UID for serializable classes. */
    private static final long serialVersionUID = 1L;

    /** the remoteContext on which all calls are passed. */
    private RemoteContextInterface remoteContext = null;

    /**
     * constructs a new RemoteContextClient.
     * @param remoteContext RemoteContextInterface; the remoteContext on which all calls are passed.
     */
    public RemoteContextClient(final RemoteContextInterface remoteContext)
    {
        super();
        this.remoteContext = remoteContext;
    }

    /** {@inheritDoc} */
    @Override
    public void addNamingListener(Name target, int scope, NamingListener l) throws NamingException
    {
        try
        {
            this.remoteContext.addNamingListener(target, scope,
                    new RemoteContextListener((ContextListenerInterface) l));
        }
        catch (RemoteException remoteException)
        {
            throw new NamingException(remoteException.getMessage());
        }
    }

    /** {@inheritDoc} */
    @Override
    public void addNamingListener(String target, int scope, NamingListener l) throws NamingException
    {
        try
        {
            this.remoteContext.addNamingListener(target, scope,
                    new RemoteContextListener((ContextListenerInterface) l));
        }
        catch (RemoteException remoteException)
        {
            throw new NamingException(remoteException.getMessage());
        }
    }

    /** {@inheritDoc} */
    @Override
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

    /** {@inheritDoc} */
    @Override
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

    /** {@inheritDoc} */
    @Override
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

    /** {@inheritDoc} */
    @Override
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

    /** {@inheritDoc} */
    @Override
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

    /** {@inheritDoc} */
    @Override
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

    /** {@inheritDoc} */
    @Override
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

    /** {@inheritDoc} */
    @Override
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

    /** {@inheritDoc} */
    @Override
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

    /** {@inheritDoc} */
    @Override
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

    /** {@inheritDoc} */
    @Override
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

    /** {@inheritDoc} */
    @Override
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

    /** {@inheritDoc} */
    @Override
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

    /** {@inheritDoc} */
    @Override
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

    /** {@inheritDoc} */
    @Override
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

    /** {@inheritDoc} */
    @Override
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

    /** {@inheritDoc} */
    @Override
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

    /** {@inheritDoc} */
    @Override
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

    /** {@inheritDoc} */
    @Override
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

    /** {@inheritDoc} */
    @Override
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

    /** {@inheritDoc} */
    @Override
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

    /** {@inheritDoc} */
    @Override
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

    /** {@inheritDoc} */
    @Override
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

    /** {@inheritDoc} */
    @Override
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

    /** {@inheritDoc} */
    @Override
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

    /** {@inheritDoc} */
    @Override
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

    /** {@inheritDoc} */
    @Override
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

    /** {@inheritDoc} */
    @Override
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

    /** {@inheritDoc} */
    @Override
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

    /** {@inheritDoc} */
    @Override
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

    /** {@inheritDoc} */
    @Override
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
