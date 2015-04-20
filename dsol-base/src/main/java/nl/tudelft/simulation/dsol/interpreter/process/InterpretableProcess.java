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
 * The Process class is an abstract Process which can be suspended and resumed.
 * <p>
 * (c) copyright 2002-2014 <a href="http://www.simulation.tudelft.nl">Delft University of Technology</a>. <br>
 * BSD-style license. See <a href="http://www.simulation.tudelft.nl/dsol/3.0/license.html">DSOL License</a>. <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs</a>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public abstract class InterpretableProcess extends EventProducer
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
    private short state = InterpretableProcess.INITIAL;

    /** the processStack of this process. */
    private final Stack<Frame> frameStack = new Stack<Frame>();

    /**
     * constructs a new Process.
     */
    public InterpretableProcess()
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
     * resumes this process.
     */
    public final void resumeProcess()
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
            this.setState(InterpretableProcess.EXECUTING);
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
     * cancels this process entirely. After the process.cancelProcess() is invoked a process can no longer be resumed.
     */
    public final void cancelProcess()
    {
        boolean executing = this.state == EXECUTING;
        if (executing)
        {
            this.suspendProcess();
        }
        this.state = InterpretableProcess.DEAD;
        if (executing)
        {
            this.resumeProcess();
        }
    }

    /**
     * suspends a process.
     */
    public final void suspendProcess()
    {
        throw new IllegalStateException("suspend should be interpreted."
                + " One may not invoke this method directly. If this exception occurs, "
                + "make sure that the method that invoked it, was interpreted.");
    }

    /**
     * sets the state of the process.
     * @param state the new state
     */
    protected final void setState(final short state)
    {
        // Let's check for a reliable order
        if (this.state == InterpretableProcess.SUSPENDED && state == InterpretableProcess.SUSPENDED)
        {
            throw new IllegalStateException("Cannot suspend a suspended process");
        }
        this.state = state;
        super.fireEvent(STATE_CHANGE_EVENT, state);
    }

    /**
     * Returns the state of a process.
     * @return the state
     */
    protected final short getState()
    {
        return this.state;
    }
}
