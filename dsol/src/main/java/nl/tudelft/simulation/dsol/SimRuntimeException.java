/*
 * @(#)SimRuntimeException.java April 4, 2003 Copyright (c) 2002-2005 Delft
 * University of Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All
 * rights reserved. This software is proprietary information of Delft University
 * of Technology 
 */
package nl.tudelft.simulation.dsol;

/**
 * This class defines SimRuntimeException. This exception is thrown throughout DSOL whenever exceptions occur which are
 * directly linked to the simulator. <br>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl"> www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:36:43 $
 * @author <a href="http://www.peter-jacobs.com/index.htm">Peter Jacobs </a>, <a href="mailto:nlang@fbk.eur.nl">Niels
 *         Lang </a><a href="mailto:a.verbraeck@tudelft.nl">Alexander Verbraeck </a>
 */
public class SimRuntimeException extends Exception
{
    /** The default serial version UID for serializable classes */
    private static final long serialVersionUID = 1L;

    /**
     * Constructor for SimRuntimeException.
     */
    public SimRuntimeException()
    {
        super();
    }

    /**
     * constructs a new SimRuntimeException
     * @param message the exception message
     */
    public SimRuntimeException(final String message)
    {
        super(message);
    }

    /**
     * constructs a new SimRuntimeException
     * @param message the exception message
     * @param cause the originating throwable
     */
    public SimRuntimeException(final String message, final Throwable cause)
    {
        super(message, cause);
    }

    /**
     * Constructor for SimRuntimeException.
     * @param cause the originating throwable
     */
    public SimRuntimeException(final Throwable cause)
    {
        super(cause);
    }
}