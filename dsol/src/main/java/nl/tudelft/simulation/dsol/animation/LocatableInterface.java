/*
 * @(#) LocatableInterface.java Sep 6, 2003 Copyright (c) 2002-2005 Delft
 * University of Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All
 * rights reserved. This software is proprietary information of Delft University
 * of Technology 
 */
package nl.tudelft.simulation.dsol.animation;

import java.rmi.RemoteException;

import javax.media.j3d.Bounds;

import nl.tudelft.simulation.language.d3.DirectedPoint;

/**
 * The locatable interface enforces knownledge on position.
 * <p>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:36:45 $
 * @author <a href="http://www.peter-jacobs.com/index.htm">Peter Jacobs </a>
 */

public interface LocatableInterface
{
    /**
     * returns the location of an object
     * @return DirectedPoint the location
     * @throws RemoteException on network failure
     */
    DirectedPoint getLocation() throws RemoteException;

    /**
     * returns the bounds of the locatable object. The bounds is the not rotated bounds around [0;0;0]
     * @return BoundingBox include this.getLocation() as center of the box..
     * @throws RemoteException on network failure
     */
    Bounds getBounds() throws RemoteException;

}