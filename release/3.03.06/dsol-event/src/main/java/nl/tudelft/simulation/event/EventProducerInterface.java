package nl.tudelft.simulation.event;

import java.rmi.RemoteException;

/**
 * The EventProducerInterface defines the registration operations of an event producer. This behavior includes adding
 * and removing listeners for a specific event type.
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
public interface EventProducerInterface
{
    /** The FIRST_POSITION in the queue. */
    short FIRST_POSITION = 0;

    /** The LAST_POSITION in the queue. */
    short LAST_POSITION = -1;

    /**
     * adds a listener as strong reference to the BEGINNING of a queue of listeners.
     * @param listener EventListenerInterface; the listener which is interested at events of eventtype.
     * @param eventType EventType; the events of interest.
     * @return the success of adding the listener. If a listener was already added false is returned.
     * @throws RemoteException If a network connection failure occurs.
     * @see nl.tudelft.simulation.event.ref.WeakReference
     */
    boolean addListener(EventListenerInterface listener, EventType eventType) throws RemoteException;

    /**
     * adds a listener to the BEGINNING of a queue of listeners.
     * @param listener EventListenerInterface; the listener which is interested at events of eventtype.
     * @param eventType EventType; the events of interest.
     * @param weak boolean; whether or not the listener is added as weak reference.
     * @return the success of adding the listener. If a listener was already added false is returned.
     * @throws RemoteException If a network connection failure occurs.
     * @see nl.tudelft.simulation.event.ref.WeakReference
     */
    boolean addListener(EventListenerInterface listener, EventType eventType, boolean weak) throws RemoteException;

    /**
     * adds a listener as strong reference to the specified position of a queue of listeners.
     * @param listener EventListenerInterface; the listener which is interested at events of eventtype.
     * @param eventType EventType; the events of interest.
     * @param position short; the position of the listener in the queue.
     * @return the success of adding the listener. If a listener was already added, or an illegal position is provided
     *         false is returned.
     * @throws RemoteException If a network connection failure occurs.
     * @see nl.tudelft.simulation.event.ref.WeakReference
     */
    boolean addListener(EventListenerInterface listener, EventType eventType, short position) throws RemoteException;

    /**
     * adds a listener to the specified position of a queue of listeners.
     * @param listener EventListenerInterface; which is interested at certain events,
     * @param eventType EventType; the events of interest.
     * @param position short; the position of the listener in the queue
     * @param weak boolean; whether the reference should be weak or strong.
     * @return the success of adding the listener. If a listener was already added or an illegal position is provided
     *         false is returned.
     * @throws RemoteException If a network connection failure occurs.
     */
    boolean addListener(EventListenerInterface listener, EventType eventType, short position, boolean weak)
            throws RemoteException;

    /**
     * removes the subscription of a listener for a specific event.
     * @param listener EventListenerInterface; which is no longer interested.
     * @param eventType EventType; the event which is of no interest any more.
     * @return the success of removing the listener. If a listener was not subscribed false is returned.
     * @throws RemoteException If a network connection failure occurs.
     */
    boolean removeListener(EventListenerInterface listener, EventType eventType) throws RemoteException;
}
