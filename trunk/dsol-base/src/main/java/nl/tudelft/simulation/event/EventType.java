/*
 * @(#) EventType.java Sep 1, 2003 Copyright (c) 2002-2005 Delft University of
 * Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved.
 * This software is proprietary information of Delft University of Technology
 * 
 */
package nl.tudelft.simulation.event;

import java.io.Serializable;

/**
 * The EventType is a masker used for the subscription to asynchronous events. Eventtypes are used by EventProducers to
 * show which events they potentially fire. EventTypes should be defined as static final fields.
 * <p>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands.
 * <p>
 * See for project information <a href="http://www.simulation.tudelft.nl/dsol/event">www.simulation.tudelft.nl/event
 * </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty
 * @author <a href="http://www.peter-jacobs.com">Peter Jacobs </a>
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:38:11 $
 * @since 1.5
 */
public final class EventType implements Serializable
{
    /** The default serial version UID for serializable classes */
    private static final long serialVersionUID = 1L;

    /** value is the flag number */
    private int value;

    /** name refers to the name of the eventType */
    private String name;

    /**
     * constructs a new EventType.
     * @param name the name of this eventType. Two values are not appreciated : <code>null</code> and <code>""</code>.
     */
    public EventType(final String name)
    {
        super();
        if (name == null || name.equals(""))
        {
            throw new IllegalArgumentException("name == null || name == \"\"");
        }
        this.value = name.hashCode();
        this.name = name;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object arg0)
    {
        if (!(arg0 instanceof EventType))
        {
            return false;
        }
        return this.value == ((EventType) arg0).value;
    }

    /**
     * Returns a hash code for the name of the this eventType. The hash code for an <code>EventType</code> object is
     * computed as <blockquote>
     * 
     * <pre>
     * 
     * 
     * 
     * 
     * 
     * 
     *        s[0]*31&circ;(n-1) + s[1]*31&circ;(n-2) + ... + s[n-1]
     * 
     * 
     * 
     * 
     * 
     * 
     * </pre>
     * 
     * </blockquote> using <code>int</code> arithmetic, where <code>s[i]</code> is the <i>i </i>th character of the name
     * of the eventType, <code>n</code> is the length of the name, and <code>^</code> indicates exponentiation. This
     * algoritm assures JVM, host, time independency.
     * @return a hash code value for this object.
     */
    @Override
    public int hashCode()
    {
        return this.value;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return this.name;
    }
}