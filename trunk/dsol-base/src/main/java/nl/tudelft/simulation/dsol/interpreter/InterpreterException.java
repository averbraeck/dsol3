/*
 * @(#) InterpreterException.java Jan 5, 2004 Copyright (c) 2002-2005 Delft
 * University of Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All
 * rights reserved. This software is proprietary information of Delft University
 * of Technology 
 */
package nl.tudelft.simulation.dsol.interpreter;

/**
 * An interpreterException.
 * <p>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:38:24 $
 * @since 1.5
 */
public class InterpreterException extends RuntimeException
{
    /**
     * constructs a new InterpreterException
     */
    public InterpreterException()
    {
        super();
    }

    /**
     * constructs a new InterpreterException
     * @param message the message
     */
    public InterpreterException(final String message)
    {
        super(message);
    }

    /**
     * constructs a new InterpreterException
     * @param message the message
     * @param cause the cause
     */
    public InterpreterException(final String message, final Throwable cause)
    {
        super(message, cause);
    }

    /**
     * constructs a new InterpreterException
     * @param cause the cause
     */
    public InterpreterException(final Throwable cause)
    {
        super(cause);
    }
}