/*
 * @(#) MULTIANEWARRAY.java $Date: 2010/08/10 11:38:20 $ Copyright (c) 2002-2005
 * Delft University of Technology Jaffalaan 5, 2628 BX Delft, the Netherlands.
 * All rights reserved. This software is proprietary information of Delft
 * University of Technology The code is published under the Lesser General
 * Public License
 */
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
import nl.tudelft.simulation.language.reflection.FieldSignature;

/**
 * The MULTINEWARRAY operation as defined in <a
 * href="http://java.sun.com/docs/books/vmspec/2nd-edition/html/Instructions2.doc9.html">
 * http://java.sun.com/docs/books/vmspec/2nd-edition/html/Instructions2.doc9.html
 * </a>.
 * <p>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft
 * University of Technology </a>, the Netherlands. <br>
 * See for project information <a
 * href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser
 * General Public License (LGPL) </a>, no warranty.
 * 
 * @author <a href="http://www.peter-jacobs.com/index.htm">Peter Jacobs </a><a
 *         href="mailto:a.verbraeck@tudelft.nl">Alexander
 *         Verbraeck </a>
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:38:20 $
 * @since 1.5
 */
public class MULTIANEWARRAY extends VoidOperation
{
    /** OP refers to the operand code */
    public static final int OP = 197;

    /** the index to load */
    private int index = -1;

    /** the dimensions of the new array */
    private int dimensions = -1;

    /**
     * constructs a new MULTIANEWARRAY
     * 
     * @param dataInput the dataInput
     * @throws IOException on IOfailure
     */
    public MULTIANEWARRAY(final DataInput dataInput) throws IOException
    {
        super();
        this.index = dataInput.readUnsignedShort();
        this.dimensions = dataInput.readUnsignedByte();
    }

    /**
     * @see nl.tudelft.simulation.dsol.interpreter.operations.VoidOperation#execute(
     *      nl.tudelft.simulation.dsol.interpreter.OperandStack,
     *      nl.tudelft.simulation.dsol.interpreter.classfile.Constant[],
     *      nl.tudelft.simulation.dsol.interpreter.LocalVariable[])
     */
    @Override
    public void execute(final OperandStack stack,
            final Constant[] constantPool, final LocalVariable[] localVariables)
    {
        ConstantClass constant = (ConstantClass) constantPool[this.index];
        Class clazz = null;
        try
        {
            clazz = constant.getValue().getClassValue();
        } catch (Exception exception)
        {
            throw new InterpreterException(exception);
        }
        int[] dimensions = new int[this.dimensions];
        for (int i = dimensions.length - 1; i >= 0; i--)
        {
            dimensions[i] = Primitive.toInteger(stack.pop()).intValue();
        }
        try
        {
            Class componentType = FieldSignature.toClass(clazz.getName()
                    .replaceAll("\\[", ""));
            Object result = newArray(0, dimensions, componentType);
            stack.push(result);
        } catch (ClassNotFoundException exception)
        {
            throw new InterpreterException(exception);
        }
    }

    /**
     * creates a new Array
     * 
     * @param depth the depth
     * @param dimensions the dimensions
     * @param clazz the clazz
     * @return Object the array
     */
    private Object newArray(final int depth, final int[] dimensions,
            final Class< ? > clazz)
    {
        try
        {
            if (depth == dimensions.length)
            {
                // last level; now we make 'basicClass' instances
                // these can be either simple types or objects
                if (clazz.isPrimitive())
                {
                    throw new InterpreterException("may not occur");
                }
                return clazz.newInstance();
            }
            if (depth == dimensions.length - 1)
            {
                if (clazz.isPrimitive())
                {
                    return Array.newInstance(clazz, dimensions[depth]);
                }
            }
            Object arrayref = Array
                    .newInstance(Object.class, dimensions[depth]);
            for (int i = 0; i < dimensions[depth]; i++)
            {
                Array.set(arrayref, i, newArray(depth + 1, dimensions, clazz));
            }
            return arrayref;
        } catch (Exception exception)
        {
            throw new InterpreterException(exception);
        }
    }

    /**
     * @see nl.tudelft.simulation.dsol.interpreter.Operation#getByteLength()
     */
    @Override
    public int getByteLength()
    {
        return OPCODE_BYTE_LENGTH + 3;
    }

    /**
     * @see nl.tudelft.simulation.dsol.interpreter.Operation#getOpcode()
     */
    @Override
    public int getOpcode()
    {
        return MULTIANEWARRAY.OP;
    }
}