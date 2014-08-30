package nl.tudelft.simulation.dsol.interpreter.classfile;

import java.io.DataInput;
import java.io.IOException;

import nl.tudelft.simulation.language.reflection.FieldSignature;

/**
 * A ConstantClass.
 * <p />
 * (c) copyright 2002-2014 <a href="http://www.simulation.tudelft.nl">Delft University of Technology</a>. <br />
 * BSD-style license. See <a href="http://www.simulation.tudelft.nl/dsol/3.0/license.html">DSOL License</a>. <br />
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs</a>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class ConstantClass extends Constant
{
    /** gets the name index */
    private int nameIndex;

    /**
     * constructs a new ConstantClass.
     * @param dataInput the inputstream to read from
     * @param constantPool the constantPool it is part of
     * @throws IOException on failure
     */
    public ConstantClass(final Constant[] constantPool, final DataInput dataInput) throws IOException
    {
        this(constantPool, dataInput.readUnsignedShort());
    }

    /**
     * constructs a new ClassConstant.
     * @param nameIndex the nameIndex
     * @param constantPool the constantPool it is part of
     */
    public ConstantClass(final Constant[] constantPool, final int nameIndex)
    {
        super(constantPool);
        this.nameIndex = nameIndex;
    }

    /** {@inheritDoc} */
    @Override
    public int getTag()
    {
        return 7;
    }

    /**
     * returns the name index
     * @return nameIndex
     */
    public int getNameIndex()
    {
        return this.nameIndex;
    }

    /**
     * returns the className of this constant
     * @return String the className
     */
    public FieldSignature getValue()
    {
        return new FieldSignature(((ConstantUTF8) super.constantPool[this.nameIndex]).getValue());
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return "ConstantClass[index=" + this.nameIndex + "]";
    }
}
