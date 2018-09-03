package nl.tudelft.simulation.dsol.interpreter.operations.reflection;

import java.io.DataInput;
import java.io.IOException;
import java.lang.reflect.Method;

import nl.tudelft.simulation.dsol.interpreter.Frame;
import nl.tudelft.simulation.dsol.interpreter.Interpreter;
import nl.tudelft.simulation.dsol.interpreter.InterpreterException;
import nl.tudelft.simulation.dsol.interpreter.classfile.ConstantInterfaceMethodref;
import nl.tudelft.simulation.dsol.interpreter.operations.InvokeOperation;
import nl.tudelft.simulation.language.primitives.Primitive;
import nl.tudelft.simulation.language.reflection.ClassUtil;
import nl.tudelft.simulation.language.reflection.MethodSignature;

/**
 * INVOKEINTERFACE.
 * <p>
 * copyright (c) 2002-2018  <a href="http://www.simulation.tudelft.nl">Delft University of Technology</a>. <br>
 * BSD-style license. See <a href="http://www.simulation.tudelft.nl/dsol/3.0/license.html">DSOL License</a>. <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs</a>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class INVOKEINTERFACE extends InvokeOperation
{
    /** OP refers to the operand code. */
    public static final int OP = 185;

    /** the index to load. */
    private final int index;

    /**
     * constructs a new INVOKEINTERFACE.
     * @param dataInput the dataInput
     * @throws IOException on IOfailure
     */
    public INVOKEINTERFACE(final DataInput dataInput) throws IOException
    {
        super();
        this.index = dataInput.readUnsignedShort();
        dataInput.skipBytes(2);
    }

    /** {@inheritDoc} */
    @Override
    public final Frame execute(final Frame frame)
    {
        try
        {
            synchronized (frame.getOperandStack())
            {
                Method method = null;

                // We resolve the class and the parameterTypes from the
                // constantPool
                ConstantInterfaceMethodref constantInterfaceMethodref =
                        (ConstantInterfaceMethodref) frame.getConstantPool()[this.index];
                Class<?>[] parameterTypes =
                        new MethodSignature(constantInterfaceMethodref.getConstantNameAndType().getDescriptor())
                                .getParameterTypes();

                // We get the objectRef
                Object objectRef = frame.getOperandStack().peek(parameterTypes.length);

                method =
                        ClassUtil.resolveMethod(objectRef, constantInterfaceMethodref.getConstantNameAndType()
                                .getName(), parameterTypes);
                // We construct all parameters
                Object[] arguments = new Object[parameterTypes.length];
                for (int i = arguments.length - 1; i > -1; i--)
                {
                    arguments[i] = Primitive.cast(parameterTypes[i], frame.getOperandStack().pop());
                }
                return this.execute(frame, frame.getOperandStack().pop(), method, arguments);
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
        return INVOKEINTERFACE.OP;
    }
}
