/*
 * @(#) Tally.java Sep 21, 2003 Copyright (c) 2002-2005 Delft University of
 * Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved.
 * This software is proprietary information of Delft University of Technology
 * 
 */
package nl.tudelft.simulation.dsol.statistics;

import java.rmi.RemoteException;

import javax.naming.Context;
import javax.naming.NamingException;

import nl.tudelft.simulation.dsol.simtime.SimTime;
import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;
import nl.tudelft.simulation.event.Event;
import nl.tudelft.simulation.event.EventInterface;
import nl.tudelft.simulation.event.EventProducerInterface;
import nl.tudelft.simulation.event.EventType;
import nl.tudelft.simulation.logger.Logger;
import nl.tudelft.simulation.naming.context.ContextUtil;

/**
 * The Tally extends the tally and links this it to the dsol framework <br>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:36:45 $
 * @author <a href="http://www.peter-jacobs.com">Peter Jacobs </a>
 * @param <T> the absolute simulation time to use in the warmup event.
 */
public class Tally<T extends SimTime<?, ?, T>> extends nl.tudelft.simulation.jstats.statistics.Tally
{
    /** */
    private static final long serialVersionUID = 20140804L;

    /** the simulator */
    private SimulatorInterface<?, ?, T> simulator = null;

    /** after the END_OF_REPLICATION we stop */
    private boolean stopped = false;

    /**
     * constructs a new Tally.
     * @param description refers to the description of this Tally.
     * @param simulator the simulator to schedule on.
     * @throws RemoteException on network failure.
     */
    public Tally(final String description, final SimulatorInterface<?, ?, T> simulator) throws RemoteException
    {
        super(description);
        this.simulator = simulator;
        if (this.simulator.getSimulatorTime().gt(this.simulator.getReplication().getTreatment().getWarmupTime()))
        {
            this.initialize();
        }
        else
        {
            this.simulator.addListener(this, SimulatorInterface.WARMUP_EVENT, false);
        }
        this.simulator.addListener(this, SimulatorInterface.END_OF_REPLICATION_EVENT, false);
        try
        {
            Context context = ContextUtil.lookup(this.simulator.getReplication().getContext(), "/statistics");
            ContextUtil.bind(context, this);
        }
        catch (RemoteException exception)
        {
            Logger.warning(this, "<init>", exception);
        }
    }

    /**
     * constructs a new Tally
     * @param description the description of this tally.
     * @param simulator the simulator to schedule on.
     * @param target the target on which to subscribe.
     * @param field the field which is counted
     * @throws RemoteException on network failure.
     */
    public Tally(final String description, final SimulatorInterface<?, ?, T> simulator, final EventProducerInterface target,
            final EventType field) throws RemoteException
    {
        this(description, simulator);
        target.addListener(this, field, false);
    }

    /**
     * @see nl.tudelft.simulation.event.EventListenerInterface #notify(nl.tudelft.simulation.event.EventInterface)
     */
    @Override
    public void notify(final EventInterface event)
    {
        if (this.stopped)
        {
            return;
        }
        try
        {
            if (event.getSource().equals(this.simulator))
            {
                if (event.getType().equals(SimulatorInterface.WARMUP_EVENT))
                {
                    this.simulator.removeListener(this, SimulatorInterface.WARMUP_EVENT);
                    super.initialize();
                    return;
                }
                if (event.getType().equals(SimulatorInterface.END_OF_REPLICATION_EVENT))
                {
                    this.stopped = true;
                    this.simulator.removeListener(this, SimulatorInterface.END_OF_REPLICATION_EVENT);
                    this.endOfReplication();
                    return;
                }
            }
            else if (this.isInitialized())
            {
                super.notify(event);
            }
        }
        catch (RemoteException remoteException)
        {
            Logger.warning(this, "notify", remoteException);
        }
    }

    /**
     * endOfReplication is invoked to store the final results
     */
    protected void endOfReplication()
    {
        try
        {
            Context context = this.simulator.getReplication().getTreatment().getExperiment().getContext();
            context = ContextUtil.lookup(context, "average");
            context = ContextUtil.lookup(context, "statistics");
            nl.tudelft.simulation.jstats.statistics.Tally tally = null;
            try
            {
                tally = (nl.tudelft.simulation.jstats.statistics.Tally) context.lookup(this.description);
            }
            catch (NamingException exception)
            {
                tally = new nl.tudelft.simulation.jstats.statistics.Tally(this.description);
                context.bind(this.description, tally);
                tally.initialize();
            }
            tally.notify(new Event(null, this, new Double(this.sampleMean)));
        }
        catch (Exception exception)
        {
            Logger.warning(this, "endOfReplication", exception);
        }
    }
}