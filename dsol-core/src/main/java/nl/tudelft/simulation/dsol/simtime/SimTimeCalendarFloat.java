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
public class SimTimeCalendarFloat extends SimTime<Calendar, UnitTimeFloat, SimTimeCalendarFloat>
{
    /** */
    private static final long serialVersionUID = 20140803L;

    /** value represents the value in milliseconds */
    private float timeMsec;

    /**
     * Constructor. super(time) calls set(time).
     * @param time the calendar time item.
     */
    public SimTimeCalendarFloat(final Calendar time)
    {
        super(time);
    }

    /**
     * Constructor based on (float) millisecond time.
     * @param timeMsec the calendar time in milliseconds.
     */
    public SimTimeCalendarFloat(final float timeMsec)
    {
        super(new GregorianCalendar());
        this.timeMsec = timeMsec;
    }

    /**
     * @see nl.tudelft.simulation.dsol.simtime.SimTime#add(java.lang.Comparable)
     */
    @Override
    public void add(UnitTimeFloat simTime)
    {
        this.timeMsec += simTime.getTimeMsec();
    }

    /**
     * @see nl.tudelft.simulation.dsol.simtime.SimTime#add(java.lang.Comparable)
     */
    @Override
    public void subtract(UnitTimeFloat simTime)
    {
        this.timeMsec -= simTime.getTimeMsec();
    }

    /**
     * @see nl.tudelft.simulation.dsol.simtime.SimTime#minus(nl.tudelft.simulation.dsol.simtime.SimTime)
     */
    @Override
    public UnitTimeFloat minus(final SimTimeCalendarFloat absoluteTime)
    {
        UnitTimeFloat ret = new UnitTimeFloat(this.timeMsec, TimeUnit.MILLISECOND);
        ret.setTimeMsec(ret.getTimeMsec() - absoluteTime.timeMsec);
        return ret;
    }

    /**
     * @see nl.tudelft.simulation.dsol.simtime.SimTime#compareTo(nl.tudelft.simulation.dsol.simtime.SimTime)
     */
    @Override
    public int compareTo(SimTimeCalendarFloat simTime)
    {
        return Float.compare(this.timeMsec, simTime.timeMsec);
    }

    /**
     * @see nl.tudelft.simulation.dsol.simtime.SimTime#setZero()
     */
    @Override
    public SimTimeCalendarFloat setZero()
    {
        this.timeMsec = 0.0f;
        return this;
    }

    /**
     * @see nl.tudelft.simulation.dsol.simtime.SimTime#copy()
     */
    @Override
    public SimTimeCalendarFloat copy()
    {
        return new SimTimeCalendarFloat(get());
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
        cal.setTimeInMillis((long) this.timeMsec);
        return cal;
    }

    /**
     * @return timeMsec
     */
    public float getTimeMsec()
    {
        return this.timeMsec;
    }

}
