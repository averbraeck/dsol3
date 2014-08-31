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
    /** The default serial version UID for serializable classes. */
    private static final long serialVersionUID = 1L;

    /** the underlying eventcontext. */
    private EventContext eventContext = null;

    /** the listeners. */
    private Map<RemoteContextListenerInterface, RemoteContextListenerClient> listeners = Collections
            .synchronizedMap(new HashMap<RemoteContextListenerInterface, RemoteContextListenerClient>());

    /**
     * constructs a new RemoteContext.
     * @param eventContext the underlying context
     * @throws RemoteException on network failure
     */
    public RemoteContext(final EventContext eventContext) throws RemoteException
    {
        super();
        this.eventContext = eventContext;
    }

    /** {@inheritDoc} */
    public void addNamingListener(Name target, int scope, RemoteContextListenerInterface l) throws NamingException
    {
        RemoteContextListenerClient client = new RemoteContextListenerClient(l);
        this.listeners.put(l, client);
        this.eventContext.addNamingListener(target, scope, client);
    }

    /** {@inheritDoc} */
    public void addNamingListener(String target, int scope, RemoteContextListenerInterface l) throws NamingException
    {
        RemoteContextListenerClient client = new RemoteContextListenerClient(l);
        this.listeners.put(l, client);
        this.eventContext.addNamingListener(target, scope, client);
    }

    /** {@inheritDoc} */
    public void removeNamingListener(RemoteContextListenerInterface l) throws NamingException
    {
        this.eventContext.removeNamingListener(this.listeners.remove(l));
    }

    /** {@inheritDoc} */
    public boolean targetMustExist() throws NamingException
    {
        return this.eventContext.targetMustExist();
    }

    /** {@inheritDoc} */
    public Object lookup(Name name) throws NamingException
    {
        return this.eventContext.lookup(name);
    }

    /** {@inheritDoc} */
    public Object lookup(String name) throws NamingException
    {
        return this.eventContext.lookup(name);
    }

    /** {@inheritDoc} */
    public void bind(Name name, Object obj) throws NamingException
    {
        this.eventContext.bind(name, obj);
    }

    /** {@inheritDoc} */
    public void bind(String name, Object obj) throws NamingException
    {
        this.eventContext.bind(name, obj);
    }

    /** {@inheritDoc} */
    public void rebind(Name name, Object obj) throws NamingException
    {
        this.eventContext.rebind(name, obj);
    }

    /** {@inheritDoc} */
    public void rebind(String name, Object obj) throws NamingException
    {
        this.eventContext.rebind(name, obj);
    }

    /** {@inheritDoc} */
    public void unbind(Name name) throws NamingException
    {
        this.eventContext.unbind(name);
    }

    /** {@inheritDoc} */
    public void unbind(String name) throws NamingException
    {
        this.eventContext.unbind(name);
    }

    /** {@inheritDoc} */
    public void rename(Name oldName, Name newName) throws NamingException
    {
        this.eventContext.rename(oldName, newName);
    }

    /** {@inheritDoc} */
    public void rename(String oldName, String newName) throws NamingException
    {
        this.eventContext.rename(oldName, newName);
    }

    /** {@inheritDoc} */
    public NamingEnumeration<NameClassPair> list(Name name) throws NamingException
    {
        return this.eventContext.list(name);
    }

    /** {@inheritDoc} */
    public NamingEnumeration<NameClassPair> list(String name) throws NamingException
    {
        return this.eventContext.list(name);
    }

    /** {@inheritDoc} */
    public NamingEnumeration<Binding> listBindings(Name name) throws NamingException
    {
        return this.eventContext.listBindings(name);
    }

    /** {@inheritDoc} */
    public NamingEnumeration<Binding> listBindings(String name) throws NamingException
    {
        return this.eventContext.listBindings(name);
    }

    /** {@inheritDoc} */
    public void destroySubcontext(Name name) throws NamingException
    {
        this.eventContext.destroySubcontext(name);
    }

    /** {@inheritDoc} */
    public void destroySubcontext(String name) throws NamingException
    {
        this.eventContext.destroySubcontext(name);
    }

    /** {@inheritDoc} */
    public RemoteContextInterface createSubcontext(Name name) throws NamingException, RemoteException
    {
        EventContext child = (EventContext) this.eventContext.createSubcontext(name);
        return new RemoteContext(child);
    }

    /** {@inheritDoc} */
    public RemoteContextInterface createSubcontext(String name) throws NamingException, RemoteException
    {
        EventContext child = (EventContext) this.eventContext.createSubcontext(name);
        return new RemoteContext(child);
    }

    /** {@inheritDoc} */
    public Object lookupLink(Name name) throws NamingException
    {
        return this.eventContext.lookupLink(name);
    }

    /** {@inheritDoc} */
    public Object lookupLink(String name) throws NamingException
    {
        return this.eventContext.lookupLink(name);
    }

    /** {@inheritDoc} */
    public NameParser getNameParser(Name name) throws NamingException
    {
        return this.eventContext.getNameParser(name);
    }

    /** {@inheritDoc} */
    public NameParser getNameParser(String name) throws NamingException
    {
        return this.eventContext.getNameParser(name);
    }

    /** {@inheritDoc} */
    public Name composeName(Name name, Name prefix) throws NamingException
    {
        return this.eventContext.composeName(name, prefix);
    }

    /** {@inheritDoc} */
    public String composeName(String name, String prefix) throws NamingException
    {
        return this.eventContext.composeName(name, prefix);
    }

    /** {@inheritDoc} */
    public Object addToEnvironment(String propName, Object propVal) throws NamingException
    {
        return this.eventContext.addToEnvironment(propName, propVal);
    }

    /** {@inheritDoc} */
    public Object removeFromEnvironment(String propName) throws NamingException
    {
        return this.eventContext.removeFromEnvironment(propName);
    }

    /** {@inheritDoc} */
    public Hashtable<?, ?> getEnvironment() throws NamingException
    {
        return this.eventContext.getEnvironment();
    }

    /** {@inheritDoc} */
    public void close() throws NamingException
    {
        this.eventContext.close();
    }

    /** {@inheritDoc} */
    public String getNameInNamespace() throws NamingException
    {
        return this.eventContext.getNameInNamespace();
    }
}
