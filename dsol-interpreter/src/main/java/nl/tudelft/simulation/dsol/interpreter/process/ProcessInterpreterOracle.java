package nl.tudelft.simulation.dsol.interpreter.process;

import java.lang.reflect.Method;

import nl.tudelft.simulation.dsol.interpreter.operations.custom.InterpreterOracleInterface;

/**
 * <p>
 * copyright (c) 2002-2018 <a href="https://simulation.tudelft.nl">Delft University of Technology</a>. <br>
 * BSD-style license. See <a href="https://simulation.tudelft.nl/dsol/3.0/license.html">DSOL License</a>. <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs</a>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class ProcessInterpreterOracle implements InterpreterOracleInterface
{
    /**
     * constructs a new ProcessInterpreterOracle.
     */
    public ProcessInterpreterOracle()
    {
        super();
    }

    /** {@inheritDoc} */
    @Override
    public final boolean shouldBeInterpreted(final Method method)
    {
        if (InterpretableProcess.class.isAssignableFrom(method.getDeclaringClass()))
        {
            return true;
        }
        return false;
    }
}
