package nl.tudelft.simulation.dsol.interpreter.operations;

import java.io.DataInput;
import java.io.IOException;
import java.lang.reflect.Field;

import nl.tudelft.simulation.dsol.interpreter.InterpreterException;
import nl.tudelft.simulation.dsol.interpreter.LocalVariable;
import nl.tudelft.simulation.dsol.interpreter.OperandStack;
import nl.tudelft.simulation.dsol.interpreter.classfile.Constant;
import nl.tudelft.simulation.dsol.interpreter.classfile.ConstantFieldref;
import nl.tudelft.simulation.language.primitives.Primitive;
import nl.tudelft.simulation.language.reflection.ClassUtil;

/**
 * The PUTFIELD operation as defined in <a
 * href="http://java.sun.com/docs/books/vmspec/2nd-edition/html/Instructions2.doc11.html">
 * http://java.sun.com/docs/books/vmspec/2nd-edition/html/Instructions2.doc11.html </a>.
 * <p />
 * (c) copyright 2002-2014 <a href="http://www.simulation.tudelft.nl">Delft University of Technology</a>. <br />
 * BSD-style license. See <a href="http://www.simulation.tudelft.nl/dsol/3.0/license.html">DSOL License</a>. <br />
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs</a>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */

public class PUTFIELD extends VoidOperation
{
    /** OP refers to the operand code. */
    public static final int OP = 181;

    /** the index to load. */
    private final int index;

    /**
     * constructs a new PUTFIELD.
     * @param dataInput the dataInput
     * @throws IOException on IOfailure
     */
    public PUTFIELD(final DataInput dataInput) throws IOException
    {
        super();
        this.index = dataInput.readUnsignedShort();
    }

    /** {@inheritDoc} */
    @Override
    public final void execute(final OperandStack stack, final Constant[] constantPool,
            final LocalVariable[] localVariables)
    {
        try
        {
            Object value = stack.pop();
            Object target = stack.pop();
            Field field = null;
            ConstantFieldref constantFieldref = (ConstantFieldref) constantPool[this.index];
            field = ClassUtil.resolveField(target, constantFieldref.getConstantNameAndType().getName());
            field.setAccessible(true);
            if (!field.getType().isPrimitive())
            {
                field.set(target, value);
                return;
            }
            field.set(target, (Primitive.cast(field.getType(), value)));
        }
        catch (Exception exception)
        {
            throw new InterpreterException(exception);
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
        return PUTFIELD.OP;
    }
}
