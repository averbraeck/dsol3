package nl.tudelft.simulation.dsol.experiment;

import java.io.Serializable;

import org.djunits.value.vdouble.scalar.Duration;
import org.djunits.value.vdouble.scalar.Time;
import org.djunits.value.vfloat.scalar.FloatDuration;
import org.djunits.value.vfloat.scalar.FloatTime;

import nl.tudelft.simulation.dsol.model.inputparameters.InputParameterMap;
import nl.tudelft.simulation.dsol.simtime.SimTime;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simtime.SimTimeDoubleUnit;
import nl.tudelft.simulation.dsol.simtime.SimTimeFloat;
import nl.tudelft.simulation.dsol.simtime.SimTimeFloatUnit;
import nl.tudelft.simulation.dsol.simtime.SimTimeLong;
import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;

/**
 * The treatment is comprises the specification of input data, the runControl and the specification of output data. (Sol:1982,
 * Oeren &amp; Zeigler:1979).
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
 * @param <T> the extended type itself to be able to implement functions on the simulation time.
 */
public class Treatment<A extends Comparable<A> & Serializable, R extends Number & Comparable<R>, T extends SimTime<A, R, T>>
        implements Serializable
{
    /** The default serial version UID for serializable classes. */
    private static final long serialVersionUID = 1L;

    /** the replication mode. */
    private final ReplicationMode replicationMode;

    /** the experiment to which this treatment belongs. */
    private final Experiment<A, R, T, ? extends SimulatorInterface<A, R, T>> experiment;

    /** warmupPeriod is the warmup period. */
    private final R warmupPeriod;

    /** runLength reflects the runLength of the treatment. */
    private final R runLength;

    /** the start time of the simulation. */
    private final T startTime;

    /** the end time of the simulation. */
    private final T endTime;

    /** the warmup time of the simulation (included in the total run length). */
    private final T warmupTime;

    /** the id. */
    private final String id;

    /**
     * constructs a Treatment.
     * @param experiment Experiment&lt;A, R, T, ? extends SimulatorInterface&lt;A, R, T&gt;&gt;; reflects the experiment
     * @param id String; an id to recognize the treatment
     * @param startTime T; the absolute start time of a run (can be zero)
     * @param warmupPeriod R; the relative warmup time of a run (can be zero), &lt;i&gt;included&lt;/i&gt; in the runLength
     * @param runLength R; the run length of a run (relative to the start time)
     * @param replicationMode ReplicationMode; the replication mode of this treatment
     */
    public Treatment(final Experiment<A, R, T, ? extends SimulatorInterface<A, R, T>> experiment, final String id,
            final T startTime, final R warmupPeriod, final R runLength, final ReplicationMode replicationMode)
    {
        super();
        this.experiment = experiment;
        this.id = id;
        this.startTime = startTime.copy();
        this.warmupPeriod = warmupPeriod;
        this.runLength = runLength;
        this.endTime = startTime.copy();
        this.endTime.add(runLength);
        this.warmupTime = startTime.copy();
        this.warmupTime.add(warmupPeriod);
        this.replicationMode = replicationMode;
    }

    /**
     * constructs a Treatment.
     * @param experiment Experiment&lt;A, R, T, ? extends SimulatorInterface&lt;A, R, T&gt;&gt;; reflects the experiment
     * @param id String; an id to recognize the treatment
     * @param startTime T; the absolute start time of a run (can be zero)
     * @param warmupPeriod R; the relative warmup time of a run (can be zero), &lt;i&gt;included&lt;/i&gt; in the runLength
     * @param runLength R; the run length of a run (relative to the start time)
     */
    public Treatment(final Experiment<A, R, T, ? extends SimulatorInterface<A, R, T>> experiment, final String id,
            final T startTime, final R warmupPeriod, final R runLength)
    {
        this(experiment, id, startTime, warmupPeriod, runLength, ReplicationMode.TERMINATING);
    }

    /**
     * @return the experiment
     */
    public Experiment<A, R, T, ? extends SimulatorInterface<A, R, T>> getExperiment()
    {
        return this.experiment;
    }

    /**
     * @return the input parameter map for this treatment
     */
    public final InputParameterMap getInputParameterMap()
    {
        return this.experiment.getModel().getInputParameterMap();
    }

    /**
     * @return the replication mode of this treatment.
     */
    public final ReplicationMode getReplicationMode()
    {
        return this.replicationMode;
    }

    /**
     * @return the runLength.
     */
    public final R getRunLength()
    {
        return this.runLength;
    }

    /**
     * @return Returns the warmupPeriod.
     */
    public final R getWarmupPeriod()
    {
        return this.warmupPeriod;
    }

    /**
     * @return the absolute end time of the simulation that can be compared with the simulator time.
     */
    public final A getEndTime()
    {
        return this.endTime.get();
    }

    /**
     * @return startTime
     */
    public final A getStartTime()
    {
        return this.startTime.get();
    }

    /**
     * @return warmupTime
     */
    public final A getWarmupTime()
    {
        return this.warmupTime.get();
    }

    /**
     * @return the absolute end time of the simulation that can be compared with the simulator time.
     */
    public final T getEndSimTime()
    {
        return this.endTime;
    }

    /**
     * @return startTime
     */
    public final T getStartSimTime()
    {
        return this.startTime;
    }

    /**
     * @return warmupTime
     */
    public final T getWarmupSimTime()
    {
        return this.warmupTime;
    }

    /**
     * @return id
     */
    public final String getId()
    {
        return this.id;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        String result = "[Treatment; warmup=" + this.warmupPeriod + " ; runLength=" + this.runLength + "]";
        return result;
    }

    /***********************************************************************************************************/
    /************************************* EASY ACCESS CLASS EXTENSIONS ****************************************/
    /***********************************************************************************************************/

    /**
     * Easy access class Treatment.TimeDouble.
     */
    public static class TimeDouble extends Treatment<Double, Double, SimTimeDouble>
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * constructs a Treatment.TimeDouble.
         * @param experiment Experiment.TimeDouble&lt;? extends SimulatorInterface.TimeDouble&gt;; reflects the experiment
         * @param id String; an id to recognize the treatment
         * @param startTime Double; the absolute start time of a run (can be zero)
         * @param warmupPeriod Double; the relative warmup time of a run (can be zero), &lt;i&gt;included&lt;/i&gt; in the
         *            runLength
         * @param runLength Double; the run length of a run (relative to the start time)
         * @param replicationMode ReplicationMode; the replication mode of this treatment
         */
        public TimeDouble(final Experiment.TimeDouble<? extends SimulatorInterface.TimeDouble> experiment, final String id,
                final Double startTime, final Double warmupPeriod, final Double runLength,
                final ReplicationMode replicationMode)
        {
            super(experiment, id, new SimTimeDouble(startTime), warmupPeriod, runLength, replicationMode);
        }

        /**
         * constructs a Treatment.TimeDouble.
         * @param experiment Experiment.TimeDouble&lt;? extends SimulatorInterface.TimeDouble&gt;; reflects the experiment
         * @param id String; an id to recognize the treatment
         * @param startTime Double; the absolute start time of a run (can be zero)
         * @param warmupPeriod Double; the relative warmup time of a run (can be zero), &lt;i&gt;included&lt;/i&gt; in the
         *            runLength
         * @param runLength Double; the run length of a run (relative to the start time)
         */
        public TimeDouble(final Experiment.TimeDouble<? extends SimulatorInterface.TimeDouble> experiment, final String id,
                final Double startTime, final Double warmupPeriod, final Double runLength)
        {
            super(experiment, id, new SimTimeDouble(startTime), warmupPeriod, runLength);
        }

        /** {@inheritDoc} */
        @SuppressWarnings("unchecked")
        @Override
        public Experiment.TimeDouble<? extends SimulatorInterface.TimeDouble> getExperiment()
        {
            return (Experiment.TimeDouble<? extends SimulatorInterface.TimeDouble>) super.getExperiment();
        }
    }

    /**
     * Easy access class Treatment.TimeFloat.
     */
    public static class TimeFloat extends Treatment<Float, Float, SimTimeFloat>
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * constructs a Treatment.TimeFloat.
         * @param experiment Experiment.TimeFloat&lt;? extends SimulatorInterface.TimeFloat&gt;; reflects the experiment
         * @param id String; an id to recognize the treatment
         * @param startTime Float; the absolute start time of a run (can be zero)
         * @param warmupPeriod Float; the relative warmup time of a run (can be zero), &lt;i&gt;included&lt;/i&gt; in the
         *            runLength
         * @param runLength Float; the run length of a run (relative to the start time)
         * @param replicationMode ReplicationMode; the replication mode of this treatment
         */
        public TimeFloat(final Experiment.TimeFloat<? extends SimulatorInterface.TimeFloat> experiment, final String id,
                final Float startTime, final Float warmupPeriod, final Float runLength, final ReplicationMode replicationMode)
        {
            super(experiment, id, new SimTimeFloat(startTime), warmupPeriod, runLength, replicationMode);
        }

        /**
         * constructs a Treatment.TimeFloat.
         * @param experiment Experiment.TimeFloat&lt;? extends SimulatorInterface.TimeFloat&gt;; reflects the experiment
         * @param id String; an id to recognize the treatment
         * @param startTime Float; the absolute start time of a run (can be zero)
         * @param warmupPeriod Float; the relative warmup time of a run (can be zero), &lt;i&gt;included&lt;/i&gt; in the
         *            runLength
         * @param runLength Float; the run length of a run (relative to the start time)
         */
        public TimeFloat(final Experiment.TimeFloat<? extends SimulatorInterface.TimeFloat> experiment, final String id,
                final Float startTime, final Float warmupPeriod, final Float runLength)
        {
            super(experiment, id, new SimTimeFloat(startTime), warmupPeriod, runLength);
        }

        /** {@inheritDoc} */
        @SuppressWarnings("unchecked")
        @Override
        public Experiment.TimeFloat<? extends SimulatorInterface.TimeFloat> getExperiment()
        {
            return (Experiment.TimeFloat<? extends SimulatorInterface.TimeFloat>) super.getExperiment();
        }
    }

    /**
     * Easy access class Treatment.TimeLong.
     */
    public static class TimeLong extends Treatment<Long, Long, SimTimeLong>
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * constructs a Treatment.TimeLong.
         * @param experiment Experiment.TimeLong&lt;? extends SimulatorInterface.TimeLong&gt;; reflects the experiment
         * @param id String; an id to recognize the treatment
         * @param startTime Long; the absolute start time of a run (can be zero)
         * @param warmupPeriod Long; the relative warmup time of a run (can be zero), &lt;i&gt;included&lt;/i&gt; in the
         *            runLength
         * @param runLength Long; the run length of a run (relative to the start time)
         * @param replicationMode ReplicationMode; the replication mode of this treatment
         */
        public TimeLong(final Experiment.TimeLong<? extends SimulatorInterface.TimeLong> experiment, final String id,
                final Long startTime, final Long warmupPeriod, final Long runLength, final ReplicationMode replicationMode)
        {
            super(experiment, id, new SimTimeLong(startTime), warmupPeriod, runLength, replicationMode);
        }

        /**
         * constructs a Treatment.TimeLong.
         * @param experiment Experiment.TimeLong&lt;? extends SimulatorInterface.TimeLong&gt;; reflects the experiment
         * @param id String; an id to recognize the treatment
         * @param startTime Long; the absolute start time of a run (can be zero)
         * @param warmupPeriod Long; the relative warmup time of a run (can be zero), &lt;i&gt;included&lt;/i&gt; in the
         *            runLength
         * @param runLength Long; the run length of a run (relative to the start time)
         */
        public TimeLong(final Experiment.TimeLong<? extends SimulatorInterface.TimeLong> experiment, final String id,
                final Long startTime, final Long warmupPeriod, final Long runLength)
        {
            super(experiment, id, new SimTimeLong(startTime), warmupPeriod, runLength);
        }

        /** {@inheritDoc} */
        @SuppressWarnings("unchecked")
        @Override
        public Experiment.TimeLong<? extends SimulatorInterface.TimeLong> getExperiment()
        {
            return (Experiment.TimeLong<? extends SimulatorInterface.TimeLong>) super.getExperiment();
        }
    }

    /**
     * Easy access class Treatment.TimeDoubleUnit.
     */
    public static class TimeDoubleUnit extends Treatment<Time, Duration, SimTimeDoubleUnit>
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * constructs a Treatment.TimeDoubleUnit.
         * @param experiment Experiment.TimeDoubleUnit&lt;? extends SimulatorInterface.TimeDoubleUnit&gt;; reflects the
         *            experiment
         * @param id String; an id to recognize the treatment
         * @param startTime Time; the absolute start time of a run (can be zero)
         * @param warmupPeriod Duration; the relative warmup time of a run (can be zero), &lt;i&gt;included&lt;/i&gt; in the
         *            runLength
         * @param runLength Duration; the run length of a run (relative to the start time)
         * @param replicationMode ReplicationMode; the replication mode of this treatment
         */
        public TimeDoubleUnit(final Experiment.TimeDoubleUnit<? extends SimulatorInterface.TimeDoubleUnit> experiment,
                final String id, final Time startTime, final Duration warmupPeriod, final Duration runLength,
                final ReplicationMode replicationMode)
        {
            super(experiment, id, new SimTimeDoubleUnit(startTime), warmupPeriod, runLength, replicationMode);
        }

        /**
         * constructs a Treatment.TimeDoubleUnit.
         * @param experiment Experiment.TimeDoubleUnit&lt;? extends SimulatorInterface.TimeDoubleUnit&gt;; reflects the
         *            experiment
         * @param id String; an id to recognize the treatment
         * @param startTime Time; the absolute start time of a run (can be zero)
         * @param warmupPeriod Duration; the relative warmup time of a run (can be zero), &lt;i&gt;included&lt;/i&gt; in the
         *            runLength
         * @param runLength Duration; the run length of a run (relative to the start time)
         */
        public TimeDoubleUnit(final Experiment.TimeDoubleUnit<? extends SimulatorInterface.TimeDoubleUnit> experiment,
                final String id, final Time startTime, final Duration warmupPeriod, final Duration runLength)
        {
            super(experiment, id, new SimTimeDoubleUnit(startTime), warmupPeriod, runLength);
        }

        /** {@inheritDoc} */
        @SuppressWarnings("unchecked")
        @Override
        public Experiment.TimeDoubleUnit<? extends SimulatorInterface.TimeDoubleUnit> getExperiment()
        {
            return (Experiment.TimeDoubleUnit<? extends SimulatorInterface.TimeDoubleUnit>) super.getExperiment();
        }
    }

    /**
     * Easy access class Treatment.TimeFloatUnit.
     */
    public static class TimeFloatUnit extends Treatment<FloatTime, FloatDuration, SimTimeFloatUnit>
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * constructs a Treatment.TimeFloatUnit.
         * @param experiment Experiment.TimeFloatUnit&lt;? extends SimulatorInterface.TimeFloatUnit&gt;; reflects the experiment
         * @param id String; an id to recognize the treatment
         * @param startTime FloatTime; the absolute start time of a run (can be zero)
         * @param warmupPeriod FloatDuration; the relative warmup time of a run (can be zero), &lt;i&gt;included&lt;/i&gt; in
         *            the runLength
         * @param runLength FloatDuration; the run length of a run (relative to the start time)
         * @param replicationMode ReplicationMode; the replication mode of this treatment
         */
        public TimeFloatUnit(final Experiment.TimeFloatUnit<? extends SimulatorInterface.TimeFloatUnit> experiment,
                final String id, final FloatTime startTime, final FloatDuration warmupPeriod, final FloatDuration runLength,
                final ReplicationMode replicationMode)
        {
            super(experiment, id, new SimTimeFloatUnit(startTime), warmupPeriod, runLength, replicationMode);
        }

        /**
         * constructs a Treatment.TimeFloatUnit.
         * @param experiment Experiment.TimeFloatUnit&lt;? extends SimulatorInterface.TimeFloatUnit&gt;; reflects the experiment
         * @param id String; an id to recognize the treatment
         * @param startTime FloatTime; the absolute start time of a run (can be zero)
         * @param warmupPeriod FloatDuration; the relative warmup time of a run (can be zero), &lt;i&gt;included&lt;/i&gt; in
         *            the runLength
         * @param runLength FloatDuration; the run length of a run (relative to the start time)
         */
        public TimeFloatUnit(final Experiment.TimeFloatUnit<? extends SimulatorInterface.TimeFloatUnit> experiment,
                final String id, final FloatTime startTime, final FloatDuration warmupPeriod, final FloatDuration runLength)
        {
            super(experiment, id, new SimTimeFloatUnit(startTime), warmupPeriod, runLength);
        }

        /** {@inheritDoc} */
        @SuppressWarnings("unchecked")
        @Override
        public Experiment.TimeFloatUnit<? extends SimulatorInterface.TimeFloatUnit> getExperiment()
        {
            return (Experiment.TimeFloatUnit<? extends SimulatorInterface.TimeFloatUnit>) super.getExperiment();
        }
    }

}
