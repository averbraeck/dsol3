package nl.tudelft.simulation.dsol.simtime;

import nl.tudelft.simulation.event.Event;
import nl.tudelft.simulation.event.EventType;

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
public class SimTimeEvent extends Event
{
    /** The default serial version UID for serializable classes */
    private static final long serialVersionUID = 1L;

    /** timeStamp refers to the time stamp of the event */
    private SimTime<?, ?, ?> timeStamp = null;

    /**
     * constructs a new timed event.
     * @param type the eventType of the event.
     * @param source the source of the event.
     * @param value the value of the event.
     * @param timeStamp the timeStamp.
     */
    public SimTimeEvent(final EventType type, final Object source, final Object value, final SimTime<?, ?, ?> timeStamp)
    {
        super(type, source, value);
        this.timeStamp = timeStamp;
    }

    /**
     * returns the timeStamp of this event.
     * @return the timestamp as double.
     */
    public SimTime<?, ?, ?> getTimeStamp()
    {
        return this.timeStamp;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return super.toString().split("]")[0] + ";" + this.getTimeStamp() + "]";
    }

}
