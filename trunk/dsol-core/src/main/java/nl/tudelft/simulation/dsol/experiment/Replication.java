package nl.tudelft.simulation.dsol.experiment;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

import javax.naming.NamingException;

import org.djunits.value.vdouble.scalar.Duration;
import org.djunits.value.vdouble.scalar.Time;
import org.djunits.value.vfloat.scalar.FloatDuration;
import org.djunits.value.vfloat.scalar.FloatTime;
import org.djutils.event.TimedEventType;
import org.djutils.metadata.MetaData;

import nl.tudelft.simulation.dsol.model.DSOLModel;
import nl.tudelft.simulation.dsol.simtime.SimTime;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simtime.SimTimeDoubleUnit;
import nl.tudelft.simulation.dsol.simtime.SimTimeFloat;
import nl.tudelft.simulation.dsol.simtime.SimTimeFloatUnit;
import nl.tudelft.simulation.dsol.simtime.SimTimeLong;
import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;
import nl.tudelft.simulation.jstats.streams.MersenneTwister;
import nl.tudelft.simulation.jstats.streams.StreamInterface;
import nl.tudelft.simulation.naming.context.ContextInterface;
import nl.tudelft.simulation.naming.context.util.ContextUtil;

/**
 * The replication of an Experiment.
 * <p>
 * Copyright (c) 2002-2021 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @param <A> the absolute storage type for the simulation time, e.g. Calendar, Duration, or Double.
 * @param <R> the relative type for time storage, e.g. Long for the Calendar. For most non-calendar types, the absolute and
 *            relative types are the same.
 * @param <T> the extended type itself to be able to implement a comparator on the simulation time.
 * @param <S> the simulator to use
 */
