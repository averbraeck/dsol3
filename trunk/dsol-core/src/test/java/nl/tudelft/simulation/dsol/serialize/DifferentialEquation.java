package nl.tudelft.simulation.dsol.serialize;

import java.rmi.RemoteException;

import nl.tudelft.simulation.dsol.simulators.DESSSimulatorInterface;
import nl.tudelft.simulation.jstats.ode.integrators.NumericalIntegrator;

/**
 * The histogram specifies a histogram chart for the DSOL framework.
 * <p>
 * (c) copyright 2004 <a href="http://www.simulation.tudelft.nl/dsol/">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl/dsol/"> www.simulation.tudelft.nl/dsol </a> <br>
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
     * @param simulator
     * @throws RemoteException
     */
    public DifferentialEquation(DESSSimulatorInterface simulator) throws RemoteException
    {
        super(simulator);
    }

    /**
     * constructs a new DifferentialEquation.
     * @param simulator
     * @param timeStep
     */
    public DifferentialEquation(DESSSimulatorInterface simulator, double timeStep)
    {
        super(simulator, timeStep);
    }

    /**
     * constructs a new DifferentialEquation.
     * @param simulator
     * @param timeStep
     * @param numericalMethod
     */
    public DifferentialEquation(DESSSimulatorInterface simulator, double timeStep, short numericalMethod)
    {
        super(simulator, timeStep, numericalMethod);
    }

    /**
     * constructs a new DifferentialEquation.
     * @param simulator
     * @param timeStep
     * @param numericalIntegrator
     */
    public DifferentialEquation(DESSSimulatorInterface simulator, double timeStep,
            NumericalIntegrator numericalIntegrator)
    {
        super(simulator, timeStep, numericalIntegrator);
    }

    /** {@inheritDoc} */
    public double[] dy(double arg0, double[] arg1)
    {
        return new double[]{1.0};
    }
}
