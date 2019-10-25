package nl.tudelft.simulation.dsol.statistics;

import java.rmi.RemoteException;
import java.util.Calendar;

import javax.naming.Context;
import javax.naming.NamingException;

import org.djunits.value.vdouble.scalar.Duration;
import org.djunits.value.vdouble.scalar.Time;
import org.djunits.value.vfloat.scalar.FloatDuration;
import org.djunits.value.vfloat.scalar.FloatTime;

import nl.tudelft.simulation.dsol.logger.SimLogger;
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
import nl.tudelft.simulation.event.Event;
import nl.tudelft.simulation.event.EventInterface;
import nl.tudelft.simulation.event.EventProducerInterface;
import nl.tudelft.simulation.event.EventType;
import nl.tudelft.simulation.jstats.statistics.Tally;
import nl.tudelft.simulation.naming.context.ContextUtil;

/**
 * The time-aware Tally extends the generic tally and links it to the dsol framework.
 * <p>
 * Copyright (c) 2002-2019 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
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
public class SimTally<A extends Comparable<A>, R extends Number & Comparable<R>, T extends SimTime<A, R, T>> extends Tally
{
    /** */
    private static final long serialVersionUID = 20140804L;

    /** the simulator. */
    private SimulatorInterface<A, R, T> simulator = null;

    /** after the END_OF_REPLICATION we stop. */
    private boolean stopped = false;

    /** SAMPLE_MEAN_EVENT is fired whenever the sample mean is updated. */
    public static final EventType TIMED_SAMPLE_MEAN_EVENT = new EventType("TIMED_SAMPLE_MEAN_EVENT");

    /** SAMPLE_VARIANCE_EVENT is fired whenever the sample variance is updated. */
    public static final EventType TIMED_SAMPLE_VARIANCE_EVENT = new EventType("TIMED_SAMPLE_VARIANCE_EVENT");

    /** MIN_EVENT is fired whenever a new minimum value has reached. */
    public static final EventType TIMED_MIN_EVENT = new EventType("TIMED_MIN_EVENT");

    /** MAX_EVENT is fired whenever a new maximum value has reached. */
    public static final EventType TIMED_MAX_EVENT = new EventType("TIMED_MAX_EVENT");

    /** N_EVENT is fired whenever on a change in measurements. */
    public static final EventType TIMED_N_EVENT = new EventType("TIMED_N_EVENT");

    /** STANDARD_DEVIATION_EVENT is fired whenever the standard deviation is updated. */
    public static final EventType TIMED_STANDARD_DEVIATION_EVENT = new EventType("TIMED_STANDARD_DEVIATION_EVENT");

    /** SUM_EVENT is fired whenever the sum is updated. */
    public static final EventType TIMED_SUM_EVENT = new EventType("TIMED_SUM_EVENT");

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
            this.simulator.addListener(this, SimulatorInterface.WARMUP_EVENT, false);
        }
        this.simulator.addListener(this, SimulatorInterface.END_REPLICATION_EVENT, false);
        try
        {
            Context context = ContextUtil.lookup(this.simulator.getReplication().getContext(), "/statistics");
            ContextUtil.bind(context, this);
        }
        catch (NamingException exception)
        {
            SimLogger.always().warn(exception, "<init>");
        }

        // subscribe to the events from the super Tally to send timed events by this simulator aware tally
        super.addListener(this, Tally.MAX_EVENT, true);
        super.addListener(this, Tally.MIN_EVENT, true);
        super.addListener(this, Tally.N_EVENT, true);
        super.addListener(this, Tally.SAMPLE_MEAN_EVENT, true);
        super.addListener(this, Tally.SAMPLE_VARIANCE_EVENT, true);
        super.addListener(this, Tally.STANDARD_DEVIATION_EVENT, true);
        super.addListener(this, Tally.SUM_EVENT, true);
    }

    /**
     * constructs a new SimTally.
     * @param description String; the description of this tally.
     * @param simulator SimulatorInterface&lt;A,R,T&gt;; the simulator to schedule on
     * @param target EventProducerInterface; the target on which to subscribe
     * @param eventType EventType; the eventType for which statistics are sampled
     * @throws RemoteException on network error for one of the listeners
     */
    public SimTally(final String description, final SimulatorInterface<A, R, T> simulator, final EventProducerInterface target,
            final EventType eventType) throws RemoteException
    {
        this(description, simulator);
        target.addListener(this, eventType, false);
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("checkstyle:designforextension")
    public void notify(final EventInterface event)
    {
        if (this.stopped)
        {
            return;
        }
        if (event.getType().equals(MAX_EVENT))
        {
            fireTimedEvent(TIMED_MAX_EVENT, event.getContent(), this.simulator.getSimulatorTime());
            return;
        }
        if (event.getType().equals(MIN_EVENT))
        {
            fireTimedEvent(TIMED_MIN_EVENT, event.getContent(), this.simulator.getSimulatorTime());
            return;
        }
        if (event.getType().equals(N_EVENT))
        {
            fireTimedEvent(TIMED_N_EVENT, event.getContent(), this.simulator.getSimulatorTime());
            return;
        }
        if (event.getType().equals(SAMPLE_MEAN_EVENT))
        {
            fireTimedEvent(TIMED_SAMPLE_MEAN_EVENT, event.getContent(), this.simulator.getSimulatorTime());
            return;
        }
        if (event.getType().equals(SAMPLE_VARIANCE_EVENT))
        {
            fireTimedEvent(TIMED_SAMPLE_VARIANCE_EVENT, event.getContent(), this.simulator.getSimulatorTime());
            return;
        }
        if (event.getType().equals(STANDARD_DEVIATION_EVENT))
        {
            fireTimedEvent(TIMED_STANDARD_DEVIATION_EVENT, event.getContent(), this.simulator.getSimulatorTime());
            return;
        }
        if (event.getType().equals(SUM_EVENT))
        {
            fireTimedEvent(TIMED_SUM_EVENT, event.getContent(), this.simulator.getSimulatorTime());
            return;
        }

        if (event.getSource().equals(this.simulator))
        {
            if (event.getType().equals(SimulatorInterface.WARMUP_EVENT))
            {
                try
                {
                    this.simulator.removeListener(this, SimulatorInterface.WARMUP_EVENT);
                }
                catch (RemoteException exception)
                {
                    SimLogger.always().warn(exception, "problem removing Listener for SimulatorIterface.WARMUP_EVENT");
                }
                super.initialize();
                return;
            }
            if (event.getType().equals(SimulatorInterface.END_REPLICATION_EVENT))
            {
                this.stopped = true;
                try
                {
                    this.simulator.removeListener(this, SimulatorInterface.END_REPLICATION_EVENT);
                }
                catch (RemoteException exception)
                {
                    SimLogger.always().warn(exception,
                            "problem removing Listener for SimulatorIterface.END_OF_REPLICATION_EVENT");
                }
                this.endOfReplication();
                return;
            }
        }
        else if (this.isInitialized())
        {
            super.notify(event);
        }
    }

    /**
     * endOfReplication is invoked to store the final results. A special Tally is created in the Context to tally the average
     * results of all replications. Herewith the confidence interval of the means over the different replications can be
     * calculated.
     */
    @SuppressWarnings("checkstyle:designforextension")
    protected void endOfReplication()
    {
        try
        {
            Context context = this.simulator.getReplication().getTreatment().getExperiment().getContext();
            context = ContextUtil.lookup(context, "average");
            context = ContextUtil.lookup(context, "statistics");
            Tally tally = null;
            try
            {
                tally = (Tally) context.lookup(this.description);
            }
            catch (NamingException exception)
            {
                tally = new Tally(this.description);
                context.bind(this.description, tally);
                tally.initialize();
            }
            tally.notify(new Event(null, this, new Double(this.sampleMean)));
        }
        catch (Exception exception)
        {
            SimLogger.always().warn(exception, "endOfReplication");
        }
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
         * @param eventType EventType; the eventType for which statistics are sampled
         * @throws RemoteException on network error for one of the listeners
         */
        public TimeDouble(final String description, final SimulatorInterface.TimeDouble simulator,
                final EventProducerInterface target, final EventType eventType) throws RemoteException
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
         * @param eventType EventType; the eventType for which statistics are sampled
         * @throws RemoteException on network error for one of the listeners
         */
        public TimeFloat(final String description, final SimulatorInterface.TimeFloat simulator,
                final EventProducerInterface target, final EventType eventType) throws RemoteException
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
         * @param eventType EventType; the eventType for which statistics are sampled
         * @throws RemoteException on network error for one of the listeners
         */
        public TimeLong(final String description, final SimulatorInterface.TimeLong simulator,
                final EventProducerInterface target, final EventType eventType) throws RemoteException
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
         * @param eventType EventType; the eventType for which statistics are sampled
         * @throws RemoteException on network error for one of the listeners
         */
        public TimeDoubleUnit(final String description, final SimulatorInterface.TimeDoubleUnit simulator,
                final EventProducerInterface target, final EventType eventType) throws RemoteException
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
         * @param eventType EventType; the eventType for which statistics are sampled
         * @throws RemoteException on network error for one of the listeners
         */
        public TimeFloatUnit(final String description, final SimulatorInterface.TimeFloatUnit simulator,
                final EventProducerInterface target, final EventType eventType) throws RemoteException
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
         * @param eventType EventType; the eventType for which statistics are sampled
         * @throws RemoteException on network error for one of the listeners
         */
        public CalendarDouble(final String description, final SimulatorInterface.CalendarDouble simulator,
                final EventProducerInterface target, final EventType eventType) throws RemoteException
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
         * @param eventType EventType; the eventType for which statistics are sampled
         * @throws RemoteException on network error for one of the listeners
         */
        public CalendarFloat(final String description, final SimulatorInterface.CalendarFloat simulator,
                final EventProducerInterface target, final EventType eventType) throws RemoteException
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
         * @param eventType EventType; the eventType for which statistics are sampled
         * @throws RemoteException on network error for one of the listeners
         */
        public CalendarLong(final String description, final SimulatorInterface.CalendarLong simulator,
                final EventProducerInterface target, final EventType eventType) throws RemoteException
        {
            super(description, simulator, target, eventType);
        }
    }

}
