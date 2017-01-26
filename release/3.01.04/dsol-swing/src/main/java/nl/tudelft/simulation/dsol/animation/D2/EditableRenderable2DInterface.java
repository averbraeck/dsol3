package nl.tudelft.simulation.dsol.animation.D2;

import java.rmi.RemoteException;

import nl.tudelft.simulation.dsol.animation.Locatable;

/**
 * This interface provides the functionality that editable animation objects must implement.
 * <p>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @version $Revision: 1.1 $ $Date: 2010/08/10 11:37:20 $
 * @since 1.5
 * @param <T> the type of Locatable.
 */
public interface EditableRenderable2DInterface<T extends Locatable> extends Renderable2DInterface<T>
{
    /**
     * Returns whether this shape is closed or open. For example an area is a closed shape while a trajectory is open
     * (has ends).
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
