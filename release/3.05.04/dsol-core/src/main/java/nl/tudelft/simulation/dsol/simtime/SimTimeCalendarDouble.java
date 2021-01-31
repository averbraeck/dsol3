package nl.tudelft.simulation.dsol.simtime;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.djunits.unit.DurationUnit;
import org.djunits.value.vdouble.scalar.Duration;

/**
 * The SimTime class with a Calendar as the absolute time, and a double in milliseconds as the relative time.
 * <p>
 * Copyright (c) 2016-2021 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class SimTimeCalendarDouble extends SimTime<Calendar, Duration, SimTimeCalendarDouble>
{
    /** */
    private static final long serialVersionUID = 20140803L;

    /** value represents the value in milliseconds. */
    private double timeMsec;

    /**
     * Constructor. super(time) calls set(time).
     * @param time Calendar; the calendar time item.
     */
    public SimTimeCalendarDouble(final Calendar time)
    {
        super(time);
    }

    /**
     * Constructor based on (double) millisecond time.
     * @param timeMsec double; the calendar time in milliseconds.
     */
    public SimTimeCalendarDouble(final double timeMsec)
    {
        super(new GregorianCalendar());
        this.timeMsec = timeMsec;
    }

    /** {@inheritDoc} */
    @Override
    public final void add(final Duration relativeTime)
    {
        this.timeMsec += 1000.0 * relativeTime.si;
    }

    /** {@inheritDoc} */
    @Override
    public final void subtract(final Duration relativeTime)
    {
        this.timeMsec -= 1000 * relativeTime.si;
    }

    /** {@inheritDoc} */
    @Override
    public final Duration diff(final Calendar simTime)
    {
        return new Duration(this.timeMsec - simTime.getTimeInMillis(), DurationUnit.MILLISECOND);
    }

    /** {@inheritDoc} */
    @Override
    public final int compareTo(final SimTimeCalendarDouble simTime)
    {
        return Double.compare(this.timeMsec, simTime.timeMsec);
    }

    /** {@inheritDoc} */
    @Override
    public final SimTimeCalendarDouble setZero()
    {
        this.timeMsec = 0.0d;
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public final SimTimeCalendarDouble copy()
    {
        return new SimTimeCalendarDouble(get());
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
    public final double getTimeMsec()
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
    public Duration getRelativeZero()
    {
        return Duration.ZERO;
    }

}
