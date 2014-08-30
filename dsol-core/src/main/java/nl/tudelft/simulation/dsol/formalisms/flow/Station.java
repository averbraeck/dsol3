package nl.tudelft.simulation.dsol.formalisms.flow;

import java.rmi.RemoteException;

import nl.tudelft.simulation.dsol.simtime.SimTime;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;
import nl.tudelft.simulation.event.EventProducer;

/**
 * A station is an object which can accept other objects.
 * <p>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl"> www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:36:44 $
 * @param <A> the absolute storage type for the simulation time, e.g. Calendar, UnitTimeDouble, or Double.
 * @param <R> the relative type for time storage, e.g. Long for the Calendar. For most non-calendar types, the absolute
 *            and relative types are the same.
 * @param <T> the extended type itself to be able to implement a comparator on the simulation time.
 * @since 1.5
 */
public abstract class Station<A extends Comparable<A>, R extends Number & Comparable<R>, T extends SimTime<A, R, T>>
        extends EventProducer implements StationInterface
{
    /** */
    private static final long serialVersionUID = 20140805L;

    /** simulator is the simulator on which behavior is scheduled. */
    protected DEVSSimulatorInterface<A, R, T> simulator;

    /** destination refers to the next station in the process-model chain. */
    protected StationInterface destination;

    /**
     * constructs a new Station.
     * @param simulator is the simulator on which behavior is scheduled
     */
    public Station(final DEVSSimulatorInterface<A, R, T> simulator)
    {
        super();
        this.simulator = simulator;
    }

    /** {@inheritDoc} */
    public void receiveObject(final Object object) throws RemoteException
    {
        this.fireTimedEvent(StationInterface.RECEIVE_EVENT, 1.0, this.simulator.getSimulatorTime());
    }

    /** {@inheritDoc} */
    public void setDestination(final StationInterface destination)
    {
        this.destination = destination;
    }

    /**
     * releases an object
     * @param object is the entity
     * @throws RemoteException on network failure
     */
    protected synchronized void releaseObject(final Object object) throws RemoteException
    {
        this.fireTimedEvent(StationInterface.RELEASE_EVENT, 0.0, this.simulator.getSimulatorTime());
        if (this.destination != null)
        {
            this.destination.receiveObject(object);
        }
    }

    /**
     * Returns the destination.
     * @return the destination station
     */
    public StationInterface getDestination()
    {
        return this.destination;
    }

}
