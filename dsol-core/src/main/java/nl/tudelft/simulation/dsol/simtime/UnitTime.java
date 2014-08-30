package nl.tudelft.simulation.dsol.simtime;

import java.io.Serializable;

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
 * @version Aug 3, 2014 <br>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @param <N> the number type to use to indicate time, such as Double, Float or Long.
 * @param <T> the class itself to force implementation of 'Comparable'.
 */
public abstract class UnitTime<N extends Number, T extends UnitTime<N, T>> extends Number implements Comparable<T>,
        Serializable
{
    /** */
    private static final long serialVersionUID = 20140802L;

    /** value represents the value in milliseconds. */
    protected N timeMsec;

    /** original unit. */
    protected final TimeUnit unit;

    /**
     * @param time in units.
     * @param unit the unit, e.g. HOUR.
     */
    public UnitTime(final N time, final TimeUnit unit)
    {
        this.timeMsec = convertToMsec(time, unit);
        this.unit = unit;
    }

    /**
     * @return the time in milliseconds
     */
    public N getTimeMsec()
    {
        return this.timeMsec;
    }

    /**
     * @param value the new time in milliseconds.
     */
    public void setTimeMsec(final N value)
    {
        this.timeMsec = value;
    }

    /**
     * @param time in units.
     * @param timeUnit the unit, e.g. HOUR.
     * @return the equivalent of this time in milliseconds, using the required umber type.
     */
    protected abstract N convertToMsec(final N time, final TimeUnit timeUnit);

    /**
     * @return the time in units.
     */
    public abstract N getTime();

    /**
     * @return the unit
     */
    public TimeUnit getUnit()
    {
        return this.unit;
    }

    /** {@inheritDoc} */
    @Override
    public int intValue()
    {
        return this.timeMsec.intValue();
    }

    /** {@inheritDoc} */
    @Override
    public long longValue()
    {
        return this.timeMsec.longValue();
    }

    /** {@inheritDoc} */
    @Override
    public float floatValue()
    {
        return this.timeMsec.floatValue();
    }

    /** {@inheritDoc} */
    @Override
    public double doubleValue()
    {
        return this.timeMsec.doubleValue();
    }

    /**
     * converts this unit-time to the target units
     * @param targetUnits the units to convert to
     * @return the amount in target units
     */
    public double convert(final TimeUnit targetUnits)
    {
        return this.timeMsec.doubleValue() * this.unit.getFactor() / targetUnits.getFactor();
    }

    /**
     * @param simTime
     * @return HH:MM:SS
     */
    public String formatHMS()
    {
        double hours = getHours();
        int days = (int) Math.floor(hours / 24.0);
        double currentHour = hours - 24.0 * days;
        int h = (int) Math.floor(currentHour);
        int m = (int) (((long) Math.floor(hours * 60.0)) % 60);
        int s = (int) (((long) Math.floor(hours * 3600.0)) % 60);
        return String.format("%02d:%02d:%02d", h, m, s);
    }

    /**
     * @param simTime
     * @return HHH:MM:SS
     */
    public String formatHHHMS()
    {
        double hours = getHours();
        int h = (int) Math.floor(hours);
        int m = (int) (((long) Math.floor(hours * 60.0)) % 60);
        int s = (int) (((long) Math.floor(hours * 3600.0)) % 60);
        return String.format("%02d:%02d:%02d", h, m, s);
    }

    /**
     * @return number of hours in SimTime
     */
    public double getHours()
    {
        return convert(TimeUnit.HOUR);
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return convert(this.unit) + this.unit.getAbbreviation();
    }
}
