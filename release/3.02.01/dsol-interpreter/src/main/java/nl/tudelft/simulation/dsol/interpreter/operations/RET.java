package nl.tudelft.simulation.dsol.interpreter.operations;

import java.io.DataInput;
import java.io.IOException;

import nl.tudelft.simulation.dsol.interpreter.LocalVariable;
import nl.tudelft.simulation.dsol.interpreter.OperandStack;
import nl.tudelft.simulation.dsol.interpreter.classfile.Constant;
import nl.tudelft.simulation.language.primitives.Primitive;

/**
 * The RET operation as defined in <a
 * href="http://java.sun.com/docs/books/vmspec/2nd-edition/html/Instructions2.doc12.html">
 * http://java.sun.com/docs/books/vmspec/2nd-edition/html/Instructions2.doc12.html </a>.
 * <p>
 * copyright (c) 2002-2018  <a href="https://simulation.tudelft.nl">Delft University of Technology</a>. <br>
 * BSD-style license. See <a href="https://simulation.tudelft.nl/dsol/3.0/license.html">DSOL License</a>. <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs</a>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class RET extends JumpOperation
{
    /** OP refers to the operand code. */
    public static final int OP = 169;

    /** the index to use. */
    private final int index;

    /** see the wide statement. */
    private final boolean widened;

    /**
     * constructs a new RET.
     * @param dataInput the dataInput to read
     * @throws IOException on IOException
     */
    public RET(final DataInput dataInput) throws IOException
    {
        this(dataInput, false);
    }

    /**
     * constructs a new RET.
     * @param dataInput the dataInput to read
     * @param widened whether or not to widen
     * @throws IOException on IOException
     */
    public RET(final DataInput dataInput, final boolean widened) throws IOException
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
    public final int execute(final OperandStack stack, final Constant[] constantPool,
            final LocalVariable[] localVariables)
    {
        return Primitive.toInteger(localVariables[this.index].getValue()).intValue();
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
        return RET.OP;
    }
}
