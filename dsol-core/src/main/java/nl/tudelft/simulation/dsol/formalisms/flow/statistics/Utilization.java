package nl.tudelft.simulation.dsol.formalisms.flow.statistics;

import java.rmi.RemoteException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import nl.tudelft.simulation.dsol.formalisms.flow.StationInterface;
import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;
import nl.tudelft.simulation.event.Event;
import nl.tudelft.simulation.event.EventInterface;
import nl.tudelft.simulation.jstats.statistics.Persistent;
import nl.tudelft.simulation.logger.Logger;
import nl.tudelft.simulation.naming.context.ContextUtil;

/**
 * A Utilization <br>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:36:46 $
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 */
public class Utilization extends Persistent
{
    /** initialzed the tally */
    private boolean initialized = false;

    /**
     * simulator
     */
    private SimulatorInterface simulator = null;

    /**
     * constructs a new Utilization.
     * @param description the description of this utilization
     * @param simulator the simulator
     * @param target the target
     * @throws RemoteException on network failure
     */
    public Utilization(final String description, final SimulatorInterface simulator, final StationInterface target)
            throws RemoteException
    {
        super(description);
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
        catch (RemoteException exception)
        {
            Logger.warning(this, "<init>", exception);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void notify(final EventInterface event)
    {
        try
        {
            if (event.getSource().equals(this.simulator))
            {
                if (event.getType().equals(SimulatorInterface.WARMUP_EVENT))
                {
                    this.initialized = true;
                    this.simulator.removeListener(this, SimulatorInterface.WARMUP_EVENT);
                    super.initialize();
                    return;
                }
                if (event.getType().equals(SimulatorInterface.END_OF_REPLICATION_EVENT))
                {
                    this.simulator.removeListener(this, SimulatorInterface.END_OF_REPLICATION_EVENT);
                    this.endOfReplication();
                    return;
                }
            }
            else if (this.initialized)
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
            Logger.warning(this, "endOfReplication", exception);
        }
    }
}
