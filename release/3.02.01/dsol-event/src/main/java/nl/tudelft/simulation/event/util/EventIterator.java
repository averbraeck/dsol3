package nl.tudelft.simulation.event.util;

import java.util.Iterator;

import nl.tudelft.simulation.event.EventProducer;
import nl.tudelft.simulation.event.EventType;

/**
 * The Event producing iterator provides a set to which one can subscribe interest in entry changes.
 * <p>
 * copyright (c) 2002-2018  <a href="http://www.simulation.tudelft.nl">Delft University of Technology</a>. <br>
 * BSD-style license. See <a href="http://www.simulation.tudelft.nl/dsol/3.0/license.html">DSOL License</a>. <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs</a>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
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
     * @param parent parent.
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
