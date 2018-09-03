package nl.tudelft.simulation.event;

/**
 * The TimedEvent is the reference implementation for a timed event.
 * <p>
 * copyright (c) 2002-2018  <a href="http://www.simulation.tudelft.nl">Delft University of Technology</a>. <br>
 * BSD-style license. See <a href="http://www.simulation.tudelft.nl/dsol/3.0/license.html">DSOL License</a>. <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs</a>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
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
     * @param type the eventType of the event.
     * @param source the source of the event.
     * @param value the value of the event.
     * @param timeStamp the timeStamp.
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
