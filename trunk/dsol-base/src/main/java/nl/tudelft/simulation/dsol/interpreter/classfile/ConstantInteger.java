/*
 * @(#) ConstantInteger.java $Date: 2007/01/07 05:00:13 $ Copyright (c)
 * 2002-2005 Delft University of Technology Jaffalaan 5, 2628 BX Delft, the
 * Netherlands. All rights reserved. This software is proprietary information of
 * Delft University of Technology The code is published under the Lesser General
 * Public License
 */
package nl.tudelft.simulation.dsol.interpreter.classfile;

import java.io.DataInput;
import java.io.IOException;

/**
 * A ConstantInteger <br>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @version $Revision: 1.1 $ $Date: 2007/01/07 05:00:13 $
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 */
public class ConstantInteger extends Constant
{
    /** the value */
    private int bytes;

    /**
     * constructs a new ConstantInteger
     * @param constantPool the constantPool it is part of
     * @param inputStream the inputstream to read from
     * @throws IOException on failure
     */
    public ConstantInteger(final Constant[] constantPool, final DataInput inputStream) throws IOException
    {
        this(constantPool, inputStream.readInt());
    }

    /**
     * constructs a new ConstantInteger
     * @param constantPool the constantPool it is part of
     * @param bytes the bytes
     */
    public ConstantInteger(final Constant[] constantPool, final int bytes)
    {
        super(constantPool);
        this.bytes = bytes;
    }

    /**
     * @see nl.tudelft.simulation.dsol.interpreter.classfile.Constant#getTag()
     */
    @Override
    public int getTag()
    {
        return 3;
    }

    /**
     * returns the value
     * @return int the value
     */
    public int getValue()
    {
        return this.bytes;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return "ConstantInteger[value=" + this.bytes + "]";
    }
}