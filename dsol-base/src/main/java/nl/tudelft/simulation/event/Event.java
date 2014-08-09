/*
 * @(#)Event April 4, 2003 Copyright (c) 2002-2005 Delft University of
 * Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved.
 * This software is proprietary information of Delft University of Technology
 * 
 */
package nl.tudelft.simulation.event;

/**
 * The Event class forms the reference implementation for the EventInterface.
 * <p>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands.
 * <p>
 * See for project information <a href="http://www.simulation.tudelft.nl/dsol/event">www.simulation.tudelft.nl/event
 * </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty
 * @author <a href="http://www.peter-jacobs.com/index.htm">Peter Jacobs </a>
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:38:11 $
 * @since 1.5
 */
public class Event implements EventInterface
{
    /** The default serial version UID for serializable classes */
    private static final long serialVersionUID = 1L;

    /** type is the type of the event */
    protected EventType type;

    /** content refers to the content of the event */
    protected Object content;

    /** the source of an event */
    protected Object source;

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

    /**
     * @see nl.tudelft.simulation.event.EventInterface#getSource()
     */
    public Object getSource()
    {
        return this.source;
    }

    /**
     * @see nl.tudelft.simulation.event.EventInterface#getContent()
     */
    public Object getContent()
    {
        return this.content;
    }

    /**
     * @see nl.tudelft.simulation.event.EventInterface#getType()
     */
    public EventType getType()
    {
        return this.type;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return "[" + this.getClass().getName() + ";" + this.getType() + ";" + this.getSource() + ";"
                + this.getContent() + "]";
    }
}