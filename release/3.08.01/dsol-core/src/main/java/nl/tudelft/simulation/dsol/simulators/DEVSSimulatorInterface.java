package nl.tudelft.simulation.dsol.simulators;

import java.io.Serializable;

import org.djunits.value.vdouble.scalar.Duration;
import org.djunits.value.vdouble.scalar.Time;
import org.djunits.value.vfloat.scalar.FloatDuration;
import org.djunits.value.vfloat.scalar.FloatTime;
import org.djutils.event.EventType;
import org.djutils.metadata.MetaData;

import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.eventlists.EventListInterface;
import nl.tudelft.simulation.dsol.formalisms.eventscheduling.Executable;
import nl.tudelft.simulation.dsol.formalisms.eventscheduling.SimEventInterface;
import nl.tudelft.simulation.dsol.simtime.SimTime;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simtime.SimTimeDoubleUnit;
import nl.tudelft.simulation.dsol.simtime.SimTimeFloat;
import nl.tudelft.simulation.dsol.simtime.SimTimeFloatUnit;
import nl.tudelft.simulation.dsol.simtime.SimTimeLong;

/**
 * The DEVS defines the interface of the DEVS simulator. DEVS stands for the Discrete Event System Specification. More
 * information on Discrete Event Simulation can be found in "Theory of Modeling and Simulation" by Bernard Zeigler et.al.
 * <p>
 * Copyright (c) 2002-2022 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @param <A> the absolute storage type for the simulation time, e.g. Time, Float, or Double.
 * @param <R> the relative type for time storage, e.g. Duration for absolute Time. For most non-unit types, the absolute and
 *            relative types are the same.
 * @param <T> the extended type itself to be able to implement a comparator on the simulation time.
 * @since 1.5
 */
