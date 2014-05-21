/*
 * @(#) OperandStack.java Jan 5, 2004 Copyright (c) 2002-2005 Delft University
 * of Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. This software is proprietary information of Delft University of
 * Technology 
 */
package nl.tudelft.simulation.dsol.interpreter;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Each frame (???3.6) contains a last-in-first-out (LIFO) stack known as its
 * operand stack. The maximum depth of the operand stack of a frame is
 * determined at compile time and is supplied along with the code for the method
 * associated with the frame (???4.7.3). Where it is clear by context, we will
 * sometimes refer to the operand stack of the current frame as simply the
 * operand stack.
 * <p>
 * The operand stack is empty when the frame that contains it is created. The
 * Java virtual machine supplies instructions to load constants or values from
 * local variables or fields onto the operand stack. Other Java virtual machine
 * instructions take operands from the operand stack, operate on them, and push
 * the result back onto the operand stack. The operand stack is also used to
 * prepare parameters to be passed to methods and to receive method results.
 * <p>
 * For example, the <link>IADD </link> instruction adds two int values together.
 * It requires that the int values to be added be the top two values of the
 * operand stack, pushed there by previous instructions. Both of the int values
 * are popped from the operand stack. They are added, and their sum is pushed
 * back onto the operand stack. Subcomputations may be nested on the operand
 * stack, resulting in values that can be used by the encompassing computation.
 * <p>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft
 * University of Technology </a>, the Netherlands. <br>
 * See for project information <a
 * href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser
 * General Public License (LGPL) </a>, no warranty.
 * 
 * @author <a href="http://www.peter-jacobs.com">Peter Jacobs </a>
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:38:24 $
 * @since 1.5
 */
public class OperandStack implements Cloneable
{
    /** the actual pointer */
    private int pointer = 0;

    /** the stackContent */
    private Object[] stack = null;

    /**
     * constructs a new OperandStack
     * 
     * @param initialSize the stackSize
     */
    public OperandStack(final int initialSize)
    {
        super();
        this.stack = new Object[initialSize];
    }

    /**
     * clears the operand stack
     */
    public void clear()
    {
        this.pointer = 0;
    }

    /**
     * is the OperandStack Empty
     * 
     * @return whether the operandStack is empty
     */
    public boolean isEmpty()
    {
        return this.pointer == 0;
    }

    /**
     * pops the first object from the operandstack.
     * 
     * @return the top object from the stack
     */
    public Object pop()
    {
        synchronized (this.stack)
        {
            return this.stack[--this.pointer];
        }
    }

    /**
     * peeks the first object from the stack. This is a pop without remove
     * 
     * @return Object the first object
     */
    public Object peek()
    {
        synchronized (this.stack)
        {
            return this.stack[this.pointer - 1];
        }
    }

    /**
     * peeks the depth-object from the stack.
     * 
     * @param depth the depth-object
     * @return the depth object.
     */
    public Object peek(final int depth)
    {
        synchronized (this.stack)
        {
            return this.stack[this.pointer - (depth + 1)];
        }
    }

    /**
     * pushes object on the stack
     * 
     * @param object the object to be pushed to the stack
     */
    public void push(final Object object)
    {
        synchronized (this.stack)
        {
            try
            {
                this.stack[this.pointer++] = object;
            } catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException)
            {
                ArrayList<Object> list = new ArrayList<Object>(Arrays
                        .asList(this.stack));
                list.add(null);
                this.stack = list.toArray();
                this.pointer--;
                this.push(object);
            }
        }
    }

    /**
     * replaces all instances of oldObject by newObject
     * 
     * @param oldObject the oldObject
     * @param newObject the newObject
     */
    public void replace(final Object oldObject, final Object newObject)
    {
        synchronized (this.stack)
        {
            for (int i = 0; i < this.pointer; i++)
            {
                if (this.stack[i] != null)
                {
                    if (this.stack[i].equals(oldObject))
                    {
                        this.stack[i] = newObject;
                    }
                }
            }
        }
    }

    /**
     * @see java.lang.Object#clone()
     */
    @Override
    public Object clone()
    {
        OperandStack newStack = new OperandStack(this.stack.length);
        newStack.pointer = this.pointer;
        newStack.stack = (Object[]) this.stack.clone();
        return newStack;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        String result = "";
        for (int i = 0; i < this.pointer; i++)
        {
            String value = "null";
            if (this.stack[i] != null)
            {
                if (this.stack[i] instanceof StringBuffer)
                {
                    value = StringBuffer.class.getName();
                } else
                {
                    value = this.stack[i].toString();
                }
            }
            result = result + "(" + i + ")" + value + "|";
        }
        return result;
    }
}