package nl.tudelft.simulation.dsol.interpreter.process;

import java.io.DataInput;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import nl.tudelft.simulation.dsol.interpreter.Frame;
import nl.tudelft.simulation.dsol.interpreter.Interpreter;
import nl.tudelft.simulation.dsol.interpreter.operations.custom.CUSTOMINVOKEVIRTUAL;
import nl.tudelft.simulation.dsol.interpreter.operations.custom.InterpreterOracleInterface;
import nl.tudelft.simulation.language.concurrent.Monitor;

/**
 * PROCESSINVOKEVIRTUAL.
 * <p>
 * copyright (c) 2002-2018  <a href="https://simulation.tudelft.nl">Delft University of Technology</a>. <br>
 * BSD-style license. See <a href="https://simulation.tudelft.nl/dsol/3.0/license.html">DSOL License</a>. <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs</a>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class PROCESSINVOKEVIRTUAL extends CUSTOMINVOKEVIRTUAL
{
    /**
     * constructs a new PROCESSINVOKEVIRTUAL.
     * @param interpreterOracle the interpreterOracle
     * @param dataInput the dataInput
     * @throws IOException on IOfailure
     */
    public PROCESSINVOKEVIRTUAL(final InterpreterOracleInterface interpreterOracle, final DataInput dataInput)
            throws IOException
    {
        super(interpreterOracle, dataInput);
    }

    /**
     * executes the operation and returns a new Frame.
     * @param frame the original frame
     * @param objectRef the object on which to invoke the method
     * @param arguments the arguments with which to invoke the method
     * @param method the method to invoke
     * @throws Exception on invocation exception
     * @return a new frame
     */
    @Override
    public Frame execute(final Frame frame, final Object objectRef, final Method method, final Object[] arguments)
            throws Exception
    {
        if (!InterpretableProcess.class.isAssignableFrom(method.getDeclaringClass()))
        {
            return super.execute(frame, objectRef, method, arguments);
        }
        if (method.getName().equals("resume"))
        {
            return super.execute(frame, objectRef, method, arguments);
        }
        // Let's check for the suspend method
        if (method.equals(ProcessFactory.suspendMethod))
        {
            // we set the state of the process to suspended
            InterpretableProcess process = (InterpretableProcess) objectRef;
            process.setState(InterpretableProcess.SUSPENDED);

            // we pause the frame
            frame.setPaused(true);
            return frame;
        }
        if (Modifier.isSynchronized(method.getModifiers()))
        {
            Monitor.lock(objectRef);
        }
        return Interpreter.createFrame(objectRef, method, arguments);
    }
}
