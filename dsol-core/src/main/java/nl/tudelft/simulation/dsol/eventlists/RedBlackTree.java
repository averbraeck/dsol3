package nl.tudelft.simulation.dsol.eventlists;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.TreeSet;

import nl.tudelft.simulation.dsol.formalisms.eventscheduling.SimEventInterface;
import nl.tudelft.simulation.dsol.simtime.SimTime;

/**
 * A RedBlackTree implementation of the eventlistInterface. This implementation is based on Java's TreeSet. This implementation
 * embeds the data structure in the event list instead of extending it (extension has the chance that future implementations can
 * break the EventList, and that the user can use functions that do not belong to an EventList).
 * <p>
 * Copyright (c) 2002-2022 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraec</a>
 * @param <T> the type of simulation time, e.g. SimTimeCalendarLong or SimTimeDouble or SimTimeDoubleUnit.
 * @since 1.5
 */
public class RedBlackTree<T extends SimTime<?, ?, T>> implements EventListInterface<T>
{
    /** The default serial version UID for serializable classes. */
    private static final long serialVersionUID = 1L;

    /** The embedded event list. */
    private TreeSet<SimEventInterface<T>> eventList;

    /**
     * Constructs a new <code>RedBlackTree</code>.
     */
    public RedBlackTree()
    {
        this.eventList = new TreeSet<>();
    }

    /** {@inheritDoc} */
    @Override
    public synchronized SimEventInterface<T> removeFirst()
    {
        SimEventInterface<T> first = first();
        if (first != null)
        {
            this.eventList.remove(first);
        }
        return first;
    }

    /** {@inheritDoc} */
    @Override
    public SimEventInterface<T> first()
    {
        try
        {
            return this.eventList.first();
        }
        catch (NoSuchElementException noSuchElementException)
        {
            return null;
        }
    }

    /** {@inheritDoc} */
    @Override
    public void add(final SimEventInterface<T> event)
    {
        this.eventList.add(event);
    }

    /** {@inheritDoc} */
    @Override
    public boolean contains(final SimEventInterface<T> event)
    {
        return this.eventList.contains(event);
    }

    /** {@inheritDoc} */
    @Override
    public void clear()
    {
        this.eventList.clear();
    }

    /** {@inheritDoc} */
    @Override
    public boolean isEmpty()
    {
        return this.eventList.isEmpty();
    }

    /** {@inheritDoc} */
    @Override
    public Iterator<SimEventInterface<T>> iterator()
    {
        return this.eventList.iterator();
    }

    /** {@inheritDoc} */
    @Override
    public boolean remove(final SimEventInterface<T> event)
    {
        return this.eventList.remove(event);
    }

    /** {@inheritDoc} */
    @Override
    public int size()
    {
        return this.eventList.size();
    }

}
