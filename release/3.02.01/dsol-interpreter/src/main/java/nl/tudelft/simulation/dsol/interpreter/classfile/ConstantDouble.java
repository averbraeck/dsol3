package nl.tudelft.simulation.dsol.interpreter.classfile;

import java.io.DataInput;
import java.io.IOException;

/**
 * A ConstantDouble.
 * <p>
 * copyright (c) 2002-2018  <a href="http://www.simulation.tudelft.nl">Delft University of Technology</a>. <br>
 * BSD-style license. See <a href="http://www.simulation.tudelft.nl/dsol/3.0/license.html">DSOL License</a>. <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs</a>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @since 1.5
 */
public final class ConstantDouble extends Constant
{
    /** the value. */
    private double bytes;

    /**
     * constructs a new ConstantDouble.
     * @param constantPool the constantPool it is part of
     * @param inputStream the inputstream to read from
     * @throws IOException on failure
     */
    public ConstantDouble(final Constant[] constantPool, final DataInput inputStream) throws IOException
    {
        this(constantPool, inputStream.readDouble());
    }

    /**
     * constructs a new ConstantDouble.
     * @param constantPool the constantPool it is part of
     * @param bytes the bytes
     */
    public ConstantDouble(final Constant[] constantPool, final double bytes)
    {
        super(constantPool);
        this.bytes = bytes;
    }

    /** {@inheritDoc} */
    @Override
    public int getTag()
    {
        return 6;
    }

    /**
     * returns the value.
     * @return double the value
     */
    public double getValue()
    {
        return this.bytes;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return "ConstantDouble[value=" + this.bytes + "]";
    }
}
