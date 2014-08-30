/*
 * @(#) Operation.java $Date: 2007/01/07 05:00:12 $ Copyright (c) 2002-2005
 * Delft University of Technology Jaffalaan 5, 2628 BX Delft, the Netherlands.
 * All rights reserved. This software is proprietary information of Delft
 * University of Technology The code is published under the Lesser General
 * Public License
 */
package nl.tudelft.simulation.dsol.interpreter;

import nl.tudelft.simulation.dsol.interpreter.classfile.MethodDescriptor;

/**
 * Represents a Java virtual machine instruction. An operation is id-ed with a short opcode and has a predefined
 * bytelength.
 * <p>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @version $Revision: 1.1 $ $Date: 2007/01/07 05:00:12 $
 * @since 1.5
 */
public abstract class Operation
{
    /** OPCODE_BYTE_LENGTH */
    public static final int OPCODE_BYTE_LENGTH = 1;

    /** RESERVED OPCODE */
    public static final int BREAKPOINT = 202;

    /** RESERVED OPCODE */
    public static final int IMPDEP1 = 254;

    /** RESERVED OPCODE */
    public static final int IMPDEP2 = 255;

    /**
     * @return Returns the opcode of the operation
     */
    public abstract int getOpcode();

    /**
     * @return Returs the byteLength
     */
    public abstract int getByteLength();

    /**
     * @see java.lang.Object#toString()
     */
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