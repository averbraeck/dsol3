package nl.tudelft.simulation.dsol.formalisms.process;

import java.rmi.RemoteException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.formalisms.Resource;
import nl.tudelft.simulation.dsol.formalisms.ResourceRequestorInterface;
import nl.tudelft.simulation.dsol.formalisms.eventscheduling.SimEvent;
import nl.tudelft.simulation.dsol.formalisms.eventscheduling.SimEventInterface;
import nl.tudelft.simulation.dsol.interpreter.process.InterpretableProcess;
import nl.tudelft.simulation.dsol.simtime.SimTime;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;

/**
 * A Process <br>
 * (c) 2002-2018 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @version 1.0 Jan 19, 2004 <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @param <A> the absolute storage type for the simulation time, e.g. Calendar, Duration, or Double.
 * @param <R> the relative type for time storage, e.g. Long for the Calendar. For most non-calendar types, the absolute
 *            and relative types are the same.
 * @param <T> the extended type itself to be able to implement a comparator on the simulation time.
 */
public abstract class Process<A extends Comparable<A>, R extends Number & Comparable<R>, T extends SimTime<A, R, T>>
        extends InterpretableProcess implements ResourceRequestorInterface<A, R, T>
{
    /** */
    private static final long serialVersionUID = 20140805L;

    /**
     * the simulator to schedule on.
     */
    protected DEVSSimulatorInterface<A, R, T> simulator = null;

    /**
     * the simEvent which is used to schedule the resume.
     */
    private SimEventInterface<T> simEvent = null;

    /** the logger. */
    protected static Logger logger = LogManager.getLogger(Process.class);

    /**
     * constructs a new Process and IMMEDIATELY STARTS ITS PROCESS METHOD.
     * @param simulator the simulator to schedule on
     */
    public Process(final DEVSSimulatorInterface<A, R, T> simulator)
    {
        this(simulator, true);
    }

    /**
     * Constructs a new <code>Process</code>.
     * @param simulator the simulator to schedule on
     * @param start whether to immediately start this process
     */
    public Process(final DEVSSimulatorInterface<A, R, T> simulator, final boolean start)
    {
        super();
        this.simulator = simulator;
        if (start)
        {
            try
            {
                T simTime = this.simulator.getSimTime();
                this.simEvent = new SimEvent<T>(simTime, this, this, "resume", null);
                this.simulator.scheduleEvent(this.simEvent);
            }
            catch (Exception exception)
            {
                logger.error("<init>", exception);
            }
        }
    }

    /**
     * processes the process.
     * @throws RemoteException on network failure
     * @throws SimRuntimeException on simulation failures.
     */
    public abstract void process() throws RemoteException, SimRuntimeException;

    /**
     * holds the process for a duration
     * @param duration the duration
     * @throws SimRuntimeException on negative duration
     * @throws RemoteException on network failure
     */
    protected void hold(final R duration) throws SimRuntimeException, RemoteException
    {
        // First we schedule the resume operation
        this.simEvent = new SimEvent<T>(this.simulator.getSimTime().plus(duration), this, this, "resume", null);
        this.simulator.scheduleEvent(this.simEvent);
        // Now we suspend
        this.suspend();
    }

    /**
     * cancels this process entirely. After the process.cancel() is invoked a process can no longer be resumed.
     */
    public void cancel()
    {
        super.cancelProcess();
        if (this.simEvent != null)
        {
            try
            {
                this.simulator.cancelEvent(this.simEvent);
            }
            catch (Exception exception)
            {
                logger.warn("cancel", exception);
            }
        }
    }

    /**
     * resumes this process.
     */
    public void resume()
    {
        this.simEvent = null;
        super.resumeProcess();
    }

    /**
     * suspends this process.
     */
    public void suspend()
    {
        super.suspendProcess();
    }

    /** {@inheritDoc} */
    @Override
    public void receiveRequestedResource(final double requestedCapacity, final Resource resource)
    {
        this.resume();
    }
}
