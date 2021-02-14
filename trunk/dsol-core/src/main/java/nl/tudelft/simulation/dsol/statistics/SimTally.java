package nl.tudelft.simulation.dsol.statistics;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.Calendar;

import javax.naming.NamingException;

import org.djunits.value.vdouble.scalar.Duration;
import org.djunits.value.vdouble.scalar.Time;
import org.djunits.value.vfloat.scalar.FloatDuration;
import org.djunits.value.vfloat.scalar.FloatTime;
import org.djutils.event.Event;
import org.djutils.event.EventInterface;
import org.djutils.event.EventProducerInterface;
import org.djutils.event.TimedEvent;
import org.djutils.event.TimedEventType;
import org.djutils.event.ref.ReferenceType;
import org.djutils.metadata.MetaData;
import org.djutils.metadata.ObjectDescriptor;
import org.djutils.stats.summarizers.event.EventBasedTally;

import nl.tudelft.simulation.dsol.experiment.Replication;
import nl.tudelft.simulation.dsol.simtime.SimTime;
import nl.tudelft.simulation.dsol.simtime.SimTimeCalendarDouble;
import nl.tudelft.simulation.dsol.simtime.SimTimeCalendarFloat;
import nl.tudelft.simulation.dsol.simtime.SimTimeCalendarLong;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simtime.SimTimeDoubleUnit;
import nl.tudelft.simulation.dsol.simtime.SimTimeFloat;
import nl.tudelft.simulation.dsol.simtime.SimTimeFloatUnit;
import nl.tudelft.simulation.dsol.simtime.SimTimeLong;
import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;
import nl.tudelft.simulation.naming.context.ContextInterface;
import nl.tudelft.simulation.naming.context.util.ContextUtil;

/**
 * The simulator aware Tally extends the djutils event-based tally and links it to the dsol framework.
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
 * @param <T> the absolute simulation time to use in the warmup event
 */
