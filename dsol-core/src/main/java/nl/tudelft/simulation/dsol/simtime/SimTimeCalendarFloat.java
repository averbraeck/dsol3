package nl.tudelft.simulation.dsol.simtime;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.djunits.unit.DurationUnit;
import org.djunits.value.vfloat.scalar.FloatDuration;

/**
 * The SimTime class with a Calendar as the absolute time, and a float in milliseconds as the relative time.
 * <p>
 * Copyright (c) 2016-2021 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class SimTimeCalendarFloat extends SimTime<Calendar, FloatDuration, SimTimeCalendarFloat>
{
    /** */
    private static final long serialVersionUID = 20140803L;

    /** value represents the value in milliseconds. */
    private float timeMsec;

    /**
     * Constructor. super(time) calls set(time).
     * @param time Calendar; the calendar time item.
     */
    public SimTimeCalendarFloat(final Calendar time)
    {
        super(time);
    }

    /**
     * Constructor based on (float) millisecond time.
     * @param timeMsec float; the calendar time in milliseconds.
     */
    public SimTimeCalendarFloat(final float timeMsec)
    {
        super(new GregorianCalendar());
        this.timeMsec = timeMsec;
    }

    /** {@inheritDoc} */
    @Override
    public final void add(final FloatDuration relativeTime)
    {
        this.timeMsec += 1000.0f * relativeTime.si;
    }

    /** {@inheritDoc} */
    @Override
    public final void subtract(final FloatDuration relativeTime)
    {
        this.timeMsec -= 1000.0f * relativeTime.si;
    }

    /** {@inheritDoc} */
    @Override
    public final FloatDuration diff(final Calendar simTime)
    {
        return new FloatDuration(this.timeMsec - simTime.getTimeInMillis(), DurationUnit.MILLISECOND);
    }

    /** {@inheritDoc} */
    @Override
    public final int compareTo(final SimTimeCalendarFloat simTime)
    {
        return Float.compare(this.timeMsec, simTime.timeMsec);
    }

    /** {@inheritDoc} */
    @Override
    public final SimTimeCalendarFloat setZero()
    {
        this.timeMsec = 0.0f;
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public final SimTimeCalendarFloat copy()
    {
        return new SimTimeCalendarFloat(get());
    }

    /** {@inheritDoc} */
    @Override
    public final void set(final Calendar absoluteTime)
    {
        this.timeMsec = absoluteTime.getTimeInMillis();
    }

    /** {@inheritDoc} */
    @Override
    public final Calendar get()
    {
        Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis((long) this.timeMsec);
        return cal;
    }

    /**
     * @return timeMsec
     */
    public final float getTimeMsec()
    {
        return this.timeMsec;
    }

    /** {@inheritDoc} */
    @Override
    public Calendar getAbsoluteZero()
    {
        return this.copy().setZero().get();
    }

    /** {@inheritDoc} */
    @Override
    public FloatDuration getRelativeZero()
    {
        return FloatDuration.ZERO;
    }

}
