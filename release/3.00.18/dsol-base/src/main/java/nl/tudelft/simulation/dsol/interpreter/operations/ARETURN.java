package nl.tudelft.simulation.dsol.interpreter.operations;

import java.lang.reflect.Method;

import nl.tudelft.simulation.dsol.interpreter.Frame;
import nl.tudelft.simulation.language.concurrent.Monitor;

/**
 * The ARETURN operation as defined in <a
 * href="http://java.sun.com/docs/books/vmspec/2nd-edition/html/Instructions2.doc.html">
 * http://java.sun.com/docs/books/vmspec/2nd-edition/html/Instructions2.doc.html </a>.
 * <p>
 * (c) copyright 2002-2014 <a href="http://www.simulation.tudelft.nl">Delft University of Technology</a>. <br>
 * BSD-style license. See <a href="http://www.simulation.tudelft.nl/dsol/3.0/license.html">DSOL License</a>. <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs</a>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class ARETURN extends ReturnOperation
{
    /** OP refers to the operand code. */
    public static final int OP = 176;

    /**
     * constructs a new ARETURN.
     */
    public ARETURN()
    {
        super();
    }

    /** {@inheritDoc} */
    @Override
    public final Object execute(final Frame frame)
    {
        if (ReturnOperation.isSynchronized(frame.getMethodDescriptor().getMethod()))
        {
            Object monitor = null;
            if (ReturnOperation.isStatic(frame.getMethodDescriptor().getMethod()))
            {
                monitor = ((Method) frame.getMethodDescriptor().getMethod()).getDeclaringClass();
            }
            else
            {
                monitor = frame.getLocalVariables()[0].getValue();
            }
            Monitor.unlock(monitor);
        }
        return frame.getOperandStack().pop();
    }

    /** {@inheritDoc} */
    @Override
    public final int getByteLength()
    {
        return OPCODE_BYTE_LENGTH;
    }

    /** {@inheritDoc} */
    @Override
    public final int getOpcode()
    {
        return ARETURN.OP;
    }
}
