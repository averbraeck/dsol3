package nl.tudelft.simulation.dsol.interpreter.operations;

import java.io.DataInput;
import java.io.IOException;
import java.lang.reflect.Array;

import nl.tudelft.simulation.dsol.interpreter.InterpreterException;
import nl.tudelft.simulation.dsol.interpreter.LocalVariable;
import nl.tudelft.simulation.dsol.interpreter.OperandStack;
import nl.tudelft.simulation.dsol.interpreter.classfile.Constant;
import nl.tudelft.simulation.dsol.interpreter.classfile.ConstantClass;
import nl.tudelft.simulation.language.primitives.Primitive;

/**
 * The ANEWARRAY operation as defined in <a
 * href="http://java.sun.com/docs/books/vmspec/2nd-edition/html/Instructions2.doc.html">
 * http://java.sun.com/docs/books/vmspec/2nd-edition/html/Instructions2.doc.html </a>.
 * <p />
 * (c) copyright 2002-2014 <a href="http://www.simulation.tudelft.nl">Delft University of Technology</a>. <br />
 * BSD-style license. See <a href="http://www.simulation.tudelft.nl/dsol/3.0/license.html">DSOL License</a>. <br />
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs</a>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class ANEWARRAY extends VoidOperation
{
    /** OP refers to the operand code. */
    public static final int OP = 189;

    /** the index to load. */
    private final int index;

    /**
     * constructs a new ANEWARRAY.
     * @param dataInput the dataInput
     * @throws IOException on IOfailure
     */
    public ANEWARRAY(final DataInput dataInput) throws IOException
    {
        super();
        this.index = dataInput.readUnsignedShort();
    }

    /** {@inheritDoc} */
    @Override
    public final void execute(final OperandStack stack, final Constant[] constantPool,
            final LocalVariable[] localVariables)
    {
        int length = Primitive.toInteger(stack.pop()).intValue();
        Class<?> clazz = null;
        try
        {
            clazz = ((ConstantClass) constantPool[this.index]).getValue().getClassValue();
        }
        catch (ClassNotFoundException exception)
        {
            throw new InterpreterException(exception);
        }
        Object array = Array.newInstance(clazz, length);
        stack.push(array);
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
        return ANEWARRAY.OP;
    }
}
