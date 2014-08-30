package nl.tudelft.simulation.event.ref;

/**
 * A StrongReference class represents a normal pointer relation to a reference. This class is created to complete the
 * java.lang.ref package. This class ensures that references can be used without casting to either an object or a
 * reference. Strong references are not created to be cleaned by the garbage collector. Since they represent normal
 * pointer relations, they are the only ones which might be serialized. This class therefore implements
 * <code>java.io.Serializable</code>
 * <p />
 * (c) copyright 2002-2014 <a href="http://www.simulation.tudelft.nl">Delft University of Technology</a>. <br />
 * BSD-style license. See <a href="http://www.simulation.tudelft.nl/dsol/3.0/license.html">DSOL License</a>. <br />
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs</a>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
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
     * Creates a new strong reference that refers to the given object. The new reference is not registered with any
     * queue.
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