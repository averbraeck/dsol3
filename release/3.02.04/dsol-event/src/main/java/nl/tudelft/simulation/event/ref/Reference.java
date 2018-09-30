package nl.tudelft.simulation.event.ref;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * A Reference interface defining the indirect pointer access to an object.
 * <p>
 * copyright (c) 2002-2018  <a href="https://simulation.tudelft.nl">Delft University of Technology</a>. <br>
 * BSD-style license. See <a href="https://simulation.tudelft.nl/dsol/3.0/license.html">DSOL License</a>. <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs</a>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @see java.lang.ref.Reference
 * @since 1.5
 * @param <T> the type of the reference
 */
public abstract class Reference<T> implements Serializable
{
    /** The default serial version UID for serializable classes. */
    private static final long serialVersionUID = 20140830L;

    /**
     * Returns this reference object's referent. If this reference object has been cleared, either by the program or by
     * the garbage collector, then this method returns <code>null</code>.
     * @return The object to which this reference refers, or <code>null</code> if this reference object has been
     *         cleared.
     */
    public abstract T get();

    /**
     * sets the value of the reference
     * @param value the value to set
     */
    protected abstract void set(final T value);

    /**
     * writes a serializable method to stream
     * @param out the output stream
     * @throws IOException on IOException
     */
    private synchronized void writeObject(final ObjectOutputStream out) throws IOException
    {
        out.writeObject(this.get());
    }

    /**
     * reads a serializable method from stream
     * @param in the input stream
     * @throws IOException on IOException
     * @throws ClassNotFoundException on ClassNotFoundException
     */
    @SuppressWarnings("unchecked")
    private void readObject(final java.io.ObjectInputStream in) throws IOException, ClassNotFoundException
    {
        this.set((T) in.readObject());
    }
}
