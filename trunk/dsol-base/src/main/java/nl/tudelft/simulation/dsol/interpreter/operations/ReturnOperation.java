/*
 * @(#) Operation.java Jan 12, 2004 Copyright (c) 2002-2005 Delft University of
 * Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved.
 * This software is proprietary information of Delft University of Technology
 * 
 */
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
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="http://www.peter-jacobs.com/index.htm">Peter Jacobs </a><a
 *         href="mailto:a.verbraeck@tudelft.nl">Alexander Verbraeck </a>
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:38:21 $
 * @since 1.5
 */
public abstract class ReturnOperation extends Operation
{
    /**
     * executes the operation
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
        return Modifier.isSynchronized(((Constructor) object).getModifiers());
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
        return Modifier.isStatic(((Constructor) object).getModifiers());
    }
}