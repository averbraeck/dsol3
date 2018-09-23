package nl.tudelft.simulation.dsol.interpreter.operations;

import java.io.DataInput;
import java.io.IOException;

import nl.tudelft.simulation.dsol.interpreter.Operation;

/**
 * The factoryInterface defines the required behavior for operation factories mapping opcodes to operations.
 * <p>
 * copyright (c) 2002-2018 <a href="https://simulation.tudelft.nl">Delft University of Technology</a>. <br>
 * BSD-style license. See <a href="https://simulation.tudelft.nl/dsol/3.0/license.html">DSOL License</a>. <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs</a>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public interface FactoryInterface
{
    /** RESERVED OPCODe. */
    int BREAKPOINT = 202;

    /** RESERVED OPCODe. */
    int IMPDEP1 = 254;

    /** RESERVED OPCODe. */
    int IMPDEP2 = 255;

    /**
     * resolves an operation for an operandCode.
     * @param dataInput the dataInput
     * @param startBytePosition the position in the byteStream
     * @return Operation the operation
     * @throws IOException on IOException
     */
    Operation readOperation(final DataInput dataInput, final int startBytePosition) throws IOException;

    /**
     * resolves an operation for an operandCode.
     * @param operand the operand
     * @param dataInput the dataInput
     * @param startBytePosition the position in the byteStream
     * @return Operation the operation
     * @throws IOException on IOException
     */
    Operation readOperation(final int operand, final DataInput dataInput, final int startBytePosition)
            throws IOException;
}
