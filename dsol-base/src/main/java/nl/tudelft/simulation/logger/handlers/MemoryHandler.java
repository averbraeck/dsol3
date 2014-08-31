package nl.tudelft.simulation.logger.handlers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 * A MemoryHandler which makes it possible to push to a dynamic target. <br>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl"> www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:39:18 $
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>, <a
 *         href="mailto:nlang@fbk.eur.nl">Niels Lang </a>
 */
public class MemoryHandler extends Handler
{
    /** the size of the memoryHandler. */
    private int size = 100;

    /** the entires of the handler. */
    private List<LogRecord> entries = Collections.synchronizedList(new ArrayList<LogRecord>(100));

    /**
     * constructs a new MemoryHandler.
     */
    public MemoryHandler()
    {
        super();
    }

    /**
     * constructs a new MemoryHandler.
     * @param size the size of the memoryHandler
     */
    public MemoryHandler(final int size)
    {
        super();
        this.size = size;
    }

    /** {@inheritDoc} */
    @Override
    public void close()
    {
        this.flush();
    }

    /** {@inheritDoc} */
    @Override
    public void flush()
    {
        this.entries.clear();
    }

    /** {@inheritDoc} */
    @Override
    public Formatter getFormatter()
    {
        return null;
    }

    /**
     * returns the size
     * @return int the number of records this handler can hold
     */
    public int getSize()
    {
        return this.size;
    }

    /** {@inheritDoc} */
    @Override
    public synchronized void publish(final LogRecord logRecord)
    {
        this.entries.add(logRecord);
        if (this.entries.size() > this.size)
        {
            this.entries.subList(0, this.entries.size() - this.size).clear();
        }
    }

    /**
     * pushes the memory to a handler
     * @param target the target
     */
    public synchronized void push(final Handler target)
    {
        for (LogRecord record : this.entries)
        {
            target.publish(record);
        }
        this.flush();
    }

    /** {@inheritDoc} */
    @Override
    public void setFormatter(final Formatter arg0)
    {
        throw new IllegalArgumentException("Cannot set a " + arg0 + " on this handler");
    }

    /**
     * sets the number of records this handler can hold
     * @param size the size
     */
    public void setSize(final int size)
    {
        this.size = size;
    }
}
