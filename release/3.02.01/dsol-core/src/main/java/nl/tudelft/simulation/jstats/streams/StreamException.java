package nl.tudelft.simulation.jstats.streams;

/**
 * copyright (c) 2002-2018  <a href="https://simulation.tudelft.nl">Delft University of Technology</a>. <br>
 * BSD-style license. See <a href="https://simulation.tudelft.nl/dsol/3.0/license.html">DSOL License</a>. <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs</a>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @version Apr 26, 2015
 */
public class StreamException extends Exception
{
    /** */
    private static final long serialVersionUID = 1L;

    /** */
    public StreamException()
    {
        super();
    }

    /**
     * @param message the description of the exception.
     */
    public StreamException(final String message)
    {
        super(message);
    }

    /**
     * @param cause the earlier exception on which this exception is based.
     */
    public StreamException(final Throwable cause)
    {
        super(cause);
    }

    /**
     * @param message the description of the exception.
     * @param cause the earlier exception on which this exception is based.
     */
    public StreamException(final String message, final Throwable cause)
    {
        super(message, cause);
    }

    /**
     * @param message the description of the exception.
     * @param cause the earlier exception on which this exception is based.
     * @param enableSuppression ..
     * @param writableStackTrace ..
     */
    public StreamException(final String message, final Throwable cause, final boolean enableSuppression,
            final boolean writableStackTrace)
    {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
