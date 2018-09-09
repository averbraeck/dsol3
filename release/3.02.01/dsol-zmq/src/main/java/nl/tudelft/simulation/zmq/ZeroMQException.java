package nl.tudelft.simulation.zmq;

/**
 * Exception for the DSOL ZeroMQ bridge.
 * <p>
 * copyright (c) 2002-2018 <a href="http://www.simulation.tudelft.nl">Delft University of Technology</a>. <br>
 * BSD-style license. See <a href="http://www.simulation.tudelft.nl/dsol/3.0/license.html">DSOL License</a>. <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs</a>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @version Oct 21, 2016
 */
public class ZeroMQException extends Exception
{
    /** */
    private static final long serialVersionUID = 30100L;

    /**
     * Create a ZeroMQ Exception.
     */
    public ZeroMQException()
    {
        super();
    }

    /**
     * Create a ZeroMQ Exception.
     * @param message the message
     */
    public ZeroMQException(final String message)
    {
        super(message);
    }

    /**
     * Create a ZeroMQ Exception.
     * @param cause the exception that caused the ZeroMQ exception
     */
    public ZeroMQException(final Throwable cause)
    {
        super(cause);
    }

    /**
     * Create a ZeroMQ Exception.
     * @param message the message
     * @param cause the exception that caused the ZeroMQ exception
     */
    public ZeroMQException(final String message, final Throwable cause)
    {
        super(message, cause);
    }

    /**
     * Create a ZeroMQ Exception.
     * @param message the message
     * @param cause the exception that caused the ZeroMQ exception
     * @param enableSuppression to enable suppressions or not
     * @param writableStackTrace to have a writable stack trace or not
     */
    public ZeroMQException(final String message, final Throwable cause, final boolean enableSuppression,
            final boolean writableStackTrace)
    {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
