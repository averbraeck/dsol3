package nl.tudelft.simulation.dsol.simulators;

import java.util.Calendar;

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

/**
 * The DEVSDESS simulator embodies both the continuous and the discrete formalism. This simulator takes pre-defined time
 * steps in between it loops over its eventlist. A better name for this formalism would therefore be the DEVSinDESS
 * formalism. More information on Modeling & Simulation can be found in Theory of Modeling and Simulation by Bernard
 * Zeigler et. al.
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
public interface DEVDESSSimulatorInterface<A extends Comparable<A>, R extends Number & Comparable<R>, T extends SimTime<A, R, T>>
        extends DEVSSimulatorInterface<A, R, T>, DESSSimulatorInterface<A, R, T>
{
    // This interface combines the DESS and DEVS interfaces and does not add any operations.

    /***********************************************************************************************************/
    /*********************************** EASY ACCESS INTERFACE EXTENSIONS **************************************/
    /***********************************************************************************************************/

    /** Easy access interface DEVDESSSimulatorInterface.Double. */
    public static interface Double extends DEVDESSSimulatorInterface<java.lang.Double, java.lang.Double, SimTimeDouble>
    {
        // typed extension
    }

    /** Easy access interface DEVDESSSimulatorInterface.Float. */
    public static interface Float extends DEVDESSSimulatorInterface<java.lang.Float, java.lang.Float, SimTimeFloat>
    {
        // typed extension
    }

    /** Easy access interface DEVDESSSimulatorInterface.Long. */
    public static interface Long extends DEVDESSSimulatorInterface<java.lang.Long, java.lang.Long, SimTimeLong>
    {
        // typed extension
    }

    /** Easy access interface DEVDESSSimulatorInterface.DoubleUnit. */
    public static interface DoubleUnit extends
            DEVDESSSimulatorInterface<UnitTimeDouble, UnitTimeDouble, SimTimeDoubleUnit>
    {
        // typed extension
    }

    /** Easy access interface DEVDESSSimulatorInterface.FloatUnit. */
    public static interface FloatUnit extends DEVDESSSimulatorInterface<UnitTimeFloat, UnitTimeFloat, SimTimeFloatUnit>
    {
        // typed extension
    }

    /** Easy access interface DEVDESSSimulatorInterface.LongUnit. */
    public static interface LongUnit extends DEVDESSSimulatorInterface<UnitTimeLong, UnitTimeLong, SimTimeLongUnit>
    {
        // typed extension
    }

    /** Easy access interface DEVDESSSimulatorInterface.CalendarDouble. */
    public static interface CalendarDouble extends
            DEVDESSSimulatorInterface<Calendar, UnitTimeDouble, SimTimeCalendarDouble>
    {
        // typed extension
    }

    /** Easy access interface DEVDESSSimulatorInterface.CalendarFloat. */
    public static interface CalendarFloat extends
            DEVDESSSimulatorInterface<Calendar, UnitTimeFloat, SimTimeCalendarFloat>
    {
        // typed extension
    }

    /** Easy access interface DEVDESSSimulatorInterface.CalendarLong. */
    public static interface CalendarLong extends DEVDESSSimulatorInterface<Calendar, UnitTimeLong, SimTimeCalendarLong>
    {
        // typed extension
    }

}
