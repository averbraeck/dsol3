package nl.tudelft.simulation.dsol.interpreter.operations;

import java.io.DataInput;
import java.io.IOException;

import nl.tudelft.simulation.dsol.interpreter.LocalVariable;
import nl.tudelft.simulation.dsol.interpreter.OperandStack;
import nl.tudelft.simulation.dsol.interpreter.classfile.Constant;

/**
 * The GOTOW operation as defined in
 * <a href="http://java.sun.com/docs/books/vmspec/2nd-edition/html/Instructions2.doc5.html">
 * http://java.sun.com/docs/books/vmspec/2nd-edition/html/Instructions2.doc5.html </a>.
 * <p>
 * copyright (c) 2002-2018 <a href="https://simulation.tudelft.nl">Delft University of Technology</a>. <br>
 * BSD-style license. See <a href="https://simulation.tudelft.nl/dsol/3.0/license.html">DSOL License</a>. <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs</a>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class GOTO_W extends JumpOperation
{
    /** OP refers to the operand code. */
    public static final int OP = 200;

    /** the index to load. */
    private final int offset;

    /**
     * constructs a new GOTOW.
     * @param dataInput the dataInput
     * @throws IOException on IOfailure
     */
    public GOTO_W(final DataInput dataInput) throws IOException
    {
        super();
        this.offset = dataInput.readInt();
    }

    /** {@inheritDoc} */
    @Override
    public final int execute(final OperandStack stack, final Constant[] constantPool,
            final LocalVariable[] localVariables)
    {
        return this.offset;
    }

    /** {@inheritDoc} */
    @Override
    public final int getByteLength()
    {
        return OPCODE_BYTE_LENGTH + 4;
    }

    @Override
    public final int getOpcode()
    {
        return GOTO_W.OP;
    }
}
