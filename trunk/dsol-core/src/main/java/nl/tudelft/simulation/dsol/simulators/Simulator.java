package nl.tudelft.simulation.dsol.simulators;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Calendar;
import java.util.concurrent.atomic.AtomicBoolean;

import org.djunits.value.vdouble.scalar.Duration;
import org.djunits.value.vdouble.scalar.Time;
import org.djunits.value.vfloat.scalar.FloatDuration;
import org.djunits.value.vfloat.scalar.FloatTime;
import org.djutils.event.EventProducer;
import org.djutils.logger.CategoryLogger;
import org.pmw.tinylog.Logger;

import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.experiment.Replication;
import nl.tudelft.simulation.dsol.experiment.ReplicationMode;
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
import nl.tudelft.simulation.dsol.statistics.StatisticsInterface;

/**
 * The Simulator class is an abstract implementation of the SimulatorInterface.
 * <p>
 * Copyright (c) 2002-2020 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @param <A> the absolute storage type for the simulation time, e.g. Calendar, Duration, or Double.
 * @param <R> the relative type for time storage, e.g. Long for the Calendar. For most non-calendar types, the absolute and
 *            relative types are the same.
 * @param <T> the extended type itself to be able to implement a comparator on the simulation time.
 * @since 1.5
 */
public abstract class Simulator<A extends Comparable<A> & Serializable, R extends Number & Comparable<R>,
        T extends SimTime<A, R, T>> extends EventProducer implements SimulatorInterface<A, R, T>, Runnable
{
    /** */
    private static final long serialVersionUID = 20140805L;

    /** simulatorTime represents the simulationTime. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected T simulatorTime;

    /**
     * stoppingState indicates whether the simulator has prepared for a stop. The simulator might keep running for a while after
     * the stop has been announced, especially in real-time simulators where sleeps can occur in the run thread. The method
     * isRunning() indicates whether the simulation is in the running state or not.
     */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected transient boolean stoppingState = true;

    /**
     * stepState indicates whether the simulator is executing an event in step mode. The step() method is typically NOT executed
     * via the WorkerThread and is therefore registered separately.
     */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected transient boolean stepState = false;

    /** replication represents the currently active replication. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected Replication<A, R, T, ? extends SimulatorInterface<A, R, T>> replication = null;

    /** a worker. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected transient SimulatorWorkerThread worker = null;

    /** the simulatorSemaphore. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected transient Object semaphore = new Object();

    /** the logger. */
    private transient SimLogger logger;

    /** the simulator id. */
    private Serializable id;

    /**
     * Constructs a new Simulator.
     * @param id the id of the simulator, used in logging and firing of events.
     */
    public Simulator(final Serializable id)
    {
        this.id = id;
        this.worker = new SimulatorWorkerThread(this.id.toString(), this);
        this.logger = new SimLogger(this);
    }

    /** {@inheritDoc} */
    @Override
    public final A getSimulatorTime()
    {
        return this.simulatorTime == null ? null : this.simulatorTime.get();
    }

    /** {@inheritDoc} */
    @Override
    public T getSimTime()
    {
        return this.simulatorTime;
    }

    /** {@inheritDoc} */
    @Override
    public Replication<A, R, T, ? extends SimulatorInterface<A, R, T>> getReplication()
    {
        return this.replication;
    }

    /** {@inheritDoc} */
    @Override
    public SimLogger getLogger()
    {
        return this.logger;
    }

    /** {@inheritDoc} */
    @Override
    public Serializable getSourceId()
    {
        return this.id;
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("checkstyle:designforextension")
    public void initialize(final Replication<A, R, T, ? extends SimulatorInterface<A, R, T>> initReplication,
            final ReplicationMode replicationMode) throws SimRuntimeException
    {
        if (initReplication == null)
        {
            throw new IllegalArgumentException("replication == null ?");
        }
        if (this.isRunning())
        {
            throw new SimRuntimeException("Cannot initialize a running simulator");
        }
        synchronized (this.semaphore)
        {
            this.removeAllListeners(StatisticsInterface.class);
            this.replication = initReplication;
            this.simulatorTime = initReplication.getTreatment().getStartSimTime().copy();
            this.fireTimedEvent(SimulatorInterface.START_REPLICATION_EVENT, null, this.simulatorTime.get());
            this.fireTimedEvent(SimulatorInterface.TIME_CHANGED_EVENT, null, this.simulatorTime.get());
            this.replication.getTreatment().getExperiment().getModel().constructModel();
        }
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("checkstyle:designforextension")
    public void endReplication()
    {
        this.stoppingState = true;
        this.stepState = false;
        // active wait till the run has completely ended
        while (isRunning())
        {
            try
            {
                Thread.sleep(1); // looks like a deadlock...
            }
            catch (InterruptedException exception)
            {
                // ignore exception
            }
        }
        this.fireTimedEvent(SimulatorInterface.STOP_EVENT, null, this.simulatorTime.get());
        this.fireTimedEvent(SimulatorInterface.END_REPLICATION_EVENT, null, this.simulatorTime.get());

        if (this.simulatorTime.lt(this.getReplication().getTreatment().getEndSimTime()))
        {
            Logger.warn("The simulator executes the endReplication method, but the simulation time " + this.simulatorTime.get()
                    + " is earlier than the replication length " + this.getReplication().getTreatment().getEndSimTime());
            this.simulatorTime = this.getReplication().getTreatment().getEndSimTime().copy();
        }
    }

    /** {@inheritDoc} */
    @Override
    public final boolean isRunning()
    {
        synchronized (this.semaphore)
        {
            return this.worker.isRunning() || this.stepState;
        }
    }

    /**
     * The run method defines the actual time step mechanism of the simulator. The implementation of this method depends on the
     * formalism. Where discrete event formalisms loop over an event list, continuous simulators take predefined time steps.
     */
    @Override
    public abstract void run();

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("checkstyle:designforextension")
    public void start(final boolean fireStartEvent) throws SimRuntimeException
    {
        System.out.println("Simulator.start: called");
        if (!this.stoppingState)
        {
            throw new SimRuntimeException("Cannot start a running simulator");
        }
        if (this.replication == null)
        {
            throw new SimRuntimeException("Cannot start a simulator without replication details");
        }
        if (this.simulatorTime.ge(this.replication.getTreatment().getEndSimTime()))
        {
            throw new SimRuntimeException("Cannot start simulator : simulatorTime = runLength");
        }
        synchronized (this.semaphore)
        {
            this.stoppingState = false;
            if (fireStartEvent)
            {
                this.fireTimedEvent(SimulatorInterface.START_EVENT, null, this.simulatorTime.get());
            }
            this.fireTimedEvent(SimulatorInterface.TIME_CHANGED_EVENT, this.simulatorTime, this.simulatorTime.get());

            if (!Thread.currentThread().getName().equals(this.worker.getName()))
            {
                System.out.println("Simulator.start: worker.interrupt()");
                this.worker.interrupt();
            }
            else
            {
                System.out.println("Simulator.start: run()");
                run();
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("checkstyle:designforextension")
    public final void start() throws SimRuntimeException
    {
        start(true);
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("checkstyle:designforextension")
    public void step(final boolean fireStepEvent) throws SimRuntimeException
    {
        // XXX: fire TimeChangedEvent?
        if (this.isRunning())
        {
            throw new SimRuntimeException("Cannot step a running simulator");
        }
        if (this.replication == null)
        {
            throw new SimRuntimeException("Cannot step a simulator without replication details");
        }
        if (this.simulatorTime.ge(this.replication.getTreatment().getEndSimTime()))
        {
            throw new SimRuntimeException("Cannot step simulator: SimulatorTime = runControl.runLength");
        }
        if (fireStepEvent)
        {
            this.fireTimedEvent(SimulatorInterface.STEP_EVENT, null, this.simulatorTime.get());
        }
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("checkstyle:designforextension")
    public final void step() throws SimRuntimeException
    {
        step(true);
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("checkstyle:designforextension")
    public void stop(final boolean fireStopEvent) throws SimRuntimeException
    {
        if (!this.isRunning())
        {
            throw new SimRuntimeException("Cannot stop an already stopped simulator");
        }
        this.stoppingState = true;
        if (fireStopEvent)
        {
            this.fireTimedEvent(SimulatorInterface.STOP_EVENT, null, this.simulatorTime.get());
        }
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("checkstyle:designforextension")
    public final void stop() throws SimRuntimeException
    {
        stop(true);
    }

    /**
     * Clean up the simulator. Remove the worker thread.
     */
    public final void cleanUp()
    {
        this.stoppingState = true;
        this.stepState = false;
        if (hasListeners())
        {
            this.removeAllListeners();
        }
        if (this.worker != null)
        {
            this.worker.cleanUp();
        }
        this.worker = null;
    }

    /**
     * writes a serializable method to stream.
     * @param out ObjectOutputStream; the outputstream
     * @throws IOException on IOException
     */
    private synchronized void writeObject(final ObjectOutputStream out) throws IOException
    {
        out.writeObject(this.id);
        out.writeObject(this.simulatorTime);
        out.writeObject(this.replication);
    }

    /**
     * reads a serializable method from stream.
     * @param in java.io.ObjectInputStream; the inputstream
     * @throws IOException on IOException
     */
    @SuppressWarnings("unchecked")
    private synchronized void readObject(final java.io.ObjectInputStream in) throws IOException
    {
        try
        {
            this.id = (Serializable) in.readObject();
            this.simulatorTime = (T) in.readObject();
            this.replication = (Replication<A, R, T, ? extends SimulatorInterface<A, R, T>>) in.readObject();
            this.stoppingState = true;
            this.stepState = false;
            this.semaphore = new Object();
            this.worker = new SimulatorWorkerThread(this.id.toString(), this);
            this.logger = new SimLogger(this);
        }
        catch (Exception exception)
        {
            throw new IOException(exception.getMessage());
        }
    }

    /** The worker thread to execute the run() method of the Simulator and to start/stop the simulation. */
    protected static class SimulatorWorkerThread extends Thread
    {
        /** the job to execute. */
        private Simulator<?, ?, ?> job = null;

        /** finalized. */
        private boolean finalized = false;

        /** running. */
        private AtomicBoolean running = new AtomicBoolean(false);

        /**
         * constructs a new SimulatorRunThread.
         * @param name String; the name of the thread
         * @param job Runnable; the job to run
         */
        protected SimulatorWorkerThread(final String name, final Simulator<?, ?, ?> job)
        {
            super(name);
            this.job = job;
            this.setDaemon(false);
            this.setPriority(Thread.NORM_PRIORITY);
            this.start();
        }

        /**
         * Clean up the worker thread. synchronized method, otherwise it does not own the Monitor on the wait.
         */
        public final synchronized void cleanUp()
        {
            this.running.set(false);
            this.finalized = true;
            if (!this.isInterrupted())
            {
                this.notify(); // in case it is in the 'wait' state
            }
            this.job = null;
        }

        /**
         * @return whether the run method of the job is running or not
         */
        public final synchronized boolean isRunning()
        {
            return this.running.get();
        }

        /** {@inheritDoc} */
        @Override
        public final synchronized void run()
        {
            while (!this.finalized) // always until finalized
            {
                try
                {
                    System.out.println("WT Start wait");
                    this.wait(); // as long as possible
                    System.out.println("WT Stop wait");
                }
                catch (InterruptedException interruptedException)
                {
                    if (!this.finalized)
                    {
                        this.interrupt(); // set the status to interrupted
                        try
                        {
                            this.running.set(true);
                            System.out.println("WT Start running");
                            this.job.run();
                            System.out.println("WT Stop running");
                            this.running.set(false);
                        }
                        catch (Exception exception)
                        {
                            CategoryLogger.always().error(exception);
                        }
                        Thread.interrupted();
                    }
                }
            }
        }
    }

    /***********************************************************************************************************/
    /************************************* EASY ACCESS CLASS EXTENSIONS ****************************************/
    /***********************************************************************************************************/

    /** Easy access class Simulator.TimeDouble. */
    public abstract static class TimeDouble extends Simulator<Double, Double, SimTimeDouble>
            implements SimulatorInterface.TimeDouble
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /**
         * Constructs a new Simulator.TimeDouble.
         * @param id the id of the simulator, used in logging and firing of events.
         */
        public TimeDouble(final Serializable id)
        {
            super(id);
        }

        /** {@inheritDoc} */
        @SuppressWarnings("unchecked")
        @Override
        public Replication.TimeDouble<? extends SimulatorInterface.TimeDouble> getReplication()
        {
            return (Replication.TimeDouble<? extends SimulatorInterface.TimeDouble>) super.getReplication();
        }
    }

    /** Easy access class Simulator.TimeFloat. */
    public abstract static class TimeFloat extends Simulator<Float, Float, SimTimeFloat> implements SimulatorInterface.TimeFloat
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /**
         * Constructs a new Simulator.TimeFloat.
         * @param id the id of the simulator, used in logging and firing of events.
         */
        public TimeFloat(final Serializable id)
        {
            super(id);
        }

        /** {@inheritDoc} */
        @SuppressWarnings("unchecked")
        @Override
        public Replication.TimeFloat<? extends SimulatorInterface.TimeFloat> getReplication()
        {
            return (Replication.TimeFloat<? extends SimulatorInterface.TimeFloat>) super.getReplication();
        }
    }

    /** Easy access class Simulator.TimeLong. */
    public abstract static class TimeLong extends Simulator<Long, Long, SimTimeLong> implements SimulatorInterface.TimeLong
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /**
         * Constructs a new Simulator.TimeLong.
         * @param id the id of the simulator, used in logging and firing of events.
         */
        public TimeLong(final Serializable id)
        {
            super(id);
        }

        /** {@inheritDoc} */
        @SuppressWarnings("unchecked")
        @Override
        public Replication.TimeLong<? extends SimulatorInterface.TimeLong> getReplication()
        {
            return (Replication.TimeLong<? extends SimulatorInterface.TimeLong>) super.getReplication();
        }
    }

    /** Easy access class Simulator.TimeDoubleUnit. */
    public abstract static class TimeDoubleUnit extends Simulator<Time, Duration, SimTimeDoubleUnit>
            implements SimulatorInterface.TimeDoubleUnit
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /**
         * Constructs a new Simulator.TimeDoubleUnit.
         * @param id the id of the simulator, used in logging and firing of events.
         */
        public TimeDoubleUnit(final Serializable id)
        {
            super(id);
        }

        /** {@inheritDoc} */
        @SuppressWarnings("unchecked")
        @Override
        public Replication.TimeDoubleUnit<? extends SimulatorInterface.TimeDoubleUnit> getReplication()
        {
            return (Replication.TimeDoubleUnit<? extends SimulatorInterface.TimeDoubleUnit>) super.getReplication();
        }
    }

    /** Easy access class Simulator.TimeFloatUnit. */
    public abstract static class TimeFloatUnit extends Simulator<FloatTime, FloatDuration, SimTimeFloatUnit>
            implements SimulatorInterface.TimeFloatUnit
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /**
         * Constructs a new Simulator.TimeFloatUnit.
         * @param id the id of the simulator, used in logging and firing of events.
         */
        public TimeFloatUnit(final Serializable id)
        {
            super(id);
        }

        /** {@inheritDoc} */
        @SuppressWarnings("unchecked")
        @Override
        public Replication.TimeFloatUnit<? extends SimulatorInterface.TimeFloatUnit> getReplication()
        {
            return (Replication.TimeFloatUnit<? extends SimulatorInterface.TimeFloatUnit>) super.getReplication();
        }
    }

    /** Easy access class Simulator.CalendarDouble. */
    public abstract static class CalendarDouble extends Simulator<Calendar, Duration, SimTimeCalendarDouble>
            implements SimulatorInterface.CalendarDouble
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /**
         * Constructs a new Simulator.CalendarDouble.
         * @param id the id of the simulator, used in logging and firing of events.
         */
        public CalendarDouble(final Serializable id)
        {
            super(id);
        }

        /** {@inheritDoc} */
        @SuppressWarnings("unchecked")
        @Override
        public Replication.CalendarDouble<? extends SimulatorInterface.CalendarDouble> getReplication()
        {
            return (Replication.CalendarDouble<? extends SimulatorInterface.CalendarDouble>) super.getReplication();
        }
    }

    /** Easy access class Simulator.CalendarFloat. */
    public abstract static class CalendarFloat extends Simulator<Calendar, FloatDuration, SimTimeCalendarFloat>
            implements SimulatorInterface.CalendarFloat
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /**
         * Constructs a new Simulator.CalendarFloat.
         * @param id the id of the simulator, used in logging and firing of events.
         */
        public CalendarFloat(final Serializable id)
        {
            super(id);
        }

        /** {@inheritDoc} */
        @SuppressWarnings("unchecked")
        @Override
        public Replication.CalendarFloat<? extends SimulatorInterface.CalendarFloat> getReplication()
        {
            return (Replication.CalendarFloat<? extends SimulatorInterface.CalendarFloat>) super.getReplication();
        }
    }

    /** Easy access class Simulator.CalendarLong. */
    public abstract static class CalendarLong extends Simulator<Calendar, Long, SimTimeCalendarLong>
            implements SimulatorInterface.CalendarLong
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /**
         * Constructs a new Simulator.CalendarLong.
         * @param id the id of the simulator, used in logging and firing of events.
         */
        public CalendarLong(final Serializable id)
        {
            super(id);
        }

        /** {@inheritDoc} */
        @SuppressWarnings("unchecked")
        @Override
        public Replication.CalendarLong<? extends SimulatorInterface.CalendarLong> getReplication()
        {
            return (Replication.CalendarLong<? extends SimulatorInterface.CalendarLong>) super.getReplication();
        }
    }

}
