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
 * A InterpreterFactory <br>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @version 1.0 Jan 14, 2004 <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
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
