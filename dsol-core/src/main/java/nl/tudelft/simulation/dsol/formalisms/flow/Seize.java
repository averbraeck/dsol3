package nl.tudelft.simulation.dsol.formalisms.flow;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import nl.tudelft.simulation.dsol.formalisms.Resource;
import nl.tudelft.simulation.dsol.formalisms.ResourceRequestorInterface;
import nl.tudelft.simulation.dsol.simtime.SimTime;
import nl.tudelft.simulation.dsol.simtime.SimTimeCalendarDouble;
import nl.tudelft.simulation.dsol.simtime.SimTimeCalendarFloat;
import nl.tudelft.simulation.dsol.simtime.SimTimeCalendarLong;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simtime.SimTimeDoubleUnit;
import nl.tudelft.simulation.dsol.simtime.SimTimeFloat;
import nl.tudelft.simulation.dsol.simtime.SimTimeFloatUnit;
import nl.tudelft.simulation.dsol.simtime.SimTimeLong;
import nl.tudelft.simulation.dsol.simtime.SimTimeLongUnit;
import nl.tudelft.simulation.dsol.simtime.UnitTimeDouble;
import nl.tudelft.simulation.dsol.simtime.UnitTimeFloat;
import nl.tudelft.simulation.dsol.simtime.UnitTimeLong;
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
 * @param <A> the absolute storage type for the simulation time, e.g. Calendar, UnitTimeDouble, or Double.
 * @param <R> the relative type for time storage, e.g. Long for the Calendar. For most non-calendar types, the absolute
 *            and relative types are the same.
 * @param <T> the extended type itself to be able to implement a comparator on the simulation time.
 * @since 1.5
 */
