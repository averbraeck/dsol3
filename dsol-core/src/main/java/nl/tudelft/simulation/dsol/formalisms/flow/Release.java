package nl.tudelft.simulation.dsol.formalisms.flow;

import java.rmi.RemoteException;

import nl.tudelft.simulation.dsol.formalisms.Resource;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;
import nl.tudelft.simulation.logger.Logger;

/**
 * The release station releases a given quantity of a claimed resource. <br>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl"> www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:36:44 $
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>, <a
 *         href="mailto:a.verbraeck@tudelft.nl">Alexander Verbraeck </a>
 */
public class Release extends Station
{

    /**
     * resource refers to the resource released
     */
    private Resource resource;

    /** amount defines the amount to be released. */
    private double amount = 1.0;

    /**
     * Constructor for Release.
     * @param simulator on which is scheduled
     * @param resource which is released
     */
    public Release(final DEVSSimulatorInterface simulator, final Resource resource)
    {
        this(simulator, resource, 1.0);
    }

    /**
     * Constructor for Release.
     * @param simulator on which is scheduled
     * @param resource which is released
     * @param amount of resource which is released
     */
    public Release(final DEVSSimulatorInterface simulator, final Resource resource, final double amount)
    {
        super(simulator);
        this.resource = resource;
        this.amount = amount;
    }

    /** {@inheritDoc} */
    @Override
    public synchronized void receiveObject(final Object object) throws RemoteException
    {
        super.receiveObject(object);
        try
        {
            this.resource.releaseCapacity(this.amount);
            this.releaseObject(object);
        }
        catch (Exception exception)
        {
            Logger.warning(this, "receiveObject", exception);
        }
    }
}
