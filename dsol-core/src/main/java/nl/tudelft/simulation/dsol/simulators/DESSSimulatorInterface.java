package nl.tudelft.simulation.dsol.simulators;

import java.rmi.RemoteException;
import java.util.Calendar;

import nl.tudelft.simulation.dsol.SimRuntimeException;
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
import nl.tudelft.simulation.event.EventType;

/**
 * The DESS defines the interface of the DESS simulator. DESS stands for the Differential Equation System Specification.
 * More information on Modeling and Simulation can be found in "Theory of Modeling and Simulation" by Bernard Zeigler
 * et. al.
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

public interface DESSSimulatorInterface<A extends Comparable<A>, R extends Number & Comparable<R>, T extends SimTime<A, R, T>>
        extends SimulatorInterface<A, R, T>
{
    /** TIME_STEP_CHANGED_EVENT is fired when the time step is set. */
    EventType TIME_STEP_CHANGED_EVENT = new EventType("TIME_STEP_CHANGED_EVENT");

    /**
     * returns the time step of the DESS simulator.
     * @return the timeStep
     * @throws RemoteException on network failure
     */
    R getTimeStep() throws RemoteException;

    /**
     * Method setTimeStep sets the time step of the simulator.
     * @param timeStep the new timeStep. Its value should be &gt; 0.0
     * @throws SimRuntimeException when timestep &lt;= 0, NaN, or Infinity
     * @throws RemoteException on network failure
     */
    void setTimeStep(R timeStep) throws SimRuntimeException, RemoteException;

    /***********************************************************************************************************/
    /*********************************** EASY ACCESS INTERFACE EXTENSIONS **************************************/
    /***********************************************************************************************************/

    /** Easy access interface DESSSimulatorInterface.TimeDouble. */
    public interface TimeDouble
            extends DESSSimulatorInterface<Double, Double, SimTimeDouble>, SimulatorInterface.TimeDouble
    {
        // typed extension
    }

    /** Easy access interface DESSSimulatorInterface.TimeFloat. */
    public interface TimeFloat extends DESSSimulatorInterface<Float, Float, SimTimeFloat>, SimulatorInterface.TimeFloat
    {
        // typed extension
    }

    /** Easy access interface DESSSimulatorInterface.TimeLong. */
    public interface TimeLong extends DESSSimulatorInterface<Long, Long, SimTimeLong>, SimulatorInterface.TimeLong
    {
        // typed extension
    }

    /** Easy access interface DESSSimulatorInterface.TimeDoubleUnit. */
    public interface TimeDoubleUnit extends DESSSimulatorInterface<UnitTimeDouble, UnitTimeDouble, SimTimeDoubleUnit>,
            SimulatorInterface.TimeDoubleUnit
    {
        // typed extension
    }

    /** Easy access interface DESSSimulatorInterface.TimeFloatUnit. */
    public interface TimeFloatUnit extends DESSSimulatorInterface<UnitTimeFloat, UnitTimeFloat, SimTimeFloatUnit>,
            SimulatorInterface.TimeFloatUnit
    {
        // typed extension
    }

    /** Easy access interface DESSSimulatorInterface.TimeLongUnit. */
    public interface TimeLongUnit
            extends DESSSimulatorInterface<UnitTimeLong, UnitTimeLong, SimTimeLongUnit>, SimulatorInterface.TimeLongUnit
    {
        // typed extension
    }

    /** Easy access interface DESSSimulatorInterface.CalendarDouble. */
    public interface CalendarDouble extends DESSSimulatorInterface<Calendar, UnitTimeDouble, SimTimeCalendarDouble>,
            SimulatorInterface.CalendarDouble
    {
        // typed extension
    }

    /** Easy access interface DESSSimulatorInterface.CalendarFloat. */
    public interface CalendarFloat extends DESSSimulatorInterface<Calendar, UnitTimeFloat, SimTimeCalendarFloat>,
            SimulatorInterface.CalendarFloat
    {
        // typed extension
    }

    /** Easy access interface DESSSimulatorInterface.CalendarLong. */
    public interface CalendarLong
            extends DESSSimulatorInterface<Calendar, UnitTimeLong, SimTimeCalendarLong>, SimulatorInterface.CalendarLong
    {
        // typed extension
    }

}
