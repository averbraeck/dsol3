package nl.tudelft.simulation.naming.listener;

import javax.naming.event.NamespaceChangeListener;
import javax.naming.event.ObjectChangeListener;

/**
 * The interface to implement for namespace and object change listeners.
 * <p>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @version 1.2 Apr 16, 2004
 * @since 1.5
 */
public interface ContextListenerInterface extends ObjectChangeListener, NamespaceChangeListener
{
    // We merely extend and provide one required interface
}
