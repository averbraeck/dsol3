/*
 * @(#) ConstantInterfaceMethodref.java $Date: 2007/01/07 05:00:12 $ Copyright
 * (c) 2002-2005 Delft University of Technology Jaffalaan 5, 2628 BX Delft, the
 * Netherlands. All rights reserved. This software is proprietary information of
 * Delft University of Technology The code is published under the Lesser General
 * Public License
 */
package nl.tudelft.simulation.dsol.interpreter.classfile;

import java.io.DataInput;
import java.io.IOException;

/**
 * A ConstantInterfaceMethodref <br>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @version $Revision: 1.1 $ $Date: 2007/01/07 05:00:12 $
 * @author <a href="http://www.peter-jacobs.com">Peter Jacobs </a>
 */
public class ConstantInterfaceMethodref extends Constant
{
    /** the class index */
    private int classIndex;

    /** the name / type index */
    private int nameAndTypeIndex;

    /**
     * constructs a new ConstantInterfaceMethodref
     * @param constantPool the constantPool it is part of
     * @param inputStream the inputstream to read from
     * @throws IOException on failure
     */
    public ConstantInterfaceMethodref(final Constant[] constantPool, final DataInput inputStream) throws IOException
    {
        this(constantPool, inputStream.readUnsignedShort(), inputStream.readUnsignedShort());
    }

    /**
     * constructs a new ConstantInterfaceMethodref
     * @param constantPool the constantPool it is part of
     * @param classIndex the classIndex
     * @param nameAndTypeIndex the NameAndTypeIndex
     */
    public ConstantInterfaceMethodref(final Constant[] constantPool, final int classIndex, final int nameAndTypeIndex)
    {
        super(constantPool);
        this.classIndex = classIndex;
        this.nameAndTypeIndex = nameAndTypeIndex;
    }

    /**
     * @see nl.tudelft.simulation.dsol.interpreter.classfile.Constant#getTag()
     */
    @Override
    public int getTag()
    {
        return 11;
    }

    /**
     * returns the classindex
     * @return classIndex
     */
    public int getClassIndex()
    {
        return this.classIndex;
    }

    /**
     * returns the nameAndTypeIndex
     * @return nameAndTypeIndex
     */
    public int getNameAndTypeIndex()
    {
        return this.nameAndTypeIndex;
    }

    /**
     * returns the constantClass of this constant
     * @return ConstantClass the constantClass
     */
    public ConstantClass getConstantClass()
    {
        return ((ConstantClass) super.constantPool[this.classIndex]);
    }

    /**
     * returns the nameAndType constant
     * @return ConstantNameAndType
     */
    public ConstantNameAndType getConstantNameAndType()
    {
        return ((ConstantNameAndType) super.constantPool[this.nameAndTypeIndex]);
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return "ConstantInterfaceMetodred[classIndex=" + this.classIndex + " nameAndTypeIndex=" + this.nameAndTypeIndex
                + "]";
    }
}