/*
 * @(#) EventListenerMap.java Jan 13, 2005 Copyright (c) 2004 Delft University
 * of Technology Jaffalaan 5, 2628 BX Delft, the Netherlands All rights
 * reserved. This software is proprietary information of Delft University of
 * Technology The code is published under the General Public License
 */
package nl.tudelft.simulation.event;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import nl.tudelft.simulation.event.ref.Reference;
import nl.tudelft.simulation.event.remote.RemoteEventListenerInterface;

/**
 * The specifies
 * <p>
 * (c) copyright 2004 <a href="http://www.simulation.tudelft.nl/dsol/">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl/dsol/"> www.simulation.tudelft.nl/dsol </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/gpl.html">General Public License (GPL) </a>, no warranty <br>
 * @author <a href="http://www.peter-jacobs.com/index.htm"> Peter Jacobs </a>
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:38:11 $
 * @since 1.5
 * @param <K> the key type in this eventlist
 * @param <V> the value type of this map
 */
public class EventListenerMap<K, V> implements Map<K, V>, Serializable
{
    /** The default serial version UID for serializable classes */
    private static final long serialVersionUID = 1L;

    /** the hasMap we map on */
    private Map<K, V> map = new HashMap<K, V>();

    /**
     * constructs a new EventListenerMap
     */
    public EventListenerMap()
    {
        super();
    }

    /**
     * @see java.util.Map#size()
     */
    public int size()
    {
        return this.map.size();
    }

    /**
     * @see java.util.Map#clear()
     */
    public void clear()
    {
        this.map.clear();
    }

    /**
     * @see java.util.Map#isEmpty()
     */
    public boolean isEmpty()
    {
        return this.map.isEmpty();
    }

    /**
     * @see java.util.Map#containsKey(java.lang.Object)
     */
    public boolean containsKey(Object key)
    {
        return this.map.containsKey(key);
    }

    /**
     * @see java.util.Map#containsValue(java.lang.Object)
     */
    public boolean containsValue(Object value)
    {
        return this.map.containsValue(value);
    }

    /**
     * @see java.util.Map#values()
     */
    public Collection<V> values()
    {
        return this.map.values();
    }

    /**
     * @see java.util.Map#putAll(java.util.Map)
     */
    public void putAll(Map<? extends K, ? extends V> t)
    {
        this.map.putAll(t);
    }

    /**
     * @see java.util.Map#entrySet()
     */
    public Set<Map.Entry<K, V>> entrySet()
    {
        return this.map.entrySet();
    }

    /**
     * @see java.util.Map#keySet()
     */
    public Set<K> keySet()
    {
        return this.map.keySet();
    }

    /**
     * @see java.util.Map#get(java.lang.Object)
     */
    public V get(Object key)
    {
        return this.map.get(key);
    }

    /**
     * @see java.util.Map#remove(java.lang.Object)
     */
    public V remove(Object key)
    {
        return this.map.remove(key);
    }

    /**
     * @see java.util.Map#put(java.lang.Object, java.lang.Object)
     */
    public V put(K key, V value)
    {
        return this.map.put(key, value);
    }

    /**
     * writes a serializable method to stream
     * @param out the outputstream
     * @throws IOException on IOException
     */
    @SuppressWarnings("unchecked")
    private synchronized void writeObject(final ObjectOutputStream out) throws IOException
    {
        Map outMap = new HashMap();
        for (Iterator<K> i = this.keySet().iterator(); i.hasNext();)
        {
            Object key = i.next();
            ArrayList entriesList = new ArrayList(Arrays.asList((Reference[]) this.get(key)));
            for (Iterator ii = entriesList.iterator(); ii.hasNext();)
            {
                Reference reference = (Reference) ii.next();
                if (reference.get() instanceof RemoteEventListenerInterface)
                {
                    ii.remove();
                }
            }
            if (!entriesList.isEmpty())
            {
                outMap.put(key, entriesList.toArray(new Reference[entriesList.size()]));
            }
        }
        out.writeObject(outMap);
    }

    /**
     * reads a serializable method from stream
     * @param in the inputstream
     * @throws IOException on IOException
     * @throws ClassNotFoundException on ClassNotFoundException
     */
    @SuppressWarnings("unchecked")
    private synchronized void readObject(final java.io.ObjectInputStream in) throws IOException, ClassNotFoundException
    {
        this.map = (HashMap) in.readObject();
    }
}