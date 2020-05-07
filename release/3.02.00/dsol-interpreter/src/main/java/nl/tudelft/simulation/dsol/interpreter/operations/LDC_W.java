package nl.tudelft.simulation.dsol.interpreter.operations;

import java.io.DataInput;
import java.io.IOException;

import nl.tudelft.simulation.dsol.interpreter.LocalVariable;
import nl.tudelft.simulation.dsol.interpreter.OperandStack;
import nl.tudelft.simulation.dsol.interpreter.classfile.Constant;
import nl.tudelft.simulation.dsol.interpreter.classfile.ConstantFloat;
import nl.tudelft.simulation.dsol.interpreter.classfile.ConstantInteger;
import nl.tudelft.simulation.dsol.interpreter.classfile.ConstantString;

/**
 * The LDC_W operation as defined in <a
 * href="http://java.sun.com/docs/books/vmspec/2nd-edition/html/Instructions2.doc8.html">
 * http://java.sun.com/docs/books/vmspec/2nd-edition/html/Instructions2.doc8.html </a>.
 * <p>
 * copyright (c) 2002-2018  <a href="http://www.simulation.tudelft.nl">Delft University of Technology</a>. <br>
 * BSD-style license. See <a href="http://www.simulation.tudelft.nl/dsol/3.0/license.html">DSOL License</a>. <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs</a>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class LDC_W extends VoidOperation
{
    /** OP refers to the operand code. */
    public static final int OP = 19;

    /** the index to load. */
    private final int index;

    /**
     * constructs a new LDC_W.
     * @param dataInput the dataInput
     * @throws IOException on IOfailure
     */
    public LDC_W(final DataInput dataInput) throws IOException
    {
        super();
        this.index = dataInput.readUnsignedShort();
    }

    /** {@inheritDoc} */
    @Override
    public final void execute(final OperandStack stack, final Constant[] constantPool,
            final LocalVariable[] localVariables)
    {
        Constant constant = constantPool[this.index];
        if (constant instanceof ConstantInteger)
        {
            stack.push(Integer.valueOf(((ConstantInteger) constant).getValue()));
        }
        else if (constant instanceof ConstantFloat)
        {
            stack.push(Float.valueOf(((ConstantFloat) constant).getValue()));
        }
        else if (constant instanceof ConstantString)
        {
            stack.push(((ConstantString) constant).getValue());
        }
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
        return LDC_W.OP;
    }
}