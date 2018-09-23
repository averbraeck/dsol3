package nl.tudelft.simulation.dsol.formalisms.flow;

import java.util.Calendar;

import org.djunits.value.vdouble.scalar.Duration;
import org.djunits.value.vdouble.scalar.Time;
import org.djunits.value.vfloat.scalar.FloatDuration;
import org.djunits.value.vfloat.scalar.FloatTime;

import nl.tudelft.simulation.dsol.formalisms.Resource;
import nl.tudelft.simulation.dsol.logger.SimLogger;
import nl.tudelft.simulation.dsol.simtime.SimTime;
import nl.tudelft.simulation.dsol.simtime.SimTimeCalendarDouble;
import nl.tudelft.simulation.dsol.simtime.SimTimeCalendarFloat;
import nl.tudelft.simulation.dsol.simtime.SimTimeCalendarLong;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simtime.SimTimeDoubleUnit;
import nl.tudelft.simulation.dsol.simtime.SimTimeFloat;
import nl.tudelft.simulation.dsol.simtime.SimTimeFloatUnit;
import nl.tudelft.simulation.dsol.simtime.SimTimeLong;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;

/**
 * The release station releases a given quantity of a claimed resource. <br>
 * (c) 2002-2018 <a href="https://simulation.tudelft.nl">Delft University of Technology </a>, the Netherlands. <br>
 * See for project information <a href="https://simulation.tudelft.nl"> www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:36:44 $
 * @author Peter Jacobs, Alexander Verbraeck
 * @param <A> the absolute storage type for the simulation time, e.g. Calendar, Duration, or Double.
 * @param <R> the relative type for time storage, e.g. Long for the Calendar. For most non-calendar types, the absolute
 *            and relative types are the same.
 * @param <T> the extended type itself to be able to implement a comparator on the simulation time.
 */
