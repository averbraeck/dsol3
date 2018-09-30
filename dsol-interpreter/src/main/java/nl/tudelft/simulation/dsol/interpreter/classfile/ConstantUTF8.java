package nl.tudelft.simulation.dsol.interpreter.classfile;

import java.io.DataInput;
import java.io.IOException;

/**
 * A ConstantUTF8.
 * <p>
 * copyright (c) 2002-2018 <a href="https://simulation.tudelft.nl">Delft University of Technology</a>. <br>
 * BSD-style license. See <a href="https://simulation.tudelft.nl/dsol/3.0/license.html">DSOL License</a>. <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs</a>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public final class ConstantUTF8 extends Constant
{
    /** gets the name index. */
    private String value;

    /**
     * constructs a new ConstantUTF8.
     * @param constantPool the constantPool it is part of
     * @param inputStream the inputstream to read from
     * @throws IOException on failure
     */
    public ConstantUTF8(final Constant[] constantPool, final DataInput inputStream) throws IOException
    {
        this(constantPool, inputStream.readUTF());
    }

    /**
     * constructs a new ConstantUTF8.
     * @param constantPool the constantPool it is part of
     * @param value the content
     */
    public ConstantUTF8(final Constant[] constantPool, final String value)
    {
        super(constantPool);
        this.value = value;
    }

    /** {@inheritDoc} */
    @Override
    public int getTag()
    {
        return 1;
    }

    /**
     * returns the name index.
     * @return nameIndex
     */
    public String getValue()
    {
        return this.value;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return "ConstantUTF8[" + this.value + "]";
    }
}
