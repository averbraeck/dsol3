package nl.tudelft.simulation.naming.listener;

import java.rmi.Remote;
import java.rmi.RemoteException;

import javax.naming.event.NamingEvent;
import javax.naming.event.NamingExceptionEvent;

/**
 * A remoteInterface for the ContextListener.
 * <p>
 * Copyright (c) 2002-2019 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. See for project information <a href="https://simulation.tudelft.nl/" target="_blank">
 * https://simulation.tudelft.nl</a>. The DSOL project is distributed under a three-clause BSD-style license, which can
 * be found at <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public interface RemoteContextListenerInterface extends Remote
{
    /**
     * Called when an object has been added.
     * <p>
     * The binding of the newly added object can be obtained using <tt>evt.getNewBinding()</tt>.
     * @param evt NamingEvent; The nonnull event.
     * @see NamingEvent#OBJECT_ADDED
     * @throws RemoteException on network failure
     */
    public void objectAdded(NamingEvent evt) throws RemoteException;

    /**
     * Called when an object has been removed.
     * <p>
     * The binding of the newly removed object can be obtained using <tt>evt.getOldBinding()</tt>.
     * @param evt NamingEvent; The nonnull event.
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
     * @param evt NamingEvent; The nonnull event.
     * @see NamingEvent#OBJECT_RENAMED
     * @throws RemoteException on network failure
     */
    public void objectRenamed(NamingEvent evt) throws RemoteException;

    /**
     * Called when an object has been changed.
     * <p>
     * The binding of the changed object can be obtained using <tt>evt.getNewBinding()</tt>. Its old binding (before the
     * change) can be obtained using <tt>evt.getOldBinding()</tt>.
     * @param evt NamingEvent; The nonnull naming event.
     * @see NamingEvent#OBJECT_CHANGED
     * @throws RemoteException on network failure.
     */
    public void objectChanged(NamingEvent evt) throws RemoteException;

    /**
     * Called when a naming exception is thrown while attempting to fire a <tt>NamingEvent</tt>.
     * @param evt NamingExceptionEvent; The nonnull event.
     * @throws RemoteException on network exception
     * @throws RemoteException on network failure
     */
    void namingExceptionThrown(NamingExceptionEvent evt) throws RemoteException;
}
