/*
 * @(#) ConstantFloat.java $Date: 2007/01/07 05:00:13 $ Copyright (c) 2002-2005
 * Delft University of Technology Jaffalaan 5, 2628 BX Delft, the Netherlands.
 * All rights reserved. This software is proprietary information of Delft
 * University of Technology The code is published under the Lesser General
 * Public License
 */
package nl.tudelft.simulation.dsol.interpreter.classfile;

import java.io.DataInput;
import java.io.IOException;

/**
 * A ConstantFloat <br>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @version $Revision: 1.1 $ $Date: 2007/01/07 05:00:13 $
 * @author <a href="http://www.peter-jacobs.com">Peter Jacobs </a>
 */
public class ConstantFloat extends Constant
{
    /** the value */
    private float bytes;

    /**
     * constructs a new ConstantFloat
     * @param constantPool the constantPool it is part of
     * @param inputStream the inputstream to read from
     * @throws IOException on failure
     */
    public ConstantFloat(final Constant[] constantPool, final DataInput inputStream) throws IOException
    {
        this(constantPool, inputStream.readInt());
    }

    /**
     * constructs a new ConstantFloat
     * @param constantPool the constantPool it is part of
     * @param bytes the bytes
     */
    public ConstantFloat(final Constant[] constantPool, final float bytes)
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
        return 4;
    }

    /**
     * returns the value
     * @return float the value
     */
    public float getValue()
    {
        return this.bytes;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return "ConstantFloat[value=" + this.bytes + "]";
    }
}