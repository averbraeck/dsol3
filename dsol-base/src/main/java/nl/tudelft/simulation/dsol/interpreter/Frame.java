package nl.tudelft.simulation.dsol.interpreter;

import nl.tudelft.simulation.dsol.interpreter.classfile.Constant;
import nl.tudelft.simulation.dsol.interpreter.classfile.MethodDescriptor;

/**
 * A Frame <br>
 * <p />
 * (c) copyright 2002-2014 <a href="http://www.simulation.tudelft.nl">Delft University of Technology</a>. <br />
 * BSD-style license. See <a href="http://www.simulation.tudelft.nl/dsol/3.0/license.html">DSOL License</a>. <br />
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs</a>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class Frame implements Cloneable
{
    /** the constantPool of this frame. */
    protected Constant[] constantPool = null;

    /** the localVariables of this frame. */
    protected LocalVariable[] localVariables = null;

    /** the operandStack of this frame. */
    protected OperandStack operandStack = null;

    /** is this frame paused. */
    private boolean paused = false;

    /**
     * the returnPosition refers the position in the operation[] to invoke on return
     */
    private int returnPosition = 0;

    /** the operations in the frame. */
    private Operation[] operations = null;

    /** the methodDescriptor of the frame. */
    private MethodDescriptor methodDescriptor;

    /**
     * constructs a new Frame.
     * @param constantPool the constantPool
     * @param localVariables the localVariables
     * @param operations the array of operations to execute
     * @param stack the stack
     * @param methodDescriptor the methodDescriptor
     */
    public Frame(final Constant[] constantPool, final LocalVariable[] localVariables, final Operation[] operations,
            final OperandStack stack, final MethodDescriptor methodDescriptor)
    {
        super();
        this.constantPool = constantPool;
        this.localVariables = localVariables;
        this.operations = operations;
        this.operandStack = stack;
        this.methodDescriptor = methodDescriptor;
    }

    /**
     * @return Returns the constantPool.
     */
    public Constant[] getConstantPool()
    {
        return this.constantPool;
    }

    /**
     * @return Returns the localVariables.
     */
    public LocalVariable[] getLocalVariables()
    {
        return this.localVariables;
    }

    /**
     * @return Returns the returnPosition.
     */
    public int getReturnPosition()
    {
        return this.returnPosition;
    }

    /**
     * @return Returns the operations.
     */
    public Operation[] getOperations()
    {
        return this.operations;
    }

    /**
     * @param returnPosition The returnPosition to set.
     */
    public void setReturnPosition(final int returnPosition)
    {
        this.returnPosition = returnPosition;
    }

    /**
     * @return Returns the methodDescriptor.
     */
    public MethodDescriptor getMethodDescriptor()
    {
        return this.methodDescriptor;
    }

    /**
     * @return Returns the operandStack.
     */
    public OperandStack getOperandStack()
    {
        return this.operandStack;
    }

    /**
     * @return Returns whether the frame is paused.
     */
    public boolean isPaused()
    {
        return this.paused;
    }

    /**
     * @param paused The paused to set.
     */
    public void setPaused(final boolean paused)
    {
        this.paused = paused;
    }

    /** {@inheritDoc} */
    @Override
    public Object clone()
    {
        LocalVariable[] variables = new LocalVariable[this.localVariables.length];
        for (int i = 0; i < variables.length; i++)
        {
            variables[i] = (LocalVariable) this.localVariables[i].clone();
        }
        OperandStack newStack = (OperandStack) this.operandStack.clone();
        Frame frame = new Frame(this.constantPool, variables, this.operations, newStack, this.methodDescriptor);
        return frame;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        String result = "\n--- " + super.toString() + "---\n";
        result = result + "method:" + this.methodDescriptor.getMethod() + "\n";
        result = result + "invoker:" + this.localVariables[0].getValue() + "\n";
        return result;
    }
}
