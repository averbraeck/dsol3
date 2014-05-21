/*
 * @(#) LineNumber.java $Date: 2007/01/07 05:00:12 $ Copyright (c) 2002-2005
 * Delft University of Technology Jaffalaan 5, 2628 BX Delft, the Netherlands.
 * All rights reserved. This software is proprietary information of Delft
 * University of Technology The code is published under the Lesser General
 * Public License
 */
package nl.tudelft.simulation.dsol.interpreter.classfile;

import java.io.DataInput;
import java.io.IOException;

/**
 * A LineNumber <br>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @version $Revision: 1.1 $ $Date: 2007/01/07 05:00:12 $
 * @author <a href="http://www.peter-jacobs.com">Peter Jacobs </a>
 */
public class LineNumber
{
    /** the startByte attribute */
    private int startByte = -1;

    /** the lineNumber attribute */
    private int lineNumber = -1;

    /**
     * constructs a new LineNumber
     * @param dataInput dataInput to use
     * @throws IOException on failure
     */
    public LineNumber(final DataInput dataInput) throws IOException
    {
        super();
        this.startByte = dataInput.readUnsignedShort();
        this.lineNumber = dataInput.readUnsignedShort();
    }

    /**
     * @return Returns the lineNumber.
     */
    public int getLineNumber()
    {
        return this.lineNumber;
    }

    /**
     * @return Returns the startByte.
     */
    public int getStartByte()
    {
        return this.startByte;
    }
}