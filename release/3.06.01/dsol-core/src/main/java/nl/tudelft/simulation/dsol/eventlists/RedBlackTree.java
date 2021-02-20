package nl.tudelft.simulation.dsol.eventlists;

import java.util.NoSuchElementException;
import java.util.TreeSet;

import nl.tudelft.simulation.dsol.formalisms.eventscheduling.SimEventInterface;
import nl.tudelft.simulation.dsol.simtime.SimTime;

/**
 * A RedBlackTree implementation of the eventlistInterface. This implementation is based on Java's TreeSet.
 * <p>
 * Copyright (c) 2002-2021 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @param <T> the type of simulation time, e.g. SimTimeCalendarLong or SimTimeDouble or SimTimeDoubleUnit.
 * @since 1.5
 */
public class RedBlackTree<T extends SimTime<?, ?, T>> extends TreeSet<SimEventInterface<T>> implements EventListInterface<T>
{
    /** The default serial version UID for serializable classes. */
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new <code>RedBlackTree</code>.
     */
    public RedBlackTree()
    {
        super();
    }

    /** {@inheritDoc} */
    @Override
    public synchronized SimEventInterface<T> removeFirst()
    {
        SimEventInterface<T> first = this.first();
        this.remove(first);
        return first;
    }

    /** {@inheritDoc} */
    @Override
    public synchronized SimEventInterface<T> removeLast()
    {
        SimEventInterface<T> last = this.last();
        this.remove(last);
        return last;
    }

    /**
     * we re-implemented the first method. Instead of throwing exceptions if the tree is empty, we return a null value.
     * @see java.util.TreeSet#first()
     * @return the first SimEvent in the tree.
     */
    @Override
    public SimEventInterface<T> first()
    {
        try
        {
            return super.first();
        }
        catch (NoSuchElementException noSuchElementException)
        {
            return null;
        }
    }

    /**
     * we re-implemented the last method. Instead of throwing exceptions if the tree is empty, we return a null value.
     * @see java.util.TreeSet#last()
     * @return the last SimEvent in the tree.
     */
    @Override
    public SimEventInterface<T> last()
    {
        try
        {
            return super.first();
        }
        catch (NoSuchElementException noSuchElementException)
        {
            return null;
        }
    }
}
