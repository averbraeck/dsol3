package nl.tudelft.simulation.dsol.statistics;

import java.io.Serializable;
import java.rmi.RemoteException;

import javax.naming.NamingException;

import org.djunits.value.vdouble.scalar.Duration;
import org.djunits.value.vdouble.scalar.Time;
import org.djunits.value.vfloat.scalar.FloatDuration;
import org.djunits.value.vfloat.scalar.FloatTime;
import org.djutils.event.EventInterface;
import org.djutils.event.EventProducerInterface;
import org.djutils.event.TimedEventType;
import org.djutils.event.ref.ReferenceType;
import org.djutils.metadata.MetaData;
import org.djutils.metadata.ObjectDescriptor;
import org.djutils.stats.summarizers.event.EventBasedCounter;

import nl.tudelft.simulation.dsol.experiment.ReplicationInterface;
import nl.tudelft.simulation.dsol.simtime.SimTime;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simtime.SimTimeDoubleUnit;
import nl.tudelft.simulation.dsol.simtime.SimTimeFloat;
import nl.tudelft.simulation.dsol.simtime.SimTimeFloatUnit;
import nl.tudelft.simulation.dsol.simtime.SimTimeLong;
import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;
import nl.tudelft.simulation.naming.context.ContextInterface;
import nl.tudelft.simulation.naming.context.util.ContextUtil;

/**
 * The time-aware counter extends the djutils event-based counter and links it to the dsol framework.
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
 * @param <T> the absolute simulation time to use in the warmup event
 */
