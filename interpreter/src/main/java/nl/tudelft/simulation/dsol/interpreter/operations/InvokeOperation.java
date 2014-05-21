/*
 * @(#) Operation.java Jan 12, 2004 Copyright (c) 2002-2005 Delft University of
 * Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved.
 * This software is proprietary information of Delft University of Technology
 * 
 */
package nl.tudelft.simulation.dsol.interpreter.operations;

import nl.tudelft.simulation.dsol.interpreter.Frame;
import nl.tudelft.simulation.dsol.interpreter.Operation;

/**
 * A Operation <br>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft
 * University of Technology </a>, the Netherlands. <br>
 * See for project information <a
 * href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser
 * General Public License (LGPL) </a>, no warranty.
 * 
 * @version 1.0 Jan 12, 2004 <br>
 * @author <a href="http://www.peter-jacobs.com">Peter Jacobs </a>
 */
public abstract class InvokeOperation extends Operation
{
    /**
     * executes the operation
     * 
     * @param frame The current frame
     * @return Frame the newly created frame or null when the invocation already
     *         took place
     */
    public abstract Frame execute(final Frame frame);
}