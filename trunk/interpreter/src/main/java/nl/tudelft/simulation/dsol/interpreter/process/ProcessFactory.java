/*
 * @(#) InterpreterFactory.java $Date: 2007/01/07 05:00:13 $ Copyright (c)
 * 2002-2005 Delft University of Technology Jaffalaan 5, 2628 BX Delft, the
 * Netherlands. All rights reserved. This software is proprietary information of
 * Delft University of Technology The code is published under the Lesser General
 * Public License
 */
package nl.tudelft.simulation.dsol.interpreter.process;

import java.io.DataInput;
import java.io.IOException;
import java.lang.reflect.Method;

import nl.tudelft.simulation.dsol.interpreter.Operation;
import nl.tudelft.simulation.dsol.interpreter.operations.custom.CustomFactory;
import nl.tudelft.simulation.dsol.interpreter.operations.custom.InterpreterOracleInterface;
import nl.tudelft.simulation.dsol.interpreter.operations.reflection.INVOKESPECIAL;
import nl.tudelft.simulation.dsol.interpreter.operations.reflection.INVOKEVIRTUAL;

/**
 * A InterpreterFactory <br>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft
 * University of Technology </a>, the Netherlands. <br>
 * See for project information <a
 * href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser
 * General Public License (LGPL) </a>, no warranty.
 * 
 * @version 1.0 Jan 14, 2004 <br>
 * @author <a href="http://www.peter-jacobs.com">Peter Jacobs </a>
 */
public class ProcessFactory extends CustomFactory
{
    /** the SUSPEND METHOD */
    public static Method SUSPEND_METHOD;

    static
    {
        try
        {
            SUSPEND_METHOD = Process.class.getMethod("suspend");
        } catch (Exception exception)
        {
            exception.printStackTrace();
        }
    }

    /**
     * constructs a new InterpreterFactory
     * 
     * @param interpreterOracle the interpreterOracle to use
     */
    public ProcessFactory(final InterpreterOracleInterface interpreterOracle)
    {
        super(interpreterOracle);
    }

    /**
     * reads a sequence of bytes and returns the appropriate bytecode
     * operations.
     * 
     * @param operand the operatand (short value)
     * @param dataInput the dataInput to read from
     * @param startBytePostion the position in the current block of bytecode.
     * @return the assemnbly Operation
     * @throws IOException on IO exception
     */
    @Override
    public Operation readOperation(final int operand,
            final DataInput dataInput, final int startBytePostion)
            throws IOException
    {
        switch (operand)
        {
            case INVOKESPECIAL.OP:
                return new PROCESSINVOKESPECIAL(super.interpreterOracle,
                        dataInput);
            case INVOKEVIRTUAL.OP:
                return new PROCESSINVOKEVIRTUAL(super.interpreterOracle,
                        dataInput);
            default:
                return super
                        .readOperation(operand, dataInput, startBytePostion);
        }
    }
}