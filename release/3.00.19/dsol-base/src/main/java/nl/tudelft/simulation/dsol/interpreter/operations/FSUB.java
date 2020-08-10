package nl.tudelft.simulation.dsol.interpreter.operations;

import nl.tudelft.simulation.dsol.interpreter.LocalVariable;
import nl.tudelft.simulation.dsol.interpreter.OperandStack;
import nl.tudelft.simulation.dsol.interpreter.classfile.Constant;

/**
 * The FSUB operation as defined in <a
 * href="http://java.sun.com/docs/books/vmspec/2nd-edition/html/Instructions2.doc4.html">
 * http://java.sun.com/docs/books/vmspec/2nd-edition/html/Instructions2.doc4.html </a>.
 * <p>
 * (c) copyright 2002-2014 <a href="http://www.simulation.tudelft.nl">Delft University of Technology</a>. <br>
 * BSD-style license. See <a href="http://www.simulation.tudelft.nl/dsol/3.0/license.html">DSOL License</a>. <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs</a>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class FSUB extends VoidOperation
{
    /** OP refers to the operand code. */
    public static final int OP = 102;

    /**
     * constructs a new FSUB.
     */
    public FSUB()
    {
        super();
    }

    /** {@inheritDoc} */
    @Override
    public final void execute(final OperandStack stack, final Constant[] constantPool,
            final LocalVariable[] localVariables)
    {
        float f2 = ((Float) stack.pop()).floatValue();
        float f1 = ((Float) stack.pop()).floatValue();
        stack.push(Float.valueOf(f1 - f2));
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
        return FSUB.OP;
    }
}