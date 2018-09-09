package nl.tudelft.simulation.language;

/**
 * DSOLException is used for generic exceptions in the DSOL packages. <br>
 * <br>
 * Copyright (c) 2003-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. See for project information
 * <a href="https://www.simulation.tudelft.nl/" target="_blank">www.simulation.tudelft.nl</a>. The source code and
 * binary code of this software is proprietary information of Delft University of Technology.
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank">Alexander Verbraeck</a>
 */
public class DSOLException extends Exception
{
    /** */
    private static final long serialVersionUID = 1L;

    /** */
    public DSOLException()
    {
        // empty constructor
    }

    /**
     * @param message the message of the exception
     */
    public DSOLException(final String message)
    {
        super(message);
    }

    /**
     * @param cause the exception that caused this exception to be triggered
     */
    public DSOLException(final Throwable cause)
    {
        super(cause);
    }

    /**
     * @param message the message of the exception
     * @param cause the exception that caused this exception to be triggered
     */
    public DSOLException(final String message, final Throwable cause)
    {
        super(message, cause);
    }

    /**
     * @param message the message of the exception
     * @param cause the exception that caused this exception to be triggered
     * @param enableSuppression whether or not suppression is enabled or disabled
     * @param writableStackTrace whether or not the stack trace should be writable
     */
    public DSOLException(final String message, final Throwable cause, final boolean enableSuppression,
            final boolean writableStackTrace)
    {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
