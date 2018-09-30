package nl.tudelft.simulation.jstats.ode.integrators;

import nl.tudelft.simulation.jstats.ode.DifferentialEquationInterface;

/**
 * The Heun numerical estimator.
 * <p>
 * Copyright (c) 2002-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. See for project information <a href="https://simulation.tudelft.nl/">https://simulation.tudelft.nl</a>. The
 * DSOL project is distributed under a three-clause BSD-style license, which can be found at <a href=
 * "https://simulation.tudelft.nl/dsol/3.0/license.html">https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank">Alexander Verbraeck</a>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 */
public class Heun extends NumericalIntegrator
{
    /** */
    private static final long serialVersionUID = 1L;

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
