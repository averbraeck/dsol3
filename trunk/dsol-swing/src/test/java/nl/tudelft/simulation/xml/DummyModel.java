package nl.tudelft.simulation.xml;

import java.rmi.RemoteException;

import nl.tudelft.simulation.dsol.ModelInterface;
import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;

/**
 * A DummyTestModel
 * <p>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @version 1.2 Sep 28, 2004
 * @since 1.5
 */
public class DummyModel implements ModelInterface
{
    /**
     * constructs a new DummyModel.
     */
    public DummyModel()
    {
        super();
    }

    /** {@inheritDoc} */
    public void constructModel(final SimulatorInterface simulator)
    {
        // We have constructed a model.
    }

    public SimulatorInterface getSimulator() throws RemoteException
    {
        return null;
    }
}
