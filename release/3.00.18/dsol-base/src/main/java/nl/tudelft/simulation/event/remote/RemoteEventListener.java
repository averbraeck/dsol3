package nl.tudelft.simulation.event.remote;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import nl.tudelft.simulation.event.EventInterface;
import nl.tudelft.simulation.event.EventListenerInterface;

/**
 * The RemoteEventListener class embodies a remoteEventListener.
 * <p>
 * (c) copyright 2002-2014 <a href="http://www.simulation.tudelft.nl">Delft University of Technology</a>. <br>
 * BSD-style license. See <a href="http://www.simulation.tudelft.nl/dsol/3.0/license.html">DSOL License</a>. <br>
 * @author <a href="https://www.linkedin.com/in/stijnpietervanhouten">Stijn-Pieter van Houten</a>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class RemoteEventListener implements RemoteEventListenerInterface
{
    /** the owner of the remote listener. */
    private EventListenerInterface owner = null;

    /**
     * Constructs a new RemoteListener.
     * @param owner The owner of the listener.
     * @throws RemoteException in case of network error
     */
    public RemoteEventListener(final EventListenerInterface owner) throws RemoteException
    {
        super();
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
