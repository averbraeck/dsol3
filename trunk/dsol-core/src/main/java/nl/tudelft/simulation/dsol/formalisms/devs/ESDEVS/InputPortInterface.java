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
 * InputPortInterface class. Describes the contract for an input port of the Classic Parallel DEVS Atomic Model with Ports
 * conform Zeigler et al (2000), section 4.2.2. and section 4.3 (pp. 84 ff).
 * <p>
 * Copyright (c) 2009-2021 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="http://tudelft.nl/mseck">Mamadou Seck</a><br>
 * @author <a href="http://tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @param <A> the absolute storage type for the simulation time, e.g. Time, Float, or Double.
 * @param <R> the relative type for time storage, e.g. Duration for absolute Time. For most non-unit types, the absolute and
 *            relative types are the same.
 * @param <T> the extended type itself to be able to implement a comparator on the simulation time.
 * @param <TYPE> The type of message the input port accepts
 */
public interface InputPortInterface<A extends Comparable<A> & Serializable, R extends Number & Comparable<R>,
        T extends SimTime<A, R, T>, TYPE>
{
    /**
     * Receive and handle an event on the port.
     * @param value TYPE; the value that is received of type &lt;T&gt;
     * @param time T; the time on which the value is received
     * @throws RemoteException a remote exception occurred
     * @throws SimRuntimeException a simulation run time exception occurred
     */
    void receive(TYPE value, T time) throws RemoteException, SimRuntimeException;

    /**
     * @return the model to which the port belongs.
     */
    AbstractDEVSModel<A, R, T> getModel();

    /***********************************************************************************************************/
    /*********************************** EASY ACCESS INTERFACE EXTENSIONS **************************************/
    /***********************************************************************************************************/

    /**
     * Easy access interface InputPortInterface.Double.
     * @param <P> The type of message the input port accepts
     */
    public interface TimeDouble<P> extends InputPortInterface<Double, Double, SimTimeDouble, P>
    {
        // typed extension
    }

    /**
     * Easy access interface InputPortInterface.Float.
     * @param <P> The type of message the input port accepts
     */

    public interface TimeFloat<P> extends InputPortInterface<Float, Float, SimTimeFloat, P>
    {
        // typed extension
    }

    /**
     * Easy access interface InputPortInterface.Long.
     * @param <P> The type of message the input port accepts
     */
    public interface TimeLong<P> extends InputPortInterface<Long, Long, SimTimeLong, P>
    {
        // typed extension
    }

    /**
     * Easy access interface InputPortInterface.DoubleUnit.
     * @param <P> The type of message the input port accepts
     */
    public interface TimeDoubleUnit<P> extends InputPortInterface<Time, Duration, SimTimeDoubleUnit, P>
    {
        // typed extension
    }

    /**
     * Easy access interface InputPortInterface.FloatUnit.
     * @param <P> The type of message the input port accepts
     */
    public interface TimeFloatUnit<P> extends InputPortInterface<FloatTime, FloatDuration, SimTimeFloatUnit, P>
    {
        // typed extension
    }

}
