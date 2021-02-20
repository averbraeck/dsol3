package nl.tudelft.simulation.dsol.formalisms.eventscheduling;

import java.io.Serializable;

import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.simtime.SimTime;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simtime.SimTimeDoubleUnit;
import nl.tudelft.simulation.dsol.simtime.SimTimeFloat;
import nl.tudelft.simulation.dsol.simtime.SimTimeFloatUnit;
import nl.tudelft.simulation.dsol.simtime.SimTimeLong;

/**
 * A SimEventInterface embodies the envelope in which the scheduled method invocation information is stored.
 * <p>
 * Copyright (c) 2002-2021 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @param <T> the type of simulation time, e.g. SimTimeCalendarLong or SimTimeDouble or SimTimeDoubleUnit.
 * @since 1.5
 */
public interface SimEventInterface<T extends SimTime<?, ?, T>> extends Serializable
{
    /** MAX_PRIORITY is a constant reflecting the maximum priority. */
    short MAX_PRIORITY = 10;

    /** NORMAL_PRIORITY is a constant reflecting the normal priority. */
    short NORMAL_PRIORITY = 5;

    /** MIN_PRIORITY is a constant reflecting the minimal priority. */
    short MIN_PRIORITY = 1;

    /**
     * executes the simEvent.
     * @throws SimRuntimeException on execution failure
     */
    void execute() throws SimRuntimeException;

    /**
     * @return the scheduled execution time of a simulation event.
     */
    T getAbsoluteExecutionTime();

    /**
     * @return The priority of a simulation event. The priorities are programmed according to the Java thread priority. Use 10
     *         (MAX_PRIORITY), 9, .. , 5 (NORMAL_PRIORITY), 1(MIN_PRIORITY)
     */
    short getPriority();

    /***********************************************************************************************************/
    /*********************************** EASY ACCESS INTERFACE EXTENSIONS **************************************/
    /***********************************************************************************************************/

    /** Easy access class SimEvent.TimeDouble. */
    public interface TimeDouble extends SimEventInterface<SimTimeDouble>
    {
        // no extra methods
    }

    /** Easy access class SimEvent.TimeFloat. */
    public interface TimeFloat extends SimEventInterface<SimTimeFloat>
    {
        // no extra methods
    }

    /** Easy access class SimEvent.TimeLong. */
    public interface TimeLong extends SimEventInterface<SimTimeLong>
    {
        // no extra methods
    }

    /** Easy access class SimEvent.TimeDoubleUnit. */
    public interface TimeDoubleUnit extends SimEventInterface<SimTimeDoubleUnit>
    {
        // no extra methods
    }

    /** Easy access class SimEvent.TimeFloatUnit. */
    public interface TimeFloatUnit extends SimEventInterface<SimTimeFloatUnit>
    {
        // no extra methods
    }

}
