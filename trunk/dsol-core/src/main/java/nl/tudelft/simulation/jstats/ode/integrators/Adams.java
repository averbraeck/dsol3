package nl.tudelft.simulation.jstats.ode.integrators;

import nl.tudelft.simulation.jstats.ode.DifferentialEquationInterface;

/**
 * The Adams-Bashforth-Moulton numerical estimator as described in <a href="http://mathworld.wolfram.com/AdamsMethod.html">
 * http://mathworld.wolfram.com/AdamsMethod.html </a>
 * <p>
 * Copyright (c) 2002-2019 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank"> Alexander Verbraeck</a>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 */
public class Adams extends CachingNumericalIntegrator
{
    /** */
    private static final long serialVersionUID = 1L;

    /**
     * constructs a new Adams integrator.
     * @param timeStep double; the timeStep to use in the estimation.
     * @param equation DifferentialEquationInterface; the equation to use.
     */
    public Adams(final double timeStep, final DifferentialEquationInterface equation)
    {
        super(timeStep, equation, 4, NumericalIntegrator.RUNGEKUTTA4, 10);
    }

    /**
     * constructs a new Adams integrator, indicating the starting method and number of substeps.
     * @param timeStep double; the timeStep to use in the estimation.
     * @param equation DifferentialEquationInterface; the equation to use.
     * @param integrationMethod short; the primer integrator to use
     * @param startingSubSteps int; the number of substeps per timestep during starting of the integrator
     */
    public Adams(final double timeStep, final DifferentialEquationInterface equation, final short integrationMethod,
            final int startingSubSteps)
    {
        super(timeStep, equation, 4, integrationMethod, startingSubSteps);
    }

    /** {@inheritDoc} */
    @Override
    public double[] next(final double x)
    {
        double[] y0 = super.getY(0);
        double[] dy0 = super.getDY(0);
        double[] dy1 = super.getDY(1);
        double[] dy2 = super.getDY(2);
        double[] dy3 = super.getDY(3);
        double[] sum =
                super.add(super.multiply(-9, dy3), super.multiply(37, dy2), super.multiply(-59, dy1), super.multiply(55, dy0));
        sum = super.multiply(this.timeStep / 24.0, sum);
        double[] p = super.add(y0, sum);
        sum = super.add(dy2, super.multiply(-5, dy1), super.multiply(19, dy0),
                super.multiply(9, this.equation.dy(x + this.timeStep, p)));
        return super.add(y0, super.multiply(this.timeStep / 24.0, sum));
    }
}
