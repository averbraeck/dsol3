package nl.tudelft.simulation.naming.listener;

import java.rmi.RemoteException;

import javax.naming.event.NamingEvent;
import javax.naming.event.NamingExceptionEvent;

import nl.tudelft.simulation.logger.Logger;

/**
 * The local wrapper for remoteContextListeners.
 * <p>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @version 1.2 Apr 14, 2004
 * @since 1.5
 */
public class RemoteContextListenerClient implements ContextListenerInterface
{
    /** the target to use. */
    private RemoteContextListenerInterface target = null;

    /**
     * constructs a new RemoteContextListenerClient
     * @param target the remote target on which method invocation must be tunneled.
     */
    public RemoteContextListenerClient(final RemoteContextListenerInterface target)
    {
        super();
        this.target = target;
    }

    /** {@inheritDoc} */
    public void namingExceptionThrown(NamingExceptionEvent evt)
    {
        try
        {
            this.target.namingExceptionThrown(evt);
        }
        catch (RemoteException remoteException)
        {
            Logger.warning(this, "objectChanged", remoteException);
        }
    }

    /** {@inheritDoc} */
    public void objectAdded(NamingEvent evt)
    {
        try
        {
            this.target.objectAdded(evt);
        }
        catch (RemoteException remoteException)
        {
            Logger.warning(this, "objectAdded", remoteException);
        }
    }

    /** {@inheritDoc} */
    public void objectRemoved(NamingEvent evt)
    {
        try
        {
            this.target.objectRemoved(evt);
        }
        catch (RemoteException remoteException)
        {
            Logger.warning(this, "objectRemoved", remoteException);
        }
    }

    /** {@inheritDoc} */
    public void objectRenamed(NamingEvent evt)
    {
        try
        {
            this.target.objectRenamed(evt);
        }
        catch (RemoteException remoteException)
        {
            Logger.warning(this, "objectRenamed", remoteException);
        }
    }

    /** {@inheritDoc} */
    public void objectChanged(NamingEvent evt)
    {
        try
        {
            this.target.objectChanged(evt);
        }
        catch (RemoteException remoteException)
        {
            Logger.warning(this, "objectChanged", remoteException);
        }
    }
}
