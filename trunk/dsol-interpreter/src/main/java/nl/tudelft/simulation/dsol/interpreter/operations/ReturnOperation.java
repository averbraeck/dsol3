package nl.tudelft.simulation.dsol.interpreter.operations;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import nl.tudelft.simulation.dsol.interpreter.Frame;
import nl.tudelft.simulation.dsol.interpreter.Operation;

/**
 * The ReturnOperation is an abstract class for all operations which do return any value.
 * <p>
 * copyright (c) 2002-2018 <a href="https://simulation.tudelft.nl">Delft University of Technology</a>. <br>
 * BSD-style license. See <a href="https://simulation.tudelft.nl/dsol/3.0/license.html">DSOL License</a>. <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs</a>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public abstract class ReturnOperation extends Operation
{
    /**
     * executes the operation.
     * @param frame the current frame
     * @return Object the result
     */
    public abstract Object execute(final Frame frame);

    /**
     * is the accessibleObject synchronized?
     * @param object the method or constructor
     * @return isSynchronized?
     */
    public static boolean isSynchronized(final AccessibleObject object)
    {
        if (object instanceof Method)
        {
            return Modifier.isSynchronized(((Method) object).getModifiers());
        }
        return Modifier.isSynchronized(((Constructor<?>) object).getModifiers());
    }

    /**
     * is the accessibleObject static?
     * @param object the method or constructor
     * @return isStatic?
     */
    public static boolean isStatic(final AccessibleObject object)
    {
        if (object instanceof Method)
        {
            return Modifier.isStatic(((Method) object).getModifiers());
        }
        return Modifier.isStatic(((Constructor<?>) object).getModifiers());
    }
}
