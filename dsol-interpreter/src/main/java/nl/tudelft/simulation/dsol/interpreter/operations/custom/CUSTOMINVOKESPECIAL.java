package nl.tudelft.simulation.dsol.interpreter.operations.custom;

import java.io.DataInput;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import nl.tudelft.simulation.dsol.interpreter.Frame;
import nl.tudelft.simulation.dsol.interpreter.Interpreter;
import nl.tudelft.simulation.language.concurrent.Monitor;

/**
 * The CUSTOMINVOKESPECIAL operation as defined in <a
 * href="http://java.sun.com/docs/books/vmspec/2nd-edition/html/Instructions2.doc6.html">
 * http://java.sun.com/docs/books/vmspec/2nd-edition/html/Instructions2.doc6.html </a>.
 * <p>
 * copyright (c) 2002-2018  <a href="http://www.simulation.tudelft.nl">Delft University of Technology</a>. <br>
 * BSD-style license. See <a href="http://www.simulation.tudelft.nl/dsol/3.0/license.html">DSOL License</a>. <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs</a>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class CUSTOMINVOKESPECIAL extends nl.tudelft.simulation.dsol.interpreter.operations.reflection.INVOKESPECIAL
{
    /** the interpreterOracle to use. */
    private final InterpreterOracleInterface interpreterOracle;

    /**
     * constructs a new CUSTOMINVOKESPECIAL.
     * @param interpreterOracle the oracle to use
     * @param dataInput the dataInput
     * @throws IOException on IOfailure
     */
    public CUSTOMINVOKESPECIAL(final InterpreterOracleInterface interpreterOracle, final DataInput dataInput)
            throws IOException
    {
        super(dataInput);
        this.interpreterOracle = interpreterOracle;
    }

    /** {@inheritDoc} */
    @Override
    public Frame execute(final Frame frame, final Object objectRef, final Method method, final Object[] arguments)
            throws Exception
    {
        if (!this.interpreterOracle.shouldBeInterpreted(method) || Modifier.isNative(method.getModifiers()))
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
