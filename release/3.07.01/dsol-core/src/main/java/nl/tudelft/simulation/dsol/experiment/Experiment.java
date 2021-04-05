package nl.tudelft.simulation.dsol.experiment;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;

import org.djunits.value.vdouble.scalar.Duration;
import org.djunits.value.vdouble.scalar.Time;
import org.djunits.value.vfloat.scalar.FloatDuration;
import org.djunits.value.vfloat.scalar.FloatTime;
import org.djutils.event.EventInterface;
import org.djutils.event.EventListenerInterface;
import org.djutils.event.EventProducer;
import org.djutils.event.EventType;
import org.djutils.event.ref.ReferenceType;
import org.djutils.exceptions.Throw;
import org.djutils.logger.CategoryLogger;
import org.djutils.metadata.MetaData;

import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.model.DSOLModel;
import nl.tudelft.simulation.dsol.simtime.SimTime;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simtime.SimTimeDoubleUnit;
import nl.tudelft.simulation.dsol.simtime.SimTimeFloat;
import nl.tudelft.simulation.dsol.simtime.SimTimeFloatUnit;
import nl.tudelft.simulation.dsol.simtime.SimTimeLong;
import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;
import nl.tudelft.simulation.naming.context.ContextInterface;
import nl.tudelft.simulation.naming.context.event.InitialEventContext;
import nl.tudelft.simulation.naming.context.util.ContextUtil;

/**
 * The Experiment specifies the parameters for a number of simulation replications, and can execute a series of replications.
 * <p>
 * Copyright (c) 2002-2021 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author Alexander Verbraeck
 * @param <A> the absolute storage type for the simulation time, e.g. Time, Float, or Double.
 * @param <R> the relative type for time storage, e.g. Duration for absolute Time. For most non-unit types, the absolute and
 *            relative types are the same.
 * @param <T> the extended type itself to be able to implement a comparator on the simulation time.
 * @param <S> the simulator to use
 */
