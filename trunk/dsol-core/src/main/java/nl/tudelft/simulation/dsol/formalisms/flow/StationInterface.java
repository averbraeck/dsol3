package nl.tudelft.simulation.dsol.formalisms.flow;

import nl.tudelft.simulation.event.EventProducerInterface;
import nl.tudelft.simulation.event.EventType;

/**
 * A station is an object which accepts other objects and is linked to a destination.
 * <p>
 * (c) 2002-2018 <a href="https://simulation.tudelft.nl">Delft University of Technology </a>, the Netherlands. <br>
 * See for project information <a href="https://simulation.tudelft.nl"> www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:36:44 $
 * @since 1.5
 */

public interface StationInterface extends EventProducerInterface
{
    /** RECEIVE_EVENT is fired whenever an entity enters the station. */
    EventType RECEIVE_EVENT = new EventType("RECEIVE_EVENT");

    /** RECEIVE_EVENT is fired whenever an entity leaves the station. */
    EventType RELEASE_EVENT = new EventType("RELEASE_EVENT");

    /**
     * Method getDestination.
     * @return StationInterface is the destination of this station
     */
    StationInterface getDestination();

    /**
     * receives an object is invoked whenever an entity arrives.
     * @param object is the entity
     */
    void receiveObject(final Object object);

    /**
     * sets the destination of this object.
     * @param destination defines the next station in the model
     */
    void setDestination(final StationInterface destination);

}
