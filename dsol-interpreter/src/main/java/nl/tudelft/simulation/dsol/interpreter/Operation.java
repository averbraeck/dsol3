package nl.tudelft.simulation.dsol.interpreter;

import nl.tudelft.simulation.dsol.interpreter.classfile.MethodDescriptor;

/**
 * Represents a Java virtual machine instruction. An operation is id-ed with a short opcode and has a predefined
 * bytelength.
 * <p>
 * copyright (c) 2002-2018  <a href="https://simulation.tudelft.nl">Delft University of Technology</a>. <br>
 * BSD-style license. See <a href="https://simulation.tudelft.nl/dsol/3.0/license.html">DSOL License</a>. <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs</a>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @since 1.5
 */
public abstract class Operation
{
    /** OPCODE_BYTE_LENGTH. */
    public static final int OPCODE_BYTE_LENGTH = 1;

    /** RESERVED OPCODE. */
    public static final int BREAKPOINT = 202;

    /** RESERVED OPCODE. */
    public static final int IMPDEP1 = 254;

    /** RESERVED OPCODE. */
    public static final int IMPDEP2 = 255;

    /**
     * @return Returns the opcode of the operation
     */
    public abstract int getOpcode();

    /**
     * @return Returs the byteLength
     */
    public abstract int getByteLength();

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return this.getClass().getName().substring(this.getClass().getName().lastIndexOf(".") + 1);
    }

    /**
     * represents a set of operations as string.
     * @param methodDescriptor the methodDescriptor
     * @param operations the operations to represent
     * @return The resulting string.
     */
    public static String toString(final MethodDescriptor methodDescriptor, final Operation[] operations)
    {
        String result = "";
        for (int i = 0; i < operations.length; i++)
        {
            result =
                    result + i + ": " + " (" + methodDescriptor.getBytePosition(i) + ")" + operations[i].toString()
                            + "\n";
        }
        return result;
    }
}
