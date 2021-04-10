package nl.tudelft.simulation.dsol.experiment;

import java.io.Serializable;

import org.djutils.exceptions.Throw;

import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.simtime.SimTime;
import nl.tudelft.simulation.naming.context.ContextInterface;

/**
 * The base class for a single replication of an Experiment.
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
public abstract class AbstractReplication<A extends Comparable<A> & Serializable, R extends Number & Comparable<R>,
        T extends SimTime<A, R, T>> implements ReplicationInterface<A, R, T>
{
    /** The default serial version UID for serializable classes. */
    private static final long serialVersionUID = 20210404L;

    /** the run control for the replication. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected final RunControlInterface<A, R, T> runControl;

    /** the context root of this replication. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected ContextInterface context;

    /**
     * Construct a stand-alone replication. Checking the validity of the arguments is left to the RunControl object.
     * @param id String; the id of the replication; should be unique within the experiment.
     * @param startTime T; the start time as a time object.
     * @param warmupPeriod R; the warmup period, included in the runlength (!)
     * @param runLength R; the total length of the run, including the warm-up period.
     * @throws NullPointerException when id, startTime, warmupPeriod or runLength is null
     * @throws SimRuntimeException when warmup period is negative, or run length is zero or negative, or when a context for the
     *             replication cannot be created, or when the warmup time is longer than or equal to the runlength
     */
    public AbstractReplication(final String id, final T startTime, final R warmupPeriod, final R runLength)
    {
        this(new RunControl<>(id, startTime, warmupPeriod, runLength));
    }

    /**
     * Construct a stand-alone replication using a RunControl to store the run information.
     * @param runControl RunControlInterface; the run control for the replication
     * @throws NullPointerException when runControl is null
     */
    public AbstractReplication(final RunControlInterface<A, R, T> runControl)
    {
        Throw.whenNull(runControl, "runControl should not be null");
        this.runControl = runControl;
    }

    /** {@inheritDoc} */
    @Override
    public final ContextInterface getContext()
    {
        return this.context;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return "Replication " + this.getDescription();
    }
}
