/*
 * @(#) ConstantNameAndType.java $Date: 2007/01/07 05:00:12 $ Copyright (c)
 * 2002-2005 Delft University of Technology Jaffalaan 5, 2628 BX Delft, the
 * Netherlands. All rights reserved. This software is proprietary information of
 * Delft University of Technology The code is published under the Lesser General
 * Public License
 */
package nl.tudelft.simulation.dsol.interpreter.classfile;

import java.io.DataInput;
import java.io.IOException;

/**
 * A ConstantNameAndType <br>
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
public class ConstantNameAndType extends Constant
{
    /** the name index */
    private int nameIndex;

    /** the descriptor index */
    private int descriptorIndex;

    /**
     * constructs a new ConstantNameAndType
     * 
     * @param constantPool the constantPool it is part of
     * @param inputStream the inputstream to read from
     * @throws IOException on failure
     */
    public ConstantNameAndType(final Constant[] constantPool,
            final DataInput inputStream) throws IOException
    {
        this(constantPool, inputStream.readUnsignedShort(), inputStream
                .readUnsignedShort());
    }

    /**
     * constructs a new ConstantNameAndType
     * 
     * @param constantPool the constantPool it is part of
     * @param nameIndex the nameIndex
     * @param descriptorIndex descriptorIndex
     */
    public ConstantNameAndType(final Constant[] constantPool,
            final int nameIndex, final int descriptorIndex)
    {
        super(constantPool);
        this.nameIndex = nameIndex;
        this.descriptorIndex = descriptorIndex;
    }

    /**
     * @see nl.tudelft.simulation.dsol.interpreter.classfile.Constant#getTag()
     */
    @Override
    public int getTag()
    {
        return 12;
    }

    /**
     * returns the nameindex
     * 
     * @return nameIndex
     */
    public int getNameIndex()
    {
        return this.nameIndex;
    }

    /**
     * returns the descriptorIndex
     * 
     * @return descriptorIndex
     */
    public int getDescriptorIndex()
    {
        return this.descriptorIndex;
    }

    /**
     * returns the name of this constant
     * 
     * @return String the name
     */
    public String getName()
    {
        return ((ConstantUTF8) super.constantPool[this.nameIndex]).getValue();
    }

    /**
     * returns the type of this constant
     * 
     * @return String the type
     */
    public String getDescriptor()
    {
        return ((ConstantUTF8) super.constantPool[this.descriptorIndex])
                .getValue();
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return "ConstantNameAndType[name_index=" + this.nameIndex
                + " descriptor_index=" + this.descriptorIndex + "]";
    }
}