package nl.tudelft.simulation.dsol.tutorial.section44;

import java.rmi.RemoteException;

import nl.tudelft.simulation.dsol.DSOLModel;
import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.simulators.DESSSimulatorInterface;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;
import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;
import nl.tudelft.simulation.language.d3.DirectedPoint;

/**
 * BoatModel, the ball example in 3D <br>
 * (c) copyright 2002-2016 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @version 1.0 10.05.2004 <br>
 * @author <a href="http://www.tbm.tudelft.nl/webstaf/royc/index.htm">Roy Chin </a>
 */
public class Model implements DSOLModel
{
    /** The default serial version UID for serializable classes. */
    private static final long serialVersionUID = 1L;

    /** the simulator. */
    private SimulatorInterface.TimeDouble simulator;

    /**
     * Constructs new BoatModel
     */
    public Model()
    {
        super();
    }

    /** {@inheritDoc} */
    @Override
    public void constructModel(final SimulatorInterface.TimeDouble simulator) throws RemoteException, SimRuntimeException
    {
        this.simulator = simulator;
        new World(new DirectedPoint(0, 0, -5.5), simulator);
        for (int i = 0; i < 10; i++)
        {
            new DiscreteBall((DEVSSimulatorInterface) simulator);
            new ContinuousBall((DESSSimulatorInterface) simulator);
        }
    }

    /**
     * @return the simulator
     */
    public SimulatorInterface.TimeDouble getSimulator()
    {
        return this.simulator;
    }
}
