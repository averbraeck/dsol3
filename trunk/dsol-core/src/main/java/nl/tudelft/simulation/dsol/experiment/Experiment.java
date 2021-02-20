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
import org.djutils.metadata.ObjectDescriptor;

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
 * The Experiment specifies the parameters for a simulation experiment.
 * <p>
 * Copyright (c) 2002-2021 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author Peter Jacobs, Alexander Verbraeck
 * @param <A> the absolute storage type for the simulation time, e.g. Calendar, Duration, or Double.
 * @param <R> the relative type for time storage, e.g. Long for the Calendar. For most non-calendar types, the absolute and
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

    /** MODEL_CHANGED_EVENT is fired whenever the model is changed. */
    public static final EventType MODEL_CHANGED_EVENT = new EventType(new MetaData("MODEL_CHANGED_EVENT", "Model changed",
            new ObjectDescriptor[] {new ObjectDescriptor("newModel", "New model", DSOLModel.class)}));

    /** SIMULATOR_CHANGED_EVENT is fired whenever the simulator is changed. */
    public static final EventType SIMULATOR_CHANGED_EVENT =
            new EventType(new MetaData("SIMULATOR_CHANGED_EVENT", "Simulator changed",
                    new ObjectDescriptor[] {new ObjectDescriptor("newSimulator", "New simulator", SimulatorInterface.class)}));

    /** replications are the replications of this experiment. */
    private List<? extends Replication<A, R, T, ? extends S>> replications;

    /** treatment represent the treatment of this experiment. */
    private Treatment<A, R, T> treatment = null;

    /** simulator reflects the simulator. */
    private S simulator;

    /** model reflects the model. */
    private DSOLModel<A, R, T, ? extends S> model;

    /** the description of this experiment. */
    private String description = null;

    /** the analyst for this experiment. */
    private String analyst = null;

    /** the current replication. */
    private int currentReplication = -1;

    /** are we already subscribed to the END_REPLICATION_EVENT. */
    private boolean subscribed = false;

    /** the context. */
    private ContextInterface context;

    /**
     * constructs a new Experiment.
     */
    public Experiment()
    {
        super();
    }

    /**
     * Construct a new Experiment.
     * @param treatment Treatment&lt;A,R,T&gt;; the treatment for this experiment
     * @param simulator S; the simulator
     * @param model DSOLModel&lt;A, R, T, S&gt;; the model to experiment with
     * @throws NullPointerException when treatment, simulator or model is null
     */
    public Experiment(final Treatment<A, R, T> treatment, final S simulator, final DSOLModel<A, R, T, ? extends S> model)
    {
        this.setSimulator(simulator);
        this.setTreatment(treatment);
        this.setModel(model);
    }

    /** {@inheritDoc} */
    @Override
    public Serializable getSourceId()
    {
        return this.description;
    }

    /**
     * Set the simulator.
     * @param simulator S; the simulator
     * @throws NullPointerException when simulator is null
     */
    public final synchronized void setSimulator(final S simulator)
    {
        Throw.whenNull(simulator, "Experiment.setSimulator: simulator cannot be null");
        if (this.simulator != null)
        {
            this.fireEvent(SIMULATOR_CHANGED_EVENT, simulator);
        }
        this.simulator = simulator;
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
    public final List<? extends Replication<A, R, T, ? extends S>> getReplications()
    {
        return this.replications;
    }

    /**
     * Set the list of replications.
     * @param replications List&lt;? extends Replication&lt;A, R, T, S&gt;&gt;; The replications to set.
     * @throws NullPointerException when the list of replications is null
     * @throws IllegalArgumentException when the list of replications is empty
     */
    public final void setReplications(final List<? extends Replication<A, R, T, ? extends S>> replications)
    {
        Throw.whenNull(replications, "Experiment: list of replications cannot be null");
        Throw.when(replications.isEmpty(), IllegalArgumentException.class, "Experiment: list of replications cannot be empty");
        this.replications = replications;
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
                "Simulator for experiment running -- ExperimentalFrame cannot be started");
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
            Replication<A, R, T, ? extends S> replication = this.replications.get(this.currentReplication);
            this.simulator.addListener(this, Replication.END_REPLICATION_EVENT, ReferenceType.STRONG);
            this.fireEvent(Experiment.START_EXPERIMENT_EVENT, null);
            this.simulator.initialize(replication, this.treatment.getReplicationMode());
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
            this.simulator.addListener(this, Replication.END_REPLICATION_EVENT, ReferenceType.STRONG);
            this.subscribed = true;
        }
        if (event.getType().equals(Replication.END_REPLICATION_EVENT))
        {
            startNextReplication();
        }
    }

    /**
     * sets the model on the experiment.
     * @param model DSOLModel&lt;A, R, T, S&gt;; the simulator model
     */
    public final synchronized void setModel(final DSOLModel<A, R, T, ? extends S> model)
    {
        Throw.whenNull(model, "Experiment.setModel: model cannot be null");
        if (this.model != null)
        {
            this.fireEvent(MODEL_CHANGED_EVENT, model);
        }
        this.model = model;
    }

    /**
     * @return the treatment of this experiment
     */
    public Treatment<A, R, T> getTreatment()
    {
        return this.treatment;
    }

    /**
     * sets the treatment of an experiment.
     * @param treatment Treatment&lt;A,R,T&gt;; the treatment
     */
    public final void setTreatment(final Treatment<A, R, T> treatment)
    {
        Throw.whenNull(treatment, "Experiment.setTreatment: treatment cannot be null");
        this.treatment = treatment;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        String result = "Experiment[" + this.description + " ; treatment=" + this.treatment.toString() + "; simulator="
                + this.simulator.getClass().getTypeName() + "]";
        return result;
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
     * @return the analyst.
     */
    public final String getAnalyst()
    {
        return this.analyst;
    }

    /**
     * @param analyst String; the analyst to set.
     */
    public final void setAnalyst(final String analyst)
    {
        this.analyst = analyst;
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
     * remove the entire experiment tree from the context.
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

    /***********************************************************************************************************/
    /************************************* EASY ACCESS CLASS EXTENSIONS ****************************************/
    /***********************************************************************************************************/

    /**
     * Easy access class Experiment.TimeDouble.
     * @param <S> the simulator to use
     */
    public static class TimeDouble<S extends SimulatorInterface.TimeDouble> extends Experiment<Double, Double, SimTimeDouble, S>
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * constructs a new Experiment.
         */
        public TimeDouble()
        {
            super();
        }

        /**
         * constructs a new Experiment.TimeDouble.
         * @param treatment Treatment.TimeDouble; the treatment for this experiment
         * @param simulator S; the simulator
         * @param model DSOLModel.TimeDouble&lt;S&gt;; the model to experiment with
         */
        public TimeDouble(final Treatment.TimeDouble treatment, final S simulator,
                final DSOLModel.TimeDouble<? extends S> model)
        {
            super(treatment, simulator, model);
        }

        /** {@inheritDoc} */
        @Override
        public DSOLModel.TimeDouble<? extends S> getModel()
        {
            return (DSOLModel.TimeDouble<? extends S>) super.getModel();
        }

        /** {@inheritDoc} */
        @Override
        public Treatment.TimeDouble getTreatment()
        {
            return (Treatment.TimeDouble) super.getTreatment();
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
         * constructs a new Experiment.
         */
        public TimeFloat()
        {
            super();
        }

        /**
         * constructs a new Experiment.TimeFloat.
         * @param treatment Treatment.TimeFloat; the treatment for this experiment
         * @param simulator S; the simulator
         * @param model DSOLModel.TimeFloat&lt;S&gt;; the model to experiment with
         */
        public TimeFloat(final Treatment.TimeFloat treatment, final S simulator, final DSOLModel.TimeFloat<? extends S> model)
        {
            super(treatment, simulator, model);
        }

        /** {@inheritDoc} */
        @Override
        public DSOLModel.TimeFloat<? extends S> getModel()
        {
            return (DSOLModel.TimeFloat<? extends S>) super.getModel();
        }

        /** {@inheritDoc} */
        @Override
        public Treatment.TimeFloat getTreatment()
        {
            return (Treatment.TimeFloat) super.getTreatment();
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
         * constructs a new Experiment.
         */
        public TimeLong()
        {
            super();
        }

        /**
         * constructs a new Experiment.TimeLong.
         * @param treatment Treatment.TimeLong; the treatment for this experiment
         * @param simulator S; the simulator
         * @param model DSOLModel.TimeLong&lt;S&gt;; the model to experiment with
         */
        public TimeLong(final Treatment.TimeLong treatment, final S simulator, final DSOLModel.TimeLong<? extends S> model)
        {
            super(treatment, simulator, model);
        }

        /** {@inheritDoc} */
        @Override
        public DSOLModel.TimeLong<? extends S> getModel()
        {
            return (DSOLModel.TimeLong<? extends S>) super.getModel();
        }

        /** {@inheritDoc} */
        @Override
        public Treatment.TimeLong getTreatment()
        {
            return (Treatment.TimeLong) super.getTreatment();
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
         * constructs a new Experiment.
         */
        public TimeDoubleUnit()
        {
            super();
        }

        /**
         * constructs a new Experiment.TimeDoubleUnit.
         * @param treatment Treatment.TimeDoubleUnit; the treatment for this experiment
         * @param simulator S; the simulator
         * @param model DSOLModel.TimeDoubleUnit&lt;S&gt;; the model to experiment with
         */
        public TimeDoubleUnit(final Treatment.TimeDoubleUnit treatment, final S simulator,
                final DSOLModel.TimeDoubleUnit<? extends S> model)
        {
            super(treatment, simulator, model);
        }

        /** {@inheritDoc} */
        @Override
        public DSOLModel.TimeDoubleUnit<? extends S> getModel()
        {
            return (DSOLModel.TimeDoubleUnit<? extends S>) super.getModel();
        }

        /** {@inheritDoc} */
        @Override
        public Treatment.TimeDoubleUnit getTreatment()
        {
            return (Treatment.TimeDoubleUnit) super.getTreatment();
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
         * constructs a new Experiment.
         */
        public TimeFloatUnit()
        {
            super();
        }

        /**
         * constructs a new Experiment.TimeFloatUnit.
         * @param treatment Treatment.TimeFloatUnit; the treatment for this experiment
         * @param simulator S; the simulator
         * @param model DSOLModel.TimeFloatUnit&lt;S&gt;; the model to experiment with
         */
        public TimeFloatUnit(final Treatment.TimeFloatUnit treatment, final S simulator,
                final DSOLModel.TimeFloatUnit<? extends S> model)
        {
            super(treatment, simulator, model);
        }

        /** {@inheritDoc} */
        @Override
        public DSOLModel.TimeFloatUnit<? extends S> getModel()
        {
            return (DSOLModel.TimeFloatUnit<? extends S>) super.getModel();
        }

        /** {@inheritDoc} */
        @Override
        public Treatment.TimeFloatUnit getTreatment()
        {
            return (Treatment.TimeFloatUnit) super.getTreatment();
        }
    }

}
