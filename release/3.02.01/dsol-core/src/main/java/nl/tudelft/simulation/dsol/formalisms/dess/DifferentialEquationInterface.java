package nl.tudelft.simulation.dsol.formalisms.dess;

import nl.tudelft.simulation.event.EventType;

/**
 * The Differential equation interface.
 * <p>
 * (c) 2002-2018 <a href="https://simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="https://simulation.tudelft.nl"> www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @version $Revision: 1.1 $ $Date: 2007/01/07 04:55:54 $
 * @since 1.5
 */
public interface DifferentialEquationInterface extends nl.tudelft.simulation.jstats.ode.DifferentialEquationInterface
{
    /** VALUE_CHANGED_EVENT is firedd on value changes. */
    EventType[] VALUE_CHANGED_EVENT = new EventType[30];

    /** FUNCTION_CHANGED_EVENT is firedd on function changes. */
    EventType FUNCTION_CHANGED_EVENT = new EventType("FUNCTION_CHANGED_EVENT");
}
