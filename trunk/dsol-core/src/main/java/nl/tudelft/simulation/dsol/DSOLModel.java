package nl.tudelft.simulation.dsol;

import java.io.Serializable;
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
import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;

/**
 * The model interface defines the model object. Since version 2.1.0 of DSOL, the ModelInterface now knows its simulator
 * and can return it to anyone interested. Through the Simulator, the Replication can be requested and through that the
 * Experiment and the Treatment under which the simulation is running.
 * <p>
 * Copyright (c) 2003-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
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
public interface DSOLModel<A extends Comparable<A>, R extends Number & Comparable<R>, T extends SimTime<A, R, T>>
        extends Serializable
{
    /**
     * construct a model on a simulator.
     * @param simulator SimulatorInterface&lt;A,R,T&gt;; is the simulator
     * @throws SimRuntimeException on model failure
     */
    void constructModel(SimulatorInterface<A, R, T> simulator) throws SimRuntimeException;

    /**
     * @return the simulator for the model
     */
    SimulatorInterface<A, R, T> getSimulator();

    /***********************************************************************************************************/
    /*********************************** EASY ACCESS INTERFACE EXTENSIONS **************************************/
    /***********************************************************************************************************/

    /** Easy access interface ModelInterface.TimeDouble. */
    public interface TimeDouble extends DSOLModel<Double, Double, SimTimeDouble>
    {
        // typed extension
    }

    /** Easy access interface ModelInterface.TimeFloat. */
    public interface TimeFloat extends DSOLModel<Float, Float, SimTimeFloat>
    {
        // typed extension
    }

    /** Easy access interface ModelInterface.TimeLong. */
    public interface TimeLong extends DSOLModel<Long, Long, SimTimeLong>
    {
        // typed extension
    }

    /** Easy access interface ModelInterface.TimeDoubleUnit. */
    public interface TimeDoubleUnit extends DSOLModel<Time, Duration, SimTimeDoubleUnit>
    {
        // typed extension
    }

    /** Easy access interface ModelInterface.TimeFloatUnit. */
    public interface TimeFloatUnit extends DSOLModel<FloatTime, FloatDuration, SimTimeFloatUnit>
    {
        // typed extension
    }

    /** Easy access interface ModelInterface.TimeCalendarDouble. */
    public interface CalendarDouble extends DSOLModel<Calendar, Duration, SimTimeCalendarDouble>
    {
        // typed extension
    }

    /** Easy access interface ModelInterface.TimeCalendarFloat. */
    public interface CalendarFloat extends DSOLModel<Calendar, FloatDuration, SimTimeCalendarFloat>
    {
        // typed extension
    }

    /** Easy access interface ModelInterface.TimeCalendarLong. */
    public interface CalendarLong extends DSOLModel<Calendar, Long, SimTimeCalendarLong>
    {
        // typed extension
    }
}
