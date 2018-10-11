package nl.tudelft.simulation.event.ref;

/**
 * A WeakReference. The WeakReference extends the <code>java.lang.ref.WeakReference</code> and besides implementing the
 * Reference interface no changes are defined.
 * <p>
 * Copyright (c) 2002-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
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
public class WeakReference<T> extends Reference<T>
{
    /** The default serial version UID for serializable classes. */
    private static final long serialVersionUID = 20140830L;

    /** the reference. */
    private transient java.lang.ref.WeakReference<T> reference = null;

    /**
     * Creates a new weak reference that refers to the given object. The new reference is not registered with any queue.
     * @param referent T; object the new weak reference will refer to
     */
    public WeakReference(final T referent)
    {
        this.reference = new java.lang.ref.WeakReference<T>(referent);
    }

    /** {@inheritDoc} */
    @Override
    public final T get()
    {
        return this.reference.get();
    }

    /** {@inheritDoc} */
    @Override
    protected final void set(final T value)
    {
        this.reference = new java.lang.ref.WeakReference<T>(value);
    }
}
