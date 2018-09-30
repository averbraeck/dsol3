package nl.tudelft.simulation.dsol.interpreter.operations;

import java.io.DataInput;
import java.io.IOException;

import nl.tudelft.simulation.dsol.interpreter.LocalVariable;
import nl.tudelft.simulation.dsol.interpreter.OperandStack;
import nl.tudelft.simulation.dsol.interpreter.classfile.Constant;

/**
 * The ALOAD operation as defined in
 * <a href="http://java.sun.com/docs/books/vmspec/2nd-edition/html/Instructions2.doc.html">
 * http://java.sun.com/docs/books/vmspec/2nd-edition/html/Instructions2.doc.html </a>.
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
public class ALOAD extends VoidOperation
{
    /** OP refers to the operand code. */
    public static final int OP = 25;

    /** the index to load. */
    private final int index;

    /** see the wide statement. */
    private final boolean widened;

    /**
     * constructs a new ALOAD.
     * @param dataInput the dataInput
     * @throws IOException on IOfailure
     */
    public ALOAD(final DataInput dataInput) throws IOException
    {
        this(dataInput, false);
    }

    /**
     * constructs a new ALOAD.
     * @param dataInput the dataInput
     * @param widened whether or not to widen
     * @throws IOException on IOfailure
     */
    public ALOAD(final DataInput dataInput, final boolean widened) throws IOException
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
        stack.push(localVariables[this.index].getValue());
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
        return ALOAD.OP;
    }
}
