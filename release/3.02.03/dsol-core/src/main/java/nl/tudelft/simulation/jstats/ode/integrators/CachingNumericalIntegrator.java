package nl.tudelft.simulation.jstats.ode.integrators;

import nl.tudelft.simulation.jstats.ode.DifferentialEquationInterface;

/**
 * The CachingNumericalIntegrator is the basis for an integrator that needs access to previously calculated values of
 * y', e.g. y'_(k-1), y'_(k-2), etc. <br>
 * (c) 2002-2018-2004 <a href="https://simulation.tudelft.nl">Delft University of Technology </a>, the Netherlands. <br>
 * See for project information <a href="https://simulation.tudelft.nl"> www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:38:40 $
 * @author <a href="mailto:a.verbraeck@tudelft.nl">Alexander Verbraeck </a>
 */
public abstract class CachingNumericalIntegrator extends NumericalIntegrator
{
    /** the number of cachePlaces to store, e.g for k-1, k-2 set it to 2 */
    private int cachePlaces = 0;

    /** the cache for y(k-1), y(k-2), etc. */
    private double[][] cacheY;

    /** the cache for y'(k-1), y'(k-2), etc. */
    private double[][] cacheDY;

    /** the number of cache places filled = the last cache place used. */
    private int lastCachePlace = -1;

    /**
     * the primer integrator
     */
    protected NumericalIntegrator startingIntegrator = null;

    /** the substeps to use when starting the integrator. */
    protected int startingSubSteps = 10;

    /**
     * constructs a new CachingNumericalIntegrator with a fixed number of cache places.
     * @param timeStep the timeStep
     * @param equation the differentialEquation
     * @param cachePlaces the number of cache places to store
     * @param integrationMethod the primer integrator to use
     * @param startingSubSteps the number of substeps per timestep during starting of the integrator
     */
    public CachingNumericalIntegrator(final double timeStep, final DifferentialEquationInterface equation,
            final int cachePlaces, final short integrationMethod, final int startingSubSteps)
    {
        super(timeStep, equation);
        this.cachePlaces = cachePlaces;
        this.cacheY = new double[cachePlaces][];
        this.cacheDY = new double[cachePlaces][];
        this.startingIntegrator =
                NumericalIntegrator.resolve(integrationMethod, timeStep / (1.0d * startingSubSteps), equation);
        this.startingSubSteps = startingSubSteps;
    }

    /** {@inheritDoc} */
    @Override
    public void setTimeStep(final double timeStep)
    {
        super.setTimeStep(timeStep);
        this.lastCachePlace = -1;
    }

    /** {@inheritDoc} */
    @Override
    public double[] next(final double x, final double[] y)
    {
        double[] ynext = null;
        // look whether we have to prime, or can calculate
        if (this.lastCachePlace < this.cachePlaces)
        {
            // calculate next y-value using the primer, which can have a
            // much smaller timestep
            ynext = y.clone();
            double xstep = x;
            for (int i = 0; i < this.startingSubSteps; i++)
            {
                ynext = this.startingIntegrator.next(xstep, ynext);
                xstep += this.timeStep / (1.0d * this.startingSubSteps);
            }
        }
        else
        {
            // calculate next y-value using the intended method
            ynext = next(x);
        }
        this.lastCachePlace++;
        this.cacheY[this.lastCachePlace % this.cachePlaces] = ynext;
        this.cacheDY[this.lastCachePlace % this.cachePlaces] = this.equation.dy(x + this.timeStep, ynext);
        return ynext;
    }

    /**
     * get a cached Y-value,
     * @param numberDown the number of the previous value we want
     * @return the corresponding Y-value
     */
    public double[] getY(final int numberDown)
    {
        if (this.lastCachePlace < this.cachePlaces)
        {
            throw new RuntimeException("Tried to retrieve y-value that was not yet primed");
        }
        if (numberDown >= this.cachePlaces)
        {
            throw new RuntimeException("Tried to retrieve y-value beyond cache limits");
        }
        return this.cacheY[(this.lastCachePlace - numberDown) % this.cachePlaces].clone();
    }

    /**
     * get a cached dY-value,
     * @param numberDown the number of the previous value we want
     * @return the corresponding dY-value
     */
    public double[] getDY(final int numberDown)
    {
        if (this.lastCachePlace < this.cachePlaces)
        {
            throw new RuntimeException("Tried to retrieve dy-value that was not yet primed");
        }
        if (numberDown >= this.cachePlaces)
        {
            throw new RuntimeException("Tried to retrieve dy-value beyond cache limits");
        }
        return this.cacheDY[(this.lastCachePlace - numberDown) % this.cachePlaces].clone();
    }

    /**
     * The integrators that extend the CachingNumericalIntegrator calculate the value of y(x+timeStep) just based on the
     * x-value. They retrieve y(x), y(x-timeStep), etc. or y(k), y(k-1) all from the cache.
     * @param x the x-value to use in the calculation
     * @return the value of y(x+timeStep)
     */
    public abstract double[] next(final double x);
}