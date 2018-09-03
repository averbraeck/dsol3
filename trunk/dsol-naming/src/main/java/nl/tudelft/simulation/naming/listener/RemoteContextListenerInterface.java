package nl.tudelft.simulation.naming.listener;

import java.rmi.Remote;
import java.rmi.RemoteException;

import javax.naming.event.NamingEvent;
import javax.naming.event.NamingExceptionEvent;

/**
 * A remoteInterface for the ContextListener.
 * <p>
 * (c) 2002-2018 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @version 1.2 Apr 14, 2004
 * @since 1.5
 */
public interface RemoteContextListenerInterface extends Remote
{
    /**
     * Called when an object has been added.
     * <p>
     * The binding of the newly added object can be obtained using <tt>evt.getNewBinding()</tt>.
     * @param evt The nonnull event.
     * @see NamingEvent#OBJECT_ADDED
     * @throws RemoteException on network failure
     */
    public void objectAdded(NamingEvent evt) throws RemoteException;

    /**
     * Called when an object has been removed.
     * <p>
     * The binding of the newly removed object can be obtained using <tt>evt.getOldBinding()</tt>.
     * @param evt The nonnull event.
     * @see NamingEvent#OBJECT_REMOVED
     * @throws RemoteException on network failure
     */
    public void objectRemoved(NamingEvent evt) throws RemoteException;

    /**
     * Called when an object has been renamed.
     * <p>
     * The binding of the renamed object can be obtained using <tt>evt.getNewBinding()</tt>. Its old binding (before the
     * rename) can be obtained using <tt>evt.getOldBinding()</tt>. One of these may be null if the old/new binding was
     * outside the scope in which the listener has registered interest.
     * @param evt The nonnull event.
     * @see NamingEvent#OBJECT_RENAMED
     * @throws RemoteException on network failure
     */
    public void objectRenamed(NamingEvent evt) throws RemoteException;

    /**
     * Called when an object has been changed.
     * <p>
     * The binding of the changed object can be obtained using <tt>evt.getNewBinding()</tt>. Its old binding (before the
     * change) can be obtained using <tt>evt.getOldBinding()</tt>.
     * @param evt The nonnull naming event.
     * @see NamingEvent#OBJECT_CHANGED
     * @throws RemoteException on network failure.
     */
    public void objectChanged(NamingEvent evt) throws RemoteException;

    /**
     * Called when a naming exception is thrown while attempting to fire a <tt>NamingEvent</tt>.
     * @param evt The nonnull event.
     * @throws RemoteException on network exception
     * @throws RemoteException on network failure
     */
    void namingExceptionThrown(NamingExceptionEvent evt) throws RemoteException;
}
