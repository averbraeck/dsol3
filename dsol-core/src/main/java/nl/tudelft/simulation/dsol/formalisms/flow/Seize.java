package nl.tudelft.simulation.dsol.formalisms.flow;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nl.tudelft.simulation.dsol.formalisms.Resource;
import nl.tudelft.simulation.dsol.formalisms.ResourceRequestorInterface;
import nl.tudelft.simulation.dsol.simtime.SimTime;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;
import nl.tudelft.simulation.event.EventType;
import nl.tudelft.simulation.logger.Logger;

/**
 * The Seize requests a resource and releases an entity whenever this resource is actually claimed.
 * <p>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl"> www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:36:44 $
 * @since 1.5
 */
public class Seize extends Station implements ResourceRequestorInterface
{
    /** QUEUE_LENGTH_EVENT is fired when the queue length is changed. */
    public static final EventType QUEUE_LENGTH_EVENT = new EventType("QUEUE_QUEUE_LENGTH_EVENTLENGTH_EVENT");

    /** DELAY_TIME is fired when a new delayTime is computed. */
    public static final EventType DELAY_TIME = new EventType("DELAY_TIME");

    /** queue refers to the list of waiting requestors. */
    private List<Request> queue = Collections.synchronizedList(new ArrayList<Request>());

    /** requestedCapacity is the amount of resource requested on the resource. */
    private double requestedCapacity = Double.NaN;

    /**
     * resource on which the capacity is requested
     */
    private Resource resource;

    /**
     * Constructor for Seize.
     * @param simulator on which behavior is scheduled
     * @param resource which is claimed
     */
    public Seize(final DEVSSimulatorInterface simulator, final Resource resource)
    {
        this(simulator, resource, 1.0);
    }

    /**
     * Constructor for Seize.
     * @param simulator on which behavior is scheduled
     * @param resource which is claimed
     * @param requestedCapacity is the amount which is claimed by the seize
     */
    public Seize(final DEVSSimulatorInterface<?, ?, ?> simulator, final Resource resource, final double requestedCapacity)
    {
        super(simulator);
        if (requestedCapacity < 0.0)
        {
            throw new IllegalArgumentException("requestedCapacity cannot < 0.0");
        }
        this.requestedCapacity = requestedCapacity;
        this.resource = resource;
    }

    /**
     * receives an object which request an amount
     * @param object the object
     * @param requestedCapacity the requested capacity
     * @throws RemoteException on network failures
     */
    public synchronized void receiveObject(final Object object, final double requestedCapacity) throws RemoteException
    {
        super.receiveObject(object);
        Request request = new Request(object, requestedCapacity, this.simulator.getSimulatorTime());
        synchronized (this.queue)
        {
            this.queue.add(request);
        }
        try
        {
            this.fireTimedEvent(Seize.QUEUE_LENGTH_EVENT, (double) this.queue.size(), this.simulator.getSimulatorTime());
            this.resource.requestCapacity(requestedCapacity, this);
        }
        catch (Exception exception)
        {
            Logger.warning(this, "receiveObject", exception);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void receiveObject(final Object object) throws RemoteException
    {
        this.receiveObject(object, this.requestedCapacity);
    }

    /**
     * sets the queue to this seize. This enables seize blocks to share one queue.
     * @param queue is a new queue.
     */
    public void setQueue(final List<Request> queue)
    {
        this.queue = queue;
    }

    /**
     * returns the queue
     * @return List the queue
     */
    public List<Request> getQueue()
    {
        return this.queue;
    }

    /** {@inheritDoc} */
    public void receiveRequestedResource(final double requestedCapacity, final Resource resource)
            throws RemoteException
    {
        for (Request request : this.queue)
        {
            if (request.getAmount() == requestedCapacity)
            {
                synchronized (this.queue)
                {
                    this.queue.remove(request);
                }
                this.fireTimedEvent(Seize.QUEUE_LENGTH_EVENT, (double) this.queue.size(), this.simulator.getSimulatorTime());
                // TODO: R delay = (this.simulator.getSimulatorTime().minus(request.getCreationTime()));
                // TODO: this.fireEvent(Seize.DELAY_TIME, delay, this.simulator.getSimulatorTime());
                this.releaseObject(request.getEntity());
                return;
            }
        }
    }

    /**
     * The private RequestClass defines the requests for resource
     */
    public static class Request
    {
        /** amount is the requested amount. */
        private final double amount;

        /** entity is the object requesting the amount. */
        private final Object entity;

        /** creationTime refers to the moment the request was created. */
        private final SimTime<?, ?, ?> creationTime;

        /**
         * Method Request.
         * @param entity the requesting entity
         * @param amount is the requested amount
         * @param creationTime the time the request was created
         */
        public Request(final Object entity, final double amount, final SimTime<?, ?, ?> creationTime)
        {
            this.entity = entity;
            this.amount = amount;
            this.creationTime = creationTime;
        }

        /**
         * Returns the amount.
         * @return double
         */
        public double getAmount()
        {
            return this.amount;
        }

        /**
         * Returns the entity.
         * @return Object
         */
        public Object getEntity()
        {
            return this.entity;
        }

        /**
         * Returns the creationTime
         * @return double
         */
        public SimTime<?, ?, ?> getCreationTime()
        {
            return this.creationTime;
        }
    }
}
