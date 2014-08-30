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
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @version 1.1 2004-03-24
 * @since 1.5
 */
public class RemoteContextClient implements EventContext, Serializable
{
    /** The default serial version UID for serializable classes. */
    private static final long serialVersionUID = 1L;

    /** the remoteContext on which all calls are passed. */
    private RemoteContextInterface remoteContext = null;

    /**
     * constructs a new RemoteContextClient.
     * @param remoteContext the remoteContext on which all calls are passed.
     */
    public RemoteContextClient(final RemoteContextInterface remoteContext)
    {
        super();
        this.remoteContext = remoteContext;
    }

    /** {@inheritDoc} */
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

    /** {@inheritDoc} */
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

    /** {@inheritDoc} */
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
