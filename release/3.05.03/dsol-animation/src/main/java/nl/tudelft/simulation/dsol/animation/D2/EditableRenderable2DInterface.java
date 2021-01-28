package nl.tudelft.simulation.dsol.animation.D2;

import java.rmi.RemoteException;

import nl.tudelft.simulation.dsol.animation.Locatable;

/**
 * This interface provides the functionality that editable animation objects must implement.
 * <p>
 * Copyright (c) 2002-2021 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @since 1.5
 * @param <T> the type of Locatable.
 */
public interface EditableRenderable2DInterface<T extends Locatable> extends Renderable2DInterface<T>
{
    /**
     * Returns whether this shape is closed or open. For example an area is a closed shape while a trajectory is open (has
     * ends).
     * @return true or false
     * @throws RemoteException RemoteException
     */
    boolean isClosedShape() throws RemoteException;

    /**
     * Is the user allowed to move this editable?
     * @return True or false
     * @throws RemoteException RemoteException
     */
    boolean allowMove() throws RemoteException;

    /**
     * Is the user allowed to rotate this editable?
     * @return True or false
     * @throws RemoteException RemoteException
     */
    boolean allowRotate() throws RemoteException;

    /**
     * Is the user allowed to scale this editable?
     * @return True or false
     * @throws RemoteException RemoteException
     */
    boolean allowScale() throws RemoteException;

    /**
     * Is the user allowed to edit individual points of this editable?
     * @return True or false
     * @throws RemoteException RemoteException
     */
    boolean allowEditPoints() throws RemoteException;

    /**
     * Is the user allowed to delete this object?
     * @return True or false
     * @throws RemoteException RemoteException
     */
    boolean allowDelete() throws RemoteException;

    /**
     * Is the user allowed to add or delete points of this editable?
     * @return True or false
     * @throws RemoteException RemoteException
     */
    boolean allowAddOrDeletePoints() throws RemoteException;

    /**
     * Get the maximum allowed number of points for this editable
     * @return Maximum number of points
     * @throws RemoteException RemoteException
     */
    int getMaxNumberOfPoints() throws RemoteException;

    /**
     * Get the minimum allowed number of points for this editable
     * @return Minimum number of points
     * @throws RemoteException RemoteException
     */
    int getMinNumberOfPoints() throws RemoteException;
}
