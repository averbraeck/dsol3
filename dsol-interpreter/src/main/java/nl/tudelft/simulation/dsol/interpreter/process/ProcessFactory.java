package nl.tudelft.simulation.dsol.interpreter.process;

import java.io.DataInput;
import java.io.IOException;
import java.lang.reflect.Method;

import nl.tudelft.simulation.dsol.interpreter.Operation;
import nl.tudelft.simulation.dsol.interpreter.operations.custom.CustomFactory;
import nl.tudelft.simulation.dsol.interpreter.operations.custom.InterpreterOracleInterface;
import nl.tudelft.simulation.dsol.interpreter.operations.reflection.INVOKESPECIAL;
import nl.tudelft.simulation.dsol.interpreter.operations.reflection.INVOKEVIRTUAL;
import nl.tudelft.simulation.logger.CategoryLogger;

/**
 * An InterpreterFactory for processes that can be suspended without threads.
 * <p>
 * copyright (c) 2002-2018 <a href="https://simulation.tudelft.nl">Delft University of Technology</a>. <br>
 * BSD-style license. See <a href="https://simulation.tudelft.nl/dsol/3.0/license.html">DSOL License</a>. <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs</a>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class ProcessFactory extends CustomFactory
{
    /** the SUSPEND METHOD. */
    protected static Method suspendMethod;

    static
    {
        try
        {
            suspendMethod = InterpretableProcess.class.getMethod("suspend");
        }
        catch (Exception exception)
        {
            CategoryLogger.always().error(exception);
        }
    }

    /**
     * constructs a new InterpreterFactory.
     * @param interpreterOracle the interpreterOracle to use
     */
    public ProcessFactory(final InterpreterOracleInterface interpreterOracle)
    {
        super(interpreterOracle);
    }

    /**
     * reads a sequence of bytes and returns the appropriate bytecode operations.
     * @param operand the operatand (short value)
     * @param dataInput the dataInput to read from
     * @param startBytePostion the position in the current block of bytecode.
     * @return the assemnbly Operation
     * @throws IOException on IO exception
     */
    @Override
    public final Operation readOperation(final int operand, final DataInput dataInput, final int startBytePostion)
            throws IOException
    {
        switch (operand)
        {
            case INVOKESPECIAL.OP:
                return new PROCESSINVOKESPECIAL(getInterpreterOracle(), dataInput);
            case INVOKEVIRTUAL.OP:
                return new PROCESSINVOKEVIRTUAL(getInterpreterOracle(), dataInput);
            default:
                return super.readOperation(operand, dataInput, startBytePostion);
        }
    }
}