public class SimCounter<A extends Comparable<A> & Serializable, R extends Number & Comparable<R>, T extends SimTime<A, R, T>>
        extends EventBasedCounter implements StatisticsInterface<A, R, T>
{
    /** */
    private static final long serialVersionUID = 20140804L;

    /** the simulator to subscribe to and from. */
    private SimulatorInterface<A, R, T> simulator = null;

    /** OBSERVATION_ADDED_EVENT is fired whenever an observation is processed. */
    public static final TimedEventType TIMED_OBSERVATION_ADDED_EVENT =
            new TimedEventType(new MetaData("TIMED_OBSERVATION_ADDED_EVENT", "observation added to Persistent",
                    new ObjectDescriptor("longValue", "long value to add to counter", Long.class)));

    /** INITIALIZED_EVENT is fired whenever a Tally is (re-)initialized. */
    public static final TimedEventType TIMED_INITIALIZED_EVENT = new TimedEventType(new MetaData("TIMED_INITIALIZED_EVENT",
            "Counter initialized", new ObjectDescriptor("simCounter", "Counter object", SimCounter.class)));

    /** gathering data stopped or not? */
    private boolean stopped = false;

    /**
     * constructs a new SimCounter.
     * @param description String; refers to the description of this counter
     * @param simulator SimulatorInterface&lt;A,R,T&gt;; the simulator
     * @throws RemoteException on network error for one of the listeners
     */
    public SimCounter(final String description, final SimulatorInterface<A, R, T> simulator) throws RemoteException
    {
        super(description);
        this.simulator = simulator;
        if (this.simulator.getSimTime().gt(this.simulator.getReplication().getWarmupSimTime()))
        {
            this.initialize();
        }
        else
        {
            this.simulator.addListener(this, ReplicationInterface.WARMUP_EVENT, ReferenceType.STRONG);
        }
        try
        {
            ContextInterface context =
                    ContextUtil.lookupOrCreateSubContext(this.simulator.getReplication().getContext(), "statistics");
            context.bindObject(this);
        }
        catch (NamingException exception)
        {
            this.simulator.getLogger().always().warn(exception, "<init>");
        }
    }

    /**
     * constructs a new SimCounter.
     * @param description String; the description
     * @param simulator SimulatorInterface&lt;A,R,T&gt;; the simulator of this model
     * @param target EventProducerInterface; the target on which to count
     * @param eventType TimedEventType; the EventType for which counting takes place
     * @throws RemoteException on network error for one of the listeners
     */
    public SimCounter(final String description, final SimulatorInterface<A, R, T> simulator,
            final EventProducerInterface target, final TimedEventType eventType) throws RemoteException
    {
        this(description, simulator);
        target.addListener(this, eventType, ReferenceType.STRONG);
    }

    /** {@inheritDoc} */
    @Override
    public void initialize()
    {
        super.initialize();
        fireTimedEvent(TIMED_INITIALIZED_EVENT, this, this.simulator.getSimulatorTime());
    }

    /** {@inheritDoc} */
    @Override
    public long ingest(final long value)
    {
        long result = super.ingest(value);
        fireTimedEvent(TIMED_OBSERVATION_ADDED_EVENT, value, this.simulator.getSimulatorTime());
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public void notify(final EventInterface event)
    {
        if (this.stopped)
        {
            return;
        }
        if (event.getSourceId().equals(this.simulator.getSourceId()))
        {
            if (event.getType().equals(ReplicationInterface.WARMUP_EVENT))
            {
                try
                {
                    this.simulator.removeListener(this, ReplicationInterface.WARMUP_EVENT);
                }
                catch (RemoteException exception)
                {
                    this.simulator.getLogger().always().warn(exception,
                            "problem removing Listener for SimulatorIterface.WARMUP_EVENT");
                }
                super.initialize();
                return;
            }
        }
        else
        {
            super.notify(event);
        }
    }

    /** {@inheritDoc} */
    @Override
    public SimulatorInterface<A, R, T> getSimulator()
    {
        return this.simulator;
    }

    /***********************************************************************************************************/
    /************************************* EASY ACCESS CLASS EXTENSIONS ****************************************/
    /***********************************************************************************************************/

    /** Easy access class SimCounter.TimeDouble. */
    public static class TimeDouble extends SimCounter<Double, Double, SimTimeDouble>
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /**
         * constructs a new SimCounter.
         * @param description String; refers to the description of this counter
         * @param simulator SimulatorInterface.TimeDouble; the simulator
         * @throws RemoteException on network error for one of the listeners
         */
        public TimeDouble(final String description, final SimulatorInterface.TimeDouble simulator) throws RemoteException
        {
            super(description, simulator);
        }

        /**
         * constructs a new SimCounter.
         * @param description String; the description
         * @param simulator SimulatorInterface.TimeDouble; the simulator of this model
         * @param target EventProducerInterface; the target on which to count
         * @param eventType TimedEventType; the EventType for which counting takes place
         * @throws RemoteException on network error for one of the listeners
         */
        public TimeDouble(final String description, final SimulatorInterface.TimeDouble simulator,
                final EventProducerInterface target, final TimedEventType eventType) throws RemoteException
        {
            super(description, simulator, target, eventType);
        }
    }

    /** Easy access class SimCounter.TimeFloat. */
    public static class TimeFloat extends SimCounter<Float, Float, SimTimeFloat>
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /**
         * constructs a new SimCounter.
         * @param description String; refers to the description of this counter
         * @param simulator SimulatorInterface.TimeFloat; the simulator
         * @throws RemoteException on network error for one of the listeners
         */
        public TimeFloat(final String description, final SimulatorInterface.TimeFloat simulator) throws RemoteException
        {
            super(description, simulator);
        }

        /**
         * constructs a new SimCounter.
         * @param description String; the description
         * @param simulator SimulatorInterface.TimeFloat; the simulator of this model
         * @param target EventProducerInterface; the target on which to count
         * @param eventType TimedEventType; the EventType for which counting takes place
         * @throws RemoteException on network error for one of the listeners
         */
        public TimeFloat(final String description, final SimulatorInterface.TimeFloat simulator,
                final EventProducerInterface target, final TimedEventType eventType) throws RemoteException
        {
            super(description, simulator, target, eventType);
        }
    }

    /** Easy access class SimCounter.TimeLong. */
    public static class TimeLong extends SimCounter<Long, Long, SimTimeLong>
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /**
         * constructs a new SimCounter.
         * @param description String; refers to the description of this counter
         * @param simulator SimulatorInterface.TimeLong; the simulator
         * @throws RemoteException on network error for one of the listeners
         */
        public TimeLong(final String description, final SimulatorInterface.TimeLong simulator) throws RemoteException
        {
            super(description, simulator);
        }

        /**
         * constructs a new SimCounter.
         * @param description String; the description
         * @param simulator SimulatorInterface.TimeLong; the simulator of this model
         * @param target EventProducerInterface; the target on which to count
         * @param eventType TimedEventType; the EventType for which counting takes place
         * @throws RemoteException on network error for one of the listeners
         */
        public TimeLong(final String description, final SimulatorInterface.TimeLong simulator,
                final EventProducerInterface target, final TimedEventType eventType) throws RemoteException
        {
            super(description, simulator, target, eventType);
        }
    }

    /** Easy access class SimCounter.TimeDoubleUnit. */
    public static class TimeDoubleUnit extends SimCounter<Time, Duration, SimTimeDoubleUnit>
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /**
         * constructs a new SimCounter.
         * @param description String; refers to the description of this counter
         * @param simulator SimulatorInterface.TimeDoubleUnit; the simulator
         * @throws RemoteException on network error for one of the listeners
         */
        public TimeDoubleUnit(final String description, final SimulatorInterface.TimeDoubleUnit simulator)
                throws RemoteException
        {
            super(description, simulator);
        }

        /**
         * constructs a new SimCounter.
         * @param description String; the description
         * @param simulator SimulatorInterface.TimeDoubleUnit; the simulator of this model
         * @param target EventProducerInterface; the target on which to count
         * @param eventType TimedEventType; the EventType for which counting takes place
         * @throws RemoteException on network error for one of the listeners
         */
        public TimeDoubleUnit(final String description, final SimulatorInterface.TimeDoubleUnit simulator,
                final EventProducerInterface target, final TimedEventType eventType) throws RemoteException
        {
            super(description, simulator, target, eventType);
        }
    }

    /** Easy access class SimCounter.TimeFloatUnit. */
    public static class TimeFloatUnit extends SimCounter<FloatTime, FloatDuration, SimTimeFloatUnit>
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /**
         * constructs a new SimCounter.
         * @param description String; refers to the description of this counter
         * @param simulator SimulatorInterface.TimeFloatUnit; the simulator
         * @throws RemoteException on network error for one of the listeners
         */
        public TimeFloatUnit(final String description, final SimulatorInterface.TimeFloatUnit simulator) throws RemoteException
        {
            super(description, simulator);
        }

        /**
         * constructs a new SimCounter.
         * @param description String; the description
         * @param simulator SimulatorInterface.TimeFloatUnit; the simulator of this model
         * @param target EventProducerInterface; the target on which to count
         * @param eventType TimedEventType; the EventType for which counting takes place
         * @throws RemoteException on network error for one of the listeners
         */
        public TimeFloatUnit(final String description, final SimulatorInterface.TimeFloatUnit simulator,
                final EventProducerInterface target, final TimedEventType eventType) throws RemoteException
        {
            super(description, simulator, target, eventType);
        }
    }

}
