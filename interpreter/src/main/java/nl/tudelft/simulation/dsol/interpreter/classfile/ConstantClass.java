/*
 * @(#) ConstantClass.java $Date: 2007/01/07 05:00:12 $ Copyright (c) 2002-2005
 * Delft University of Technology Jaffalaan 5, 2628 BX Delft, the Netherlands.
 * All rights reserved. This software is proprietary information of Delft
 * University of Technology The code is published under the Lesser General
 * Public License
 */
package nl.tudelft.simulation.dsol.interpreter.classfile;

import java.io.DataInput;
import java.io.IOException;

import nl.tudelft.simulation.language.reflection.FieldSignature;

/**
 * A ConstantClass <br>
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
public class ConstantClass extends Constant
{
    /** gets the name index */
    private int nameIndex;

    /**
     * constructs a new ConstantClass
     * 
     * @param dataInput the inputstream to read from
     * @param constantPool the constantPool it is part of
     * @throws IOException on failure
     */
    public ConstantClass(final Constant[] constantPool,
            final DataInput dataInput) throws IOException
    {
        this(constantPool, dataInput.readUnsignedShort());
    }

    /**
     * constructs a new ClassConstant
     * 
     * @param nameIndex the nameIndex
     * @param constantPool the constantPool it is part of
     */
    public ConstantClass(final Constant[] constantPool, final int nameIndex)
    {
        super(constantPool);
        this.nameIndex = nameIndex;
    }

    /**
     * @see nl.tudelft.simulation.dsol.interpreter.classfile.Constant#getTag()
     */
    @Override
    public int getTag()
    {
        return 7;
    }

    /**
     * returns the name index
     * 
     * @return nameIndex
     */
    public int getNameIndex()
    {
        return this.nameIndex;
    }

    /**
     * returns the className of this constant
     * 
     * @return String the className
     */
    public FieldSignature getValue()
    {
        return new FieldSignature(
                ((ConstantUTF8) super.constantPool[this.nameIndex]).getValue());
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return "ConstantClass[index=" + this.nameIndex + "]";
    }
}