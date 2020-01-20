package nl.tudelft.simulation.naming.listener;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import javax.naming.event.NamingEvent;
import javax.naming.event.NamingExceptionEvent;

/**
 * A RemoteContextListener.
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
public class RemoteContextListener extends UnicastRemoteObject implements RemoteContextListenerInterface
{
    /** The default serial version UID for serializable classes. */
    private static final long serialVersionUID = 1L;

    /** the listener. */
    private ContextListenerInterface listener = null;

    /**
     * constructs a new RemoteContextListener
     * @param listener ContextListenerInterface; the target.
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
