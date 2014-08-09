/*
 * @(#) TABLESWITCH.java $Date: 2010/08/10 11:38:21 $ Copyright (c) 2002-2005
 * Delft University of Technology Jaffalaan 5, 2628 BX Delft, the Netherlands.
 * All rights reserved. This software is proprietary information of Delft
 * University of Technology The code is published under the Lesser General
 * Public License
 */
package nl.tudelft.simulation.dsol.interpreter.operations;

import java.io.DataInput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import nl.tudelft.simulation.dsol.interpreter.LocalVariable;
import nl.tudelft.simulation.dsol.interpreter.OperandStack;
import nl.tudelft.simulation.dsol.interpreter.classfile.Constant;

/**
 * The TABLESWITCH operation as defined in <a
 * href="http://java.sun.com/docs/books/vmspec/2nd-edition/html/Instructions2.doc14.html">
 * http://java.sun.com/docs/books/vmspec/2nd-edition/html/Instructions2.doc14.html </a>.
 * <p>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="http://www.peter-jacobs.com/index.htm">Peter Jacobs </a><a
 *         href="mailto:a.verbraeck@tudelft.nl">Alexander Verbraeck </a>
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:38:21 $
 * @since 1.5
 */
public class TABLESWITCH extends JumpOperation
{
    /** OP refers to the operand code */
    public static final int OP = 170;

    /** the byteLength of this statement */
    private int byteLength = 0;

    /** the start position of the table */
    private int lowValue = -1;

    /** the end position of the table */
    private int highValue = -1;

    /** the offset table */
    private List<Integer> offsets = new ArrayList<Integer>();

    /**
     * constructs a new TABLESWITCH
     * @param dataInput the dataInput
     * @param padding the number of bytes to pad
     * @throws IOException on IOfailure
     */
    public TABLESWITCH(final DataInput dataInput, final int padding) throws IOException
    {
        super();

        // First we pad
        if (padding > 0)
        {
            dataInput.skipBytes(padding);
            this.byteLength = this.byteLength + padding;
        }

        // Now we place the default value at position 0
        this.offsets.add(new Integer(dataInput.readInt()));
        this.lowValue = dataInput.readInt();
        this.highValue = dataInput.readInt();
        int entries = this.highValue - this.lowValue + 1;
        this.byteLength = this.byteLength + 12;
        for (int i = 0; i < entries; i++)
        {
            Integer offset = new Integer(dataInput.readInt());
            this.offsets.add(offset);
            this.byteLength = this.byteLength + 4;
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
        int index = ((Integer) stack.pop()).intValue();
        int offset = -1;
        if (index < this.lowValue || index > this.highValue)
        {
            offset = this.offsets.get(0).intValue();
        }
        else
        {
            offset = this.offsets.get(index - this.lowValue + 1).intValue();
        }
        return offset;
    }

    /**
     * @see nl.tudelft.simulation.dsol.interpreter.Operation#getByteLength()
     */
    @Override
    public int getByteLength()
    {
        return OPCODE_BYTE_LENGTH + this.byteLength;
    }

    /**
     * @see nl.tudelft.simulation.dsol.interpreter.Operation#getOpcode()
     */
    @Override
    public int getOpcode()
    {
        return TABLESWITCH.OP;
    }
}