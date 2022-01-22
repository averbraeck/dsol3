package nl.tudelft.simulation.dsol.simulators;

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
 * The DEVSDESS simulator embodies both the continuous and the discrete formalism. This simulator takes pre-defined time steps
 * in between it loops over its eventlist. A better name for this formalism would therefore be the DEVSinDESS formalism. More
 * information on Modeling and Simulation can be found in Theory of Modeling and Simulation by Bernard Zeigler et.al.
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
@SuppressWarnings("linelength")
public interface DEVDESSSimulatorInterface<A extends Comparable<A> & Serializable, R extends Number & Comparable<R>,
        T extends SimTime<A, R, T>> extends DEVSSimulatorInterface<A, R, T>, DESSSimulatorInterface<A, R, T>
{
    // This interface combines the DESS and DEVS interfaces and does not add any operations.

    /***********************************************************************************************************/
    /*********************************** EASY ACCESS INTERFACE EXTENSIONS **************************************/
    /***********************************************************************************************************/

    /** Easy access interface DEVDESSSimulatorInterface.TimeDouble. */
    public interface TimeDouble extends DEVDESSSimulatorInterface<java.lang.Double, java.lang.Double, SimTimeDouble>,
            DEVSSimulatorInterface.TimeDouble, DESSSimulatorInterface.TimeDouble
    {
        // typed extension
    }

    /** Easy access interface DEVDESSSimulatorInterface.TimeFloat. */
    public interface TimeFloat extends DEVDESSSimulatorInterface<java.lang.Float, java.lang.Float, SimTimeFloat>,
            DEVSSimulatorInterface.TimeFloat, DESSSimulatorInterface.TimeFloat
    {
        // typed extension
    }

    /** Easy access interface DEVDESSSimulatorInterface.TimeLong. */
    public interface TimeLong extends DEVDESSSimulatorInterface<java.lang.Long, java.lang.Long, SimTimeLong>,
            DEVSSimulatorInterface.TimeLong, DESSSimulatorInterface.TimeLong
    {
        // typed extension
    }

    /** Easy access interface DEVDESSSimulatorInterface.TimeDoubleUnit. */
    public interface TimeDoubleUnit extends DEVDESSSimulatorInterface<Time, Duration, SimTimeDoubleUnit>,
            DEVSSimulatorInterface.TimeDoubleUnit, DESSSimulatorInterface.TimeDoubleUnit
    {
        // typed extension
    }

    /** Easy access interface DEVDESSSimulatorInterface.TimeFloatUnit. */
    public interface TimeFloatUnit extends DEVDESSSimulatorInterface<FloatTime, FloatDuration, SimTimeFloatUnit>,
            DEVSSimulatorInterface.TimeFloatUnit, DESSSimulatorInterface.TimeFloatUnit
    {
        // typed extension
    }

}
