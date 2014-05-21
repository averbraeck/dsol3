/*
 * @(#) DUP2_X1.java $Date: 2010/08/10 11:38:23 $ Copyright (c) 2002-2005 Delft
 * University of Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All
 * rights reserved. This software is proprietary information of Delft University
 * of Technology 
 */
package nl.tudelft.simulation.dsol.interpreter.operations;

import nl.tudelft.simulation.dsol.interpreter.LocalVariable;
import nl.tudelft.simulation.dsol.interpreter.OperandStack;
import nl.tudelft.simulation.dsol.interpreter.classfile.Constant;

/**
 * The DUP_X2 operation as defined in <a
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
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:38:23 $
 * @since 1.5
 */
public class DUP2_X2 extends VoidOperation
{
    /** OP refers to the operand code */
    public static final int OP = 94;

    /**
     * constructs a new DUP2_X1
     */
    public DUP2_X2()
    {
        super();
    }

    /**
     * @see nl.tudelft.simulation.dsol.interpreter.operations.VoidOperation
     *      #execute(nl.tudelft.simulation.dsol.interpreter.OperandStack,
     *      nl.tudelft.simulation.dsol.interpreter.classfile.Constant[],
     *      nl.tudelft.simulation.dsol.interpreter.LocalVariable[])
     */
    @Override
    public void execute(final OperandStack stack,
            final Constant[] constantPool, final LocalVariable[] localVariables)
    {
        Object obj1 = stack.pop();
        Object obj2 = stack.pop();
        if ((obj1 instanceof Long) || (obj1 instanceof Double))
        {
            if ((obj2 instanceof Long) || (obj2 instanceof Double))
            {
                // form 4
                stack.push(obj1);
                stack.push(obj2);
                stack.push(obj1);
            } else
            {
                // form 1
                Object obj3 = stack.pop();
                stack.push(obj1);
                stack.push(obj3);
                stack.push(obj2);
                stack.push(obj1);
            }
        } else
        {
            Object obj3 = stack.pop();
            if ((obj3 instanceof Long) || (obj3 instanceof Double))
            {
                // form 3
                stack.push(obj2);
                stack.push(obj1);
                stack.push(obj3);
                stack.push(obj2);
                stack.push(obj1);
            } else
            {
                // form 1
                Object obj4 = stack.pop();
                stack.push(obj2);
                stack.push(obj1);
                stack.push(obj4);
                stack.push(obj3);
                stack.push(obj2);
                stack.push(obj1);
            }
        }
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
        return DUP2_X2.OP;
    }
}