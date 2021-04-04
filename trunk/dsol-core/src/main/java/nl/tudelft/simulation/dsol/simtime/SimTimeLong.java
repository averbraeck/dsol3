package nl.tudelft.simulation.dsol.simtime;

/**
 * The SimTime class with a long as the absolute time, and a long as the relative time. No units are used.
 * <p>
 * Copyright (c) 2016-2021 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class SimTimeLong extends SimTime<Long, Long, SimTimeLong>
{
    /** */
    private static final long serialVersionUID = 20140803L;

    /** the locally stored time. */
    private long time;

    /**
     * @param time long; the initial time.
     */
    public SimTimeLong(final long time)
    {
        super(time);
    }

    /** {@inheritDoc} */
    @Override
    public final void add(final Long relativeTime)
    {
        this.time += relativeTime;
    }

    /** {@inheritDoc} */
    @Override
    public final void subtract(final Long relativeTime)
    {
        this.time -= relativeTime;
    }

    /** {@inheritDoc} */
    @Override
    public final Long diff(final Long simTime)
    {
        return this.get().longValue() - simTime.longValue();
    }

    /** {@inheritDoc} */
    @Override
    public final void set(final Long absoluteTime)
    {
        this.time = absoluteTime;
    }

    /** {@inheritDoc} */
    @Override
    public final Long get()
    {
        return this.time;
    }

    /** {@inheritDoc} */
    @Override
    public final SimTimeLong setZero()
    {
        this.time = 0L;
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public final SimTimeLong copy()
    {
        return new SimTimeLong(this.time);
    }

    /** {@inheritDoc} */
    @Override
    public Long getAbsoluteZero()
    {
        return 0L;
    }

    /** {@inheritDoc} */
    @Override
    public Long getRelativeZero()
    {
        return 0L;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (this.time ^ (this.time >>> 32));
        return result;
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("checkstyle:needbraces")
    public boolean equals(final Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SimTimeLong other = (SimTimeLong) obj;
        if (this.time != other.time)
            return false;
        return true;
    }

}
