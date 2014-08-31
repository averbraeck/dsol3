package nl.tudelft.simulation.dsol.interpreter.operations;

import java.io.DataInput;
import java.io.IOException;
import java.lang.reflect.Array;

import nl.tudelft.simulation.dsol.interpreter.LocalVariable;
import nl.tudelft.simulation.dsol.interpreter.OperandStack;
import nl.tudelft.simulation.dsol.interpreter.classfile.Constant;

/**
 * The NEWARRAY operation as defined in <a
 * href="http://java.sun.com/docs/books/vmspec/2nd-edition/html/Instructions2.doc10.html">
 * http://java.sun.com/docs/books/vmspec/2nd-edition/html/Instructions2.doc10.html </a>.
 * <p />
 * (c) copyright 2002-2014 <a href="http://www.simulation.tudelft.nl">Delft University of Technology</a>. <br />
 * BSD-style license. See <a href="http://www.simulation.tudelft.nl/dsol/3.0/license.html">DSOL License</a>. <br />
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs</a>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class NEWARRAY extends VoidOperation
{
    /** OP refers to the operand code. */
    public static final int OP = 188;

    /** the type to load. */
    private int atype = -1;

    /**
     * constructs a new NEWARRAY.
     * @param dataInput the dataInput
     * @throws IOException on IOfailure
     */
    public NEWARRAY(final DataInput dataInput) throws IOException
    {
        super();
        this.atype = dataInput.readUnsignedByte();
    }

    /** {@inheritDoc} */
    @Override
    public final void execute(final OperandStack stack, final Constant[] constantPool,
            final LocalVariable[] localVariables)
    {
        int length = ((Integer) stack.pop()).intValue();
        Class<?> clazz = null;
        switch (this.atype)
        {
            case 4:
                clazz = boolean.class;
                break;
            case 5:
                clazz = char.class;
                break;
            case 6:
                clazz = float.class;
                break;
            case 7:
                clazz = double.class;
                break;
            case 8:
                clazz = byte.class;
                break;
            case 9:
                clazz = short.class;
                break;
            case 10:
                clazz = int.class;
                break;
            case 11:
                clazz = long.class;
                break;
            default:
                throw new RuntimeException("unknown atype in NEWARRAY");
        }
        Object array = Array.newInstance(clazz, length);
        stack.push(array);
    }

    /** {@inheritDoc} */
    @Override
    public final int getByteLength()
    {
        return OPCODE_BYTE_LENGTH + 1;
    }

    /** {@inheritDoc} */
    @Override
    public final int getOpcode()
    {
        return NEWARRAY.OP;
    }
}
