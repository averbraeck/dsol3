/*
 * @(#) StrongReference.java Dec 9, 2003 Copyright (c) 2002-2005 Delft
 * University of Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All
 * rights reserved. This software is proprietary information of Delft University
 * of Technology 
 */
package nl.tudelft.simulation.event.ref;

/**
 * A StrongReference class represents a normal pointer relation to a reference.
 * This class is created to complete the java.lang.ref package. This class
 * ensures that references can be used without casting to either an object or a
 * reference. Strong references are not created to be cleaned by the garbage
 * collector. Since they represent normal pointer relations, they are the only
 * ones which might be serialized. This class therefore implements
 * <code>java.io.Serializable</code>
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
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:38:11 $
 * @since 1.5
 * @param <T> the type of the reference
 */
public class StrongReference<T> extends Reference<T>
{
    /** The default serial version UID for serializable classes */
    private static final long serialVersionUID = 1L;
    
    /** the referent */
    private transient T referent = null;

    /**
     * Creates a new strong reference that refers to the given object. The new
     * reference is not registered with any queue.
     * 
     * @param referent object the new strong reference will refer to
     */
    public StrongReference(final T referent)
    {
        this.referent = referent;
    }

    /**
     * @see nl.tudelft.simulation.event.ref.Reference#get()
     */
    @Override
	public T get()
    {
        return this.referent;
    }

    /**
     * @see nl.tudelft.simulation.event.ref.Reference#set(java.lang.Object)
     */
    @Override
	protected void set(final T value)
    {
        this.referent = value;
    }
}