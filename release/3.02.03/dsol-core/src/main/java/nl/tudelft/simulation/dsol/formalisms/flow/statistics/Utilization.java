package nl.tudelft.simulation.dsol.formalisms.flow.statistics;

import java.rmi.RemoteException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import nl.tudelft.simulation.dsol.formalisms.flow.StationInterface;
import nl.tudelft.simulation.dsol.logger.SimLogger;
import nl.tudelft.simulation.dsol.simtime.SimTime;
import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;
import nl.tudelft.simulation.dsol.statistics.Persistent;
import nl.tudelft.simulation.event.Event;
import nl.tudelft.simulation.event.EventInterface;
import nl.tudelft.simulation.naming.context.ContextUtil;

/**
 * A Utilization <br>
 * (c) 2002-2018 <a href="https://simulation.tudelft.nl">Delft University of Technology </a>, the Netherlands. <br>
 * See for project information <a href="https://simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:36:46 $
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @param <A> the absolute time type to use in timed events
 * @param <R> the relative time type
 * @param <T> the absolute simulation time to use in the warmup event
 */
public class Utilization<A extends Comparable<A>, R extends Number & Comparable<R>, T extends SimTime<A, R, T>>
        extends Persistent<A, R, T>
{
    /** */
    private static final long serialVersionUID = 1L;

    /** initialzed the tally. */
    private boolean initialized = false;

    /** the simulator. */
    private SimulatorInterface<A, R, T> simulator = null;

    /**
     * constructs a new Utilization.
     * @param description the description of this utilization
     * @param simulator the simulator
     * @param target the target
     * @throws RemoteException on network error for one of the listeners
     */
    public Utilization(final String description, final SimulatorInterface<A, R, T> simulator,
            final StationInterface target) throws RemoteException
    {
        super(description, simulator);
        this.simulator = simulator;
        target.addListener(this, StationInterface.RECEIVE_EVENT, false);
        target.addListener(this, StationInterface.RELEASE_EVENT, false);
        this.simulator.addListener(this, SimulatorInterface.WARMUP_EVENT, false);
        this.simulator.addListener(this, SimulatorInterface.END_OF_REPLICATION_EVENT, false);
        try
        {
            Context context = ContextUtil.lookup(simulator.getReplication().getContext(), "/statistics");
            ContextUtil.bind(context, this);
        }
        catch (NamingException exception)
        {
            SimLogger.always().warn(exception, "<init>");
        }
    }

    /** {@inheritDoc} */
    @Override
    public void notify(final EventInterface event)
    {
        if (event.getSource().equals(this.simulator))
        {
            if (event.getType().equals(SimulatorInterface.WARMUP_EVENT))
            {
                this.initialized = true;
                try
                {
                    this.simulator.removeListener(this, SimulatorInterface.WARMUP_EVENT);
                }
                catch (RemoteException exception)
                {
                    SimLogger.always().warn(exception,
                            "problem removing Listener for SimulatorIterface.WARMUP_EVENT");
                }
                super.initialize();
                return;
            }
            if (event.getType().equals(SimulatorInterface.END_OF_REPLICATION_EVENT))
            {
                try
                {
                    this.simulator.removeListener(this, SimulatorInterface.END_OF_REPLICATION_EVENT);
                }
                catch (RemoteException exception)
                {
                    SimLogger.always().warn(exception,
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
            String[] parts = nl.tudelft.simulation.naming.context.ContextUtil.resolveKey(this).split("/");
            String key = "";
            for (int i = 0; i < parts.length; i++)
            {
                if (i != parts.length - 2)
                {
                    key = key + parts[i] + "/";
                }
            }
            key = key.substring(0, key.length() - 1);
            nl.tudelft.simulation.jstats.statistics.Tally tally = null;
            try
            {
                tally = (nl.tudelft.simulation.jstats.statistics.Tally) new InitialContext().lookup(key);
            }
            catch (NamingException exception)
            {
                tally = new nl.tudelft.simulation.jstats.statistics.Tally(this.description);
                new InitialContext().bind(key, tally);
                tally.initialize();
            }
            tally.notify(new Event(null, this, new Double(this.sampleMean)));
        }
        catch (Exception exception)
        {
            SimLogger.always().warn(exception, "endOfReplication");
        }
    }
}
