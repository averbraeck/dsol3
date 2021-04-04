package nl.tudelft.simulation.dsol.formalisms.flow.statistics;

import java.io.Serializable;
import java.rmi.RemoteException;

import org.djutils.event.Event;
import org.djutils.event.EventInterface;
import org.djutils.event.ref.ReferenceType;
import org.djutils.stats.summarizers.event.EventBasedTally;

import nl.tudelft.simulation.dsol.experiment.ReplicationInterface;
import nl.tudelft.simulation.dsol.formalisms.flow.StationInterface;
import nl.tudelft.simulation.dsol.simtime.SimTime;
import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;
import nl.tudelft.simulation.dsol.statistics.SimPersistent;
import nl.tudelft.simulation.naming.context.ContextInterface;
import nl.tudelft.simulation.naming.context.util.ContextUtil;

/**
 * A Utilization statistic for the flow components.
 * <p>
 * Copyright (c) 2002-2021 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @param <A> the absolute time type to use in timed events
 * @param <R> the relative time type
 * @param <T> the absolute simulation time to use in the warmup event
 */
public class Utilization<A extends Comparable<A> & Serializable, R extends Number & Comparable<R>, T extends SimTime<A, R, T>>
        extends SimPersistent<A, R, T>
{
    /** */
    private static final long serialVersionUID = 1L;

    /** initialzed the tally. */
    private boolean initialized = false;

    /** the simulator. */
    private SimulatorInterface<A, R, T> simulator = null;

    /**
     * constructs a new Utilization.
     * @param description String; the description of this utilization
     * @param simulator SimulatorInterface&lt;A,R,T&gt;; the simulator
     * @param target StationInterface&lt;A,R,T&gt;; the target
     * @throws RemoteException on network error for one of the listeners
     */
    public Utilization(final String description, final SimulatorInterface<A, R, T> simulator,
            final StationInterface<A, R, T> target) throws RemoteException
    {
        super(description, simulator);
        this.simulator = simulator;
        target.addListener(this, StationInterface.RECEIVE_EVENT, ReferenceType.STRONG);
        target.addListener(this, StationInterface.RELEASE_EVENT, ReferenceType.STRONG);
        this.simulator.addListener(this, ReplicationInterface.WARMUP_EVENT, ReferenceType.STRONG);
        this.simulator.addListener(this, ReplicationInterface.END_REPLICATION_EVENT, ReferenceType.STRONG);
        // object is already bound, because SimPersistend (super) bound the statistic to the Context
    }

    /** {@inheritDoc} */
    @Override
    public void notify(final EventInterface event)
    {
        if (event.getSourceId().equals(this.simulator.getSourceId()))
        {
            if (event.getType().equals(ReplicationInterface.WARMUP_EVENT))
            {
                this.initialized = true;
                try
                {
                    this.simulator.removeListener(this, ReplicationInterface.WARMUP_EVENT);
                }
                catch (RemoteException exception)
                {
                    this.simulator.getLogger().always().warn(exception,
                            "problem removing Listener for SimulatorIterface.WARMUP_EVENT");
                }
                super.initialize();
                return;
            }
            if (event.getType().equals(ReplicationInterface.END_REPLICATION_EVENT))
            {
                try
                {
                    this.simulator.removeListener(this, ReplicationInterface.END_REPLICATION_EVENT);
                }
                catch (RemoteException exception)
                {
                    this.simulator.getLogger().always().warn(exception,
                            "problem removing Listener for SimulatorIterface.END_OF_REPLICATION_EVENT");
                }
                this.endOfReplication();
                return;
            }
        }
        else if (this.initialized)
        {
            super.notify(event);
        }
    }

    /** {@inheritDoc} */
    @Override
    protected void endOfReplication()
    {
        try
        {
            // TODO: do only if replication is part of an experiment or a series of replications
            // TODO: store one level higher than the replication itself
            ContextInterface context = ContextUtil
                    .lookupOrCreateSubContext(this.simulator.getReplication().getContext(), "statistics");
            EventBasedTally experimentTally;
            if (context.hasKey(getDescription()))
            {
                experimentTally = (EventBasedTally) context.getObject(getDescription());
            }
            else
            {
                experimentTally = new EventBasedTally(getDescription());
                context.bindObject(getDescription(), experimentTally);
                experimentTally.initialize();
            }
            experimentTally.notify(new Event(null, getSourceId(), Double.valueOf(getWeightedSampleMean())));
        }
        catch (Exception exception)
        {
            this.simulator.getLogger().always().warn(exception, "endOfReplication");
        }
    }
}
