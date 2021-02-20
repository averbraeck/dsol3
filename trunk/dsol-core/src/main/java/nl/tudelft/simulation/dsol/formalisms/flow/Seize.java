package nl.tudelft.simulation.dsol.formalisms.flow;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.djunits.value.vdouble.scalar.Duration;
import org.djunits.value.vdouble.scalar.Time;
import org.djunits.value.vfloat.scalar.FloatDuration;
import org.djunits.value.vfloat.scalar.FloatTime;
import org.djutils.event.TimedEventType;
import org.djutils.metadata.MetaData;
import org.djutils.metadata.ObjectDescriptor;

import nl.tudelft.simulation.dsol.formalisms.Resource;
import nl.tudelft.simulation.dsol.formalisms.ResourceRequestorInterface;
import nl.tudelft.simulation.dsol.simtime.SimTime;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simtime.SimTimeDoubleUnit;
import nl.tudelft.simulation.dsol.simtime.SimTimeFloat;
import nl.tudelft.simulation.dsol.simtime.SimTimeFloatUnit;
import nl.tudelft.simulation.dsol.simtime.SimTimeLong;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;

/**
 * The Seize requests a resource and releases an entity whenever this resource is actually claimed.
 * <p>
 * Copyright (c) 2002-2021 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @param <A> the absolute storage type for the simulation time, e.g. Calendar, Duration, or Double.
 * @param <R> the relative type for time storage, e.g. Long for the Calendar. For most non-calendar types, the absolute and
 *            relative types are the same.
 * @param <T> the extended type itself to be able to implement a comparator on the simulation time.
 * @since 1.5
 */
