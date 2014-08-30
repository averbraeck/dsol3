/*
 * @(#) RET.java $Date: 2010/08/10 11:38:20 $ Copyright (c) 2002-2005 Delft
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
import nl.tudelft.simulation.language.primitives.Primitive;

/**
 * The RET operation as defined in <a
 * href="http://java.sun.com/docs/books/vmspec/2nd-edition/html/Instructions2.doc12.html">
 * http://java.sun.com/docs/books/vmspec/2nd-edition/html/Instructions2.doc12.html </a>.
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
public class RET extends JumpOperation
{
    /** OP refers to the operand code */
    public static final int OP = 169;

    /** the index to use */
    private int index = -1;

    /** see the wide statement */
    private boolean widened = false;

    /**
     * constructs a new RET
     * @param dataInput the dataInput to read
     * @throws IOException on IOException
     */
    public RET(final DataInput dataInput) throws IOException
    {
        this(dataInput, false);
    }

    /**
     * constructs a new RET
     * @param dataInput the dataInput to read
     * @param widened whether or not to widen
     * @throws IOException on IOException
     */
    public RET(final DataInput dataInput, final boolean widened) throws IOException
    {
        super();
        this.widened = widened;
        if (widened)
        {
            this.index = dataInput.readUnsignedShort();
        }
        else
        {
            this.index = dataInput.readUnsignedByte();
        }
    }

    /**
     * @see nl.tudelft.simulation.dsol.interpreter.operations.JumpOperation
     *      #execute(nl.tudelft.simulation.dsol.interpreter.OperandStack,
     *      nl.tudelft.simulation.dsol.interpreter.classfile.Constant[],
     *      nl.tudelft.simulation.dsol.interpreter.LocalVariable[])
     */
    @Override
    public int execute(final OperandStack stack, final Constant[] constantPool, final LocalVariable[] localVariables)
    {
        return Primitive.toInteger(localVariables[this.index].getValue()).intValue();
    }

    /**
     * @see nl.tudelft.simulation.dsol.interpreter.Operation#getByteLength()
     */
    @Override
    public int getByteLength()
    {
        int result = OPCODE_BYTE_LENGTH + 1;
        if (this.widened)
        {
            result++;
        }
        return result;
    }

    /**
     * @see nl.tudelft.simulation.dsol.interpreter.Operation#getOpcode()
     */
    @Override
    public int getOpcode()
    {
        return RET.OP;
    }
}