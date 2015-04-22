package nl.tudelft.simulation.dsol.interpreter.operations;

import java.io.DataInput;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import nl.tudelft.simulation.dsol.interpreter.Frame;
import nl.tudelft.simulation.dsol.interpreter.Interpreter;
import nl.tudelft.simulation.language.concurrent.Monitor;

// TODO check if correct. INVOKEDYNAMIC has been copied from INVOKESTATIC and that is 100% sure not correct.

/**
 * The INVOKEDYNAMIC operation as defined in <a
 * href="http://java.sun.com/docs/books/vmspec/2nd-edition/html/Instructions2.doc6.html">
 * http://java.sun.com/docs/books/vmspec/2nd-edition/html/Instructions2.doc6.html </a>.
 * <p>
 * (c) copyright 2002-2014 <a href="http://www.simulation.tudelft.nl">Delft University of Technology</a>. <br>
 * BSD-style license. See <a href="http://www.simulation.tudelft.nl/dsol/3.0/license.html">DSOL License</a>. <br>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class INVOKEDYNAMIC extends nl.tudelft.simulation.dsol.interpreter.operations.reflection.INVOKEDYNAMIC
{
    /**
     * constructs a new INVOKEDYNAMIC.
     * @param dataInput the dataInput
     * @throws IOException on IOfailure
     */
    public INVOKEDYNAMIC(final DataInput dataInput) throws IOException
    {
        super(dataInput);
    }

    /** {@inheritDoc} */
    @Override
    public final Frame execute(final Frame frame, final Object objectRef, final Method method, final Object[] arguments)
            throws Exception
    {
        if (Modifier.isNative(method.getModifiers()))
        {
            return super.execute(frame, objectRef, method, arguments);
        }
        if (Modifier.isSynchronized(method.getModifiers()))
        {
            Monitor.lock(objectRef);
        }
        return Interpreter.createFrame(objectRef, method, arguments);
    }
}
