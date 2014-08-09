/*
 * @(#) PUTSTATIC.java $Date: 2010/08/10 11:38:20 $ Copyright (c) 2002-2005
 * Delft University of Technology Jaffalaan 5, 2628 BX Delft, the Netherlands.
 * All rights reserved. This software is proprietary information of Delft
 * University of Technology The code is published under the Lesser General
 * Public License
 */
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
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="http://www.peter-jacobs.com/index.htm">Peter Jacobs </a><a
 *         href="mailto:a.verbraeck@tudelft.nl">Alexander Verbraeck </a>
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:38:20 $
 * @since 1.5
 */
public class PUTSTATIC extends VoidOperation
{
    /** OP refers to the operand code */
    public static final int OP = 179;

    /** the index to load */
    private int index = -1;

    /**
     * constructs a new PUTSTATIC
     * @param dataInput the dataInput
     * @throws IOException on IOfailure
     */
    public PUTSTATIC(final DataInput dataInput) throws IOException
    {
        super();
        this.index = dataInput.readUnsignedShort();
    }

    /**
     * @see nl.tudelft.simulation.dsol.interpreter.operations.VoidOperation#execute(nl.tudelft.simulation.dsol.interpreter.OperandStack,
     *      nl.tudelft.simulation.dsol.interpreter.classfile.Constant[],
     *      nl.tudelft.simulation.dsol.interpreter.LocalVariable[])
     */
    @Override
    public void execute(final OperandStack stack, final Constant[] constantPool, final LocalVariable[] localVariables)
    {
        try
        {
            Field field = null;
            ConstantFieldref constantFieldref = (ConstantFieldref) constantPool[this.index];
            Class referenceClass = constantFieldref.getConstantClass().getValue().getClassValue();
            field = ClassUtil.resolveField(referenceClass, constantFieldref.getConstantNameAndType().getName());
            field.setAccessible(true);
            Object value = stack.pop();
            Object target = null;
            if (!field.getType().isPrimitive())
            {
                field.set(target, value);
                return;
            }
            if (field.getType().equals(boolean.class) || field.getType().equals(byte.class)
                    || field.getType().equals(char.class) || field.getType().equals(short.class)
                    || field.getType().equals(int.class))
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
        return PUTSTATIC.OP;
    }
}