package nl.tudelft.simulation.event;

/**
 * The EventProducerParent is an event producer used in JUNIT tests.
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
public class EventProducerParent extends EventProducerChild
{
    /** The default serial version UID for serializable classes. */
    private static final long serialVersionUID = 1L;

    /** event_c is merely a test event. */
    @SuppressWarnings("hiding")
    public static final EventType EVENT_C = new EventType("EVENT_C");

    /** event_d is merely a test event. */
    protected static EventType eventD = new EventType("EVENT_D");

    /** event_e is merely a test event. */
    public static final EventType EVENT_E = new EventType("EVENT_E");

    /**
     * constructs a new EventProducerChild.
     */
    public EventProducerParent()
    {
        super();
    }

    /** {@inheritDoc} */
    @Override
    public EventInterface fireEvent(final EventInterface event)
    {
        return super.fireEvent(event);
    }
}
