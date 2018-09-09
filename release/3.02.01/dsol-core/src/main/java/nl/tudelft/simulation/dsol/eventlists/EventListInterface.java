package nl.tudelft.simulation.dsol.eventlists;

import java.util.SortedSet;

import nl.tudelft.simulation.dsol.formalisms.eventscheduling.SimEventInterface;
import nl.tudelft.simulation.dsol.simtime.SimTime;

/**
 * The EventListInterface defines the required methods for discrete event lists. A number of competitive algoritms can
 * be used to implement such eventlist. Among these implementations are the Red-Black, the SplayTree, and others.
 * <p>
 * (c) 2002-2018 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl"> www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:36:44 $
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
