package nl.tudelft.simulation.dsol.eventlists;

import java.util.NoSuchElementException;
import java.util.SortedSet;

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
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:36:44 $
 * @since 1.5
 */
public interface EventListInterface extends SortedSet<SimEventInterface>
{
    /**
     * Returns the first (lowest) element currently in this sorted set.
     * @return the first (lowest) element currently in this sorted set.
     * @throws NoSuchElementException sorted set is empty.
     */
    SimEventInterface removeFirst();

    /**
     * Returns the last (highest) element currently in this sorted set.
     * @return the last (highest) element currently in this sorted set.
     * @throws NoSuchElementException sorted set is empty.
     */
    SimEventInterface removeLast();
}
