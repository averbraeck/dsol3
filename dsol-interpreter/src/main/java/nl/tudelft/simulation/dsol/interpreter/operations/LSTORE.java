package nl.tudelft.simulation.dsol.interpreter.operations;

import java.io.DataInput;
import java.io.IOException;

import nl.tudelft.simulation.dsol.interpreter.LocalVariable;
import nl.tudelft.simulation.dsol.interpreter.OperandStack;
import nl.tudelft.simulation.dsol.interpreter.classfile.Constant;

/**
 * The LSTORE operation as defined in
 * <a href="https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5">
 * https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5 </a>.
 * <p>
 * Copyright (c) 2002-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. See for project information <a href="https://simulation.tudelft.nl/" target="_blank">
 * https://simulation.tudelft.nl</a>. The DSOL project is distributed under a three-clause BSD-style license, which can
 * be found at <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs</a>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class LSTORE extends VoidOperation
{
    /** OP refers to the operand code. */
    public static final int OP = 55;

    /** the index to load. */
    private final int index;

    /** see the wide statement. */
    private final boolean widened;

    /**
     * constructs a new LSTORE.
     * @param dataInput the dataInput
     * @throws IOException on IOfailure
     */
    public LSTORE(final DataInput dataInput) throws IOException
    {
        this(dataInput, false);
    }

    /**
     * constructs a new LSTORE.
     * @param dataInput the dataInput
     * @param widened whether or not to widen
     * @throws IOException on IOfailure
     */
    public LSTORE(final DataInput dataInput, final boolean widened) throws IOException
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

    /** {@inheritDoc} */
    @Override
    public final void execute(final OperandStack stack, final Constant[] constantPool,
            final LocalVariable[] localVariables)
    {
        localVariables[this.index].setValue(stack.pop());
    }

    /** {@inheritDoc} */
    @Override
    public final int getByteLength()
    {
        int result = OPCODE_BYTE_LENGTH + 1;
        if (this.widened)
        {
            result++;
        }
        return result;

    }

    /** {@inheritDoc} */
    @Override
    public final int getOpcode()
    {
        return LSTORE.OP;
    }
}
