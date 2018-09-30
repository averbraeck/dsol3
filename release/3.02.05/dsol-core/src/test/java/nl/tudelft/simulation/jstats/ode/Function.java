package nl.tudelft.simulation.jstats.ode;

/**
 * <p>
 * Copyright (c) 2002-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. See for project information <a href="https://simulation.tudelft.nl/" target="_blank">
 * https://simulation.tudelft.nl</a>. The DSOL project is distributed under a three-clause BSD-style license, which can
 * be found at <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @since 1.5
 */
public class Function extends DifferentialEquation
{
    /**
     * constructs a new Function.
     * @param stepSize the stepSize
     * @param integrationMethod the methodOfIntegration
     */
    public Function(final double stepSize, final short integrationMethod)
    {
        super(stepSize, integrationMethod);
        super.initialize(0, new double[]{0.5, 1.5});
    }

    /** {@inheritDoc} */
    @Override
    public double[] dy(final double x, final double[] y)
    {
        return new double[]{y[1], -0.2 * y[1] - Math.sin(y[0])};
    }
}
