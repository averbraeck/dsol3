package nl.tudelft.simulation.dsol.simtime;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * <p>
 * Copyright (c) 2002-2014 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved.
 * <p>
 * See for project information <a href="http://www.simulation.tudelft.nl/"> www.simulation.tudelft.nl</a>.
 * <p>
 * The DSOL project is distributed under the following BSD-style license:<br>
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
 * following conditions are met:
 * <ul>
 * <li>Redistributions of source code must retain the above copyright notice, this list of conditions and the following
 * disclaimer.</li>
 * <li>Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the
 * following disclaimer in the documentation and/or other materials provided with the distribution.</li>
 * <li>Neither the name of Delft University of Technology, nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.</li>
 * </ul>
 * This software is provided by the copyright holders and contributors "as is" and any express or implied warranties,
 * including, but not limited to, the implied warranties of merchantability and fitness for a particular purpose are
 * disclaimed. In no event shall the copyright holder or contributors be liable for any direct, indirect, incidental,
 * special, exemplary, or consequential damages (including, but not limited to, procurement of substitute goods or
 * services; loss of use, data, or profits; or business interruption) however caused and on any theory of liability,
 * whether in contract, strict liability, or tort (including negligence or otherwise) arising in any way out of the use
 * of this software, even if advised of the possibility of such damage.
 * @version Jul 25, 2014 <br>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class SimTimeCalendarLong extends SimTime<Calendar, UnitTimeLong, SimTimeCalendarLong>
{
    /** */
    private static final long serialVersionUID = 1L;

    /** value represents the value in milliseconds */
    private long timeMsec;

    /**
     * Constructor. super(time) calls set(time).
     * @param time the calendar time item.
     */
    public SimTimeCalendarLong(final Calendar time)
    {
        super(time);
    }

    /**
     * Constructor based on (long) millisecond time.
     * @param timeMsec the calendar time in milliseconds.
     */
    public SimTimeCalendarLong(final long timeMsec)
    {
        super(new GregorianCalendar());
        this.timeMsec = timeMsec;
    }

    /**
     * @see nl.tudelft.simulation.dsol.simtime.SimTime#add(java.lang.Comparable)
     */
    @Override
    public void add(UnitTimeLong simTime)
    {
        this.timeMsec += simTime.getTimeMsec();
    }

    /**
     * @see nl.tudelft.simulation.dsol.simtime.SimTime#add(java.lang.Comparable)
     */
    @Override
    public void subtract(UnitTimeLong simTime)
    {
        this.timeMsec -= simTime.getTimeMsec();
    }

    /**
     * @see nl.tudelft.simulation.dsol.simtime.SimTime#minus(nl.tudelft.simulation.dsol.simtime.SimTime)
     */
    @Override
    public UnitTimeLong minus(final SimTimeCalendarLong absoluteTime)
    {
        UnitTimeLong ret = new UnitTimeLong(this.timeMsec, TimeUnit.MILLISECOND);
        ret.setTimeMsec(ret.getTimeMsec() - absoluteTime.timeMsec);
        return ret;
    }

    /**
     * @see nl.tudelft.simulation.dsol.simtime.SimTime#compareTo(nl.tudelft.simulation.dsol.simtime.SimTime)
     */
    @Override
    public int compareTo(SimTimeCalendarLong simTime)
    {
        return Long.compare(this.timeMsec, simTime.timeMsec);
    }

    /**
     * @see nl.tudelft.simulation.dsol.simtime.SimTime#setZero()
     */
    @Override
    public SimTimeCalendarLong setZero()
    {
        this.timeMsec = 0L;
        return this;
    }

    /**
     * @see nl.tudelft.simulation.dsol.simtime.SimTime#copy()
     */
    @Override
    public SimTimeCalendarLong copy()
    {
        return new SimTimeCalendarLong(get());
    }

    /**
     * @see nl.tudelft.simulation.dsol.simtime.SimTime#set(java.lang.Comparable)
     */
    @Override
    public void set(Calendar value)
    {
        this.timeMsec = value.getTimeInMillis();
    }

    /**
     * @see nl.tudelft.simulation.dsol.simtime.SimTime#get()
     */
    @Override
    public Calendar get()
    {
        Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis(this.timeMsec);
        return cal;
    }

    /**
     * @return timeMsec
     */
    public long getTimeMsec()
    {
        return this.timeMsec;
    }

}
