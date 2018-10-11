package nl.tudelft.simulation.immutablecollections;

import java.util.HashMap;
import java.util.Map;

/**
 * An immutable wrapper for a HashMap.
 * <p>
 * Copyright (c) 2016-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. See for project information <a href="https://simulation.tudelft.nl/" target="_blank">
 * https://simulation.tudelft.nl</a>. The DSOL project is distributed under a three-clause BSD-style license, which can
 * be found at <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 * @author <a href="http://www.transport.citg.tudelft.nl">Wouter Schakel</a>
 * @param <K> the key type of content of this Map
 * @param <V> the value type of content of this Map
 */
public class ImmutableHashMap<K, V> extends ImmutableAbstractMap<K, V>
{
    /** */
    private static final long serialVersionUID = 20160507L;

    /**
     * @param map Map&lt;K,V&gt;; the map to use for the immutable map.
     */
    public ImmutableHashMap(final Map<K, V> map)
    {
        super(new HashMap<K, V>(map), Immutable.COPY);
    }

    /**
     * @param map Map&lt;K,V&gt;; the map to use for the immutable map.
     * @param copyOrWrap COPY stores a safe, internal copy of the collection; WRAP stores a pointer to the original
     *            collection
     */
    public ImmutableHashMap(final Map<K, V> map, final Immutable copyOrWrap)
    {
        super(copyOrWrap == Immutable.COPY ? new HashMap<K, V>(map) : map, copyOrWrap);
    }

    /**
     * @param immutableMap ImmutableAbstractMap&lt;K,V&gt;; the map to use for the immutable map.
     */
    public ImmutableHashMap(final ImmutableAbstractMap<K, V> immutableMap)
    {
        super(new HashMap<K, V>(immutableMap.getMap()), Immutable.COPY);
    }

    /**
     * @param immutableMap ImmutableAbstractMap&lt;K,V&gt;; the map to use for the immutable map.
     * @param copyOrWrap COPY stores a safe, internal copy of the collection; WRAP stores a pointer to the original
     *            collection
     */
    public ImmutableHashMap(final ImmutableAbstractMap<K, V> immutableMap, final Immutable copyOrWrap)
    {
        super(copyOrWrap == Immutable.COPY ? new HashMap<K, V>(immutableMap.getMap()) : immutableMap.getMap(),
                copyOrWrap);
    }

    /** {@inheritDoc} */
    @Override
    protected final Map<K, V> getMap()
    {
        return super.getMap();
    }

    /** {@inheritDoc} */
    @Override
    public final Map<K, V> toMap()
    {
        return new HashMap<K, V>(getMap());
    }

    /** {@inheritDoc} */
    @Override
    public final ImmutableSet<K> keySet()
    {
        return new ImmutableHashSet<K>(getMap().keySet());
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("checkstyle:designforextension")
    public String toString()
    {
        Map<K, V> map = getMap();
        if (null == map)
        {
            return "ImmutableHashMap []";
        }
        return "ImmutableHashMap [" + map.toString() + "]";
    }

}
