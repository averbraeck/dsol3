/*
 * @(#) ConstantString.java $Date: 2007/01/07 05:00:12 $ Copyright (c) 2002-2005
 * Delft University of Technology Jaffalaan 5, 2628 BX Delft, the Netherlands.
 * All rights reserved. This software is proprietary information of Delft
 * University of Technology The code is published under the Lesser General
 * Public License
 */
package nl.tudelft.simulation.dsol.interpreter.classfile;

import java.io.DataInput;
import java.io.IOException;

/**
 * A ConstantString <br>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft
 * University of Technology </a>, the Netherlands. <br>
 * See for project information <a
 * href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser
 * General Public License (LGPL) </a>, no warranty.
 * 
 * @version $Revision: 1.1 $ $Date: 2007/01/07 05:00:12 $
 * @author <a href="http://www.peter-jacobs.com">Peter Jacobs </a>
 */
public class ConstantString extends Constant
{
    /** gets the name index */
    private int stringIndex;

    /**
     * constructs a new ConstantString
     * 
     * @param constantPool the constantPool it is part of
     * @param inputStream the inputstream to read from
     * @throws IOException on failure
     */
    public ConstantString(final Constant[] constantPool,
            final DataInput inputStream) throws IOException
    {
        this(constantPool, inputStream.readUnsignedShort());
    }

    /**
     * constructs a new ConstantString
     * 
     * @param constantPool the constantPool it is part of
     * @param stringIndex the stringIndex
     */
    public ConstantString(final Constant[] constantPool, final int stringIndex)
    {
        super(constantPool);
        this.stringIndex = stringIndex;
    }

    /**
     * @see nl.tudelft.simulation.dsol.interpreter.classfile.Constant#getTag()
     */
    @Override
    public int getTag()
    {
        return 8;
    }

    /**
     * returns the name index
     * 
     * @return stringIndex
     */
    public int getStringIndex()
    {
        return this.stringIndex;
    }

    /**
     * returns the className of this constant
     * 
     * @return String the className
     */
    public String getValue()
    {
        return ((ConstantUTF8) super.constantPool[this.stringIndex]).getValue();
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return "ConstantString[index=" + this.stringIndex + "]";
    }
}