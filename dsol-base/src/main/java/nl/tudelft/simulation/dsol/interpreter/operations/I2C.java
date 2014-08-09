/*
 * @(#) I2I.java $Date: 2010/08/10 11:38:22 $ Copyright (c) 2002-2005 Delft
 * University of Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All
 * rights reserved. This software is proprietary information of Delft University
 * of Technology 
 */
package nl.tudelft.simulation.dsol.interpreter.operations;

import nl.tudelft.simulation.dsol.interpreter.LocalVariable;
import nl.tudelft.simulation.dsol.interpreter.OperandStack;
import nl.tudelft.simulation.dsol.interpreter.classfile.Constant;
import nl.tudelft.simulation.language.primitives.Primitive;

/**
 * The I2C operation as defined in <a
 * href="http://java.sun.com/docs/books/vmspec/2nd-edition/html/Instructions2.doc6.html">
 * http://java.sun.com/docs/books/vmspec/2nd-edition/html/Instructions2.doc6.html </a>.
 * <p>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="http://www.peter-jacobs.com/index.htm">Peter Jacobs </a><a
 *         href="mailto:a.verbraeck@tudelft.nl">Alexander Verbraeck </a>
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:38:22 $
 * @since 1.5
 */
public class I2C extends VoidOperation
{
    /** OP refers to the operand code */
    public static final int OP = 146;

    /**
     * constructs a new I2I
     */
    public I2C()
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
    public void execute(final OperandStack stack, final Constant[] constantPool, final LocalVariable[] localVariables)
    {
        int i = Primitive.toInteger(stack.pop()).intValue();
        // push an integer onto the stack, as chars are not really known in the
        // JVM
        stack.push(new Integer((char) i));
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
        return I2C.OP;
    }
}