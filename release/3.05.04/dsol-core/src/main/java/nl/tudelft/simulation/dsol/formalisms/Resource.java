package nl.tudelft.simulation.dsol.formalisms;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import org.djutils.event.EventProducer;
import org.djutils.event.TimedEventType;
import org.djutils.metadata.MetaData;
import org.djutils.metadata.ObjectDescriptor;

import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.simtime.SimTime;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;

/**
 * A resource defines a shared and limited amount.
 * <p>
 * Copyright (c) 2002-2021 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @param <A> the absolute time type to use in timed events
 * @param <R> the relative time type
 * @param <T> the simulation time type to use.
 * @since 1.5
 */
public class Resource<A extends Comparable<A> & Serializable, R extends Number & Comparable<R>, T extends SimTime<A, R, T>>
        extends EventProducer
{
    /** */
    private static final long serialVersionUID = 20140805L;

    /** the counter counting the requests. package visibility, so Request can access it. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    static long counter = 0;

    /** UTILIZATION_EVENT is fired on activity. */
    public static final TimedEventType UTILIZATION_EVENT = new TimedEventType(new MetaData("UTILIZATION_EVENT",
            "Utilization changed", new ObjectDescriptor("newUtilization", "new utilization", Double.class)));

    /** RESOURCE_REQUESTED_QUEUE_LENGTH fired on changes in queue length. */
    public static final TimedEventType RESOURCE_REQUESTED_QUEUE_LENGTH =
            new TimedEventType(new MetaData("RESOURCE_REQUESTED_QUEUE_LENGTH", "Queue length changed",
                    new ObjectDescriptor("newQueueLength", "new queue length", Integer.class)));

    /** the minimum priority. */
    public static final int MIN_REQUEST_PRIORITY = 0;

    /** the maximum priority. */
    public static final int MAX_REQUEST_PRIORITY = 10;

    /** the default average priority. */
    public static final int DEFAULT_REQUEST_PRIORITY = 5;

    /** capacity defines the maximum capacity of the resource. */
    private double capacity;

    /** claimedCapacity defines the currently claimed capacity. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected double claimedCapacity = 0.0;

    /** request defines the list of requestors for this resource. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected SortedSet<Request<A, R, T>> requests =
            Collections.synchronizedSortedSet(new TreeSet<Request<A, R, T>>(new RequestComparator()));

    /** simulator defines the simulator on which is scheduled. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected DEVSSimulatorInterface<A, R, T> simulator;

    /** the description of the resource. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected String description = "resource";

    /**
     * Method Resource.
     * @param simulator DEVSSimulatorInterface&lt;A,R,T&gt;; on which is scheduled
     * @param description String; the description of this resource
     * @param capacity double; of the resource
     */
    public Resource(final DEVSSimulatorInterface<A, R, T> simulator, final String description, final double capacity)
    {
        super();
        this.description = description;
        this.simulator = simulator;
        this.capacity = capacity;
    }

    /** {@inheritDoc} */
    @Override
    public Serializable getSourceId()
    {
        return this.description;
    }

    /**
     * Method Resource.
     * @param simulator DEVSSimulatorInterface&lt;A,R,T&gt;; on which is scheduled
     * @param capacity double; of the resource
     */
    public Resource(final DEVSSimulatorInterface<A, R, T> simulator, final double capacity)
    {
        this(simulator, "resource", capacity);
    }

    /**
     * returns the maximum, and thus original capacity of the resource.
     * @return capacity the maximum, and thus original capacity of the resource.
     */
    public final double getCapacity()
    {
        return this.capacity;
    }

    /**
     * returns the amount of currently claimed capacity.
     * @return the amount of currently claimed capacity.
     */
    public final double getClaimedCapacity()
    {
        return this.claimedCapacity;
    }

    /**
     * returns the currently available capacity on this resource. This method is implemented as
     * <code>return this.getCapacity()-this.getClaimedCapacity()</code>
     * @return the currently available capacity on this resource.
     */
    public final double getAvailableCapacity()
    {
        return this.capacity - this.claimedCapacity;
    }

    /**
     * returns the number of instances currently waiting for this resource.
     * @return the number of instances currently waiting for this resource
     */
    public final int getQueueLength()
    {
        return this.requests.size();
    }

    /**
     * Method alterClaimedCapacity.
     * @param amount double; refers the amount which is added to the claimed capacity
     * @throws RemoteException on network failure
     */
    private synchronized void alterClaimedCapacity(final double amount) throws RemoteException
    {
        this.claimedCapacity += amount;
        this.fireTimedEvent(Resource.UTILIZATION_EVENT, this.claimedCapacity, this.simulator.getSimulatorTime());
    }

    /**
     * sets the capacity of the resource.
     * @param capacity double; the new maximal capacity
     */
    public final void setCapacity(final double capacity)
    {
        this.capacity = capacity;
        try
        {
            this.releaseCapacity(0.0);
        }
        catch (RemoteException remoteException)
        {
            // This exception cannot occur.
            this.simulator.getLogger().always().warn(remoteException, "setCapacity");
        }
    }

    /**
     * requests an amount of capacity from the resource.
     * @param amount double; the requested amount
     * @param requestor ResourceRequestorInterface&lt;A,R,T&gt;; the RequestorInterface requesting the amount
     * @throws RemoteException on network failure
     * @throws SimRuntimeException on other failures
     */
    public final synchronized void requestCapacity(final double amount, final ResourceRequestorInterface<A, R, T> requestor)
            throws RemoteException, SimRuntimeException
    {
        this.requestCapacity(amount, requestor, Resource.DEFAULT_REQUEST_PRIORITY);
    }

    /**
     * requests an amount of capacity from the resource.
     * @param amount double; the requested amount
     * @param requestor ResourceRequestorInterface&lt;A,R,T&gt;; the RequestorInterface requesting the amount
     * @param priority int; the priority of the request
     * @throws RemoteException on network failure
     * @throws SimRuntimeException on other failures
     */
    public final synchronized void requestCapacity(final double amount, final ResourceRequestorInterface<A, R, T> requestor,
            final int priority) throws RemoteException, SimRuntimeException
    {
        if (amount < 0.0)
        {
            throw new SimRuntimeException("requested capacity on resource cannot <0.0");
        }
        if ((this.claimedCapacity + amount) <= this.capacity)
        {
            this.alterClaimedCapacity(amount);
            this.simulator.scheduleEventNow(this, requestor, "receiveRequestedResource",
                    new Object[] {Double.valueOf(amount), this});
        }
        else
        {
            synchronized (this.requests)
            {
                this.requests.add(new Request<A, R, T>(requestor, amount, priority));
            }
            this.fireTimedEvent(Resource.RESOURCE_REQUESTED_QUEUE_LENGTH, this.requests.size(),
                    this.simulator.getSimulatorTime());
        }
    }

    /**
     * releases an amount of capacity from the resource.
     * @param amount double; the amount to release
     * @throws RemoteException on network failure
     */
    public final void releaseCapacity(final double amount) throws RemoteException
    {
        if (amount < 0.0)
        {
            throw new IllegalArgumentException("released capacity on resource cannot <0.0");
        }
        if (amount > 0.0)
        {
            this.alterClaimedCapacity(-Math.min(this.capacity, amount));
        }
        synchronized (this.requests)
        {
            for (Iterator<Request<A, R, T>> i = this.requests.iterator(); i.hasNext();)
            {
                Request<A, R, T> request = i.next();
                if ((this.capacity - this.claimedCapacity) >= request.getAmount())
                {
                    this.alterClaimedCapacity(request.getAmount());
                    request.getRequestor().receiveRequestedResource(request.getAmount(), this);
                    synchronized (this.requests)
                    {
                        i.remove();
                    }
                    this.fireTimedEvent(Resource.RESOURCE_REQUESTED_QUEUE_LENGTH, this.requests.size(),
                            this.simulator.getSimulatorTime());
                }
                else
                {
                    return;
                }
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return this.description;
    }

    /**
     * the RequestComparator. This comparator first checks on priority, then on ID.
     */
    protected class RequestComparator implements Comparator<Request<A, R, T>>
    {
        /** {@inheritDoc} */
        @Override
        public final int compare(final Request<A, R, T> arg0, final Request<A, R, T> arg1)
        {
            if (arg0.getPriority() > arg1.getPriority())
            {
                return -1;
            }
            if (arg0.getPriority() < arg1.getPriority())
            {
                return 1;
            }
            if (arg0.getId() < arg1.getId())
            {
                return -1;
            }
            if (arg0.getId() > arg1.getId())
            {
                return 1;
            }
            return 0;
        }
    }

    /**
     * A Request.
     * @param <A> the absolute time type to use in timed events
     * @param <R> the relative time type
     * @param <T> the simulation time type to use.
     */
    public static class Request<A extends Comparable<A> & Serializable, R extends Number & Comparable<R>,
            T extends SimTime<A, R, T>>
    {
        /** the priority of the request. */
        private int priority = 5;

        /** the number of this request. */
        private long id = -1;

        /** requestor the resourceRequestor. */
        private ResourceRequestorInterface<A, R, T> requestor;

        /** amount is the amount requested by the resource. */
        private double amount;

        /**
         * constructs a new Request.
         * @param requestor ResourceRequestorInterface&lt;A,R,T&gt;; the requestor
         * @param amount double; the requested amount
         * @param priority int; the priority of the request
         */
        public Request(final ResourceRequestorInterface<A, R, T> requestor, final double amount, final int priority)
        {
            this.requestor = requestor;
            this.amount = amount;
            this.priority = priority;
            Resource.counter++;
            this.id = Resource.counter;
        }

        /**
         * gets the requested amount.
         * @return the requested amount
         */
        public final double getAmount()
        {
            return this.amount;
        }

        /**
         * gets the requestor.
         * @return the Requestor.
         */
        public final ResourceRequestorInterface<A, R, T> getRequestor()
        {
            return this.requestor;
        }

        /**
         * returns the priority of the request.
         * @return the priority
         */
        public final int getPriority()
        {
            return this.priority;
        }

        /**
         * returns the id of the request.
         * @return the id
         */
        public final long getId()
        {
            return this.id;
        }

        /** {@inheritDoc} */
        @Override
        public final String toString()
        {
            return "RequestForResource[requestor=" + this.requestor + ";amount=" + this.amount + ";priority=" + this.priority
                    + "]";
        }
    }

}
