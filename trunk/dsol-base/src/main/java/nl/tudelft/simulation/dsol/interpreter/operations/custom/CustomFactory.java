package nl.tudelft.simulation.dsol.interpreter.operations.custom;

import java.io.DataInput;
import java.io.IOException;

import nl.tudelft.simulation.dsol.interpreter.Operation;
import nl.tudelft.simulation.dsol.interpreter.operations.InterpreterFactory;
import nl.tudelft.simulation.dsol.interpreter.operations.reflection.INVOKEINTERFACE;
import nl.tudelft.simulation.dsol.interpreter.operations.reflection.INVOKESPECIAL;
import nl.tudelft.simulation.dsol.interpreter.operations.reflection.INVOKESTATIC;
import nl.tudelft.simulation.dsol.interpreter.operations.reflection.INVOKEVIRTUAL;

/**
 * A InterpreterFactory.
 * <p />
 * (c) copyright 2002-2014 <a href="http://www.simulation.tudelft.nl">Delft University of Technology</a>. <br />
 * BSD-style license. See <a href="http://www.simulation.tudelft.nl/dsol/3.0/license.html">DSOL License</a>. <br />
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs</a>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class CustomFactory extends InterpreterFactory
{
    /** the interpreterOracle to use. */
    private final InterpreterOracleInterface interpreterOracle;

    /**
     * constructs a new InterpreterFactory.
     * @param interpreterOracle the oracle to use
     */
    public CustomFactory(final InterpreterOracleInterface interpreterOracle)
    {
        super();
        this.interpreterOracle = interpreterOracle;
    }

    /** {@inheritDoc} */
    @Override
    public Operation readOperation(final int operand, final DataInput dataInput, final int startBytePostion)
            throws IOException
    {
        switch (operand)
        {
            case INVOKEINTERFACE.OP:
                return new CUSTOMINVOKEINTERFACE(this.interpreterOracle, dataInput);
            case INVOKESPECIAL.OP:
                return new CUSTOMINVOKESPECIAL(this.interpreterOracle, dataInput);
            case INVOKESTATIC.OP:
                return new CUSTOMINVOKESTATIC(this.interpreterOracle, dataInput);
            case INVOKEVIRTUAL.OP:
                return new CUSTOMINVOKEVIRTUAL(this.interpreterOracle, dataInput);
            default:
                return super.readOperation(operand, dataInput, startBytePostion);
        }
    }

    /**
     * @return interpreterOracle
     */
    public final InterpreterOracleInterface getInterpreterOracle()
    {
        return this.interpreterOracle;
    }
}
