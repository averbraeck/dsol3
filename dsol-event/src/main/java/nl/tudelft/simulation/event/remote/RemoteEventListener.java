package nl.tudelft.simulation.event.remote;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import nl.tudelft.simulation.event.EventInterface;
import nl.tudelft.simulation.event.EventListenerInterface;

/**
 * The RemoteEventListener class embodies a remote EventListener.
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
public class RemoteEventListener implements RemoteEventListenerInterface
{
    /** the owner of the remote listener. */
    private EventListenerInterface owner = null;

    /**
     * Constructs a new RemoteListener.
     * @param owner EventListenerInterface; The owner of the listener.
     * @throws RemoteException in case of network error
     */
    public RemoteEventListener(final EventListenerInterface owner) throws RemoteException
    {
        super();
        // TODO: UnicastRemoteObject.exportObject(this) is deprecated.
        UnicastRemoteObject.exportObject(this);
        this.owner = owner;
    }

    /** {@inheritDoc} */
    @Override
    public void notify(final EventInterface event) throws RemoteException
    {
        this.owner.notify(event);
    }
}