public interface DEVSSimulatorInterface<A extends Comparable<A> & Serializable, R extends Number & Comparable<R>,
        T extends SimTime<A, R, T>> extends SimulatorInterface<A, R, T>
{
    /** The EVENTLIST_CHANGED_EVENT is fired when the eventList is changed. */
    EventType EVENTLIST_CHANGED_EVENT = new EventType(new MetaData("EVENTLIST_CHANGED_EVENT", "Eventlist changed"));

    /**
     * cancels an event from the event list.
     * @param event SimEventInterface&lt;T&gt;; a simulation event to be canceled.
     * @return boolean the succes of the operation.
     */
    boolean cancelEvent(SimEventInterface<T> event);

    /**
     * returns the eventlist of the simulator.
     * @return the eventlist.
     */
    EventListInterface<T> getEventList();

    /**
     * Method scheduleEvent schedules an event on the eventlist.
     * @param event SimEventInterface&lt;T&gt;; a simulation event
     * @return the simulation event so it can be cancelled later
     * @throws SimRuntimeException whenever event is scheduled in past.
     */
    SimEventInterface<T> scheduleEvent(SimEventInterface<T> event) throws SimRuntimeException;

    /**
     * schedules a methodCall at a relative duration. The executionTime is thus simulator.getSimulatorTime()+relativeDuration.
     * @param relativeDelay R; the relativeDelay in timeUnits of the simulator.
     * @param priority short; the priority compared to other events scheduled at the same time.
     * @param source Object; the source of the event
     * @param target Object; the target
     * @param method String; the method
     * @param args Object[]; the arguments.
     * @return the simulation event so it can be cancelled later
     * @throws SimRuntimeException whenever the event is scheduled in the past.
     */
    SimEventInterface<T> scheduleEventRel(R relativeDelay, short priority, Object source, Object target, String method,
            Object[] args) throws SimRuntimeException;

    /**
     * schedules a methodCall at a relative duration. The executionTime is thus simulator.getSimulatorTime()+relativeDuration.
     * @param relativeDelay R; the relativeDelay in timeUnits of the simulator.
     * @param source Object; the source of the event
     * @param target Object; the target
     * @param method String; the method
     * @param args Object[]; the arguments.
     * @return the simulation event so it can be cancelled later
     * @throws SimRuntimeException whenever the event is scheduled in the past.
     */
    SimEventInterface<T> scheduleEventRel(R relativeDelay, Object source, Object target, String method, Object[] args)
            throws SimRuntimeException;

    /**
     * schedules a methodCall at an absolute time.
     * @param absoluteTime T; the exact time to schedule the method on the simulator.
     * @param priority short; the priority compared to other events scheduled at the same time.
     * @param source Object; the source of the event
     * @param target Object; the target
     * @param method String; the method
     * @param args Object[]; the arguments.
     * @return the simulation event so it can be cancelled later
     * @throws SimRuntimeException whenever the event is scheduled in the past.
     */
    SimEventInterface<T> scheduleEventAbs(T absoluteTime, short priority, Object source, Object target, String method,
            Object[] args) throws SimRuntimeException;

    /**
     * schedules a methodCall at an absolute time.
     * @param absoluteTime A; the exact time to schedule the method on the simulator.
     * @param source Object; the source of the event
     * @param target Object; the target
     * @param method String; the method
     * @param args Object[]; the arguments.
     * @return the simulation event so it can be cancelled later
     * @throws SimRuntimeException whenever the event is scheduled in the past.
     */
    SimEventInterface<T> scheduleEventAbs(A absoluteTime, Object source, Object target, String method, Object[] args)
            throws SimRuntimeException;

    /**
     * schedules a methodCall at an absolute time.
     * @param absoluteTime A; the exact time to schedule the method on the simulator.
     * @param priority short; the priority compared to other events scheduled at the same time.
     * @param source Object; the source of the event
     * @param target Object; the target
     * @param method String; the method
     * @param args Object[]; the arguments.
     * @return the simulation event so it can be cancelled later
     * @throws SimRuntimeException whenever the event is scheduled in the past.
     */
    SimEventInterface<T> scheduleEventAbs(A absoluteTime, short priority, Object source, Object target, String method,
            Object[] args) throws SimRuntimeException;

    /**
     * schedules a methodCall at an absolute time.
     * @param absoluteTime T; the exact time to schedule the method on the simulator.
     * @param source Object; the source of the event
     * @param target Object; the target
     * @param method String; the method
     * @param args Object[]; the arguments.
     * @return the simulation event so it can be cancelled later
     * @throws SimRuntimeException whenever the event is scheduled in the past.
     */
    SimEventInterface<T> scheduleEventAbs(T absoluteTime, Object source, Object target, String method, Object[] args)
            throws SimRuntimeException;

    /**
     * schedules a methodCall immediately.
     * @param priority short; the priority compared to other events scheduled at the same time.
     * @param source Object; the source of the event
     * @param target Object; the target
     * @param method String; the method
     * @param args Object[]; the arguments.
     * @return the simulation event so it can be cancelled later
     * @throws SimRuntimeException whenever the event is scheduled in the past.
     */
    SimEventInterface<T> scheduleEventNow(short priority, Object source, Object target, String method, Object[] args)
            throws SimRuntimeException;

    /**
     * schedules a methodCall immediately.
     * @param source Object; the source of the event
     * @param target Object; the target
     * @param method String; the method
     * @param args Object[]; the arguments.
     * @return the simulation event so it can be cancelled later
     * @throws SimRuntimeException whenever the event is scheduled in the past.
     */
    SimEventInterface<T> scheduleEventNow(Object source, Object target, String method, Object[] args)
            throws SimRuntimeException;

    /**
     * schedules a lambda expression at a relative duration. The executionTime is thus
     * simulator.getSimulatorTime()+relativeDuration.
     * @param relativeDelay R; the relativeDelay in timeUnits of the simulator.
     * @param priority short; the priority compared to other events scheduled at the same time.
     * @param executable Executable; the lambda expression to execute
     * @return the simulation event so it can be cancelled later
     * @throws SimRuntimeException whenever the event is scheduled in the past.
     */
    SimEventInterface<T> scheduleEventRel(R relativeDelay, short priority, Executable executable) throws SimRuntimeException;

    /**
     * schedules a lambda expression at a relative duration. The executionTime is thus
     * simulator.getSimulatorTime()+relativeDuration.
     * @param relativeDelay R; the relativeDelay in timeUnits of the simulator.
     * @param executable Executable; the lambda expression to execute
     * @return the simulation event so it can be cancelled later
     * @throws SimRuntimeException whenever the event is scheduled in the past.
     */
    SimEventInterface<T> scheduleEventRel(R relativeDelay, Executable executable) throws SimRuntimeException;

    /**
     * schedules a lambda expression at an absolute time.
     * @param absoluteTime T; the exact time to schedule the method on the simulator.
     * @param priority short; the priority compared to other events scheduled at the same time.
     * @param executable Executable; the lambda expression to execute
     * @return the simulation event so it can be cancelled later
     * @throws SimRuntimeException whenever the event is scheduled in the past.
     */
    SimEventInterface<T> scheduleEventAbs(T absoluteTime, short priority, Executable executable) throws SimRuntimeException;

    /**
     * schedules a lambda expression at an absolute time.
     * @param absoluteTime A; the exact time to schedule the method on the simulator.
     * @param executable Executable; the lambda expression to execute
     * @return the simulation event so it can be cancelled later
     * @throws SimRuntimeException whenever the event is scheduled in the past.
     */
    SimEventInterface<T> scheduleEventAbs(A absoluteTime, Executable executable) throws SimRuntimeException;

    /**
     * schedules a lambda expression at an absolute time.
     * @param absoluteTime A; the exact time to schedule the method on the simulator.
     * @param priority short; the priority compared to other events scheduled at the same time.
     * @param executable Executable; the lambda expression to execute
     * @return the simulation event so it can be cancelled later
     * @throws SimRuntimeException whenever the event is scheduled in the past.
     */
    SimEventInterface<T> scheduleEventAbs(A absoluteTime, short priority, Executable executable) throws SimRuntimeException;

    /**
     * schedules a lambda expression at an absolute time.
     * @param absoluteTime T; the exact time to schedule the method on the simulator.
     * @param executable Executable; the lambda expression to execute
     * @return the simulation event so it can be cancelled later
     * @throws SimRuntimeException whenever the event is scheduled in the past.
     */
    SimEventInterface<T> scheduleEventAbs(T absoluteTime, Executable executable) throws SimRuntimeException;

    /**
     * schedules a lambda expression immediately.
     * @param priority short; the priority compared to other events scheduled at the same time.
     * @param executable Executable; the lambda expression to execute
     * @return the simulation event so it can be cancelled later
     * @throws SimRuntimeException whenever the event is scheduled in the past.
     */
    SimEventInterface<T> scheduleEventNow(short priority, Executable executable) throws SimRuntimeException;

    /**
     * schedules a lambda expression immediately.
     * @param executable Executable; the lambda expression to execute
     * @return the simulation event so it can be cancelled later
     * @throws SimRuntimeException whenever the event is scheduled in the past.
     */
    SimEventInterface<T> scheduleEventNow(Executable executable) throws SimRuntimeException;

    /**
     * Method setEventList sets the eventlist.
     * @param eventList EventListInterface&lt;T&gt;; the eventList for the simulator.
     * @throws SimRuntimeException whenever simulator.isRunning()==true
     */
    void setEventList(EventListInterface<T> eventList) throws SimRuntimeException;

    /** @return pauseOnError whether we pause on an error or not. */
    boolean isPauseOnError();

    /**
     * Set the boolean whether we pause on an error or not.
     * @param pauseOnError boolean; set true or false.
     */
    void setPauseOnError(boolean pauseOnError);

    /***********************************************************************************************************/
    /*********************************** EASY ACCESS INTERFACE EXTENSIONS **************************************/
    /***********************************************************************************************************/

    /** Easy access interface DEVSSimulatorInterface.TimeDouble. */
    public interface TimeDouble extends DEVSSimulatorInterface<Double, Double, SimTimeDouble>, SimulatorInterface.TimeDouble
    {
        // typed extension
    }

    /** Easy access interface DEVSSimulatorInterface.TimeFloat. */
    public interface TimeFloat extends DEVSSimulatorInterface<Float, Float, SimTimeFloat>, SimulatorInterface.TimeFloat
    {
        // typed extension
    }

    /** Easy access interface DEVSSimulatorInterface.TimeLong. */
    public interface TimeLong extends DEVSSimulatorInterface<Long, Long, SimTimeLong>, SimulatorInterface.TimeLong
    {
        // typed extension
    }

    /** Easy access interface DEVSSimulatorInterface.TimeDoubleUnit. */
    public interface TimeDoubleUnit
            extends DEVSSimulatorInterface<Time, Duration, SimTimeDoubleUnit>, SimulatorInterface.TimeDoubleUnit
    {
        // typed extension
    }

    /** Easy access interface DEVSSimulatorInterface.TimeFloatUnit. */
    public interface TimeFloatUnit
            extends DEVSSimulatorInterface<FloatTime, FloatDuration, SimTimeFloatUnit>, SimulatorInterface.TimeFloatUnit
    {
        // typed extension
    }

}
