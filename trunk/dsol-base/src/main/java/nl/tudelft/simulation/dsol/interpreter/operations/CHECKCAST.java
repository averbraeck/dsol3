/*
 * @(#) CHECKCAST.java $Date: 2010/08/10 11:38:20 $ Copyright (c) 2002-2005
 * Delft University of Technology Jaffalaan 5, 2628 BX Delft, the Netherlands.
 * All rights reserved. This software is proprietary information of Delft
 * University of Technology The code is published under the Lesser General
 * Public License
 */
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
 * <p>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a><a
 *         href="mailto:a.verbraeck@tudelft.nl">Alexander Verbraeck </a>
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:38:20 $
 * @since 1.5
 */
public class CHECKCAST extends VoidOperation
{
    /** OP refers to the operand code */
    public static final int OP = 192;

    /** the index to load */
    private int index = -1;

    /**
     * constructs a new ALOAD
     * @param dataInput the dataInput
     * @throws IOException on IOfailure
     */
    public CHECKCAST(final DataInput dataInput) throws IOException
    {
        super();
        this.index = dataInput.readUnsignedShort();
    }

    /**
     * @see nl.tudelft.simulation.dsol.interpreter.operations.VoidOperation
     *      #execute(nl.tudelft.simulation.dsol.interpreter.OperandStack,
     *      nl.tudelft.simulation.dsol.interpreter.classfile.Constant[],
     *      nl.tudelft.simulation.dsol.interpreter.LocalVariable[])
     */
    @Override
    public void execute(final OperandStack stack, final Constant[] constantPool, final LocalVariable[] localVariables)
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
        return CHECKCAST.OP;
    }
}