package nl.tudelft.simulation.dsol.formalisms;

import java.rmi.RemoteException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;
import nl.tudelft.simulation.event.EventProducer;
import nl.tudelft.simulation.event.EventType;
import nl.tudelft.simulation.logger.Logger;

/**
 * A resource defines a shared and limited amount.
 * <p>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl"> www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:36:45 $
 * @since 1.5
 */
public class Resource extends EventProducer
{
    /** */
    private static final long serialVersionUID = 20140805L;

    /** the counter counting the requests. */
    protected static long counter = 0;

    /** UTILIZATION_EVENT is fired on activity */
    public static final EventType UTILIZATION_EVENT = new EventType("UTILIZATION_EVENT");

    /** RESOURCE_REQUESTED_QUEUE_LENGTH fired on changes in queue length. */
    public static final EventType RESOURCE_REQUESTED_QUEUE_LENGTH = new EventType("RESOURCE_REQUESTED_QUEUE_LENGTH");

    /** the minimum priority */
    public static final int MIN_REQUEST_PRIORITY = 0;

    /** the maximum priority */
    public static final int MAX_REQUEST_PRIORITY = 10;

    /** the default average priority */
    public static final int DEFAULT_REQUEST_PRIORITY = 5;

    /** capacity defines the maximuum capacity of the resource. */
    protected double capacity;

    /** claimedCapacity defines the currently claimed capacity */
    protected double claimedCapacity = 0.0;

    /** request defines the list of requestors for this resource. */
    protected SortedSet<Request> requests = Collections.synchronizedSortedSet(new TreeSet<Request>(
            new RequestComparator()));

    /** simulator defines the simulator on which is scheduled. */
    protected DEVSSimulatorInterface<?, ?, ?> simulator;

    /** the description of the resource. */
    protected String description = "resource";

    /**
     * Method Resource.
     * @param simulator on which is scheduled
     * @param description the description of this resource
     * @param capacity of the resource
     */
    public Resource(final DEVSSimulatorInterface<?, ?, ?> simulator, final String description, final double capacity)
    {
        super();
        this.description = description;
        this.simulator = simulator;
        this.capacity = capacity;
    }

    /**
     * Method Resource.
     * @param simulator on which is scheduled
     * @param capacity of the resource
     */
    public Resource(final DEVSSimulatorInterface<?, ?, ?> simulator, final double capacity)
    {
        this(simulator, "resource", capacity);
    }

    /**
     * returns the maximum, and thus original capacity of the resource.
     * @return capacity the maximum, and thus original capacity of the resource.
     */
    public double getCapacity()
    {
        return this.capacity;
    }

    /**
     * returns the amount of currently claimed capacity.
     * @return the amount of currently claimed capacity.
     */
    public double getClaimedCapacity()
    {
        return this.claimedCapacity;
    }

    /**
     * returns the currently available capacity on this resource. This method is implemented as
     * <code>return this.getCapacity()-this.getClaimedCapacity()</code>
     * @return the currently available capacity on this resource.
     */
    public double getAvailableCapacity()
    {
        return this.capacity - this.claimedCapacity;
    }

    /**
     * returns the number of instances currently waiting for this resource
     * @return the number of instances currently waiting for this resource
     */
    public int getQueueLength()
    {
        return this.requests.size();
    }

    /**
     * Method alterClaimedCapacity.
     * @param amount refers the amount which is added to the claimed capacity
     * @throws RemoteException on network failure
     */
    private synchronized void alterClaimedCapacity(final double amount) throws RemoteException
    {
        this.claimedCapacity += amount;
        this.fireTimedEvent(Resource.UTILIZATION_EVENT, this.claimedCapacity, this.simulator.getSimulatorTime());
    }

    /**
     * sets the capacity of the resource
     * @param capacity the new maximal capacity
     */
    public void setCapacity(final double capacity)
    {
        this.capacity = capacity;
        try
        {
            this.releaseCapacity(0.0);
        }
        catch (RemoteException remoteException)
        {
            // This exception cannot occur.
            Logger.warning(this, "setCapacity", remoteException);
        }
    }

