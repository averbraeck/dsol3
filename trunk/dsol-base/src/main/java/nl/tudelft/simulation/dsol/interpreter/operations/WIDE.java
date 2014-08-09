/*
 * @(#) WIDE.java $Date: 2010/08/10 11:38:22 $ Copyright (c) 2002-2005 Delft
 * University of Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All
 * rights reserved. This software is proprietary information of Delft University
 * of Technology 
 */
package nl.tudelft.simulation.dsol.interpreter.operations;

import java.io.DataInput;
import java.io.IOException;

import nl.tudelft.simulation.dsol.interpreter.Operation;

/**
 * The WIDE operation as defined in <a
 * href="http://java.sun.com/docs/books/vmspec/2nd-edition/html/Instructions2.doc15.html">
 * http://java.sun.com/docs/books/vmspec/2nd-edition/html/Instructions2.doc15.html </a>.
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
public class WIDE extends Operation
{
    /** OP refers to the operand code */
    public static final int OP = 196;

    /** the index to the target */
    private Operation target;

    /**
     * constructs a new WIDE
     * @param dataInput the dataInput
     * @throws IOException on IOfailure
     */
    public WIDE(final DataInput dataInput) throws IOException
    {
        super();
        int operand = dataInput.readUnsignedShort();
        switch (operand)
        {
            case ILOAD.OP:
                this.target = new ILOAD(dataInput, true);
                break;
            case FLOAD.OP:
                this.target = new FLOAD(dataInput, true);
                break;
            case ALOAD.OP:
                this.target = new ALOAD(dataInput, true);
                break;
            case LLOAD.OP:
                this.target = new LLOAD(dataInput, true);
                break;
            case DLOAD.OP:
                this.target = new DLOAD(dataInput, true);
                break;
            case ISTORE.OP:
                this.target = new ISTORE(dataInput, true);
                break;
            case FSTORE.OP:
                this.target = new FSTORE(dataInput, true);
                break;
            case ASTORE.OP:
                this.target = new ASTORE(dataInput, true);
                break;
            case LSTORE.OP:
                this.target = new LSTORE(dataInput, true);
                break;
            case DSTORE.OP:
                this.target = new DSTORE(dataInput, true);
                break;
            case RET.OP:
                this.target = new RET(dataInput, true);
                break;
            case IINC.OP:
                this.target = new IINC(dataInput, true);
                break;
            default:
                throw new IOException("Cannot use operand=" + operand + " in wide");
        }
    }

    /**
     * @see nl.tudelft.simulation.dsol.interpreter.Operation#getByteLength()
     */
    @Override
    public int getByteLength()
    {
        return 2 * OPCODE_BYTE_LENGTH + this.target.getByteLength();
    }

    /**
     * @see nl.tudelft.simulation.dsol.interpreter.Operation#getOpcode()
     */
    @Override
    public int getOpcode()
    {
        return WIDE.OP;
    }

    /**
     * @return Returns the target.
     */
    public Operation getTarget()
    {
        return this.target;
    }

}