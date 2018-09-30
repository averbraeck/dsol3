package nl.tudelft.simulation.dsol.formalisms.flow;

import java.io.Serializable;
import java.rmi.MarshalledObject;
import java.util.Calendar;

import org.djunits.value.vdouble.scalar.Duration;
import org.djunits.value.vdouble.scalar.Time;
import org.djunits.value.vfloat.scalar.FloatDuration;
import org.djunits.value.vfloat.scalar.FloatTime;

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
 * A duplicate station duplicates incoming objects and sends them to their alternative destination.
 * <p>
 * (c) 2002-2018 <a href="https://simulation.tudelft.nl">Delft University of Technology </a>, the Netherlands. <br>
 * See for project information <a href="https://simulation.tudelft.nl"> www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @param <A> the absolute storage type for the simulation time, e.g. Calendar, Duration, or Double.
 * @param <R> the relative type for time storage, e.g. Long for the Calendar. For most non-calendar types, the absolute
 *            and relative types are the same.
 * @param <T> the extended type itself to be able to implement a comparator on the simulation time.
 * @since 1.5
 */
public class Duplicate<A extends Comparable<A>, R extends Number & Comparable<R>, T extends SimTime<A, R, T>>
        extends Station<A, R, T>
{
    /** */
    private static final long serialVersionUID = 1L;

    /** DuplicateDestination which is the duplicate definition. */
    private StationInterface<A, R, T> duplicateDestination;

    /** numberCopies refers to the number of duplicates. */
    private int numberCopies;

    /**
     * Creates a new Duplicate that makes 1 copy.
     * @param simulator on which is scheduled
     * @param duplicateDestination the duplicate destination
     */
    public Duplicate(final DEVSSimulatorInterface<A, R, T> simulator,
            final StationInterface<A, R, T> duplicateDestination)
    {
        this(simulator, duplicateDestination, 1);
    }

    /**
     * Create a new Duplicate that makes numberCopies copies.
     * @param simulator on which is scheduled
     * @param duplicateDestination which is the duplicate definition
     * @param numberCopies the number of copies
     */
    public Duplicate(final DEVSSimulatorInterface<A, R, T> simulator,
            final StationInterface<A, R, T> duplicateDestination, final int numberCopies)
    {
        super(simulator);
        this.duplicateDestination = duplicateDestination;
        this.numberCopies = numberCopies;
    }

    /** {@inheritDoc} */
    @Override
    public synchronized void receiveObject(final Object object)
    {
        super.receiveObject(object);
        try
        {
            this.releaseObject(object);
            if (object instanceof Serializable)
            {
                for (int i = 0; i < this.numberCopies; i++)
                {
                    Object clone = new MarshalledObject<Object>(object).get();
                    this.fireEvent(StationInterface.RELEASE_EVENT, 1);
                    this.duplicateDestination.receiveObject(clone);
                }
            }
            else
            {
                throw new Exception(
                        "cannot duplicate object: " + object.getClass() + " does not implement java.io.Serializable");
            }
        }
        catch (Exception exception)
        {
            SimLogger.always().warn(exception, "receiveMethod");
        }
    }

    /***********************************************************************************************************/
    /************************************* EASY ACCESS CLASS EXTENSIONS ****************************************/
    /***********************************************************************************************************/

    /** Easy access class Duplicate.TimeDouble. */
    public static class TimeDouble extends Duplicate<Double, Double, SimTimeDouble>
            implements StationInterface.TimeDouble
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * Creates a new Duplicate that makes 1 copy.
         * @param simulator on which is scheduled
         * @param duplicateDestination the duplicate destination
         */
        public TimeDouble(final DEVSSimulatorInterface.TimeDouble simulator,
                final StationInterface.TimeDouble duplicateDestination)
        {
            super(simulator, duplicateDestination);
        }

        /**
         * Create a new Duplicate that makes numberCopies copies.
         * @param simulator on which is scheduled
         * @param duplicateDestination which is the duplicate definition
         * @param numberCopies the number of copies
         */
        public TimeDouble(final DEVSSimulatorInterface.TimeDouble simulator,
                final StationInterface.TimeDouble duplicateDestination, final int numberCopies)
        {
            super(simulator, duplicateDestination, numberCopies);
        }
    }

    /** Easy access class Duplicate.TimeFloat. */
    public static class TimeFloat extends Duplicate<Float, Float, SimTimeFloat> implements StationInterface.TimeFloat
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * Creates a new Duplicate that makes 1 copy.
         * @param simulator on which is scheduled
         * @param duplicateDestination the duplicate destination
         */
        public TimeFloat(final DEVSSimulatorInterface.TimeFloat simulator,
                final StationInterface.TimeFloat duplicateDestination)
        {
            super(simulator, duplicateDestination);
        }

        /**
         * Create a new Duplicate that makes numberCopies copies.
         * @param simulator on which is scheduled
         * @param duplicateDestination which is the duplicate definition
         * @param numberCopies the number of copies
         */
        public TimeFloat(final DEVSSimulatorInterface.TimeFloat simulator,
                final StationInterface.TimeFloat duplicateDestination, final int numberCopies)
        {
            super(simulator, duplicateDestination, numberCopies);
        }
    }

    /** Easy access class Duplicate.TimeLong. */
    public static class TimeLong extends Duplicate<Long, Long, SimTimeLong> implements StationInterface.TimeLong
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * Creates a new Duplicate that makes 1 copy.
         * @param simulator on which is scheduled
         * @param duplicateDestination the duplicate destination
         */
        public TimeLong(final DEVSSimulatorInterface.TimeLong simulator,
                final StationInterface.TimeLong duplicateDestination)
        {
            super(simulator, duplicateDestination);
        }

        /**
         * Create a new Duplicate that makes numberCopies copies.
         * @param simulator on which is scheduled
         * @param duplicateDestination which is the duplicate definition
         * @param numberCopies the number of copies
         */
        public TimeLong(final DEVSSimulatorInterface.TimeLong simulator,
                final StationInterface.TimeLong duplicateDestination, final int numberCopies)
        {
            super(simulator, duplicateDestination, numberCopies);
        }
    }

    /** Easy access class Duplicate.TimeDoubleUnit. */
    public static class TimeDoubleUnit extends Duplicate<Time, Duration, SimTimeDoubleUnit>
            implements StationInterface.TimeDoubleUnit
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * Creates a new Duplicate that makes 1 copy.
         * @param simulator on which is scheduled
         * @param duplicateDestination the duplicate destination
         */
        public TimeDoubleUnit(final DEVSSimulatorInterface.TimeDoubleUnit simulator,
                final StationInterface.TimeDoubleUnit duplicateDestination)
        {
            super(simulator, duplicateDestination);
        }

        /**
         * Create a new Duplicate that makes numberCopies copies.
         * @param simulator on which is scheduled
         * @param duplicateDestination which is the duplicate definition
         * @param numberCopies the number of copies
         */
        public TimeDoubleUnit(final DEVSSimulatorInterface.TimeDoubleUnit simulator,
                final StationInterface.TimeDoubleUnit duplicateDestination, final int numberCopies)
        {
            super(simulator, duplicateDestination, numberCopies);
        }
    }

    /** Easy access class Duplicate.TimeFloatUnit. */
    public static class TimeFloatUnit extends Duplicate<FloatTime, FloatDuration, SimTimeFloatUnit>
            implements StationInterface.TimeFloatUnit
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * Creates a new Duplicate that makes 1 copy.
         * @param simulator on which is scheduled
         * @param duplicateDestination the duplicate destination
         */
        public TimeFloatUnit(final DEVSSimulatorInterface.TimeFloatUnit simulator,
                final StationInterface.TimeFloatUnit duplicateDestination)
        {
            super(simulator, duplicateDestination);
        }

        /**
         * Create a new Duplicate that makes numberCopies copies.
         * @param simulator on which is scheduled
         * @param duplicateDestination which is the duplicate definition
         * @param numberCopies the number of copies
         */
        public TimeFloatUnit(final DEVSSimulatorInterface.TimeFloatUnit simulator,
                final StationInterface.TimeFloatUnit duplicateDestination, final int numberCopies)
        {
            super(simulator, duplicateDestination, numberCopies);
        }
    }

    /** Easy access class Duplicate.CalendarDouble. */
    public static class CalendarDouble extends Duplicate<Calendar, Duration, SimTimeCalendarDouble>
            implements StationInterface.CalendarDouble
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * Creates a new Duplicate that makes 1 copy.
         * @param simulator on which is scheduled
         * @param duplicateDestination the duplicate destination
         */
        public CalendarDouble(final DEVSSimulatorInterface.CalendarDouble simulator,
                final StationInterface.CalendarDouble duplicateDestination)
        {
            super(simulator, duplicateDestination);
        }

        /**
         * Create a new Duplicate that makes numberCopies copies.
         * @param simulator on which is scheduled
         * @param duplicateDestination which is the duplicate definition
         * @param numberCopies the number of copies
         */
        public CalendarDouble(final DEVSSimulatorInterface.CalendarDouble simulator,
                final StationInterface.CalendarDouble duplicateDestination, final int numberCopies)
        {
            super(simulator, duplicateDestination, numberCopies);
        }
    }

    /** Easy access class Duplicate.CalendarFloat. */
    public static class CalendarFloat extends Duplicate<Calendar, FloatDuration, SimTimeCalendarFloat>
            implements StationInterface.CalendarFloat
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * Creates a new Duplicate that makes 1 copy.
         * @param simulator on which is scheduled
         * @param duplicateDestination the duplicate destination
         */
        public CalendarFloat(final DEVSSimulatorInterface.CalendarFloat simulator,
                final StationInterface.CalendarFloat duplicateDestination)
        {
            super(simulator, duplicateDestination);
        }

        /**
         * Create a new Duplicate that makes numberCopies copies.
         * @param simulator on which is scheduled
         * @param duplicateDestination which is the duplicate definition
         * @param numberCopies the number of copies
         */
        public CalendarFloat(final DEVSSimulatorInterface.CalendarFloat simulator,
                final StationInterface.CalendarFloat duplicateDestination, final int numberCopies)
        {
            super(simulator, duplicateDestination, numberCopies);
        }
    }

    /** Easy access class Duplicate.CalendarLong. */
    public static class CalendarLong extends Duplicate<Calendar, Long, SimTimeCalendarLong>
            implements StationInterface.CalendarLong
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * Creates a new Duplicate that makes 1 copy.
         * @param simulator on which is scheduled
         * @param duplicateDestination the duplicate destination
         */
        public CalendarLong(final DEVSSimulatorInterface.CalendarLong simulator,
                final StationInterface.CalendarLong duplicateDestination)
        {
            super(simulator, duplicateDestination);
        }

        /**
         * Create a new Duplicate that makes numberCopies copies.
         * @param simulator on which is scheduled
         * @param duplicateDestination which is the duplicate definition
         * @param numberCopies the number of copies
         */
        public CalendarLong(final DEVSSimulatorInterface.CalendarLong simulator,
                final StationInterface.CalendarLong duplicateDestination, final int numberCopies)
        {
            super(simulator, duplicateDestination, numberCopies);
        }
    }

}
