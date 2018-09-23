package nl.tudelft.simulation.dsol.interpreter.operations;

import nl.tudelft.simulation.dsol.interpreter.LocalVariable;
import nl.tudelft.simulation.dsol.interpreter.OperandStack;
import nl.tudelft.simulation.dsol.interpreter.Operation;
import nl.tudelft.simulation.dsol.interpreter.classfile.Constant;

/**
 * The VoidOperation is an abstract class for all operations which do not return any value. The VoidOperation only pops
 * and pushes from the stack.
 * <p>
 * copyright (c) 2002-2018 <a href="https://simulation.tudelft.nl">Delft University of Technology</a>. <br>
 * BSD-style license. See <a href="https://simulation.tudelft.nl/dsol/3.0/license.html">DSOL License</a>. <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs</a>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public abstract class VoidOperation extends Operation
{
    /**
     * executes the operation.
     * @param stack the stack to operate on
     * @param constantPool the constantpool
     * @param localvariables the localvariables
     */
    public abstract void execute(final OperandStack stack, final Constant[] constantPool,
            final LocalVariable[] localvariables);
}
