package nl.tudelft.simulation.examples.dsol.dess;

import java.rmi.RemoteException;

import nl.tudelft.simulation.dsol.DSOLModel;
import nl.tudelft.simulation.dsol.formalisms.dess.DifferentialEquationInterface;
import nl.tudelft.simulation.dsol.simulators.DESSSimulatorInterface;
import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;
import nl.tudelft.simulation.dsol.statistics.Persistent;

/**
 * @author peter
 */
public class DESSModel implements DSOLModel
{
    /** The default serial version UID for serializable classes. */
    private static final long serialVersionUID = 1L;

    /** the simulator. */
    private SimulatorInterface.TimeDouble simulator;

    /**
     * constructs a new DESSModel.
     */
    public DESSModel()
    {
        super();
    }

    /** {@inheritDoc} */
    @Override
    public void constructModel(final SimulatorInterface.TimeDouble simulator) throws RemoteException
    {
        this.simulator = simulator;
        Distance distance = new Distance((DESSSimulatorInterface) simulator);

        Persistent persistent =
                new Persistent("persistent on distance", simulator, distance,
                        DifferentialEquationInterface.VALUE_CHANGED_EVENT[0]);
        XYChart chart = new XYChart(simulator, "xyplot of distance");
        chart.add(persistent);
    }

    /**
     * @return the simulator
     */
    public SimulatorInterface.TimeDouble getSimulator()
    {
        return this.simulator;
    }
}
