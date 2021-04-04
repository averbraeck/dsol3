package nl.tudelft.simulation.dsol.formalisms.devs.ESDEVS;

import java.io.Serializable;

import nl.tudelft.simulation.dsol.simtime.SimTime;

/**
 * EIC class. EIC stands for External Input Coupling, which is a coupling between the outside of a coupled model and a component
 * within that coupled model. The definition can be found in Zeigler et al. (2000), p. 86-87.
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
 * @param <P> the type of message the EIC accepts.
 */
public class EIC<A extends Comparable<A> & Serializable, R extends Number & Comparable<R>, T extends SimTime<A, R, T>, P>
{
    /** the output port of the sending component. */
    private InputPortInterface<A, R, T, P> fromPort;

    /** the input port of the receiving component. */
    private InputPortInterface<A, R, T, P> toPort;

    /**
     * Make the wiring between output and input.
     * @param fromPort InputPortInterface&lt;A,R,T,P&gt;; the output port of the sending component
     * @param toPort InputPortInterface&lt;A,R,T,P&gt;; input port of the receiving component
     * @throws Exception in case of wiring to self
     */
    public EIC(final InputPortInterface<A, R, T, P> fromPort, final InputPortInterface<A, R, T, P> toPort) throws Exception
    {
        this.fromPort = fromPort;
        this.toPort = toPort;

        if (this.fromPort.getModel().equals(toPort.getModel()))
        {
            throw new Exception("no self coupling allowed");
        }
    }

    /**
     * @return the output port of the sending component.
     */
    public final InputPortInterface<A, R, T, P> getFromPort()
    {
        return this.fromPort;
    }

    /**
     * @return the input port of the receiving component.
     */
    public final InputPortInterface<A, R, T, P> getToPort()
    {
        return this.toPort;
    }
}
