package nl.tudelft.simulation.dsol.interpreter.operations;

import nl.tudelft.simulation.dsol.interpreter.LocalVariable;
import nl.tudelft.simulation.dsol.interpreter.OperandStack;
import nl.tudelft.simulation.dsol.interpreter.Operation;
import nl.tudelft.simulation.dsol.interpreter.classfile.Constant;

/**
 * The JumpOperation is an abstract class for all operations which return an offset value to jump to a bytecode
 * statement.
 * <p>
 * copyright (c) 2002-2018 <a href="https://simulation.tudelft.nl">Delft University of Technology</a>. <br>
 * BSD-style license. See <a href="https://simulation.tudelft.nl/dsol/3.0/license.html">DSOL License</a>. <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs</a>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public abstract class JumpOperation extends Operation
{
    /**
     * executes the operation.
     * @param stack the stack to operate on
     * @param constantPool the constantpool
     * @param localvariables the localvariables
     * @return int the offset in bytes relative to the operand byte of this operation
     */
    public abstract int execute(final OperandStack stack, final Constant[] constantPool,
            final LocalVariable[] localvariables);
}
