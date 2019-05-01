package nl.tudelft.simulation.dsol.animation;

import java.rmi.RemoteException;

import javax.media.j3d.Bounds;

import nl.tudelft.simulation.language.d3.DirectedPoint;

/**
 * The locatable interface enforces knowledge on position.
 * <p>
 * Copyright (c) 2002-2019 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 */
public interface Locatable
{
    /**
     * returns the location of an object.
     * @return DirectedPoint the location
     * @throws RemoteException on network failure
     */
    DirectedPoint getLocation() throws RemoteException;

    /**
     * returns the bounds of the locatable object. The bounds is the not rotated bounds around [0;0;0]
     * @return BoundingBox include this.getLocation() as center of the box.
     * @throws RemoteException on network failure
     */
    Bounds getBounds() throws RemoteException;
}
