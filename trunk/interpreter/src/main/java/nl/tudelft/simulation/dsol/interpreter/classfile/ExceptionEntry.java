/*
 * @(#) ExceptionEntry.java $Date: 2007/01/07 05:00:12 $ Copyright (c) 2002-2005
 * Delft University of Technology Jaffalaan 5, 2628 BX Delft, the Netherlands.
 * All rights reserved. This software is proprietary information of Delft
 * University of Technology The code is published under the Lesser General
 * Public License
 */
package nl.tudelft.simulation.dsol.interpreter.classfile;

import java.io.DataInput;
import java.io.IOException;

/**
 * A ExceptionEntry <br>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @version $Revision: 1.1 $
 * @author <a href="http://www.peter-jacobs.com">Peter Jacobs </a>
 */
public class ExceptionEntry
{
    /** the start byte of the entry */
    private int startByte;

    /** the end byte of the entry */
    private int endByte;

    /** the handler number */
    private int handler;

    /** the catchType of this handler */
    private Class catchType = Exception.class;

    /**
     * constructs a new ExceptionEntry
     * @param dataInput the input to read
     * @param constantPool the constantPool of this entry
     * @throws IOException on IOFailure
     */
    public ExceptionEntry(final DataInput dataInput, final Constant[] constantPool) throws IOException
    {
        super();
        this.startByte = dataInput.readUnsignedShort();
        this.endByte = dataInput.readUnsignedShort();
        this.handler = dataInput.readUnsignedShort();
        int catchTypeIndex = dataInput.readUnsignedShort();

        if (catchTypeIndex > 0)
        {
            try
            {
                this.catchType = ((ConstantClass) constantPool[catchTypeIndex]).getValue().getClassValue();
            }
            catch (Exception exception)
            {
                throw new IOException("could not resolve catchType in ExceptionEntry");
            }
        }
    }

    /**
     * @return Returns the catchType.
     */
    public Class<?> getCatchType()
    {
        return this.catchType;
    }

    /**
     * @return Returns the endByte.
     */
    public int getEndByte()
    {
        return this.endByte;
    }

    /**
     * @return Returns the handler.
     */
    public int getHandler()
    {
        return this.handler;
    }

    /**
     * @return Returns the startByte.
     */
    public int getStartByte()
    {
        return this.startByte;
    }

    /**
     * Resolves the exceptionEntry for this particular exceptionType
     * @param entries the entries to choose from
     * @param exceptionType the exception type
     * @param bytePosition the position where the exception starts
     * @return the most specific exceptionType. null is no exceptionType is found
     */
    public static ExceptionEntry resolveExceptionEntry(final ExceptionEntry[] entries, Class exceptionType,
            final int bytePosition)
    {
        if (entries == null || exceptionType == null)
        {
            return null;
        }
        for (int i = 0; i < entries.length; i++)
        {
            if (entries[i].getCatchType().isAssignableFrom(exceptionType) && bytePosition >= entries[i].getStartByte()
                    && bytePosition <= entries[i].getEndByte())
            {
                return entries[i];
            }
        }
        return null;
    }
}