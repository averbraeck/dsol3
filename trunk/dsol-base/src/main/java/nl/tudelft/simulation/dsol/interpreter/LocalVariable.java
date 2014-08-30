/*
 * @(#) Constant.java Jan 5, 2004 Copyright (c) 2002-2005 Delft University of
 * Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved.
 * This software is proprietary information of Delft University of Technology
 * 
 */
package nl.tudelft.simulation.dsol.interpreter;

import nl.tudelft.simulation.dsol.interpreter.classfile.LocalVariableDescriptor;

/**
 * Each frame (???3.6) contains an array of variables known as its local variables. The length of the local variable
 * array of a frame is determined at compile time and supplied in the binary representation of a class or interface
 * along with the code for the method associated with the frame (???4.7.3). A single local variable can hold a value of
 * type boolean, byte, char, short, int, float, reference, or returnAddress. A pair of local variables can hold a value
 * of type long or double.
 * <p>
 * Local variables are addressed by indexing. The index of the first local variable is zero. An integer is be considered
 * to be an index into the local variable array if and only if that integer is between zero and one less than the size
 * of the local variable array.
 * <p>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:38:24 $
 * @since 1.5
 */
public class LocalVariable implements Cloneable
{
    /** the localVariableDescriptor */
    private LocalVariableDescriptor localVariableDescriptor = null;

    /** the runtime value of the localVariable */
    private Object value = null;

    /**
     * constructs a new LocalVariable
     * @param localVariableDescriptor the descriptor
     */
    public LocalVariable(final LocalVariableDescriptor localVariableDescriptor)
    {
        this.localVariableDescriptor = localVariableDescriptor;
    }

    /**
     * @return Returns the localVariableDescriptor.
     */
    public LocalVariableDescriptor getLocalVariableDescriptor()
    {
        return this.localVariableDescriptor;
    }

    /**
     * @return Returns the value.
     */
    public synchronized Object getValue()
    {
        return this.value;
    }

    /**
     * @param value The value to set.
     */
    public synchronized void setValue(final Object value)
    {
        this.value = value;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        String result = "variable";
        if (this.localVariableDescriptor != null)
        {
            result = result + " descriptor=" + this.localVariableDescriptor.toString();
        }
        if (this.value != null)
        {
            String valueString = null;
            if (this.value instanceof StringBuffer)
            {
                valueString = StringBuffer.class.getName();
            }
            else
            {
                valueString = this.value.toString();
            }
            result = result + valueString;
        }
        return result;
    }

    /**
     * creates a new array of local variables
     * @param descriptors the descriptors
     * @return LocalVariable[]
     */
    public static LocalVariable[] newInstance(final LocalVariableDescriptor[] descriptors)
    {
        LocalVariable[] result = new LocalVariable[descriptors.length];
        for (int i = 0; i < result.length; i++)
        {
            result[i] = new LocalVariable(descriptors[i]);
        }
        return result;
    }

    /**
     * replaces the value of a localVarialbe
     * @param localVariables the set to introspect
     * @param oldValue the oldValue
     * @param newValue the new value
     */
    public static void replace(final LocalVariable[] localVariables, final Object oldValue, final Object newValue)
    {
        synchronized (localVariables)
        {
            for (int i = 0; i < localVariables.length; i++)
            {
                if (oldValue.equals(localVariables[i].getValue()))
                {
                    localVariables[i].setValue(newValue);
                }
            }
        }
    }

    /**
     * parses the localVariables to string
     * @param localVariables the localVariables
     * @return String the result
     */
    public static String toString(final LocalVariable[] localVariables)
    {
        String result = "";
        for (int i = 0; i < localVariables.length; i++)
        {
            result = result + i + ": " + localVariables[i].toString() + "\n";
        }
        return result;
    }

    /**
     * @see java.lang.Object#clone()
     */
    @Override
    public Object clone()
    {
        LocalVariable result = new LocalVariable(this.localVariableDescriptor);
        result.value = this.value;
        return result;
    }

}