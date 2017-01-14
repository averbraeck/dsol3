package nl.tudelft.simulation.jstats.ode.integrators;

import nl.tudelft.simulation.jstats.ode.DifferentialEquationInterface;

/**
 * The Heun numerical estimator.
 * <p>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:38:40 $
 * @since 1.5
 */
public class Heun extends NumericalIntegrator
{
    /**
     * constructs a new Heun.
     * @param timeStep the timeStep
     * @param equation the equation
     */
    public Heun(final double timeStep, final DifferentialEquationInterface equation)
    {
        super(timeStep, equation);
    }

    /** {@inheritDoc} */
    @Override
    public double[] next(final double x, final double[] y)
    {
        double[] f0 = this.equation.dy(x, y);
        double[] p = super.add(y, super.multiply(this.timeStep, f0));
        return super.add(y, super.multiply(0.5 * this.timeStep, super.add(f0, this.equation.dy(x + this.timeStep, p))));
    }
}
