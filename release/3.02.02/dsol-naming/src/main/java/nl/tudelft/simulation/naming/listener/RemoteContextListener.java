package nl.tudelft.simulation.naming.listener;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import javax.naming.event.NamingEvent;
import javax.naming.event.NamingExceptionEvent;

/**
 * A RemoteContextListener.
 * <p>
 * (c) 2002-2018 <a href="https://simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="https://simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @version 1.2 Apr 14, 2004
 * @since 1.5
 */
public class RemoteContextListener extends UnicastRemoteObject implements RemoteContextListenerInterface
{
    /** The default serial version UID for serializable classes. */
    private static final long serialVersionUID = 1L;

    /** the listener. */
    private ContextListenerInterface listener = null;

    /**
     * constructs a new RemoteContextListener
     * @param listener the target.
     * @throws RemoteException on network failure.
     */
    public RemoteContextListener(final ContextListenerInterface listener) throws RemoteException
    {
        super();
        this.listener = listener;
    }

    /** {@inheritDoc} */
    @Override
    public void objectChanged(NamingEvent evt)
    {
        this.listener.objectChanged(evt);
    }

    /** {@inheritDoc} */
    @Override
    public void objectAdded(NamingEvent evt)
    {
        this.listener.objectAdded(evt);
    }

    /** {@inheritDoc} */
    @Override
    public void objectRemoved(NamingEvent evt)
    {
        this.listener.objectRemoved(evt);
    }

    /** {@inheritDoc} */
    @Override
    public void objectRenamed(NamingEvent evt)
    {
        this.listener.objectRemoved(evt);
    }

    /** {@inheritDoc} */
    @Override
    public void namingExceptionThrown(NamingExceptionEvent evt)
    {
        this.listener.namingExceptionThrown(evt);
    }
}
