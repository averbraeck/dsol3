package nl.tudelft.simulation.dsol.simtime;

/**
 * The SimTime class with a float as the absolute time, and a float as the relative time. No units are used.
 * <p>
 * Copyright (c) 2016-2021 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class SimTimeFloat extends SimTime<Float, Float, SimTimeFloat>
{
    /** */
    private static final long serialVersionUID = 20140803L;

    /** the locally stored time. */
    private float time;

    /**
     * @param time float; the initial time.
     */
    public SimTimeFloat(final float time)
    {
        super(time);
    }

    /** {@inheritDoc} */
    @Override
    public final void add(final Float relativeTime)
    {
        this.time += relativeTime;
    }

    /** {@inheritDoc} */
    @Override
    public final void subtract(final Float relativeTime)
    {
        this.time -= relativeTime;
    }

    /** {@inheritDoc} */
    @Override
    public final Float diff(final Float simTime)
    {
        return this.get().floatValue() - simTime.floatValue();
    }

    /** {@inheritDoc} */
    @Override
    public final void set(final Float absoluteValue)
    {
        this.time = absoluteValue;
    }

    /** {@inheritDoc} */
    @Override
    public final Float get()
    {
        return this.time;
    }

    /** {@inheritDoc} */
    @Override
    public final SimTimeFloat setZero()
    {
        this.time = 0.0f;
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public final SimTimeFloat copy()
    {
        return new SimTimeFloat(this.time);
    }

    /** {@inheritDoc} */
    @Override
    public Float getAbsoluteZero()
    {
        return 0.0f;
    }

    /** {@inheritDoc} */
    @Override
    public Float getRelativeZero()
    {
        return 0.0f;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + Float.floatToIntBits(this.time);
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
        SimTimeFloat other = (SimTimeFloat) obj;
        if (Float.floatToIntBits(this.time) != Float.floatToIntBits(other.time))
            return false;
        return true;
    }

}
