/*
 * @(#) Operation.java Jan 12, 2004 Copyright (c) 2002-2005 Delft University of
 * Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved.
 * This software is proprietary information of Delft University of Technology
 * 
 */
package nl.tudelft.simulation.dsol.interpreter.operations;

import nl.tudelft.simulation.dsol.interpreter.LocalVariable;
import nl.tudelft.simulation.dsol.interpreter.OperandStack;
import nl.tudelft.simulation.dsol.interpreter.Operation;
import nl.tudelft.simulation.dsol.interpreter.classfile.Constant;

/**
 * The VoidOperation is an abstract class for all operations which do not return any value. The VoidOperation only pops
 * and pushes from the stack.
 * <p>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="http://www.peter-jacobs.com/index.htm">Peter Jacobs </a><a
 *         href="mailto:a.verbraeck@tudelft.nl">Alexander Verbraeck </a>
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:38:22 $
 * @since 1.5
 */
public abstract class VoidOperation extends Operation
{
    /**
     * executes the operation
     * @param stack the stack to operate on
     * @param constantPool the constantpool
     * @param localvariables the localvariables
     */
    public abstract void execute(final OperandStack stack, final Constant[] constantPool,
            final LocalVariable[] localvariables);
}