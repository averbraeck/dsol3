/*
 * @(#) InterpreterOracleInterface.java Feb 1, 2005 Copyright (c) 2005 Delft
 * University of Technology Jaffalaan 5, 2628 BX Delft, the Netherlands All
 * rights reserved. This software is proprietary information of Delft University
 * of Technology The code is published under the General Public License
 */
package nl.tudelft.simulation.dsol.interpreter.operations.custom;

import java.lang.reflect.Method;

/**
 * The InterpreterOracleInterface specifies an interface for selecting which methods must be interpreted.
 * <p>
 * (c) copyright 2005 <a href="http://www.simulation.tudelft.nl/dsol/">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl/dsol/"> www.simulation.tudelft.nl/dsol </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/gpl.html">General Public License (GPL) </a>, no warranty <br>
 * @author <a href="http://www.peter-jacobs.com/index.htm"> Peter Jacobs </a>
 * @version $Revision: 1.1 $ $Date: 2007/01/07 05:00:13 $
 * @since 1.5
 */
public interface InterpreterOracleInterface
{
    /**
     * whether to interpret methods defined in instances of myClass
     * @param method the method to inspect
     * @return whether to interpret methods defined in instances of myClass
     */
    public boolean shouldBeInterpreted(final Method method);
}