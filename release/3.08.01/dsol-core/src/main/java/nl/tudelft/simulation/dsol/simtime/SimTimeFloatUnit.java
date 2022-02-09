package nl.tudelft.simulation.dsol.simtime;

import org.djunits.value.vfloat.scalar.FloatDuration;
import org.djunits.value.vfloat.scalar.FloatTime;

/**
 * The SimTime class with a FloatTime as the absolute time, and a FloatDuration as the relative time. The units are defined in
 * the djunits package. More information can be found at <a href="https://djunits.org">https://djunits.org</a>.
 * <p>
 * Copyright (c) 2016-2022 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class SimTimeFloatUnit extends SimTime<FloatTime, FloatDuration, SimTimeFloatUnit>
{
    /** */
    private static final long serialVersionUID = 1L;

    /** time as a DJUNITS object. */
    private FloatTime time;

    /**
     * @param time FloatTime; the initial time.
     */
    public SimTimeFloatUnit(final FloatTime time)
    {
        super(time);
    }

    /** {@inheritDoc} */
    @Override
    public void add(final FloatDuration relativeTime)
    {
        this.time = this.time.plus(relativeTime);
    }

    /** {@inheritDoc} */
    @Override
    public void subtract(final FloatDuration relativeTime)
    {
        this.time = this.time.minus(relativeTime);
    }

    /** {@inheritDoc} */
    @Override
    public FloatDuration diff(final FloatTime simTime)
    {
        return this.time.minus(simTime);
    }

    /** {@inheritDoc} */
    @Override
    public SimTimeFloatUnit setZero()
    {
        this.time = FloatTime.ZERO;
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public SimTimeFloatUnit copy()
    {
        return new SimTimeFloatUnit(this.time);
    }

    /** {@inheritDoc} */
    @Override
    public void set(final FloatTime absoluteTime)
    {
        this.time = absoluteTime;
    }

    /** {@inheritDoc} */
    @Override
    public FloatTime get()
    {
        return this.time;
    }

    /** {@inheritDoc} */
    @Override
    public FloatTime getAbsoluteZero()
    {
        return FloatTime.ZERO;
    }

    /** {@inheritDoc} */
    @Override
    public FloatDuration getRelativeZero()
    {
        return FloatDuration.ZERO;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.time == null) ? 0 : this.time.hashCode());
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
        SimTimeFloatUnit other = (SimTimeFloatUnit) obj;
        if (this.time == null)
        {
            if (other.time != null)
                return false;
        }
        else if (!this.time.equals(other.time))
            return false;
        return true;
    }

}
