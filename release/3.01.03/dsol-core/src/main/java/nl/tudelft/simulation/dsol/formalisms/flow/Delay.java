package nl.tudelft.simulation.dsol.formalisms.flow;

import java.rmi.RemoteException;
import java.util.Calendar;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
import nl.tudelft.simulation.dsol.simtime.dist.DistContinuousTime;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;

/**
 * The Delay object is a station which delays an entity by some time units. When an entity arrives at a delay object,
 * dsol delays the entity by the resulting time period. During the time delay, the entity is held in the delay object.
 * <p>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl"> www.simulation.tudelft.nl </a> <br>
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
public class Delay<A extends Comparable<A>, R extends Number & Comparable<R>, T extends SimTime<A, R, T>>
        extends Station<A, R, T>
{
    /** */
    private static final long serialVersionUID = 20140805L;

    /** delayDistribution which is the distribution defining the delay. */
    private DistContinuousTime<R> delayDistribution;
    
    /** the logger. */
    private static Logger logger = LogManager.getLogger(Delay.class);

    /**
     * Constructor for Delay.
     * @param simulator is the simulator
     * @param delayDistribution is the delayDistribution
     */
    public Delay(final DEVSSimulatorInterface<A, R, T> simulator, final DistContinuousTime<R> delayDistribution)
    {
        super(simulator);
        this.delayDistribution = delayDistribution;
    }

    /** {@inheritDoc} */
    @Override
    public final synchronized void receiveObject(final Object object) throws RemoteException
    {
        super.receiveObject(object);
        try
        {
            this.simulator.scheduleEventRel(this.delayDistribution.draw(), this, this, "releaseObject",
                    new Object[]{object});
        }
        catch (Exception exception)
        {
            logger.warn("receiveObject", exception);
        }
    }

    /***********************************************************************************************************/
    /************************************* EASY ACCESS CLASS EXTENSIONS ****************************************/
    /***********************************************************************************************************/

    /** Easy access class Delay.TimeDouble. */
    public static class TimeDouble extends Delay<Double, Double, SimTimeDouble>
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * Constructor for TimeDouble Delay.
         * @param simulator is the simulator
         * @param delayDistribution is the delayDistribution
         */
        public TimeDouble(final DEVSSimulatorInterface.TimeDouble simulator,
                final DistContinuousTime.TimeDouble delayDistribution)
        {
            super(simulator, delayDistribution);
        }
    }

    /** Easy access class Delay.TimeFloat. */
    public static class TimeFloat extends Delay<Float, Float, SimTimeFloat>
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * Constructor for TimeFloat Delay.
         * @param simulator is the simulator
         * @param delayDistribution is the delayDistribution
         */
        public TimeFloat(final DEVSSimulatorInterface.TimeFloat simulator,
                final DistContinuousTime.TimeFloat delayDistribution)
        {
            super(simulator, delayDistribution);
        }
    }

    /** Easy access class Delay.TimeLong. */
    public static class TimeLong extends Delay<Long, Long, SimTimeLong>
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * Constructor for TimeLong Delay.
         * @param simulator is the simulator
         * @param delayDistribution is the delayDistribution
         */
        public TimeLong(final DEVSSimulatorInterface.TimeLong simulator,
                final DistContinuousTime.TimeLong delayDistribution)
        {
            super(simulator, delayDistribution);
        }
    }

    /** Easy access class Delay.TimeDoubleUnit. */
    public static class TimeDoubleUnit extends Delay<UnitTimeDouble, UnitTimeDouble, SimTimeDoubleUnit>
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * Constructor for TimeDoubleUnit Delay.
         * @param simulator is the simulator
         * @param delayDistribution is the delayDistribution
         */
        public TimeDoubleUnit(final DEVSSimulatorInterface.TimeDoubleUnit simulator,
                final DistContinuousTime.TimeDoubleUnit delayDistribution)
        {
            super(simulator, delayDistribution);
        }
    }

    /** Easy access class Delay.TimeFloatUnit. */
    public static class TimeFloatUnit extends Delay<UnitTimeFloat, UnitTimeFloat, SimTimeFloatUnit>
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * Constructor for TimeFloatUnit Delay.
         * @param simulator is the simulator
         * @param delayDistribution is the delayDistribution
         */
        public TimeFloatUnit(final DEVSSimulatorInterface.TimeFloatUnit simulator,
                final DistContinuousTime.TimeFloatUnit delayDistribution)
        {
            super(simulator, delayDistribution);
        }
    }

    /** Easy access class Delay.TimeLongUnit. */
    public static class TimeLongUnit extends Delay<UnitTimeLong, UnitTimeLong, SimTimeLongUnit>
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * Constructor for TimeLongUnit Delay.
         * @param simulator is the simulator
         * @param delayDistribution is the delayDistribution
         */
        public TimeLongUnit(final DEVSSimulatorInterface.TimeLongUnit simulator,
                final DistContinuousTime.TimeLongUnit delayDistribution)
        {
            super(simulator, delayDistribution);
        }
    }

    /** Easy access class Delay.CalendarDouble. */
    public static class CalendarDouble extends Delay<Calendar, UnitTimeDouble, SimTimeCalendarDouble>
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * Constructor for CalendarDouble Delay.
         * @param simulator is the simulator
         * @param delayDistribution is the delayDistribution
         */
        public CalendarDouble(final DEVSSimulatorInterface.CalendarDouble simulator,
                final DistContinuousTime.CalendarDouble delayDistribution)
        {
            super(simulator, delayDistribution);
        }
    }

    /** Easy access class Delay.CalendarFloat. */
    public static class CalendarFloat extends Delay<Calendar, UnitTimeFloat, SimTimeCalendarFloat>
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * Constructor for CalendarFloat Delay.
         * @param simulator is the simulator
         * @param delayDistribution is the delayDistribution
         */
        public CalendarFloat(final DEVSSimulatorInterface.CalendarFloat simulator,
                final DistContinuousTime.CalendarFloat delayDistribution)
        {
            super(simulator, delayDistribution);
        }
    }

    /** Easy access class Delay.CalendarLong. */
    public static class CalendarLong extends Delay<Calendar, UnitTimeLong, SimTimeCalendarLong>
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * Constructor for CalendarLong Delay.
         * @param simulator is the simulator
         * @param delayDistribution is the delayDistribution
         */
        public CalendarLong(final DEVSSimulatorInterface.CalendarLong simulator,
                final DistContinuousTime.CalendarLong delayDistribution)
        {
            super(simulator, delayDistribution);
        }
    }
}
