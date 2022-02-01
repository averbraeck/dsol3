package nl.tudelft.simulation.zmq;

/**
 * Exception for the DSOL ZeroMQ bridge.
 * <p>
 * Copyright (c) 2002-2022 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs</a>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
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
     * @param message String; the message
     */
    public ZeroMQException(final String message)
    {
        super(message);
    }

    /**
     * Create a ZeroMQ Exception.
     * @param cause Throwable; the exception that caused the ZeroMQ exception
     */
    public ZeroMQException(final Throwable cause)
    {
        super(cause);
    }

    /**
     * Create a ZeroMQ Exception.
     * @param message String; the message
     * @param cause Throwable; the exception that caused the ZeroMQ exception
     */
    public ZeroMQException(final String message, final Throwable cause)
    {
        super(message, cause);
    }

    /**
     * Create a ZeroMQ Exception.
     * @param message String; the message
     * @param cause Throwable; the exception that caused the ZeroMQ exception
     * @param enableSuppression boolean; to enable suppressions or not
     * @param writableStackTrace boolean; to have a writable stack trace or not
     */
    public ZeroMQException(final String message, final Throwable cause, final boolean enableSuppression,
            final boolean writableStackTrace)
    {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
