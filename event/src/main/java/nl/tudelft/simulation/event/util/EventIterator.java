/*
 * @(#) EventIterator.java Nov 19, 2003 Copyright (c) 2002-2005 Delft University
 * of Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. This software is proprietary information of Delft University of
 * Technology 
 */
package nl.tudelft.simulation.event.util;

import java.util.Iterator;

import nl.tudelft.simulation.event.EventProducer;
import nl.tudelft.simulation.event.EventType;

/**
 * The Event producing iterator provides a set to which one can subscribe interest in entry changes.
 * <p>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands.
 * <p>
 * See for project information <a href="http://www.simulation.tudelft.nl/dsol/event">www.simulation.tudelft.nl/event
 * </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty
 * @author <a href="http://www.peter-jacobs.com">Peter Jacobs </a>
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:38:12 $
 * @since 1.5
 * @param <T> the type of the iterator
 */
public class EventIterator<T> extends EventProducer implements java.util.Iterator<T>
{
    /** The default serial version UID for serializable classes */
    private static final long serialVersionUID = 1L;

    /** OBJECT_REMOVED_EVENT is fired on removel of entries */
    public static final EventType OBJECT_REMOVED_EVENT = new EventType("OBJECT_REMOVED_EVENT");

    /** our parent iterator */
    private Iterator<T> parent = null;

    /**
     * constructs a new Iterator.
     * @param parent parent.
     */
    public EventIterator(final Iterator<T> parent)
    {
        super();
        this.parent = parent;
    }

    /**
     * @see java.util.Iterator#hasNext()
     */
    public boolean hasNext()
    {
        return this.parent.hasNext();
    }

    /**
     * @see java.util.Iterator#next()
     */
    public T next()
    {
        return this.parent.next();
    }

    /**
     * @see java.util.Iterator#remove()
     */
    public void remove()
    {
        this.parent.remove();
        this.fireEvent(OBJECT_REMOVED_EVENT, null);
    }
}