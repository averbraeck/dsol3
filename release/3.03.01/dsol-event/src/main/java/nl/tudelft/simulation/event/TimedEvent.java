package nl.tudelft.simulation.event;

/**
 * The TimedEvent is the reference implementation for a timed event.
 * <p>
 * Copyright (c) 2002-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. See for project information <a href="https://simulation.tudelft.nl/" target="_blank">
 * https://simulation.tudelft.nl</a>. The DSOL project is distributed under a three-clause BSD-style license, which can
 * be found at <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @param <T> the object type of the timestamp
 * @since 1.5
 */
public class TimedEvent<T extends Comparable<T>> extends Event implements Comparable<TimedEvent<T>>
{
    /** The default serial version UID for serializable classes. */
    private static final long serialVersionUID = 20140826L;

    /** timeStamp refers to the time stamp of the event. */
    private final T timeStamp;

    /**
     * constructs a new timed event.
     * @param type EventType; the eventType of the event.
     * @param source Object; the source of the event.
     * @param value Object; the value of the event.
     * @param timeStamp T; the timeStamp.
     */
    public TimedEvent(final EventType type, final Object source, final Object value, final T timeStamp)
    {
        super(type, source, value);
        this.timeStamp = timeStamp;
    }

    /**
     * returns the timeStamp of this event.
     * @return the timestamp as double.
     */
    public T getTimeStamp()
    {
        return this.timeStamp;
    }

    /** {@inheritDoc} */
    @Override
    public int compareTo(TimedEvent<T> o)
    {
        return this.timeStamp.compareTo(o.getTimeStamp());
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return super.toString().split("]")[0] + ";" + this.getTimeStamp() + "]";
    }
}
