package nl.tudelft.simulation.event;

/**
 * The Event class forms the reference implementation for the EventInterface.
 * <p>
 * copyright (c) 2002-2018  <a href="https://simulation.tudelft.nl">Delft University of Technology</a>. <br>
 * BSD-style license. See <a href="https://simulation.tudelft.nl/dsol/3.0/license.html">DSOL License</a>. <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs</a>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @since 1.5
 */
public class Event implements EventInterface
{
    /** The default serial version UID for serializable classes. */
    private static final long serialVersionUID = 20140826L;

    /** type is the type of the event. */
    private final EventType type;

    /** content refers to the content of the event. */
    private final Object content;

    /** the source of an event. */
    private final Object source;

    /**
     * constructs a new Event.
     * @param type the name of the Event.
     * @param source the source of the sender.
     * @param content the content of the event.
     */
    public Event(final EventType type, final Object source, final Object content)
    {
        this.type = type;
        this.source = source;
        this.content = content;
    }

    /** {@inheritDoc} */
    @Override
    public final Object getSource()
    {
        return this.source;
    }

    /** {@inheritDoc} */
    @Override
    public final Object getContent()
    {
        return this.content;
    }

    /** {@inheritDoc} */
    @Override
    public final EventType getType()
    {
        return this.type;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return "[" + this.getClass().getName() + ";" + this.getType() + ";" + this.getSource() + ";"
                + this.getContent() + "]";
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.content == null) ? 0 : this.content.hashCode());
        result = prime * result + ((this.source == null) ? 0 : this.source.hashCode());
        result = prime * result + ((this.type == null) ? 0 : this.type.hashCode());
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(final Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null || getClass() != obj.getClass())
        {
            return false;
        }

        // content
        Event other = (Event) obj;
        if (this.content == null)
        {
            if (other.content != null)
            {
                return false;
            }
        }
        else if (!this.content.equals(other.content))
        {
            return false;
        }

        // source
        if (this.source == null)
        {
            if (other.source != null)
            {
                return false;
            }
        }
        else if (!this.source.equals(other.source))
        {
            return false;
        }

        // type
        if (this.type == null)
        {
            if (other.type != null)
            {
                return false;
            }
        }
        else if (!this.type.equals(other.type))
        {
            return false;
        }
        return true;
    }
}
