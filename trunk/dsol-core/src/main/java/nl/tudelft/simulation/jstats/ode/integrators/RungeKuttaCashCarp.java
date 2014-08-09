/*
 * @(#) RungeKuttaCashCarp.java May 12, 2004 Copyright (c) 2002-2005 Delft
 * University of Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All
 * rights reserved. This software is proprietary information of Delft University
 * of Technology 
 */
package nl.tudelft.simulation.jstats.ode.integrators;

import nl.tudelft.simulation.jstats.ode.DifferentialEquationInterface;

/**
 * The RungeKuttaCashCarp.java numerical integrator.
 * <p>
 * (c) copyright 2004 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="mailto:a.verbraeck@tudelft.nl">Alexander Verbraeck </a>
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:38:40 $
 * @since 1.5
 */
public class RungeKuttaCashCarp extends NumericalIntegrator
{
    /** the parameters for a_i, in f(x_n + a_i h, .) */
    protected static double[] a = new double[]{0d, 1d / 5d, 3d / 10d, 3d / 5d, 1d, 7d / 8d};

    /** the parameters for b_ij, in f(., y_n + b_p1 k1 + bp2 k2 + ...) */
    protected static double[][] b = new double[][]{{0d, 0d, 0d, 0d, 0d},
            {1d / 5d, 3d / 40d, 3d / 10d, -11d / 54d, 1631d / 55296d}, {0d, 9d / 40d, -9d / 10d, 5d / 2d, 175d / 212d},
            {0d, 0d, 6d / 5d, -70d / 27d, 575d / 13824d}, {0d, 0d, 0d, 35 / 27d, 44275d / 110592d},
            {0d, 0d, 0d, 0d, 253d / 4096d}};

    /** the parameters for c_i, in y_n+1 = y_n + c_1 k_1 + c_2 k_2 + ... */
    protected static double[] c = new double[]{37d / 378d, 0d, 250d / 621d, 125d / 594d, 0d, 512d / 1771d};

    /** the parameters for c4_i, in y_n+1 = y_n + c4_1 k_1 + c4_2 k_2 + ... */
    protected static double[] c4 = new double[]{2825d / 27648d, 0d, 18575d / 48384d, 13525d / 55296d, 277d / 14336d,
            1d / 4d};

    /** the numer of k-s in the method */
    protected static int nk = 6;

    /**
     * constructs a new RungeKuttaCashCarp
     * @param timeStep the timeStep
     * @param equation the differentialEquation
     */
    public RungeKuttaCashCarp(final double timeStep, final DifferentialEquationInterface equation)
    {
        super(timeStep, equation);
    }

    /**
     * @see nl.tudelft.simulation.jstats.ode.integrators.NumericalIntegrator #next(double,double[])
     */
    @Override
    public double[] next(final double x, final double[] y)
    {
        double[][] k = new double[nk][];
        for (int i = 0; i < nk; i++)
        {
            double[] ysum = y.clone();
            for (int j = 0; j < i; j++)
            {
                if (b[i][j] != 0.0)
                {
                    ysum = this.add(ysum, this.multiply(b[i][j], k[j]));
                }
            }
            k[i] = this.multiply(this.timeStep, this.equation.dy(x + a[i] * this.timeStep, ysum));
        }
        double[] sum = y.clone();
        super.error = new double[y.length];
        for (int i = 0; i < nk; i++)
        {
            sum = this.add(sum, this.multiply(c[i], k[i]));
            super.error = this.add(super.error, this.multiply(c[i] - c4[i], k[i]));
        }
        return sum;
    }
}