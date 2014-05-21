/*
 * @(#)DEVSSimulatorInterface.java Aug 18, 2003 Copyright (c) 2002-2005 Delft
 * University of Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All
 * rights reserved. This software is proprietary information of Delft University
 * of Technology 
 */
package nl.tudelft.simulation.dsol.simulators;

import java.rmi.RemoteException;

import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.eventlists.EventListInterface;
import nl.tudelft.simulation.dsol.experiment.TimeUnitInterface;
import nl.tudelft.simulation.dsol.formalisms.eventscheduling.SimEventInterface;
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
 * @since 1.5
 */

public interface DEVSSimulatorInterface extends SimulatorInterface
{
    /** The EVENTLIST_CHANGED_EVENT is fired when the eventList is changed */
    EventType EVENTLIST_CHANGED_EVENT = new EventType("EVENTLIST_CHANGED_EVENT");

    /**
     * cancels an event from the event list.
     * @param event a simulation event to be canceled.
     * @return boolean the succes of the operation.
     * @throws RemoteException on network failure.
     */
    boolean cancelEvent(SimEventInterface event) throws RemoteException;

    /**
     * returns the eventlist of the simulator.
     * @return the eventlist.
     * @throws RemoteException on network failure
     */
    EventListInterface getEventList() throws RemoteException;

    /**
     * Method scheduleEvent schedules an event on the eventlist.
     * @param event a simulation event
     * @throws RemoteException on network failure
     * @throws SimRuntimeException whenever event is scheduled in past.
     */
    void scheduleEvent(SimEventInterface event) throws RemoteException, SimRuntimeException;

    /**
     * schedules a methodCall at a relative duration. The executionTime is thus
     * simulator.getSimulatorTime()+relativeDuration.
     * @param priority the priority compared to other events scheduled at the same time.
     * @param source the source of the event
     * @param target the target
     * @param method the method
     * @param args the arguments.
     * @param relativeDelay the relativeDelay in timeUnits of the simulator.
     * @throws RemoteException on network failure.
     * @throws SimRuntimeException whenever the event is scheduled in the past.
     */
    void scheduleEvent(double relativeDelay, short priority, Object source, Object target, String method, Object[] args)
            throws RemoteException, SimRuntimeException;

    /**
     * schedules a methodCall at a relative duration. The executionTime is thus
     * simulator.getSimulatorTime()+relativeDuration.
     * @param priority the priority compared to other events scheduled at the same time.
     * @param timeUnit the timeUnits of the delay
     * @param source the source of the event
     * @param target the target
     * @param method the method
     * @param args the arguments.
     * @param relativeDelay the relativeDelay in timeUnits of the simulator.
     * @throws RemoteException on network failure.
     * @throws SimRuntimeException whenever the event is scheduled in the past.
     */
    void scheduleEvent(double relativeDelay, TimeUnitInterface timeUnit, short priority, Object source, Object target,
            String method, Object[] args) throws RemoteException, SimRuntimeException;

    /**
     * schedules a methodCall at a relative duration. The executionTime is thus
     * simulator.getSimulatorTime()+relativeDuration.
     * @param source the source of the event
     * @param target the target
     * @param method the method
     * @param args the arguments.
     * @param relativeDelay the relativeDelay in timeUnits of the simulator.
     * @throws RemoteException on network failure.
     * @throws SimRuntimeException whenever the event is scheduled in the past.
     */
    void scheduleEvent(double relativeDelay, Object source, Object target, String method, Object[] args)
            throws RemoteException, SimRuntimeException;

    /**
     * schedules a methodCall at a relative duration. The executionTime is thus
     * simulator.getSimulatorTime()+relativeDuration.
     * @param timeUnit the timeUnits of the delay
     * @param source the source of the event
     * @param target the target
     * @param method the method
     * @param args the arguments.
     * @param relativeDelay the relativeDelay in timeUnits of the simulator.
     * @throws RemoteException on network failure.
     * @throws SimRuntimeException whenever the event is scheduled in the past.
     */
    void scheduleEvent(double relativeDelay, TimeUnitInterface timeUnit, Object source, Object target, String method,
            Object[] args) throws RemoteException, SimRuntimeException;

    /**
     * Method setEventList sets the eventlist.
     * @param eventList the eventList for the simulator.
     * @throws RemoteException on network failure
     * @throws SimRuntimeException whenever simulator.isRunning()==true
     */
    void setEventList(EventListInterface eventList) throws RemoteException, SimRuntimeException;
}