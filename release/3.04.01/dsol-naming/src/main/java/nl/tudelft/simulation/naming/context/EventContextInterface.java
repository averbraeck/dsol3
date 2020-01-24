package nl.tudelft.simulation.naming.context;

import java.rmi.RemoteException;

import org.djutils.event.EventListenerInterface;
import org.djutils.event.ref.ReferenceType;

/**
 * EventContextInterface.java.
 * <p>
 * Copyright (c) 2020-2020 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank">Alexander Verbraeck</a>
 */
public interface EventContextInterface extends ContextInterface
{
    /**
     * adds a listener as strong reference to the BEGINNING of a queue of listeners.
     * @param listener EventListenerInterface; the listener which is interested at events of eventType.
     * @param contextScope ContextScope; the part of the tree that the listener is aimed at (current node, current node and
     *            keys, subtree).
     * @return the success of adding the listener. If a listener was already added false is returned.
     * @throws RemoteException if a network connection failure occurs.
     * @see org.djutils.event.ref.WeakReference
     */
    boolean addListener(EventListenerInterface listener, ContextScope contextScope) throws RemoteException;

    /**
     * adds a listener to the BEGINNING of a queue of listeners.
     * @param listener EventListenerInterface; the listener which is interested at events of eventType.
     * @param contextScope ContextScope; the part of the tree that the listener is aimed at (current node, current node and
     *            keys, subtree).
     * @param referenceType ReferenceType; whether the listener is added as a strong or as a weak reference.
     * @return the success of adding the listener. If a listener was already added false is returned.
     * @throws RemoteException If a network connection failure occurs.
     * @see org.djutils.event.ref.WeakReference
     */
    boolean addListener(EventListenerInterface listener, ContextScope contextScope, ReferenceType referenceType)
            throws RemoteException;

    /**
     * adds a listener as strong reference to the specified position of a queue of listeners.
     * @param listener EventListenerInterface; the listener which is interested at events of eventType.
     * @param contextScope ContextScope; the part of the tree that the listener is aimed at (current node, current node and
     *            keys, subtree).
     * @param position int; the position of the listener in the queue.
     * @return the success of adding the listener. If a listener was already added, or an illegal position is provided false is
     *         returned.
     * @throws RemoteException If a network connection failure occurs.
     * @see org.djutils.event.ref.WeakReference
     */
    boolean addListener(EventListenerInterface listener, ContextScope contextScope, int position) throws RemoteException;

    /**
     * adds a listener to the specified position of a queue of listeners.
     * @param listener EventListenerInterface; which is interested at certain events,
     * @param contextScope ContextScope; the part of the tree that the listener is aimed at (current node, current node and
     *            keys, subtree).
     * @param position int; the position of the listener in the queue
     * @param referenceType ReferenceType; whether the listener is added as a strong or as a weak reference.
     * @return the success of adding the listener. If a listener was already added or an illegal position is provided false is
     *         returned.
     * @throws RemoteException If a network connection failure occurs.
     */
    boolean addListener(EventListenerInterface listener, ContextScope contextScope, int position, ReferenceType referenceType)
            throws RemoteException;

    /**
     * removes the subscription of a listener for a specific event.
     * @param listener EventListenerInterface; which is no longer interested.
     * @param contextScope ContextScope;the scope which is of no interest any more.
     * @return the success of removing the listener. If a listener was not subscribed false is returned.
     * @throws RemoteException If a network connection failure occurs.
     */
    boolean removeListener(EventListenerInterface listener, ContextScope contextScope) throws RemoteException;

}
