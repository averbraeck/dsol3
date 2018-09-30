package nl.tudelft.simulation.dsol.interpreter.operations;

import java.io.DataInput;
import java.io.IOException;

import nl.tudelft.simulation.dsol.interpreter.LocalVariable;
import nl.tudelft.simulation.dsol.interpreter.OperandStack;
import nl.tudelft.simulation.dsol.interpreter.classfile.Constant;

/**
 * The JSR_W operation as defined in
 * <a href="http://java.sun.com/docs/books/vmspec/2nd-edition/html/Instructions2.doc7.html">
 * http://java.sun.com/docs/books/vmspec/2nd-edition/html/Instructions2.doc7.html </a>.
 * <p>
 * copyright (c) 2002-2018 <a href="https://simulation.tudelft.nl">Delft University of Technology</a>. <br>
 * BSD-style license. See <a href="https://simulation.tudelft.nl/dsol/3.0/license.html">DSOL License</a>. <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs</a>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class JSR_W extends JumpOperation
{
    /** OP refers to the operand code. */
    public static final int OP = 201;

    /** the index to load. */
    private final int offset;

    /** the startAddress of the next operation. */
    private final int nextAddress;

    /**
     * constructs a new JSR_W.
     * @param dataInput the dataInput
     * @param startBytePosition the startBytePosition of this operation
     * @throws IOException on IOfailure
     */
    public JSR_W(final DataInput dataInput, final int startBytePosition) throws IOException
    {
        super();
        this.offset = dataInput.readInt();
        this.nextAddress = this.getByteLength() + startBytePosition;
    }

    /** {@inheritDoc} */
    @Override
    public final int execute(final OperandStack stack, final Constant[] constantPool,
            final LocalVariable[] localVariables)
    {
        stack.push(Integer.valueOf(this.nextAddress));
        return this.offset;
    }

    /** {@inheritDoc} */
    @Override
    public final int getByteLength()
    {
        return OPCODE_BYTE_LENGTH + 4;
    }

    /** {@inheritDoc} */
    @Override
    public final int getOpcode()
    {
        return JSR_W.OP;
    }
}
