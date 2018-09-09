package nl.tudelft.simulation.dsol.interpreter.operations;

import nl.tudelft.simulation.dsol.interpreter.LocalVariable;
import nl.tudelft.simulation.dsol.interpreter.OperandStack;
import nl.tudelft.simulation.dsol.interpreter.classfile.Constant;

/**
 * The DUP2_X1 operation as defined in <a
 * href="http://java.sun.com/docs/books/vmspec/2nd-edition/html/Instructions2.doc3.html">
 * http://java.sun.com/docs/books/vmspec/2nd-edition/html/Instructions2.doc3.html </a>.
 * <p>
 * copyright (c) 2002-2018  <a href="http://www.simulation.tudelft.nl">Delft University of Technology</a>. <br>
 * BSD-style license. See <a href="http://www.simulation.tudelft.nl/dsol/3.0/license.html">DSOL License</a>. <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs</a>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class DUP2_X1 extends VoidOperation
{
    /** OP refers to the operand code. */
    public static final int OP = 93;

    /**
     * constructs a new DUP2_X1.
     */
    public DUP2_X1()
    {
        super();
    }

    /** {@inheritDoc} */
    @Override
    public final void execute(final OperandStack stack, final Constant[] constantPool,
            final LocalVariable[] localVariables)
    {
        Object obj1 = stack.pop();
        Object obj2 = stack.pop();
        if ((obj1 instanceof Long) || (obj1 instanceof Double))
        {
            stack.push(obj1);
            stack.push(obj2);
            stack.push(obj1);
        }
        else
        {
            Object obj3 = stack.pop();
            stack.push(obj2);
            stack.push(obj1);
            stack.push(obj3);
            stack.push(obj2);
            stack.push(obj1);
        }

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
        return DUP2_X1.OP;
    }
}
