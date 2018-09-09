package nl.tudelft.simulation.dsol.interpreter.operations;

import java.io.DataInput;
import java.io.IOException;

import nl.tudelft.simulation.dsol.interpreter.LocalVariable;
import nl.tudelft.simulation.dsol.interpreter.OperandStack;
import nl.tudelft.simulation.dsol.interpreter.classfile.Constant;

/**
 * The IF_ACMPEQ operation as defined in <a
 * href="http://java.sun.com/docs/books/vmspec/2nd-edition/html/Instructions2.doc6.html">
 * http://java.sun.com/docs/books/vmspec/2nd-edition/html/Instructions2.doc6.html </a>.
 * <p>
 * copyright (c) 2002-2018  <a href="https://simulation.tudelft.nl">Delft University of Technology</a>. <br>
 * BSD-style license. See <a href="https://simulation.tudelft.nl/dsol/3.0/license.html">DSOL License</a>. <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs</a>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class IF_ACMPEQ extends JumpOperation
{
    /** OP refers to the operand code. */
    public static final int OP = 165;

    /** the index to load. */
    private final int offset;

    /**
     * constructs a new IF_ACMPEQ.
     * @param dataInput the dataInput
     * @throws IOException on IOfailure
     */
    public IF_ACMPEQ(final DataInput dataInput) throws IOException
    {
        super();
        this.offset = dataInput.readShort();
    }

    /** {@inheritDoc} */
    @Override
    public final int execute(final OperandStack stack, final Constant[] constantPool,
            final LocalVariable[] localVariables)
    {
        Object obj2 = stack.pop();
        Object obj1 = stack.pop();
        if (obj1.equals(obj2))
        {
            return this.offset;
        }
        return this.getByteLength();
    }

    /** {@inheritDoc} */
    @Override
    public final int getByteLength()
    {
        return OPCODE_BYTE_LENGTH + 2;
    }

    /** {@inheritDoc} */
    @Override
    public final int getOpcode()
    {
        return IF_ACMPEQ.OP;
    }
}
