package nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS;

import java.io.Serializable;
import java.rmi.RemoteException;

import org.djunits.value.vdouble.scalar.Duration;
import org.djunits.value.vdouble.scalar.Time;
import org.djunits.value.vfloat.scalar.FloatDuration;
import org.djunits.value.vfloat.scalar.FloatTime;

import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.simtime.SimTime;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simtime.SimTimeDoubleUnit;
import nl.tudelft.simulation.dsol.simtime.SimTimeFloat;
import nl.tudelft.simulation.dsol.simtime.SimTimeFloatUnit;
import nl.tudelft.simulation.dsol.simtime.SimTimeLong;

/**
 * OutputPortInterface class. Describes the contract for an output port of the Classic Parallel DEVS Atomic Model with Ports
 * conform Zeigler et al (2000), section 4.2.2. and section 4.3 (pp. 84 ff).
 * <p>
 * Copyright (c) 2009-2021 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="http://tudelft.nl/mseck">Mamadou Seck</a><br>
 * @author <a href="http://tudelft.nl/averbraeck">Alexander Verbraeck</a><br>
 * @param <A> the absolute storage type for the simulation time, e.g. Time, Float, or Double.
 * @param <R> the relative type for time storage, e.g. Duration for absolute Time. For most non-unit types, the absolute and
 *            relative types are the same.
 * @param <T> the extended type itself to be able to implement a comparator on the simulation time.
 * @param <TYPE> The type of messages the input port accepts.
 */
public interface OutputPortInterface<A extends Comparable<A> & Serializable, R extends Number & Comparable<R>,
        T extends SimTime<A, R, T>, TYPE>
{

    /**
     * Send a message through the output port.
     * @param value TYPE; the value to transfer.
     * @throws SimRuntimeException a simulation runtime exception
     * @throws RemoteException a remote exception
     */
    void send(TYPE value) throws SimRuntimeException, RemoteException;

    /**
     * @return the model to which the port belongs.
     */
    AbstractDEVSModel<A, R, T> getModel();

    /***********************************************************************************************************/
    /*********************************** EASY ACCESS INTERFACE EXTENSIONS **************************************/
    /***********************************************************************************************************/

    /**
     * Easy access interface OutputPortInterface.Double.
     * @param <P> The type of message the input port accepts
     */
    public interface TimeDouble<P> extends OutputPortInterface<Double, Double, SimTimeDouble, P>
    {
        // typed extension
    }

    /**
     * Easy access interface OutputPortInterface.Float.
     * @param <P> The type of message the input port accepts
     */

    public interface TimeFloat<P> extends OutputPortInterface<Float, Float, SimTimeFloat, P>
    {
        // typed extension
    }

    /**
     * Easy access interface OutputPortInterface.Long.
     * @param <P> The type of message the input port accepts
     */
    public interface TimeLong<P> extends OutputPortInterface<Long, Long, SimTimeLong, P>
    {
        // typed extension
    }

    /**
     * Easy access interface OutputPortInterface.DoubleUnit.
     * @param <P> The type of message the input port accepts
     */
    public interface TimeDoubleUnit<P> extends OutputPortInterface<Time, Duration, SimTimeDoubleUnit, P>
    {
        // typed extension
    }

    /**
     * Easy access interface OutputPortInterface.FloatUnit.
     * @param <P> The type of message the input port accepts
     */
    public interface TimeFloatUnit<P> extends OutputPortInterface<FloatTime, FloatDuration, SimTimeFloatUnit, P>
    {
        // typed extension
    }

}
