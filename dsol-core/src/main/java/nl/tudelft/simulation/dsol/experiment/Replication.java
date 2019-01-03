package nl.tudelft.simulation.dsol.experiment;

import java.io.Serializable;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.naming.Context;
import javax.naming.NamingException;

import org.djunits.value.vdouble.scalar.Duration;
import org.djunits.value.vdouble.scalar.Time;
import org.djunits.value.vfloat.scalar.FloatDuration;
import org.djunits.value.vfloat.scalar.FloatTime;

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
import nl.tudelft.simulation.jstats.streams.MersenneTwister;
import nl.tudelft.simulation.jstats.streams.StreamInterface;
import nl.tudelft.simulation.naming.context.ContextUtil;

/**
 * The replication of an Experiment.
 * <p>
 * Copyright (c) 2002-2019 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
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
public class Replication<A extends Comparable<A>, R extends Number & Comparable<R>, T extends SimTime<A, R, T>,
        S extends SimulatorInterface<A, R, T>> implements Serializable
{
    /** The default serial version UID for serializable classes. */
    private static final long serialVersionUID = 1L;

    /** streams used in the replication. */
    private Map<String, StreamInterface> streams = new HashMap<String, StreamInterface>();

    /** description the description of the replication. */
    private String description = "rep_no_description";

    /** the id of the replication. */
    private final String id;

    /** the experiment to which this replication belongs. */
    private final Experiment<A, R, T, S> experiment;

    /** the contextRoot of this replication. */
    private Context context = null;

    /**
     * constructs a new Replication.
     * @param id the id of the replication, which has to be unique within the experiment
     * @param experiment Experiment&lt;A,R,T&gt;; the experiment to which this replication belongs
     * @throws NamingException in case a context for the replication cannot be created
     */
    public Replication(final String id, final Experiment<A, R, T, S> experiment) throws NamingException
    {
        super();
        this.id = id;
        this.experiment = experiment;
        setContext();
        this.streams.put("default", new MersenneTwister(this.id.hashCode()));
    }

    /**
     * constructs a new Replication.
     * @param id the id of the replication, which has to be unique within the experiment
     * @param experiment Experiment&lt;A,R,T&gt;; the experiment to which this replication belongs
     * @throws NamingException in case a context for the replication cannot be created
     */
    public Replication(final int id, final Experiment<A, R, T, S> experiment) throws NamingException
    {
        super();
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
     * @param model DSOLModel&lt;A,R,T&gt;; the model for which this is the replication
     * @return a Replication object with corresponding experiment and treatment
     * @throws NamingException in case a context for the replication cannot be created
     * @param <A> the absolute storage type for the simulation time, e.g. Calendar, Duration, or Double.
     * @param <R> the relative type for time storage, e.g. Long for the Calendar. For most non-calendar types, the absolute and
     *            relative types are the same.
     * @param <T> the extended type itself to be able to implement a comparator on the simulation time.
     * @param <S> the simulator type, consistent with the simulation time
     */
    public static <A extends Comparable<A>, R extends Number & Comparable<R>, T extends SimTime<A, R, T>,
            S extends SimulatorInterface<A, R, T>> Replication<A, R, T, S> create(final String id, final T startTime,
                    final R warmupPeriod, final R runLength, final DSOLModel<A, R, T, S> model) throws NamingException
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
        this.context = ContextUtil.lookup(this.experiment.getContext(), String.valueOf(this.id.hashCode()));
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
    @SuppressWarnings("checkstyle:designforextension")
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
    public Experiment<A, R, T, S> getExperiment()
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
    public final Context getContext()
    {
        return this.context;
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("checkstyle:designforextension")
    public String toString()
    {
        String result = super.toString() + " ; " + this.getDescription() + " ; streams=[";
        for (StreamInterface stream : this.streams.values())
        {
            result = result + stream.toString() + " ; ";
        }
        result = result.substring(0, result.length() - 2) + "]";
        return result;
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
         * @param experiment Experiment.TimeDouble; the experiment to which this replication belongs
         * @throws NamingException in case a context for the replication cannot be created
         */
        public TimeDouble(final String id, final Experiment.TimeDouble<S> experiment) throws NamingException
        {
            super(id, experiment);
        }

        /**
         * constructs a new Replication.TimeDouble.
         * @param id int; the id of the replication; should be unique within the experiment.
         * @param experiment Experiment.TimeDouble; the experiment to which this replication belongs
         * @throws NamingException in case a context for the replication cannot be created
         */
        public TimeDouble(final int id, final Experiment.TimeDouble<S> experiment) throws NamingException
        {
            super(id, experiment);
        }

        /**
         * constructs a stand-alone Replication and make a treatment and experiment as well.
         * @param id String; the id of the replication.
         * @param startTime double; the start time
         * @param warmupPeriod double; the warmup period, included in the runlength (!)
         * @param runLength double; the total length of the run, including the warm-up period.
         * @param model DSOLModel.TimeDouble; the model for which this is the replication
         * @return a Replication object with corresponding experiment and treatment
         * @throws NamingException in case a context for the replication cannot be created
         * @param <S> the simulator to use
         */
        public static <S extends SimulatorInterface.TimeDouble> Replication.TimeDouble<S> create(final String id,
                final double startTime, final double warmupPeriod, final double runLength, final DSOLModel.TimeDouble<S> model)
                throws NamingException
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
        public Experiment.TimeDouble<S> getExperiment()
        {
            return (Experiment.TimeDouble<S>) super.getExperiment();
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
         * @param experiment Experiment.TimeFloat; the experiment to which this replication belongs
         * @throws NamingException in case a context for the replication cannot be created
         */
        public TimeFloat(final String id, final Experiment.TimeFloat<S> experiment) throws NamingException
        {
            super(id, experiment);
        }

        /**
         * constructs a new Replication.TimeFloat.
         * @param id int; the id of the replication; should be unique within the experiment.
         * @param experiment Experiment.TimeFloat; the experiment to which this replication belongs
         * @throws NamingException in case a context for the replication cannot be created
         */
        public TimeFloat(final int id, final Experiment.TimeFloat<S> experiment) throws NamingException
        {
            super(id, experiment);
        }

        /**
         * constructs a stand-alone Replication and make a treatment and experiment as well.
         * @param id String; the id of the replication.
         * @param startTime double; the start time
         * @param warmupPeriod double; the warmup period, included in the runlength (!)
         * @param runLength double; the total length of the run, including the warm-up period.
         * @param model DSOLModel.TimeFloat; the model for which this is the replication
         * @return a Replication object with corresponding experiment and treatment
         * @throws NamingException in case a context for the replication cannot be created
         * @param <S> the simulator to use
         */
        public static <S extends SimulatorInterface.TimeFloat> Replication.TimeFloat<S> create(final String id,
                final float startTime, final float warmupPeriod, final float runLength, final DSOLModel.TimeFloat<S> model)
                throws NamingException
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
        public Experiment.TimeFloat<S> getExperiment()
        {
            return (Experiment.TimeFloat<S>) super.getExperiment();
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
         * @param experiment Experiment.TimeLong; the experiment to which this replication belongs
         * @throws NamingException in case a context for the replication cannot be created
         */
        public TimeLong(final String id, final Experiment.TimeLong<S> experiment) throws NamingException
        {
            super(id, experiment);
        }

        /**
         * constructs a new Replication.TimeLong.
         * @param id int; the id of the replication; should be unique within the experiment.
         * @param experiment Experiment.TimeLong; the experiment to which this replication belongs
         * @throws NamingException in case a context for the replication cannot be created
         */
        public TimeLong(final int id, final Experiment.TimeLong<S> experiment) throws NamingException
        {
            super(id, experiment);
        }

        /**
         * constructs a stand-alone Replication and make a treatment and experiment as well.
         * @param id String; the id of the replication.
         * @param startTime long; the start time
         * @param warmupPeriod long; the warmup period, included in the runlength (!)
         * @param runLength long; the total length of the run, including the warm-up period.
         * @param model DSOLModel.TimeLong; the model for which this is the replication
         * @return a Replication object with corresponding experiment and treatment
         * @throws NamingException in case a context for the replication cannot be created
         * @param <S> the simulator to use
         */
        public static <S extends SimulatorInterface.TimeLong> Replication.TimeLong<S> create(final String id,
                final long startTime, final long warmupPeriod, final long runLength, final DSOLModel.TimeLong<S> model)
                throws NamingException
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
        public Experiment.TimeLong<S> getExperiment()
        {
            return (Experiment.TimeLong<S>) super.getExperiment();
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
         * @param experiment Experiment.TimeDoubleUnit; the experiment to which this replication belongs
         * @throws NamingException in case a context for the replication cannot be created
         */
        public TimeDoubleUnit(final String id, final Experiment.TimeDoubleUnit<S> experiment) throws NamingException
        {
            super(id, experiment);
        }

        /**
         * constructs a new Replication.TimeDoubleUnit.
         * @param id int; the id of the replication; should be unique within the experiment.
         * @param experiment Experiment.TimeDoubleUnit; the experiment to which this replication belongs
         * @throws NamingException in case a context for the replication cannot be created
         */
        public TimeDoubleUnit(final int id, final Experiment.TimeDoubleUnit<S> experiment) throws NamingException
        {
            super(id, experiment);
        }

        /**
         * constructs a stand-alone Replication and make a treatment and experiment as well.
         * @param id String; the id of the replication.
         * @param startTime Time; the start time
         * @param warmupPeriod Duration; the warmup period, included in the runlength (!)
         * @param runLength Duration; the total length of the run, including the warm-up period.
         * @param model DSOLModel.TimeDoubleUnit; the model for which this is the replication
         * @return a Replication object with corresponding experiment and treatment
         * @throws NamingException in case a context for the replication cannot be created
         * @param <S> the simulator to use
         */
        public static <S extends SimulatorInterface.TimeDoubleUnit> Replication.TimeDoubleUnit<S> create(final String id,
                final Time startTime, final Duration warmupPeriod, final Duration runLength,
                final DSOLModel.TimeDoubleUnit<S> model) throws NamingException
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
        public Experiment.TimeDoubleUnit<S> getExperiment()
        {
            return (Experiment.TimeDoubleUnit<S>) super.getExperiment();
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
         * @param experiment Experiment.TimeFloatUnit; the experiment to which this replication belongs
         * @throws NamingException in case a context for the replication cannot be created
         */
        public TimeFloatUnit(final String id, final Experiment.TimeFloatUnit<S> experiment) throws NamingException
        {
            super(id, experiment);
        }

        /**
         * constructs a new Replication.TimeFloatUnit.
         * @param id int; the id of the replication; should be unique within the experiment.
         * @param experiment Experiment.TimeFloatUnit; the experiment to which this replication belongs
         * @throws NamingException in case a context for the replication cannot be created
         */
        public TimeFloatUnit(final int id, final Experiment.TimeFloatUnit<S> experiment) throws NamingException
        {
            super(id, experiment);
        }

        /**
         * constructs a stand-alone Replication and make a treatment and experiment as well.
         * @param id String; the id of the replication.
         * @param startTime FloatTime; the start time
         * @param warmupPeriod FloatDuration; the warmup period, included in the runlength (!)
         * @param runLength FloatDuration; the total length of the run, including the warm-up period.
         * @param model DSOLModel.TimeFloatUnit; the model for which this is the replication
         * @return a Replication object with corresponding experiment and treatment
         * @throws NamingException in case a context for the replication cannot be created
         * @param <S> the simulator to use
         */
        public static <S extends SimulatorInterface.TimeFloatUnit> Replication.TimeFloatUnit<S> create(final String id,
                final FloatTime startTime, final FloatDuration warmupPeriod, final FloatDuration runLength,
                final DSOLModel.TimeFloatUnit<S> model) throws NamingException
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
        public Experiment.TimeFloatUnit<S> getExperiment()
        {
            return (Experiment.TimeFloatUnit<S>) super.getExperiment();
        }

        /** {@inheritDoc} */
        @Override
        public Treatment.TimeFloatUnit getTreatment()
        {
            return (Treatment.TimeFloatUnit) super.getTreatment();
        }
    }

    /**
     * Easy access class Replication.CalendarDouble.
     * @param <S> the simulator to use
     */
    public static class CalendarDouble<S extends SimulatorInterface.CalendarDouble>
            extends Replication<Calendar, Duration, SimTimeCalendarDouble, S>
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * constructs a new Replication.CalendarDouble.
         * @param id String; the id of the replication; should be unique within the experiment.
         * @param experiment Experiment.CalendarDouble; the experiment to which this replication belongs
         * @throws NamingException in case a context for the replication cannot be created
         */
        public CalendarDouble(final String id, final Experiment.CalendarDouble<S> experiment) throws NamingException
        {
            super(id, experiment);
        }

        /**
         * constructs a new Replication.CalendarDouble.
         * @param id int; the id of the replication; should be unique within the experiment.
         * @param experiment Experiment.CalendarDouble; the experiment to which this replication belongs
         * @throws NamingException in case a context for the replication cannot be created
         */
        public CalendarDouble(final int id, final Experiment.CalendarDouble<S> experiment) throws NamingException
        {
            super(id, experiment);
        }

        /**
         * constructs a stand-alone Replication and make a treatment and experiment as well.
         * @param id String; the id of the replication.
         * @param startTime Calendar; the start time
         * @param warmupPeriod Duration; the warmup period, included in the runlength (!)
         * @param runLength Duration; the total length of the run, including the warm-up period.
         * @param model DSOLModel.CalendarDouble; the model for which this is the replication
         * @return a Replication object with corresponding experiment and treatment
         * @throws NamingException in case a context for the replication cannot be created
         * @param <S> the simulator to use
         */
        public static <S extends SimulatorInterface.CalendarDouble> Replication.CalendarDouble<S> create(final String id,
                final Calendar startTime, final Duration warmupPeriod, final Duration runLength,
                final DSOLModel.CalendarDouble<S> model) throws NamingException
        {
            Experiment.CalendarDouble<S> experiment = new Experiment.CalendarDouble<S>();
            experiment.setModel(model);
            Treatment.CalendarDouble treatment =
                    new Treatment.CalendarDouble(experiment, "Treatment for " + id, startTime, warmupPeriod, runLength);
            experiment.setTreatment(treatment);
            return new Replication.CalendarDouble<S>(id, experiment);
        }

        /** {@inheritDoc} */
        @Override
        public Experiment.CalendarDouble<S> getExperiment()
        {
            return (Experiment.CalendarDouble<S>) super.getExperiment();
        }

        /** {@inheritDoc} */
        @Override
        public Treatment.CalendarDouble getTreatment()
        {
            return (Treatment.CalendarDouble) super.getTreatment();
        }
    }

    /**
     * Easy access class Replication.CalendarFloat.
     * @param <S> the simulator to use
     */
    public static class CalendarFloat<S extends SimulatorInterface.CalendarFloat>
            extends Replication<Calendar, FloatDuration, SimTimeCalendarFloat, S>
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * constructs a new Replication.CalendarFloat.
         * @param id String; the id of the replication; should be unique within the experiment.
         * @param experiment Experiment.CalendarFloat; the experiment to which this replication belongs
         * @throws NamingException in case a context for the replication cannot be created
         */
        public CalendarFloat(final String id, final Experiment.CalendarFloat<S> experiment) throws NamingException
        {
            super(id, experiment);
        }

        /**
         * constructs a new Replication.CalendarFloat.
         * @param id int; the id of the replication; should be unique within the experiment.
         * @param experiment Experiment.CalendarFloat; the experiment to which this replication belongs
         * @throws NamingException in case a context for the replication cannot be created
         */
        public CalendarFloat(final int id, final Experiment.CalendarFloat<S> experiment) throws NamingException
        {
            super(id, experiment);
        }

        /**
         * constructs a stand-alone Replication and make a treatment and experiment as well.
         * @param id String; the id of the replication.
         * @param startTime Calendar; the start time
         * @param warmupPeriod FloatDuration; the warmup period, included in the runlength (!)
         * @param runLength FloatDuration; the total length of the run, including the warm-up period.
         * @param model DSOLModel.CalendarFloat; the model for which this is the replication
         * @return a Replication object with corresponding experiment and treatment
         * @throws NamingException in case a context for the replication cannot be created
         * @param <S> the simulator to use
         */
        public static <S extends SimulatorInterface.CalendarFloat> Replication.CalendarFloat<S> create(final String id,
                final Calendar startTime, final FloatDuration warmupPeriod, final FloatDuration runLength,
                final DSOLModel.CalendarFloat<S> model) throws NamingException
        {
            Experiment.CalendarFloat<S> experiment = new Experiment.CalendarFloat<S>();
            experiment.setModel(model);
            Treatment.CalendarFloat treatment =
                    new Treatment.CalendarFloat(experiment, "Treatment for " + id, startTime, warmupPeriod, runLength);
            experiment.setTreatment(treatment);
            return new Replication.CalendarFloat<S>(id, experiment);
        }

        /** {@inheritDoc} */
        @Override
        public Experiment.CalendarFloat<S> getExperiment()
        {
            return (Experiment.CalendarFloat<S>) super.getExperiment();
        }

        /** {@inheritDoc} */
        @Override
        public Treatment.CalendarFloat getTreatment()
        {
            return (Treatment.CalendarFloat) super.getTreatment();
        }
    }

    /**
     * Easy access class Replication.CalendarLong.
     * @param <S> the simulator to use
     */
    public static class CalendarLong<S extends SimulatorInterface.CalendarLong>
            extends Replication<Calendar, Long, SimTimeCalendarLong, S>
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * constructs a new Replication.CalendarLong.
         * @param id String; the id of the replication; should be unique within the experiment.
         * @param experiment Experiment.CalendarLong; the experiment to which this replication belongs
         * @throws NamingException in case a context for the replication cannot be created
         */
        public CalendarLong(final String id, final Experiment.CalendarLong<S> experiment) throws NamingException
        {
            super(id, experiment);
        }

        /**
         * constructs a new Replication.CalendarLong.
         * @param id int; the id of the replication; should be unique within the experiment.
         * @param experiment Experiment.CalendarLong; the experiment to which this replication belongs
         * @throws NamingException in case a context for the replication cannot be created
         */
        public CalendarLong(final int id, final Experiment.CalendarLong<S> experiment) throws NamingException
        {
            super(id, experiment);
        }

        /**
         * constructs a stand-alone Replication and make a treatment and experiment as well.
         * @param id String; the id of the replication.
         * @param startTime Calendar; the start time
         * @param warmupPeriod long; the warmup period, included in the runlength (!)
         * @param runLength long; the total length of the run, including the warm-up period.
         * @param model DSOLModel.CalendarLong; the model for which this is the replication
         * @return a Replication object with corresponding experiment and treatment
         * @throws NamingException in case a context for the replication cannot be created
         * @param <S> the simulator to use
         */
        public static <S extends SimulatorInterface.CalendarLong> Replication.CalendarLong<S> create(final String id,
                final Calendar startTime, final long warmupPeriod, final long runLength, final DSOLModel.CalendarLong<S> model)
                throws NamingException
        {
            Experiment.CalendarLong<S> experiment = new Experiment.CalendarLong<S>();
            experiment.setModel(model);
            Treatment.CalendarLong treatment =
                    new Treatment.CalendarLong(experiment, "Treatment for " + id, startTime, warmupPeriod, runLength);
            experiment.setTreatment(treatment);
            return new Replication.CalendarLong<S>(id, experiment);
        }

        /** {@inheritDoc} */
        @Override
        public Experiment.CalendarLong<S> getExperiment()
        {
            return (Experiment.CalendarLong<S>) super.getExperiment();
        }

        /** {@inheritDoc} */
        @Override
        public Treatment.CalendarLong getTreatment()
        {
            return (Treatment.CalendarLong) super.getTreatment();
        }
    }
}
