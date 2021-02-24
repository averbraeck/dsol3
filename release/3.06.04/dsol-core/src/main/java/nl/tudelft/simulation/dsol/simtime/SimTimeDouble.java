package nl.tudelft.simulation.dsol.simtime;

/**
 * The SimTime class with a double as the absolute time, and a double as the relative time. No units are used.
 * <p>
 * Copyright (c) 2016-2021 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class SimTimeDouble extends SimTime<Double, Double, SimTimeDouble>
{
    /** */
    private static final long serialVersionUID = 20140803L;

    /** the locally stored time. */
    private double time;

    /**
     * @param time double; the initial time.
     */
    public SimTimeDouble(final double time)
    {
        super(time);
    }

    /** {@inheritDoc} */
    @Override
    public final void add(final Double relativeTime)
    {
        this.time += relativeTime;
    }

    /** {@inheritDoc} */
    @Override
    public final void subtract(final Double relativeTime)
    {
        this.time -= relativeTime;
    }

    /** {@inheritDoc} */
    @Override
    public final Double diff(final Double simTime)
    {
        return this.time - simTime;
    }

    /** {@inheritDoc} */
    @Override
    public final void set(final Double absoluteTime)
    {
        this.time = absoluteTime;
    }

    /** {@inheritDoc} */
    @Override
    public final Double get()
    {
        return this.time;
    }

    /** {@inheritDoc} */
    @Override
    public final SimTimeDouble setZero()
    {
        this.time = 0.0d;
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public final SimTimeDouble copy()
    {
        return new SimTimeDouble(this.time);
    }

    /** {@inheritDoc} */
    @Override
    public Double getAbsoluteZero()
    {
        return 0.0d;
    }

    /** {@inheritDoc} */
    @Override
    public Double getRelativeZero()
    {
        return 0.0d;
    }

}
