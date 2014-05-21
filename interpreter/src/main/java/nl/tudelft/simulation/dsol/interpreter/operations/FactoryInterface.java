/*
 * @(#) FactoryInterface.java Jan 14, 2004 Copyright (c) 2002-2005 Delft
 * University of Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All
 * rights reserved. This software is proprietary information of Delft University
 * of Technology 
 */
package nl.tudelft.simulation.dsol.interpreter.operations;

import java.io.DataInput;
import java.io.IOException;

import nl.tudelft.simulation.dsol.interpreter.Operation;

/**
 * The factoryInterface defines the required behavior for operation factories mapping opcodes to operations.
 * <p>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="http://www.peter-jacobs.com/index.htm">Peter Jacobs </a>
 * @version 1.2 Apr 6, 2004
 * @since 1.5
 */
public interface FactoryInterface
{
    /** RESERVED OPCODE */
    int BREAKPOINT = 202;

    /** RESERVED OPCODE */
    int IMPDEP1 = 254;

    /** RESERVED OPCODE */
    int IMPDEP2 = 255;

    /**
     * resolves an operation for an operandCode
     * @param dataInput the dataInput
     * @param startBytePosition the position in the byteStream
     * @return Operation the operation
     * @throws IOException on IOException
     */
    Operation readOperation(final DataInput dataInput, final int startBytePosition) throws IOException;

    /**
     * resolves an operation for an operandCode
     * @param operand the operand
     * @param dataInput the dataInput
     * @param startBytePosition the position in the byteStream
     * @return Operation the operation
     * @throws IOException on IOException
     */
    Operation readOperation(final int operand, final DataInput dataInput, final int startBytePosition)
            throws IOException;
}