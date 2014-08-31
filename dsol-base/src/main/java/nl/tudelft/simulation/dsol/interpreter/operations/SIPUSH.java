package nl.tudelft.simulation.dsol.interpreter.operations;

import java.io.DataInput;
import java.io.IOException;

import nl.tudelft.simulation.dsol.interpreter.LocalVariable;
import nl.tudelft.simulation.dsol.interpreter.OperandStack;
import nl.tudelft.simulation.dsol.interpreter.classfile.Constant;

/**
 * The SIPUSH operation as defined in <a
 * href="http://java.sun.com/docs/books/vmspec/2nd-edition/html/Instructions2.doc13.html">
 * http://java.sun.com/docs/books/vmspec/2nd-edition/html/Instructions2.doc13.html </a>.
 * <p />
 * (c) copyright 2002-2014 <a href="http://www.simulation.tudelft.nl">Delft University of Technology</a>. <br />
 * BSD-style license. See <a href="http://www.simulation.tudelft.nl/dsol/3.0/license.html">DSOL License</a>. <br />
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs</a>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class SIPUSH extends VoidOperation
{
    /** OP refers to the operand code. */
    public static final int OP = 17;

    /** the index to load. */
    private short value = -1;

    /**
     * constructs a new SIPUSH.
     * @param dataInput the dataInput
     * @throws IOException on IOfailure
     */
    public SIPUSH(final DataInput dataInput) throws IOException
    {
        super();
        this.value = (short) dataInput.readUnsignedShort();
    }

    /** {@inheritDoc} */
    @Override
    public final void execute(final OperandStack stack, final Constant[] constantPool,
            final LocalVariable[] localVariables)
    {
        stack.push(Integer.valueOf(this.value));
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
        return SIPUSH.OP;
    }
}
