package nl.tudelft.simulation.event;

import java.rmi.RemoteException;
import java.util.EventListener;

/**
 * The EventListenerInterface creates a callback method for publishers to inform their clients.
 * <p>
 * copyright (c) 2002-2018  <a href="https://simulation.tudelft.nl">Delft University of Technology</a>. <br>
 * BSD-style license. See <a href="https://simulation.tudelft.nl/dsol/3.0/license.html">DSOL License</a>. <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs</a>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @since 1.1
 */
public interface EventListenerInterface extends EventListener
{
    /**
     * notifies the event listener of an event. This operation forms the callback method of the asynchronous
     * communication protocol expressed in the event package.
     * @param event the event which is sent to the listener.
     * @throws RemoteException If a network connection failure occurs.
     */
    void notify(EventInterface event) throws RemoteException;
}
