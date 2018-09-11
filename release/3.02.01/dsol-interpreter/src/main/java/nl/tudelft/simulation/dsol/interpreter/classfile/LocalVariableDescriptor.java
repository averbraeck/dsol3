package nl.tudelft.simulation.dsol.interpreter.classfile;

import java.io.DataInput;
import java.io.IOException;

import nl.tudelft.simulation.language.reflection.FieldSignature;

/**
 * A Local Variable descriptor in the bytecode.
 * <p>
 * copyright (c) 2002-2018  <a href="https://simulation.tudelft.nl">Delft University of Technology</a>. <br>
 * BSD-style license. See <a href="https://simulation.tudelft.nl/dsol/3.0/license.html">DSOL License</a>. <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs</a>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public final class LocalVariableDescriptor
{
    /** the start attribute of a localVariable. */
    private int startByte = -1;

    /** the length attribute of a localVariable. */
    private int length = -1;

    /** the index attribute of a localVariable. */
    private int index = -1;

    /** the name of the variable. */
    private String name;

    /** the descriptor of the variable. */
    private FieldSignature fieldSignature;

    /**
     * constructs a new LocalVariableDescriptor.
     * @param dataInput the dataInput to read
     * @param constantPool the constantPool for this variable
     * @throws IOException on failure
     */
    public LocalVariableDescriptor(final DataInput dataInput, final Constant[] constantPool) throws IOException
    {
        this.startByte = dataInput.readUnsignedShort();
        this.length = dataInput.readUnsignedShort();
        this.name = ((ConstantUTF8) constantPool[dataInput.readUnsignedShort()]).getValue();
        this.fieldSignature =
                new FieldSignature(((ConstantUTF8) constantPool[dataInput.readUnsignedShort()]).getValue());
        this.index = dataInput.readUnsignedShort();
    }

    /**
     * @return Returns the fieldSignature.
     */
    public FieldSignature getFieldSignature()
    {
        return this.fieldSignature;
    }

    /**
     * @return Returns the length.
     */
    public int getIndex()
    {
        return this.index;
    }

    /**
     * @return Returns the length.
     */
    public int getLength()
    {
        return this.length;
    }

    /**
     * @return Returns the name.
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * @return Returns the startByte.
     */
    public int getStartByte()
    {
        return this.startByte;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return "[" + this.name + ";" + this.fieldSignature + "]";
    }
}
