package nl.tudelft.simulation.dsol.eventlists;

import java.util.SortedSet;

import nl.tudelft.simulation.dsol.formalisms.eventscheduling.SimEventInterface;
import nl.tudelft.simulation.dsol.simtime.SimTime;

/**
 * The EventListInterface defines the required methods for discrete event lists. A number of competitive algoritms can be used
 * to implement such eventlist. Among these implementations are the Red-Black, the SplayTree, and others.
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
public interface EventListInterface<T extends SimTime<?, ?, T>> extends SortedSet<SimEventInterface<T>>
{
    /**
     * Returns the first (lowest) element currently in this sorted set.
     * @return the first (lowest) element currently in this sorted set.
     */
    SimEventInterface<T> removeFirst();

    /**
     * Returns the last (highest) element currently in this sorted set.
     * @return the last (highest) element currently in this sorted set.
     */
    SimEventInterface<T> removeLast();
}