public class Seize<A extends Comparable<A>, R extends Number & Comparable<R>, T extends SimTime<A, R, T>>
        extends Station<A, R, T> implements ResourceRequestorInterface<T>
{
    /** */
    private static final long serialVersionUID = 20140911L;

    /** QUEUE_LENGTH_EVENT is fired when the queue length is changed. */
    public static final EventType QUEUE_LENGTH_EVENT = new EventType("QUEUE_LENGTH_EVENT");

    /** DELAY_TIME is fired when a new delayTime is computed. */
    public static final EventType DELAY_TIME = new EventType("DELAY_TIME");

    /** queue refers to the list of waiting requestors. */
    private List<Request<A, R, T>> queue = Collections.synchronizedList(new ArrayList<Request<A, R, T>>());

    /** requestedCapacity is the amount of resource requested on the resource. */
    private double requestedCapacity = Double.NaN;

    /**
     * resource on which the capacity is requested.
     */
    private Resource<T> resource;

    /**
     * Constructor for Seize.
     * @param simulator on which behavior is scheduled
     * @param resource which is claimed
     */
    public Seize(final DEVSSimulatorInterface<A, R, T> simulator, final Resource<T> resource)
    {
        this(simulator, resource, 1.0);
    }

    /**
     * Constructor for Seize.
     * @param simulator on which behavior is scheduled
     * @param resource which is claimed
     * @param requestedCapacity is the amount which is claimed by the seize
     */
    public Seize(final DEVSSimulatorInterface<A, R, T> simulator, final Resource<T> resource,
            final double requestedCapacity)
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
     * receives an object which request an amount.
     * @param object the object
     * @param _requestedCapacity the requested capacity
     * @throws RemoteException on network failures
     */
    public final synchronized void receiveObject(final Object object, final double _requestedCapacity)
            throws RemoteException
    {
        super.receiveObject(object);
        Request<A, R, T> request = new Request<A, R, T>(object, _requestedCapacity, this.simulator.getSimulatorTime());
        synchronized (this.queue)
        {
            this.queue.add(request);
        }
        try
        {
            this.fireTimedEvent(Seize.QUEUE_LENGTH_EVENT, (double) this.queue.size(),
                    this.simulator.getSimulatorTime());
            this.resource.requestCapacity(_requestedCapacity, this);
        }
        catch (Exception exception)
        {
            Logger.warning(this, "receiveObject", exception);
        }
    }

    /** {@inheritDoc} */
    @Override
    public final void receiveObject(final Object object) throws RemoteException
    {
        this.receiveObject(object, this.requestedCapacity);
    }

    /**
     * sets the queue to this seize. This enables seize blocks to share one queue.
     * @param queue is a new queue.
     */
    public final void setQueue(final List<Request<A, R, T>> queue)
    {
        this.queue = queue;
    }

    /**
     * returns the queue.
     * @return List the queue
     */
    @SuppressWarnings("checkstyle:designforextension")
    public List<Request<A, R, T>> getQueue()
    {
        return this.queue;
    }

    /** {@inheritDoc} */
    @Override
    public final void receiveRequestedResource(final double _requestedCapacity, final Resource<T> _resource)
            throws RemoteException
    {
        for (Request<A, R, T> request : this.queue)
        {
            if (request.getAmount() == _requestedCapacity)
            {
                synchronized (this.queue)
                {
                    this.queue.remove(request);
                }
                this.fireTimedEvent(Seize.QUEUE_LENGTH_EVENT, (double) this.queue.size(),
                        this.simulator.getSimulatorTime());
                R delay = this.simulator.getSimulatorTime().minus(request.getCreationTime());
                this.fireTimedEvent(Seize.DELAY_TIME, delay, this.simulator.getSimulatorTime());
                this.releaseObject(request.getEntity());
                return;
            }
        }
    }

    /**
     * The private RequestClass defines the requests for resource.
     * @param <A> the absolute storage type for the simulation time, e.g. Calendar, UnitTimeDouble, or Double.
     * @param <R> the relative type for time storage, e.g. Long for the Calendar. For most non-calendar types, the
     *            absolute and relative types are the same.
     * @param <T> the extended type itself to be able to implement a comparator on the simulation time.
     */
    public static class Request<A extends Comparable<A>, R extends Number & Comparable<R>, T extends SimTime<A, R, T>>
    {
        /** amount is the requested amount. */
        private final double amount;

        /** entity is the object requesting the amount. */
        private final Object entity;

        /** creationTime refers to the moment the request was created. */
        private final T creationTime;

        /**
         * Method Request.
         * @param entity the requesting entity
         * @param amount is the requested amount
         * @param creationTime the time the request was created
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
    public static class TimeDouble extends Seize<Double, Double, SimTimeDouble>
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * Constructor for Seize.
         * @param simulator on which behavior is scheduled
         * @param resource which is claimed
         */
        public TimeDouble(final DEVSSimulatorInterface.TimeDouble simulator, final Resource<SimTimeDouble> resource)
        {
            super(simulator, resource);
        }

        /**
         * Constructor for Seize.
         * @param simulator on which behavior is scheduled
         * @param resource which is claimed
         * @param requestedCapacity is the amount which is claimed by the seize
         */
        public TimeDouble(final DEVSSimulatorInterface.TimeDouble simulator, final Resource<SimTimeDouble> resource,
                final double requestedCapacity)
        {
            super(simulator, resource, requestedCapacity);
        }
    }

    /** Easy access class Seize.TimeFloat. */
    public static class TimeFloat extends Seize<Float, Float, SimTimeFloat>
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * Constructor for Seize.
         * @param simulator on which behavior is scheduled
         * @param resource which is claimed
         */
        public TimeFloat(final DEVSSimulatorInterface.TimeFloat simulator, final Resource<SimTimeFloat> resource)
        {
            super(simulator, resource);
        }

        /**
         * Constructor for Seize.
         * @param simulator on which behavior is scheduled
         * @param resource which is claimed
         * @param requestedCapacity is the amount which is claimed by the seize
         */
        public TimeFloat(final DEVSSimulatorInterface.TimeFloat simulator, final Resource<SimTimeFloat> resource,
                final double requestedCapacity)
        {
            super(simulator, resource, requestedCapacity);
        }

    }

    /** Easy access class Seize.TimeLong. */
    public static class TimeLong extends Seize<Long, Long, SimTimeLong>
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * Constructor for Seize.
         * @param simulator on which behavior is scheduled
         * @param resource which is claimed
         */
        public TimeLong(final DEVSSimulatorInterface.TimeLong simulator, final Resource<SimTimeLong> resource)
        {
            super(simulator, resource);
        }

        /**
         * Constructor for Seize.
         * @param simulator on which behavior is scheduled
         * @param resource which is claimed
         * @param requestedCapacity is the amount which is claimed by the seize
         */
        public TimeLong(final DEVSSimulatorInterface.TimeLong simulator, final Resource<SimTimeLong> resource,
                final double requestedCapacity)
        {
            super(simulator, resource, requestedCapacity);
        }
    }

    /** Easy access class Seize.TimeDoubleUnit. */
    public static class TimeDoubleUnit extends Seize<UnitTimeDouble, UnitTimeDouble, SimTimeDoubleUnit>
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * Constructor for Seize.
         * @param simulator on which behavior is scheduled
         * @param resource which is claimed
         */
        public TimeDoubleUnit(final DEVSSimulatorInterface.TimeDoubleUnit simulator,
                final Resource<SimTimeDoubleUnit> resource)
        {
            super(simulator, resource);
        }

        /**
         * Constructor for Seize.
         * @param simulator on which behavior is scheduled
         * @param resource which is claimed
         * @param requestedCapacity is the amount which is claimed by the seize
         */
        public TimeDoubleUnit(final DEVSSimulatorInterface.TimeDoubleUnit simulator,
                final Resource<SimTimeDoubleUnit> resource, final double requestedCapacity)
        {
            super(simulator, resource, requestedCapacity);
        }
    }

    /** Easy access class Seize.TimeFloatUnit. */
    public static class TimeFloatUnit extends Seize<UnitTimeFloat, UnitTimeFloat, SimTimeFloatUnit>
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * Constructor for Seize.
         * @param simulator on which behavior is scheduled
         * @param resource which is claimed
         */
        public TimeFloatUnit(final DEVSSimulatorInterface.TimeFloatUnit simulator,
                final Resource<SimTimeFloatUnit> resource)
        {
            super(simulator, resource);
        }

        /**
         * Constructor for Seize.
         * @param simulator on which behavior is scheduled
         * @param resource which is claimed
         * @param requestedCapacity is the amount which is claimed by the seize
         */
        public TimeFloatUnit(final DEVSSimulatorInterface.TimeFloatUnit simulator,
                final Resource<SimTimeFloatUnit> resource, final double requestedCapacity)
        {
            super(simulator, resource, requestedCapacity);
        }
    }

    /** Easy access class Seize.TimeLongUnit. */
    public static class TimeLongUnit extends Seize<UnitTimeLong, UnitTimeLong, SimTimeLongUnit>
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * Constructor for Seize.
         * @param simulator on which behavior is scheduled
         * @param resource which is claimed
         */
        public TimeLongUnit(final DEVSSimulatorInterface.TimeLongUnit simulator,
                final Resource<SimTimeLongUnit> resource)
        {
            super(simulator, resource);
        }

        /**
         * Constructor for Seize.
         * @param simulator on which behavior is scheduled
         * @param resource which is claimed
         * @param requestedCapacity is the amount which is claimed by the seize
         */
        public TimeLongUnit(final DEVSSimulatorInterface.TimeLongUnit simulator,
                final Resource<SimTimeLongUnit> resource, final double requestedCapacity)
        {
            super(simulator, resource, requestedCapacity);
        }
    }

    /** Easy access class Seize.CalendarDouble. */
    public static class CalendarDouble extends Seize<Calendar, UnitTimeDouble, SimTimeCalendarDouble>
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * Constructor for Seize.
         * @param simulator on which behavior is scheduled
         * @param resource which is claimed
         */
        public CalendarDouble(final DEVSSimulatorInterface.CalendarDouble simulator,
                final Resource<SimTimeCalendarDouble> resource)
        {
            super(simulator, resource);
        }

        /**
         * Constructor for Seize.
         * @param simulator on which behavior is scheduled
         * @param resource which is claimed
         * @param requestedCapacity is the amount which is claimed by the seize
         */
        public CalendarDouble(final DEVSSimulatorInterface.CalendarDouble simulator,
                final Resource<SimTimeCalendarDouble> resource, final double requestedCapacity)
        {
            super(simulator, resource, requestedCapacity);
        }
    }

    /** Easy access class Seize.CalendarFloat. */
    public static class CalendarFloat extends Seize<Calendar, UnitTimeFloat, SimTimeCalendarFloat>
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * Constructor for Seize.
         * @param simulator on which behavior is scheduled
         * @param resource which is claimed
         */
        public CalendarFloat(final DEVSSimulatorInterface.CalendarFloat simulator,
                final Resource<SimTimeCalendarFloat> resource)
        {
            super(simulator, resource);
        }

        /**
         * Constructor for Seize.
         * @param simulator on which behavior is scheduled
         * @param resource which is claimed
         * @param requestedCapacity is the amount which is claimed by the seize
         */
        public CalendarFloat(final DEVSSimulatorInterface.CalendarFloat simulator,
                final Resource<SimTimeCalendarFloat> resource, final double requestedCapacity)
        {
            super(simulator, resource, requestedCapacity);
        }
    }

    /** Easy access class Seize.CalendarLong. */
    public static class CalendarLong extends Seize<Calendar, UnitTimeLong, SimTimeCalendarLong>
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * Constructor for Seize.
         * @param simulator on which behavior is scheduled
         * @param resource which is claimed
         */
        public CalendarLong(final DEVSSimulatorInterface.CalendarLong simulator,
                final Resource<SimTimeCalendarLong> resource)
        {
            super(simulator, resource);
        }

        /**
         * Constructor for Seize.
         * @param simulator on which behavior is scheduled
         * @param resource which is claimed
         * @param requestedCapacity is the amount which is claimed by the seize
         */
        public CalendarLong(final DEVSSimulatorInterface.CalendarLong simulator,
                final Resource<SimTimeCalendarLong> resource, final double requestedCapacity)
        {
            super(simulator, resource, requestedCapacity);
        }
    }
}
