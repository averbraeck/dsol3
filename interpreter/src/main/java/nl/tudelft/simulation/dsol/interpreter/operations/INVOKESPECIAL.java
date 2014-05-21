/*
 * @(#) INVOKESPECIAL.java $Date: 2010/08/10 11:38:21 $ Copyright (c) 2002-2005
 * Delft University of Technology Jaffalaan 5, 2628 BX Delft, the Netherlands.
 * All rights reserved. This software is proprietary information of Delft
 * University of Technology The code is published under the Lesser General
 * Public License
 */
package nl.tudelft.simulation.dsol.interpreter.operations;

import java.io.DataInput;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import nl.tudelft.simulation.dsol.interpreter.Frame;
import nl.tudelft.simulation.dsol.interpreter.Interpreter;
import nl.tudelft.simulation.language.concurrent.Monitor;

/**
 * The INVOKESPECIAL operation as defined in <a
 * href="http://java.sun.com/docs/books/vmspec/2nd-edition/html/Instructions2.doc6.html">
 * http://java.sun.com/docs/books/vmspec/2nd-edition/html/Instructions2.doc6.html
 * </a>.
 * <p>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft
 * University of Technology </a>, the Netherlands. <br>
 * See for project information <a
 * href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser
 * General Public License (LGPL) </a>, no warranty.
 * 
 * @author <a href="http://www.peter-jacobs.com/index.htm">Peter Jacobs </a><a
 *         href="mailto:a.verbraeck@tudelft.nl">Alexander
 *         Verbraeck </a>
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:38:21 $
 * @since 1.5
 */
public class INVOKESPECIAL
        extends
        nl.tudelft.simulation.dsol.interpreter.operations.reflection.INVOKESPECIAL
{
    /**
     * constructs a new INVOKESPECIAL
     * 
     * @param dataInput the dataInput
     * @throws IOException on IOfailure
     */
    public INVOKESPECIAL(final DataInput dataInput) throws IOException
    {
        super(dataInput);
    }

    /**
     * @see nl.tudelft.simulation.dsol.interpreter.operations.reflection.INVOKESPECIAL
     *      #execute(nl.tudelft.simulation.dsol.interpreter.Frame,
     *      java.lang.Object, java.lang.reflect.Method, java.lang.Object[])
     */
    @Override
    public Frame execute(final Frame frame, final Object objectRef,
            final Method method, final Object[] arguments) throws Exception
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