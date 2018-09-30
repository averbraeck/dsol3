package nl.tudelft.simulation.immutablecollections;

/**
 * Indicate whether the immutable collection contains a COPY of the collection (neither changeable by the user of the
 * immutable collection, nor by anyone holding a pointer to the original collection), or a WRAP for the original
 * collection (not changeable by the user of the immutable collection, but can be changed by anyone holding a pointer to
 * the original collection that is wrapped).
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
 */
public enum Immutable
{
    /**
     * A copy is neither changeable by the user of the immutable collection, nor by anyone holding a pointer to the
     * original collection that is put into the immutable collection.
     */
    COPY,

    /**
     * A wrapped immutable collection is not changeable by the user of the immutable collection, but can be changed by
     * anyone holding a pointer to the original collection that is wrapped.
     */
    WRAP;

    /**
     * @return whether the immutable is a COPY
     */
    public boolean isCopy()
    {
        return this.equals(COPY);
    }

    /**
     * @return whether the immutable is a WRAP
     */
    public boolean isWrap()
    {
        return this.equals(WRAP);
    }

}
