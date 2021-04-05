package nl.tudelft.simulation.dsol.experiment;

import java.io.Serializable;

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

/**
 * The interface for a replication. Several types of replications exist, such as the SingleReplication and the
 * ExperimentReplication.
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
@SuppressWarnings("checkstyle:interfaceistype")
public interface ReplicationInterface<A extends Comparable<A> & Serializable, R extends Number & Comparable<R>,
        T extends SimTime<A, R, T>> extends RunControlInterface<A, R, T>
{
    /** START_REPLICATION_EVENT is fired when a replication is started. */
    TimedEventType START_REPLICATION_EVENT = new TimedEventType(new MetaData("START_REPLICATION_EVENT", "Replication started"));

    /** END_REPLICATION_EVENT is fired when a replication is finished. */
    TimedEventType END_REPLICATION_EVENT = new TimedEventType(new MetaData("END_REPLICATION_EVENT", "Replication ended"));

    /** WARMUP_EVENT is fired when the warmup period is over, and statistics have to be reset. */
    TimedEventType WARMUP_EVENT = new TimedEventType(new MetaData("WARMUP_EVENT", "warmup time"));

    /* ********************************************************************************************************* */
    /* ********************************** EASY ACCESS INTERFACE EXTENSIONS ************************************* */
    /* ********************************************************************************************************* */

    /**
     * Easy access interface ReplicationInterface.TimeDouble.
     */
    public interface TimeDouble extends ReplicationInterface<Double, Double, SimTimeDouble>, RunControlInterface.TimeDouble
    {
        // tagging interface
    }

    /**
     * Easy access interface ReplicationInterface.TimeFloat.
     */
    public interface TimeFloat extends ReplicationInterface<Float, Float, SimTimeFloat>, RunControlInterface.TimeFloat
    {
        // tagging interface
    }

    /**
     * Easy access interface ReplicationInterface.TimeLong.
     */
    public interface TimeLong extends ReplicationInterface<Long, Long, SimTimeLong>, RunControlInterface.TimeLong
    {
        // tagging interface
    }

    /**
     * Easy access interface ReplicationInterface.TimeDoubleUnit.
     */
    public interface TimeDoubleUnit
            extends ReplicationInterface<Time, Duration, SimTimeDoubleUnit>, RunControlInterface.TimeDoubleUnit
    {
        // tagging interface
    }

    /**
     * Easy access interface ReplicationInterface.TimeDoubleUnit.
     */
    public interface TimeFloatUnit
            extends ReplicationInterface<FloatTime, FloatDuration, SimTimeFloatUnit>, RunControlInterface.TimeFloatUnit
    {
        // tagging interface
    }

}
