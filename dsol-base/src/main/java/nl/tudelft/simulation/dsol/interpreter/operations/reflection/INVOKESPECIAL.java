/*
 * @(#) INVOKESPECIAL.java $Date: 2010/08/10 11:38:24 $ Copyright (c) 2002-2005
 * Delft University of Technology Jaffalaan 5, 2628 BX Delft, the Netherlands.
 * All rights reserved. This software is proprietary information of Delft
 * University of Technology The code is published under the Lesser General
 * Public License
 */
package nl.tudelft.simulation.dsol.interpreter.operations.reflection;

import java.io.DataInput;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import nl.tudelft.simulation.dsol.interpreter.Frame;
import nl.tudelft.simulation.dsol.interpreter.InterpreterException;
import nl.tudelft.simulation.dsol.interpreter.LocalVariable;
import nl.tudelft.simulation.dsol.interpreter.classfile.ConstantMethodref;
import nl.tudelft.simulation.dsol.interpreter.operations.InvokeOperation;
import nl.tudelft.simulation.dsol.interpreter.operations.NEW;
import nl.tudelft.simulation.language.primitives.Primitive;
import nl.tudelft.simulation.language.reflection.ClassUtil;
import nl.tudelft.simulation.language.reflection.MethodSignature;

/**
 * INVOKESPECIAL <br>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @version 1.0 $Revision: 1.2 $ $Date: 2010/08/10 11:38:24 $
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a> <br>
 *         <a href="mailto:a.verbraeck@tudelft.nl">Alexander Verbraeck </a>
 */
public class INVOKESPECIAL extends InvokeOperation
{
    /** OP refers to the operand code */
    public static final int OP = 183;

    /** the index to load */
    protected int index = -1;

    /**
     * constructs a new INVOKESPECIAL
     * @param dataInput the dataInput
     * @throws IOException on IOfailure
     */
    public INVOKESPECIAL(final DataInput dataInput) throws IOException
    {
        super();
        this.index = dataInput.readUnsignedShort();
    }

    /**
     * @see nl.tudelft.simulation.dsol.interpreter.operations.InvokeOperation
     *      #execute(nl.tudelft.simulation.dsol.interpreter.Frame)
     */
    @Override
    public Frame execute(final Frame frame)
    {
        try
        {
            ConstantMethodref constantMethodref = (ConstantMethodref) frame.getConstantPool()[this.index];
            Class[] parameterTypes =
                    new MethodSignature(constantMethodref.getConstantNameAndType().getDescriptor()).getParameterTypes();
            String methodName = constantMethodref.getConstantNameAndType().getName();
            if (methodName.equals("<init>"))
            {
                Object[] args = new Object[parameterTypes.length];
                for (int i = args.length - 1; i > -1; i--)
                {
                    args[i] = Primitive.cast(parameterTypes[i], frame.getOperandStack().pop());
                }
                Object objectRef = frame.getOperandStack().pop();
                Class instanceClass = ((NEW.UninitializedInstance) objectRef).getInstanceClass();
                Constructor<?> constructor = ClassUtil.resolveConstructor(instanceClass, parameterTypes);
                constructor.setAccessible(true);
                Object result = constructor.newInstance(args);
                frame.getOperandStack().replace(objectRef, result);
                LocalVariable.replace(frame.getLocalVariables(), objectRef, result);
                return null;
            }

            Object objectRef = frame.getOperandStack().peek(parameterTypes.length);

            // Now the normal methods
            Method method = null;

            // look if we need to resolve a super() method
            Class referenceClass = constantMethodref.getConstantClass().getValue().getClassValue();
            if (objectRef.getClass().getSuperclass().equals(referenceClass))
            {
                method = ClassUtil.resolveMethod(objectRef.getClass().getSuperclass(), methodName, parameterTypes);
            }
            else
            {
                method = ClassUtil.resolveMethod(objectRef, methodName, parameterTypes);
            }

            synchronized (frame.getOperandStack())
            {
                // Let's create the arguments
                Object[] args = new Object[parameterTypes.length];
                for (int i = args.length - 1; i > -1; i--)
                {
                    args[i] = Primitive.cast(parameterTypes[i], frame.getOperandStack().pop());
                }
                frame.getOperandStack().pop();
                return this.execute(frame, objectRef, method, args);
            }
        }
        catch (Exception exception)
        {
            throw new InterpreterException(exception);
        }
    }

    /**
     * executes the method on the objectRef
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

    /**
     * @see nl.tudelft.simulation.dsol.interpreter.Operation#getByteLength()
     */
    @Override
    public int getByteLength()
    {
        return OPCODE_BYTE_LENGTH + 2;
    }

    /**
     * @see nl.tudelft.simulation.dsol.interpreter.Operation#getOpcode()
     */
    @Override
    public int getOpcode()
    {
        return INVOKESPECIAL.OP;
    }
}