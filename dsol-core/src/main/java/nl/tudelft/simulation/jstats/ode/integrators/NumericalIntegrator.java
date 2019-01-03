package nl.tudelft.simulation.jstats.ode.integrators;

import java.io.Serializable;

import nl.tudelft.simulation.jstats.ode.DifferentialEquationInterface;

/**
 * Provides basic methods for all numerical integration methods. They mostly include matrix computation.
 * <p>
 * Copyright (c) 2002-2019 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. See for project information <a href="https://simulation.tudelft.nl/" target="_blank">
 * https://simulation.tudelft.nl</a>. The DSOL project is distributed under a three-clause BSD-style license, which can
 * be found at <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank"> Alexander Verbraeck</a>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 */
public abstract class NumericalIntegrator implements Serializable
{
    /** */
    private static final long serialVersionUID = 1L;

    /** Euler's integration. */
    public static final short EULER = 0;

    /** Heun's integration. */
    public static final short HEUN = 1;

    /** RungeKutta's (3rd level) integration. */
    public static final short RUNGEKUTTA3 = 2;

    /** RungeKutta's (4th level) integration. */
    public static final short RUNGEKUTTA4 = 3;

    /** Adam's integration. */
    public static final short ADAMS = 4;

    /** Gill's integration. */
    public static final short GILL = 5;

    /** Milne's integration. */
    public static final short MILNE = 6;

    /** Runge-Kutta-Fehlberg integration. */
    public static final short RUNGEKUTTAFEHLBERG = 7;

    /** Runge-Kutta-Cash-Carp integration. */
    public static final short RUNGEKUTTACASHCARP = 8;

    /** The default integrator. */
    public static final short DEFAULT_INTEGRATOR = NumericalIntegrator.RUNGEKUTTA4;

    /**
     * @param integrationMethod short; the type of integrator to create
     * @param timeStep double; the starting timestep to use
     * @param equation DifferentialEquationInterface; the differential equation
     * @return the integrator
     */
    public static NumericalIntegrator resolve(final short integrationMethod, final double timeStep,
            final DifferentialEquationInterface equation)
    {
        switch (integrationMethod)
        {
            case NumericalIntegrator.ADAMS:
                return new Adams(timeStep, equation);
            case NumericalIntegrator.EULER:
                return new Euler(timeStep, equation);
            case NumericalIntegrator.HEUN:
                return new Heun(timeStep, equation);
            case NumericalIntegrator.RUNGEKUTTA3:
                return new RungeKutta3(timeStep, equation);
            case NumericalIntegrator.RUNGEKUTTA4:
                return new RungeKutta4(timeStep, equation);
            case NumericalIntegrator.GILL:
                return new Gill(timeStep, equation);
            case NumericalIntegrator.MILNE:
                return new Milne(timeStep, equation);
            case NumericalIntegrator.RUNGEKUTTAFEHLBERG:
                return new RungeKuttaFehlberg(timeStep, equation);
            case NumericalIntegrator.RUNGEKUTTACASHCARP:
                return new RungeKuttaCashCarp(timeStep, equation);
            default:
                throw new IllegalArgumentException("unknown integration method");
        }
    }

    /** the timeStep to use. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected double timeStep = Double.NaN;

    /** the calculated error of the last step. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected double[] error = null;

    /** the equation to integrate. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected DifferentialEquationInterface equation = null;

    /**
     * constructs a new NumericalIntegrator.
     * @param timeStep double; the timeStep
     * @param equation DifferentialEquationInterface; the differentialEquation
     */
    public NumericalIntegrator(final double timeStep, final DifferentialEquationInterface equation)
    {
        this.timeStep = timeStep;
        this.equation = equation;
    }

    /**
     * computes the next value.
     * @param x double; the x value corresponding to the last y-value computed
     * @param y double[]; the last y value
     * @return the new value
     */
    public abstract double[] next(double x, double[] y);

    /**
     * multiplies a vector with a constant.
     * @param constant double; the constant
     * @param vector double[]; the vector
     * @return the new vector
     */
    protected double[] multiply(final double constant, final double[] vector)
    {
        double[] prod = new double[vector.length];
        for (int i = 0; i < vector.length; i++)
        {
            prod[i] = constant * vector[i];
        }
        return prod;
    }

    /**
     * adds two vectors.
     * @param a double[]; vector a
     * @param b double[]; vector b
     * @return the new vector
     */
    protected double[] add(final double[] a, final double[] b)
    {
        double[] sum = new double[a.length];
        for (int i = 0; i < a.length; i++)
        {
            sum[i] = a[i] + b[i];
        }
        return sum;
    }

    /**
     * adds a number of vectors.
     * @param a double[]; vector a
     * @param b double[]; vector b
     * @param c double[]; vector c
     * @return the new vector
     */
    protected double[] add(final double[] a, final double[] b, final double[] c)
    {
        double[] sum = new double[a.length];
        for (int i = 0; i < a.length; i++)
        {
            sum[i] = a[i] + b[i] + c[i];
        }
        return sum;
    }

    /**
     * adds a number of vectors.
     * @param a double[]; vector a
     * @param b double[]; vector b
     * @param c double[]; vector c
     * @param d double[]; vector d
     * @return the sum
     */
    protected double[] add(final double[] a, final double[] b, final double[] c, final double[] d)
    {
        double[] sum = new double[a.length];
        for (int i = 0; i < a.length; i++)
        {
            sum[i] = a[i] + b[i] + c[i] + d[i];
        }
        return sum;
    }

    /**
     * adds a number of vectors.
     * @param a double[]; vector a
     * @param b double[]; vector b
     * @param c double[]; vector c
     * @param d double[]; vector d
     * @param e double[]; vector e
     * @return the sum
     */
    protected double[] add(final double[] a, final double[] b, final double[] c, final double[] d, final double[] e)
    {
        double[] sum = new double[a.length];
        for (int i = 0; i < a.length; i++)
        {
            sum[i] = a[i] + b[i] + c[i] + d[i] + e[i];
        }
        return sum;
    }

    /**
     * adds a number of vectors.
     * @param a double[]; vector a
     * @param b double[]; vector b
     * @param c double[]; vector c
     * @param d double[]; vector d
     * @param e double[]; vector e
     * @param f double[]; vector f
     * @return the sum
     */
    protected double[] add(final double[] a, final double[] b, final double[] c, final double[] d, final double[] e,
            final double[] f)
    {
        double[] sum = new double[a.length];
        for (int i = 0; i < a.length; i++)
        {
            sum[i] = a[i] + b[i] + c[i] + d[i] + e[i] + f[i];
        }
        return sum;
    }

    /**
     * @return Returns the timeStep.
     */
    public double getTimeStep()
    {
        return this.timeStep;
    }

    /**
     * @param timeStep double; The timeStep to set.
     */
    public void setTimeStep(final double timeStep)
    {
        this.timeStep = timeStep;
    }

    /**
     * @return Returns the error.
     */
    public double[] getError()
    {
        return this.error;
    }

}
