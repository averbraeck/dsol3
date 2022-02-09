package nl.tudelft.simulation.dsol.experiment;

import java.io.Serializable;

import org.djunits.value.vdouble.scalar.Duration;
import org.djunits.value.vdouble.scalar.Time;
import org.djunits.value.vfloat.scalar.FloatDuration;
import org.djunits.value.vfloat.scalar.FloatTime;

import nl.tudelft.simulation.dsol.simtime.SimTime;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simtime.SimTimeDoubleUnit;
import nl.tudelft.simulation.dsol.simtime.SimTimeFloat;
import nl.tudelft.simulation.dsol.simtime.SimTimeFloatUnit;
import nl.tudelft.simulation.dsol.simtime.SimTimeLong;

/**
 * The interface for a run control as used in the Experiment and in the Replication.
 * <p>
 * Copyright (c) 2021-2022 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @param <A> the absolute storage type for the simulation time, e.g. Time, Float, or Double.
 * @param <R> the relative type for time storage, e.g. Duration for absolute Time. For most non-unit types, the absolute and
 *            relative types are the same.
 * @param <T> the simulation time type to be able to implement a comparator on the simulation time.
 */
public interface RunControlInterface<A extends Comparable<A> & Serializable, R extends Number & Comparable<R>,
        T extends SimTime<A, R, T>> extends Serializable
{
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

    /* ********************************************************************************************************* */
    /* ********************************** EASY ACCESS INTERFACE EXTENSIONS ************************************* */
    /* ********************************************************************************************************* */

    /**
     * Easy access interface ReplicationInterface.TimeDouble.
     */
    public interface TimeDouble extends RunControlInterface<Double, Double, SimTimeDouble>
    {
        // tagging interface
    }

    /**
     * Easy access interface ReplicationInterface.TimeFloat.
     */
    public interface TimeFloat extends RunControlInterface<Float, Float, SimTimeFloat>
    {
        // tagging interface
    }

    /**
     * Easy access interface ReplicationInterface.TimeLong.
     */
    public interface TimeLong extends RunControlInterface<Long, Long, SimTimeLong>
    {
        // tagging interface
    }

    /**
     * Easy access interface ReplicationInterface.TimeDoubleUnit.
     */
    public interface TimeDoubleUnit extends RunControlInterface<Time, Duration, SimTimeDoubleUnit>
    {
        // tagging interface
    }

    /**
     * Easy access interface ReplicationInterface.TimeDoubleUnit.
     */
    public interface TimeFloatUnit extends RunControlInterface<FloatTime, FloatDuration, SimTimeFloatUnit>
    {
        // tagging interface
    }

}
