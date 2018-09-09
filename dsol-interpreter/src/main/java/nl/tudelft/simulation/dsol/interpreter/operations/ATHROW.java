package nl.tudelft.simulation.dsol.interpreter.operations;

import nl.tudelft.simulation.dsol.interpreter.Frame;
import nl.tudelft.simulation.dsol.interpreter.Operation;
import nl.tudelft.simulation.dsol.interpreter.classfile.ExceptionEntry;

/**
 * The ATHROW operation as defined in <a
 * href="http://java.sun.com/docs/books/vmspec/2nd-edition/html/Instructions2.doc.html">
 * http://java.sun.com/docs/books/vmspec/2nd-edition/html/Instructions2.doc.html </a>.
 * <p>
 * copyright (c) 2002-2018  <a href="https://simulation.tudelft.nl">Delft University of Technology</a>. <br>
 * BSD-style license. See <a href="https://simulation.tudelft.nl/dsol/3.0/license.html">DSOL License</a>. <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs</a>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class ATHROW extends Operation
{
    /** OP refers to the operand code. */
    public static final int OP = 191;

    /** the bytePosition of this frame. */
    private int bytePosition;

    /**
     * constructs a new ARETURN.
     * @param bytePosition where the ARETURN starts
     */
    public ATHROW(final int bytePosition)
    {
        super();
        this.bytePosition = bytePosition;
    }

    /**
     * executes the aThrow operation on a frame.
     * @param frame the frame
     * @return the frame
     */
    public final Frame execute(final Frame frame)
    {
        Throwable throwable = new NullPointerException();
        if (!frame.getOperandStack().isEmpty())
        {
            throwable = (Throwable) frame.getOperandStack().pop();
        }
        // Let's clear all but the exception from the stack
        frame.getOperandStack().clear();
        frame.getOperandStack().push(throwable);
        // Now we search for a handler
        ExceptionEntry[] exceptionEntries = frame.getMethodDescriptor().getExceptionTable();
        ExceptionEntry exceptionEntry =
                ExceptionEntry.resolveExceptionEntry(exceptionEntries, throwable.getClass(), this.bytePosition);
        frame.getOperandStack().push(exceptionEntry);
        return frame;
    }

    /** {@inheritDoc} */
    @Override
    public final int getByteLength()
    {
        return OPCODE_BYTE_LENGTH;
    }

    /** {@inheritDoc} */
    @Override
    public final int getOpcode()
    {
        return ATHROW.OP;
    }

    /**
     * @return the bytePosition
     */
    public final int getBytePosition()
    {
        return this.bytePosition;
    }

    /**
     * As an exception, the Interpreter class needs to be able to set the byte position.
     * @param bytePosition set bytePosition
     */
    public final void setBytePosition(final int bytePosition)
    {
        this.bytePosition = bytePosition;
    }
}
