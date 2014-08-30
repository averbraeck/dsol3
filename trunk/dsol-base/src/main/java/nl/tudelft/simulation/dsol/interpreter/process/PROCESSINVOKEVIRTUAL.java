/*
 * @(#) PROCESSINVOKEVIRTUAL.java $Date: 2010/08/10 11:38:24 $ Copyright (c)
 * 2002-2005 Delft University of Technology Jaffalaan 5, 2628 BX Delft, the
 * Netherlands. All rights reserved. This software is proprietary information of
 * Delft University of Technology The code is published under the Lesser General
 * Public License
 */
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
 * PROCESSINVOKEVIRTUAL <br>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @version $Revision: 1.2 $
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a> <br>
 *         <a href="mailto:a.verbraeck@tudelft.nl">Alexander Verbraeck </a>
 */
public class PROCESSINVOKEVIRTUAL extends CUSTOMINVOKEVIRTUAL
{
    /**
     * constructs a new PROCESSINVOKEVIRTUAL
     * @param interpreterOracle
     * @param dataInput the dataInput
     * @throws IOException on IOfailure
     */
    public PROCESSINVOKEVIRTUAL(final InterpreterOracleInterface interpreterOracle, final DataInput dataInput)
            throws IOException
    {
        super(interpreterOracle, dataInput);
    }

    /**
     * executes the operation and returns a new Frame
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
        if (!Process.class.isAssignableFrom(method.getDeclaringClass()))
        {
            return super.execute(frame, objectRef, method, arguments);
        }
        if (method.getName().equals("resume"))
        {
            return super.execute(frame, objectRef, method, arguments);
        }
        // Let's check for the suspend method
        if (method.equals(ProcessFactory.SUSPEND_METHOD))
        {
            // we set the state of the process to suspended
            Process process = (Process) objectRef;
            process.setState(Process.SUSPENDED);

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