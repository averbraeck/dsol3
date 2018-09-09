package nl.tudelft.simulation.dsol.statistics;

import java.rmi.RemoteException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import nl.tudelft.simulation.dsol.formalisms.process.Process;
import nl.tudelft.simulation.dsol.simtime.SimTime;
import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;
import nl.tudelft.simulation.event.Event;
import nl.tudelft.simulation.event.EventInterface;
import nl.tudelft.simulation.event.EventProducerInterface;
import nl.tudelft.simulation.event.EventType;
import nl.tudelft.simulation.naming.context.ContextUtil;

/**
 * The counter extends the counter and links this it to the dsol framework <br>
 * (c) 2002-2018 <a href="https://simulation.tudelft.nl">Delft University of Technology </a>, the Netherlands. <br>
 * See for project information <a href="https://simulation.tudelft.nl"> www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:36:45 $
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @param <A> the absolute time type to use in timed events
 * @param <R> the relative time type
 * @param <T> the absolute simulation time to use in the warmup event
 */
public class Counter<A extends Comparable<A>, R extends Number & Comparable<R>, T extends SimTime<A, R, T>>
        extends nl.tudelft.simulation.jstats.statistics.Counter
{
    /** */
    private static final long serialVersionUID = 20140804L;

    /** the simulator to subscribe to and from. */
    private SimulatorInterface<A, R, T> simulator = null;

    /** we stopped the counter. */
    private boolean stopped = false;

    /** the logger. */
    private static Logger logger = LogManager.getLogger(Process.class);

    /**
     * constructs a new Counter.
     * @param description refers to the description of this counter
     * @param simulator the simulator
     * @throws RemoteException  on network error for one of the listeners
     */
    public Counter(final String description, final SimulatorInterface<A, R, T> simulator) throws RemoteException
    {
        super(description);
        this.simulator = simulator;
        if (this.simulator.getSimTime().gt(this.simulator.getReplication().getTreatment().getWarmupSimTime()))
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
        catch (NamingException exception)
        {
            logger.warn("<init>", exception);
        }
    }

    /**
     * constructs a new Counter.
     * @param description the description
     * @param simulator the simulator of this model
     * @param target the target on which to count
     * @param field the field which is counted
     * @throws RemoteException  on network error for one of the listeners
     */
    public Counter(final String description, final SimulatorInterface<A, R, T> simulator,
            final EventProducerInterface target, final EventType field) throws RemoteException
    {
        this(description, simulator);
        target.addListener(this, field, false);
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("checkstyle:designforextension")
    public void notify(final EventInterface event)
    {
        if (this.stopped)
        {
            return;
        }
        if (event.getSource().equals(this.simulator))
        {
            if (event.getType().equals(SimulatorInterface.WARMUP_EVENT))
            {
                try
                {
                    this.simulator.removeListener(this, SimulatorInterface.WARMUP_EVENT);
                }
                catch (RemoteException exception)
                {
                    logger.warn("problen removing Listener for SimulatorIterface.WARMUP_EVENT", exception);
                }
                super.initialize();
                return;
            }
            if (event.getType().equals(SimulatorInterface.END_OF_REPLICATION_EVENT))
            {
                this.stopped = true;
                try
                {
                    this.simulator.removeListener(this, SimulatorInterface.END_OF_REPLICATION_EVENT);
                }
                catch (RemoteException exception)
                {
                    logger.warn("problen removing Listener for SimulatorIterface.END_OF_REPLICATION_EVENT", exception);
                }
                this.endOfReplication();
                return;
            }
        }
        else if (this.isInitialized())
        {
            super.notify(event);
        }
    }

    /**
     * endOfReplication is invoked to store the final results.
     */
    @SuppressWarnings("checkstyle:designforextension")
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
            tally.notify(new Event(null, this, new Long(this.count)));
        }
        catch (Exception exception)
        {
            logger.warn("endOfReplication", exception);
        }
    }
}
