package nl.tudelft.simulation.dsol.interpreter.process;

import java.util.Stack;

import nl.tudelft.simulation.dsol.interpreter.Frame;
import nl.tudelft.simulation.dsol.interpreter.Interpreter;
import nl.tudelft.simulation.dsol.interpreter.InterpreterException;
import nl.tudelft.simulation.event.EventProducer;
import nl.tudelft.simulation.event.EventType;
import nl.tudelft.simulation.language.reflection.ClassUtil;
import nl.tudelft.simulation.logger.Logger;

/**
 * The Process class is an abstract Process which can be suspended and resumed. <br>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:38:24 $
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 */
public abstract class Process extends EventProducer
{
    /** */
    private static final long serialVersionUID = 20140830L;

    /** the initial state. */
    public static final short INITIAL = 0;

    /** the initial state. */
    public static final short EXECUTING = 1;

    /** the initial state. */
    public static final short SUSPENDED = 2;

    /** the initial state. */
    public static final short DEAD = 3;

    /** the EventType. */
    public static final EventType STATE_CHANGE_EVENT = new EventType("STATE_CHANGE_EVENT");

    /** the state of the process. */
    protected short state = Process.INITIAL;

    /** the processStack of this process. */
    protected Stack<Frame> frameStack = new Stack<Frame>();

    /**
     * constructs a new Process.
     */
    public Process()
    {
        super();
        try
        {
            this.frameStack.push(Interpreter.createFrame(this, ClassUtil.resolveMethod(this, "process", null), null));
        }
        catch (Exception exception)
        {
            Logger.warning(this, "<init>", exception);
        }
    }

    /**
     * resumes this process
     */
    public void resume()
    {
        if (this.frameStack.isEmpty() || this.state == DEAD)
        {
            this.frameStack.clear();
            return;
        }
        if (this.state == EXECUTING)
        {
            throw new IllegalStateException("Cannot resume a process in state==executing");
        }
        try
        {
            this.setState(Process.EXECUTING);
            this.frameStack.peek().setPaused(false);
            Interpreter.interpret(this.frameStack);
        }
        catch (InterpreterException exception)
        {
            exception.printStackTrace();
            Logger.warning(this, "<init>", exception);
        }
    }

    /**
     * cancels this process entirely. After the process.cancel() is invoked a process can no longer be resumed.
     */
    public void cancel()
    {
        boolean executing = this.state == EXECUTING;
        if (executing)
        {
            this.suspend();
        }
        this.state = Process.DEAD;
        if (executing)
        {
            this.resume();
        }
    }

    /**
     * suspends a process
     */
    public final void suspend()
    {
        throw new IllegalStateException("suspend should be interpreted."
                + " One may not invoke this method directly. If this exception occurs, "
                + "make sure that the method that invoked it, was interpreted.");
    }

    /**
     * sets the state of the process
     * @param state the new state
     */
    protected final void setState(final short state)
    {
        // Let's check for a reliable order
        if (this.state == Process.SUSPENDED && state == Process.SUSPENDED)
        {
            throw new IllegalStateException("Cannot suspend a suspended process");
        }
        this.state = state;
        super.fireEvent(STATE_CHANGE_EVENT, state);
    }

    /**
     * Returns the state of a process
     * @return the state
     */
    public short getState()
    {
        return this.state;
    }
}