public class Experiment<A extends Comparable<A> & Serializable, R extends Number & Comparable<R>, T extends SimTime<A, R, T>,
        S extends SimulatorInterface<A, R, T>> extends EventProducer
        implements EventListenerInterface, RunControlInterface<A, R, T>
{
    /** The default serial version UID for serializable classes. */
    private static final long serialVersionUID = 1L;

    /** START_EXPERIMENT_EVENT is fired when the experiment starts. */
    public static final EventType START_EXPERIMENT_EVENT =
            new EventType(new MetaData("START_EXPERIMENT_EVENT", "Start of experiment"));

    /** END_EXPERIMENT_EVENT is fired when the experiment is ended. */
    public static final EventType END_EXPERIMENT_EVENT =
            new EventType(new MetaData("END_EXPERIMENT_EVENT", "End of experiment"));

    /** The started replications of this experiment. */
    private List<ExperimentReplication<A, R, T>> startedReplications = new ArrayList<>();

    /** The simulator that will execute the replications. */
    private final S simulator;

    /** The model that has to be executed. */
    private final DSOLModel<A, R, T, ? extends S> model;

    /** The id of this experiment. */
    private final String id;

    /** The description of this experiment; when not set, the id will be used. */
    private String description = null;

    /** the number of replications to execute. */
    private final int numberOfReplications;

    /** The current replication. */
    private int currentReplicationNumber = -1;

    /** the start time of the simulation for all replications. */
    private final T startTime;

    /** the end time of the simulation for all replications. */
    private final T endTime;

    /** the warmup time of the simulation for all replications (included in the total run length). */
    private final T warmupTime;

    /** The Experiment context. */
    private ContextInterface context;
    
    /** the class that updates the seeds of the streams between replications. */
    private StreamUpdater streamUpdater = new SimpleStreamUpdater();

    /** the worker thread to carry out the experiment. */
    private ExperimentThread experimentThread;
    
    /** is the simulation experiment running? */
    private boolean running = false;

    /**
     * Construct a new Experiment.
     * @param id String; the id of the experiment
     * @param simulator S; the simulator
     * @param model DSOLModel&lt;A, R, T, S&gt;; the model to experiment with
     * @param startTime T; the start time as a time object.
     * @param warmupPeriod R; the warmup period, included in the runlength (!)
     * @param runLength R; the total length of the run, including the warm-up period.
     * @param numberOfReplications int; the number of replications to execute
     * @throws NullPointerException when id, startTime, warmupPeriod or runLength is null
     * @throws SimRuntimeException when warmup period is negative, or run length is zero or negative, or a context for the
     *             experiment cannot be created
     */
    public Experiment(final String id, final S simulator, final DSOLModel<A, R, T, ? extends S> model, final T startTime,
            final R warmupPeriod, final R runLength, final int numberOfReplications)
    {
        Throw.whenNull(simulator, "Experiment.setSimulator: simulator cannot be null");
        Throw.whenNull(model, "Experiment.setModel: model cannot be null");
        Throw.whenNull(id, "id should not be null");
        Throw.whenNull(startTime, "startTime should not be null");
        Throw.whenNull(warmupPeriod, "warmupPeriod should not be null");
        Throw.whenNull(runLength, "runLength should not be null");
        Throw.when(warmupPeriod.doubleValue() < 0.0, SimRuntimeException.class, "warmup period should not be negative");
        Throw.when(runLength.doubleValue() <= 0.0, SimRuntimeException.class, "run length should not be zero or negative");
        Throw.when(numberOfReplications <= 0, SimRuntimeException.class, "number of replications can not be zero or negative");

        this.id = id;
        this.description = id;
        this.simulator = simulator;
        this.model = model;
        this.startTime = startTime;
        this.endTime = startTime.plus(runLength);
        this.warmupTime = startTime.plus(warmupPeriod);
        this.numberOfReplications = numberOfReplications;
    }

    /** {@inheritDoc} */
    @Override
    public Serializable getSourceId()
    {
        return this.id;
    }

    /**
     * Return the simulator.
     * @return S; the simulator
     */
    public final S getSimulator()
    {
        return this.simulator;
    }

    /**
     * Return the model.
     * @return DSOLModel; the model
     */
    public DSOLModel<A, R, T, ? extends S> getModel()
    {
        return this.model;
    }

    /**
     * Return the list of started replications. Not all replications might have finished yet.
     * @return List&lt;Replication&lt;A, R, T, S&gt;&gt;; the list of started replications
     */
    public final List<? extends ExperimentReplication<A, R, T>> getStartedReplications()
    {
        return this.startedReplications;
    }

    /**
     * Start the extire experiment on a simulator, and execute ehe replications one by one.
     * @throws RemoteException on network error if started by RMI
     * @throws SimRuntimeException when there are no more replications to run, or when the simulator is already running
     */
    public synchronized void start() throws RemoteException
    {
        Throw.when(this.currentReplicationNumber >= this.numberOfReplications - 1, SimRuntimeException.class,
                "Experiment: No more replications");
        Throw.when(this.simulator.isStartingOrRunning(), SimRuntimeException.class,
                "Simulator for experiment running -- Experiment cannot be started");
        this.fireEvent(Experiment.START_EXPERIMENT_EVENT, null);
        this.experimentThread = new ExperimentThread(this);
        this.running = true;
        this.experimentThread.start();
    }

    /**
     * Start the next replication from the list of replications, or fire END_EXPERIMENT_EVENT when there are no more
     * non-executed replications.
     * @throws RemoteException on network error if started by RMI
     */
    protected void startNextReplication() throws RemoteException
    {
        Throw.when(this.currentReplicationNumber >= this.numberOfReplications - 1, SimRuntimeException.class,
                "Trying to run replication beyond given number");
        this.currentReplicationNumber++;
        ExperimentReplication<A, R, T> replication = makeExperimentReplication();
        this.startedReplications.add(replication);
        this.streamUpdater.updateSeeds(this.model.getStreams(), this.currentReplicationNumber);
        this.simulator.initialize(getModel(), replication);
        this.simulator.addListener(this, ReplicationInterface.END_REPLICATION_EVENT, ReferenceType.STRONG);
        this.simulator.start();
    }

    /**
     * Fire the end Experiment event.
     */
    protected void endExperiment()
    {
        this.fireEvent(Experiment.END_EXPERIMENT_EVENT, null);
        this.running = false;
    }

    /**
     * Create a new replication for an experiment. This method can be overridden in the inner classes.
     * @return ExperimentReplication; a new replication for an experiment
     */
    protected ExperimentReplication<A, R, T> makeExperimentReplication()
    {
        return new ExperimentReplication<A, R, T>("Replication " + this.currentReplicationNumber, this.startTime,
                getWarmupPeriod(), getRunLength(), this);
    }

    /** {@inheritDoc} */
    @Override
    public void notify(final EventInterface event) throws RemoteException
    {
        if (event.getType().equals(ReplicationInterface.END_REPLICATION_EVENT))
        {
            this.experimentThread.interrupt();
        }
    }

    /**
     * Reset the experiment so it can be run again.
     */
    public void reset()
    {
        this.currentReplicationNumber = -1;
        for (ExperimentReplication<A, R, T> replication : this.startedReplications)
        {
            replication.removeFromContext();
        }
        this.startedReplications.clear();
    }

    /** {@inheritDoc} */
    @Override
    public final String getId()
    {
        return this.id;
    }

    /** {@inheritDoc} */
    @Override
    public final String getDescription()
    {
        return this.description;
    }

    /** {@inheritDoc} */
    @Override
    public final void setDescription(final String description)
    {
        this.description = description;
    }

    /**
     * Return the current (running or finished) replication.
     * @return int; the current replication (still running or finished in case of last replication)
     */
    public final int getCurrentReplication()
    {
        return this.currentReplicationNumber;
    }

    /** {@inheritDoc} */
    @Override
    public final ContextInterface getContext()
    {
        try
        {
            if (this.context == null)
            {
                ContextInterface rootContext = InitialEventContext.instantiate("root");
                this.context = ContextUtil.lookupOrCreateSubContext(rootContext, this.id);
            }
            return this.context;
        }
        catch (RemoteException | NamingException exception)
        {
            throw new SimRuntimeException("Cannot destroy context for replication. Error is: " + exception.getMessage());
        }
    }

    /**
     * Remove the entire experiment tree from the context.
     */
    public final void removeFromContext()
    {
        try
        {
            if (this.context != null)
            {
                ContextInterface rootContext = InitialEventContext.instantiate("root");
                ContextUtil.destroySubContext(rootContext, this.id);
            }
        }
        catch (RemoteException | NamingException exception)
        {
            throw new SimRuntimeException("Cannot destroy context for replication. Error is: " + exception.getMessage());
        }
    }

    /** {@inheritDoc} */
    @Override
    public T getStartSimTime()
    {
        return this.startTime;
    }

    /** {@inheritDoc} */
    @Override
    public T getEndSimTime()
    {
        return this.endTime;
    }

    /** {@inheritDoc} */
    @Override
    public T getWarmupSimTime()
    {
        return this.warmupTime;
    }

    /**
     * Return the current stream updater.
     * @return streamUpdater StreamUpdater; the current stream updater
     */
    public final StreamUpdater getStreamUpdater()
    {
        return this.streamUpdater;
    }

    /**
     * Set a new StreamUpdater to update the random seeds between replications.
     * @param streamUpdater StreamUpdater; the new stream updater
     */
    public final void setStreamUpdater(final StreamUpdater streamUpdater)
    {
        this.streamUpdater = streamUpdater;
    }

    /**
     * Return the current replication number, which is -1 if the experiment has not yet started.
     * @return int; the current replication number
     */
    public final int getCurrentReplicationNumber()
    {
        return this.currentReplicationNumber;
    }

    /**
     * Return the total number of replications to execute.
     * @return int; the total number of replications to execute
     */
    public final int getNumberOfReplications()
    {
        return this.numberOfReplications;
    }

    /**
     * Return whether the experiment is running or not.
     * @return boolean; whether the experiment is running or not
     */
    public final boolean isRunning()
    {
        return this.running;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return "Experiment[" + this.description + " ; simulator=" + this.simulator.getClass().getTypeName() + "]";
    }

    /* ********************************************************************************************************* */
    /* ************************************** EXPERIMENT RUNNER CLASS ****************************************** */
    /* ********************************************************************************************************* */

    /** The ExperimentRunner job. */
    protected static class ExperimentThread extends Thread
    {
        /** the experiment. */
        private final Experiment<?, ?, ?, ?> experiment;

        /**
         * Construct the ExperimentRunner with a pointer to the Experiment.
         * @param experiment Experiment; the experiment
         */
        public ExperimentThread(final Experiment<?, ?, ?, ?> experiment)
        {
            this.experiment = experiment;
        }

        /** {@inheritDoc} */
        @Override
        public void run()
        {
            synchronized (this)
            {
                while (this.experiment.getCurrentReplicationNumber() < this.experiment.getNumberOfReplications() - 1)
                {
                    try
                    {
                        this.experiment.startNextReplication();
                        wait(); // wait for END_REPLICATION event
                    }
                    catch (RemoteException e)
                    {
                        CategoryLogger.always().error(e);
                        break;
                    }
                    catch (InterruptedException ie)
                    {
                        Thread.interrupted(); // clear the interrupted flag
                        // start next experiment.
                    }
                }
                this.experiment.endExperiment();
            }
        }

    }

    /* ********************************************************************************************************* */
    /* ************************************ EASY ACCESS CLASS EXTENSIONS *************************************** */
    /* ********************************************************************************************************* */

    /**
     * Easy access class Experiment.TimeDouble.
     * @param <S> the simulator to use
     */
    public static class TimeDouble<S extends SimulatorInterface.TimeDouble> extends Experiment<Double, Double, SimTimeDouble, S>
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * Construct a new Experiment.
         * @param id String; the id of the experiment
         * @param simulator S; the simulator
         * @param model DSOLModel.TimeDouble; the model to experiment with
         * @param startTime double; the start time as a time object.
         * @param warmupPeriod double; the warmup period, included in the runlength (!)
         * @param runLength double; the total length of the run, including the warm-up period.
         * @param numberOfReplications int; the number of replications to execute
         * @throws NullPointerException when id, startTime, warmupPeriod or runLength is null
         * @throws SimRuntimeException when warmup period is negative, or run length is zero or negative, or a context for the
         *             experiment cannot be created
         */
        public TimeDouble(final String id, final S simulator, final DSOLModel.TimeDouble<? extends S> model,
                final double startTime, final double warmupPeriod, final double runLength, final int numberOfReplications)
        {
            super(id, simulator, model, new SimTimeDouble(startTime), warmupPeriod, runLength, numberOfReplications);
        }

        /** {@inheritDoc} */
        @Override
        public DSOLModel.TimeDouble<? extends S> getModel()
        {
            return (DSOLModel.TimeDouble<? extends S>) super.getModel();
        }

        /** {@inheritDoc} */
        @Override
        protected ExperimentReplication.TimeDouble makeExperimentReplication()
        {
            return new ExperimentReplication.TimeDouble("Replication " + getCurrentReplicationNumber(), getStartTime(),
                    getWarmupPeriod(), getRunLength(), this);
        }
    }

    /**
     * Easy access class Experiment.TimeFloat.
     * @param <S> the simulator to use
     */
    public static class TimeFloat<S extends SimulatorInterface.TimeFloat> extends Experiment<Float, Float, SimTimeFloat, S>
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * Construct a new Experiment.
         * @param id String; the id of the experiment
         * @param simulator S; the simulator
         * @param model DSOLModel.TimeFloat; the model to experiment with
         * @param startTime float; the start time as a time object.
         * @param warmupPeriod float; the warmup period, included in the runlength (!)
         * @param runLength float; the total length of the run, including the warm-up period.
         * @param numberOfReplications int; the number of replications to execute
         * @throws NullPointerException when id, startTime, warmupPeriod or runLength is null
         * @throws SimRuntimeException when warmup period is negative, or run length is zero or negative, or a context for the
         *             experiment cannot be created
         */
        public TimeFloat(final String id, final S simulator, final DSOLModel.TimeFloat<? extends S> model,
                final float startTime, final float warmupPeriod, final float runLength, final int numberOfReplications)
        {
            super(id, simulator, model, new SimTimeFloat(startTime), warmupPeriod, runLength, numberOfReplications);
        }

        /** {@inheritDoc} */
        @Override
        public DSOLModel.TimeFloat<? extends S> getModel()
        {
            return (DSOLModel.TimeFloat<? extends S>) super.getModel();
        }

        /** {@inheritDoc} */
        @Override
        protected ExperimentReplication.TimeFloat makeExperimentReplication()
        {
            return new ExperimentReplication.TimeFloat("Replication " + getCurrentReplicationNumber(), getStartTime(),
                    getWarmupPeriod(), getRunLength(), this);
        }
    }

    /**
     * Easy access class Experiment.TimeLong.
     * @param <S> the simulator to use
     */
    public static class TimeLong<S extends SimulatorInterface.TimeLong> extends Experiment<Long, Long, SimTimeLong, S>
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * Construct a new Experiment.
         * @param id String; the id of the experiment
         * @param simulator S; the simulator
         * @param model DSOLModel.TimeLong; the model to experiment with
         * @param startTime long; the start time as a time object.
         * @param warmupPeriod long; the warmup period, included in the runlength (!)
         * @param runLength long; the total length of the run, including the warm-up period.
         * @param numberOfReplications int; the number of replications to execute
         * @throws NullPointerException when id, startTime, warmupPeriod or runLength is null
         * @throws SimRuntimeException when warmup period is negative, or run length is zero or negative, or a context for the
         *             experiment cannot be created
         */
        public TimeLong(final String id, final S simulator, final DSOLModel.TimeLong<? extends S> model, final long startTime,
                final long warmupPeriod, final long runLength, final int numberOfReplications)
        {
            super(id, simulator, model, new SimTimeLong(startTime), warmupPeriod, runLength, numberOfReplications);
        }

        /** {@inheritDoc} */
        @Override
        public DSOLModel.TimeLong<? extends S> getModel()
        {
            return (DSOLModel.TimeLong<? extends S>) super.getModel();
        }

        /** {@inheritDoc} */
        @Override
        protected ExperimentReplication.TimeLong makeExperimentReplication()
        {
            return new ExperimentReplication.TimeLong("Replication " + getCurrentReplicationNumber(), getStartTime(),
                    getWarmupPeriod(), getRunLength(), this);
        }
    }

    /**
     * Easy access class Experiment.TimeDoubleUnit.
     * @param <S> the simulator to use
     */
    public static class TimeDoubleUnit<S extends SimulatorInterface.TimeDoubleUnit>
            extends Experiment<Time, Duration, SimTimeDoubleUnit, S>
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * Construct a new Experiment.
         * @param id String; the id of the experiment
         * @param simulator S; the simulator
         * @param model DSOLModel.TimeDoubleUnit; the model to experiment with
         * @param startTime Time; the start time as a time object.
         * @param warmupPeriod Duration; the warmup period, included in the runlength (!)
         * @param runLength Duration; the total length of the run, including the warm-up period.
         * @param numberOfReplications int; the number of replications to execute
         * @throws NullPointerException when id, startTime, warmupPeriod or runLength is null
         * @throws SimRuntimeException when warmup period is negative, or run length is zero or negative, or a context for the
         *             experiment cannot be created
         */
        public TimeDoubleUnit(final String id, final S simulator, final DSOLModel.TimeDoubleUnit<? extends S> model,
                final Time startTime, final Duration warmupPeriod, final Duration runLength, final int numberOfReplications)
        {
            super(id, simulator, model, new SimTimeDoubleUnit(startTime), warmupPeriod, runLength, numberOfReplications);
        }

        /** {@inheritDoc} */
        @Override
        public DSOLModel.TimeDoubleUnit<? extends S> getModel()
        {
            return (DSOLModel.TimeDoubleUnit<? extends S>) super.getModel();
        }

        /** {@inheritDoc} */
        @Override
        protected ExperimentReplication.TimeDoubleUnit makeExperimentReplication()
        {
            return new ExperimentReplication.TimeDoubleUnit("Replication " + getCurrentReplicationNumber(), getStartTime(),
                    getWarmupPeriod(), getRunLength(), this);
        }
    }

    /**
     * Easy access class Experiment.TimeFloatUnit.
     * @param <S> the simulator to use
     */
    public static class TimeFloatUnit<S extends SimulatorInterface.TimeFloatUnit>
            extends Experiment<FloatTime, FloatDuration, SimTimeFloatUnit, S>
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * Construct a new Experiment.
         * @param id String; the id of the experiment
         * @param simulator S; the simulator
         * @param model DSOLModel.TimeFloatUnit; the model to experiment with
         * @param startTime FloatTime; the start time as a time object.
         * @param warmupPeriod FloatDuration; the warmup period, included in the runlength (!)
         * @param runLength FloatDuration; the total length of the run, including the warm-up period.
         * @param numberOfReplications int; the number of replications to execute
         * @throws NullPointerException when id, startTime, warmupPeriod or runLength is null
         * @throws SimRuntimeException when warmup period is negative, or run length is zero or negative, or a context for the
         *             experiment cannot be created
         */
        public TimeFloatUnit(final String id, final S simulator, final DSOLModel.TimeFloatUnit<? extends S> model,
                final FloatTime startTime, final FloatDuration warmupPeriod, final FloatDuration runLength,
                final int numberOfReplications)
        {
            super(id, simulator, model, new SimTimeFloatUnit(startTime), warmupPeriod, runLength, numberOfReplications);
        }

        /** {@inheritDoc} */
        @Override
        public DSOLModel.TimeFloatUnit<? extends S> getModel()
        {
            return (DSOLModel.TimeFloatUnit<? extends S>) super.getModel();
        }

        /** {@inheritDoc} */
        @Override
        protected ExperimentReplication.TimeFloatUnit makeExperimentReplication()
        {
            return new ExperimentReplication.TimeFloatUnit("Replication " + getCurrentReplicationNumber(), getStartTime(),
                    getWarmupPeriod(), getRunLength(), this);
        }
    }

}
