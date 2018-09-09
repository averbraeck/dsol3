package nl.tudelft.simulation.dsol.experiment;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Properties;

import org.djunits.value.vdouble.scalar.Duration;
import org.djunits.value.vdouble.scalar.Time;
import org.djunits.value.vfloat.scalar.FloatDuration;
import org.djunits.value.vfloat.scalar.FloatTime;

import nl.tudelft.simulation.dsol.simtime.SimTime;
import nl.tudelft.simulation.dsol.simtime.SimTimeCalendarDouble;
import nl.tudelft.simulation.dsol.simtime.SimTimeCalendarFloat;
import nl.tudelft.simulation.dsol.simtime.SimTimeCalendarLong;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simtime.SimTimeDoubleUnit;
import nl.tudelft.simulation.dsol.simtime.SimTimeFloat;
import nl.tudelft.simulation.dsol.simtime.SimTimeFloatUnit;
import nl.tudelft.simulation.dsol.simtime.SimTimeLong;

/**
 * The treatment is comprises the specification of input data, the runControl and the specification of output data.
 * (Sol:1982, Oeren &amp; Zeigler:1979) <br>
 * (c) 2002-2018 <a href="https://simulation.tudelft.nl">Delft University of Technology </a>, the Netherlands. <br>
 * See for project information <a href="https://simulation.tudelft.nl"> www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:36:44 $
 * @author Peter Jacobs, Alexander Verbraeck
 * @param <A> the absolute storage type for the simulation time, e.g. Calendar, Duration, or Double.
 * @param <R> the relative type for time storage, e.g. Long for the Calendar. For most non-calendar types, the absolute
 *            and relative types are the same.
 * @param <T> the extended type itself to be able to implement functions on the simulation time.
 */
