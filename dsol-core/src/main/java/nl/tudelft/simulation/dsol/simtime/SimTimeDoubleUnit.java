package nl.tudelft.simulation.dsol.simtime;

import org.djunits.value.vdouble.scalar.Duration;
import org.djunits.value.vdouble.scalar.Time;

/**
 * <p>
 * Copyright (c) 2002-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved.
 * <p>
 * See for project information <a href="https://simulation.tudelft.nl/"> www.simulation.tudelft.nl</a>.
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
public class SimTimeDoubleUnit extends SimTime<Time, Duration, SimTimeDoubleUnit>
{
    /** */
    private static final long serialVersionUID = 1L;

    /** time as a DJUNITS object. */
    private Time time;

    /**
     * @param time the initial time.
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
    public Duration minus(final SimTimeDoubleUnit simTime)
    {
        return this.time.minus(simTime.get());
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

}
