package nl.tudelft.simulation.dsol.interpreter.operations;

import java.io.DataInput;
import java.io.IOException;

import nl.tudelft.simulation.dsol.interpreter.Operation;

/**
 * The WIDE operation as defined in <a
 * href="http://java.sun.com/docs/books/vmspec/2nd-edition/html/Instructions2.doc15.html">
 * http://java.sun.com/docs/books/vmspec/2nd-edition/html/Instructions2.doc15.html </a>.
 * <p />
 * (c) copyright 2002-2014 <a href="http://www.simulation.tudelft.nl">Delft University of Technology</a>. <br />
 * BSD-style license. See <a href="http://www.simulation.tudelft.nl/dsol/3.0/license.html">DSOL License</a>. <br />
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs</a>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class WIDE extends Operation
{
    /** OP refers to the operand code. */
    public static final int OP = 196;

    /** the index to the target. */
    private Operation target;

    /**
     * constructs a new WIDE.
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

    /** {@inheritDoc} */
    @Override
    public final int getByteLength()
    {
        return 2 * OPCODE_BYTE_LENGTH + this.target.getByteLength();
    }

    /** {@inheritDoc} */
    @Override
    public final int getOpcode()
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
