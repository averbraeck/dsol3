/*
 * @(#) DifferentialEquation.java Apr 20, 2004 Copyright (c) 2002-2005 Delft
 * University of Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All
 * rights reserved. This software is proprietary information of Delft University
 * of Technology 
 */

package nl.tudelft.simulation.jstats.ode;

import nl.tudelft.simulation.event.EventProducer;
import nl.tudelft.simulation.jstats.ode.integrators.NumericalIntegrator;

/**
 * The DifferentialEquation is the abstract basis for
 * <p>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft
 * University of Technology </a>, the Netherlands. <br>
 * See for project information <a
 * href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser
 * General Public License (LGPL) </a>, no warranty.
 * 
 * @author <a href="http://www.peter-jacobs.com/index.htm">Peter Jacobs </a>
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:38:41 $
 * @since 1.5
 */
public abstract class DifferentialEquation extends EventProducer implements
        DifferentialEquationInterface
{

    /**
     * the integrator
     */
    private NumericalIntegrator integrator = null;

    /** the initial value array */
    protected double[] y0 = null;

    /** a timeStep */
    protected double timeStep = Double.NaN;

    /** the first x value to start integration */
    protected double x0 = Double.NaN;

    /**
     * constructs a new DifferentialEquation with default integrator.
     * 
     * @param timeStep the timeStep to use.
     */
    public DifferentialEquation(final double timeStep)
    {
        this(timeStep, NumericalIntegrator.DEFAULT_INTEGRATOR);
    }

    /**
     * constructs a new DifferentialEquation with a user-specified integrator.
     * 
     * @param timeStep the timeStep to use.
     * @param integrator the integrator to use.
     */
    public DifferentialEquation(final double timeStep,
            final NumericalIntegrator integrator)
    {
        super();
        this.timeStep = timeStep;
        this.integrator = integrator;
    }

    /**
     * constructs a new DifferentialEquation with a preselected integrator.
     * 
     * @param timeStep the timeStep to use.
     * @param integrationMethod the integrator to use.
     */
    public DifferentialEquation(final double timeStep,
            final short integrationMethod)
    {
        super();
        this.timeStep = timeStep;
        this.integrator = NumericalIntegrator.resolve(integrationMethod,
                timeStep, this);
    }

    /**
     * @see nl.tudelft.simulation.jstats.ode.DifferentialEquationInterface#initialize(double,
     *      double[])
     */
    public void initialize(final double x, final double[] y)
    {
        this.x0 = x;
        this.y0 = y;
    }

    /**
     * @see nl.tudelft.simulation.jstats.ode.DifferentialEquationInterface#y(double)
     */
    public double[] y(final double x)
    {
        // If the ODE is not initialized, the cache is empty.
        if (Double.isNaN(this.x0))
        {
            throw new RuntimeException("differential equation not initialized");
        }
        // x<initialX this is not supported.
        if (x < this.x0)
        {
            throw new RuntimeException("cannot compute values x<x0");
        }
        return this.integrateY(x, this.x0, this.y0);
    }

    /**
     * integrates Y
     * 
     * @param x the x-value
     * @param initialX the initial X value
     * @param initialY the initial Y value
     * @return the new Y value
     */
    protected double[] integrateY(final double x, double initialX,
            double[] initialY)
    {
        // we request the new value from the integrator.
        while (x > initialX + this.timeStep)
        {
            initialY = this.integrator.next(initialX, initialY);
            initialX = initialX + this.timeStep;
        }
        // We are in our final step.
        double[] nextValue = this.integrator.next(initialX, initialY);
        double ratio = (x - initialX) / this.timeStep;
        for (int i = 0; i < initialY.length; i++)
        {
            initialY[i] = initialY[i] + ratio * (nextValue[i] - initialY[i]);
        }
        return initialY;
    }

    /**
     * @return Returns the integrator.
     */
    public NumericalIntegrator getIntegrator()
    {
        return this.integrator;
    }

    /**
     * @param integrator The integrator to set.
     */
    public void setIntegrator(final NumericalIntegrator integrator)
    {
        this.integrator = integrator;
    }

}