package nl.tudelft.simulation.dsol.simulators;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.djunits.value.vdouble.scalar.Duration;
import org.djunits.value.vdouble.scalar.Time;
import org.djunits.value.vfloat.scalar.FloatDuration;
import org.djunits.value.vfloat.scalar.FloatTime;
import org.djutils.event.EventProducer;
import org.djutils.event.TimedEventType;
import org.djutils.exceptions.Throw;
import org.djutils.logger.CategoryLogger;
import org.pmw.tinylog.Logger;

import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.experiment.ReplicationInterface;
import nl.tudelft.simulation.dsol.formalisms.eventscheduling.SimEvent;
import nl.tudelft.simulation.dsol.logger.SimLogger;
import nl.tudelft.simulation.dsol.model.DSOLModel;
import nl.tudelft.simulation.dsol.simtime.SimTime;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simtime.SimTimeDoubleUnit;
import nl.tudelft.simulation.dsol.simtime.SimTimeFloat;
import nl.tudelft.simulation.dsol.simtime.SimTimeFloatUnit;
import nl.tudelft.simulation.dsol.simtime.SimTimeLong;

/**
 * The Simulator class is an abstract implementation of the SimulatorInterface.
 * <p>
 * Copyright (c) 2002-2022 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @param <A> the absolute storage type for the simulation time, e.g. Time, Float, or Double.
 * @param <R> the relative type for time storage, e.g. Duration for absolute Time. For most non-unit types, the absolute and
 *            relative types are the same.
 * @param <T> the extended type itself to be able to implement a comparator on the simulation time.
 */
