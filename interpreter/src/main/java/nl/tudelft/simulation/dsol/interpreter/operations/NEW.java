/*
 * @(#) NEW.java $Date: 2010/08/10 11:38:22 $ Copyright (c) 2002-2005 Delft
 * University of Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All
 * rights reserved. This software is proprietary information of Delft University
 * of Technology 
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
 * The NEW operation as defined in <a
 * href="http://java.sun.com/docs/books/vmspec/2nd-edition/html/Instructions2.doc10.html">
 * http://java.sun.com/docs/books/vmspec/2nd-edition/html/Instructions2.doc10.html </a>.
 * <p>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="http://www.peter-jacobs.com/index.htm">Peter Jacobs </a><a
 *         href="mailto:a.verbraeck@tudelft.nl">Alexander Verbraeck </a>
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:38:22 $
 * @since 1.5
 */
public class NEW extends VoidOperation
{
    /** OP refers to the operand code */
    public static final int OP = 187;

    /** index refers to the constantpool index */
    private int index = -1;

    /**
     * constructs a new NEW
     * @param dataInput the dataInput
     * @throws IOException on IOfailure
     */
    public NEW(final DataInput dataInput) throws IOException
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
            Class instanceClass = null;
            instanceClass = ((ConstantClass) constantPool[this.index]).getValue().getClassValue();
            stack.push(new UninitializedInstance(instanceClass));
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
        return NEW.OP;
    }

    /**
     * holder class for "to-be-constructed" instances
     */
    public static class UninitializedInstance
    {
        /** the value */
        private Class instanceClass = null;

        /**
         * constructs a new UninitializedInstance
         * @param instanceClass the class of which an instance must be made
         */
        public UninitializedInstance(final Class instanceClass)
        {
            this.instanceClass = instanceClass;
        }

        /**
         * @return return the instanceClass
         */
        public Class getInstanceClass()
        {
            return this.instanceClass;
        }
    }
}