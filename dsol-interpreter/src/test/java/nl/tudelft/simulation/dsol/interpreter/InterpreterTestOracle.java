package nl.tudelft.simulation.dsol.interpreter;

import java.lang.reflect.Method;

import nl.tudelft.simulation.dsol.interpreter.operations.custom.InterpreterOracleInterface;

/**
 * 
 * <p>
 * copyright (c) 2002-2018  <a href="https://simulation.tudelft.nl">Delft University of Technology</a>. <br>
 * BSD-style license. See <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank"> DSOL License</a>. <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs</a>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
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
