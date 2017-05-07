package nl.tudelft.simulation.examples.dsol.dess;

import java.rmi.RemoteException;

import nl.tudelft.simulation.dsol.formalisms.dess.DifferentialEquation;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simulators.DESSSimulatorInterface;

/**
 * @author peter
 */
public class Speed extends DifferentialEquation<Double, Double, SimTimeDouble>
{
    /** */
    private static final long serialVersionUID = 1L;

    /**
     * constructs a new Speed.
     * @param simulator the simulator
     * @throws RemoteException on network failureS
     */
    public Speed(final DESSSimulatorInterface.TimeDouble simulator) throws RemoteException
    {
        super(simulator);
    }

    /** {@inheritDoc} */
    @Override
    public double[] dy(final double x, final double[] y)
    {
        return new double[]{0.5};
    }
}
