package nl.tudelft.simulation.dsol.formalisms.flow;

import java.rmi.RemoteException;
import java.util.Calendar;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import nl.tudelft.simulation.dsol.formalisms.Resource;
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
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;

/**
 * The release station releases a given quantity of a claimed resource. <br>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl"> www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:36:44 $
 * @author Peter Jacobs, Alexander Verbraeck
 * @param <A> the absolute storage type for the simulation time, e.g. Calendar, UnitTimeDouble, or Double.
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
    private Resource<T> resource;

    /** amount defines the amount to be released. */
    private double amount = 1.0;
    
    /** the logger. */
    private static Logger logger = LogManager.getLogger(Release.class);

    /**
     * Constructor for Release.
     * @param simulator on which is scheduled
     * @param resource which is released
     */
    public Release(final DEVSSimulatorInterface<A, R, T> simulator, final Resource<T> resource)
    {
        this(simulator, resource, 1.0);
    }

    /**
     * Constructor for Release.
     * @param simulator on which is scheduled
     * @param resource which is released
     * @param amount of resource which is released
     */
    public Release(final DEVSSimulatorInterface<A, R, T> simulator, final Resource<T> resource, final double amount)
    {
        super(simulator);
        this.resource = resource;
        this.amount = amount;
    }

    /** {@inheritDoc} */
    @Override
    public final synchronized void receiveObject(final Object object) throws RemoteException
    {
        super.receiveObject(object);
        try
        {
            this.resource.releaseCapacity(this.amount);
            this.releaseObject(object);
        }
        catch (Exception exception)
        {
            logger.warn("receiveObject", exception);
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
        public TimeDouble(final DEVSSimulatorInterface.TimeDouble simulator, final Resource<SimTimeDouble> resource)
        {
            super(simulator, resource);
        }

        /**
         * Constructor for Release.
         * @param simulator on which is scheduled
         * @param resource which is released
         * @param amount of resource which is released
         */
        public TimeDouble(final DEVSSimulatorInterface.TimeDouble simulator, final Resource<SimTimeDouble> resource,
                final double amount)
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
        public TimeFloat(final DEVSSimulatorInterface.TimeFloat simulator, final Resource<SimTimeFloat> resource)
        {
            super(simulator, resource);
        }

        /**
         * Constructor for Release.
         * @param simulator on which is scheduled
         * @param resource which is released
         * @param amount of resource which is released
         */
        public TimeFloat(final DEVSSimulatorInterface.TimeFloat simulator, final Resource<SimTimeFloat> resource,
                final double amount)
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
        public TimeLong(final DEVSSimulatorInterface.TimeLong simulator, final Resource<SimTimeLong> resource)
        {
            super(simulator, resource);
        }

        /**
         * Constructor for Release.
         * @param simulator on which is scheduled
         * @param resource which is released
         * @param amount of resource which is released
         */
        public TimeLong(final DEVSSimulatorInterface.TimeLong simulator, final Resource<SimTimeLong> resource,
                final double amount)
        {
            super(simulator, resource, amount);
        }
    }

    /** Easy access class Release.TimeDoubleUnit. */
    public static class TimeDoubleUnit extends Release<UnitTimeDouble, UnitTimeDouble, SimTimeDoubleUnit>
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * Constructor for Release.
         * @param simulator on which is scheduled
         * @param resource which is released
         */
        public TimeDoubleUnit(final DEVSSimulatorInterface.TimeDoubleUnit simulator,
                final Resource<SimTimeDoubleUnit> resource)
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
                final Resource<SimTimeDoubleUnit> resource, final double amount)
        {
            super(simulator, resource, amount);
        }
    }

    /** Easy access class Release.TimeFloatUnit. */
    public static class TimeFloatUnit extends Release<UnitTimeFloat, UnitTimeFloat, SimTimeFloatUnit>
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * Constructor for Release.
         * @param simulator on which is scheduled
         * @param resource which is released
         */
        public TimeFloatUnit(final DEVSSimulatorInterface.TimeFloatUnit simulator,
                final Resource<SimTimeFloatUnit> resource)
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
                final Resource<SimTimeFloatUnit> resource, final double amount)
        {
            super(simulator, resource, amount);
        }
    }

    /** Easy access class Release.TimeLongUnit. */
    public static class TimeLongUnit extends Release<UnitTimeLong, UnitTimeLong, SimTimeLongUnit>
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * Constructor for Release.
         * @param simulator on which is scheduled
         * @param resource which is released
         */
        public TimeLongUnit(final DEVSSimulatorInterface.TimeLongUnit simulator,
                final Resource<SimTimeLongUnit> resource)
        {
            super(simulator, resource);
        }

        /**
         * Constructor for Release.
         * @param simulator on which is scheduled
         * @param resource which is released
         * @param amount of resource which is released
         */
        public TimeLongUnit(final DEVSSimulatorInterface.TimeLongUnit simulator,
                final Resource<SimTimeLongUnit> resource, final double amount)
        {
            super(simulator, resource, amount);
        }
    }

    /** Easy access class Release.CalendarDouble. */
    public static class CalendarDouble extends Release<Calendar, UnitTimeDouble, SimTimeCalendarDouble>
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * Constructor for Release.
         * @param simulator on which is scheduled
         * @param resource which is released
         */
        public CalendarDouble(final DEVSSimulatorInterface.CalendarDouble simulator,
                final Resource<SimTimeCalendarDouble> resource)
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
                final Resource<SimTimeCalendarDouble> resource, final double amount)
        {
            super(simulator, resource, amount);
        }
    }

    /** Easy access class Release.CalendarFloat. */
    public static class CalendarFloat extends Release<Calendar, UnitTimeFloat, SimTimeCalendarFloat>
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * Constructor for Release.
         * @param simulator on which is scheduled
         * @param resource which is released
         */
        public CalendarFloat(final DEVSSimulatorInterface.CalendarFloat simulator,
                final Resource<SimTimeCalendarFloat> resource)
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
                final Resource<SimTimeCalendarFloat> resource, final double amount)
        {
            super(simulator, resource, amount);
        }
    }

    /** Easy access class Release.CalendarLong. */
    public static class CalendarLong extends Release<Calendar, UnitTimeLong, SimTimeCalendarLong>
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * Constructor for Release.
         * @param simulator on which is scheduled
         * @param resource which is released
         */
        public CalendarLong(final DEVSSimulatorInterface.CalendarLong simulator,
                final Resource<SimTimeCalendarLong> resource)
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
                final Resource<SimTimeCalendarLong> resource, final double amount)
        {
            super(simulator, resource, amount);
        }
    }
}
