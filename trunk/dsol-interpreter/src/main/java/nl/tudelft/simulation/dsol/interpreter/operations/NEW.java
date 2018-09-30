package nl.tudelft.simulation.dsol.interpreter.operations;

import java.io.DataInput;
import java.io.IOException;

import nl.tudelft.simulation.dsol.interpreter.InterpreterException;
import nl.tudelft.simulation.dsol.interpreter.LocalVariable;
import nl.tudelft.simulation.dsol.interpreter.OperandStack;
import nl.tudelft.simulation.dsol.interpreter.classfile.Constant;
import nl.tudelft.simulation.dsol.interpreter.classfile.ConstantClass;

/**
 * The NEW operation as defined in
 * <a href="http://java.sun.com/docs/books/vmspec/2nd-edition/html/Instructions2.doc10.html">
 * http://java.sun.com/docs/books/vmspec/2nd-edition/html/Instructions2.doc10.html </a>.
 * <p>
 * Copyright (c) 2002-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. See for project information <a href="https://simulation.tudelft.nl/" target="_blank">
 * https://simulation.tudelft.nl</a>. The DSOL project is distributed under a three-clause BSD-style license, which can
 * be found at <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs</a>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class NEW extends VoidOperation
{
    /** OP refers to the operand code. */
    public static final int OP = 187;

    /** index refers to the constantpool index. */
    private final int index;

    /**
     * constructs a new NEW.
     * @param dataInput the dataInput
     * @throws IOException on IOfailure
     */
    public NEW(final DataInput dataInput) throws IOException
    {
        super();
        this.index = dataInput.readUnsignedShort();
    }

    /** {@inheritDoc} */
    @Override
    public final void execute(final OperandStack stack, final Constant[] constantPool,
            final LocalVariable[] localVariables)
    {
        try
        {
            Class<?> instanceClass = null;
            instanceClass = ((ConstantClass) constantPool[this.index]).getValue().getClassValue();
            stack.push(new UninitializedInstance(instanceClass));
        }
        catch (Exception exception)
        {
            throw new InterpreterException(exception);
        }
    }

    /** {@inheritDoc} */
    @Override
    public final int getByteLength()
    {
        return OPCODE_BYTE_LENGTH + 2;
    }

    /** {@inheritDoc} */
    @Override
    public final int getOpcode()
    {
        return NEW.OP;
    }

    /**
     * holder class for "to-be-constructed" instances.
     */
    public static class UninitializedInstance
    {
        /** the value. */
        private Class<?> instanceClass = null;

        /**
         * constructs a new UninitializedInstance.
         * @param instanceClass the class of which an instance must be made
         */
        public UninitializedInstance(final Class<?> instanceClass)
        {
            this.instanceClass = instanceClass;
        }

        /**
         * @return return the instanceClass
         */
        public final Class<?> getInstanceClass()
        {
            return this.instanceClass;
        }
    }
}
