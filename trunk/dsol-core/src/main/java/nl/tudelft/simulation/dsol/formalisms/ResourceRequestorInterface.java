package nl.tudelft.simulation.dsol.formalisms;

import java.io.Serializable;
import java.rmi.RemoteException;

import nl.tudelft.simulation.dsol.simtime.SimTime;

/**
 * This interface provides a callback method to the resource. Whenever resource is available this method is invoked on the
 * requestor.
 * <p>
 * Copyright (c) 2002-2022 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @param <A> the absolute time type to use in timed events
 * @param <R> the relative time type
 * @param <T> the simulation time type.
 * @since 1.5
 */
public interface ResourceRequestorInterface<A extends Comparable<A> & Serializable, R extends Number & Comparable<R>,
        T extends SimTime<A, R, T>>
{
    /**
     * receive the requested resource.
     * @param requestedCapacity double; reflects the amount requested.
     * @param resource Resource&lt;A,R,T&gt;; the requested resource.
     * @throws RemoteException on network failure.
     */
    void receiveRequestedResource(double requestedCapacity, Resource<A, R, T> resource) throws RemoteException;
}
