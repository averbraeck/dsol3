package nl.tudelft.simulation.dsol.formalisms.flow;

import java.io.Serializable;

import org.djunits.value.vdouble.scalar.Duration;
import org.djunits.value.vdouble.scalar.Time;
import org.djunits.value.vfloat.scalar.FloatDuration;
import org.djunits.value.vfloat.scalar.FloatTime;
import org.djutils.event.EventProducerInterface;
import org.djutils.event.TimedEventType;
import org.djutils.metadata.MetaData;
import org.djutils.metadata.ObjectDescriptor;

import nl.tudelft.simulation.dsol.simtime.SimTime;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simtime.SimTimeDoubleUnit;
import nl.tudelft.simulation.dsol.simtime.SimTimeFloat;
import nl.tudelft.simulation.dsol.simtime.SimTimeFloatUnit;
import nl.tudelft.simulation.dsol.simtime.SimTimeLong;

/**
 * A station is an object which accepts other objects and is linked to a destination.
 * <p>
 * Copyright (c) 2002-2021 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @param <A> the absolute storage type for the simulation time, e.g. Time, Float, or Double.
 * @param <R> the relative type for time storage, e.g. Duration for absolute Time. For most non-unit types, the absolute and
 *            relative types are the same.
 * @param <T> the extended type itself to be able to implement a comparator on the simulation time.
 * @since 1.5
 */

public interface StationInterface<A extends Comparable<A> & Serializable, R extends Number & Comparable<R>,
        T extends SimTime<A, R, T>> extends EventProducerInterface
{
    /** RECEIVE_EVENT is fired whenever an entity enters the station. */
    TimedEventType RECEIVE_EVENT = new TimedEventType(new MetaData("RECEIVE_EVENT", "Object received",
            new ObjectDescriptor("receivedObject", "received object", Serializable.class)));

    /** RECEIVE_EVENT is fired whenever an entity leaves the station. */
    TimedEventType RELEASE_EVENT = new TimedEventType(new MetaData("RELEASE_EVENT", "Object released",
            new ObjectDescriptor("releasedObject", "released object", Serializable.class)));

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

}