public class Release<A extends Comparable<A>, R extends Number & Comparable<R>, T extends SimTime<A, R, T>>
        extends Station<A, R, T>
{
    /** */
    private static final long serialVersionUID = 20151028L;

    /** resource refers to the resource released. */
    private Resource<A, R, T> resource;

    /** amount defines the amount to be released. */
    private double amount = 1.0;

    /**
     * Constructor for Release.
     * @param simulator on which is scheduled
     * @param resource which is released
     */
    public Release(final DEVSSimulatorInterface<A, R, T> simulator, final Resource<A, R, T> resource)
    {
        this(simulator, resource, 1.0);
    }

    /**
     * Constructor for Release.
     * @param simulator on which is scheduled
     * @param resource which is released
     * @param amount of resource which is released
     */
    public Release(final DEVSSimulatorInterface<A, R, T> simulator, final Resource<A, R, T> resource,
            final double amount)
    {
        super(simulator);
        this.resource = resource;
        this.amount = amount;
    }

    /** {@inheritDoc} */
    @Override
    public final synchronized void receiveObject(final Object object)
    {
        super.receiveObject(object);
        try
        {
            this.resource.releaseCapacity(this.amount);
            this.releaseObject(object);
        }
        catch (Exception exception)
        {
            SimLogger.always().warn(exception, "receiveObject");
        }
    }

    /***********************************************************************************************************/
    /************************************* EASY ACCESS CLASS EXTENSIONS ****************************************/
    /***********************************************************************************************************/

    /** Easy access class Release.TimeDouble. */
    public static class TimeDouble extends Release<Double, Double, SimTimeDouble>
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * Constructor for Release.
         * @param simulator on which is scheduled
         * @param resource which is released
         */
        public TimeDouble(final DEVSSimulatorInterface.TimeDouble simulator,
                final Resource<Double, Double, SimTimeDouble> resource)
        {
            super(simulator, resource);
        }

        /**
         * Constructor for Release.
         * @param simulator on which is scheduled
         * @param resource which is released
         * @param amount of resource which is released
         */
        public TimeDouble(final DEVSSimulatorInterface.TimeDouble simulator,
                final Resource<Double, Double, SimTimeDouble> resource, final double amount)
        {
            super(simulator, resource, amount);
        }
    }

    /** Easy access class Release.TimeFloat. */
    public static class TimeFloat extends Release<Float, Float, SimTimeFloat>
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * Constructor for Release.
         * @param simulator on which is scheduled
         * @param resource which is released
         */
        public TimeFloat(final DEVSSimulatorInterface.TimeFloat simulator,
                final Resource<Float, Float, SimTimeFloat> resource)
        {
            super(simulator, resource);
        }

        /**
         * Constructor for Release.
         * @param simulator on which is scheduled
         * @param resource which is released
         * @param amount of resource which is released
         */
        public TimeFloat(final DEVSSimulatorInterface.TimeFloat simulator,
                final Resource<Float, Float, SimTimeFloat> resource, final double amount)
        {
            super(simulator, resource, amount);
        }
    }

    /** Easy access class Release.TimeLong. */
    public static class TimeLong extends Release<Long, Long, SimTimeLong>
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * Constructor for Release.
         * @param simulator on which is scheduled
         * @param resource which is released
         */
        public TimeLong(final DEVSSimulatorInterface.TimeLong simulator,
                final Resource<Long, Long, SimTimeLong> resource)
        {
            super(simulator, resource);
        }

        /**
         * Constructor for Release.
         * @param simulator on which is scheduled
         * @param resource which is released
         * @param amount of resource which is released
         */
        public TimeLong(final DEVSSimulatorInterface.TimeLong simulator,
                final Resource<Long, Long, SimTimeLong> resource, final double amount)
        {
            super(simulator, resource, amount);
        }
    }

    /** Easy access class Release.TimeDoubleUnit. */
    public static class TimeDoubleUnit extends Release<Time, Duration, SimTimeDoubleUnit>
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * Constructor for Release.
         * @param simulator on which is scheduled
         * @param resource which is released
         */
        public TimeDoubleUnit(final DEVSSimulatorInterface.TimeDoubleUnit simulator,
                final Resource<Time, Duration, SimTimeDoubleUnit> resource)
        {
            super(simulator, resource);
        }

        /**
         * Constructor for Release.
         * @param simulator on which is scheduled
         * @param resource which is released
         * @param amount of resource which is released
         */
        public TimeDoubleUnit(final DEVSSimulatorInterface.TimeDoubleUnit simulator,
                final Resource<Time, Duration, SimTimeDoubleUnit> resource, final double amount)
        {
            super(simulator, resource, amount);
        }
    }

    /** Easy access class Release.TimeFloatUnit. */
    public static class TimeFloatUnit extends Release<FloatTime, FloatDuration, SimTimeFloatUnit>
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * Constructor for Release.
         * @param simulator on which is scheduled
         * @param resource which is released
         */
        public TimeFloatUnit(final DEVSSimulatorInterface.TimeFloatUnit simulator,
                final Resource<FloatTime, FloatDuration, SimTimeFloatUnit> resource)
        {
            super(simulator, resource);
        }

        /**
         * Constructor for Release.
         * @param simulator on which is scheduled
         * @param resource which is released
         * @param amount of resource which is released
         */
        public TimeFloatUnit(final DEVSSimulatorInterface.TimeFloatUnit simulator,
                final Resource<FloatTime, FloatDuration, SimTimeFloatUnit> resource, final double amount)
        {
            super(simulator, resource, amount);
        }
    }

    /** Easy access class Release.CalendarDouble. */
    public static class CalendarDouble extends Release<Calendar, Duration, SimTimeCalendarDouble>
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * Constructor for Release.
         * @param simulator on which is scheduled
         * @param resource which is released
         */
        public CalendarDouble(final DEVSSimulatorInterface.CalendarDouble simulator,
                final Resource<Calendar, Duration, SimTimeCalendarDouble> resource)
        {
            super(simulator, resource);
        }

        /**
         * Constructor for Release.
         * @param simulator on which is scheduled
         * @param resource which is released
         * @param amount of resource which is released
         */
        public CalendarDouble(final DEVSSimulatorInterface.CalendarDouble simulator,
                final Resource<Calendar, Duration, SimTimeCalendarDouble> resource, final double amount)
        {
            super(simulator, resource, amount);
        }
    }

    /** Easy access class Release.CalendarFloat. */
    public static class CalendarFloat extends Release<Calendar, FloatDuration, SimTimeCalendarFloat>
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * Constructor for Release.
         * @param simulator on which is scheduled
         * @param resource which is released
         */
        public CalendarFloat(final DEVSSimulatorInterface.CalendarFloat simulator,
                final Resource<Calendar, FloatDuration, SimTimeCalendarFloat> resource)
        {
            super(simulator, resource);
        }

        /**
         * Constructor for Release.
         * @param simulator on which is scheduled
         * @param resource which is released
         * @param amount of resource which is released
         */
        public CalendarFloat(final DEVSSimulatorInterface.CalendarFloat simulator,
                final Resource<Calendar, FloatDuration, SimTimeCalendarFloat> resource, final double amount)
        {
            super(simulator, resource, amount);
        }
    }

    /** Easy access class Release.CalendarLong. */
    public static class CalendarLong extends Release<Calendar, Long, SimTimeCalendarLong>
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * Constructor for Release.
         * @param simulator on which is scheduled
         * @param resource which is released
         */
        public CalendarLong(final DEVSSimulatorInterface.CalendarLong simulator,
                final Resource<Calendar, Long, SimTimeCalendarLong> resource)
        {
            super(simulator, resource);
        }

        /**
         * Constructor for Release.
         * @param simulator on which is scheduled
         * @param resource which is released
         * @param amount of resource which is released
         */
        public CalendarLong(final DEVSSimulatorInterface.CalendarLong simulator,
                final Resource<Calendar, Long, SimTimeCalendarLong> resource, final double amount)
        {
            super(simulator, resource, amount);
        }
    }
}
