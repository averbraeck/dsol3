/*
 * RemoteEventListener.java Created @ Mar 24, 2004 Copyright (c) 2002-2005 Delft
 * University of Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All
 * rights reserved. This software is proprietary information of Delft University
 * of Technology 
 */

package nl.tudelft.simulation.event.remote;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Logger;

import nl.tudelft.simulation.event.EventInterface;
import nl.tudelft.simulation.event.EventListenerInterface;

/**
 * The RemoteEventListener class embodies a remoteEventListener.
 * <p>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands.
 * <p>
 * See for project information <a href="http://www.simulation.tudelft.nl/dsol/event">www.simulation.tudelft.nl/event
 * </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:38:12 $
 * @author <a href="mailto:stijnh@tbm.tudelft.nl">Stijn-Pieter van Houten </a>
 */
public class RemoteEventListener implements RemoteEventListenerInterface
{

    /** the owner of the remote listener */
    private EventListenerInterface owner = null;

    /**
     * Constructs a new RemoteListener.
     * @param owner The owner of the listener.
     */
    public RemoteEventListener(final EventListenerInterface owner)
    {
        super();
        try
        {
            UnicastRemoteObject.exportObject(this);
        }
        catch (RemoteException remoteException)
        {
            Logger.getLogger("nl.tudelft.simulation.event").severe(remoteException.getMessage());
        }

        this.owner = owner;
    }

    /**
     * @see nl.tudelft.simulation.event.EventListenerInterface#notify(nl.tudelft.simulation.event.EventInterface)
     */
    public void notify(final EventInterface event) throws RemoteException
    {
        this.owner.notify(event);
    }
}