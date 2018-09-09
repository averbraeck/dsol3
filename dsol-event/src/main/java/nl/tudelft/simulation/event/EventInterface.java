package nl.tudelft.simulation.event;

import java.io.Serializable;

/**
 * The EventInterface defines the interface for all events in the DSOL project.
 * <p>
 * copyright (c) 2002-2018  <a href="https://simulation.tudelft.nl">Delft University of Technology</a>. <br>
 * BSD-style license. See <a href="https://simulation.tudelft.nl/dsol/3.0/license.html">DSOL License</a>. <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs</a>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @since 1.5
 */

public interface EventInterface extends Serializable
{

    /**
     * returns the source of the event. The source is the sender of the event
     * @return the source of the event
     */
    Object getSource();

    /**
     * returns the content of this event.
     * @return the content of this event
     */
    Object getContent();

    /**
     * returns the type of the event.
     * @return the eventType of the event
     */
    EventType getType();
}
