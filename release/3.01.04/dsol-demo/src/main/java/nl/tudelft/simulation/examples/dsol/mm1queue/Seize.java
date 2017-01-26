package nl.tudelft.simulation.examples.dsol.mm1queue;

import java.rmi.RemoteException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import nl.tudelft.simulation.dsol.formalisms.Resource;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;

/**
 * The Seize is an extended Seize block whic sets delay times on arriving customers. <br>
 * (c) copyright 2003 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the Netherlands.
 * <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/gpl.html">General Public License (GPL) </a>, no warranty <br>
 * @version 2.0 21.09.2003 <br>
 * @author <a href="http://www.tbm.tudelft.nl/webstaf/peterja/index.htm">Peter Jacobs </a>
 */
public class Seize extends nl.tudelft.simulation.dsol.formalisms.flow.Seize.TimeDouble
{
    /** */
    private static final long serialVersionUID = 1L;
    
    /** the logger. */
    private static Logger logger = LogManager.getLogger(Seize.class);

    /**
     * constructs a new Seize.
     * @param simulator the devs simulator on which to schedule
     * @param resource the resource to claim
     */
    public Seize(final DEVSSimulatorInterface.TimeDouble simulator, final Resource<SimTimeDouble> resource)
    {
        super(simulator, resource);
    }

    /**
     * constructs a new Seize.
     * @param simulator the devs simulator on which to schedule
     * @param resource the resource to claim
     * @param requestedCapacity the amount to claim
     */
    public Seize(final DEVSSimulatorInterface.TimeDouble simulator, final Resource<SimTimeDouble> resource,
            final double requestedCapacity)
    {
        super(simulator, resource, requestedCapacity);
    }

    /** {@inheritDoc} */
    @Override
    public final void receiveObject(final Object object) throws RemoteException
    {
        Customer customer = (Customer) object;
        try
        {
            customer.setEntranceTime(this.simulator.getSimulatorTime().get());
        }
        catch (RemoteException exception)
        {
            logger.warn("receiveObject", exception);
        }
        super.receiveObject(object);
    }
}
