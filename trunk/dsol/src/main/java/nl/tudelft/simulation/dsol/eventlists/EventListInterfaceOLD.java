/*
 * Created on Mar 28, 2004 Copyright (c) 2002-2005 Delft University of
 * Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved.
 * This software is proprietary information of Delft University of Technology
 * 
 */
package nl.tudelft.simulation.dsol.eventlists;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;

import nl.tudelft.simulation.dsol.formalisms.eventscheduling.SimEventInterface;

/**
 * The EventListInterface defines the required methods for discrete event lists. A number of competitive algoritms can
 * be used to implement such eventlist. Among these implementations are the Red-Black, the SplayTree, and others.
 * <p>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl"> www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="http://www.peter-jacobs.com">Peter Jacobs </a>
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:36:45 $
 * @since 1.5
 */
public interface EventListInterfaceOLD extends Serializable
{
    /**
     * adds an event to the eventlist
     * @param event the event to add
     * @return true whenever the event was not already scheduled.
     */
    boolean add(final SimEventInterface event);

    /**
     * adds a collection of events to the tree
     * @param collection the collection
     * @return true whenever the collection was sucessfully added.
     */
    boolean addAll(final Collection<?> collection);

    /**
     * clears the eventlist by removing all its scheduled events.
     */
    void clear();

    /**
     * returns whether event is an entry of the eventlist
     * @param event the event
     * @return true if event in tree; otherwise false.
     */
    boolean contains(final SimEventInterface event);

    /**
     * returns whether the collection is in the list.
     * @param collection the collection to test
     * @return true if event in tree; otherwise false.
     */
    boolean containsAll(final Collection<?> collection);

    /**
     * returns the first scheduled event
     * @return first scheduled event.
     */
    SimEventInterface first();

    /**
     * returns whether the eventlist is empty
     * @return true if empty.
     */
    boolean isEmpty();

    /**
     * returns the iterator for this eventlist
     * @return the iterator
     */
    Iterator<?> iterator();

    /**
     * returns the last scheduled event
     * @return last scheduled event.
     */
    SimEventInterface last();

    /**
     * removes the event from this tree
     * @param event the event to be removed
     * @return true if the event was in the tree and succesfully removed.
     */
    boolean remove(final SimEventInterface event);

    /**
     * removes a collection of events from this tree
     * @param collection the colleciton
     * @return true if the event was in the tree and succesfully removed.
     */
    boolean removeAll(final Collection<?> collection);

    /**
     * removes the first event from the eventlist.
     * @return the first event
     */
    SimEventInterface removeFirst();

    /**
     * removes the last event from the eventlist.
     * @return the last event
     */
    SimEventInterface removeLast();

    /**
     * returns the number of scheduled events
     * @return the number of scheduled events.
     */
    int size();

    /**
     * returns the eventlist as array of simevents
     * @return the eventlist as array of simevents.
     */
    SimEventInterface[] toArray();
}