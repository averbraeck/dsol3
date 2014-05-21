/*
 * @(#) Constant.java $Date: 2010/08/10 11:38:24 $ Copyright (c) 2002-2005 Delft
 * University of Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All
 * rights reserved. This software is proprietary information of Delft University
 * of Technology 
 */
package nl.tudelft.simulation.dsol.interpreter.classfile;

import java.io.DataInput;
import java.io.IOException;

/**
 * A Constant <br>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:38:24 $
 * @author <a href="http://www.peter-jacobs.com">Peter Jacobs </a>
 */
public abstract class Constant
{
    /** the constantPool it is part of */
    protected Constant[] constantPool = null;

    /**
     * constructs a new Constant
     * @param constantPool the constantPool it is part of
     */
    public Constant(final Constant[] constantPool)
    {
        super();
        this.constantPool = constantPool;
    }

    /**
     * returns the tag of the constant
     * @return int the constant tag
     */
    public abstract int getTag();

    /**
     * reads a constant from the stream
     * @param dataInput the dataInput
     * @param constantPool the constantPool
     * @return Constant
     * @throws IOException on exception
     */
    public static Constant readConstant(final Constant[] constantPool, final DataInput dataInput) throws IOException
    {
        int tag = dataInput.readUnsignedByte();
        switch (tag)
        {
            case 7:
                return new ConstantClass(constantPool, dataInput);
            case 9:
                return new ConstantFieldref(constantPool, dataInput);
            case 10:
                return new ConstantMethodref(constantPool, dataInput);
            case 11:
                return new ConstantInterfaceMethodref(constantPool, dataInput);
            case 8:
                return new ConstantString(constantPool, dataInput);
            case 3:
                return new ConstantInteger(constantPool, dataInput);
            case 4:
                return new ConstantFloat(constantPool, dataInput);
            case 5:
                return new ConstantLong(constantPool, dataInput);
            case 6:
                return new ConstantDouble(constantPool, dataInput);
            case 12:
                return new ConstantNameAndType(constantPool, dataInput);
            case 1:
                return new ConstantUTF8(constantPool, dataInput);
            default:
                throw new IOException("unknow tag constant");
        }
    }

    /**
     * parses the constantPool to string
     * @param constantPool the pool
     * @return String
     */
    public static String toString(final Constant[] constantPool)
    {
        String result = "";
        for (int i = 0; i < constantPool.length; i++)
        {
            if (constantPool[i] != null)
            {
                result = result + i + ": " + constantPool[i].toString() + "\n";
            }
            else
            {
                result = result + i + ": empty \n";
            }
        }
        return result;
    }
}