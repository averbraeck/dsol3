package nl.tudelft.simulation.immutablecollections;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * An immutable wrapper for a HashSet.
 * <p>
 * Copyright (c) 2013-2018 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights
 * reserved. <br>
 * BSD-style license. See <a href="https://simulation.tudelft.nl/dsol/3.0/license.html">DSOL License</a>.
 * </p>
 * $LastChangedDate: 2015-07-24 02:58:59 +0200 (Fri, 24 Jul 2015) $, @version $Revision: 1147 $, by $Author: averbraeck
 * $, initial version May 7, 2016 <br>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @author <a href="http://www.tudelft.nl/pknoppers">Peter Knoppers</a>
 * @author <a href="http://www.transport.citg.tudelft.nl">Wouter Schakel</a>
 * @param <E> the type of content of this Set
 */
public class ImmutableHashSet<E> extends ImmutableAbstractSet<E>
{
    /** */
    private static final long serialVersionUID = 20160507L;

    /**
     * @param collection the collection to use for the immutable set.
     */
    public ImmutableHashSet(final Collection<? extends E> collection)
    {
        super(new HashSet<E>(collection), Immutable.COPY);
    }

    /**
     * @param set the set to use for the immutable set.
     * @param copyOrWrap COPY stores a safe, internal copy of the collection; WRAP stores a pointer to the original
     *            collection
     */
    public ImmutableHashSet(final Set<E> set, final Immutable copyOrWrap)
    {
        super(copyOrWrap == Immutable.COPY ? new HashSet<E>(set) : set, copyOrWrap);
    }

    /**
     * @param collection the collection to use for the immutable set.
     */
    public ImmutableHashSet(final ImmutableAbstractCollection<? extends E> collection)
    {
        super(new HashSet<E>(collection.getCollection()), Immutable.COPY);
    }

    /**
     * @param set the set to use for the immutable set.
     * @param copyOrWrap COPY stores a safe, internal copy of the collection; WRAP stores a pointer to the original
     *            collection
     */
    public ImmutableHashSet(final ImmutableAbstractSet<E> set, final Immutable copyOrWrap)
    {
        super(copyOrWrap == Immutable.COPY ? new HashSet<E>(set.getCollection()) : set.getCollection(), copyOrWrap);
    }

    /** {@inheritDoc} */
    @Override
    protected Set<E> getCollection()
    {
        return super.getCollection();
    }

    /** {@inheritDoc} */
    @Override
    public final Set<E> toSet()
    {
        return new HashSet<E>(getCollection());
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("checkstyle:designforextension")
    public String toString()
    {
        Set<E> set = getCollection();
        if (null == set)
        {
            return "ImmutableHashSet []";
        }
        return "ImmutableHashSet [" + set.toString() + "]";
    }

}
