package nl.tudelft.simulation.dsol.interpreter.classfile;

import java.io.DataInput;
import java.io.IOException;

/**
 * A LineNumber.
 * <p>
 * copyright (c) 2002-2018  <a href="https://simulation.tudelft.nl">Delft University of Technology</a>. <br>
 * BSD-style license. See <a href="https://simulation.tudelft.nl/dsol/3.0/license.html">DSOL License</a>. <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs</a>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public final class LineNumber
{
    /** the startByte attribute. */
    private int startByte = -1;

    /** the lineNumber attribute. */
    private int lineNumber = -1;

    /**
     * constructs a new LineNumber.
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
