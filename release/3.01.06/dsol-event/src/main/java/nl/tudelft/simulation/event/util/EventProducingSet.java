package nl.tudelft.simulation.event.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import nl.tudelft.simulation.event.EventProducer;
import nl.tudelft.simulation.event.EventType;

/**
 * The Event producing set provides a set to which one can subscribe interest in entry changes. This class does not keep
 * track of changes which take place indirectly. One is for example not notified on <code>map.iterator.remove()</code>.
 * A listener must subscribe to the iterator individually.
 * <p>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands.
 * <p>
 * (c) copyright 2002-2014 <a href="http://www.simulation.tudelft.nl">Delft University of Technology</a>. <br>
 * BSD-style license. See <a href="http://www.simulation.tudelft.nl/dsol/3.0/license.html">DSOL License</a>. <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs</a>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @since 1.5
 * @param <T> the type of the set
 */
public class EventProducingSet<T> extends EventProducer implements Set<T>
{
    /** The default serial version UID for serializable classes. */
    private static final long serialVersionUID = 1L;

    /** OBJECT_ADDED_EVENT is fired on new entries. */
    public static final EventType OBJECT_ADDED_EVENT = new EventType("OBJECT_ADDED_EVENT");

    /** OBJECT_REMOVED_EVENT is fired on removal of entries. */
    public static final EventType OBJECT_REMOVED_EVENT = new EventType("OBJECT_REMOVED_EVENT");

    /** the parent set. */
    private Set<T> parent = null;

    /**
     * constructs a new EventProducingList.
     * @param parent the parent set.
     */
    public EventProducingSet(final Set<T> parent)
    {
        super();
        this.parent = parent;
    }

    /** {@inheritDoc} */
    @Override
    public int size()
    {
        return this.parent.size();
    }

    /** {@inheritDoc} */
    @Override
    public boolean isEmpty()
    {
        return this.parent.isEmpty();
    }

    /** {@inheritDoc} */
    @Override
    public void clear()
    {
        this.parent.clear();
        this.fireEvent(OBJECT_REMOVED_EVENT);
    }

    /** {@inheritDoc} */
    @Override
    public boolean add(final T o)
    {
        boolean result = this.parent.add(o);
        this.fireEvent(OBJECT_ADDED_EVENT);
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public boolean addAll(final Collection<? extends T> c)
    {
        boolean result = this.parent.addAll(c);
        this.fireEvent(OBJECT_ADDED_EVENT);
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public boolean contains(final Object o)
    {
        return this.parent.contains(o);
    }

    /** {@inheritDoc} */
    @Override
    public boolean containsAll(final Collection<?> c)
    {
        return this.parent.containsAll(c);
    }

    /** {@inheritDoc} */
    @Override
    public Iterator<T> iterator()
    {
        return new EventIterator<T>(this.parent.iterator());
    }

    /** {@inheritDoc} */
    @Override
    public boolean remove(final Object o)
    {
        boolean result = this.parent.remove(o);
        this.fireEvent(OBJECT_REMOVED_EVENT);
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public boolean removeAll(final Collection<?> c)
    {
        boolean result = this.parent.removeAll(c);
        this.fireEvent(OBJECT_REMOVED_EVENT);
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public boolean retainAll(final Collection<?> c)
    {
        boolean result = this.parent.retainAll(c);
        this.fireEvent(OBJECT_REMOVED_EVENT);
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public Object[] toArray()
    {
        return this.parent.toArray();
    }

    /** {@inheritDoc} */
    @Override
    public <E> E[] toArray(final E[] a)
    {
        return this.parent.toArray(a);
    }
}
