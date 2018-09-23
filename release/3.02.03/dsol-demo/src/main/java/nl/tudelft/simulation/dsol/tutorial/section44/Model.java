package nl.tudelft.simulation.dsol.tutorial.section44;

import java.rmi.RemoteException;

import javax.naming.NamingException;

import nl.tudelft.simulation.dsol.DSOLModel;
import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.logger.SimLogger;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simulators.DEVDESSSimulatorInterface;
import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;
import nl.tudelft.simulation.language.d3.DirectedPoint;

/**
 * BallModel, the ball example in 3D <br>
 * copyright (c) 2002-2018 <a href="https://simulation.tudelft.nl">Delft University of Technology </a>, the Netherlands.
 * <br>
 * See for project information <a href="https://simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @version 1.0 10.05.2004 <br>
 * @author <a href="http://www.tbm.tudelft.nl/webstaf/royc/index.htm">Roy Chin </a>
 */
public class Model implements DSOLModel<Double, Double, SimTimeDouble>
{
    /** The default serial version UID for serializable classes. */
    private static final long serialVersionUID = 1L;

    /** the simulator. */
    private DEVDESSSimulatorInterface.TimeDouble simulator;

    /**
     * Constructs new BallModel.
     */
    public Model()
    {
        super();
    }

    /** {@inheritDoc} */
    @Override
    public void constructModel(final SimulatorInterface<Double, Double, SimTimeDouble> pSimulator)
            throws SimRuntimeException
    {
        this.simulator = (DEVDESSSimulatorInterface.TimeDouble) pSimulator;
        new World(new DirectedPoint(0, 0, -5.5), this.simulator);
        for (int i = 0; i < 10; i++)
        {
            try
            {
                new DiscreteBall(this.simulator);
                new ContinuousBall(this.simulator);
            }
            catch (NamingException | RemoteException exception)
            {
                SimLogger.always().error(exception);
            }
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
