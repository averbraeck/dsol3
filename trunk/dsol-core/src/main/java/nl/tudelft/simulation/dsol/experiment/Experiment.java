package nl.tudelft.simulation.dsol.experiment;

import java.io.Serializable;
import java.rmi.RemoteException;
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
        S extends SimulatorInterface<A, R, T>> extends EventProducer implements EventListenerInterface, Serializable
{
    /** The default serial version UID for serializable classes. */
    private static final long serialVersionUID = 1L;

    /** START_EXPERIMENT_EVENT is fired when the experiment starts. */
    public static final EventType START_EXPERIMENT_EVENT =
            new EventType(new MetaData("START_EXPERIMENT_EVENT", "Start of experiment"));

    /** END_EXPERIMENT_EVENT is fired when the experiment is ended. */
    public static final EventType END_EXPERIMENT_EVENT =
            new EventType(new MetaData("END_EXPERIMENT_EVENT", "End of experiment"));

    /** The replications of this experiment. */
    private List<? extends ReplicationInterface<A, R, T>> replications;

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
    private int currentReplication = -1;

    /** the start time of the simulation for all replications. */
    private final T startTime;

    /** the end time of the simulation for all replications. */
    private final T endTime;

    /** the warmup time of the simulation for all replications (included in the total run length). */
    private final T warmupTime;
    
    /** Are we already subscribed to the END_REPLICATION_EVENT. */
    private boolean subscribed = false;

    /** The Experiment context. */
    private ContextInterface context;

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
     * @return SimulatorInterface
     */
    public final S getSimulator()
    {
        return this.simulator;
    }

    /**
     * Return the model.
     * @return DSOLModel the model
     */
    public DSOLModel<A, R, T, ? extends S> getModel()
    {
        return this.model;
    }

    /**
     * Return the list of replications.
     * @return List&lt;Replication&lt;A, R, T, S&gt;&gt;; the list of replications
     */
    public final List<? extends ReplicationInterface<A, R, T>> getReplications()
    {
        return this.replications;
    }

    /**
     * starts the experiment on a simulator.
     * @throws RemoteException on netowrk error if started by RMI
     * @throws SimRuntimeException when there are no more replications to run, or when the simulator is already running
     */
    public synchronized void start() throws RemoteException
    {
        Throw.when(this.currentReplication >= this.replications.size(), SimRuntimeException.class,
                "Experiment: No more replications");
        Throw.when(this.simulator.isStartingOrRunning(), SimRuntimeException.class,
                "Simulator for experiment running -- Experiment cannot be started");
        startNextReplication();
    }

    /**
     * Start the next replication from the list of replications, or fire END_EXPERIMENT_EVENT when there are no more
     * non-executed replications.
     * @throws RemoteException on netowrk error if started by RMI
     */
    private void startNextReplication() throws RemoteException
    {
        if (this.currentReplication < (this.replications.size() - 1))
        {
            // we can run the next replication
            this.currentReplication++;
            ReplicationInterface<A, R, T> replication = this.replications.get(this.currentReplication);
            this.simulator.addListener(this, ReplicationInterface.END_REPLICATION_EVENT, ReferenceType.STRONG);
            this.fireEvent(Experiment.START_EXPERIMENT_EVENT, null);
            this.simulator.initialize(getModel(), replication);
            this.simulator.start();
        }
        else
        {
            // There is no experiment to run anymore
            this.fireEvent(Experiment.END_EXPERIMENT_EVENT, null);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void notify(final EventInterface event) throws RemoteException
    {
        if (!this.subscribed)
        {
            this.simulator.addListener(this, ReplicationInterface.END_REPLICATION_EVENT, ReferenceType.STRONG);
            this.subscribed = true;
        }
        if (event.getType().equals(ReplicationInterface.END_REPLICATION_EVENT))
        {
            startNextReplication();
        }
    }

    /**
     * resets the experiment.
     */
    public void reset()
    {
        this.currentReplication = -1;
    }

    /**
     * @return Returns the experiment description.
     */
    public final String getDescription()
    {
        return this.description;
    }

    /**
     * @param description String; The description to set.
     */
    public final void setDescription(final String description)
    {
        this.description = description;
    }

    /**
     * @return currentReplication
     */
    public final int getCurrentReplication()
    {
        return this.currentReplication;
    }

    /**
     * @return the context of the experiment, based on the hashCode.
     * @throws NamingException if context could not be found or created.
     * @throws RemoteException on RMI error
     */
    public final ContextInterface getContext() throws NamingException, RemoteException
    {
        if (this.context == null)
        {
            ContextInterface rootContext = InitialEventContext.instantiate("root");
            this.context = ContextUtil.lookupOrCreateSubContext(rootContext, String.valueOf(hashCode()));
        }
        return this.context;
    }

    /**
     * Remove the entire experiment tree from the context.
     * @throws NamingException if context could not be found or removed.
     * @throws RemoteException on RMI error
     */
    public final void removeFromContext() throws NamingException, RemoteException
    {
        if (this.context != null)
        {
            ContextInterface rootContext = InitialEventContext.instantiate("root");
            ContextUtil.destroySubContext(rootContext, String.valueOf(hashCode()));
        }
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return "Experiment[" + this.description + " ; simulator=" + this.simulator.getClass().getTypeName() + "]";
    }

    /***********************************************************************************************************/
    /************************************* EASY ACCESS CLASS EXTENSIONS ****************************************/
    /***********************************************************************************************************/

//    /**
//     * Easy access class Experiment.TimeDouble.
//     * @param <S> the simulator to use
//     */
//    public static class TimeDouble<S extends SimulatorInterface.TimeDouble> extends Experiment<Double, Double, SimTimeDouble, S>
//    {
//        /** */
//        private static final long serialVersionUID = 20150422L;
//
//        /**
//         * constructs a new Experiment.TimeDouble.
//         * @param treatment Treatment.TimeDouble; the treatment for this experiment
//         * @param simulator S; the simulator
//         * @param model DSOLModel.TimeDouble&lt;S&gt;; the model to experiment with
//         */
//        public TimeDouble(final Treatment.TimeDouble treatment, final S simulator,
//                final DSOLModel.TimeDouble<? extends S> model)
//        {
//            super(treatment, simulator, model);
//        }
//
//        /** {@inheritDoc} */
//        @Override
//        public DSOLModel.TimeDouble<? extends S> getModel()
//        {
//            return (DSOLModel.TimeDouble<? extends S>) super.getModel();
//        }
//    }
//
//    /**
//     * Easy access class Experiment.TimeFloat.
//     * @param <S> the simulator to use
//     */
//    public static class TimeFloat<S extends SimulatorInterface.TimeFloat> extends Experiment<Float, Float, SimTimeFloat, S>
//    {
//        /** */
//        private static final long serialVersionUID = 20150422L;
//
//        /**
//         * constructs a new Experiment.TimeFloat.
//         * @param treatment Treatment.TimeFloat; the treatment for this experiment
//         * @param simulator S; the simulator
//         * @param model DSOLModel.TimeFloat&lt;S&gt;; the model to experiment with
//         */
//        public TimeFloat(final Treatment.TimeFloat treatment, final S simulator, final DSOLModel.TimeFloat<? extends S> model)
//        {
//            super(treatment, simulator, model);
//        }
//
//        /** {@inheritDoc} */
//        @Override
//        public DSOLModel.TimeFloat<? extends S> getModel()
//        {
//            return (DSOLModel.TimeFloat<? extends S>) super.getModel();
//        }
//    }
//
//    /**
//     * Easy access class Experiment.TimeLong.
//     * @param <S> the simulator to use
//     */
//    public static class TimeLong<S extends SimulatorInterface.TimeLong> extends Experiment<Long, Long, SimTimeLong, S>
//    {
//        /** */
//        private static final long serialVersionUID = 20150422L;
//
//        /**
//         * constructs a new Experiment.TimeLong.
//         * @param treatment Treatment.TimeLong; the treatment for this experiment
//         * @param simulator S; the simulator
//         * @param model DSOLModel.TimeLong&lt;S&gt;; the model to experiment with
//         */
//        public TimeLong(final Treatment.TimeLong treatment, final S simulator, final DSOLModel.TimeLong<? extends S> model)
//        {
//            super(treatment, simulator, model);
//        }
//
//        /** {@inheritDoc} */
//        @Override
//        public DSOLModel.TimeLong<? extends S> getModel()
//        {
//            return (DSOLModel.TimeLong<? extends S>) super.getModel();
//        }
//    }
//
//    /**
//     * Easy access class Experiment.TimeDoubleUnit.
//     * @param <S> the simulator to use
//     */
//    public static class TimeDoubleUnit<S extends SimulatorInterface.TimeDoubleUnit>
//            extends Experiment<Time, Duration, SimTimeDoubleUnit, S>
//    {
//        /** */
//        private static final long serialVersionUID = 20150422L;
//
//        /**
//         * constructs a new Experiment.TimeDoubleUnit.
//         * @param treatment Treatment.TimeDoubleUnit; the treatment for this experiment
//         * @param simulator S; the simulator
//         * @param model DSOLModel.TimeDoubleUnit&lt;S&gt;; the model to experiment with
//         */
//        public TimeDoubleUnit(final Treatment.TimeDoubleUnit treatment, final S simulator,
//                final DSOLModel.TimeDoubleUnit<? extends S> model)
//        {
//            super(treatment, simulator, model);
//        }
//
//        /** {@inheritDoc} */
//        @Override
//        public DSOLModel.TimeDoubleUnit<? extends S> getModel()
//        {
//            return (DSOLModel.TimeDoubleUnit<? extends S>) super.getModel();
//        }
//    }
//
//    /**
//     * Easy access class Experiment.TimeFloatUnit.
//     * @param <S> the simulator to use
//     */
//    public static class TimeFloatUnit<S extends SimulatorInterface.TimeFloatUnit>
//            extends Experiment<FloatTime, FloatDuration, SimTimeFloatUnit, S>
//    {
//        /** */
//        private static final long serialVersionUID = 20150422L;
//
//        /**
//         * constructs a new Experiment.TimeFloatUnit.
//         * @param treatment Treatment.TimeFloatUnit; the treatment for this experiment
//         * @param simulator S; the simulator
//         * @param model DSOLModel.TimeFloatUnit&lt;S&gt;; the model to experiment with
//         */
//        public TimeFloatUnit(final Treatment.TimeFloatUnit treatment, final S simulator,
//                final DSOLModel.TimeFloatUnit<? extends S> model)
//        {
//            super(treatment, simulator, model);
//        }
//
//        /** {@inheritDoc} */
//        @Override
//        public DSOLModel.TimeFloatUnit<? extends S> getModel()
//        {
//            return (DSOLModel.TimeFloatUnit<? extends S>) super.getModel();
//        }
//    }

}
