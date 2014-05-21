/*
 * @(#) ARETURN.java $Date: 2010/08/10 11:38:20 $ Copyright (c) 2002-2005 Delft
 * University of Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All
 * rights reserved. This software is proprietary information of Delft University
 * of Technology 
 */
package nl.tudelft.simulation.dsol.interpreter.operations;

import nl.tudelft.simulation.dsol.interpreter.Frame;
import nl.tudelft.simulation.dsol.interpreter.Operation;
import nl.tudelft.simulation.dsol.interpreter.classfile.ExceptionEntry;

/**
 * The ATHROW operation as defined in <a
 * href="http://java.sun.com/docs/books/vmspec/2nd-edition/html/Instructions2.doc.html">
 * http://java.sun.com/docs/books/vmspec/2nd-edition/html/Instructions2.doc.html </a>.
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
public class ATHROW extends Operation
{
    /** OP refers to the operand code */
    public static final int OP = 191;

    /** the bytePosition of this frame */
    private int bytePosition = -1;

    /**
     * constructs a new ARETURN
     * @param bytePosition where the ARETURN starts
     */
    public ATHROW(final int bytePosition)
    {
        super();
        this.bytePosition = bytePosition;
    }

    /**
     * executes the aThrow operation on a frame
     * @param frame the frame
     * @return the frame
     */
    public Frame execute(final Frame frame)
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

    /**
     * @see nl.tudelft.simulation.dsol.interpreter.Operation#getByteLength()
     */
    @Override
    public int getByteLength()
    {
        return OPCODE_BYTE_LENGTH;
    }

    /**
     * @see nl.tudelft.simulation.dsol.interpreter.Operation#getOpcode()
     */
    @Override
    public int getOpcode()
    {
        return ATHROW.OP;
    }

    /**
     * sets the bytePosition of this ATROW operation
     * @param bytePosition the bytePosition
     */
    public void setBytePosition(int bytePosition)
    {
        this.bytePosition = bytePosition;
    }

    /**
     * @return the bytePosition
     */
    public int getBytePosition()
    {
        return this.bytePosition;
    }
}