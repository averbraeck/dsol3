package nl.tudelft.simulation.examples.dsol.terminal;

import nl.tudelft.simulation.event.EventProducer;
import nl.tudelft.simulation.event.EventType;

/**
 * <p>
 * copyright (c) 2002-2018 <a href="https://simulation.tudelft.nl">Delft University of Technology</a>. <br>
 * BSD-style license. See <a href="https://simulation.tudelft.nl/dsol/3.0/license.html">DSOL License</a>. <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs</a>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @version Jul 25, 2018
 */
public class Ship extends EventProducer
{
    /** */
    private static final long serialVersionUID = 1L;

    /** the number of loaded containers. */
    private int containers = 0;

    /** the ship's capacity. */
    private final int capacity;

    /** the ship-full event. */
    public static final EventType SHIP_FULL_EVENT = new EventType("SHIP_FULL_EVENT");

    /**
     * @param capacity the ship's capacity
     */
    public Ship(final int capacity)
    {
        this.capacity = capacity;
    }

    /**
     * increase the number of containers and fire an event when full.
     */
    public synchronized void incContainers()
    {
        this.containers++;
        if (this.containers >= this.capacity)
        {
            if (Terminal.DEBUG)
            {
                System.out.println("SHIP IS FULL -- EVENT FIRED");
            }
            fireEvent(SHIP_FULL_EVENT, this.containers);
        }
    }

    /**
     * @return containers
     */
    public final int getContainers()
    {
        return this.containers;
    }
}
