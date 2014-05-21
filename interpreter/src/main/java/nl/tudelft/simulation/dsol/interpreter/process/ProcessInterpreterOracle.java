/*
 * @(#) ProcessInterpreterOracle.java Feb 1, 2005 Copyright (c) 2004 Delft
 * University of Technology Jaffalaan 5, 2628 BX Delft, the Netherlands All
 * rights reserved. This software is proprietary information of Delft University
 * of Technology The code is published under the General Public License
 */
package nl.tudelft.simulation.dsol.interpreter.process;

import java.lang.reflect.Method;

import nl.tudelft.simulation.dsol.interpreter.operations.custom.InterpreterOracleInterface;

/**
 * The specifies
 * <p>
 * (c) copyright 2004 <a href="http://www.simulation.tudelft.nl/dsol/">Delft
 * University of Technology </a>, the Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl/dsol/">
 * www.simulation.tudelft.nl/dsol </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/gpl.html">General Public
 * License (GPL) </a>, no warranty <br>
 * 
 * @author <a href="http://www.peter-jacobs.com/index.htm"> Peter Jacobs </a>
 * @version $Revision: 1.1 $ $Date: 2007/01/07 05:00:13 $
 * @since 1.5
 */
public class ProcessInterpreterOracle implements InterpreterOracleInterface
{
    /**
     * constructs a new ProcessInterpreterOracle
     */
    public ProcessInterpreterOracle()
    {
        super();
    }

    /**
     * should this method be interpreted?
     * 
     * @param method the method to introspect
     * @return whether to interprete this method
     */
    public boolean shouldBeInterpreted(final Method method)
    {
        if (Process.class.isAssignableFrom(method.getDeclaringClass()))
        {
            return true;
        }
        return false;
    }
}