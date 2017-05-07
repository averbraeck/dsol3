package nl.tudelft.simulation.examples.dsol.animation;

import java.rmi.RemoteException;

import nl.tudelft.simulation.dsol.DSOLModel;
import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;
import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;

/**
 * @author peter
 */
public class BallModel implements DSOLModel.TimeDouble
{
    /** The default serial version UID for serializable classes. */
    private static final long serialVersionUID = 1L;

    /** the simulator. */
    private DEVSSimulatorInterface.TimeDouble simulator;

    /**
     * constructs a new BallModel.
     */
    public BallModel()
    {
        super();
    }

    /** {@inheritDoc} */
    @Override
    public void constructModel(final SimulatorInterface<Double, Double, SimTimeDouble> sim)
            throws SimRuntimeException, RemoteException
    {
        this.simulator = (DEVSSimulatorInterface.TimeDouble) sim;
        for (int i = 0; i < 10; i++)
        {
            new DiscreteBall(this.simulator);
        }
    }

    /** {@inheritDoc} */
    @Override
    public SimulatorInterface.TimeDouble getSimulator()
    {
        return this.simulator;
    }
}
