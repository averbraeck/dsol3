package nl.tudelft.simulation.dsol.interpreter.classfile;

import java.io.DataInput;
import java.io.IOException;

/**
 * A ConstantFieldref.
 * <p />
 * (c) copyright 2002-2014 <a href="http://www.simulation.tudelft.nl">Delft University of Technology</a>. <br />
 * BSD-style license. See <a href="http://www.simulation.tudelft.nl/dsol/3.0/license.html">DSOL License</a>. <br />
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs</a>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public final class ConstantFieldref extends Constant
{
    /** the class index. */
    private int classIndex;

    /** the name / type index. */
    private int nameAndTypeIndex;

    /**
     * constructs a new ConstantFieldref.
     * @param constantPool the constantPool it is part of
     * @param inputStream the inputstream to read from
     * @throws IOException on failure
     */
    public ConstantFieldref(final Constant[] constantPool, final DataInput inputStream) throws IOException
    {
        this(constantPool, inputStream.readUnsignedShort(), inputStream.readUnsignedShort());
    }

    /**
     * constructs a new ConstantFieldref.
     * @param constantPool the constantPool it is part of
     * @param classIndex the classIndex
     * @param nameAndTypeIndex the NameAndTypeIndex
     */
    public ConstantFieldref(final Constant[] constantPool, final int classIndex, final int nameAndTypeIndex)
    {
        super(constantPool);
        this.classIndex = classIndex;
        this.nameAndTypeIndex = nameAndTypeIndex;
    }

    /** {@inheritDoc} */
    @Override
    public int getTag()
    {
        return 9;
    }

    /**
     * returns the classindex.
     * @return classIndex
     */
    public int getClassIndex()
    {
        return this.classIndex;
    }

    /**
     * returns the nameAndTypeIndex.
     * @return nameAndTypeIndex
     */
    public int getNameAndTypeIndex()
    {
        return this.nameAndTypeIndex;
    }

    /**
     * returns the constantClass of this constant.
     * @return ConstantClass the constantClass
     */
    public ConstantClass getConstantClass()
    {
        return ((ConstantClass) super.getConstantPool()[this.classIndex]);
    }

    /**
     * returns the nameAndType constant.
     * @return ConstantNameAndType
     */
    public ConstantNameAndType getConstantNameAndType()
    {
        return ((ConstantNameAndType) super.getConstantPool()[this.nameAndTypeIndex]);
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return "ConstantFieldref[classIndex=" + this.classIndex + " nameAndTypeIndex=" + this.nameAndTypeIndex + "]";
    }
}
