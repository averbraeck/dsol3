package nl.tudelft.simulation.dsol.experiment;

import java.io.Serializable;
import java.util.Map;

import org.djunits.value.vdouble.scalar.Duration;
import org.djunits.value.vdouble.scalar.Time;
import org.djunits.value.vfloat.scalar.FloatDuration;
import org.djunits.value.vfloat.scalar.FloatTime;
import org.djutils.event.TimedEventType;
import org.djutils.metadata.MetaData;

import nl.tudelft.simulation.dsol.simtime.SimTime;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simtime.SimTimeDoubleUnit;
import nl.tudelft.simulation.dsol.simtime.SimTimeFloat;
import nl.tudelft.simulation.dsol.simtime.SimTimeFloatUnit;
import nl.tudelft.simulation.dsol.simtime.SimTimeLong;
import nl.tudelft.simulation.jstats.streams.StreamInterface;
import nl.tudelft.simulation.naming.context.ContextInterface;

/**
 * The interface for a replication. Several types of replications exist, such as the SingleReplication and the Replication.
 * <p>
 * Copyright (c) 2002-2021 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @param <A> the absolute storage type for the simulation time, e.g. Time, Float, or Double.
 * @param <R> the relative type for time storage, e.g. Duration for absolute Time. For most non-unit types, the absolute and
 *            relative types are the same.
 * @param <T> the extended type itself to be able to implement a comparator on the simulation time.
 */
public interface ReplicationInterface<A extends Comparable<A> & Serializable, R extends Number & Comparable<R>,
        T extends SimTime<A, R, T>> extends Serializable
{
    /** START_REPLICATION_EVENT is fired when a replication is started. */
    TimedEventType START_REPLICATION_EVENT = new TimedEventType(new MetaData("START_REPLICATION_EVENT", "Replication started"));

    /** END_REPLICATION_EVENT is fired when a replication is finished. */
    TimedEventType END_REPLICATION_EVENT = new TimedEventType(new MetaData("END_REPLICATION_EVENT", "Replication ended"));

    /** WARMUP_EVENT is fired when the warmup period is over, and statistics have to be reset. */
    TimedEventType WARMUP_EVENT = new TimedEventType(new MetaData("WARMUP_EVENT", "warmup time"));

    /**
     * Return the id of this replication.
     * @return String; the id of this replication
     */
    String getId();

    /**
     * Sets the description of this replication.
     * @param description String; the description of this replication
     */
    void setDescription(String description);

    /**
     * Return the description of this replication.
     * @return String; the description of this replication
     */
    String getDescription();

    /**
     * Return the streams to use in this replication, mapping stream ids to streams.
     * @return Map&lt;String, StreamInterface&gt;; the streams to use in this replication
     */
    Map<String, StreamInterface> getStreams();

    /**
     * Return a specific stream for this replication, based on a stream id.
     * @param streamId String; the id of the stream to be retrieved
     * @return StreamInterface the stream
     */
    StreamInterface getStream(String streamId);

    /**
     * Reset the streams to their original seed value.
     */
    void reset();

    /**
     * Sets the streams for this replication.
     * @param streams Map&lt;String,StreamInterface&gt;; the map of streams and their ids
     */
    void setStreams(Map<String, StreamInterface> streams);

    /**
     * Return the specific context for this replication, e.g. to store statistics and animation uniquely beloging to this
     * replication.
     * @return ContextInterface; the specific context for this replication
     */
    ContextInterface getContext();

    /**
     * Return the run length of this replication in relative units.
     * @return R; the runLength.
     */
    default R getRunLength()
    {
        return getEndSimTime().diff(getStartSimTime());
    }

    /**
     * Return the warmup period of this replication in relative units.
     * @return R; the warmup period.
     */
    default R getWarmupPeriod()
    {
        return getWarmupSimTime().diff(getStartSimTime());
    }

    /**
     * Return the absolute start time of the simulation.
     * @return A; the absolute start time of the simulation
     */
    default A getStartTime()
    {
        return getStartSimTime().get();
    }

    /**
     * Return the absolute end time of the simulation.
     * @return A; the absolute end time of the simulation
     */
    default A getEndTime()
    {
        return getEndSimTime().get();
    }

    /**
     * Return the absolute moment when the warmup event will take place.
     * @return A; the absolute moment when the warmup event will take place
     */
    default A getWarmupTime()
    {
        return getWarmupSimTime().get();
    }

    /**
     * Return the absolute start time of the simulation as a SimTime object.
     * @return T; the absolute start time of the simulation as a SimTime object
     */
    T getStartSimTime();

    /**
     * Return the absolute end time of the simulation as a SimTime object.
     * @return T; the absolute end time of the simulation as a SimTime object
     */
    T getEndSimTime();

    /**
     * Return the absolute warmup time of the simulation as a SimTime object.
     * @return T; the absolute warmup time of the simulation as a SimTime object
     */
    T getWarmupSimTime();

    /***********************************************************************************************************/
    /*********************************** EASY ACCESS INTERFACE EXTENSIONS **************************************/
    /***********************************************************************************************************/

    /**
     * Easy access interface ReplicationInterface.TimeDouble.
     */
    public interface TimeDouble extends ReplicationInterface<Double, Double, SimTimeDouble>
    {
        // tagging interface
    }

    /**
     * Easy access interface ReplicationInterface.TimeFloat.
     */
    public interface TimeFloat extends ReplicationInterface<Float, Float, SimTimeFloat>
    {
        // tagging interface
    }

    /**
     * Easy access interface ReplicationInterface.TimeLong.
     */
    public interface TimeLong extends ReplicationInterface<Long, Long, SimTimeLong>
    {
        // tagging interface
    }

    /**
     * Easy access interface ReplicationInterface.TimeDoubleUnit.
     */
    public interface TimeDoubleUnit extends ReplicationInterface<Time, Duration, SimTimeDoubleUnit>
    {
        // tagging interface
    }

    /**
     * Easy access interface ReplicationInterface.TimeDoubleUnit.
     */
    public interface TimeFloatUnit extends ReplicationInterface<FloatTime, FloatDuration, SimTimeFloatUnit>
    {
        // tagging interface
    }

}
