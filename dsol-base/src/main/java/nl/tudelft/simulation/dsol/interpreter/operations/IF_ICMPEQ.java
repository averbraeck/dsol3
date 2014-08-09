/*
 * @(#) IF_ICMPEQ.java $Date: 2010/08/10 11:38:23 $ Copyright (c) 2002-2005
 * Delft University of Technology Jaffalaan 5, 2628 BX Delft, the Netherlands.
 * All rights reserved. This software is proprietary information of Delft
 * University of Technology The code is published under the Lesser General
 * Public License
 */
package nl.tudelft.simulation.dsol.interpreter.operations;

import java.io.DataInput;
import java.io.IOException;

import nl.tudelft.simulation.dsol.interpreter.LocalVariable;
import nl.tudelft.simulation.dsol.interpreter.OperandStack;
import nl.tudelft.simulation.dsol.interpreter.classfile.Constant;
import nl.tudelft.simulation.language.primitives.Primitive;

/**
 * The IF_ICMPEQ operation as defined in <a
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
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:38:23 $
 * @since 1.5
 */
public class IF_ICMPEQ extends JumpOperation
{
    /** OP refers to the operand code */
    public static final int OP = 159;

    /** the index to load */
    private int offset = -1;

    /**
     * constructs a new IF_ICMPEQ
     * @param dataInput the dataInput
     * @throws IOException on IOfailure
     */
    public IF_ICMPEQ(final DataInput dataInput) throws IOException
    {
        super();
        this.offset = dataInput.readShort();
    }

    /**
     * @see nl.tudelft.simulation.dsol.interpreter.operations.JumpOperation
     *      #execute(nl.tudelft.simulation.dsol.interpreter.OperandStack,
     *      nl.tudelft.simulation.dsol.interpreter.classfile.Constant[],
     *      nl.tudelft.simulation.dsol.interpreter.LocalVariable[])
     */
    @Override
    public int execute(final OperandStack stack, final Constant[] constantPool, final LocalVariable[] localVariables)
    {
        Integer obj2 = Primitive.toInteger(stack.pop());
        Integer obj1 = Primitive.toInteger(stack.pop());
        if (obj1.compareTo(obj2) == 0)
        {
            return this.offset;
        }
        return this.getByteLength();
    }

    /**
     * @see nl.tudelft.simulation.dsol.interpreter.Operation#getByteLength()
     */
    @Override
    public int getByteLength()
    {
        return OPCODE_BYTE_LENGTH + 2;
    }

    /**
     * @see nl.tudelft.simulation.dsol.interpreter.Operation#getOpcode()
     */
    @Override
    public int getOpcode()
    {
        return IF_ICMPEQ.OP;
    }
}