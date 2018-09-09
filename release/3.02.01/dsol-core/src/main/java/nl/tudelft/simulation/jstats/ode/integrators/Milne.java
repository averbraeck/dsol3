package nl.tudelft.simulation.jstats.ode.integrators;

import nl.tudelft.simulation.jstats.ode.DifferentialEquationInterface;

/**
 * The Milne numerical estimator as described in <a href="http://mathworld.wolfram.com/MilnesMethod.html">
 * http://mathworld.wolfram.com/MilnesMethod.html </a>
 * <p>
 * (c) 2002-2018 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:38:40 $
 * @since 1.5
 */
public class Milne extends CachingNumericalIntegrator
{
    /**
     * constructs a new Milne integrator.
     * @param timeStep the timeStep to use in the estimation.
     * @param equation the equation to use.
     */
    public Milne(final double timeStep, final DifferentialEquationInterface equation)
    {
        super(timeStep, equation, 4, NumericalIntegrator.RUNGEKUTTA4, 10);
    }

    /**
     * constructs a new Milne integrator, indicating the starting method and number of substeps
     * @param timeStep the timeStep to use in the estimation.
     * @param equation the equation to use.
     * @param integrationMethod the primer integrator to use
     * @param startingSubSteps the number of substeps per timestep during starting of the integrator
     */
    public Milne(final double timeStep, final DifferentialEquationInterface equation, final short integrationMethod,
            final int startingSubSteps)
    {
        super(timeStep, equation, 4, integrationMethod, startingSubSteps);
    }

    /** {@inheritDoc} */
    @Override
    public double[] next(final double x)
    {
        double[] y3 = super.getY(3);
        double[] y1 = super.getY(1);
        double[] dy2 = super.getDY(2);
        double[] dy1 = super.getDY(1);
        double[] dy0 = super.getDY(0);

        // Let's evaluate the predictor
        double[] p = super.add(y3, super.multiply(4 * this.timeStep / 3.0,
                super.add(super.multiply(2.0, dy0), super.multiply(-1.0, dy1), super.multiply(2.0, dy2))));

        // Now we compute the corrector
        return super.add(y1, super.multiply(this.timeStep / 3.0,
                super.add(super.multiply(1.0, dy1), super.multiply(4.0, dy0), this.equation.dy(x + this.timeStep, p))));
    }
}
