/*
 * @(#) EventProducingMap.java Nov 19, 2003 Copyright (c) 2002-2005 Delft
 * University of Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All
 * rights reserved. This software is proprietary information of Delft University
 * of Technology 
 */
package nl.tudelft.simulation.event.util;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import nl.tudelft.simulation.event.EventProducer;
import nl.tudelft.simulation.event.EventType;

/**
 * The Event producing map provides a map to which one can subscribe interest in
 * entry changes. This class does not keep track of changes which take place
 * indirectly. One is for example not notified on
 * <code>map.iterator.remove()</code>. A listener must subscribe to the
 * iterator, key set, etc. individually.
 * <p>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft
 * University of Technology </a>, the Netherlands.
 * <p>
 * See for project information <a
 * href="http://www.simulation.tudelft.nl/dsol/event">www.simulation.tudelft.nl/event
 * </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser
 * General Public License (LGPL) </a>, no warranty
 * 
 * @author <a href="http://www.peter-jacobs.com">Peter Jacobs </a>
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:38:12 $
 * @since 1.5
 * @param <K> the key
 * @param <V> the key
 */
public class EventProducingMap<K, V> extends EventProducer implements Map<K, V>
{
    /** The default serial version UID for serializable classes */
    private static final long serialVersionUID = 1L;
    
    /** OBJECT_ADDED_EVENT is fired on new entries */
    public static final EventType OBJECT_ADDED_EVENT = new EventType(
            "OBJECT_ADDED_EVENT");

    /** OBJECT_REMOVED_EVENT is fired on removel of entries */
    public static final EventType OBJECT_REMOVED_EVENT = new EventType(
            "OBJECT_REMOVED_EVENT");

    /** the parent map */
    private Map<K, V> parent = null;

    /**
     * constructs a new EventProducingMap.
     * 
     * @param parent the parent map.
     */
    public EventProducingMap(final Map<K, V> parent)
    {
        super();
        this.parent = parent;
    }

    /**
     * @see java.util.Map#size()
     */
    public int size()
    {
        return this.parent.size();
    }

    /**
     * @see java.util.Map#isEmpty()
     */
    public boolean isEmpty()
    {
        return this.parent.isEmpty();
    }

    /**
     * @see java.util.Map#containsKey(java.lang.Object)
     */
    public boolean containsKey(final Object arg0)
    {
        return this.parent.containsKey(arg0);
    }

    /**
     * @see java.util.Map#containsValue(java.lang.Object)
     */
    public boolean containsValue(final Object arg0)
    {
        return this.parent.containsValue(arg0);
    }

    /**
     * @see java.util.Map#get(java.lang.Object)
     */
    public V get(final Object arg0)
    {
        return this.parent.get(arg0);
    }

    /**
     * @see java.util.Map#put(java.lang.Object, java.lang.Object)
     */
    public V put(final K arg0, final V arg1)
    {
        V result = this.parent.put(arg0, arg1);
        this.fireEvent(OBJECT_ADDED_EVENT, null);
        return result;
    }

    /**
     * @see java.util.Map#remove(java.lang.Object)
     */
    public V remove(final Object arg0)
    {
        V result = this.parent.remove(arg0);
        this.fireEvent(OBJECT_REMOVED_EVENT, null);
        return result;
    }

    /**
     * @see java.util.Map#putAll(java.util.Map)
     */
    public void putAll(final Map< ? extends K, ? extends V> arg0)
    {
        this.parent.putAll(arg0);
        this.fireEvent(OBJECT_ADDED_EVENT, null);
    }

    /**
     * @see java.util.Map#clear()
     */
    public void clear()
    {
        this.parent.clear();
        this.fireEvent(OBJECT_REMOVED_EVENT, null);
    }

    /**
     * @see java.util.Map#keySet()
     */
    public Set<K> keySet()
    {
        return this.parent.keySet();
    }

    /**
     * @see java.util.Map#values()
     */
    public Collection<V> values()
    {
        return this.parent.values();
    }

    /**
     * @see java.util.Map#entrySet()
     */
    public Set<Map.Entry<K, V>> entrySet()
    {
        return this.parent.entrySet();
    }
}