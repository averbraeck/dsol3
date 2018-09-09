package nl.tudelft.simulation.dsol.serialize;

import java.rmi.RemoteException;

import nl.tudelft.simulation.dsol.simulators.DESSSimulatorInterface;
import nl.tudelft.simulation.jstats.ode.integrators.NumericalIntegrator;

/**
 * The histogram specifies a histogram chart for the DSOL framework.
 * <p>
 * copyright (c) 2004-2018 <a href="https://simulation.tudelft.nl/dsol/">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="https://simulation.tudelft.nl/dsol/"> www.simulation.tudelft.nl/dsol </a>
 * <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs"> Peter Jacobs </a>
 * @version 1.0 Dec 7, 2004
 * @since 1.5
 */
public class DifferentialEquation extends nl.tudelft.simulation.dsol.formalisms.dess.DifferentialEquation
{

    /**
     * constructs a new DifferentialEquation.
     * @param simulator the simulator
     * @throws RemoteException on network error
     */
    public DifferentialEquation(DESSSimulatorInterface simulator) throws RemoteException
    {
        super(simulator);
    }

    /**
     * constructs a new DifferentialEquation.
     * @param simulator the simulator
     * @param timeStep the time step
     * @throws RemoteException on network error
     */
    public DifferentialEquation(DESSSimulatorInterface simulator, double timeStep) throws RemoteException
    {
        super(simulator, timeStep);
    }

    /**
     * constructs a new DifferentialEquation.
     * @param simulator the simulator
     * @param timeStep the time step
     * @param numericalMethod the integration method
     * @throws RemoteException on network error
     */
    public DifferentialEquation(DESSSimulatorInterface simulator, double timeStep, short numericalMethod) throws RemoteException
    {
        super(simulator, timeStep, numericalMethod);
    }

    /**
     * constructs a new DifferentialEquation.
     * @param simulator the simulator
     * @param timeStep the time step
     * @param numericalIntegrator the integration method
     * @throws RemoteException on network error
     */
    public DifferentialEquation(DESSSimulatorInterface simulator, double timeStep,
            NumericalIntegrator numericalIntegrator) throws RemoteException
    {
        super(simulator, timeStep, numericalIntegrator);
    }

    /** {@inheritDoc} */
    @Override
    public double[] dy(double arg0, double[] arg1)
    {
        return new double[]{1.0};
    }
}
