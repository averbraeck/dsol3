package nl.tudelft.simulation.dsol.interpreter.operations.reflection;

import java.io.DataInput;
import java.io.IOException;

import nl.tudelft.simulation.dsol.interpreter.Operation;
import nl.tudelft.simulation.dsol.interpreter.operations.InterpreterFactory;

/**
 * An InterpreterFactory.
 * <p>
 * copyright (c) 2002-2018  <a href="http://www.simulation.tudelft.nl">Delft University of Technology</a>. <br>
 * BSD-style license. See <a href="http://www.simulation.tudelft.nl/dsol/3.0/license.html">DSOL License</a>. <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs</a>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class ReflectionFactory extends InterpreterFactory
{
    /**
     * constructs a new InterpreterFactory.
     */
    public ReflectionFactory()
    {
        super();
    }

    /** {@inheritDoc} */
    @Override
    public final Operation readOperation(final int operand, final DataInput dataInput, final int startBytePostion)
            throws IOException
    {
        switch (operand)
        {
            case INVOKEINTERFACE.OP:
                return new INVOKEINTERFACE(dataInput);
            case INVOKESPECIAL.OP:
                return new INVOKESPECIAL(dataInput);
            case INVOKESTATIC.OP:
                return new INVOKESTATIC(dataInput);
            case INVOKEVIRTUAL.OP:
                return new INVOKEVIRTUAL(dataInput);
            default:
                return super.readOperation(operand, dataInput, startBytePostion);
        }
    }
}
