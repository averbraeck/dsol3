package nl.tudelft.simulation.dsol.formalisms.eventscheduling;

import java.io.Serializable;

import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.simtime.SimTime;

/**
 * A SimEventInterface embodies the envolope in the scheduled method invocation information is stored.
 * <p>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl"> www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @version $Revision: 1.1 $ $Date: 2010/08/10 11:36:43 $
 * @param <T> the type of simulation time, e.g. SimTimeCalendarLong or SimTimeDouble or SimTimeDoubleUnit.
 * @since 1.5
 */
public interface SimEventInterface<T extends SimTime<?, ?, T>> extends Serializable
{
    /** MAX_PRIORITY is a constant reflecting the maximum priority */
    short MAX_PRIORITY = 10;

    /** NORMAL_PRIORITY is a constant reflecting the normal priority */
    short NORMAL_PRIORITY = 5;

    /** MIN_PRIORITY is a constant reflecting the minimal priority */
    short MIN_PRIORITY = 1;

    /**
     * executes the simEvent.
     * @throws SimRuntimeException on execution failure
     */
    void execute() throws SimRuntimeException;

    /**
     * @return the scheduled execution time of a simulation event.
     */
    T getAbsoluteExecutionTime();

    /**
     * @return The priority of a simulation event. The priorities are programmed according to the Java thread priority.
     *         Use 10 (MAX_PRIORITY), -9, .. , 5 (NORMAL_PRIORITY), 1(MIN_PRIORITY)
     */
    short getPriority();
}
