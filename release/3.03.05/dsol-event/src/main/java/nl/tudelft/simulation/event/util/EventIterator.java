package nl.tudelft.simulation.event.util;

import java.util.Iterator;

import nl.tudelft.simulation.event.EventProducer;
import nl.tudelft.simulation.event.EventType;

/**
 * The Event producing iterator provides a set to which one can subscribe interest in entry changes.
 * <p>
 * Copyright (c) 2002-2019 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. See for project information <a href="https://simulation.tudelft.nl/" target="_blank">
 * https://simulation.tudelft.nl</a>. The DSOL project is distributed under a three-clause BSD-style license, which can
 * be found at <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @since 1.5
 * @param <T> the type of the iterator
 */
public class EventIterator<T> extends EventProducer implements Iterator<T>
{
    /** The default serial version UID for serializable classes. */
    private static final long serialVersionUID = 1L;

    /** OBJECT_REMOVED_EVENT is fired on removal of entries. */
    public static final EventType OBJECT_REMOVED_EVENT = new EventType("OBJECT_REMOVED_EVENT");

    /** our parent iterator. */
    private Iterator<T> parent = null;

    /**
     * constructs a new Iterator.
     * @param parent Iterator&lt;T&gt;; parent.
     */
    public EventIterator(final Iterator<T> parent)
    {
        super();
        this.parent = parent;
    }

    /** {@inheritDoc} */
    @Override
    public boolean hasNext()
    {
        return this.parent.hasNext();
    }

    /** {@inheritDoc} */
    @Override
    public T next()
    {
        return this.parent.next();
    }

    /** {@inheritDoc} */
    @Override
    public void remove()
    {
        this.parent.remove();
        this.fireEvent(OBJECT_REMOVED_EVENT);
    }
}
