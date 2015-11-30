package nl.tudelft.simulation.event;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nl.tudelft.simulation.event.ref.Reference;
import nl.tudelft.simulation.event.remote.RemoteEventListenerInterface;

/**
 * The EventListenerMap maps EventTypes on lists of References to EventListeners. The References can be Weak or Strong.
 * <p>
 * (c) copyright 2002-2014 <a href="http://www.simulation.tudelft.nl">Delft University of Technology</a>. <br>
 * BSD-style license. See <a href="http://www.simulation.tudelft.nl/dsol/3.0/license.html">DSOL License</a>. <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs</a>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @since 1.5
 */
public final class EventListenerMap implements Map<EventType, List<Reference<EventListenerInterface>>>, Serializable
{
    /** The default serial version UID for serializable classes. */
    private static final long serialVersionUID = 20140830L;

    /** the hasMap we map on. */
    private Map<EventType, List<Reference<EventListenerInterface>>> map = new HashMap<>();

    /**
     * constructs a new EventListenerMap.
     */
    public EventListenerMap()
    {
        super();
    }

    /** {@inheritDoc} */
    @Override
    public int size()
    {
        return this.map.size();
    }

    /** {@inheritDoc} */
    @Override
    public void clear()
    {
        this.map.clear();
    }

    /** {@inheritDoc} */
    @Override
    public boolean isEmpty()
    {
        return this.map.isEmpty();
    }

    /** {@inheritDoc} */
    @Override
    public boolean containsKey(final Object key)
    {
        return this.map.containsKey(key);
    }

    /** {@inheritDoc} */
    @Override
    public boolean containsValue(final Object value)
    {
        return this.map.containsValue(value);
    }

    /** {@inheritDoc} */
    @Override
    public Collection<List<Reference<EventListenerInterface>>> values()
    {
        return this.map.values();
    }

    /** {@inheritDoc} */
    @Override
    public void putAll(final Map<? extends EventType, ? extends List<Reference<EventListenerInterface>>> m)
    {
        this.map.putAll(m);
    }

    /** {@inheritDoc} */
    @Override
    public Set<Map.Entry<EventType, List<Reference<EventListenerInterface>>>> entrySet()
    {
        return this.map.entrySet();
    }

    /** {@inheritDoc} */
    @Override
    public Set<EventType> keySet()
    {
        return this.map.keySet();
    }

    /** {@inheritDoc} */
    @Override
    public List<Reference<EventListenerInterface>> get(final Object key)
    {
        return this.map.get(key);
    }

    /** {@inheritDoc} */
    @Override
    public List<Reference<EventListenerInterface>> remove(final Object key)
    {
        return this.map.remove(key);
    }

    /** {@inheritDoc} */
    @Override
    public List<Reference<EventListenerInterface>> put(final EventType key,
            final List<Reference<EventListenerInterface>> value)
    {
        return this.map.put(key, value);
    }

    /**
     * writes a serializable method to stream.
     * @param out the output stream
     * @throws IOException on IOException
     */
    private synchronized void writeObject(final ObjectOutputStream out) throws IOException
    {
        Map<EventType, List<Reference<EventListenerInterface>>> outMap = new HashMap<>();
        for (Iterator<EventType> i = this.keySet().iterator(); i.hasNext();)
        {
            EventType key = i.next();
            List<Reference<EventListenerInterface>> entriesList = this.get(key);
            for (Iterator<Reference<EventListenerInterface>> ii = entriesList.iterator(); ii.hasNext();)
            {
                Reference<EventListenerInterface> reference = ii.next();
                if (reference.get() instanceof RemoteEventListenerInterface)
                {
                    ii.remove();
                }
            }
            if (!entriesList.isEmpty())
            {
                outMap.put(key, entriesList);
            }
        }
        out.writeObject(outMap);
    }

    /**
     * reads a serializable method from stream.
     * @param in the input stream
     * @throws IOException on IOException
     * @throws ClassNotFoundException on ClassNotFoundException
     */
    @SuppressWarnings("unchecked")
    private void readObject(final java.io.ObjectInputStream in) throws IOException, ClassNotFoundException
    {
        this.map = (HashMap<EventType, List<Reference<EventListenerInterface>>>) in.readObject();
    }
}
