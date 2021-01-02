package nl.tudelft.simulation.dsol.simtime;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * The SimTime class with a Calendar as the absolute time, and a long in milliseconds as the relative time.
 * <p>
 * Copyright (c) 2016-2021 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class SimTimeCalendarLong extends SimTime<Calendar, Long, SimTimeCalendarLong>
{
    /** */
    private static final long serialVersionUID = 1L;

    /** value represents the value in milliseconds. */
    private long timeMsec;

    /**
     * Constructor. super(time) calls set(time).
     * @param time Calendar; the calendar time item.
     */
    public SimTimeCalendarLong(final Calendar time)
    {
        super(time);
    }

    /**
     * Constructor based on (long) millisecond time.
     * @param timeMsec long; the calendar time in milliseconds.
     */
    public SimTimeCalendarLong(final long timeMsec)
    {
        super(new GregorianCalendar());
        this.timeMsec = timeMsec;
    }

    /** {@inheritDoc} */
    @Override
    public final void add(final Long relativeTime)
    {
        this.timeMsec += relativeTime;
    }

    /** {@inheritDoc} */
    @Override
    public final void subtract(final Long relativeTime)
    {
        this.timeMsec -= relativeTime;
    }

    /** {@inheritDoc} */
    @Override
    public final Long diff(final Calendar simTime)
    {
        return this.timeMsec - simTime.getTimeInMillis();
    }

    /** {@inheritDoc} */
    @Override
    public final int compareTo(final SimTimeCalendarLong simTime)
    {
        return Long.compare(this.timeMsec, simTime.timeMsec);
    }

    /** {@inheritDoc} */
    @Override
    public final SimTimeCalendarLong setZero()
    {
        this.timeMsec = 0L;
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public final SimTimeCalendarLong copy()
    {
        return new SimTimeCalendarLong(get());
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
        cal.setTimeInMillis(this.timeMsec);
        return cal;
    }

    /**
     * @return timeMsec
     */
    public final long getTimeMsec()
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
    public Long getRelativeZero()
    {
        return 0L;
    }

}
