package nl.tudelft.simulation.jstats.ode.integrators;

import nl.tudelft.simulation.jstats.ode.DifferentialEquationInterface;

/**
 * The Gill numerical estimator as described in <a href="http://mathworld.wolfram.com/GillsMethod.html">
 * http://mathworld.wolfram.com/GillsMethod.html </a>
 * <p>
 * Copyright (c) 2002-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. See for project information <a href="https://simulation.tudelft.nl/" target="_blank">
 * https://simulation.tudelft.nl</a>. The DSOL project is distributed under a three-clause BSD-style license, which can
 * be found at <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank"> Alexander Verbraeck</a>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 */
public class Gill extends NumericalIntegrator
{
    /** */
    private static final long serialVersionUID = 1L;

    /** constant: sqrt(2). */
    private static final double SQRT2 = Math.sqrt(2.0d);

    /** constant: 1/2 sqrt(2). */
    private static final double SQRT2D2 = 0.5d * Math.sqrt(2.0d);

    /**
     * constructs a new Gill integrator.
     * @param timeStep double; the timeStep
     * @param equation DifferentialEquationInterface; the differentialEquation
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
