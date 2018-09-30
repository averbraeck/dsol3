package nl.tudelft.simulation.dsol.interpreter.operations;

import nl.tudelft.simulation.dsol.interpreter.LocalVariable;
import nl.tudelft.simulation.dsol.interpreter.OperandStack;
import nl.tudelft.simulation.dsol.interpreter.classfile.Constant;

/**
 * The DUP_X2 operation as defined in
 * <a href="http://java.sun.com/docs/books/vmspec/2nd-edition/html/Instructions2.doc3.html">
 * http://java.sun.com/docs/books/vmspec/2nd-edition/html/Instructions2.doc3.html </a>.
 * <p>
 * copyright (c) 2002-2018 <a href="https://simulation.tudelft.nl">Delft University of Technology</a>. <br>
 * BSD-style license. See <a href="https://simulation.tudelft.nl/dsol/3.0/license.html">DSOL License</a>. <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs</a>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class DUP2_X2 extends VoidOperation
{
    /** OP refers to the operand code. */
    public static final int OP = 94;

    /**
     * constructs a new DUP2_X2.
     */
    public DUP2_X2()
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
            if ((obj2 instanceof Long) || (obj2 instanceof Double))
            {
                // form 4
                stack.push(obj1);
                stack.push(obj2);
                stack.push(obj1);
            }
            else
            {
                // form 1
                Object obj3 = stack.pop();
                stack.push(obj1);
                stack.push(obj3);
                stack.push(obj2);
                stack.push(obj1);
            }
        }
        else
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
            }
            else
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
        return DUP2_X2.OP;
    }
}
