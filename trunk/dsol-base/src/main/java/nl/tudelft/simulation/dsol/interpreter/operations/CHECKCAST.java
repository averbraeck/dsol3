package nl.tudelft.simulation.dsol.interpreter.operations;

import java.io.DataInput;
import java.io.IOException;

import nl.tudelft.simulation.dsol.interpreter.InterpreterException;
import nl.tudelft.simulation.dsol.interpreter.LocalVariable;
import nl.tudelft.simulation.dsol.interpreter.OperandStack;
import nl.tudelft.simulation.dsol.interpreter.classfile.Constant;
import nl.tudelft.simulation.dsol.interpreter.classfile.ConstantClass;

/**
 * The CHECKCAST operation as defined in <a
 * href="http://java.sun.com/docs/books/vmspec/2nd-edition/html/Instructions2.doc2.html">
 * http://java.sun.com/docs/books/vmspec/2nd-edition/html/Instructions2.doc2.html </a>.
 * <p />
 * (c) copyright 2002-2014 <a href="http://www.simulation.tudelft.nl">Delft University of Technology</a>. <br />
 * BSD-style license. See <a href="http://www.simulation.tudelft.nl/dsol/3.0/license.html">DSOL License</a>. <br />
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs</a>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class CHECKCAST extends VoidOperation
{
    /** OP refers to the operand code. */
    public static final int OP = 192;

    /** the index to load. */
    private final int index;

    /**
     * constructs a new CHECKCAST.
     * @param dataInput the dataInput
     * @throws IOException on IOfailure
     */
    public CHECKCAST(final DataInput dataInput) throws IOException
    {
        super();
        this.index = dataInput.readUnsignedShort();
    }

    /** {@inheritDoc} */
    @Override
    public final void execute(final OperandStack stack, final Constant[] constantPool,
            final LocalVariable[] localVariables)
    {
        Object objectReference = stack.peek();
        if (objectReference == null)
        {
            return;
        }
        Class<?> clazz = null;
        try
        {
            clazz = ((ConstantClass) constantPool[this.index]).getValue().getClassValue();
        }
        catch (ClassNotFoundException exception)
        {
            throw new InterpreterException(exception);
        }
        if (!clazz.isAssignableFrom(objectReference.getClass()))
        {
            throw new ClassCastException("CHECKCAST operation failed");
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
        return CHECKCAST.OP;
    }
}
