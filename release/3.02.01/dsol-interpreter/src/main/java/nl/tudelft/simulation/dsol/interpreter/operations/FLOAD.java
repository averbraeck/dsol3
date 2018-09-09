package nl.tudelft.simulation.dsol.interpreter.operations;

import java.io.DataInput;
import java.io.IOException;

import nl.tudelft.simulation.dsol.interpreter.LocalVariable;
import nl.tudelft.simulation.dsol.interpreter.OperandStack;
import nl.tudelft.simulation.dsol.interpreter.classfile.Constant;

/**
 * The FLOAD operation as defined in <a
 * href="http://java.sun.com/docs/books/vmspec/2nd-edition/html/Instructions2.doc4.html">
 * http://java.sun.com/docs/books/vmspec/2nd-edition/html/Instructions2.doc4.html </a>.
 * <p>
 * copyright (c) 2002-2018  <a href="http://www.simulation.tudelft.nl">Delft University of Technology</a>. <br>
 * BSD-style license. See <a href="http://www.simulation.tudelft.nl/dsol/3.0/license.html">DSOL License</a>. <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs</a>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class FLOAD extends VoidOperation
{
    /** OP refers to the operand code. */
    public static final int OP = 23;

    /** the index to load. */
    private final int index;

    /** see the wide statement. */
    private final boolean widened;

    /**
     * constructs a new FLOAD.
     * @param dataInput the dataInput
     * @throws IOException on IOfailure
     */
    public FLOAD(final DataInput dataInput) throws IOException
    {
        this(dataInput, false);
    }

    /**
     * constructs a new FLOAD.
     * @param dataInput the dataInput
     * @param widened whether or not to widen
     * @throws IOException on IOfailure
     */
    public FLOAD(final DataInput dataInput, final boolean widened) throws IOException
    {
        super();
        this.widened = widened;
        if (widened)
        {
            this.index = dataInput.readUnsignedShort();
        }
        else
        {
            this.index = dataInput.readUnsignedByte();
        }
    }

    /** {@inheritDoc} */
    @Override
    public final void execute(final OperandStack stack, final Constant[] constantPool,
            final LocalVariable[] localVariables)
    {
        stack.push(localVariables[this.index].getValue());
    }

    /** {@inheritDoc} */
    @Override
    public final int getByteLength()
    {
        int result = OPCODE_BYTE_LENGTH + 1;
        if (this.widened)
        {
            result++;
        }
        return result;

    }

    /** {@inheritDoc} */
    @Override
    public final int getOpcode()
    {
        return FLOAD.OP;
    }
}