public class Seize<A extends Comparable<A> & Serializable, R extends Number & Comparable<R>, T extends SimTime<A, R, T>>
        extends Station<A, R, T> implements ResourceRequestorInterface<A, R, T>
{
    /** */
    private static final long serialVersionUID = 20140911L;

    /** QUEUE_LENGTH_EVENT is fired when the queue length is changed. */
    public static final TimedEventType QUEUE_LENGTH_EVENT = new TimedEventType(new MetaData("QUEUE_LENGTH_EVENT",
            "Queue length", new ObjectDescriptor("queueLength", "Queue length", Integer.class)));

    /** DELAY_TIME is fired when a new delayTime is computed. */
    public static final TimedEventType DELAY_TIME = new TimedEventType(new MetaData("DELAY_TIME", "Delay time",
            new ObjectDescriptor("delayTime", "Delay time (as a double)", Double.class)));

    /** queue refers to the list of waiting requestors. */
    private List<Request<A, R, T>> queue = Collections.synchronizedList(new ArrayList<Request<A, R, T>>());

    /** requestedCapacity is the amount of resource requested on the resource. */
    private double requestedCapacity = Double.NaN;

    /** resource on which the capacity is requested. */
    private Resource<A, R, T> resource;

    /**
     * Constructor for Seize.
     * @param id Serializable; the id of the Station
     * @param simulator DEVSSimulatorInterface&lt;A,R,T&gt;; on which behavior is scheduled
     * @param resource Resource&lt;A,R,T&gt;; which is claimed
     */
    public Seize(final Serializable id, final DEVSSimulatorInterface<A, R, T> simulator, final Resource<A, R, T> resource)
    {
        this(id, simulator, resource, 1.0);
    }

    /**
     * Constructor for Seize.
     * @param id Serializable; the id of the Station
     * @param simulator DEVSSimulatorInterface&lt;A,R,T&gt;; on which behavior is scheduled
     * @param resource Resource&lt;A,R,T&gt;; which is claimed
     * @param requestedCapacity double; is the amount which is claimed by the seize
     */
    public Seize(final Serializable id, final DEVSSimulatorInterface<A, R, T> simulator, final Resource<A, R, T> resource,
            final double requestedCapacity)
    {
        super(id, simulator);
        if (requestedCapacity < 0.0)
        {
            throw new IllegalArgumentException("requestedCapacity cannot < 0.0");
        }
        this.requestedCapacity = requestedCapacity;
        this.resource = resource;
    }

    /**
     * receives an object which request an amount.
     * @param object Object; the object
     * @param pRequestedCapacity double; the requested capacity
     */
    public final synchronized void receiveObject(final Object object, final double pRequestedCapacity)

    {
        super.receiveObject(object);
        Request<A, R, T> request = new Request<A, R, T>(object, pRequestedCapacity, this.simulator.getSimTime());
        synchronized (this.queue)
        {
            this.queue.add(request);
        }
        try
        {
            this.fireTimedEvent(Seize.QUEUE_LENGTH_EVENT, this.queue.size(), this.simulator.getSimulatorTime());
            this.resource.requestCapacity(pRequestedCapacity, this);
        }
        catch (Exception exception)
        {
            this.simulator.getLogger().always().warn(exception, "receiveObject");
        }
    }

    /** {@inheritDoc} */
    @Override
    public void receiveObject(final Object object)
    {
        this.receiveObject(object, this.requestedCapacity);
    }

    /**
     * sets the queue to this seize. This enables seize blocks to share one queue.
     * @param queue List&lt;Request&lt;A,R,T&gt;&gt;; is a new queue.
     */
    public final void setQueue(final List<Request<A, R, T>> queue)
    {
        this.queue = queue;
    }

    /**
     * returns the queue.
     * @return List the queue
     */
    public List<Request<A, R, T>> getQueue()
    {
        return this.queue;
    }

    /** {@inheritDoc} */
    @Override
    public final void receiveRequestedResource(final double pRequestedCapacity, final Resource<A, R, T> pResource)

    {
        for (Request<A, R, T> request : this.queue)
        {
            if (request.getAmount() == pRequestedCapacity)
            {
                synchronized (this.queue)
                {
                    this.queue.remove(request);
                }
                this.fireTimedEvent(Seize.QUEUE_LENGTH_EVENT, this.queue.size(), this.simulator.getSimulatorTime());
                R delay = this.simulator.getSimTime().diff(request.getCreationTime());
                this.fireTimedEvent(Seize.DELAY_TIME, delay.doubleValue(), this.simulator.getSimulatorTime());
                this.releaseObject(request.getEntity());
                return;
            }
        }
    }

    /**
     * The private RequestClass defines the requests for resource.
     * @param <A> the absolute storage type for the simulation time, e.g. Calendar, Duration, or Double.
     * @param <R> the relative type for time storage, e.g. Long for the Calendar. For most non-calendar types, the absolute and
     *            relative types are the same.
     * @param <T> the extended type itself to be able to implement a comparator on the simulation time.
     */
    public static class Request<A extends Comparable<A> & Serializable, R extends Number & Comparable<R>,
            T extends SimTime<A, R, T>>
    {
        /** amount is the requested amount. */
        private final double amount;

        /** entity is the object requesting the amount. */
        private final Object entity;

        /** creationTime refers to the moment the request was created. */
        private final T creationTime;

        /**
         * Method Request.
         * @param entity Object; the requesting entity
         * @param amount double; is the requested amount
         * @param creationTime T; the time the request was created
         */
        public Request(final Object entity, final double amount, final T creationTime)
        {
            this.entity = entity;
            this.amount = amount;
            this.creationTime = creationTime;
        }

        /**
         * Returns the amount.
         * @return double
         */
        public final double getAmount()
        {
            return this.amount;
        }

        /**
         * Returns the entity.
         * @return Object
         */
        public final Object getEntity()
        {
            return this.entity;
        }

        /**
         * Returns the creationTime.
         * @return double
         */
        public final T getCreationTime()
        {
            return this.creationTime;
        }
    }

    /***********************************************************************************************************/
    /************************************* EASY ACCESS CLASS EXTENSIONS ****************************************/
    /***********************************************************************************************************/

    /** Easy access class Seize.TimeDouble. */
    public static class TimeDouble extends Seize<Double, Double, SimTimeDouble> implements StationInterface.TimeDouble
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * Constructor for Seize.
         * @param id Serializable; the id of the Station
         * @param simulator DEVSSimulatorInterface.TimeDouble; on which behavior is scheduled
         * @param resource Resource&lt;Double,Double,SimTimeDouble&gt;; which is claimed
         */
        public TimeDouble(final Serializable id, final DEVSSimulatorInterface.TimeDouble simulator,
                final Resource<Double, Double, SimTimeDouble> resource)
        {
            super(id, simulator, resource);
        }

        /**
         * Constructor for Seize.
         * @param id Serializable; the id of the Station
         * @param simulator DEVSSimulatorInterface.TimeDouble; on which behavior is scheduled
         * @param resource Resource&lt;Double,Double,SimTimeDouble&gt;; which is claimed
         * @param requestedCapacity double; is the amount which is claimed by the seize
         */
        public TimeDouble(final Serializable id, final DEVSSimulatorInterface.TimeDouble simulator,
                final Resource<Double, Double, SimTimeDouble> resource, final double requestedCapacity)
        {
            super(id, simulator, resource, requestedCapacity);
        }
    }

    /** Easy access class Seize.TimeFloat. */
    public static class TimeFloat extends Seize<Float, Float, SimTimeFloat> implements StationInterface.TimeFloat
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * Constructor for Seize.
         * @param id Serializable; the id of the Station
         * @param simulator DEVSSimulatorInterface.TimeFloat; on which behavior is scheduled
         * @param resource Resource&lt;Float,Float,SimTimeFloat&gt;; which is claimed
         */
        public TimeFloat(final Serializable id, final DEVSSimulatorInterface.TimeFloat simulator,
                final Resource<Float, Float, SimTimeFloat> resource)
        {
            super(id, simulator, resource);
        }

        /**
         * Constructor for Seize.
         * @param id Serializable; the id of the Station
         * @param simulator DEVSSimulatorInterface.TimeFloat; on which behavior is scheduled
         * @param resource Resource&lt;Float,Float,SimTimeFloat&gt;; which is claimed
         * @param requestedCapacity double; is the amount which is claimed by the seize
         */
        public TimeFloat(final Serializable id, final DEVSSimulatorInterface.TimeFloat simulator,
                final Resource<Float, Float, SimTimeFloat> resource, final double requestedCapacity)
        {
            super(id, simulator, resource, requestedCapacity);
        }

    }

    /** Easy access class Seize.TimeLong. */
    public static class TimeLong extends Seize<Long, Long, SimTimeLong> implements StationInterface.TimeLong
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * Constructor for Seize.
         * @param id Serializable; the id of the Station
         * @param simulator DEVSSimulatorInterface.TimeLong; on which behavior is scheduled
         * @param resource Resource&lt;Long,Long,SimTimeLong&gt;; which is claimed
         */
        public TimeLong(final Serializable id, final DEVSSimulatorInterface.TimeLong simulator,
                final Resource<Long, Long, SimTimeLong> resource)
        {
            super(id, simulator, resource);
        }

        /**
         * Constructor for Seize.
         * @param id Serializable; the id of the Station
         * @param simulator DEVSSimulatorInterface.TimeLong; on which behavior is scheduled
         * @param resource Resource&lt;Long,Long,SimTimeLong&gt;; which is claimed
         * @param requestedCapacity double; is the amount which is claimed by the seize
         */
        public TimeLong(final Serializable id, final DEVSSimulatorInterface.TimeLong simulator,
                final Resource<Long, Long, SimTimeLong> resource, final double requestedCapacity)
        {
            super(id, simulator, resource, requestedCapacity);
        }
    }

    /** Easy access class Seize.TimeDoubleUnit. */
    public static class TimeDoubleUnit extends Seize<Time, Duration, SimTimeDoubleUnit>
            implements StationInterface.TimeDoubleUnit
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * Constructor for Seize.
         * @param id Serializable; the id of the Station
         * @param simulator DEVSSimulatorInterface.TimeDoubleUnit; on which behavior is scheduled
         * @param resource Resource&lt;Time,Duration,SimTimeDoubleUnit&gt;; which is claimed
         */
        public TimeDoubleUnit(final Serializable id, final DEVSSimulatorInterface.TimeDoubleUnit simulator,
                final Resource<Time, Duration, SimTimeDoubleUnit> resource)
        {
            super(id, simulator, resource);
        }

        /**
         * Constructor for Seize.
         * @param id Serializable; the id of the Station
         * @param simulator DEVSSimulatorInterface.TimeDoubleUnit; on which behavior is scheduled
         * @param resource Resource&lt;Time,Duration,SimTimeDoubleUnit&gt;; which is claimed
         * @param requestedCapacity double; is the amount which is claimed by the seize
         */
        public TimeDoubleUnit(final Serializable id, final DEVSSimulatorInterface.TimeDoubleUnit simulator,
                final Resource<Time, Duration, SimTimeDoubleUnit> resource, final double requestedCapacity)
        {
            super(id, simulator, resource, requestedCapacity);
        }
    }

    /** Easy access class Seize.TimeFloatUnit. */
    public static class TimeFloatUnit extends Seize<FloatTime, FloatDuration, SimTimeFloatUnit>
            implements StationInterface.TimeFloatUnit
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * Constructor for Seize.
         * @param id Serializable; the id of the Station
         * @param simulator DEVSSimulatorInterface.TimeFloatUnit; on which behavior is scheduled
         * @param resource Resource&lt;FloatTime,FloatDuration,SimTimeFloatUnit&gt;; which is claimed
         */
        public TimeFloatUnit(final Serializable id, final DEVSSimulatorInterface.TimeFloatUnit simulator,
                final Resource<FloatTime, FloatDuration, SimTimeFloatUnit> resource)
        {
            super(id, simulator, resource);
        }

        /**
         * Constructor for Seize.
         * @param id Serializable; the id of the Station
         * @param simulator DEVSSimulatorInterface.TimeFloatUnit; on which behavior is scheduled
         * @param resource Resource&lt;FloatTime,FloatDuration,SimTimeFloatUnit&gt;; which is claimed
         * @param requestedCapacity double; is the amount which is claimed by the seize
         */
        public TimeFloatUnit(final Serializable id, final DEVSSimulatorInterface.TimeFloatUnit simulator,
                final Resource<FloatTime, FloatDuration, SimTimeFloatUnit> resource, final double requestedCapacity)
        {
            super(id, simulator, resource, requestedCapacity);
        }
    }

}
