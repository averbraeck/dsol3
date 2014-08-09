/*
 * @(#) Constant.java $Date: 2010/08/10 11:38:24 $ Copyright (c) 2002-2005 Delft
 * University of Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All
 * rights reserved. This software is proprietary information of Delft University
 * of Technology 
 */
package nl.tudelft.simulation.dsol.interpreter.classfile;

import java.io.DataInput;
import java.io.IOException;

import nl.tudelft.simulation.language.reflection.FieldSignature;

/**
 * A Constant <br>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:38:24 $
 * @author <a href="http://www.peter-jacobs.com">Peter Jacobs </a>
 */
public class LocalVariableDescriptor
{
    /** the start attribute of a localVariable */
    private int startByte = -1;

    /** the length attribute of a localVariable */
    private int length = -1;

    /** the index attribute of a localVariable */
    private int index = -1;

    /** the name of the variable */
    private String name;

    /** the descriptor of the variable */
    private FieldSignature fieldSignature;

    /**
     * constructs a new LocalVariableDescriptor
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

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return "[" + this.name + ";" + this.fieldSignature + "]";
    }
}