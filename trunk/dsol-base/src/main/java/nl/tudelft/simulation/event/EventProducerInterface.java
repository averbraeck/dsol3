package nl.tudelft.simulation.event;

import java.rmi.RemoteException;

/**
 * The EventProducerInterface defines the registration operations of an event producer. This behavior includes adding
 * and removing listeners for a specific event type.
 * <p>
 * (c) copyright 2002-2014 <a href="http://www.simulation.tudelft.nl">Delft University of Technology</a>. <br>
 * BSD-style license. See <a href="http://www.simulation.tudelft.nl/dsol/3.0/license.html">DSOL License</a>. <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs</a>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @since 1.5
 */
public interface EventProducerInterface
{
    /** The FIRST_POSITION in the queue. */
    short FIRST_POSITION = 0;

    /** The LAST_POSITION in the queue. */
    short LAST_POSITION = -1;

    /**
     * adds a listener as strong reference to the BEGINNING of a queue of listeners.
     * @param listener the listener which is interested at events of eventtype.
     * @param eventType the events of interest.
     * @return the success of adding the listener. If a listener was already added false is returned.
     * @throws RemoteException If a network connection failure occurs.
     * @see nl.tudelft.simulation.event.ref.WeakReference
     */
    boolean addListener(EventListenerInterface listener, EventType eventType) throws RemoteException;

    /**
     * adds a listener to the BEGINNING of a queue of listeners.
     * @param listener the listener which is interested at events of eventtype.
     * @param eventType the events of interest.
     * @param weak whether or not the listener is added as weak reference.
     * @return the success of adding the listener. If a listener was already added false is returned.
     * @throws RemoteException If a network connection failure occurs.
     * @see nl.tudelft.simulation.event.ref.WeakReference
     */
    boolean addListener(EventListenerInterface listener, EventType eventType, boolean weak) throws RemoteException;

    /**
     * adds a listener as strong reference to the specified position of a queue of listeners.
     * @param listener the listener which is interested at events of eventtype.
     * @param eventType the events of interest.
     * @param position the position of the listener in the queue.
     * @return the success of adding the listener. If a listener was already added, or an illegal position is provided
     *         false is returned.
     * @throws RemoteException If a network connection failure occurs.
     * @see nl.tudelft.simulation.event.ref.WeakReference
     */
    boolean addListener(EventListenerInterface listener, EventType eventType, short position) throws RemoteException;

    /**
     * adds a listener to the specified position of a queue of listeners.
     * @param listener which is interested at certain events,
     * @param eventType the events of interest.
     * @param position the position of the listener in the queue
     * @param weak whether the reference should be weak or strong.
     * @return the success of adding the listener. If a listener was already added or an illegal position is provided
     *         false is returned.
     * @throws RemoteException If a network connection failure occurs.
     */
    boolean addListener(EventListenerInterface listener, EventType eventType, short position, boolean weak)
            throws RemoteException;

    /**
     * removes the subscription of a listener for a specific event.
     * @param listener which is no longer interested.
     * @param eventType the event which is of no interest any more.
     * @return the success of removing the listener. If a listener was not subscribed false is returned.
     * @throws RemoteException If a network connection failure occurs.
     */
    boolean removeListener(EventListenerInterface listener, EventType eventType) throws RemoteException;
}
