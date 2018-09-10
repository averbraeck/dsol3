package nl.tudelft.simulation.dsol.interpreter.classfile;

import java.io.DataInput;
import java.io.IOException;

/**
 * A ConstantInteger.
 * <p>
 * copyright (c) 2002-2018  <a href="https://simulation.tudelft.nl">Delft University of Technology</a>. <br>
 * BSD-style license. See <a href="https://simulation.tudelft.nl/dsol/3.0/license.html">DSOL License</a>. <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs</a>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public final class ConstantInteger extends Constant
{
    /** the value. */
    private int bytes;

    /**
     * constructs a new ConstantInteger.
     * @param constantPool the constantPool it is part of
     * @param inputStream the inputstream to read from
     * @throws IOException on failure
     */
    public ConstantInteger(final Constant[] constantPool, final DataInput inputStream) throws IOException
    {
        this(constantPool, inputStream.readInt());
    }

    /**
     * constructs a new ConstantInteger.
     * @param constantPool the constantPool it is part of
     * @param bytes the bytes
     */
    public ConstantInteger(final Constant[] constantPool, final int bytes)
    {
        super(constantPool);
        this.bytes = bytes;
    }

    /** {@inheritDoc} */
    @Override
    public int getTag()
    {
        return 3;
    }

    /**
     * returns the value.
     * @return int the value
     */
    public int getValue()
    {
        return this.bytes;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return "ConstantInteger[value=" + this.bytes + "]";
    }
}
