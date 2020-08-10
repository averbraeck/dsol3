package nl.tudelft.simulation.jstats.ode.integrators;

import nl.tudelft.simulation.jstats.ode.DifferentialEquationInterface;

/**
 * The Gill numerical estimator as described in <a href="http://mathworld.wolfram.com/GillsMethod.html">
 * http://mathworld.wolfram.com/GillsMethod.html </a>
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
public class Gill extends NumericalIntegrator
{
    /** constant: sqrt(2) */
    private static final double SQRT2 = Math.sqrt(2.0d);

    /** constant: 1/2 sqrt(2) */
    private static final double SQRT2D2 = 0.5d * Math.sqrt(2.0d);

    /**
     * constructs a new Gill integrator.
     * @param timeStep the timeStep
     * @param equation the differentialEquation
     */
    public Gill(final double timeStep, final DifferentialEquationInterface equation)
    {
        super(timeStep, equation);
    }

    /** {@inheritDoc} */
    @Override
    public double[] next(final double x, final double[] y)
    {
        double[] k1 = this.equation.dy(x, y);
        double[] k2 = this.equation.dy(x + 0.5d * this.timeStep, super.add(y, super.multiply(0.5d, k1)));
        double[] k3 = this.equation.dy(x + 0.5d * this.timeStep,
                super.add(y, super.multiply((-0.5d + Gill.SQRT2D2), k1), super.multiply((1.0d - Gill.SQRT2D2), k2)));
        double[] k4 = this.equation.dy(x + this.timeStep,
                super.add(y, super.multiply((-Gill.SQRT2D2), k2), super.multiply((1.0d + Gill.SQRT2D2), k3)));
        double[] sum =
                super.add(k1, super.multiply((2.0d - Gill.SQRT2), k2), super.multiply((2.0d + Gill.SQRT2), k3), k4);
        return super.add(y, super.multiply(this.timeStep / 6.0d, sum));
    }
}