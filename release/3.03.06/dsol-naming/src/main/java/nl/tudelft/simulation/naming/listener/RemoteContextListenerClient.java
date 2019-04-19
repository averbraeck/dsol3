package nl.tudelft.simulation.naming.listener;

import java.rmi.RemoteException;

import javax.naming.event.NamingEvent;
import javax.naming.event.NamingExceptionEvent;

import org.djutils.logger.CategoryLogger;

/**
 * The local wrapper for RemoteContextListeners.
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
public class RemoteContextListenerClient implements ContextListenerInterface
{
    /** the target to use. */
    private RemoteContextListenerInterface target = null;

    /**
     * constructs a new RemoteContextListenerClient
     * @param target RemoteContextListenerInterface; the remote target on which method invocation must be tunneled.
     */
    public RemoteContextListenerClient(final RemoteContextListenerInterface target)
    {
        super();
        this.target = target;
    }

    /** {@inheritDoc} */
    @Override
    public void namingExceptionThrown(NamingExceptionEvent evt)
    {
        try
        {
            this.target.namingExceptionThrown(evt);
        }
        catch (RemoteException remoteException)
        {
            CategoryLogger.always().warn(remoteException, "objectChanged");
        }
    }

    /** {@inheritDoc} */
    @Override
    public void objectAdded(NamingEvent evt)
    {
        try
        {
            this.target.objectAdded(evt);
        }
        catch (RemoteException remoteException)
        {
            CategoryLogger.always().warn(remoteException, "objectAdded");
        }
    }

    /** {@inheritDoc} */
    @Override
    public void objectRemoved(NamingEvent evt)
    {
        try
        {
            this.target.objectRemoved(evt);
        }
        catch (RemoteException remoteException)
        {
            CategoryLogger.always().warn(remoteException, "objectRemoved");
        }
    }

    /** {@inheritDoc} */
    @Override
    public void objectRenamed(NamingEvent evt)
    {
        try
        {
            this.target.objectRenamed(evt);
        }
        catch (RemoteException remoteException)
        {
            CategoryLogger.always().warn(remoteException, "objectRenamed");
        }
    }

    /** {@inheritDoc} */
    @Override
    public void objectChanged(NamingEvent evt)
    {
        try
        {
            this.target.objectChanged(evt);
        }
        catch (RemoteException remoteException)
        {
            CategoryLogger.always().warn(remoteException, "objectChanged");
        }
    }
}
