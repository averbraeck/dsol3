package nl.tudelft.simulation.examples.dsol.animation;

import java.rmi.RemoteException;

import nl.tudelft.simulation.dsol.DSOLModel;
import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.simulators.DESSSimulatorInterface;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;
import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;
import nl.tudelft.simulation.examples.dsol.animation.discrete.Ball;

/**
 * @author peter
 */
public class BallModel implements DSOLModel
{
    /** The default serial version UID for serializable classes. */
    private static final long serialVersionUID = 1L;

    /** the simulator. */
    private SimulatorInterface.TimeDouble simulator;

    /**
     * constructs a new BallModel.
     */
    public BallModel()
    {
        super();
    }

    /** {@inheritDoc} */
    @Override
    public void constructModel(final SimulatorInterface.TimeDouble sim) throws SimRuntimeException, RemoteException
    {
        this.simulator = sim;
        for (int i = 0; i < 10; i++)
        {
            new Ball((DEVSSimulatorInterface) sim);
            new nl.tudelft.simulation.examples.dsol.animation.continuous.Ball((DESSSimulatorInterface) sim);
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