public class SimTally<A extends Comparable<A> & Serializable, R extends Number & Comparable<R>, T extends SimTime<A, R, T>>
        extends EventBasedTally implements StatisticsInterface<A, R, T>
{
    /** */
    private static final long serialVersionUID = 20140804L;

    /** the simulator. */
    private final SimulatorInterface<A, R, T> simulator;

    /** OBSERVATION_ADDED_EVENT is fired whenever an observation is processed. */
    public static final TimedEventType TIMED_OBSERVATION_ADDED_EVENT =
            new TimedEventType(new MetaData("TIMED_OBSERVATION_ADDED_EVENT", "observation added to Tally",
                    new ObjectDescriptor("value", "Observation value", Double.class)));

    /** INITIALIZED_EVENT is fired whenever a Tally is (re-)initialized. */
    public static final TimedEventType TIMED_INITIALIZED_EVENT = new TimedEventType(new MetaData("TIMED_INITIALIZED_EVENT",
            "Tally initialized", new ObjectDescriptor("simTally", "Tally object", SimTally.class)));

    /**
     * constructs a new SimTally.
     * @param description String; refers to the description of this Tally.
     * @param simulator SimulatorInterface&lt;A,R,T&gt;; the simulator to schedule on.
     * @throws RemoteException on network error for one of the listeners
     */
    public SimTally(final String description, final SimulatorInterface<A, R, T> simulator) throws RemoteException
    {
        super(description);
        this.simulator = simulator;
        if (this.simulator.getSimTime().gt(this.simulator.getReplication().getTreatment().getWarmupSimTime()))
        {
            this.initialize();
        }
        else
        {
            this.simulator.addListener(this, Replication.WARMUP_EVENT, ReferenceType.STRONG);
        }
        this.simulator.addListener(this, Replication.END_REPLICATION_EVENT, ReferenceType.STRONG);
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
     * constructs a new SimTally based on an eventType for which statistics are sampled.
     * @param description String; the description of this tally.
     * @param simulator SimulatorInterface&lt;A,R,T&gt;; the simulator to schedule on
     * @param target EventProducerInterface; the target on which to subscribe
     * @param eventType TimedEventType; the eventType for which statistics are sampled
     * @throws RemoteException on network error for one of the listeners
     */
    public SimTally(final String description, final SimulatorInterface<A, R, T> simulator, final EventProducerInterface target,
            final TimedEventType eventType) throws RemoteException
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
    @SuppressWarnings({"checkstyle:designforextension", "unchecked"})
    public void notify(final EventInterface event)
    {
        if (event.getSourceId().equals(this.simulator.getSourceId()))
        {
            if (event.getType().equals(Replication.WARMUP_EVENT))
            {
                try
                {
                    this.simulator.removeListener(this, Replication.WARMUP_EVENT);
                }
                catch (RemoteException exception)
                {
                    this.simulator.getLogger().always().warn(exception,
                            "problem removing Listener for SimulatorIterface.WARMUP_EVENT");
                }
                initialize();
                return;
            }
            if (event.getType().equals(Replication.END_REPLICATION_EVENT))
            {
                try
                {
                    this.simulator.removeListener(this, Replication.END_REPLICATION_EVENT);
                }
                catch (RemoteException exception)
                {
                    this.simulator.getLogger().always().warn(exception,
                            "problem removing Listener for SimulatorIterface.END_OF_REPLICATION_EVENT");
                }
                this.endOfReplication();
                return;
            }
        }
        else if (event instanceof TimedEvent<?>)
        {
            TimedEvent<?> timedEvent = (TimedEvent<?>) event;
            if (timedEvent.getTimeStamp() instanceof SimTime)
            {
                // Tally can handle Number (and therefore also Time and Duration) and Calendar but not SimTime
                super.notify(new TimedEvent<A>(timedEvent.getType(), timedEvent.getSourceId(), timedEvent.getContent(),
                        ((SimTime<A, R, T>) timedEvent.getTimeStamp()).get()));
            }
            else
            {
                super.notify(event);
            }
        }
        else
        {
            this.simulator.getLogger().always().warn("SimPersistent: event not a TimedEvent");
        }
    }

    /** {@inheritDoc} */
    @Override
    public double ingest(final double value)
    {
        super.ingest(value);
        fireTimedEvent(TIMED_OBSERVATION_ADDED_EVENT, value, this.simulator.getSimulatorTime());
        return value;
    }

    /**
     * endOfReplication is invoked to store summary statistics. A special Tally is created in the Context of the experiment to
     * tally the average results of all replications. Herewith the confidence interval of the means over the different
     * replications can be calculated.
     */
    protected void endOfReplication()
    {
        try
        {
            ContextInterface context = ContextUtil
                    .lookupOrCreateSubContext(this.simulator.getReplication().getExperiment().getContext(), "statistics");
            EventBasedTally experimentTally;
            if (context.hasKey(getDescription()))
            {
                experimentTally = (EventBasedTally) context.getObject(getDescription());
            }
            else
            {
                experimentTally = new EventBasedTally(getDescription());
                context.bindObject(getDescription(), experimentTally);
                experimentTally.initialize();
            }
            experimentTally.notify(new Event(null, getSourceId(), Double.valueOf(getSampleMean())));

            // TODO: make summary statistics for all statistics values
        }
        catch (Exception exception)
        {
            this.simulator.getLogger().always().warn(exception, "endOfReplication");
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

    /** Easy access class SimTally.TimeDouble. */
    public static class TimeDouble extends SimTally<Double, Double, SimTimeDouble>
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /**
         * constructs a new SimTally.
         * @param description String; refers to the description of this counter
         * @param simulator SimulatorInterface.TimeDouble; the simulator
         * @throws RemoteException on network error for one of the listeners
         */
        public TimeDouble(final String description, final SimulatorInterface.TimeDouble simulator) throws RemoteException
        {
            super(description, simulator);
        }

        /**
         * constructs a new SimTally.
         * @param description String; the description of this tally.
         * @param simulator SimulatorInterface.TimeDouble; the simulator to schedule on
         * @param target EventProducerInterface; the target on which to subscribe
         * @param eventType TimedEventType; the eventType for which statistics are sampled
         * @throws RemoteException on network error for one of the listeners
         */
        public TimeDouble(final String description, final SimulatorInterface.TimeDouble simulator,
                final EventProducerInterface target, final TimedEventType eventType) throws RemoteException
        {
            super(description, simulator, target, eventType);
        }
    }

    /** Easy access class SimTally.TimeFloat. */
    public static class TimeFloat extends SimTally<Float, Float, SimTimeFloat>
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /**
         * constructs a new SimTally.
         * @param description String; refers to the description of this counter
         * @param simulator SimulatorInterface.TimeFloat; the simulator
         * @throws RemoteException on network error for one of the listeners
         */
        public TimeFloat(final String description, final SimulatorInterface.TimeFloat simulator) throws RemoteException
        {
            super(description, simulator);
        }

        /**
         * constructs a new SimTally.
         * @param description String; the description of this tally.
         * @param simulator SimulatorInterface.TimeFloat; the simulator to schedule on
         * @param target EventProducerInterface; the target on which to subscribe
         * @param eventType TimedEventType; the eventType for which statistics are sampled
         * @throws RemoteException on network error for one of the listeners
         */
        public TimeFloat(final String description, final SimulatorInterface.TimeFloat simulator,
                final EventProducerInterface target, final TimedEventType eventType) throws RemoteException
        {
            super(description, simulator, target, eventType);
        }
    }

    /** Easy access class SimTally.TimeLong. */
    public static class TimeLong extends SimTally<Long, Long, SimTimeLong>
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /**
         * constructs a new SimTally.
         * @param description String; refers to the description of this counter
         * @param simulator SimulatorInterface.TimeLong; the simulator
         * @throws RemoteException on network error for one of the listeners
         */
        public TimeLong(final String description, final SimulatorInterface.TimeLong simulator) throws RemoteException
        {
            super(description, simulator);
        }

        /**
         * constructs a new SimTally.
         * @param description String; the description of this tally.
         * @param simulator SimulatorInterface.TimeLong; the simulator to schedule on
         * @param target EventProducerInterface; the target on which to subscribe
         * @param eventType TimedEventType; the eventType for which statistics are sampled
         * @throws RemoteException on network error for one of the listeners
         */
        public TimeLong(final String description, final SimulatorInterface.TimeLong simulator,
                final EventProducerInterface target, final TimedEventType eventType) throws RemoteException
        {
            super(description, simulator, target, eventType);
        }
    }

    /** Easy access class SimTally.TimeDoubleUnit. */
    public static class TimeDoubleUnit extends SimTally<Time, Duration, SimTimeDoubleUnit>
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /**
         * constructs a new SimTally.
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
         * constructs a new SimTally.
         * @param description String; the description of this tally.
         * @param simulator SimulatorInterface.TimeDoubleUnit; the simulator to schedule on
         * @param target EventProducerInterface; the target on which to subscribe
         * @param eventType TimedEventType; the eventType for which statistics are sampled
         * @throws RemoteException on network error for one of the listeners
         */
        public TimeDoubleUnit(final String description, final SimulatorInterface.TimeDoubleUnit simulator,
                final EventProducerInterface target, final TimedEventType eventType) throws RemoteException
        {
            super(description, simulator, target, eventType);
        }
    }

    /** Easy access class SimTally.TimeFloatUnit. */
    public static class TimeFloatUnit extends SimTally<FloatTime, FloatDuration, SimTimeFloatUnit>
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /**
         * constructs a new SimTally.
         * @param description String; refers to the description of this counter
         * @param simulator SimulatorInterface.TimeFloatUnit; the simulator
         * @throws RemoteException on network error for one of the listeners
         */
        public TimeFloatUnit(final String description, final SimulatorInterface.TimeFloatUnit simulator) throws RemoteException
        {
            super(description, simulator);
        }

        /**
         * constructs a new SimTally.
         * @param description String; the description of this tally.
         * @param simulator SimulatorInterface.TimeFloatUnit; the simulator to schedule on
         * @param target EventProducerInterface; the target on which to subscribe
         * @param eventType TimedEventType; the eventType for which statistics are sampled
         * @throws RemoteException on network error for one of the listeners
         */
        public TimeFloatUnit(final String description, final SimulatorInterface.TimeFloatUnit simulator,
                final EventProducerInterface target, final TimedEventType eventType) throws RemoteException
        {
            super(description, simulator, target, eventType);
        }
    }

    /** Easy access class SimTally.CalendarDouble. */
    public static class CalendarDouble extends SimTally<Calendar, Duration, SimTimeCalendarDouble>
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /**
         * constructs a new SimTally.
         * @param description String; refers to the description of this counter
         * @param simulator SimulatorInterface.CalendarDouble; the simulator
         * @throws RemoteException on network error for one of the listeners
         */
        public CalendarDouble(final String description, final SimulatorInterface.CalendarDouble simulator)
                throws RemoteException
        {
            super(description, simulator);
        }

        /**
         * constructs a new SimTally.
         * @param description String; the description of this tally.
         * @param simulator SimulatorInterface.CalendarDouble; the simulator to schedule on
         * @param target EventProducerInterface; the target on which to subscribe
         * @param eventType TimedEventType; the eventType for which statistics are sampled
         * @throws RemoteException on network error for one of the listeners
         */
        public CalendarDouble(final String description, final SimulatorInterface.CalendarDouble simulator,
                final EventProducerInterface target, final TimedEventType eventType) throws RemoteException
        {
            super(description, simulator, target, eventType);
        }
    }

    /** Easy access class SimTally.CalendarFloat. */
    public static class CalendarFloat extends SimTally<Calendar, FloatDuration, SimTimeCalendarFloat>
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /**
         * constructs a new SimTally.
         * @param description String; refers to the description of this counter
         * @param simulator SimulatorInterface.CalendarFloat; the simulator
         * @throws RemoteException on network error for one of the listeners
         */
        public CalendarFloat(final String description, final SimulatorInterface.CalendarFloat simulator) throws RemoteException
        {
            super(description, simulator);
        }

        /**
         * constructs a new SimTally.
         * @param description String; the description of this tally.
         * @param simulator SimulatorInterface.CalendarFloat; the simulator to schedule on
         * @param target EventProducerInterface; the target on which to subscribe
         * @param eventType TimedEventType; the eventType for which statistics are sampled
         * @throws RemoteException on network error for one of the listeners
         */
        public CalendarFloat(final String description, final SimulatorInterface.CalendarFloat simulator,
                final EventProducerInterface target, final TimedEventType eventType) throws RemoteException
        {
            super(description, simulator, target, eventType);
        }
    }

    /** Easy access class SimTally.CalendarLong. */
    public static class CalendarLong extends SimTally<Calendar, Long, SimTimeCalendarLong>
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /**
         * constructs a new SimTally.
         * @param description String; refers to the description of this counter
         * @param simulator SimulatorInterface.CalendarLong; the simulator
         * @throws RemoteException on network error for one of the listeners
         */
        public CalendarLong(final String description, final SimulatorInterface.CalendarLong simulator) throws RemoteException
        {
            super(description, simulator);
        }

        /**
         * constructs a new SimTally.
         * @param description String; the description of this tally.
         * @param simulator SimulatorInterface.CalendarLong; the simulator to schedule on
         * @param target EventProducerInterface; the target on which to subscribe
         * @param eventType TimedEventType; the eventType for which statistics are sampled
         * @throws RemoteException on network error for one of the listeners
         */
        public CalendarLong(final String description, final SimulatorInterface.CalendarLong simulator,
                final EventProducerInterface target, final TimedEventType eventType) throws RemoteException
        {
            super(description, simulator, target, eventType);
        }
    }

}
