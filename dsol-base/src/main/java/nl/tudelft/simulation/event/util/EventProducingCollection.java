/*
 * @(#) EventProducingCollection.java Nov 19, 2003 Copyright (c) 2002-2005 Delft
 * University of Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All
 * rights reserved. This software is proprietary information of Delft University
 * of Technology 
 */
package nl.tudelft.simulation.event.util;

import java.util.Collection;
import java.util.Iterator;

import nl.tudelft.simulation.event.EventProducer;
import nl.tudelft.simulation.event.EventType;

/**
 * The Event producing collection provides a set to which one can subscribe interest in entry changes. This class does
 * not keep track of changes which take place indirectly. One is for example not notified on
 * <code>map.iterator.remove()</code>. A listener must subscribe to the iterator individually.
 * <p>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands.
 * <p>
 * See for project information <a href="http://www.simulation.tudelft.nl/dsol/event">www.simulation.tudelft.nl/event
 * </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:38:12 $
 * @since 1.5
 * @param <T> The type t od the eventproducing list.
 */
public class EventProducingCollection<T> extends EventProducer implements Collection<T>
{
    /** The default serial version UID for serializable classes */
    private static final long serialVersionUID = 1L;

    /** OBJECT_ADDED_EVENT is fired on new entries */
    public static final EventType OBJECT_ADDED_EVENT = new EventType("OBJECT_ADDED_EVENT");

    /** OBJECT_REMOVED_EVENT is fired on removel of entries */
    public static final EventType OBJECT_REMOVED_EVENT = new EventType("OBJECT_REMOVED_EVENT");

    /** the parent collection */
    private Collection<T> parent = null;

    /**
     * constructs a new EventProducingList.
     * @param parent the parent collection.
     */
    public EventProducingCollection(final Collection<T> parent)
    {
        super();
        this.parent = parent;
    }

    /**
     * @see java.util.Collection#size()
     */
    public int size()
    {
        return this.parent.size();
    }

    /**
     * @see java.util.Collection#isEmpty()
     */
    public boolean isEmpty()
    {
        return this.parent.isEmpty();
    }

    /**
     * @see java.util.Collection#clear()
     */
    public void clear()
    {
        this.parent.clear();
        this.fireEvent(OBJECT_REMOVED_EVENT, null);
    }

    /**
     * @see java.util.Collection#add(java.lang.Object)
     */
    public boolean add(final T o)
    {
        boolean result = this.parent.add(o);
        this.fireEvent(OBJECT_ADDED_EVENT, null);
        return result;
    }

    /**
     * @see java.util.Collection#addAll(java.util.Collection)
     */
    public boolean addAll(final Collection<? extends T> c)
    {
        boolean result = this.parent.addAll(c);
        this.fireEvent(OBJECT_ADDED_EVENT, null);
        return result;
    }

    /**
     * @see java.util.Collection#contains(java.lang.Object)
     */
    public boolean contains(final Object o)
    {
        return this.parent.contains(o);
    }

    /**
     * @see java.util.Collection#containsAll(java.util.Collection)
     */
    public boolean containsAll(final Collection<?> c)
    {
        return this.parent.containsAll(c);
    }

    /**
     * @see java.util.Collection#iterator()
     */
    public Iterator<T> iterator()
    {
        return new EventIterator<T>(this.parent.iterator());
    }

    /**
     * @see java.util.Collection#remove(java.lang.Object)
     */
    public boolean remove(final Object o)
    {
        boolean result = this.parent.remove(o);
        this.fireEvent(OBJECT_REMOVED_EVENT, null);
        return result;
    }

    /**
     * @see java.util.Collection#removeAll(java.util.Collection)
     */
    public boolean removeAll(final Collection<?> c)
    {
        boolean result = this.parent.removeAll(c);
        this.fireEvent(OBJECT_REMOVED_EVENT, null);
        return result;
    }

    /**
     * @see java.util.Collection#retainAll(java.util.Collection)
     */
    public boolean retainAll(final Collection<?> c)
    {
        boolean result = this.parent.retainAll(c);
        this.fireEvent(OBJECT_REMOVED_EVENT, null);
        return result;
    }

    /**
     * @see java.util.Collection#toArray()
     */
    public Object[] toArray()
    {
        return this.parent.toArray();
    }

    /**
     * @see java.util.Collection#toArray(T[])
     */
    public <E> E[] toArray(final E[] a)
    {
        return this.parent.toArray(a);
    }
}