/*
 * @(#) DRETURN.java $Date: 2010/08/10 11:38:20 $ Copyright (c) 2002-2005 Delft
 * University of Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All
 * rights reserved. This software is proprietary information of Delft University
 * of Technology 
 */
package nl.tudelft.simulation.dsol.interpreter.operations;

import java.lang.reflect.Method;

import nl.tudelft.simulation.dsol.interpreter.Frame;
import nl.tudelft.simulation.language.concurrent.Monitor;

/**
 * The DRETURN operation as defined in <a
 * href="http://java.sun.com/docs/books/vmspec/2nd-edition/html/Instructions2.doc3.html">
 * http://java.sun.com/docs/books/vmspec/2nd-edition/html/Instructions2.doc3.html
 * </a>.
 * <p>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft
 * University of Technology </a>, the Netherlands. <br>
 * See for project information <a
 * href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser
 * General Public License (LGPL) </a>, no warranty.
 * 
 * @author <a href="http://www.peter-jacobs.com/index.htm">Peter Jacobs </a><a
 *         href="mailto:a.verbraeck@tudelft.nl">Alexander
 *         Verbraeck </a>
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:38:20 $
 * @since 1.5
 */
public class DRETURN extends ReturnOperation
{
    /** OP refers to the operand code */
    public static final int OP = 175;

    /**
     * constructs a new DRETURN
     */
    public DRETURN()
    {
        super();
    }

    /**
     * @see nl.tudelft.simulation.dsol.interpreter.operations.ReturnOperation
     *      #execute(nl.tudelft.simulation.dsol.interpreter.Frame)
     */
    @Override
    public Object execute(final Frame frame)
    {
        if (ReturnOperation.isSynchronized(frame.getMethodDescriptor()
                .getMethod()))
        {
            Object monitor = null;
            if (ReturnOperation.isStatic(frame.getMethodDescriptor()
                    .getMethod()))
            {
                monitor = ((Method) frame.getMethodDescriptor().getMethod())
                        .getDeclaringClass();
            } else
            {
                monitor = frame.getLocalVariables()[0].getValue();
            }
            Monitor.unlock(monitor);
        }
        return frame.getOperandStack().pop();
    }

    /**
     * @see nl.tudelft.simulation.dsol.interpreter.Operation#getByteLength()
     */
    @Override
    public int getByteLength()
    {
        return OPCODE_BYTE_LENGTH;
    }

    /**
     * @see nl.tudelft.simulation.dsol.interpreter.Operation#getOpcode()
     */
    @Override
    public int getOpcode()
    {
        return DRETURN.OP;
    }
}