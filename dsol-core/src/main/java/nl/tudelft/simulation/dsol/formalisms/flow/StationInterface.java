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
import nl.tudelft.simulation.event.EventProducerInterface;
import nl.tudelft.simulation.event.EventType;

/**
 * A station is an object which accepts other objects and is linked to a destination.
 * <p>
 * Copyright (c) 2002-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. See for project information <a href="https://simulation.tudelft.nl/" target="_blank">
 * https://simulation.tudelft.nl</a>. The DSOL project is distributed under a three-clause BSD-style license, which can
 * be found at <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @param <A> the absolute storage type for the simulation time, e.g. Calendar, Duration, or Double.
 * @param <R> the relative type for time storage, e.g. Long for the Calendar. For most non-calendar types, the absolute
 *            and relative types are the same.
 * @param <T> the extended type itself to be able to implement a comparator on the simulation time.
 * @since 1.5
 */

public interface StationInterface<A extends Comparable<A>, R extends Number & Comparable<R>, T extends SimTime<A, R, T>>
        extends EventProducerInterface
{
    /** RECEIVE_EVENT is fired whenever an entity enters the station. */
    EventType RECEIVE_EVENT = new EventType("RECEIVE_EVENT");

    /** RECEIVE_EVENT is fired whenever an entity leaves the station. */
    EventType RELEASE_EVENT = new EventType("RELEASE_EVENT");

    /**
     * Method getDestination.
     * @return StationInterface is the destination of this station
     */
    StationInterface<A, R, T> getDestination();

    /**
     * receives an object is invoked whenever an entity arrives.
     * @param object Object; is the entity
     */
    void receiveObject(Object object);

    /**
     * sets the destination of this object.
     * @param destination StationInterface&lt;A,R,T&gt;; defines the next station in the model
     */
    void setDestination(StationInterface<A, R, T> destination);

    /***********************************************************************************************************/
    /*********************************** EASY ACCESS INTERFACE EXTENSIONS **************************************/
    /***********************************************************************************************************/

    /** Easy access interface StationInterface.Double. */
    public interface TimeDouble extends StationInterface<Double, Double, SimTimeDouble>
    {
        // typed extension
    }

    /** Easy access interface StationInterface.Float. */
    public interface TimeFloat extends StationInterface<Float, Float, SimTimeFloat>
    {
        // typed extension
    }

    /** Easy access interface StationInterface.Long. */
    public interface TimeLong extends StationInterface<Long, Long, SimTimeLong>
    {
        // typed extension
    }

    /** Easy access interface StationInterface.DoubleUnit. */
    public interface TimeDoubleUnit extends StationInterface<Time, Duration, SimTimeDoubleUnit>
    {
        // typed extension
    }

    /** Easy access interface StationInterface.FloatUnit. */
    public interface TimeFloatUnit extends StationInterface<FloatTime, FloatDuration, SimTimeFloatUnit>
    {
        // typed extension
    }

    /** Easy access interface StationInterface.CalendarDouble. */
    public interface CalendarDouble extends StationInterface<Calendar, Duration, SimTimeCalendarDouble>
    {
        // typed extension
    }

    /** Easy access interface StationInterface.CalendarFloat. */
    public interface CalendarFloat extends StationInterface<Calendar, FloatDuration, SimTimeCalendarFloat>
    {
        // typed extension
    }

    /** Easy access interface StationInterface.CalendarLong. */
    public interface CalendarLong extends StationInterface<Calendar, Long, SimTimeCalendarLong>
    {
        // typed extension
    }

}