public class Replication<A extends Comparable<A> & Serializable, R extends Number & Comparable<R>, T extends SimTime<A, R, T>,
        S extends SimulatorInterface<A, R, T>> implements Serializable
{
    /** The default serial version UID for serializable classes. */
    private static final long serialVersionUID = 1L;

    /** START_REPLICATION_EVENT is fired when a replication is started. */
    public static final TimedEventType START_REPLICATION_EVENT =
            new TimedEventType(new MetaData("START_REPLICATION_EVENT", "Replication started"));

    /** END_REPLICATION_EVENT is fired when a replication is finished. */
    public static final TimedEventType END_REPLICATION_EVENT =
            new TimedEventType(new MetaData("END_REPLICATION_EVENT", "Replication ended"));

    /** WARMUP_EVENT is fired when the warmup period is over, and statistics have to be reset. */
    public static final TimedEventType WARMUP_EVENT = new TimedEventType(new MetaData("WARMUP_EVENT", "warmup time"));

    /** streams used in the replication. */
    private Map<String, StreamInterface> streams = new HashMap<String, StreamInterface>();

    /** description the description of the replication. */
    private String description = "rep_no_description";

    /** the id of the replication. */
    private final String id;

    /** the experiment to which this replication belongs. */
    private final Experiment<A, R, T, ? extends S> experiment;

    /** the contextRoot of this replication. */
    private ContextInterface context = null;

    /**
     * constructs a new Replication.
     * @param id String; the id of the replication, which has to be unique within the experiment
     * @param experiment Experiment&lt;A, R, T, ? extends S&gt;; the experiment to which this replication belongs
     * @throws NamingException in case a context for the replication cannot be created
     */
    public Replication(final String id, final Experiment<A, R, T, ? extends S> experiment) throws NamingException
    {
        this.id = id;
        this.experiment = experiment;
        setContext();
        this.streams.put("default", new MersenneTwister(this.id.hashCode()));
    }

    /**
     * constructs a new Replication.
     * @param id int; the id of the replication, which has to be unique within the experiment
     * @param experiment Experiment&lt;A, R, T, ? extends S&gt;; the experiment to which this replication belongs
     * @throws NamingException in case a context for the replication cannot be created
     */
    public Replication(final int id, final Experiment<A, R, T, ? extends S> experiment) throws NamingException
    {
        this.id = "" + id;
        this.experiment = experiment;
        setContext();
        this.streams.put("default", new MersenneTwister(this.id.hashCode()));
    }

    /**
     * constructs a stand-alone Replication and make a treatment and experiment as well.
     * @param id String; the id of the replication; should be unique within the experiment.
     * @param startTime T; the start time as a time object.
     * @param warmupPeriod R; the warmup period, included in the runlength (!)
     * @param runLength R; the total length of the run, including the warm-up period.
     * @param model DSOLModel&lt;A, R, T, ? extends S&gt;; the model for which this is the replication
     * @return a Replication object with corresponding experiment and treatment
     * @throws NamingException in case a context for the replication cannot be created
     * @param <A> the absolute storage type for the simulation time, e.g. Calendar, Duration, or Double.
     * @param <R> the relative type for time storage, e.g. Long for the Calendar. For most non-calendar types, the absolute and
     *            relative types are the same.
     * @param <T> the extended type itself to be able to implement a comparator on the simulation time.
     * @param <S> the simulator type, consistent with the simulation time
     */
    public static <A extends Comparable<A> & Serializable, R extends Number & Comparable<R>, T extends SimTime<A, R, T>,
            S extends SimulatorInterface<A, R, T>> Replication<A, R, T, S> create(final String id, final T startTime,
                    final R warmupPeriod, final R runLength, final DSOLModel<A, R, T, ? extends S> model) throws NamingException
    {
        Experiment<A, R, T, S> experiment = new Experiment<>();
        experiment.setModel(model);
        Treatment<A, R, T> treatment =
                new Treatment<A, R, T>(experiment, "Treatment for " + id, startTime, warmupPeriod, runLength);
        experiment.setTreatment(treatment);
        return new Replication<A, R, T, S>(id, experiment);
    }

    /**
     * set the context for this replication.
     * @throws NamingException in case a context for the experiment or replication cannot be created
     */
    private void setContext() throws NamingException
    {
        try
        {
            this.context =
                    ContextUtil.lookupOrCreateSubContext(this.experiment.getContext(), String.valueOf(this.id.hashCode()));
        }
        catch (RemoteException remoteException)
        {
            throw new NamingException(
                    "Cannot lookup or create context for experiment. Error is: " + remoteException.getMessage());
        }
    }

    /**
     * @return String the description of this replication
     */
    public final String getDescription()
    {
        return this.description;
    }

    /**
     * @return Map the streams of this replication
     */
    public final Map<String, StreamInterface> getStreams()
    {
        return this.streams;
    }

    /**
     * returns a specific stream.
     * @param name String; the name of the stream
     * @return StreamInterface the stream
     */
    public final StreamInterface getStream(final String name)
    {
        return this.streams.get(name);
    }

    /**
     * resets the streams.
     */
    public synchronized void reset()
    {
        for (StreamInterface stream : this.streams.values())
        {
            stream.reset();
        }
    }

    /**
     * Sets the description of this replication.
     * @param description String; the description of this replication
     */
    public final void setDescription(final String description)
    {
        this.description = description;
    }

    /**
     * sets the stream for this replication.
     * @param streams Map&lt;String,StreamInterface&gt;; the map of stream,name tuples
     */
    public final void setStreams(final Map<String, StreamInterface> streams)
    {
        this.streams = streams;
    }

    /**
     * @return Returns the experiment.
     */
    public Experiment<A, R, T, ? extends S> getExperiment()
    {
        return this.experiment;
    }

    /**
     * @return Returns the treatment. This is a convenience method to avoid the getExperiment().getTreatment() many times.
     */
    public Treatment<A, R, T> getTreatment()
    {
        return this.experiment.getTreatment();
    }

    /**
     * @return Returns the context.
     */
    public final ContextInterface getContext()
    {
        return this.context;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return "Replication " + this.getDescription();
    }

    /***********************************************************************************************************/
    /************************************* EASY ACCESS CLASS EXTENSIONS ****************************************/
    /***********************************************************************************************************/

    /**
     * Easy access class Replication.TimeDouble.
     * @param <S> the simulator to use
     */
    public static class TimeDouble<S extends SimulatorInterface.TimeDouble>
            extends Replication<Double, Double, SimTimeDouble, S>
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * constructs a new Replication.TimeDouble.
         * @param id String; the id of the replication; should be unique within the experiment.
         * @param experiment Experiment.TimeDouble&lt;? extends S&gt;; the experiment to which this replication belongs
         * @throws NamingException in case a context for the replication cannot be created
         */
        public TimeDouble(final String id, final Experiment.TimeDouble<? extends S> experiment) throws NamingException
        {
            super(id, experiment);
        }

        /**
         * constructs a new Replication.TimeDouble.
         * @param id int; the id of the replication; should be unique within the experiment.
         * @param experiment Experiment.TimeDouble&lt;? extends S&gt;; the experiment to which this replication belongs
         * @throws NamingException in case a context for the replication cannot be created
         */
        public TimeDouble(final int id, final Experiment.TimeDouble<? extends S> experiment) throws NamingException
        {
            super(id, experiment);
        }

        /**
         * constructs a stand-alone Replication and make a treatment and experiment as well.
         * @param id String; the id of the replication.
         * @param startTime double; the start time
         * @param warmupPeriod double; the warmup period, included in the runlength (!)
         * @param runLength double; the total length of the run, including the warm-up period.
         * @param model DSOLModel.TimeDouble&lt;? extends S&gt;; the model for which this is the replication
         * @return a Replication object with corresponding experiment and treatment
         * @throws NamingException in case a context for the replication cannot be created
         * @param <S> the simulator to use
         */
        public static <S extends SimulatorInterface.TimeDouble> Replication.TimeDouble<S> create(final String id,
                final double startTime, final double warmupPeriod, final double runLength,
                final DSOLModel.TimeDouble<? extends S> model) throws NamingException
        {
            Experiment.TimeDouble<S> experiment = new Experiment.TimeDouble<S>();
            experiment.setModel(model);
            Treatment.TimeDouble treatment =
                    new Treatment.TimeDouble(experiment, "Treatment for " + id, startTime, warmupPeriod, runLength);
            experiment.setTreatment(treatment);
            return new Replication.TimeDouble<S>(id, experiment);
        }

        /** {@inheritDoc} */
        @Override
        public Experiment.TimeDouble<? extends S> getExperiment()
        {
            return (Experiment.TimeDouble<? extends S>) super.getExperiment();
        }

        /** {@inheritDoc} */
        @Override
        public Treatment.TimeDouble getTreatment()
        {
            return (Treatment.TimeDouble) super.getTreatment();
        }
    }

    /**
     * Easy access class Replication.TimeFloat.
     * @param <S> the simulator to use
     */
    public static class TimeFloat<S extends SimulatorInterface.TimeFloat> extends Replication<Float, Float, SimTimeFloat, S>
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * constructs a new Replication.TimeFloat.
         * @param id String; the id of the replication; should be unique within the experiment.
         * @param experiment Experiment.TimeFloat&lt;? extends S&gt;; the experiment to which this replication belongs
         * @throws NamingException in case a context for the replication cannot be created
         */
        public TimeFloat(final String id, final Experiment.TimeFloat<? extends S> experiment) throws NamingException
        {
            super(id, experiment);
        }

        /**
         * constructs a new Replication.TimeFloat.
         * @param id int; the id of the replication; should be unique within the experiment.
         * @param experiment Experiment.TimeFloat&lt;? extends S&gt;; the experiment to which this replication belongs
         * @throws NamingException in case a context for the replication cannot be created
         */
        public TimeFloat(final int id, final Experiment.TimeFloat<? extends S> experiment) throws NamingException
        {
            super(id, experiment);
        }

        /**
         * constructs a stand-alone Replication and make a treatment and experiment as well.
         * @param id String; the id of the replication.
         * @param startTime float; the start time
         * @param warmupPeriod float; the warmup period, included in the runlength (!)
         * @param runLength float; the total length of the run, including the warm-up period.
         * @param model DSOLModel.TimeFloat&lt;S&gt;; the model for which this is the replication
         * @return a Replication object with corresponding experiment and treatment
         * @throws NamingException in case a context for the replication cannot be created
         * @param <S> the simulator to use
         */
        public static <S extends SimulatorInterface.TimeFloat> Replication.TimeFloat<S> create(final String id,
                final float startTime, final float warmupPeriod, final float runLength,
                final DSOLModel.TimeFloat<? extends S> model) throws NamingException
        {
            Experiment.TimeFloat<S> experiment = new Experiment.TimeFloat<S>();
            experiment.setModel(model);
            Treatment.TimeFloat treatment =
                    new Treatment.TimeFloat(experiment, "Treatment for " + id, startTime, warmupPeriod, runLength);
            experiment.setTreatment(treatment);
            return new Replication.TimeFloat<S>(id, experiment);
        }

        /** {@inheritDoc} */
        @Override
        public Experiment.TimeFloat<? extends S> getExperiment()
        {
            return (Experiment.TimeFloat<? extends S>) super.getExperiment();
        }

        /** {@inheritDoc} */
        @Override
        public Treatment.TimeFloat getTreatment()
        {
            return (Treatment.TimeFloat) super.getTreatment();
        }
    }

    /**
     * Easy access class Replication.TimeLong.
     * @param <S> the simulator to use
     */
    public static class TimeLong<S extends SimulatorInterface.TimeLong> extends Replication<Long, Long, SimTimeLong, S>
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * constructs a new Replication.TimeLong.
         * @param id String; the id of the replication; should be unique within the experiment.
         * @param experiment Experiment.TimeLong&lt;? extends S&gt;; the experiment to which this replication belongs
         * @throws NamingException in case a context for the replication cannot be created
         */
        public TimeLong(final String id, final Experiment.TimeLong<? extends S> experiment) throws NamingException
        {
            super(id, experiment);
        }

        /**
         * constructs a new Replication.TimeLong.
         * @param id int; the id of the replication; should be unique within the experiment.
         * @param experiment Experiment.TimeLong&lt;? extends S&gt;; the experiment to which this replication belongs
         * @throws NamingException in case a context for the replication cannot be created
         */
        public TimeLong(final int id, final Experiment.TimeLong<? extends S> experiment) throws NamingException
        {
            super(id, experiment);
        }

        /**
         * constructs a stand-alone Replication and make a treatment and experiment as well.
         * @param id String; the id of the replication.
         * @param startTime long; the start time
         * @param warmupPeriod long; the warmup period, included in the runlength (!)
         * @param runLength long; the total length of the run, including the warm-up period.
         * @param model DSOLModel.TimeLong&lt;? extends S&gt;; the model for which this is the replication
         * @return a Replication object with corresponding experiment and treatment
         * @throws NamingException in case a context for the replication cannot be created
         * @param <S> the simulator to use
         */
        public static <S extends SimulatorInterface.TimeLong> Replication.TimeLong<S> create(final String id,
                final long startTime, final long warmupPeriod, final long runLength,
                final DSOLModel.TimeLong<? extends S> model) throws NamingException
        {
            Experiment.TimeLong<S> experiment = new Experiment.TimeLong<S>();
            experiment.setModel(model);
            Treatment.TimeLong treatment =
                    new Treatment.TimeLong(experiment, "Treatment for " + id, startTime, warmupPeriod, runLength);
            experiment.setTreatment(treatment);
            return new Replication.TimeLong<S>(id, experiment);
        }

        /** {@inheritDoc} */
        @Override
        public Experiment.TimeLong<? extends S> getExperiment()
        {
            return (Experiment.TimeLong<? extends S>) super.getExperiment();
        }

        /** {@inheritDoc} */
        @Override
        public Treatment.TimeLong getTreatment()
        {
            return (Treatment.TimeLong) super.getTreatment();
        }
    }

    /**
     * Easy access class Replication.TimeDoubleUnit.
     * @param <S> the simulator to use
     */
    public static class TimeDoubleUnit<S extends SimulatorInterface.TimeDoubleUnit>
            extends Replication<Time, Duration, SimTimeDoubleUnit, S>
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * constructs a new Replication.TimeDoubleUnit.
         * @param id String; the id of the replication; should be unique within the experiment.
         * @param experiment Experiment.TimeDoubleUnit&lt;? extends S&gt;; the experiment to which this replication belongs
         * @throws NamingException in case a context for the replication cannot be created
         */
        public TimeDoubleUnit(final String id, final Experiment.TimeDoubleUnit<? extends S> experiment) throws NamingException
        {
            super(id, experiment);
        }

        /**
         * constructs a new Replication.TimeDoubleUnit.
         * @param id int; the id of the replication; should be unique within the experiment.
         * @param experiment Experiment.TimeDoubleUnit&lt;? extends S&gt;; the experiment to which this replication belongs
         * @throws NamingException in case a context for the replication cannot be created
         */
        public TimeDoubleUnit(final int id, final Experiment.TimeDoubleUnit<? extends S> experiment) throws NamingException
        {
            super(id, experiment);
        }

        /**
         * constructs a stand-alone Replication and make a treatment and experiment as well.
         * @param id String; the id of the replication.
         * @param startTime Time; the start time
         * @param warmupPeriod Duration; the warmup period, included in the runlength (!)
         * @param runLength Duration; the total length of the run, including the warm-up period.
         * @param model DSOLModel.TimeDoubleUnit&lt;? extends S&gt;; the model for which this is the replication
         * @return a Replication object with corresponding experiment and treatment
         * @throws NamingException in case a context for the replication cannot be created
         * @param <S> the simulator to use
         */
        public static <S extends SimulatorInterface.TimeDoubleUnit> Replication.TimeDoubleUnit<S> create(final String id,
                final Time startTime, final Duration warmupPeriod, final Duration runLength,
                final DSOLModel.TimeDoubleUnit<? extends S> model) throws NamingException
        {
            Experiment.TimeDoubleUnit<S> experiment = new Experiment.TimeDoubleUnit<S>();
            experiment.setModel(model);
            Treatment.TimeDoubleUnit treatment =
                    new Treatment.TimeDoubleUnit(experiment, "Treatment for " + id, startTime, warmupPeriod, runLength);
            experiment.setTreatment(treatment);
            return new Replication.TimeDoubleUnit<S>(id, experiment);
        }

        /** {@inheritDoc} */
        @Override
        public Experiment.TimeDoubleUnit<? extends S> getExperiment()
        {
            return (Experiment.TimeDoubleUnit<? extends S>) super.getExperiment();
        }

        /** {@inheritDoc} */
        @Override
        public Treatment.TimeDoubleUnit getTreatment()
        {
            return (Treatment.TimeDoubleUnit) super.getTreatment();
        }
    }

    /**
     * Easy access class Replication.TimeDoubleUnit.
     * @param <S> the simulator to use
     */
    public static class TimeFloatUnit<S extends SimulatorInterface.TimeFloatUnit>
            extends Replication<FloatTime, FloatDuration, SimTimeFloatUnit, S>
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * constructs a new Replication.TimeFloatUnit.
         * @param id String; the id of the replication; should be unique within the experiment.
         * @param experiment Experiment.TimeFloatUnit&lt;? extends S&gt;; the experiment to which this replication belongs
         * @throws NamingException in case a context for the replication cannot be created
         */
        public TimeFloatUnit(final String id, final Experiment.TimeFloatUnit<? extends S> experiment) throws NamingException
        {
            super(id, experiment);
        }

        /**
         * constructs a new Replication.TimeFloatUnit.
         * @param id int; the id of the replication; should be unique within the experiment.
         * @param experiment Experiment.TimeFloatUnit&lt;? extends S&gt;; the experiment to which this replication belongs
         * @throws NamingException in case a context for the replication cannot be created
         */
        public TimeFloatUnit(final int id, final Experiment.TimeFloatUnit<? extends S> experiment) throws NamingException
        {
            super(id, experiment);
        }

        /**
         * constructs a stand-alone Replication and make a treatment and experiment as well.
         * @param id String; the id of the replication.
         * @param startTime FloatTime; the start time
         * @param warmupPeriod FloatDuration; the warmup period, included in the runlength (!)
         * @param runLength FloatDuration; the total length of the run, including the warm-up period.
         * @param model DSOLModel.TimeFloatUnit&lt;? extends S&gt;; the model for which this is the replication
         * @return a Replication object with corresponding experiment and treatment
         * @throws NamingException in case a context for the replication cannot be created
         * @param <S> the simulator to use
         */
        public static <S extends SimulatorInterface.TimeFloatUnit> Replication.TimeFloatUnit<S> create(final String id,
                final FloatTime startTime, final FloatDuration warmupPeriod, final FloatDuration runLength,
                final DSOLModel.TimeFloatUnit<? extends S> model) throws NamingException
        {
            Experiment.TimeFloatUnit<S> experiment = new Experiment.TimeFloatUnit<S>();
            experiment.setModel(model);
            Treatment.TimeFloatUnit treatment =
                    new Treatment.TimeFloatUnit(experiment, "Treatment for " + id, startTime, warmupPeriod, runLength);
            experiment.setTreatment(treatment);
            return new Replication.TimeFloatUnit<S>(id, experiment);
        }

        /** {@inheritDoc} */
        @Override
        public Experiment.TimeFloatUnit<? extends S> getExperiment()
        {
            return (Experiment.TimeFloatUnit<? extends S>) super.getExperiment();
        }

        /** {@inheritDoc} */
        @Override
        public Treatment.TimeFloatUnit getTreatment()
        {
            return (Treatment.TimeFloatUnit) super.getTreatment();
        }
    }

}