    /**
     * requests an amount of capacity from the resource \
     * @param amount the requested amount
     * @param requestor the RequestorInterface requesting the amount
     * @throws RemoteException on network failure
     * @throws SimRuntimeException on other failures
     */
    public synchronized void requestCapacity(final double amount, final ResourceRequestorInterface requestor)
            throws RemoteException, SimRuntimeException
    {
        this.requestCapacity(amount, requestor, Resource.DEFAULT_REQUEST_PRIORITY);
    }

    /**
     * requests an amount of capacity from the resource \
     * @param amount the requested amount
     * @param requestor the RequestorInterface requesting the amount
     * @param priority the priority of the request
     * @throws RemoteException on network failure
     * @throws SimRuntimeException on other failures
     */
    public synchronized void requestCapacity(final double amount, final ResourceRequestorInterface requestor,
            final int priority) throws RemoteException, SimRuntimeException
    {
        if (amount < 0.0)
        {
            throw new SimRuntimeException("requested capacity on resource cannot <0.0");
        }
        if ((this.claimedCapacity + amount) <= this.capacity)
        {
            this.alterClaimedCapacity(amount);
            this.simulator.scheduleEventNow(this, requestor, "receiveRequestedResource", new Object[]{
                    new Double(amount), this});
        }
        else
        {
            synchronized (this.requests)
            {
                this.requests.add(new Request(requestor, amount, priority));
            }
            this.fireTimedEvent(Resource.RESOURCE_REQUESTED_QUEUE_LENGTH, (double) this.requests.size(),
                    this.simulator.getSimulatorTime());
        }
    }

    /**
     * releases an amount of capacity from the resource.
     * @param amount the amount to release
     * @throws RemoteException on network failure
     */
    public void releaseCapacity(final double amount) throws RemoteException
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
            for (Iterator<Request> i = this.requests.iterator(); i.hasNext();)
            {
                Request request = i.next();
                if ((this.capacity - this.claimedCapacity) >= request.getAmount())
                {
                    this.alterClaimedCapacity(request.getAmount());
                    request.getRequestor().receiveRequestedResource(request.getAmount(), this);
                    synchronized (this.requests)
                    {
                        i.remove();
                    }
                    this.fireTimedEvent(Resource.RESOURCE_REQUESTED_QUEUE_LENGTH, (double) this.requests.size(),
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
    protected class RequestComparator implements Comparator<Request>
    {
        /**
         * compares two request.
         * @param arg0 the first request
         * @param arg1 the second request
         * @return the integer value
         */
        public int compare(final Request arg0, final Request arg1)
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
     */
    public static class Request
    {
        /** the priority of the request. */
        private int priority = 5;

        /** the number of this request. */
        private long id = -1;

        /** requestor the resourceRequestor. */
        private ResourceRequestorInterface requestor;

        /** amount is the amount requested by the resource. */
        private double amount;

        /**
         * constructs a new Request.
         * @param requestor the requestor
         * @param amount the requested amount
         * @param priority the priority of the request
         */
        public Request(final ResourceRequestorInterface requestor, final double amount, final int priority)
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
        public double getAmount()
        {
            return this.amount;
        }

        /**
         * gets the requestor.
         * @return the Requestor.
         */
        public ResourceRequestorInterface getRequestor()
        {
            return this.requestor;
        }

        /**
         * returns the priority of the request
         * @return the priority
         */
        public int getPriority()
        {
            return this.priority;
        }

        /**
         * returns the id of the request
         * @return the id
         */
        public long getId()
        {
            return this.id;
        }

        /** {@inheritDoc} */
        @Override
        public String toString()
        {
            return "RequestForResource[requestor=" + this.requestor + ";amount=" + this.amount + ";priority="
                    + this.priority + "]";
        }
    }
}
