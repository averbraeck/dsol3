package nl.tudelft.simulation.event;

import java.rmi.RemoteException;
import java.util.EventListener;

/**
 * The EventListenerInterface creates a callback method for publishers to inform their clients.
 * <p>
 * Copyright (c) 2002-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. See for project information <a href="https://simulation.tudelft.nl/" target="_blank">
 * https://simulation.tudelft.nl</a>. The DSOL project is distributed under a three-clause BSD-style license, which can
 * be found at <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public interface EventListenerInterface extends EventListener
{
    /**
     * notifies the event listener of an event. This operation forms the callback method of the asynchronous
     * communication protocol expressed in the event package.
     * @param event EventInterface; the event which is sent to the listener.
     * @throws RemoteException If a network connection failure occurs.
     */
    void notify(EventInterface event) throws RemoteException;
}
