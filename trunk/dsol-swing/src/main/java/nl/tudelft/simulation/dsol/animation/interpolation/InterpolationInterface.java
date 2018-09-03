package nl.tudelft.simulation.dsol.animation.interpolation;

import java.rmi.RemoteException;

import nl.tudelft.simulation.language.d3.DirectedPoint;

/**
 * A InterpolationInterface <br>
 * (c) 2002-2018 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @version $Revision: 1.1 $ $Date: 2010/08/10 11:37:25 $
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 */
public interface InterpolationInterface
{
    /**
     * returns the current location
     * @param time the current time
     * @return DirectedPoint the current location
     * @throws RemoteException on network failure
     */
    DirectedPoint getLocation(final double time) throws RemoteException;
}
