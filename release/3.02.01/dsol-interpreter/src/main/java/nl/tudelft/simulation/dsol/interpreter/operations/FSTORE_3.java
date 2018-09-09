package nl.tudelft.simulation.dsol.interpreter.operations;

import nl.tudelft.simulation.dsol.interpreter.LocalVariable;
import nl.tudelft.simulation.dsol.interpreter.OperandStack;
import nl.tudelft.simulation.dsol.interpreter.classfile.Constant;

/**
 * The FSTORE_3 operation as defined in <a
 * href="http://java.sun.com/docs/books/vmspec/2nd-edition/html/Instructions2.doc4.html">
 * http://java.sun.com/docs/books/vmspec/2nd-edition/html/Instructions2.doc4.html </a>.
 * <p>
 * copyright (c) 2002-2018  <a href="https://simulation.tudelft.nl">Delft University of Technology</a>. <br>
 * BSD-style license. See <a href="https://simulation.tudelft.nl/dsol/3.0/license.html">DSOL License</a>. <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs</a>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class FSTORE_3 extends VoidOperation
{
    /** OP refers to the operand code. */
    public static final int OP = 70;

    /**
     * constructs a new FSTORE_3.
     */
    public FSTORE_3()
    {
        super();
    }

    /** {@inheritDoc} */
    @Override
    public final void execute(final OperandStack stack, final Constant[] constantPool,
            final LocalVariable[] localVariables)
    {
        localVariables[3].setValue(stack.pop());
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
        return FSTORE_3.OP;
    }
}
