/*
 * @(#) RemoteContextListener.java Apr 14, 2004 Copyright (c) 2002-2005 Delft
 * University of Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All
 * rights reserved. This software is proprietary information of Delft University
 * of Technology 
 */
package nl.tudelft.simulation.naming.listener;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import javax.naming.event.NamingEvent;
import javax.naming.event.NamingExceptionEvent;

/**
 * A RemoteContextListener.
 * <p>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft
 * University of Technology </a>, the Netherlands. <br>
 * See for project information <a
 * href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser
 * General Public License (LGPL) </a>, no warranty.
 * 
 * @author <a href="http://www.peter-jacobs.com/index.htm">Peter Jacobs </a>
 * @version 1.2 Apr 14, 2004
 * @since 1.5
 */
public class RemoteContextListener extends UnicastRemoteObject implements
        RemoteContextListenerInterface
{
    /** The default serial version UID for serializable classes */
    private static final long serialVersionUID = 1L;
    
    /** the listener */
    private ContextListenerInterface listener = null;

    /**
     * constructs a new RemoteContextListener
     * 
     * @param listener the target.
     * @throws RemoteException on network failure.
     */
    public RemoteContextListener(final ContextListenerInterface listener)
            throws RemoteException
    {
        super();
        this.listener = listener;
    }

    /**
     * @see nl.tudelft.simulation.naming.listener.RemoteContextListenerInterface
     *      #objectChanged(javax.naming.event.NamingEvent)
     */
    public void objectChanged(NamingEvent evt)
    {
        this.listener.objectChanged(evt);
    }

    /**
     * @see nl.tudelft.simulation.naming.listener.RemoteContextListenerInterface#objectAdded(javax.naming.event.NamingEvent)
     */
    public void objectAdded(NamingEvent evt)
    {
        this.listener.objectAdded(evt);
    }

    /**
     * @see nl.tudelft.simulation.naming.listener.RemoteContextListenerInterface#objectRemoved(javax.naming.event.NamingEvent)
     */
    public void objectRemoved(NamingEvent evt)
    {
        this.listener.objectRemoved(evt);
    }

    /**
     * @see nl.tudelft.simulation.naming.listener.RemoteContextListenerInterface#objectRenamed(javax.naming.event.NamingEvent)
     */
    public void objectRenamed(NamingEvent evt)
    {
        this.listener.objectRemoved(evt);
    }

    /**
     * @see nl.tudelft.simulation.naming.listener.RemoteContextListenerInterface
     *      #namingExceptionThrown(javax.naming.event.NamingExceptionEvent)
     */
    public void namingExceptionThrown(NamingExceptionEvent evt)
    {
        this.listener.namingExceptionThrown(evt);
    }
}