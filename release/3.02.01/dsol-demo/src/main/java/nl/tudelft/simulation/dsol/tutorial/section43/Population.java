package nl.tudelft.simulation.dsol.tutorial.section43;

import java.rmi.RemoteException;

import nl.tudelft.simulation.dsol.formalisms.dess.DifferentialEquation;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simulators.DESSSimulatorInterface;

/**
 * The population differential equation.
 * <p>
 * copyright (c) 2002-2018 <a href="https://simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="https://simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @version 1.2 Apr 22, 2004
 * @since 1.5
 */
public class Population extends DifferentialEquation<Double, Double, SimTimeDouble>
{
    /** */
    private static final long serialVersionUID = 1L;

    /** Lotka-Volterra parameters. */
    private double a;

    /** Lotka-Volterra parameters. */
    private double b;

    /** Lotka-Volterra parameters. */
    private double c;

    /** Lotka-Volterra parameters. */
    private double d;

    /**
     * constructs a new Population.
     * @param simulator the simulator
     * @throws RemoteException  on networn error for the listeners
     */
    public Population(final DESSSimulatorInterface.TimeDouble simulator) throws RemoteException
    {
        super(simulator, simulator.getTimeStep());
        double predator = 10;
        double prey = 20;
        this.initialize(0.0, new double[]{predator, prey});
        this.a = 1;
        this.b = 0.1;
        this.c = 1;
        this.d = 0.2;
    }

    /** {@inheritDoc} */
    @Override
    public final double[] dy(final double time, final double[] y)
    {
        double[] dy = new double[2];
        dy[0] = -this.a * y[0] + this.b * y[0] * y[1];
        dy[1] = this.c * y[1] - this.d * y[1] * y[0];
        return dy;
    }
}
