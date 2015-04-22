package nl.tudelft.simulation.dsol.experiment;

import java.io.Serializable;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import nl.tudelft.simulation.dsol.ModelInterface;
import nl.tudelft.simulation.dsol.simtime.SimTime;
import nl.tudelft.simulation.dsol.simtime.SimTimeCalendarDouble;
import nl.tudelft.simulation.dsol.simtime.SimTimeCalendarFloat;
import nl.tudelft.simulation.dsol.simtime.SimTimeCalendarLong;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simtime.SimTimeDoubleUnit;
import nl.tudelft.simulation.dsol.simtime.SimTimeFloat;
import nl.tudelft.simulation.dsol.simtime.SimTimeFloatUnit;
import nl.tudelft.simulation.dsol.simtime.SimTimeLong;
import nl.tudelft.simulation.dsol.simtime.SimTimeLongUnit;
import nl.tudelft.simulation.dsol.simtime.UnitTimeDouble;
import nl.tudelft.simulation.dsol.simtime.UnitTimeFloat;
import nl.tudelft.simulation.dsol.simtime.UnitTimeLong;
import nl.tudelft.simulation.jstats.streams.StreamInterface;

/**
 * The replication of a runcontrol.
 * <p>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:36:44 $
 * @param <A> the absolute storage type for the simulation time, e.g. Calendar, UnitTimeDouble, or Double.
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
     * @param context the name under which this replication can be found in the nameSpace
     * @param experiment the experiment to which this replication belongs
     */
    public Replication(final Context context, final Experiment<A, R, T> experiment)
    {
        super();
        this.experiment = experiment;
        this.context = context;
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
            final ModelInterface<A, R, T> model) throws NamingException
    {
        super();
        this.context = new InitialContext();
        this.experiment = new Experiment<A, R, T>(this.context);
        this.experiment.setModel(model);
        Treatment<A, R, T> treatment =
                new Treatment<A, R, T>(this.experiment, "Treatment for " + id, startTime, warmupPeriod, runLength);
        this.experiment.setTreatment(treatment);
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
         * @param context the name under which this replication can be found in the nameSpace
         * @param experiment the experiment to which this replication belongs
         */
        public TimeDouble(final Context context, final Experiment.TimeDouble experiment)
        {
            super(context, experiment);
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
        public TimeDouble(final String id, final SimTimeDouble startTime, final Double warmupPeriod,
                final Double runLength, final ModelInterface.TimeDouble model) throws NamingException
        {
            super(id, startTime, warmupPeriod, runLength, model);
        }
    }

    /** Easy access class Treatment.TimeFloat. */
    public static class TimeFloat extends Replication<Float, Float, SimTimeFloat>
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * constructs a new Replication.TimeFloat.
         * @param context the name under which this replication can be found in the nameSpace
         * @param experiment the experiment to which this replication belongs
         */
        public TimeFloat(final Context context, final Experiment.TimeFloat experiment)
        {
            super(context, experiment);
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
        public TimeFloat(final String id, final SimTimeFloat startTime, final Float warmupPeriod,
                final Float runLength, final ModelInterface.TimeFloat model) throws NamingException
        {
            super(id, startTime, warmupPeriod, runLength, model);
        }
    }

    /** Easy access class Treatment.TimeLong. */
    public static class TimeLong extends Replication<Long, Long, SimTimeLong>
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * constructs a new Replication.TimeLong.
         * @param context the name under which this replication can be found in the nameSpace
         * @param experiment the experiment to which this replication belongs
         */
        public TimeLong(final Context context, final Experiment.TimeLong experiment)
        {
            super(context, experiment);
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
        public TimeLong(final String id, final SimTimeLong startTime, final Long warmupPeriod, final Long runLength,
                final ModelInterface.TimeLong model) throws NamingException
        {
            super(id, startTime, warmupPeriod, runLength, model);
        }
    }

    /** Easy access class Treatment.TimeDoubleUnit. */
    public static class TimeDoubleUnit extends Replication<UnitTimeDouble, UnitTimeDouble, SimTimeDoubleUnit>
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * constructs a new Replication.TimeDoubleUnit.
         * @param context the name under which this replication can be found in the nameSpace
         * @param experiment the experiment to which this replication belongs
         */
        public TimeDoubleUnit(final Context context, final Experiment.TimeDoubleUnit experiment)
        {
            super(context, experiment);
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
        public TimeDoubleUnit(final String id, final SimTimeDoubleUnit startTime, final UnitTimeDouble warmupPeriod,
                final UnitTimeDouble runLength, final ModelInterface.TimeDoubleUnit model) throws NamingException
        {
            super(id, startTime, warmupPeriod, runLength, model);
        }
    }

    /** Easy access class Treatment.TimeDoubleUnit. */
    public static class TimeFloatUnit extends Replication<UnitTimeFloat, UnitTimeFloat, SimTimeFloatUnit>
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * constructs a new Replication.TimeFloatUnit.
         * @param context the name under which this replication can be found in the nameSpace
         * @param experiment the experiment to which this replication belongs
         */
        public TimeFloatUnit(final Context context, final Experiment.TimeFloatUnit experiment)
        {
            super(context, experiment);
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
        public TimeFloatUnit(final String id, final SimTimeFloatUnit startTime, final UnitTimeFloat warmupPeriod,
                final UnitTimeFloat runLength, final ModelInterface.TimeFloatUnit model) throws NamingException
        {
            super(id, startTime, warmupPeriod, runLength, model);
        }
    }

    /** Easy access class Treatment.TimeLongUnit. */
    public static class TimeLongUnit extends Replication<UnitTimeLong, UnitTimeLong, SimTimeLongUnit>
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * constructs a new Replication.TimeLongUnit.
         * @param context the name under which this replication can be found in the nameSpace
         * @param experiment the experiment to which this replication belongs
         */
        public TimeLongUnit(final Context context, final Experiment.TimeLongUnit experiment)
        {
            super(context, experiment);
        }

        /**
         * constructs a stand-alone Replication.TimeLongUnit and make a treatment and experiment as well.
         * @param id the id of the replication.
         * @param startTime the start time as a time object.
         * @param warmupPeriod the warmup period, included in the runlength (!)
         * @param runLength the total length of the run, including the warm-up period.
         * @param model the model for which this is the replication
         * @throws NamingException in case a context for the replication cannot be created
         */
        public TimeLongUnit(final String id, final SimTimeLongUnit startTime, final UnitTimeLong warmupPeriod,
                final UnitTimeLong runLength, final ModelInterface.TimeLongUnit model) throws NamingException
        {
            super(id, startTime, warmupPeriod, runLength, model);
        }
    }

    /** Easy access class Treatment.CalendarDouble. */
    public static class CalendarDouble extends Replication<Calendar, UnitTimeDouble, SimTimeCalendarDouble>
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * constructs a new Replication.CalendarDouble.
         * @param context the name under which this replication can be found in the nameSpace
         * @param experiment the experiment to which this replication belongs
         */
        public CalendarDouble(final Context context, final Experiment.CalendarDouble experiment)
        {
            super(context, experiment);
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
        public CalendarDouble(final String id, final SimTimeCalendarDouble startTime,
                final UnitTimeDouble warmupPeriod, final UnitTimeDouble runLength,
                final ModelInterface.CalendarDouble model) throws NamingException
        {
            super(id, startTime, warmupPeriod, runLength, model);
        }
    }

    /** Easy access class Treatment.CalendarFloat. */
    public static class CalendarFloat extends Replication<Calendar, UnitTimeFloat, SimTimeCalendarFloat>
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * constructs a new Replication.CalendarFloat.
         * @param context the name under which this replication can be found in the nameSpace
         * @param experiment the experiment to which this replication belongs
         */
        public CalendarFloat(final Context context, final Experiment.CalendarFloat experiment)
        {
            super(context, experiment);
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
        public CalendarFloat(final String id, final SimTimeCalendarFloat startTime,
                final UnitTimeFloat warmupPeriod, final UnitTimeFloat runLength,
                final ModelInterface.CalendarFloat model) throws NamingException
        {
            super(id, startTime, warmupPeriod, runLength, model);
        }
    }

    /** Easy access class Treatment.CalendarLong. */
    public static class CalendarLong extends Replication<Calendar, UnitTimeLong, SimTimeCalendarLong>
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * constructs a new Replication.CalendarLong.
         * @param context the name under which this replication can be found in the nameSpace
         * @param experiment the experiment to which this replication belongs
         */
        public CalendarLong(final Context context, final Experiment.CalendarLong experiment)
        {
            super(context, experiment);
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
        public CalendarLong(final String id, final SimTimeCalendarLong startTime,
                final UnitTimeLong warmupPeriod, final UnitTimeLong runLength,
                final ModelInterface.CalendarLong model) throws NamingException
        {
            super(id, startTime, warmupPeriod, runLength, model);
        }
    }
}
