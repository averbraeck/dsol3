package nl.tudelft.simulation.event;

import java.io.Serializable;

/**
 * The EventProducerChild is an event producer used in JUNIT tests.
 * <p>
 * (c) 2002-2018-2004 <a href="https://simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="https://simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:38:11 $
 * @since 1.5
 */
public class EventProducerChild extends EventProducer implements Serializable
{
    /** The default serial version UID for serializable classes. */
    private static final long serialVersionUID = 1L;

    /** EVENT_A is merely a test event. */
    public static final EventType EVENT_A = new EventType("EVENT_A");

    /** EVENT_B is merely a test event. */
    public static final EventType EVENT_B = new EventType("EVENT_B");

    /** EVENT_C is merely a test event. */
    public static final EventType EVENT_C = new EventType("EVENT_C");

    /**
     * constructs a new EventProducerChild.
     */
    protected EventProducerChild()
    {
        super();
    }
}
