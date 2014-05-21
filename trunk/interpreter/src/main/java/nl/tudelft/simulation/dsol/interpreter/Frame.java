/*
 * @(#) Frame.java Jan 5, 2004 Copyright (c) 2002-2005 Delft University of
 * Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved.
 * This software is proprietary information of Delft University of Technology
 * 
 */
package nl.tudelft.simulation.dsol.interpreter;

import nl.tudelft.simulation.dsol.interpreter.classfile.Constant;
import nl.tudelft.simulation.dsol.interpreter.classfile.MethodDescriptor;

/**
 * A Frame <br>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft
 * University of Technology </a>, the Netherlands. <br>
 * See for project information <a
 * href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser
 * General Public License (LGPL) </a>, no warranty.
 * 
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:38:24 $
 * @author <a href="http://www.peter-jacobs.com">Peter Jacobs </a>
 */
public class Frame implements Cloneable
{
    /** the constantPool of this frame */
    protected Constant[] constantPool = null;

    /** the localVariables of this frame */
    protected LocalVariable[] localVariables = null;

    /** the operandStack of this frame */
    protected OperandStack operandStack = null;

    /** is this frame paused */
    private boolean paused = false;

    /**
     * the returnPosition refers the position in the operation[] to invoke on
     * return
     */
    private int returnPosition = 0;

    /** the operations in the frame */
    private Operation[] operations = null;

    /** the methodDescriptor of the frame */
    private MethodDescriptor methodDescriptor;

    /**
     * constructs a new Frame
     * 
     * @param constantPool the constantPool
     * @param localVariables the localVariables
     * @param operations the array of operations to execute
     * @param stack the stack
     * @param methodDescriptor the methodDescriptor
     */
    public Frame(final Constant[] constantPool,
            final LocalVariable[] localVariables, final Operation[] operations,
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

    /**
     * @see java.lang.Object#clone()
     */
    @Override
    public Object clone()
    {
        LocalVariable[] variables = new LocalVariable[this.localVariables.length];
        for (int i = 0; i < variables.length; i++)
        {
            variables[i] = (LocalVariable) this.localVariables[i].clone();
        }
        OperandStack newStack = (OperandStack) this.operandStack.clone();
        Frame frame = new Frame(this.constantPool, variables, this.operations,
                newStack, this.methodDescriptor);
        return frame;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        String result = "\n--- " + super.toString() + "---\n";
        result = result + "method:" + this.methodDescriptor.getMethod() + "\n";
        result = result + "invoker:" + this.localVariables[0].getValue() + "\n";
        return result;
    }
}