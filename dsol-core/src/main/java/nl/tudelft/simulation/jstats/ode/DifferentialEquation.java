package nl.tudelft.simulation.jstats.ode;

import nl.tudelft.simulation.event.EventProducer;
import nl.tudelft.simulation.jstats.ode.integrators.NumericalIntegrator;

/**
 * The DifferentialEquation is the abstract basis for the DESS formalism.
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
public abstract class DifferentialEquation extends EventProducer implements DifferentialEquationInterface
{
    /** */
    private static final long serialVersionUID = 1L;

    /** the numerical integrator for the differential equations. */
    private NumericalIntegrator integrator = null;

    /** the initial value array. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected double[] y0 = null;

    /** the timeStep. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected double timeStep = Double.NaN;

    /** the first x value to start integration. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected double x0 = Double.NaN;

    /**
     * constructs a new DifferentialEquation with default integrator.
     * @param timeStep the timeStep to use.
     */
    public DifferentialEquation(final double timeStep)
    {
        this(timeStep, NumericalIntegrator.DEFAULT_INTEGRATOR);
    }

    /**
     * constructs a new DifferentialEquation with a user-specified integrator.
     * @param timeStep the timeStep to use.
     * @param integrator the integrator to use.
     */
    public DifferentialEquation(final double timeStep, final NumericalIntegrator integrator)
    {
        super();
        this.timeStep = timeStep;
        this.integrator = integrator;
    }

    /**
     * constructs a new DifferentialEquation with a preselected integrator.
     * @param timeStep the timeStep to use.
     * @param integrationMethod the integrator to use.
     */
    public DifferentialEquation(final double timeStep, final short integrationMethod)
    {
        super();
        this.timeStep = timeStep;
        this.integrator = NumericalIntegrator.resolve(integrationMethod, timeStep, this);
    }

    /** {@inheritDoc} */
    @Override
    public void initialize(final double x, final double[] y)
    {
        this.x0 = x;
        this.y0 = y;
    }

    /** {@inheritDoc} */
    @Override
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
     * integrates Y.
     * @param x the x-value
     * @param initialX the initial X value, non-final (will be updated)
     * @param initialY the initial Y value, non-final (will be updated)
     * @return the new Y value
     */
    @SuppressWarnings("checkstyle:finalparameters")
    protected double[] integrateY(final double x, /* non-final */ double initialX, /* non-final */ double[] initialY)
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
