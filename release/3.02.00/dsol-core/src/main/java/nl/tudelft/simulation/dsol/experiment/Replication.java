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

import nl.tudelft.simulation.dsol.DSOLModel;
import nl.tudelft.simulation.dsol.simtime.SimTime;
import nl.tudelft.simulation.dsol.simtime.SimTimeCalendarDouble;
import nl.tudelft.simulation.dsol.simtime.SimTimeCalendarFloat;
import nl.tudelft.simulation.dsol.simtime.SimTimeCalendarLong;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simtime.SimTimeDoubleUnit;
import nl.tudelft.simulation.dsol.simtime.SimTimeFloat;
import nl.tudelft.simulation.dsol.simtime.SimTimeFloatUnit;
import nl.tudelft.simulation.dsol.simtime.SimTimeLong;
import nl.tudelft.simulation.jstats.streams.StreamInterface;
import nl.tudelft.simulation.naming.context.ContextUtil;

/**
 * The replication of a runcontrol.
 * <p>
 * (c) 2002-2018 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:36:44 $
 * @param <A> the absolute storage type for the simulation time, e.g. Calendar, Duration, or Double.
 * @param <R> the relative type for time storage, e.g. Long for the Calendar. For most non-calendar types, the absolute
 *            and relative types are the same.
 * @param <T> the extended type itself to be able to implement a comparator on the simulation time.
 * @since 1.5
 */
