package nl.tudelft.simulation.dsol.interpreter.operations;

import java.io.DataInput;
import java.io.IOException;

import nl.tudelft.simulation.dsol.interpreter.LocalVariable;
import nl.tudelft.simulation.dsol.interpreter.OperandStack;
import nl.tudelft.simulation.dsol.interpreter.classfile.Constant;
import nl.tudelft.simulation.language.primitives.Primitive;

/**
 * The IFNE operation as defined in <a
 * href="http://java.sun.com/docs/books/vmspec/2nd-edition/html/Instructions2.doc6.html">
 * http://java.sun.com/docs/books/vmspec/2nd-edition/html/Instructions2.doc6.html </a>.
 * <p>
 * (c) copyright 2002-2014 <a href="http://www.simulation.tudelft.nl">Delft University of Technology</a>. <br>
 * BSD-style license. See <a href="http://www.simulation.tudelft.nl/dsol/3.0/license.html">DSOL License</a>. <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs</a>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class IFNE extends JumpOperation
{
    /** OP refers to the operand code. */
    public static final int OP = 154;

    /** the index to load. */
    private final int offset;

    /**
     * constructs a new IFNE.
     * @param dataInput the dataInput
     * @throws IOException on IOfailure
     */
    public IFNE(final DataInput dataInput) throws IOException
    {
        super();
        this.offset = dataInput.readShort();
    }

    /** {@inheritDoc} */
    @Override
    public final int execute(final OperandStack stack, final Constant[] constantPool,
            final LocalVariable[] localVariables)
    {
        Integer objectRef = Primitive.toInteger(stack.pop());
        if (objectRef.compareTo(Integer.valueOf(0)) != 0)
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
        return IFNE.OP;
    }
}
