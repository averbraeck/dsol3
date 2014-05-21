/*
 * @(#) Reference.java Dec 9, 2003 Copyright (c) 2002-2005 Delft University of
 * Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved.
 * This software is proprietary information of Delft University of Technology
 * 
 */
package nl.tudelft.simulation.event.ref;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * A Reference interface defining the indirect pointer access to an object.
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
 * @see java.lang.ref.Reference
 * @since 1.5
 * @param <T>
 */
public abstract class Reference<T> implements Serializable
{
    /** The default serial version UID for serializable classes */
    private static final long serialVersionUID = 1L;
    
    /**
     * Returns this reference object's referent. If this reference object has
     * been cleared, either by the program or by the garbage collector, then
     * this method returns <code>null</code>.
     * 
     * @return The object to which this reference refers, or <code>null</code>
     *         if this reference object has been cleared
     */
    public abstract T get();

    /**
     * sets the value of the reference
     * 
     * @param value the value to set
     */
    protected abstract void set(final T value);

    /**
     * writes a serializable method to stream
     * 
     * @param out the outputstream
     * @throws IOException on IOException
     */
    private synchronized void writeObject(final ObjectOutputStream out)
            throws IOException
    {
        out.writeObject(this.get());
    }
    
    /**
     * reads a serializable method from stream
     * 
     * @param in the inputstream
     * @throws IOException on IOException
     * @throws ClassNotFoundException on ClassNotFoundException
     */
    @SuppressWarnings("unchecked")
    private synchronized void readObject(final java.io.ObjectInputStream in)
            throws IOException, ClassNotFoundException
    {
        this.set((T) in.readObject());
    }
}