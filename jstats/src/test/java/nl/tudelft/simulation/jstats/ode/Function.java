/*
 * @(#) Function.java Apr 20, 2004 Copyright (c) 2002-2005 Delft University of
 * Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved.
 * This software is proprietary information of Delft University of Technology
 * 
 */
package nl.tudelft.simulation.jstats.ode;

/**
 * <p>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft
 * University of Technology </a>, the Netherlands. <br>
 * See for project information <a
 * href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser
 * General Public License (LGPL) </a>, no warranty.
 * 
 * @author <a href="http://www.peter-jacobs.com/index.htm">Peter Jacobs </a>
 * @version 1.2 Apr 20, 2004
 * @since 1.5
 */
public class Function extends DifferentialEquation
{
    /**
     * constructs a new Function
     * 
     * @param stepSize the stepSize
     * @param integrationMethod the methodOfIntegration
     */
    public Function(final double stepSize, final short integrationMethod)
    {
        super(stepSize, integrationMethod);
        super.initialize(0, new double[] { 0.5, 1.5 });
    }

    /**
     * @see nl.tudelft.simulation.jstats.ode.DifferentialEquationInterface#dy(double,
     *      double[])
     */
    public double[] dy(final double x, final double[] y)
    {
        return new double[] { y[1], -0.2 * y[1] - Math.sin(y[0]) };
    }
}