public class Replication<A extends Comparable<A>, R extends Number & Comparable<R>, T extends SimTime<A, R, T>>
        implements Serializable
{
    /** The default serial version UID for serializable classes. */
    private static final long serialVersionUID = 1L;

    /** streams used in the replication. */
    private Map<String, StreamInterface> streams = new HashMap<String, StreamInterface>();

    /** description the description of the replication. */
    private String description = "rep_no_description";

    /** the experiment to which this replication belongs. */
    private Experiment<A, R, T> experiment = null;

    /** the contextRoot of this replication. */
    private Context context = null;

    /**
     * constructs a new Replication.
     * @param experiment the experiment to which this replication belongs
     * @throws NamingException in case a context for the replication cannot be created
     */
    public Replication(final Experiment<A, R, T> experiment) throws NamingException
    {
        super();
        this.experiment = experiment;
        setContext(String.valueOf(hashCode()));
    }

    /**
     * constructs a stand-alone Replication and make a treatment and experiment as well.
     * @param id the id of the replication.
     * @param startTime the start time as a time object.
     * @param warmupPeriod the warmup period, included in the runlength (!)
     * @param runLength the total length of the run, including the warm-up period.
     * @param model the model for which this is the replication
     * @throws NamingException in case a context for the replication cannot be created
     */
    public Replication(final String id, final T startTime, final R warmupPeriod, final R runLength,
            final DSOLModel<A, R, T> model) throws NamingException
    {
        super();
        this.experiment = new Experiment<A, R, T>();
        this.experiment.setModel(model);
        Treatment<A, R, T> treatment =
                new Treatment<A, R, T>(this.experiment, "Treatment for " + id, startTime, warmupPeriod, runLength);
        this.experiment.setTreatment(treatment);
        setContext(id);
    }

    /**
     * set the context for this replication.
     * @param id the id of the replication.
     * @throws NamingException in case a context for the experiment or replication cannot be created
     */
    private void setContext(final String id) throws NamingException
    {
        this.context = ContextUtil.lookup(this.experiment.getContext(), String.valueOf(id));
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
     * @param name the name of the stream
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
     * @param description the description of this replication
     */
    public final void setDescription(final String description)
    {
        this.description = description;
    }

    /**
     * sets the stream for this replication.
     * @param streams the map of stream,name tuples
     */
    public final void setStreams(final Map<String, StreamInterface> streams)
    {
        this.streams = streams;
    }

    /**
     * @return Returns the experiment.
     */
    public final Experiment<A, R, T> getExperiment()
    {
        return this.experiment;
    }

    /**
     * @return Returns the treatment. This is a convenience method to avoid the getExperiment().getTreatment() many
     *         times.
     */
    public final Treatment<A, R, T> getTreatment()
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

    /** Easy access class Treatment.TimeDouble. */
    public static class TimeDouble extends Replication<Double, Double, SimTimeDouble>
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * constructs a new Replication.TimeDouble.
         * @param experiment the experiment to which this replication belongs
         * @throws NamingException in case a context for the replication cannot be created
         */
        public TimeDouble(final Experiment.TimeDouble experiment) throws NamingException
        {
            super(experiment);
        }

        /**
         * constructs a stand-alone Replication.TimeDouble and make a treatment and experiment as well.
         * @param id the id of the replication.
         * @param startTime the start time as a time object.
         * @param warmupPeriod the warmup period, included in the runlength (!)
         * @param runLength the total length of the run, including the warm-up period.
         * @param model the model for which this is the replication
         * @throws NamingException in case a context for the replication cannot be created
         */
        public TimeDouble(final String id, final Double startTime, final Double warmupPeriod, final Double runLength,
                final DSOLModel.TimeDouble model) throws NamingException
        {
            super(id, new SimTimeDouble(startTime), warmupPeriod, runLength, model);
        }
    }

    /** Easy access class Treatment.TimeFloat. */
    public static class TimeFloat extends Replication<Float, Float, SimTimeFloat>
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * constructs a new Replication.TimeFloat.
         * @param experiment the experiment to which this replication belongs
         * @throws NamingException in case a context for the replication cannot be created
         */
        public TimeFloat(final Experiment.TimeFloat experiment) throws NamingException
        {
            super(experiment);
        }

        /**
         * constructs a stand-alone Replication.TimeFloat and make a treatment and experiment as well.
         * @param id the id of the replication.
         * @param startTime the start time as a time object.
         * @param warmupPeriod the warmup period, included in the runlength (!)
         * @param runLength the total length of the run, including the warm-up period.
         * @param model the model for which this is the replication
         * @throws NamingException in case a context for the replication cannot be created
         */
        public TimeFloat(final String id, final Float startTime, final Float warmupPeriod, final Float runLength,
                final DSOLModel.TimeFloat model) throws NamingException
        {
            super(id, new SimTimeFloat(startTime), warmupPeriod, runLength, model);
        }
    }

    /** Easy access class Treatment.TimeLong. */
    public static class TimeLong extends Replication<Long, Long, SimTimeLong>
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * constructs a new Replication.TimeLong.
         * @param experiment the experiment to which this replication belongs
         * @throws NamingException in case a context for the replication cannot be created
         */
        public TimeLong(final Experiment.TimeLong experiment) throws NamingException
        {
            super(experiment);
        }

        /**
         * constructs a stand-alone Replication.TimeLong and make a treatment and experiment as well.
         * @param id the id of the replication.
         * @param startTime the start time as a time object.
         * @param warmupPeriod the warmup period, included in the runlength (!)
         * @param runLength the total length of the run, including the warm-up period.
         * @param model the model for which this is the replication
         * @throws NamingException in case a context for the replication cannot be created
         */
        public TimeLong(final String id, final Long startTime, final Long warmupPeriod, final Long runLength,
                final DSOLModel.TimeLong model) throws NamingException
        {
            super(id, new SimTimeLong(startTime), warmupPeriod, runLength, model);
        }
    }

    /** Easy access class Treatment.TimeDoubleUnit. */
    public static class TimeDoubleUnit extends Replication<Time, Duration, SimTimeDoubleUnit>
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * constructs a new Replication.TimeDoubleUnit.
         * @param experiment the experiment to which this replication belongs
         * @throws NamingException in case a context for the replication cannot be created
         */
        public TimeDoubleUnit(final Experiment.TimeDoubleUnit experiment) throws NamingException
        {
            super(experiment);
        }

        /**
         * constructs a stand-alone Replication.TimeDoubleUnit and make a treatment and experiment as well.
         * @param id the id of the replication.
         * @param startTime the start time as a time object.
         * @param warmupPeriod the warmup period, included in the runlength (!)
         * @param runLength the total length of the run, including the warm-up period.
         * @param model the model for which this is the replication
         * @throws NamingException in case a context for the replication cannot be created
         */
        public TimeDoubleUnit(final String id, final Time startTime, final Duration warmupPeriod,
                final Duration runLength, final DSOLModel.TimeDoubleUnit model) throws NamingException
        {
            super(id, new SimTimeDoubleUnit(startTime), warmupPeriod, runLength, model);
        }
    }

    /** Easy access class Treatment.TimeDoubleUnit. */
    public static class TimeFloatUnit extends Replication<FloatTime, FloatDuration, SimTimeFloatUnit>
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * constructs a new Replication.TimeFloatUnit.
         * @param experiment the experiment to which this replication belongs
         * @throws NamingException in case a context for the replication cannot be created
         */
        public TimeFloatUnit(final Experiment.TimeFloatUnit experiment) throws NamingException
        {
            super(experiment);
        }

        /**
         * constructs a stand-alone Replication.TimeFloatUnit and make a treatment and experiment as well.
         * @param id the id of the replication.
         * @param startTime the start time as a time object.
         * @param warmupPeriod the warmup period, included in the runlength (!)
         * @param runLength the total length of the run, including the warm-up period.
         * @param model the model for which this is the replication
         * @throws NamingException in case a context for the replication cannot be created
         */
        public TimeFloatUnit(final String id, final FloatTime startTime, final FloatDuration warmupPeriod,
                final FloatDuration runLength, final DSOLModel.TimeFloatUnit model) throws NamingException
        {
            super(id, new SimTimeFloatUnit(startTime), warmupPeriod, runLength, model);
        }
    }

    /** Easy access class Treatment.CalendarDouble. */
    public static class CalendarDouble extends Replication<Calendar, Duration, SimTimeCalendarDouble>
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * constructs a new Replication.CalendarDouble.
         * @param experiment the experiment to which this replication belongs
         * @throws NamingException in case a context for the replication cannot be created
         */
        public CalendarDouble(final Experiment.CalendarDouble experiment) throws NamingException
        {
            super(experiment);
        }

        /**
         * constructs a stand-alone Replication.CalendarDouble and make a treatment and experiment as well.
         * @param id the id of the replication.
         * @param startTime the start time as a time object.
         * @param warmupPeriod the warmup period, included in the runlength (!)
         * @param runLength the total length of the run, including the warm-up period.
         * @param model the model for which this is the replication
         * @throws NamingException in case a context for the replication cannot be created
         */
        public CalendarDouble(final String id, final Calendar startTime, final Duration warmupPeriod,
                final Duration runLength, final DSOLModel.CalendarDouble model) throws NamingException
        {
            super(id, new SimTimeCalendarDouble(startTime), warmupPeriod, runLength, model);
        }
    }

    /** Easy access class Treatment.CalendarFloat. */
    public static class CalendarFloat extends Replication<Calendar, FloatDuration, SimTimeCalendarFloat>
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * constructs a new Replication.CalendarFloat.
         * @param experiment the experiment to which this replication belongs
         * @throws NamingException in case a context for the replication cannot be created
         */
        public CalendarFloat(final Experiment.CalendarFloat experiment) throws NamingException
        {
            super(experiment);
        }

        /**
         * constructs a stand-alone Replication.CalendarFloat and make a treatment and experiment as well.
         * @param id the id of the replication.
         * @param startTime the start time as a time object.
         * @param warmupPeriod the warmup period, included in the runlength (!)
         * @param runLength the total length of the run, including the warm-up period.
         * @param model the model for which this is the replication
         * @throws NamingException in case a context for the replication cannot be created
         */
        public CalendarFloat(final String id, final Calendar startTime, final FloatDuration warmupPeriod,
                final FloatDuration runLength, final DSOLModel.CalendarFloat model) throws NamingException
        {
            super(id, new SimTimeCalendarFloat(startTime), warmupPeriod, runLength, model);
        }
    }

    /** Easy access class Treatment.CalendarLong. */
    public static class CalendarLong extends Replication<Calendar, Long, SimTimeCalendarLong>
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * constructs a new Replication.CalendarLong.
         * @param experiment the experiment to which this replication belongs
         * @throws NamingException in case a context for the replication cannot be created
         */
        public CalendarLong(final Experiment.CalendarLong experiment) throws NamingException
        {
            super(experiment);
        }

        /**
         * constructs a stand-alone Replication.CalendarLong and make a treatment and experiment as well.
         * @param id the id of the replication.
         * @param startTime the start time as a time object.
         * @param warmupPeriod the warmup period, included in the runlength (!)
         * @param runLength the total length of the run, including the warm-up period.
         * @param model the model for which this is the replication
         * @throws NamingException in case a context for the replication cannot be created
         */
        public CalendarLong(final String id, final Calendar startTime, final Long warmupPeriod, final Long runLength,
                final DSOLModel.CalendarLong model) throws NamingException
        {
            super(id, new SimTimeCalendarLong(startTime), warmupPeriod, runLength, model);
        }
    }
}