public class Treatment<A extends Comparable<A>, R extends Number & Comparable<R>, T extends SimTime<A, R, T>>
        implements Serializable
{
    /** The default serial version UID for serializable classes. */
    private static final long serialVersionUID = 1L;

    /** the replication mode. */
    private final ReplicationMode replicationMode;

    /** the experiment to which this treatment belongs. */
    private final Experiment<A, R, T> experiment;

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

    /** the properties of this treatment. */
    private Properties properties = new Properties();

    /** the id. */
    private final String id;

    /**
     * constructs a Treatment.
     * @param experiment reflects the experiment
     * @param id an id to recognize the treatment
     * @param startTime the absolute start time of a run (can be zero)
     * @param warmupPeriod the relative warmup time of a run (can be zero), <i>included</i> in the runLength
     * @param runLength the run length of a run (relative to the start time)
     * @param replicationMode the replication mode of this treatment
     */
    public Treatment(final Experiment<A, R, T> experiment, final String id, final T startTime, final R warmupPeriod,
            final R runLength, final ReplicationMode replicationMode)
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
     * @param experiment reflects the experiment
     * @param id an id to recognize the treatment
     * @param startTime the absolute start time of a run (can be zero)
     * @param warmupPeriod the relative warmup time of a run (can be zero), <i>included</i> in the runLength
     * @param runLength the run length of a run (relative to the start time)
     */
    public Treatment(final Experiment<A, R, T> experiment, final String id, final T startTime, final R warmupPeriod,
            final R runLength)
    {
        this(experiment, id, startTime, warmupPeriod, runLength, ReplicationMode.TERMINATING);
    }

    /**
     * @return the experiment
     */
    public final Experiment<A, R, T> getExperiment()
    {
        return this.experiment;
    }

    /**
     * @return Properties for this treatment
     */
    public final Properties getProperties()
    {
        return this.properties;
    }

    /**
     * sets the properties.
     * @param properties the properties
     */
    public final void setProperties(final Properties properties)
    {
        this.properties = properties;
    }

    /**
     * @return the replication mode of this treatment.
     */
    public final ReplicationMode getReplicationMode()
    {
        return this.replicationMode;
    }

    /**
     * @return Returns the runLength.
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
    @SuppressWarnings("checkstyle:designforextension")
    public String toString()
    {
        String result = "[Treatment; warmup=" + this.warmupPeriod + " ; runLength=" + this.runLength + "]";
        return result;
    }

    /***********************************************************************************************************/
    /************************************* EASY ACCESS CLASS EXTENSIONS ****************************************/
    /***********************************************************************************************************/

    /** Easy access class Treatment.TimeDouble. */
    public static class TimeDouble extends Treatment<Double, Double, SimTimeDouble>
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * constructs a Treatment.TimeDouble.
         * @param experiment reflects the experiment
         * @param id an id to recognize the treatment
         * @param startTime the absolute start time of a run (can be zero)
         * @param warmupPeriod the relative warmup time of a run (can be zero), <i>included</i> in the runLength
         * @param runLength the run length of a run (relative to the start time)
         * @param replicationMode the replication mode of this treatment
         */
        public TimeDouble(final Experiment.TimeDouble experiment, final String id, final Double startTime,
                final Double warmupPeriod, final Double runLength, final ReplicationMode replicationMode)
        {
            super(experiment, id, new SimTimeDouble(startTime), warmupPeriod, runLength, replicationMode);
        }

        /**
         * constructs a Treatment.TimeDouble.
         * @param experiment reflects the experiment
         * @param id an id to recognize the treatment
         * @param startTime the absolute start time of a run (can be zero)
         * @param warmupPeriod the relative warmup time of a run (can be zero), <i>included</i> in the runLength
         * @param runLength the run length of a run (relative to the start time)
         */
        public TimeDouble(final Experiment.TimeDouble experiment, final String id, final Double startTime,
                final Double warmupPeriod, final Double runLength)
        {
            super(experiment, id, new SimTimeDouble(startTime), warmupPeriod, runLength);
        }
    }

    /** Easy access class Treatment.TimeFloat. */
    public static class TimeFloat extends Treatment<Float, Float, SimTimeFloat>
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * constructs a Treatment.TimeFloat.
         * @param experiment reflects the experiment
         * @param id an id to recognize the treatment
         * @param startTime the absolute start time of a run (can be zero)
         * @param warmupPeriod the relative warmup time of a run (can be zero), <i>included</i> in the runLength
         * @param runLength the run length of a run (relative to the start time)
         * @param replicationMode the replication mode of this treatment
         */
        public TimeFloat(final Experiment.TimeFloat experiment, final String id, final Float startTime,
                final Float warmupPeriod, final Float runLength, final ReplicationMode replicationMode)
        {
            super(experiment, id, new SimTimeFloat(startTime), warmupPeriod, runLength, replicationMode);
        }

        /**
         * constructs a Treatment.TimeFloat.
         * @param experiment reflects the experiment
         * @param id an id to recognize the treatment
         * @param startTime the absolute start time of a run (can be zero)
         * @param warmupPeriod the relative warmup time of a run (can be zero), <i>included</i> in the runLength
         * @param runLength the run length of a run (relative to the start time)
         */
        public TimeFloat(final Experiment.TimeFloat experiment, final String id, final Float startTime,
                final Float warmupPeriod, final Float runLength)
        {
            super(experiment, id, new SimTimeFloat(startTime), warmupPeriod, runLength);
        }
    }

    /** Easy access class Treatment.TimeLong. */
    public static class TimeLong extends Treatment<Long, Long, SimTimeLong>
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * constructs a Treatment.TimeLong.
         * @param experiment reflects the experiment
         * @param id an id to recognize the treatment
         * @param startTime the absolute start time of a run (can be zero)
         * @param warmupPeriod the relative warmup time of a run (can be zero), <i>included</i> in the runLength
         * @param runLength the run length of a run (relative to the start time)
         * @param replicationMode the replication mode of this treatment
         */
        public TimeLong(final Experiment.TimeLong experiment, final String id, final Long startTime,
                final Long warmupPeriod, final Long runLength, final ReplicationMode replicationMode)
        {
            super(experiment, id, new SimTimeLong(startTime), warmupPeriod, runLength, replicationMode);
        }

        /**
         * constructs a Treatment.TimeLong.
         * @param experiment reflects the experiment
         * @param id an id to recognize the treatment
         * @param startTime the absolute start time of a run (can be zero)
         * @param warmupPeriod the relative warmup time of a run (can be zero), <i>included</i> in the runLength
         * @param runLength the run length of a run (relative to the start time)
         */
        public TimeLong(final Experiment.TimeLong experiment, final String id, final Long startTime,
                final Long warmupPeriod, final Long runLength)
        {
            super(experiment, id, new SimTimeLong(startTime), warmupPeriod, runLength);
        }
    }

    /** Easy access class Treatment.TimeDoubleUnit. */
    public static class TimeDoubleUnit extends Treatment<Time, Duration, SimTimeDoubleUnit>
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * constructs a Treatment.TimeDoubleUnit.
         * @param experiment reflects the experiment
         * @param id an id to recognize the treatment
         * @param startTime the absolute start time of a run (can be zero)
         * @param warmupPeriod the relative warmup time of a run (can be zero), <i>included</i> in the runLength
         * @param runLength the run length of a run (relative to the start time)
         * @param replicationMode the replication mode of this treatment
         */
        public TimeDoubleUnit(final Experiment.TimeDoubleUnit experiment, final String id, final Time startTime,
                final Duration warmupPeriod, final Duration runLength, final ReplicationMode replicationMode)
        {
            super(experiment, id, new SimTimeDoubleUnit(startTime), warmupPeriod, runLength, replicationMode);
        }

        /**
         * constructs a Treatment.TimeDoubleUnit.
         * @param experiment reflects the experiment
         * @param id an id to recognize the treatment
         * @param startTime the absolute start time of a run (can be zero)
         * @param warmupPeriod the relative warmup time of a run (can be zero), <i>included</i> in the runLength
         * @param runLength the run length of a run (relative to the start time)
         */
        public TimeDoubleUnit(final Experiment.TimeDoubleUnit experiment, final String id, final Time startTime,
                final Duration warmupPeriod, final Duration runLength)
        {
            super(experiment, id, new SimTimeDoubleUnit(startTime), warmupPeriod, runLength);
        }
    }

    /** Easy access class Treatment.TimeFloatUnit. */
    public static class TimeFloatUnit extends Treatment<FloatTime, FloatDuration, SimTimeFloatUnit>
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * constructs a Treatment.TimeFloatUnit.
         * @param experiment reflects the experiment
         * @param id an id to recognize the treatment
         * @param startTime the absolute start time of a run (can be zero)
         * @param warmupPeriod the relative warmup time of a run (can be zero), <i>included</i> in the runLength
         * @param runLength the run length of a run (relative to the start time)
         * @param replicationMode the replication mode of this treatment
         */
        public TimeFloatUnit(final Experiment.TimeFloatUnit experiment, final String id, final FloatTime startTime,
                final FloatDuration warmupPeriod, final FloatDuration runLength, final ReplicationMode replicationMode)
        {
            super(experiment, id, new SimTimeFloatUnit(startTime), warmupPeriod, runLength, replicationMode);
        }

        /**
         * constructs a Treatment.TimeFloatUnit.
         * @param experiment reflects the experiment
         * @param id an id to recognize the treatment
         * @param startTime the absolute start time of a run (can be zero)
         * @param warmupPeriod the relative warmup time of a run (can be zero), <i>included</i> in the runLength
         * @param runLength the run length of a run (relative to the start time)
         */
        public TimeFloatUnit(final Experiment.TimeFloatUnit experiment, final String id, final FloatTime startTime,
                final FloatDuration warmupPeriod, final FloatDuration runLength)
        {
            super(experiment, id, new SimTimeFloatUnit(startTime), warmupPeriod, runLength);
        }
    }

    /** Easy access class Treatment.CalendarDouble. */
    public static class CalendarDouble extends Treatment<Calendar, Duration, SimTimeCalendarDouble>
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * constructs a Treatment.CalendarDouble.
         * @param experiment reflects the experiment
         * @param id an id to recognize the treatment
         * @param startTime the absolute start time of a run (can be zero)
         * @param warmupPeriod the relative warmup time of a run (can be zero), <i>included</i> in the runLength
         * @param runLength the run length of a run (relative to the start time)
         * @param replicationMode the replication mode of this treatment
         */
        public CalendarDouble(final Experiment.CalendarDouble experiment, final String id, final Calendar startTime,
                final Duration warmupPeriod, final Duration runLength, final ReplicationMode replicationMode)
        {
            super(experiment, id, new SimTimeCalendarDouble(startTime), warmupPeriod, runLength, replicationMode);
        }

        /**
         * constructs a Treatment.CalendarDouble.
         * @param experiment reflects the experiment
         * @param id an id to recognize the treatment
         * @param startTime the absolute start time of a run (can be zero)
         * @param warmupPeriod the relative warmup time of a run (can be zero), <i>included</i> in the runLength
         * @param runLength the run length of a run (relative to the start time)
         */
        public CalendarDouble(final Experiment.CalendarDouble experiment, final String id, final Calendar startTime,
                final Duration warmupPeriod, final Duration runLength)
        {
            super(experiment, id, new SimTimeCalendarDouble(startTime), warmupPeriod, runLength);
        }
    }

    /** Easy access class Treatment.CalendarFloat. */
    public static class CalendarFloat extends Treatment<Calendar, FloatDuration, SimTimeCalendarFloat>
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * constructs a Treatment.CalendarFloat.
         * @param experiment reflects the experiment
         * @param id an id to recognize the treatment
         * @param startTime the absolute start time of a run (can be zero)
         * @param warmupPeriod the relative warmup time of a run (can be zero), <i>included</i> in the runLength
         * @param runLength the run length of a run (relative to the start time)
         * @param replicationMode the replication mode of this treatment
         */
        public CalendarFloat(final Experiment.CalendarFloat experiment, final String id, final Calendar startTime,
                final FloatDuration warmupPeriod, final FloatDuration runLength, final ReplicationMode replicationMode)
        {
            super(experiment, id, new SimTimeCalendarFloat(startTime), warmupPeriod, runLength, replicationMode);
        }

        /**
         * constructs a Treatment.CalendarFloat.
         * @param experiment reflects the experiment
         * @param id an id to recognize the treatment
         * @param startTime the absolute start time of a run (can be zero)
         * @param warmupPeriod the relative warmup time of a run (can be zero), <i>included</i> in the runLength
         * @param runLength the run length of a run (relative to the start time)
         */
        public CalendarFloat(final Experiment.CalendarFloat experiment, final String id, final Calendar startTime,
                final FloatDuration warmupPeriod, final FloatDuration runLength)
        {
            super(experiment, id, new SimTimeCalendarFloat(startTime), warmupPeriod, runLength);
        }
    }

    /** Easy access class Treatment.CalendarLong. */
    public static class CalendarLong extends Treatment<Calendar, Long, SimTimeCalendarLong>
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * constructs a Treatment.CalendarLong.
         * @param experiment reflects the experiment
         * @param id an id to recognize the treatment
         * @param startTime the absolute start time of a run (can be zero)
         * @param warmupPeriod the relative warmup time of a run (can be zero), <i>included</i> in the runLength
         * @param runLength the run length of a run (relative to the start time)
         * @param replicationMode the replication mode of this treatment
         */
        public CalendarLong(final Experiment.CalendarLong experiment, final String id, final Calendar startTime,
                final Long warmupPeriod, final Long runLength, final ReplicationMode replicationMode)
        {
            super(experiment, id, new SimTimeCalendarLong(startTime), warmupPeriod, runLength, replicationMode);
        }

        /**
         * constructs a Treatment.CalendarLong.
         * @param experiment reflects the experiment
         * @param id an id to recognize the treatment
         * @param startTime the absolute start time of a run (can be zero)
         * @param warmupPeriod the relative warmup time of a run (can be zero), <i>included</i> in the runLength
         * @param runLength the run length of a run (relative to the start time)
         */
        public CalendarLong(final Experiment.CalendarLong experiment, final String id, final Calendar startTime,
                final Long warmupPeriod, final Long runLength)
        {
            super(experiment, id, new SimTimeCalendarLong(startTime), warmupPeriod, runLength);
        }
    }
}
