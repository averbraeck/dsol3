package nl.tudelft.simulation.examples.dsol.dess;

import java.rmi.RemoteException;

import nl.tudelft.simulation.dsol.formalisms.dess.DifferentialEquation;
import nl.tudelft.simulation.dsol.simulators.DESSSimulatorInterface;

/**
 * @author peter
 */
public class Distance extends DifferentialEquation
{
    /** the speed. */
    private Speed speed = null;

    /**
     * constructs a new Distance.
     * @param simulator the simulator
     * @throws RemoteException on network error
     */
    public Distance(final DESSSimulatorInterface.TimeDouble simulator) throws RemoteException
    {
        super(simulator);
        this.speed = new Speed(simulator);
        this.speed.initialize(10, new double[]{0});
        this.initialize(10, new double[]{0});
    }

    /** {@inheritDoc} */
    @Override
    public double[] dy(double x, double[] y)
    {
        return this.speed.y(x);
    }
}
