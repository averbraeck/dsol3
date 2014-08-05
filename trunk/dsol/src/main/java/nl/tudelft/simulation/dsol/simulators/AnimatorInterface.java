/*
 * @(#)AnimatorInterface.java Aug 18, 2003 Copyright (c) 2002-2005 Delft
 * University of Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All
 * rights reserved. This software is proprietary information of Delft University
 * of Technology 
 */
package nl.tudelft.simulation.dsol.simulators;

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
import nl.tudelft.simulation.event.EventType;

/**
 * The AnimatorInterface defines a DEVSDESS simulator with wallclock delay between the consequtive time steps.
 * <p>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl"> www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="http://www.peter-jacobs.com">Peter Jacobs </a>
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:36:44 $
 * @param <A> the absolute storage type for the simulation time, e.g. Calendar, UnitTimeDouble, or Double.
 * @param <R> the relative type for time storage, e.g. Long for the Calendar. For most non-calendar types, the absolute
 *            and relative types are the same.
 * @param <T> the extended type itself to be able to implement a comparator on the simulation time.
 * @since 1.5
 */
public interface AnimatorInterface<A extends Comparable<A>, R extends Number & Comparable<R>, T extends SimTime<A, R, T>>
        extends DEVDESSSimulatorInterface<A, R, T>
{
    /** DEFAULT_ANIMATION_DELAY of 0 miliseconds used in the animator */
    long DEFAULT_ANIMATION_DELAY = 0L;

    /** UPDATE_ANIMATION_EVENT is fired to wake up animatable components */
    EventType UPDATE_ANIMATION_EVENT = new EventType("UPDATE_ANIMATION_EVENT");

    /** ANIMATION_DELAY_CHANGED_EVENT is fired when the time step is set */
    EventType ANIMATION_DELAY_CHANGED_EVENT = new EventType("ANIMATION_DELAY_CHANGED_EVENT");

    /**
     * returns the animation delay between each consequtive timestep
     * @return the animaiton delay in milliseconds wallclock
     * @throws RemoteException on network failure
     */
    long getAnimationDelay() throws RemoteException;

    /**
     * sets the animationDelay
     * @param miliseconds the animation delay
     * @throws RemoteException on network failure
     */
    void setAnimationDelay(long miliseconds) throws RemoteException;

    /***********************************************************************************************************/
    /*********************************** EASY ACCESS INTERFACE EXTENSIONS **************************************/
    /***********************************************************************************************************/

    /** Easy access interface AnimatorInterface.Double */
    public static interface Double extends AnimatorInterface<java.lang.Double, java.lang.Double, SimTimeDouble>
    {
        // typed extension
    }

    /** Easy access interface AnimatorInterface.Float */
    public static interface Float extends AnimatorInterface<java.lang.Float, java.lang.Float, SimTimeFloat>
    {
        // typed extension
    }

    /** Easy access interface AnimatorInterface.Long */
    public static interface Long extends AnimatorInterface<java.lang.Long, java.lang.Long, SimTimeLong>
    {
        // typed extension
    }

    /** Easy access interface AnimatorInterface.DoubleUnit */
    public static interface DoubleUnit extends AnimatorInterface<UnitTimeDouble, UnitTimeDouble, SimTimeDoubleUnit>
    {
        // typed extension
    }

    /** Easy access interface AnimatorInterface.FloatUnit */
    public static interface FloatUnit extends AnimatorInterface<UnitTimeFloat, UnitTimeFloat, SimTimeFloatUnit>
    {
        // typed extension
    }

    /** Easy access interface AnimatorInterface.LongUnit */
    public static interface LongUnit extends AnimatorInterface<UnitTimeLong, UnitTimeLong, SimTimeLongUnit>
    {
        // typed extension
    }

    /** Easy access interface AnimatorInterface.CalendarDouble */
    public static interface CalendarDouble extends AnimatorInterface<Calendar, UnitTimeDouble, SimTimeCalendarDouble>
    {
        // typed extension
    }

    /** Easy access interface AnimatorInterface.CalendarFloat */
    public static interface CalendarFloat extends AnimatorInterface<Calendar, UnitTimeFloat, SimTimeCalendarFloat>
    {
        // typed extension
    }

    /** Easy access interface AnimatorInterface.CalendarLong */
    public static interface CalendarLong extends AnimatorInterface<Calendar, UnitTimeLong, SimTimeCalendarLong>
    {
        // typed extension
    }

}