public abstract class Simulator<A extends Comparable<A> & Serializable, R extends Number & Comparable<R>,
        T extends SimTime<A, R, T>> extends EventProducer implements SimulatorInterface<A, R, T>, Runnable
{
    /** */
    private static final long serialVersionUID = 20140805L;

    /** simulatorTime represents the simulationTime. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected T simulatorTime;

    /** The runUntil time in case we want to stop before the end of the replication time. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected T runUntilTime;

    /** whether the runUntilTime should carry out the calculation(s) for that time or not. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected boolean runUntilIncluding = true;

    /** The run state of the simulator, that indicates the state of the Simulator state machine. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected RunState runState = RunState.NOT_INITIALIZED;

    /** The replication state of the simulator, that indicates the state of the Replication state machine. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected ReplicationState replicationState = ReplicationState.NOT_INITIALIZED;

    /** The currently active replication; is null before initialize() has been called. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected ReplicationInterface<A, R, T> replication = null;

    /** The model that is currently active; is null before initialize() has been called. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected DSOLModel<A, R, T, ? extends SimulatorInterface<A, R, T>> model = null;

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

    /** the methods to execute after model initialization, e.g., to set-up the initial events. */
    private final List<SimEvent.TimeLong> initialmethodCalls = new ArrayList<>();

    /**
     * Constructs a new Simulator.
     * @param id the id of the simulator, used in logging and firing of events.
     */
    public Simulator(final Serializable id)
    {
        Throw.whenNull(id, "id cannot be null");
        this.id = id;
        this.logger = new SimLogger(this);
    }

    /** {@inheritDoc} */
    @SuppressWarnings({"hiding", "checkstyle:hiddenfield"})
    @Override
    public void initialize(final DSOLModel<A, R, T, ? extends SimulatorInterface<A, R, T>> model,
            final ReplicationInterface<A, R, T> replication) throws SimRuntimeException
    {
        Throw.whenNull(model, "Simulator.initialize: model cannot be null");
        Throw.whenNull(replication, "Simulator.initialize: replication cannot be null");
        Throw.when(isStartingOrRunning(), SimRuntimeException.class, "Cannot initialize a running simulator");
        synchronized (this.semaphore)
        {
            if (this.worker != null)
            {
                cleanUp();
            }
            this.worker = new SimulatorWorkerThread(this.id.toString(), this);
            this.replication = replication;
            this.model = model;
            this.simulatorTime = replication.getStartSimTime().copy();
            model.constructModel();
            this.runState = RunState.INITIALIZED;
            this.replicationState = ReplicationState.INITIALIZED;

            for (SimEvent.TimeLong initialMethodCall : this.initialmethodCalls)
            {
                initialMethodCall.execute();
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    public void addScheduledMethodOnInitialize(final Object source, final Object target, final String method,
            final Object[] args) throws SimRuntimeException
    {
        this.initialmethodCalls.add(new SimEvent.TimeLong(0L, source, target, method, args));
    }

    /**
     * Implementation of the start method. Checks preconditions for running and fires the right events.
     * @throws SimRuntimeException when the simulator is already running, or when the replication is missing or has ended
     */
    public void startImpl() throws SimRuntimeException
    {
        Throw.when(isStartingOrRunning(), SimRuntimeException.class, "Cannot start a running simulator");
        Throw.when(this.replication == null, SimRuntimeException.class, "Cannot start a simulator without replication details");
        Throw.when(!isInitialized(), SimRuntimeException.class, "Cannot start an uninitialized simulator");
        Throw.when(
                !(this.replicationState == ReplicationState.INITIALIZED || this.replicationState == ReplicationState.STARTED),
                SimRuntimeException.class, "State of the replication should be INITIALIZED or STARTED to run a simulationF");
        Throw.when(this.simulatorTime.ge(this.replication.getEndSimTime()), SimRuntimeException.class,
                "Cannot start simulator : simulatorTime >= runLength");
        synchronized (this.semaphore)
        {
            this.runState = RunState.STARTING;
            if (this.replicationState == ReplicationState.INITIALIZED)
            {
                fireTimedEvent(ReplicationInterface.START_REPLICATION_EVENT, null, getSimulatorTime());
                this.replicationState = ReplicationState.STARTED;
            }
            this.fireEvent(SimulatorInterface.STARTING_EVENT, null);
            if (!Thread.currentThread().getName().equals(this.worker.getName()))
            {
                this.worker.interrupt();
            }
            else
            {
                run();
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    public void start() throws SimRuntimeException
    {
        this.runUntilTime = this.replication.getEndSimTime();
        this.runUntilIncluding = true;
        startImpl();
    }

    /** {@inheritDoc} */
    @Override
    public void runUpTo(final T stopTime) throws SimRuntimeException
    {
        this.runUntilTime = stopTime;
        this.runUntilIncluding = false;
        startImpl();
    }

    /** {@inheritDoc} */
    @Override
    public void runUpToAndIncluding(final T stopTime) throws SimRuntimeException
    {
        this.runUntilTime = stopTime;
        this.runUntilIncluding = true;
        startImpl();
    }

    /**
     * The implementation body of the step() method. The stepImpl() method should fire the TIME_CHANGED_EVENT before the
     * execution of the simulation event, or before executing the integration of the differential equation for the next
     * timestep. So the time is changed first to match the lgic carried out for that time, and then the action for that time is
     * carried out.
     */
    protected abstract void stepImpl();

    /** {@inheritDoc} */
    @Override
    public void step() throws SimRuntimeException
    {
        Throw.when(isStartingOrRunning(), SimRuntimeException.class, "Cannot step a running simulator");
        Throw.when(!isInitialized(), SimRuntimeException.class, "Cannot start an uninitialized simulator");
        Throw.when(
                !(this.replicationState == ReplicationState.INITIALIZED || this.replicationState == ReplicationState.STARTED),
                SimRuntimeException.class, "State of the replication should be INITIALIZED or STARTED to run a simulationF");
        Throw.when(this.simulatorTime.ge(this.replication.getEndSimTime()), SimRuntimeException.class,
                "Cannot step simulator : simulatorTime >= runLength");
        try
        {
            synchronized (this.semaphore)
            {
                this.runState = RunState.STARTED;
                fireTimedEvent(SimulatorInterface.START_EVENT, null, getSimulatorTime());
                stepImpl();
            }
        }
        finally
        {
            fireTimedEvent(SimulatorInterface.STOP_EVENT, null, getSimulatorTime());
            this.runState = RunState.STOPPED;
        }
    }

    /**
     * Implementation of the stop behavior.
     */
    protected void stopImpl()
    {
        this.runState = RunState.STOPPING;
    }

    /** {@inheritDoc} */
    @Override
    public void stop() throws SimRuntimeException
    {
        Throw.when(isStoppingOrStopped(), SimRuntimeException.class, "Cannot stop an already stopped simulator");
        this.fireEvent(SimulatorInterface.STOPPING_EVENT, null);
        stopImpl();
    }

    /**
     * Fire the WARMUP event to clear the statistics after the warmup period. Note that for a discrete event simulator, the
     * warmup event can be scheduled, whereas for a continuous simulator, the warmup event must be detected based on the
     * simulation time.
     */
    public void warmup()
    {
        fireTimedEvent(ReplicationInterface.WARMUP_EVENT, null, getSimulatorTime());
    }

    /** {@inheritDoc} */
    @Override
    public void cleanUp()
    {
        stopImpl();
        if (hasListeners())
        {
            this.removeAllListeners();
        }
        if (this.worker != null)
        {
            this.worker.cleanUp();
            this.worker = null;
        }
        this.runState = RunState.NOT_INITIALIZED;
        this.replicationState = ReplicationState.NOT_INITIALIZED;
    }

    /**
     * The method that is called when the replication ends. Note that it can also forcefully terminate the replication before
     * the actual replication time is over. It immediately fires a STOP_EVENT followed by an END_REPLICATION_EVENT, and stops
     * the running of the simulator. When the simulation time is not equal to or larger than the length of the replication, a
     * logger warning is given, but the method is fully executed. In that case it does set the simulation time to the end time
     * of the replication, to avoid restarting of the simulator.
     */
    protected void endReplication()
    {
        this.replicationState = ReplicationState.ENDING;
        this.worker.interrupt(); // just to be sure
        if (this.simulatorTime.lt(this.getReplication().getEndSimTime()))
        {
            Logger.warn("The simulator executes the endReplication method, but the simulation time " + this.simulatorTime.get()
                    + " is earlier than the replication length " + this.getReplication().getEndSimTime());
            this.simulatorTime = this.getReplication().getEndSimTime().copy();
        }
    }

    /**
     * The run method defines the actual time step mechanism of the simulator. The implementation of this method depends on the
     * formalism. Where discrete event formalisms loop over an event list, continuous simulators take predefined time steps.
     * Make sure that:<br>
     * - SimulatorInterface.TIME_CHANGED_EVENT is fired when the time of the simulator changes<br>
     * - the warmup() method is called when the warmup period has expired (through an event or based on simulation time)<br>
     * - the endReplication() method is called when the replication has ended<br>
     * - the simulator runs until the runUntil time, which is also set by the start() method.
     */
    @Override
    public abstract void run();

    /** {@inheritDoc} */
    @Override
    public A getSimulatorTime()
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
    public ReplicationInterface<A, R, T> getReplication()
    {
        return this.replication;
    }

    /** {@inheritDoc} */
    @Override
    public DSOLModel<A, R, T, ? extends SimulatorInterface<A, R, T>> getModel()
    {
        return this.model;
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
    public RunState getRunState()
    {
        return this.runState;
    }

    /** {@inheritDoc} */
    @Override
    public ReplicationState getReplicationState()
    {
        return this.replicationState;
    }

    /**
     * fireTimedEvent method to be called for a no-payload TimedEvent.
     * @param event the event to fire at the current time
     */
    protected void fireTimedEvent(final TimedEventType event)
    {
        fireTimedEvent(event, null, getSimulatorTime());
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
            this.replication = (ReplicationInterface<A, R, T>) in.readObject();
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
        public synchronized void cleanUp()
        {
            this.running.set(false);
            this.finalized = true;
            if (!this.isInterrupted())
            {
                this.notify(); // in case it is in the 'wait' state
            }
        }

        /**
         * @return whether the run method of the job is running or not
         */
        public synchronized boolean isRunning()
        {
            return this.running.get();
        }

        /** {@inheritDoc} */
        @Override
        public synchronized void run()
        {
            while (!this.finalized) // always until finalized
            {
                try
                {
                    this.wait(); // as long as possible
                }
                catch (InterruptedException interruptedException)
                {
                    if (!this.finalized)
                    {
                        if (this.job.replicationState != ReplicationState.ENDING)
                        {
                            this.running.set(true);
                            try
                            {
                                if (this.job.replicationState == ReplicationState.INITIALIZED)
                                {
                                    this.job.fireTimedEvent(ReplicationInterface.START_REPLICATION_EVENT);
                                    this.job.replicationState = ReplicationState.STARTED;
                                }
                                this.job.fireTimedEvent(SimulatorInterface.START_EVENT);
                                this.job.runState = RunState.STARTED;
                                this.job.run();
                                this.job.stopImpl();
                                this.job.fireTimedEvent(SimulatorInterface.STOP_EVENT);
                                this.job.runState = RunState.STOPPED;
                            }
                            catch (Exception exception)
                            {
                                CategoryLogger.always().error(exception);
                                exception.printStackTrace();
                            }
                        }
                        this.running.set(false);
                        if (this.job.replicationState == ReplicationState.ENDING)
                        {
                            this.job.replicationState = ReplicationState.ENDED;
                            this.job.runState = RunState.ENDED;
                            this.job.fireTimedEvent(ReplicationInterface.END_REPLICATION_EVENT);
                            this.finalized = true;
                        }
                    }
                    Thread.interrupted(); // clear the interrupted flag
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
        @Override
        public ReplicationInterface.TimeDouble getReplication()
        {
            return (ReplicationInterface.TimeDouble) super.getReplication();
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
        @Override
        public ReplicationInterface.TimeFloat getReplication()
        {
            return (ReplicationInterface.TimeFloat) super.getReplication();
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
        @Override
        public ReplicationInterface.TimeLong getReplication()
        {
            return (ReplicationInterface.TimeLong) super.getReplication();
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
        @Override
        public ReplicationInterface.TimeDoubleUnit getReplication()
        {
            return (ReplicationInterface.TimeDoubleUnit) super.getReplication();
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
        @Override
        public ReplicationInterface.TimeFloatUnit getReplication()
        {
            return (ReplicationInterface.TimeFloatUnit) super.getReplication();
        }
    }

}
