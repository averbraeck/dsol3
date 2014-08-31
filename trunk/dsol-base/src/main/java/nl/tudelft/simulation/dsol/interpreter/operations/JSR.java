package nl.tudelft.simulation.dsol.interpreter.operations;

import java.io.DataInput;
import java.io.IOException;

import nl.tudelft.simulation.dsol.interpreter.LocalVariable;
import nl.tudelft.simulation.dsol.interpreter.OperandStack;
import nl.tudelft.simulation.dsol.interpreter.classfile.Constant;

/**
 * The JSR operation as defined in <a
 * href="http://java.sun.com/docs/books/vmspec/2nd-edition/html/Instructions2.doc7.html">
 * http://java.sun.com/docs/books/vmspec/2nd-edition/html/Instructions2.doc7.html </a>.
 * <p />
 * (c) copyright 2002-2014 <a href="http://www.simulation.tudelft.nl">Delft University of Technology</a>. <br />
 * BSD-style license. See <a href="http://www.simulation.tudelft.nl/dsol/3.0/license.html">DSOL License</a>. <br />
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs</a>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class JSR extends JumpOperation
{
    /** OP refers to the operand code. */
    public static final int OP = 168;

    /** the index to load. */
    private final int offset;

    /** the startAddress of the next operation. */
    private final int nextAddress;

    /**
     * constructs a new JSR.
     * @param dataInput the dataInput
     * @param startBytePosition the startBytePosition of this operation
     * @throws IOException on IOfailure
     */
    public JSR(final DataInput dataInput, final int startBytePosition) throws IOException
    {
        super();
        this.offset = dataInput.readShort();
        this.nextAddress = this.getByteLength() + startBytePosition;
    }

    /** {@inheritDoc} */
    @Override
    public final int execute(final OperandStack stack, final Constant[] constantPool, final LocalVariable[] localVariables)
    {
        stack.push(new Integer(this.nextAddress));
        return this.offset;
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
        return JSR.OP;
    }
}
