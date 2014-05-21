/*
 * @(#)ResourceRequestorInterface.java Feb 1, 2003 Copyright (c) 2002-2005 Delft
 * University of Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All
 * rights reserved. This software is proprietary information of Delft University
 * of Technology 
 */
package nl.tudelft.simulation.dsol.formalisms;

import java.rmi.RemoteException;

/**
 * This interface provides a callback method to the resource. Whenever resource is available this method is invoked on
 * the requestor.
 * <p>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl"> www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="http://www.peter-jacobs.com">Peter Jacobs </a>
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:36:45 $
 * @since 1.5
 */
public interface ResourceRequestorInterface
{
    /**
     * receive the requested resource
     * @param requestedCapacity reflects the amount requested
     * @param resource the requested resource
     * @throws RemoteException on network failure
     */
    void receiveRequestedResource(final double requestedCapacity, final Resource resource) throws RemoteException;
}