/*
 * @(#) RungeKutta4.java Apr 20, 2004 Copyright (c) 2002-2005 Delft University
 * of Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. This software is proprietary information of Delft University of
 * Technology 
 */
package nl.tudelft.simulation.jstats.ode.integrators;

import nl.tudelft.simulation.jstats.ode.DifferentialEquationInterface;

/**
 * The RungeKutta4 numerical integrator.
 * <p>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft
 * University of Technology </a>, the Netherlands. <br>
 * See for project information <a
 * href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser
 * General Public License (LGPL) </a>, no warranty.
 * 
 * @author <a href="http://www.peter-jacobs.com/index.htm">Peter Jacobs </a>
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:38:39 $
 * @since 1.5
 */
public class RungeKutta4 extends NumericalIntegrator
{
    /**
     * constructs a new RungeKutta4
     * 
     * @param timeStep the timeStep
     * @param equation the differentialEquation
     */
    public RungeKutta4(final double timeStep,
            final DifferentialEquationInterface equation)
    {
        super(timeStep, equation);
    }

    /**
     * @see nl.tudelft.simulation.jstats.ode.integrators.NumericalIntegrator
     *      #next(double,double[])
     */
    @Override
    public double[] next(final double x, final double[] y)
    {
        double[] k1 = this.equation.dy(x, y);
        double[] k2 = this.equation.dy(x + 0.5 * this.timeStep, super.add(y,
                super.multiply(0.5 * this.timeStep, k1)));
        double[] k3 = this.equation.dy(x + 0.5 * this.timeStep, super.add(y,
                super.multiply(0.5 * this.timeStep, k2)));
        double[] k4 = this.equation.dy(x + this.timeStep, super.add(y, super
                .multiply(this.timeStep, k3)));
        double[] sum = super.add(k1, super.multiply(2.0, k2), super.multiply(
                2.0, k3), k4);
        return super.add(y, super.multiply(this.timeStep / 6.0, sum));
    }
}