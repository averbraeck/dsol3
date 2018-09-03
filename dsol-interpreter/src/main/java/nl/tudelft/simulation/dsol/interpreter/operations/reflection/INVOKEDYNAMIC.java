package nl.tudelft.simulation.dsol.interpreter.operations.reflection;

import java.io.DataInput;
import java.io.IOException;
import java.lang.reflect.Method;

import nl.tudelft.simulation.dsol.interpreter.Frame;
import nl.tudelft.simulation.dsol.interpreter.Interpreter;
import nl.tudelft.simulation.dsol.interpreter.InterpreterException;
import nl.tudelft.simulation.dsol.interpreter.classfile.ConstantMethodref;
import nl.tudelft.simulation.dsol.interpreter.operations.InvokeOperation;
import nl.tudelft.simulation.language.primitives.Primitive;
import nl.tudelft.simulation.language.reflection.ClassUtil;
import nl.tudelft.simulation.language.reflection.MethodSignature;

//TODO Was missing in DSOL1 and DSOL2. Check if correct. INVOKEDYNAMIC has been copied from INVOKESTATIC and that is 100% sure not correct.

/**
 * INVOKEDYNAMIC.
 * <p>
 * copyright (c) 2002-2018  <a href="http://www.simulation.tudelft.nl">Delft University of Technology</a>. <br>
 * BSD-style license. See <a href="http://www.simulation.tudelft.nl/dsol/3.0/license.html">DSOL License</a>. <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs</a>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class INVOKEDYNAMIC extends InvokeOperation
{
    /** OP refers to the operand code. */
    public static final int OP = 186;

    /** the index to load. */
    private final int index;

    /**
     * constructs a new INVOKEDYNAMIC.
     * @param dataInput the dataInput
     * @throws IOException on IOfailure
     */
    public INVOKEDYNAMIC(final DataInput dataInput) throws IOException
    {
        super();
        System.err.println("INVOKEDYNAMIC has been copied from INVOKESTATIC and that is 100% sure not correct");
        this.index = dataInput.readUnsignedShort();
        dataInput.readUnsignedShort(); // 2 bytes with signature 0 0
    }

    /** {@inheritDoc} */
    @Override
    public final Frame execute(final Frame frame)
    {
        try
        {
            Method method = null;
            ConstantMethodref constantMethodref = (ConstantMethodref) frame.getConstantPool()[this.index];
            Class<?> referenceClass = constantMethodref.getConstantClass().getValue().getClassValue();
            Class<?>[] parameterTypes =
                    new MethodSignature(constantMethodref.getConstantNameAndType().getDescriptor()).getParameterTypes();

            String methodName = constantMethodref.getConstantNameAndType().getName();
            method = ClassUtil.resolveMethod(referenceClass, methodName, parameterTypes);

            synchronized (frame.getOperandStack())
            {
                // Let's create the arguments
                Object[] args = new Object[parameterTypes.length];
                for (int i = args.length - 1; i > -1; i--)
                {
                    args[i] = Primitive.cast(parameterTypes[i], frame.getOperandStack().pop());
                }
                return this.execute(frame, method.getDeclaringClass(), method, args);
            }
        }
        catch (Exception exception)
        {
            throw new InterpreterException(exception);
        }
    }

    /**
     * executes the method on the objectRef.
     * @param frame the frame
     * @param objectRef the objectRef
     * @param method the method
     * @param arguments the arguments
     * @return the resulting Frame
     * @throws Exception on reflection exception
     */
    public Frame execute(final Frame frame, final Object objectRef, final Method method, final Object[] arguments)
            throws Exception
    {
        if (Interpreter.DEBUG)
        {
            System.out.println("  invoke " + objectRef.getClass().getSimpleName() + "." + method.getName());
        }
        
        method.setAccessible(true);
        Object result = null;
        try
        {
            result = method.invoke(objectRef, arguments);
        }
        catch (Exception exception)
        {
            frame.getOperandStack().push(exception.getCause());
            throw exception;
        }

        // Let's see what to do with the stack
        if (!method.getReturnType().equals(void.class))
        {
            frame.getOperandStack().push(result);
        }
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public final int getByteLength()
    {
        return OPCODE_BYTE_LENGTH + 4;
    }

    /** {@inheritDoc} */
    @Override
    public final int getOpcode()
    {
        return INVOKEDYNAMIC.OP;
    }
}
