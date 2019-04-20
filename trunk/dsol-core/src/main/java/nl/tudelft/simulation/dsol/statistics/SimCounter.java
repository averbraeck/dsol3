package nl.tudelft.simulation.dsol.statistics;

import java.rmi.RemoteException;
import java.util.Calendar;

import javax.naming.Context;
import javax.naming.InitialContext;
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
import nl.tudelft.simulation.jstats.statistics.Counter;
import nl.tudelft.simulation.jstats.statistics.Tally;
import nl.tudelft.simulation.naming.context.ContextUtil;

/**
 * The time-aware counter extends the generic counter and links it to the dsol framework.
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
public class SimCounter<A extends Comparable<A>, R extends Number & Comparable<R>, T extends SimTime<A, R, T>> extends Counter
{
    /** */
    private static final long serialVersionUID = 20140804L;

    /** the simulator to subscribe to and from. */
    private SimulatorInterface<A, R, T> simulator = null;

    /** we stopped the counter. */
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
    }

    /**
     * constructs a new SimCounter.
     * @param description String; the description
     * @param simulator SimulatorInterface&lt;A,R,T&gt;; the simulator of this model
     * @param target EventProducerInterface; the target on which to count
     * @param eventType EventType; the EventType for which counting takes place
     * @throws RemoteException on network error for one of the listeners
     */
    public SimCounter(final String description, final SimulatorInterface<A, R, T> simulator,
            final EventProducerInterface target, final EventType eventType) throws RemoteException
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
     * endOfReplication is invoked to store the final results. A special Tally is created in the Context to tally the counters
     * of all replications. Herewith the confidence interval of the average counter results over the different replications can
     * be calculated.
     */
    @SuppressWarnings("checkstyle:designforextension")
    protected void endOfReplication()
    {
        try
        {
            String[] parts = nl.tudelft.simulation.naming.context.ContextUtil.resolveKey(this).split("/");
            String key = "";
            for (int i = 0; i < parts.length; i++)
            {
                if (i != parts.length - 2)
                {
                    key = key + parts[i] + "/";
                }
            }
            key = key.substring(0, key.length() - 1);
            Tally tally = null;
            try
            {
                tally = (Tally) new InitialContext().lookup(key);
            }
            catch (NamingException exception)
            {
                tally = new Tally(this.description);
                new InitialContext().bind(key, tally);
                tally.initialize();
            }
            tally.notify(new Event(null, this, new Long(this.count)));
        }
        catch (Exception exception)
        {
            SimLogger.always().warn(exception, "endOfReplication");
        }
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
         * @param eventType EventType; the EventType for which counting takes place
         * @throws RemoteException on network error for one of the listeners
         */
        public TimeDouble(final String description, final SimulatorInterface.TimeDouble simulator,
                final EventProducerInterface target, final EventType eventType) throws RemoteException
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
         * @param eventType EventType; the EventType for which counting takes place
         * @throws RemoteException on network error for one of the listeners
         */
        public TimeFloat(final String description, final SimulatorInterface.TimeFloat simulator,
                final EventProducerInterface target, final EventType eventType) throws RemoteException
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
         * @param eventType EventType; the EventType for which counting takes place
         * @throws RemoteException on network error for one of the listeners
         */
        public TimeLong(final String description, final SimulatorInterface.TimeLong simulator,
                final EventProducerInterface target, final EventType eventType) throws RemoteException
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
         * @param eventType EventType; the EventType for which counting takes place
         * @throws RemoteException on network error for one of the listeners
         */
        public TimeDoubleUnit(final String description, final SimulatorInterface.TimeDoubleUnit simulator,
                final EventProducerInterface target, final EventType eventType) throws RemoteException
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
         * @param eventType EventType; the EventType for which counting takes place
         * @throws RemoteException on network error for one of the listeners
         */
        public TimeFloatUnit(final String description, final SimulatorInterface.TimeFloatUnit simulator,
                final EventProducerInterface target, final EventType eventType) throws RemoteException
        {
            super(description, simulator, target, eventType);
        }
    }

    /** Easy access class SimCounter.CalendarDouble. */
    public static class CalendarDouble extends SimCounter<Calendar, Duration, SimTimeCalendarDouble>
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /**
         * constructs a new SimCounter.
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
         * constructs a new SimCounter.
         * @param description String; the description
         * @param simulator SimulatorInterface.CalendarDouble; the simulator of this model
         * @param target EventProducerInterface; the target on which to count
         * @param eventType EventType; the EventType for which counting takes place
         * @throws RemoteException on network error for one of the listeners
         */
        public CalendarDouble(final String description, final SimulatorInterface.CalendarDouble simulator,
                final EventProducerInterface target, final EventType eventType) throws RemoteException
        {
            super(description, simulator, target, eventType);
        }
    }

    /** Easy access class SimCounter.CalendarFloat. */
    public static class CalendarFloat extends SimCounter<Calendar, FloatDuration, SimTimeCalendarFloat>
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /**
         * constructs a new SimCounter.
         * @param description String; refers to the description of this counter
         * @param simulator SimulatorInterface.CalendarFloat; the simulator
         * @throws RemoteException on network error for one of the listeners
         */
        public CalendarFloat(final String description, final SimulatorInterface.CalendarFloat simulator) throws RemoteException
        {
            super(description, simulator);
        }

        /**
         * constructs a new SimCounter.
         * @param description String; the description
         * @param simulator SimulatorInterface.CalendarFloat; the simulator of this model
         * @param target EventProducerInterface; the target on which to count
         * @param eventType EventType; the EventType for which counting takes place
         * @throws RemoteException on network error for one of the listeners
         */
        public CalendarFloat(final String description, final SimulatorInterface.CalendarFloat simulator,
                final EventProducerInterface target, final EventType eventType) throws RemoteException
        {
            super(description, simulator, target, eventType);
        }
    }

    /** Easy access class SimCounter.CalendarLong. */
    public static class CalendarLong extends SimCounter<Calendar, Long, SimTimeCalendarLong>
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /**
         * constructs a new SimCounter.
         * @param description String; refers to the description of this counter
         * @param simulator SimulatorInterface.CalendarLong; the simulator
         * @throws RemoteException on network error for one of the listeners
         */
        public CalendarLong(final String description, final SimulatorInterface.CalendarLong simulator) throws RemoteException
        {
            super(description, simulator);
        }

        /**
         * constructs a new SimCounter.
         * @param description String; the description
         * @param simulator SimulatorInterface.CalendarLong; the simulator of this model
         * @param target EventProducerInterface; the target on which to count
         * @param eventType EventType; the EventType for which counting takes place
         * @throws RemoteException on network error for one of the listeners
         */
        public CalendarLong(final String description, final SimulatorInterface.CalendarLong simulator,
                final EventProducerInterface target, final EventType eventType) throws RemoteException
        {
            super(description, simulator, target, eventType);
        }
    }

}
