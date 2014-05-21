/*
 * @(#)TimedEventInterface.java April 4, 2003 Copyright (c) 2002-2005 Delft
 * University of Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All
 * rights reserved. This software is proprietary information of Delft University
 * of Technology 
 */
package nl.tudelft.simulation.event;

/**
 * The TimedEvent is the reference implementation for a timed event.
 * <p>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft
 * University of Technology </a>, the Netherlands.
 * <p>
 * See for project information <a
 * href="http://www.simulation.tudelft.nl/dsol/event">www.simulation.tudelft.nl/event
 * </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser
 * General Public License (LGPL) </a>, no warranty
 * 
 * @author <a href="http://www.peter-jacobs.com">Peter Jacobs </a>
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:38:12 $
 * @since 1.5
 */
public class TimedEvent extends Event
{
    /** The default serial version UID for serializable classes */
    private static final long serialVersionUID = 1L;
    
    /** timeStamp refers to the time stamp of the event */
    private double timeStamp = Double.NaN;

    /**
     * constructs a new timed event.
     * 
     * @param type the eventType of the event.
     * @param source the source of the event.
     * @param value the value of the event.
     * @param timeStamp the timeStamp.
     */
    public TimedEvent(final EventType type, final Object source,
            final Object value, final double timeStamp)
    {
        super(type, source, value);
        this.timeStamp = timeStamp;
    }

    /**
     * returns the timeStamp of this event.
     * 
     * @return the timestamp as double.
     */
    public double getTimeStamp()
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