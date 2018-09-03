package nl.tudelft.simulation.dsol.interpreter.operations;

import java.io.DataInput;
import java.io.IOException;
import java.lang.reflect.Field;

import nl.tudelft.simulation.dsol.interpreter.InterpreterException;
import nl.tudelft.simulation.dsol.interpreter.LocalVariable;
import nl.tudelft.simulation.dsol.interpreter.OperandStack;
import nl.tudelft.simulation.dsol.interpreter.classfile.Constant;
import nl.tudelft.simulation.dsol.interpreter.classfile.ConstantFieldref;
import nl.tudelft.simulation.language.reflection.ClassUtil;

/**
 * The PUTSTATIC operation as defined in <a
 * href="http://java.sun.com/docs/books/vmspec/2nd-edition/html/Instructions2.doc11.html">
 * http://java.sun.com/docs/books/vmspec/2nd-edition/html/Instructions2.doc11.html </a>.
 * <p>
 * copyright (c) 2002-2018  <a href="http://www.simulation.tudelft.nl">Delft University of Technology</a>. <br>
 * BSD-style license. See <a href="http://www.simulation.tudelft.nl/dsol/3.0/license.html">DSOL License</a>. <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs</a>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class PUTSTATIC extends VoidOperation
{
    /** OP refers to the operand code. */
    public static final int OP = 179;

    /** the index to load. */
    private final int index;

    /**
     * constructs a new PUTSTATIC.
     * @param dataInput the dataInput
     * @throws IOException on IOfailure
     */
    public PUTSTATIC(final DataInput dataInput) throws IOException
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
            Field field = null;
            ConstantFieldref constantFieldref = (ConstantFieldref) constantPool[this.index];
            Class<?> referenceClass = constantFieldref.getConstantClass().getValue().getClassValue();
            field = ClassUtil.resolveField(referenceClass, constantFieldref.getConstantNameAndType().getName());
            field.setAccessible(true);
            Object value = stack.pop();
            Object target = null;
            
            if (!field.getType().isPrimitive())
            {
                field.set(target, value);
                return;
            }
            
            if (field.getType().equals(boolean.class))
            {
                field.setBoolean(target, ((int) value) == 1);
                return;
            }

            if (field.getType().equals(byte.class))
            {
                field.setByte(target, ((Integer) value).byteValue());
                return;
            }
            
            if (field.getType().equals(char.class))
            {
                field.setChar(target, (char) (((Integer) value).byteValue()));
                return;
            }
            
            if (field.getType().equals(short.class))
            {
                field.setShort(target, ((Short) value).shortValue());
                return;
            }
            
            if (field.getType().equals(int.class))
            {
                field.setInt(target, ((Integer) value).intValue());
                return;
            }
            
            if (field.getType().equals(float.class))
            {
                field.setFloat(target, ((Float) value).floatValue());
                return;
            }
            
            if (field.getType().equals(long.class))
            {
                field.setLong(target, ((Long) value).longValue());
                return;
            }
            
            if (field.getType().equals(double.class))
            {
                field.setDouble(target, ((Double) value).doubleValue());
                return;
            }
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
        return PUTSTATIC.OP;
    }
}
