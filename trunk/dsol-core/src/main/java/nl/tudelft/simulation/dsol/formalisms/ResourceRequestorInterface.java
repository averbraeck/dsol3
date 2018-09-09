package nl.tudelft.simulation.dsol.formalisms;

import java.rmi.RemoteException;

import nl.tudelft.simulation.dsol.simtime.SimTime;

/**
 * This interface provides a callback method to the resource. Whenever resource is available this method is invoked on
 * the requestor.
 * <p>
 * (c) 2002-2018 <a href="https://simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="https://simulation.tudelft.nl"> www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:36:45 $
 * @param <A> the absolute time type to use in timed events
 * @param <R> the relative time type
 * @param <T> the simulation time type.
 * @since 1.5
 */
public interface ResourceRequestorInterface<A extends Comparable<A>, R extends Number & Comparable<R>, T extends SimTime<A, R, T>>
{
    /**
     * receive the requested resource.
     * @param requestedCapacity reflects the amount requested.
     * @param resource the requested resource.
     * @throws RemoteException on network failure.
     */
    void receiveRequestedResource(final double requestedCapacity, final Resource<A, R, T> resource) throws RemoteException;
}
