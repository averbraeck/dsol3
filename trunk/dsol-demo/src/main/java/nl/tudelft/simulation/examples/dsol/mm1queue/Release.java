package nl.tudelft.simulation.examples.dsol.mm1queue;

import java.rmi.RemoteException;

import nl.tudelft.simulation.dsol.formalisms.Resource;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;
import nl.tudelft.simulation.event.EventType;

/**
 * The Seize is an extended Seize block which computes the servicetime. <br>
 * (c) copyright 2003 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the Netherlands.
 * <br>
 * See for project information <a href="http://www.simulation.tudelft.nl"> www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/gpl.html">General Public License (GPL) </a>, no warranty <br>
 * @version 2.0 21.09.2003 <br>
 * @author <a href="http://www.tbm.tudelft.nl/webstaf/peterja/index.htm">Peter Jacobs </a>
 */
public class Release extends nl.tudelft.simulation.dsol.formalisms.flow.Release.TimeDouble
{
    /** */
    private static final long serialVersionUID = 1L;

    /** SERVICE_TIME_EVENT is fired when a customer is released. */
    public static final EventType SERVICE_TIME_EVENT = new EventType("SERVICE_TIME_EVENT");

    /**
     * constructs a new Release.
     * @param simulator the simulator on which to schedule
     * @param resource the resource to be released
     */
    public Release(final DEVSSimulatorInterface.TimeDouble simulator, final Resource<SimTimeDouble> resource)
    {
        super(simulator, resource);
    }

    /**
     * constructs a new Release.
     * @param simulator the simulator on which to schedule
     * @param resource the resource to be released
     * @param amount the amount to be released
     */
    public Release(final DEVSSimulatorInterface.TimeDouble simulator, final Resource<SimTimeDouble> resource,
            final double amount)
    {
        super(simulator, resource, amount);
    }

    /** {@inheritDoc} */
    @Override
    public final synchronized void releaseObject(final Object object) throws RemoteException
    {
        if (object instanceof Customer)
        {
            Customer customer = (Customer) object;
            double serviceTime = this.simulator.getSimulatorTime().get() - customer.getEntranceTime();
            this.fireTimedEvent(Release.SERVICE_TIME_EVENT, serviceTime, this.simulator.getSimulatorTime());
            super.releaseObject(object);
        }
    }
}
