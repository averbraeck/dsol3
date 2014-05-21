/*
 * @(#) LOOKUPSWITCH.java $Date: 2010/08/10 11:38:20 $ Copyright (c) 2002-2005
 * Delft University of Technology Jaffalaan 5, 2628 BX Delft, the Netherlands.
 * All rights reserved. This software is proprietary information of Delft
 * University of Technology The code is published under the Lesser General
 * Public License
 */
package nl.tudelft.simulation.dsol.interpreter.operations;

import java.io.DataInput;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import nl.tudelft.simulation.dsol.interpreter.LocalVariable;
import nl.tudelft.simulation.dsol.interpreter.OperandStack;
import nl.tudelft.simulation.dsol.interpreter.classfile.Constant;

/**
 * The LOOKUPSWITCH operation as defined in <a
 * href="http://java.sun.com/docs/books/vmspec/2nd-edition/html/Instructions2.doc8.html">
 * http://java.sun.com/docs/books/vmspec/2nd-edition/html/Instructions2.doc8.html </a>.
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
public class LOOKUPSWITCH extends JumpOperation
{
    /** OP refers to the operand code */
    public static final int OP = 171;

    /** the byteLength of this statement */
    private int byteLength = 0;

    /** the offset table */
    private Map<Object, Integer> offsets = new HashMap<Object, Integer>();

    /**
     * constructs a new LOOKUPSWITCH
     * @param dataInput the dataInput
     * @param padding the amount of bytes to pad
     * @throws IOException on IOfailure
     */
    public LOOKUPSWITCH(final DataInput dataInput, final int padding) throws IOException
    {
        super();
        // If we pad, we pad!
        if (padding > 0)
        {
            dataInput.skipBytes(padding);
            this.byteLength = this.byteLength + padding;
        }

        this.offsets.put("default", new Integer(dataInput.readInt()));
        int entries = dataInput.readInt();
        this.byteLength = this.byteLength + 8;
        for (int i = 0; i < entries; i++)
        {
            Integer match = new Integer(dataInput.readInt());
            Integer offset = new Integer(dataInput.readInt());
            this.offsets.put(match, offset);
            this.byteLength = this.byteLength + 8;
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
        Integer key = (Integer) stack.pop();
        Integer offset = this.offsets.get(key);
        if (offset == null)
        {
            offset = this.offsets.get("default");
        }
        return offset.intValue();
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
        return LOOKUPSWITCH.OP;
    }
}