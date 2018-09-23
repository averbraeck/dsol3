package nl.tudelft.simulation.dsol.interpreter.classfile;

import java.io.DataInput;
import java.io.IOException;

/**
 * A ConstantNameAndType.
 * <p>
 * copyright (c) 2002-2018 <a href="https://simulation.tudelft.nl">Delft University of Technology</a>. <br>
 * BSD-style license. See <a href="https://simulation.tudelft.nl/dsol/3.0/license.html">DSOL License</a>. <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs</a>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public final class ConstantNameAndType extends Constant
{
    /** the name index. */
    private int nameIndex;

    /** the descriptor index. */
    private int descriptorIndex;

    /**
     * constructs a new ConstantNameAndType.
     * @param constantPool the constantPool it is part of
     * @param inputStream the inputstream to read from
     * @throws IOException on failure
     */
    public ConstantNameAndType(final Constant[] constantPool, final DataInput inputStream) throws IOException
    {
        this(constantPool, inputStream.readUnsignedShort(), inputStream.readUnsignedShort());
    }

    /**
     * constructs a new ConstantNameAndType.
     * @param constantPool the constantPool it is part of
     * @param nameIndex the nameIndex
     * @param descriptorIndex descriptorIndex
     */
    public ConstantNameAndType(final Constant[] constantPool, final int nameIndex, final int descriptorIndex)
    {
        super(constantPool);
        this.nameIndex = nameIndex;
        this.descriptorIndex = descriptorIndex;
    }

    /** {@inheritDoc} */
    @Override
    public int getTag()
    {
        return 12;
    }

    /**
     * returns the nameindex.
     * @return nameIndex
     */
    public int getNameIndex()
    {
        return this.nameIndex;
    }

    /**
     * returns the descriptorIndex.
     * @return descriptorIndex
     */
    public int getDescriptorIndex()
    {
        return this.descriptorIndex;
    }

    /**
     * returns the name of this constant.
     * @return String the name
     */
    public String getName()
    {
        return ((ConstantUTF8) super.getConstantPool()[this.nameIndex]).getValue();
    }

    /**
     * returns the type of this constant.
     * @return String the type
     */
    public String getDescriptor()
    {
        return ((ConstantUTF8) super.getConstantPool()[this.descriptorIndex]).getValue();
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return "ConstantNameAndType[name_index=" + this.nameIndex + " descriptor_index=" + this.descriptorIndex + "]";
    }
}
