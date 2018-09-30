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
 * copyright (c) 2002-2018 <a href="https://simulation.tudelft.nl">Delft University of Technology</a>. <br>
 * BSD-style license. See <a href="https://simulation.tudelft.nl/dsol/3.0/license.html">DSOL License</a>. <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs</a>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @since 1.5
 */
public class LocalVariable implements Cloneable
{
    /** the localVariableDescriptor. */
    private final LocalVariableDescriptor localVariableDescriptor;

    /** the runtime value of the localVariable. */
    private Object value = null;

    /**
     * constructs a new LocalVariable.
     * @param localVariableDescriptor the descriptor
     */
    public LocalVariable(final LocalVariableDescriptor localVariableDescriptor)
    {
        this.localVariableDescriptor = localVariableDescriptor;
    }

    /**
     * @return Returns the localVariableDescriptor.
     */
    public final LocalVariableDescriptor getLocalVariableDescriptor()
    {
        return this.localVariableDescriptor;
    }

    /**
     * @return Returns the value.
     */
    public final synchronized Object getValue()
    {
        return this.value;
    }

    /**
     * @param value The value to set.
     */
    public final synchronized void setValue(final Object value)
    {
        this.value = value;
    }

    /** {@inheritDoc} */
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
     * creates a new array of local variables.
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
     * replaces the value of a local variable.
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
     * parses the localVariables to string.
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

    /** {@inheritDoc} */
    @Override
    public Object clone()
    {
        LocalVariable result = new LocalVariable(this.localVariableDescriptor);
        result.value = this.value;
        return result;
    }

}
