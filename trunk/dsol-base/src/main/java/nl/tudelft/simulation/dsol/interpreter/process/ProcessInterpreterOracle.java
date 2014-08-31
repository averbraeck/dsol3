package nl.tudelft.simulation.dsol.interpreter.process;

import java.lang.reflect.Method;

import nl.tudelft.simulation.dsol.interpreter.operations.custom.InterpreterOracleInterface;

/**
 * <p />
 * (c) copyright 2002-2014 <a href="http://www.simulation.tudelft.nl">Delft University of Technology</a>. <br />
 * BSD-style license. See <a href="http://www.simulation.tudelft.nl/dsol/3.0/license.html">DSOL License</a>. <br />
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs</a>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
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
