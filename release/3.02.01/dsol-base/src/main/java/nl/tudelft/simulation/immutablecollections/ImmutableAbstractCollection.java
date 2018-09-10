package nl.tudelft.simulation.immutablecollections;

import java.util.Collection;

import nl.tudelft.simulation.language.Throw;

/**
 * An abstract base class for an immutable wrapper for a Set.
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
public abstract class ImmutableAbstractCollection<E> implements ImmutableCollection<E>
{
    /** */
    private static final long serialVersionUID = 20180908L;

    /** COPY stores a safe, internal copy of the collection; WRAP stores a pointer to the original collection. */
    protected final Immutable copyOrWrap;

    /**
     * Construct an abstract immutable collection.
     * @param copyOrWrap indicates whether the immutable is a copy or a wrap
     */
    public ImmutableAbstractCollection(final Immutable copyOrWrap)
    {
        Throw.whenNull(copyOrWrap, "the copyOrWrap argument should be Immutable.COPY or Immutable.WRAP");
        this.copyOrWrap = copyOrWrap;
    }

    /**
     * Returns the underlying collection of this immutable collection. In case of Immutable.WRAP, this will be the
     * original collection. In case of IMMUTABLE.COPY, this will be the internally stored (mutable) copy of the
     * collection.
     * @return the underlying collection of this immutable collection.
     */
    protected abstract Collection<E> getCollection();
}
