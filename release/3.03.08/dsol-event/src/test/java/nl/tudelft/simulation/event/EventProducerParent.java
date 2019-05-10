package nl.tudelft.simulation.event;

/**
 * The EventProducerParent is an event producer used in JUNIT tests.
 * <p>
 * Copyright (c) 2004-2019 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
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
