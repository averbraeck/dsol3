package nl.tudelft.simulation.dsol.interpreter.operations.custom;

import java.lang.reflect.Method;

/**
 * The InterpreterOracleInterface specifies an interface for selecting which methods must be interpreted.
 * <p>
 * copyright (c) 2002-2018 <a href="https://simulation.tudelft.nl">Delft University of Technology</a>. <br>
 * BSD-style license. See <a href="https://simulation.tudelft.nl/dsol/3.0/license.html">DSOL License</a>. <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs</a>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public interface InterpreterOracleInterface
{
    /**
     * whether to interpret methods defined in instances of myClass.
     * @param method the method to inspect
     * @return whether to interpret methods defined in instances of myClass
     */
    boolean shouldBeInterpreted(final Method method);
}
