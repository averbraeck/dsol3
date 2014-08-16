/*
 * @(#)ModelInterface.java Aug 18, 2003 Copyright (c) 2002-2005 Delft University
 * of Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. This software is proprietary information of Delft University of
 * Technology .
 */
package nl.tudelft.simulation.dsol;

import java.io.Serializable;
import java.rmi.RemoteException;
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
import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;

/**
 * The model interface defines the model object. Since version 2.1.0 of DSOL, the ModelInterface now knows its simulator
 * and can return it to anyone interested. Through the Simulator, the Replication can be requested and through that the
 * Experiment and the Treatment under which the simulation is running.
 * <p>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl"> www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="http://www.peter-jacobs.com">Peter Jacobs </a>
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:36:43 $
 * @param <A> the absolute storage type for the simulation time, e.g. Calendar, UnitTimeDouble, or Double.
 * @param <R> the relative type for time storage, e.g. Long for the Calendar. For most non-calendar types, the absolute
 *            and relative types are the same.
 * @param <T> the extended type itself to be able to implement a comparator on the simulation time.
 * @since 1.5
 */
public interface ModelInterface<A extends Comparable<A>, R extends Number & Comparable<R>, T extends SimTime<A, R, T>>
        extends Serializable
{
    /**
     * construct a model on a simulator
     * @param simulator is the simulator
     * @throws SimRuntimeException on model failure
     * @throws RemoteException on network failure
     */
    void constructModel(SimulatorInterface<A, R, T> simulator) throws SimRuntimeException, RemoteException;

    /**
     * @return the simulator for the model
     * @throws RemoteException on network failure
     */
    SimulatorInterface<A, R, T> getSimulator() throws RemoteException;

    /***********************************************************************************************************/
    /*********************************** EASY ACCESS INTERFACE EXTENSIONS **************************************/
    /***********************************************************************************************************/

    /** Easy access interface ModelInterface.Double */
    public static interface Double extends ModelInterface<java.lang.Double, java.lang.Double, SimTimeDouble>
    {
        // typed extension
    }

    /** Easy access interface ModelInterface.Float */
    public static interface Float extends ModelInterface<java.lang.Float, java.lang.Float, SimTimeFloat>
    {
        // typed extension
    }

    /** Easy access interface ModelInterface.Long */
    public static interface Long extends ModelInterface<java.lang.Long, java.lang.Long, SimTimeLong>
    {
        // typed extension
    }

    /** Easy access interface ModelInterface.DoubleUnit */
    public static interface DoubleUnit extends ModelInterface<UnitTimeDouble, UnitTimeDouble, SimTimeDoubleUnit>
    {
        // typed extension
    }

    /** Easy access interface ModelInterface.FloatUnit */
    public static interface FloatUnit extends ModelInterface<UnitTimeFloat, UnitTimeFloat, SimTimeFloatUnit>
    {
        // typed extension
    }

    /** Easy access interface ModelInterface.LongUnit */
    public static interface LongUnit extends ModelInterface<UnitTimeLong, UnitTimeLong, SimTimeLongUnit>
    {
        // typed extension
    }

    /** Easy access interface ModelInterface.CalendarDouble */
    public static interface CalendarDouble extends ModelInterface<Calendar, UnitTimeDouble, SimTimeCalendarDouble>
    {
        // typed extension
    }

    /** Easy access interface ModelInterface.CalendarFloat */
    public static interface CalendarFloat extends ModelInterface<Calendar, UnitTimeFloat, SimTimeCalendarFloat>
    {
        // typed extension
    }

    /** Easy access interface ModelInterface.CalendarLong */
    public static interface CalendarLong extends ModelInterface<Calendar, UnitTimeLong, SimTimeCalendarLong>
    {
        // typed extension
    }

}