package nl.tudelft.simulation.dsol.interpreter.classfile;

import java.io.DataInput;
import java.io.IOException;

/**
 * A ConstantString.
 * <p>
 * copyright (c) 2002-2018  <a href="http://www.simulation.tudelft.nl">Delft University of Technology</a>. <br>
 * BSD-style license. See <a href="http://www.simulation.tudelft.nl/dsol/3.0/license.html">DSOL License</a>. <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs</a>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public final class ConstantString extends Constant
{
    /** gets the name index. */
    private int stringIndex;

    /**
     * constructs a new ConstantString.
     * @param constantPool the constantPool it is part of
     * @param inputStream the inputstream to read from
     * @throws IOException on failure
     */
    public ConstantString(final Constant[] constantPool, final DataInput inputStream) throws IOException
    {
        this(constantPool, inputStream.readUnsignedShort());
    }

    /**
     * constructs a new ConstantString.
     * @param constantPool the constantPool it is part of
     * @param stringIndex the stringIndex
     */
    public ConstantString(final Constant[] constantPool, final int stringIndex)
    {
        super(constantPool);
        this.stringIndex = stringIndex;
    }

    /** {@inheritDoc} */
    @Override
    public int getTag()
    {
        return 8;
    }

    /**
     * returns the name index.
     * @return stringIndex
     */
    public int getStringIndex()
    {
        return this.stringIndex;
    }

    /**
     * returns the className of this constant.
     * @return String the className
     */
    public String getValue()
    {
        return ((ConstantUTF8) super.getConstantPool()[this.stringIndex]).getValue();
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return "ConstantString[index=" + this.stringIndex + "]";
    }
}
