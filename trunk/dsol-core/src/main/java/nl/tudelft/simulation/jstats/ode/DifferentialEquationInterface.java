package nl.tudelft.simulation.jstats.ode;

/**
 * An interface for the DifferentialEquation.
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
public interface DifferentialEquationInterface
{
    /**
     * initializes the differential equation.
     * @param x the x-value
     * @param y the y-value
     */
    void initialize(double x, double[] y);

    /**
     * returns y as a function of x.
     * @param x the x-value
     * @return y
     */
    double[] y(double x);

    /**
     * returns dy as a function of x,y.
     * @param x the x-value
     * @param y the y-value
     * @return dy/dx as a function of x,y
     */
    double[] dy(double x, double[] y);
}
