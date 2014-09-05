package nl.tudelft.simulation.dsol.interpreter;

import java.lang.reflect.Method;

import nl.tudelft.simulation.dsol.interpreter.operations.custom.InterpreterOracleInterface;

/**
 * 
 * <p />
 * (c) copyright 2002-2014 <a href="http://www.simulation.tudelft.nl">Delft University of Technology</a>. <br />
 * BSD-style license. See <a href="http://www.simulation.tudelft.nl/dsol/3.0/license.html">DSOL License</a>. <br />
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs</a>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @version Aug 31, 2014
 */
public class InterpreterTestOracle implements InterpreterOracleInterface
{
    /** {@inheritDoc} */
    @Override
    public boolean shouldBeInterpreted(final Method method)
    {
        // interpret all method calls...
        return true;
    }

}
