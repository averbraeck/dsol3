/*
 * @(#) DifferentialEquationInterface.java Apr 20, 2004 Copyright (c) 2002-2005
 * Delft University of Technology Jaffalaan 5, 2628 BX Delft, the Netherlands.
 * All rights reserved. This software is proprietary information of Delft
 * University of Technology The code is published under the Lesser General
 * Public License
 */
package nl.tudelft.simulation.jstats.ode;

/**
 * An interface for the DifferentialEquation.
 * <p>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="http://www.peter-jacobs.com/index.htm">Peter Jacobs </a>
 * @version $Revision: 1.1 $ $Date: 2007/01/07 05:00:55 $
 * @since 1.5
 */
public interface DifferentialEquationInterface
{
    /**
     * initializes the differential equation
     * @param x the x-value
     * @param y the y-value
     */
    void initialize(double x, double[] y);

    /**
     * returns y as a function of x
     * @param x the x-value
     * @return y
     */
    double[] y(double x);

    /**
     * returns dy as a function of x,y
     * @param x the x-value
     * @param y the y-value
     * @return dy/dx as a function of x,y
     */
    double[] dy(double x, double[] y);
}