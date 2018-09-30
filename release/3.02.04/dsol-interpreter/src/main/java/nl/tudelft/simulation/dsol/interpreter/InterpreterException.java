package nl.tudelft.simulation.dsol.interpreter;

/**
 * An interpreterException.
 * <p>
 * copyright (c) 2002-2018 <a href="https://simulation.tudelft.nl">Delft University of Technology</a>. <br>
 * BSD-style license. See <a href="https://simulation.tudelft.nl/dsol/3.0/license.html">DSOL License</a>. <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs</a>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @since 1.5
 */
public class InterpreterException extends RuntimeException
{
    /** */
    private static final long serialVersionUID = 20140830L;

    /**
     * constructs a new InterpreterException.
     */
    public InterpreterException()
    {
        super();
    }

    /**
     * constructs a new InterpreterException.
     * @param message the message
     */
    public InterpreterException(final String message)
    {
        super(message);
    }

    /**
     * constructs a new InterpreterException.
     * @param message the message
     * @param cause the cause
     */
    public InterpreterException(final String message, final Throwable cause)
    {
        super(message, cause);
    }

    /**
     * constructs a new InterpreterException.
     * @param cause the cause
     */
    public InterpreterException(final Throwable cause)
    {
        super(cause);
    }
}
