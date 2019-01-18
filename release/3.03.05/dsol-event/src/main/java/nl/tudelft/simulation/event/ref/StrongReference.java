package nl.tudelft.simulation.event.ref;

/**
 * A StrongReference class represents a normal pointer relation to a reference. This class is created to complete the
 * java.lang.ref package. This class ensures that references can be used without casting to either an object or a
 * reference. Strong references are not created to be cleaned by the garbage collector. Since they represent normal
 * pointer relations, they are the only ones which might be serialized. This class therefore implements
 * <code>java.io.Serializable</code>
 * <p>
 * Copyright (c) 2002-2019 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. See for project information <a href="https://simulation.tudelft.nl/" target="_blank">
 * https://simulation.tudelft.nl</a>. The DSOL project is distributed under a three-clause BSD-style license, which can
 * be found at <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @since 1.5
 * @param <T> the type of the reference
 */
public class StrongReference<T> extends Reference<T>
{
    /** The default serial version UID for serializable classes. */
    private static final long serialVersionUID = 1L;

    /** the referent. */
    private transient T referent = null;

    /**
     * Creates a new strong reference that refers to the given object. The new reference is not registered with any
     * queue.
     * @param referent T; object the new strong reference will refer to
     */
    public StrongReference(final T referent)
    {
        this.referent = referent;
    }

    /** {@inheritDoc} */
    @Override
    public final T get()
    {
        return this.referent;
    }

    /** {@inheritDoc} */
    @Override
    protected final void set(final T value)
    {
        this.referent = value;
    }
}
