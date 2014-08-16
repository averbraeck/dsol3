/*
 * @(#)DEVSSimulatorInterface.java Aug 18, 2003 Copyright (c) 2002-2005 Delft
 * University of Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All
 * rights reserved. This software is proprietary information of Delft University
 * of Technology 
 */
package nl.tudelft.simulation.dsol.simulators;

import java.rmi.RemoteException;
import java.util.Calendar;

import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.eventlists.EventListInterface;
import nl.tudelft.simulation.dsol.formalisms.eventscheduling.SimEventInterface;
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
 * The DEVS defines the interface of the DEVS simulator. DEVS stands for the Discrete Event System Specification. More
 * information on Discrete Event Simulation can be found in "Theory of Modeling and Simulation" by Bernard Zeigler et.
 * al.
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
public interface DEVSSimulatorInterface<A extends Comparable<A>, R extends Number & Comparable<R>, T extends SimTime<A, R, T>>
        extends SimulatorInterface<A, R, T>
{
    /** The EVENTLIST_CHANGED_EVENT is fired when the eventList is changed */
    EventType EVENTLIST_CHANGED_EVENT = new EventType("EVENTLIST_CHANGED_EVENT");

    /**
     * cancels an event from the event list.
     * @param event a simulation event to be canceled.
     * @return boolean the succes of the operation.
     * @throws RemoteException on network failure.
     */
    boolean cancelEvent(SimEventInterface<T> event) throws RemoteException;

    /**
     * returns the eventlist of the simulator.
     * @return the eventlist.
     * @throws RemoteException on network failure
     */
    EventListInterface<T> getEventList() throws RemoteException;

    /**
     * Method scheduleEvent schedules an event on the eventlist.
     * @param event a simulation event
     * @throws RemoteException on network failure
     * @throws SimRuntimeException whenever event is scheduled in past.
     */
    void scheduleEvent(SimEventInterface<T> event) throws RemoteException, SimRuntimeException;

    /**
     * schedules a methodCall at a relative duration. The executionTime is thus
     * simulator.getSimulatorTime()+relativeDuration.
     * @param relativeDelay the relativeDelay in timeUnits of the simulator.
     * @param priority the priority compared to other events scheduled at the same time.
     * @param source the source of the event
     * @param target the target
     * @param method the method
     * @param args the arguments.
     * @throws RemoteException on network failure.
     * @throws SimRuntimeException whenever the event is scheduled in the past.
     */
    void scheduleEventRel(R relativeDelay, short priority, Object source, Object target, String method, Object[] args)
            throws RemoteException, SimRuntimeException;

    /**
     * schedules a methodCall at a relative duration. The executionTime is thus
     * simulator.getSimulatorTime()+relativeDuration.
     * @param relativeDelay the relativeDelay in timeUnits of the simulator.
     * @param source the source of the event
     * @param target the target
     * @param method the method
     * @param args the arguments.
     * @throws RemoteException on network failure.
     * @throws SimRuntimeException whenever the event is scheduled in the past.
     */
    void scheduleEventRel(R relativeDelay, Object source, Object target, String method, Object[] args)
            throws RemoteException, SimRuntimeException;

    /**
     * schedules a methodCall at an absolute time.
     * @param absoluteTime the exact time to schedule the method on the simulator.
     * @param priority the priority compared to other events scheduled at the same time.
     * @param source the source of the event
     * @param target the target
     * @param method the method
     * @param args the arguments.
     * @throws RemoteException on network failure.
     * @throws SimRuntimeException whenever the event is scheduled in the past.
     */
    void scheduleEventAbs(T absoluteTime, short priority, Object source, Object target, String method, Object[] args)
            throws RemoteException, SimRuntimeException;

    /**
     * schedules a methodCall at an absolute time.
     * @param absoluteTime the exact time to schedule the method on the simulator.
     * @param source the source of the event
     * @param target the target
     * @param method the method
     * @param args the arguments.
     * @throws RemoteException on network failure.
     * @throws SimRuntimeException whenever the event is scheduled in the past.
     */
    void scheduleEventAbs(A absoluteTime, Object source, Object target, String method, Object[] args)
            throws RemoteException, SimRuntimeException;

    /**
     * schedules a methodCall at an absolute time.
     * @param absoluteTime the exact time to schedule the method on the simulator.
     * @param priority the priority compared to other events scheduled at the same time.
     * @param source the source of the event
     * @param target the target
     * @param method the method
     * @param args the arguments.
     * @throws RemoteException on network failure.
     * @throws SimRuntimeException whenever the event is scheduled in the past.
     */
    void scheduleEventAbs(A absoluteTime, short priority, Object source, Object target, String method, Object[] args)
            throws RemoteException, SimRuntimeException;

    /**
     * schedules a methodCall at an absolute time.
     * @param absoluteTime the exact time to schedule the method on the simulator.
     * @param source the source of the event
     * @param target the target
     * @param method the method
     * @param args the arguments.
     * @throws RemoteException on network failure.
     * @throws SimRuntimeException whenever the event is scheduled in the past.
     */
    void scheduleEventAbs(T absoluteTime, Object source, Object target, String method, Object[] args)
            throws RemoteException, SimRuntimeException;

    /**
     * schedules a methodCall immediately.
     * @param priority the priority compared to other events scheduled at the same time.
     * @param source the source of the event
     * @param target the target
     * @param method the method
     * @param args the arguments.
     * @throws RemoteException on network failure.
     * @throws SimRuntimeException whenever the event is scheduled in the past.
     */
    void scheduleEventNow(short priority, Object source, Object target, String method, Object[] args)
            throws RemoteException, SimRuntimeException;

    /**
     * schedules a methodCall immediately.
     * @param source the source of the event
     * @param target the target
     * @param method the method
     * @param args the arguments.
     * @throws RemoteException on network failure.
     * @throws SimRuntimeException whenever the event is scheduled in the past.
     */
    void scheduleEventNow(Object source, Object target, String method, Object[] args) throws RemoteException,
            SimRuntimeException;

    /**
     * Method setEventList sets the eventlist.
     * @param eventList the eventList for the simulator.
     * @throws RemoteException on network failure
     * @throws SimRuntimeException whenever simulator.isRunning()==true
     */
    void setEventList(EventListInterface<T> eventList) throws RemoteException, SimRuntimeException;
    
    /***********************************************************************************************************/
    /*********************************** EASY ACCESS INTERFACE EXTENSIONS **************************************/
    /***********************************************************************************************************/

    /** Easy access interface DEVSSimulatorInterface.Double */
    public static interface Double extends DEVSSimulatorInterface<java.lang.Double, java.lang.Double, SimTimeDouble>
    {
        // typed extension
    }

    /** Easy access interface DEVSSimulatorInterface.Float */
    public static interface Float extends DEVSSimulatorInterface<java.lang.Float, java.lang.Float, SimTimeFloat>
    {
        // typed extension
    }

    /** Easy access interface DEVSSimulatorInterface.Long */
    public static interface Long extends DEVSSimulatorInterface<java.lang.Long, java.lang.Long, SimTimeLong>
    {
        // typed extension
    }

    /** Easy access interface DEVSSimulatorInterface.DoubleUnit */
    public static interface DoubleUnit extends DEVSSimulatorInterface<UnitTimeDouble, UnitTimeDouble, SimTimeDoubleUnit>
    {
        // typed extension
    }

    /** Easy access interface DEVSSimulatorInterface.FloatUnit */
    public static interface FloatUnit extends DEVSSimulatorInterface<UnitTimeFloat, UnitTimeFloat, SimTimeFloatUnit>
    {
        // typed extension
    }

    /** Easy access interface DEVSSimulatorInterface.LongUnit */
    public static interface LongUnit extends DEVSSimulatorInterface<UnitTimeLong, UnitTimeLong, SimTimeLongUnit>
    {
        // typed extension
    }

    /** Easy access interface DEVSSimulatorInterface.CalendarDouble */
    public static interface CalendarDouble extends DEVSSimulatorInterface<Calendar, UnitTimeDouble, SimTimeCalendarDouble>
    {
        // typed extension
    }

    /** Easy access interface DEVSSimulatorInterface.CalendarFloat */
    public static interface CalendarFloat extends DEVSSimulatorInterface<Calendar, UnitTimeFloat, SimTimeCalendarFloat>
    {
        // typed extension
    }

    /** Easy access interface DEVSSimulatorInterface.CalendarLong */
    public static interface CalendarLong extends DEVSSimulatorInterface<Calendar, UnitTimeLong, SimTimeCalendarLong>
    {
        // typed extension
    }

}