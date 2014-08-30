/*
 * @(#) LDC.java $Date: 2010/08/10 11:38:23 $ Copyright (c) 2002-2005 Delft
 * University of Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All
 * rights reserved. This software is proprietary information of Delft University
 * of Technology 
 */
package nl.tudelft.simulation.dsol.interpreter.operations;

import java.io.DataInput;
import java.io.IOException;

import nl.tudelft.simulation.dsol.interpreter.LocalVariable;
import nl.tudelft.simulation.dsol.interpreter.OperandStack;
import nl.tudelft.simulation.dsol.interpreter.classfile.Constant;
import nl.tudelft.simulation.dsol.interpreter.classfile.ConstantClass;
import nl.tudelft.simulation.dsol.interpreter.classfile.ConstantFloat;
import nl.tudelft.simulation.dsol.interpreter.classfile.ConstantInteger;
import nl.tudelft.simulation.dsol.interpreter.classfile.ConstantString;
import nl.tudelft.simulation.language.reflection.FieldSignature;
import nl.tudelft.simulation.logger.Logger;

/**
 * The LDC operation as defined in <a
 * href="http://java.sun.com/docs/books/vmspec/2nd-edition/html/Instructions2.doc8.html">
 * http://java.sun.com/docs/books/vmspec/2nd-edition/html/Instructions2.doc8.html </a>.
 * <p>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a><a
 *         href="mailto:a.verbraeck@tudelft.nl">Alexander Verbraeck </a>
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:38:23 $
 * @since 1.5
 */
public class LDC extends VoidOperation
{
    /** OP refers to the operand code */
    public static final int OP = 18;

    /** the index to load */
    private int index = -1;

    /**
     * constructs a new LDC
     * @param dataInput the dataInput
     * @throws IOException on IOfailure
     */
    public LDC(final DataInput dataInput) throws IOException
    {
        super();
        this.index = dataInput.readUnsignedByte();
    }

    /**
     * @see nl.tudelft.simulation.dsol.interpreter.operations.VoidOperation#execute(nl.tudelft.simulation.dsol.interpreter.OperandStack,
     *      nl.tudelft.simulation.dsol.interpreter.classfile.Constant[],
     *      nl.tudelft.simulation.dsol.interpreter.LocalVariable[])
     */
    @Override
    public void execute(final OperandStack stack, final Constant[] constantPool, final LocalVariable[] localVariables)
    {
        Constant constant = constantPool[this.index];
        if (constant instanceof ConstantInteger)
        {
            stack.push(new Integer(((ConstantInteger) constant).getValue()));
        }
        else if (constant instanceof ConstantFloat)
        {
            stack.push(new Float(((ConstantFloat) constant).getValue()));
        }
        else if (constant instanceof ConstantString)
        {
            stack.push(((ConstantString) constant).getValue());
        }
        else if (constant instanceof ConstantClass)
        {
            FieldSignature object = ((ConstantClass) constant).getValue();
            try
            {
                stack.push(object.getClassValue());
            }
            catch (ClassNotFoundException classNotFoundException)
            {
                Logger.warning(this, "execute", classNotFoundException);
            }
        }
    }

    /**
     * @see nl.tudelft.simulation.dsol.interpreter.Operation#getByteLength()
     */
    @Override
    public int getByteLength()
    {
        return OPCODE_BYTE_LENGTH + 1;
    }

    /**
     * @see nl.tudelft.simulation.dsol.interpreter.Operation#getOpcode()
     */
    @Override
    public int getOpcode()
    {
        return LDC.OP;
    }
}