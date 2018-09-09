package nl.tudelft.simulation.dsol.interpreter.operations;

import java.io.DataInput;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import nl.tudelft.simulation.dsol.interpreter.LocalVariable;
import nl.tudelft.simulation.dsol.interpreter.OperandStack;
import nl.tudelft.simulation.dsol.interpreter.classfile.Constant;
import nl.tudelft.simulation.dsol.interpreter.classfile.ConstantClass;
import nl.tudelft.simulation.dsol.interpreter.classfile.ConstantFloat;
import nl.tudelft.simulation.dsol.interpreter.classfile.ConstantInteger;
import nl.tudelft.simulation.dsol.interpreter.classfile.ConstantString;
import nl.tudelft.simulation.language.reflection.FieldSignature;

/**
 * The LDC operation as defined in <a
 * href="http://java.sun.com/docs/books/vmspec/2nd-edition/html/Instructions2.doc8.html">
 * http://java.sun.com/docs/books/vmspec/2nd-edition/html/Instructions2.doc8.html </a>.
 * <p>
 * copyright (c) 2002-2018  <a href="https://simulation.tudelft.nl">Delft University of Technology</a>. <br>
 * BSD-style license. See <a href="https://simulation.tudelft.nl/dsol/3.0/license.html">DSOL License</a>. <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs</a>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class LDC extends VoidOperation
{
    /** OP refers to the operand code. */
    public static final int OP = 18;

    /** the index to load. */
    private final int index;

    /** the logger./ */
    private static Logger logger = LogManager.getLogger(VoidOperation.class);

    /**
     * constructs a new LDC.
     * @param dataInput the dataInput
     * @throws IOException on IOfailure
     */
    public LDC(final DataInput dataInput) throws IOException
    {
        super();
        this.index = dataInput.readUnsignedByte();
    }

    /** {@inheritDoc} */
    @Override
    public final void execute(final OperandStack stack, final Constant[] constantPool,
            final LocalVariable[] localVariables)
    {
        Constant constant = constantPool[this.index];
        if (constant instanceof ConstantInteger)
        {
            stack.push(Integer.valueOf(((ConstantInteger) constant).getValue()));
        }
        else if (constant instanceof ConstantFloat)
        {
            stack.push(Float.valueOf(((ConstantFloat) constant).getValue()));
        }
        else if (constant instanceof ConstantString)
        {
            stack.push(((ConstantString) constant).getValue());
        }
        else if (constant instanceof ConstantClass)
        {
            FieldSignature object = ((ConstantClass) constant).getValue();
            try
            {
                stack.push(object.getClassValue());
            }
            catch (ClassNotFoundException classNotFoundException)
            {
                logger.warn("execute", classNotFoundException);
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    public final int getByteLength()
    {
        return OPCODE_BYTE_LENGTH + 1;
    }

    /** {@inheritDoc} */
    @Override
    public final int getOpcode()
    {
        return LDC.OP;
    }
}
