package nl.tudelft.simulation.dsol.interpreter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A InterpretationThread <br>
 * <p>
 * copyright (c) 2002-2018  <a href="https://simulation.tudelft.nl">Delft University of Technology</a>. <br>
 * BSD-style license. See <a href="https://simulation.tudelft.nl/dsol/3.0/license.html">DSOL License</a>. <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs</a>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @since 1.5
 */
public final class InterpretationThread extends Thread
{
    /** the target of this interpretation. */
    private Runnable target = null;

    /** the logger./ */
    private static Logger logger = LogManager.getLogger(InterpretationThread.class);

    /**
     * constructs a new InterpretationThread.
     * @param target the target.
     */
    public InterpretationThread(final Runnable target)
    {
        super();
        this.target = target;
    }

    /**
     * constructs a new InterpretationThread.
     * @param target the target.
     * @param name the name of the thread
     */
    public InterpretationThread(final Runnable target, final String name)
    {
        super(name);
        this.target = target;
    }

    /**
     * constructs a new InterpretationThread.
     * @param group the threadGroup
     * @param target the target.
     */
    public InterpretationThread(final ThreadGroup group, final Runnable target)
    {
        super(group, target);
        this.target = target;
    }

    /**
     * constructs a new InterpretationThread.
     * @param group the threadGroup
     * @param target the target.
     * @param name the name of the thread
     */
    public InterpretationThread(final ThreadGroup group, final Runnable target, final String name)
    {
        super(group, target, name);
        this.target = target;
    }

    /** {@inheritDoc} */
    @Override
    public void run()
    {
        try
        {
            Interpreter.invoke(this.target, this.target.getClass().getDeclaredMethod("run"), null);
        }
        catch (Exception exception)
        {
            logger.warn("run", exception);
        }
    }
}
