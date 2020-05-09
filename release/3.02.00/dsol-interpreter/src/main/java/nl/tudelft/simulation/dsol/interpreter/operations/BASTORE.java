package nl.tudelft.simulation.dsol.interpreter.operations;

import java.lang.reflect.Array;

import nl.tudelft.simulation.dsol.interpreter.LocalVariable;
import nl.tudelft.simulation.dsol.interpreter.OperandStack;
import nl.tudelft.simulation.dsol.interpreter.classfile.Constant;
import nl.tudelft.simulation.language.primitives.Primitive;

/**
 * The BASTORE operation as defined in <a
 * href="http://java.sun.com/docs/books/vmspec/2nd-edition/html/Instructions2.doc1.html">
 * http://java.sun.com/docs/books/vmspec/2nd-edition/html/Instructions2.doc1.html </a>.
 * <p>
 * copyright (c) 2002-2018  <a href="http://www.simulation.tudelft.nl">Delft University of Technology</a>. <br>
 * BSD-style license. See <a href="http://www.simulation.tudelft.nl/dsol/3.0/license.html">DSOL License</a>. <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs</a>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class BASTORE extends VoidOperation
{
    /** OP refers to the operand code. */
    public static final int OP = 84;

    /**
     * constructs a new BASTORE.
     */
    public BASTORE()
    {
        super();
    }

    /** {@inheritDoc} */
    @Override
    public final void execute(final OperandStack stack, final Constant[] constantPool,
            final LocalVariable[] localVariables)
    {
        byte value = Primitive.toByte(stack.pop()).byteValue();
        int index = Primitive.toInteger(stack.pop()).intValue();
        Object arrayref = stack.pop();
        Array.setByte(arrayref, index, value);
    }

    /** {@inheritDoc} */
    @Override
    public final int getByteLength()
    {
        return OPCODE_BYTE_LENGTH;
    }

    /** {@inheritDoc} */
    @Override
    public final int getOpcode()
    {
        return BASTORE.OP;
    }
}