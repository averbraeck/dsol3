package nl.tudelft.simulation.dsol.experiment;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.List;

import javax.naming.Context;
import javax.naming.NamingException;

import org.djunits.value.vdouble.scalar.Duration;
import org.djunits.value.vdouble.scalar.Time;
import org.djunits.value.vfloat.scalar.FloatDuration;
import org.djunits.value.vfloat.scalar.FloatTime;

import nl.tudelft.simulation.dsol.logger.Cat;
import nl.tudelft.simulation.dsol.logger.SimLogger;
import nl.tudelft.simulation.dsol.model.DSOLModel;
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
import nl.tudelft.simulation.event.EventListenerInterface;
import nl.tudelft.simulation.event.EventProducer;
import nl.tudelft.simulation.event.EventType;
import nl.tudelft.simulation.naming.context.ContextUtil;

/**
 * The Experiment specifies the parameters for a simulation experiment.
 * <p>
 * Copyright (c) 2002-2019 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
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
public class Experiment<A extends Comparable<A>, R extends Number & Comparable<R>, T extends SimTime<A, R, T>,
        S extends SimulatorInterface<A, R, T>> extends EventProducer implements EventListenerInterface, Serializable
{
    /** The default serial version UID for serializable classes. */
    private static final long serialVersionUID = 1L;

    /** END_OF_EXPERIMENT_EVENT is fired when the experiment is ended. */
    public static final EventType END_OF_EXPERIMENT_EVENT = new EventType("END_OF_EXPERIMENT_EVENT");

    /** MODEL_CHANGED_EVENT is fired whenever the model is changed. */
    public static final EventType MODEL_CHANGED_EVENT = new EventType("MODEL_CHANGED_EVENT");

    /** SIMULATOR_CHANGED_EVENT is fired whenever the simulator is changed. */
    public static final EventType SIMULATOR_CHANGED_EVENT = new EventType("SIMULATOR_CHANGED_EVENT");

    /** replications are the replications of this experiment. */
    private List<? extends Replication<A, R, T, S>> replications;

    /** treatment represent the treatment of this experiment. */
    private Treatment<A, R, T> treatment = null;

    /** simulator reflects the simulator. */
    private S simulator;

    /** model reflects the model. */
    private DSOLModel<A, R, T, S> model;

    /** the description of this experiment. */
    private String description = null;

    /** the analyst for this experiment. */
    private String analyst = null;

    /** the current replication. */
    private int currentReplication = -1;

    /** are we already subscribed to the END_OF_REPLICATION_EVENT. */
    private boolean subscribed = false;

    /**
     * constructs a new Experiment.
     */
    public Experiment()
    {
        super();
    }

    /**
     * constructs a new Experiment.
     * @param treatment Treatment&lt;A,R,T&gt;; the treatment for this experiment
     * @param simulator S; the simulator
     * @param model DSOLModel&lt;A, R, T, S&gt;; the model to experiment with
     */
    public Experiment(final Treatment<A, R, T> treatment, final S simulator, final DSOLModel<A, R, T, S> model)
    {
        this.setSimulator(simulator);
        this.setTreatment(treatment);
        this.setModel(model);
    }

    /**
     * sets the simulator.
     * @param simulator S; the simulator
     */
    public final synchronized void setSimulator(final S simulator)
    {
        this.simulator = simulator;
        this.fireEvent(SIMULATOR_CHANGED_EVENT, simulator);
    }

    /**
     * returns the simulator.
     * @return SimulatorInterface
     */
    public final S getSimulator()
    {
        return this.simulator;
    }

    /**
     * returns the model.
     * @return DSOLModel the model
     */
    public DSOLModel<A, R, T, S> getModel()
    {
        return this.model;
    }

    /**
     * @return Returns the replications.
     */
    public final List<? extends Replication<A, R, T, S>> getReplications()
    {
        return this.replications;
    }

    /**
     * @param replications List&lt;? extends Replication&lt;A, R, T, S&gt;&gt;; The replications to set.
     */
    public final void setReplications(final List<? extends Replication<A, R, T, S>> replications)
    {
        this.replications = replications;
    }

    /**
     * starts the experiment on a simulator.
     */
    @SuppressWarnings("checkstyle:designforextension")
    public synchronized void start()
    {
        try
        {
            this.notify(new Event(SimulatorInterface.END_REPLICATION_EVENT, this.simulator, Boolean.TRUE));
        }
        catch (RemoteException remoteException)
        {
            SimLogger.always().warn(remoteException, "notify");
        }
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("checkstyle:designforextension")
    public void notify(final EventInterface event) throws RemoteException
    {
        if (!this.subscribed)
        {
            this.simulator.addListener(this, SimulatorInterface.END_REPLICATION_EVENT, false);
            this.subscribed = true;
        }
        if (event.getType().equals(SimulatorInterface.END_REPLICATION_EVENT))
        {
            if (this.currentReplication < (this.getReplications().size() - 1))
            {
                // we can run the next replication
                try
                {
                    this.currentReplication++;
                    Replication<A, R, T, S> next = this.getReplications().get(this.currentReplication);
                    this.simulator.initialize(next, this.treatment.getReplicationMode());
                    this.simulator.start();
                }
                catch (Exception exception)
                {
                    SimLogger.always().error(exception);
                }
            }
            else
            {
                SimLogger.filter(Cat.DSOL).debug("Last replication carried out");
                // There is no experiment to run anymore
                this.fireEvent(Experiment.END_OF_EXPERIMENT_EVENT, true);
            }
        }
    }

    /**
     * sets the model on the experiment.
     * @param model DSOLModel&lt;A, R, T, S&gt;; the simulator model
     */
    public final synchronized void setModel(final DSOLModel<A, R, T, S> model)
    {
        this.model = model;
        this.fireEvent(MODEL_CHANGED_EVENT, model);
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
        this.treatment = treatment;
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("checkstyle:designforextension")
    public String toString()
    {
        String result = "Experiment[" + this.description + " ; treatment=" + this.treatment.toString() + "; simulator="
                + this.simulator.getClass().getTypeName() + "]";
        return result;
    }

    /**
     * resets the experiment.
     */
    @SuppressWarnings("checkstyle:designforextension")
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
     * @return the context of the experiment, based on the hashCode.
     * @throws NamingException if context could not be found or created.
     */
    public final Context getContext() throws NamingException
    {
        return ContextUtil.lookup(String.valueOf(hashCode()));
    }

    /**
     * remove the entire experiment tree from the context.
     * @throws NamingException if context could not be found or removed.
     */
    public final void removeFromContext() throws NamingException
    {
        ContextUtil.destroySubContext(String.valueOf(hashCode()));
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
        public TimeDouble(final Treatment.TimeDouble treatment, final S simulator, final DSOLModel.TimeDouble<S> model)
        {
            super(treatment, simulator, model);
        }

        /** {@inheritDoc} */
        @Override
        public DSOLModel.TimeDouble<S> getModel()
        {
            return (DSOLModel.TimeDouble<S>) super.getModel();
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
        public TimeFloat(final Treatment.TimeFloat treatment, final S simulator, final DSOLModel.TimeFloat<S> model)
        {
            super(treatment, simulator, model);
        }

        /** {@inheritDoc} */
        @Override
        public DSOLModel.TimeFloat<S> getModel()
        {
            return (DSOLModel.TimeFloat<S>) super.getModel();
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
        public TimeLong(final Treatment.TimeLong treatment, final S simulator, final DSOLModel.TimeLong<S> model)
        {
            super(treatment, simulator, model);
        }

        /** {@inheritDoc} */
        @Override
        public DSOLModel.TimeLong<S> getModel()
        {
            return (DSOLModel.TimeLong<S>) super.getModel();
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
                final DSOLModel.TimeDoubleUnit<S> model)
        {
            super(treatment, simulator, model);
        }

        /** {@inheritDoc} */
        @Override
        public DSOLModel.TimeDoubleUnit<S> getModel()
        {
            return (DSOLModel.TimeDoubleUnit<S>) super.getModel();
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
        public TimeFloatUnit(final Treatment.TimeFloatUnit treatment, final S simulator, final DSOLModel.TimeFloatUnit<S> model)
        {
            super(treatment, simulator, model);
        }

        /** {@inheritDoc} */
        @Override
        public DSOLModel.TimeFloatUnit<S> getModel()
        {
            return (DSOLModel.TimeFloatUnit<S>) super.getModel();
        }

        /** {@inheritDoc} */
        @Override
        public Treatment.TimeFloatUnit getTreatment()
        {
            return (Treatment.TimeFloatUnit) super.getTreatment();
        }
    }

    /**
     * Easy access class Experiment.CalendarDouble.
     * @param <S> the simulator to use
     */
    public static class CalendarDouble<S extends SimulatorInterface.CalendarDouble>
            extends Experiment<Calendar, Duration, SimTimeCalendarDouble, S>
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * constructs a new Experiment.
         */
        public CalendarDouble()
        {
            super();
        }

        /**
         * constructs a new Experiment.CalendarDouble.
         * @param treatment Treatment.CalendarDouble; the treatment for this experiment
         * @param simulator S; the simulator
         * @param model DSOLModel.CalendarDouble&lt;S&gt;; the model to experiment with
         */
        public CalendarDouble(final Treatment.CalendarDouble treatment, final S simulator,
                final DSOLModel.CalendarDouble<S> model)
        {
            super(treatment, simulator, model);
        }

        /** {@inheritDoc} */
        @Override
        public DSOLModel.CalendarDouble<S> getModel()
        {
            return (DSOLModel.CalendarDouble<S>) super.getModel();
        }

        /** {@inheritDoc} */
        @Override
        public Treatment.CalendarDouble getTreatment()
        {
            return (Treatment.CalendarDouble) super.getTreatment();
        }
    }

    /**
     * Easy access class Experiment.CalendarFloat.
     * @param <S> the simulator to use
     */
    public static class CalendarFloat<S extends SimulatorInterface.CalendarFloat>
            extends Experiment<Calendar, FloatDuration, SimTimeCalendarFloat, S>
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * constructs a new Experiment.
         */
        public CalendarFloat()
        {
            super();
        }

        /**
         * constructs a new Experiment.CalendarFloat.
         * @param treatment Treatment.CalendarFloat; the treatment for this experiment
         * @param simulator S; the simulator
         * @param model DSOLModel.CalendarFloat&lt;S&gt;; the model to experiment with
         */
        public CalendarFloat(final Treatment.CalendarFloat treatment, final S simulator, final DSOLModel.CalendarFloat<S> model)
        {
            super(treatment, simulator, model);
        }

        /** {@inheritDoc} */
        @Override
        public DSOLModel.CalendarFloat<S> getModel()
        {
            return (DSOLModel.CalendarFloat<S>) super.getModel();
        }

        /** {@inheritDoc} */
        @Override
        public Treatment.CalendarFloat getTreatment()
        {
            return (Treatment.CalendarFloat) super.getTreatment();
        }
    }

    /**
     * Easy access class Experiment.CalendarLong.
     * @param <S> the simulator to use
     */
    public static class CalendarLong<S extends SimulatorInterface.CalendarLong>
            extends Experiment<Calendar, Long, SimTimeCalendarLong, S>
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * constructs a new Experiment.
         */
        public CalendarLong()
        {
            super();
        }

        /**
         * constructs a new Experiment.CalendarLong.
         * @param treatment Treatment.CalendarLong; the treatment for this experiment
         * @param simulator S; the simulator
         * @param model DSOLModel.CalendarLong&lt;S&gt;; the model to experiment with
         */
        public CalendarLong(final Treatment.CalendarLong treatment, final S simulator, final DSOLModel.CalendarLong<S> model)
        {
            super(treatment, simulator, model);
        }

        /** {@inheritDoc} */
        @Override
        public DSOLModel.CalendarLong<S> getModel()
        {
            return (DSOLModel.CalendarLong<S>) super.getModel();
        }

        /** {@inheritDoc} */
        @Override
        public Treatment.CalendarLong getTreatment()
        {
            return (Treatment.CalendarLong) super.getTreatment();
        }
    }

}
