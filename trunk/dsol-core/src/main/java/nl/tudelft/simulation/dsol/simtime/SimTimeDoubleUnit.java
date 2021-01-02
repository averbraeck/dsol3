package nl.tudelft.simulation.dsol.simtime;

import org.djunits.value.vdouble.scalar.Duration;
import org.djunits.value.vdouble.scalar.Time;

/**
 * The SimTime class with a Time as the absolute time, and a Duration as the relative time. The units are defined in the djunits
 * package. More information can be found at <a href="https://djunits.org">https://djunits.org</a>.
 * <p>
 * Copyright (c) 2016-2021 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class SimTimeDoubleUnit extends SimTime<Time, Duration, SimTimeDoubleUnit>
{
    /** */
    private static final long serialVersionUID = 1L;

    /** time as a DJUNITS object. */
    private Time time;

    /**
     * @param time Time; the initial time.
     */
    public SimTimeDoubleUnit(final Time time)
    {
        super(time);
    }

    /** {@inheritDoc} */
    @Override
    public final void add(final Duration relativeTime)
    {
        this.time = this.time.plus(relativeTime);
    }

    /** {@inheritDoc} */
    @Override
    public final void subtract(final Duration relativeTime)
    {
        this.time = this.time.minus(relativeTime);
    }

    /** {@inheritDoc} */
    @Override
    public Duration diff(final Time simTime)
    {
        return this.time.minus(simTime);
    }

    /** {@inheritDoc} */
    @Override
    public final SimTimeDoubleUnit setZero()
    {
        this.time = Time.ZERO;
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public final SimTimeDoubleUnit copy()
    {
        return new SimTimeDoubleUnit(this.time);
    }

    /** {@inheritDoc} */
    @Override
    public final void set(final Time absoluteTime)
    {
        this.time = absoluteTime;
    }

    /** {@inheritDoc} */
    @Override
    public final Time get()
    {
        return this.time;
    }

    /** {@inheritDoc} */
    @Override
    public Time getAbsoluteZero()
    {
        return Time.ZERO;
    }

    /** {@inheritDoc} */
    @Override
    public Duration getRelativeZero()
    {
        return Duration.ZERO;
    }

}
