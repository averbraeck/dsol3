package nl.tudelft.simulation.dsol.formalisms.flow;

import java.util.Calendar;

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
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;
import nl.tudelft.simulation.event.EventProducer;

/**
 * A station is an object which can accept other objects.
 * <p>
 * (c) 2002-2018 <a href="https://simulation.tudelft.nl">Delft University of Technology </a>, the Netherlands. <br>
 * See for project information <a href="https://simulation.tudelft.nl"> www.simulation.tudelft.nl </a> <br>
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
public abstract class Station<A extends Comparable<A>, R extends Number & Comparable<R>, T extends SimTime<A, R, T>>
        extends EventProducer implements StationInterface
{
    /** */
    private static final long serialVersionUID = 20140805L;

    /** simulator is the simulator on which behavior is scheduled. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected DEVSSimulatorInterface<A, R, T> simulator;

    /** destination refers to the next station in the process-model chain. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected StationInterface destination;

    /**
     * constructs a new Station.
     * @param simulator is the simulator on which behavior is scheduled
     */
    public Station(final DEVSSimulatorInterface<A, R, T> simulator)
    {
        super();
        this.simulator = simulator;
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("checkstyle:designforextension")
    public void receiveObject(final Object object)
    {
        this.fireTimedEvent(StationInterface.RECEIVE_EVENT, 1.0, this.simulator.getSimulatorTime());
    }

    /** {@inheritDoc} */
    @Override
    public final void setDestination(final StationInterface destination)
    {
        this.destination = destination;
    }

    /**
     * releases an object.
     * @param object is the entity
     */
    @SuppressWarnings("checkstyle:designforextension")
    protected synchronized void releaseObject(final Object object)
    {
        this.fireTimedEvent(StationInterface.RELEASE_EVENT, 0.0, this.simulator.getSimulatorTime());
        if (this.destination != null)
        {
            this.destination.receiveObject(object);
        }
    }

    /** {@inheritDoc} */
    @Override
    public final StationInterface getDestination()
    {
        return this.destination;
    }

    /***********************************************************************************************************/
    /************************************* EASY ACCESS CLASS EXTENSIONS ****************************************/
    /***********************************************************************************************************/

    /** Easy access class Station.TimeDouble. */
    public static class TimeDouble extends Station<Double, Double, SimTimeDouble>
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * constructs a new Station.TimeDouble.
         * @param simulator is the simulator on which behavior is scheduled
         */
        public TimeDouble(final DEVSSimulatorInterface.TimeDouble simulator)
        {
            super(simulator);
        }
    }

    /** Easy access class Station.TimeFloat. */
    public static class TimeFloat extends Station<Float, Float, SimTimeFloat>
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * constructs a new Station.TimeFloat.
         * @param simulator is the simulator on which behavior is scheduled
         */
        public TimeFloat(final DEVSSimulatorInterface.TimeFloat simulator)
        {
            super(simulator);
        }
    }

    /** Easy access class Station.TimeLong. */
    public static class TimeLong extends Station<Long, Long, SimTimeLong>
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * constructs a new Station.TimeLong.
         * @param simulator is the simulator on which behavior is scheduled
         */
        public TimeLong(final DEVSSimulatorInterface.TimeLong simulator)
        {
            super(simulator);
        }
    }

    /** Easy access class Station.TimeDoubleUnit. */
    public static class TimeDoubleUnit extends Station<Time, Duration, SimTimeDoubleUnit>
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * constructs a new Station.TimeDoubleUnit.
         * @param simulator is the simulator on which behavior is scheduled
         */
        public TimeDoubleUnit(final DEVSSimulatorInterface.TimeDoubleUnit simulator)
        {
            super(simulator);
        }
    }

    /** Easy access class Station.TimeFloatUnit. */
    public static class TimeFloatUnit extends Station<FloatTime, FloatDuration, SimTimeFloatUnit>
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * constructs a new Station.TimeFloatUnit.
         * @param simulator is the simulator on which behavior is scheduled
         */
        public TimeFloatUnit(final DEVSSimulatorInterface.TimeFloatUnit simulator)
        {
            super(simulator);
        }
    }

    /** Easy access class Station.CalendarDouble. */
    public static class CalendarDouble extends Station<Calendar, Duration, SimTimeCalendarDouble>
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * constructs a new Station.CalendarDouble.
         * @param simulator is the simulator on which behavior is scheduled
         */
        public CalendarDouble(final DEVSSimulatorInterface.CalendarDouble simulator)
        {
            super(simulator);
        }
    }

    /** Easy access class Station.CalendarFloat. */
    public static class CalendarFloat extends Station<Calendar, FloatDuration, SimTimeCalendarFloat>
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * constructs a new Station.CalendarFloat.
         * @param simulator is the simulator on which behavior is scheduled
         */
        public CalendarFloat(final DEVSSimulatorInterface.CalendarFloat simulator)
        {
            super(simulator);
        }
    }

    /** Easy access class Station.CalendarLong. */
    public static class CalendarLong extends Station<Calendar, Long, SimTimeCalendarLong>
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * constructs a new Station.CalendarLong.
         * @param simulator is the simulator on which behavior is scheduled
         */
        public CalendarLong(final DEVSSimulatorInterface.CalendarLong simulator)
        {
            super(simulator);
        }
    }

}
