/*
 * @(#)EventListenerInterface April 4, 2003 Copyright (c) 2002-2005 Delft
 * University of Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All
 * rights reserved. This software is proprietary information of Delft University
 * of Technology 
 */
package nl.tudelft.simulation.event;

import java.rmi.RemoteException;
import java.util.EventListener;

/**
 * The EventListenerInterface creates a callback method for publishers to inform their clients.
 * <p>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands.
 * <p>
 * See for project information <a href="http://www.simulation.tudelft.nl/dsol/event">www.simulation.tudelft.nl/event
 * </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty
 * @author <a href="http://www.peter-jacobs.com">Peter Jacobs </a>
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:38:11 $
 * @since 1.1
 */
public interface EventListenerInterface extends EventListener
{
    /**
     * notifies the eventlistener of an event. This operation forms the callback method of the asynchronous
     * communication prototcol expressed in the event package.
     * @param event the event which is sent to the listener.
     * @throws RemoteException If a network connection failure occurs.
     */
    void notify(EventInterface event) throws RemoteException;
}