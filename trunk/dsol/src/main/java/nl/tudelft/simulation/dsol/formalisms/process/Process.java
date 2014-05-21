/*
 * @(#) Process.java Jan 19, 2004 Copyright (c) 2002-2005 Delft University of
 * Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved.
 * This software is proprietary information of Delft University of Technology
 * 
 */
package nl.tudelft.simulation.dsol.formalisms.process;

import java.rmi.RemoteException;

import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.formalisms.Resource;
import nl.tudelft.simulation.dsol.formalisms.ResourceRequestorInterface;
import nl.tudelft.simulation.dsol.formalisms.eventscheduling.SimEvent;
import nl.tudelft.simulation.dsol.formalisms.eventscheduling.SimEventInterface;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;
import nl.tudelft.simulation.logger.Logger;

/**
 * A Process <br>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @version 1.0 Jan 19, 2004 <br>
 * @author <a href="http://www.peter-jacobs.com">Peter Jacobs </a>
 */
public abstract class Process extends nl.tudelft.simulation.dsol.interpreter.process.Process implements
        ResourceRequestorInterface
{
    /**
     * the simulator to schedule on
     */
    protected DEVSSimulatorInterface simulator = null;

    /**
     * the simEvent which is used to schedule the resume.
     */
    private SimEventInterface simEvent = null;

    /**
     * constructs a new Process and IMMEDIATELY STARTS ITS PROCESS METHOD
     * @param simulator the simulator to schedule on
     */
    public Process(final DEVSSimulatorInterface simulator)
    {
        this(simulator, true);
    }

    /**
     * Constructs a new <code>Process</code>
     * @param simulator the simulator to schedule on
     * @param start whether to immediately start this process
     */
    public Process(final DEVSSimulatorInterface simulator, final boolean start)
    {
        super();
        this.simulator = simulator;
        if (start)
        {
            try
            {
                double simulatorTime = this.simulator.getSimulatorTime();
                if (Double.isNaN(simulatorTime))
                {
                    simulatorTime = 0.0;
                }
                this.simEvent = new SimEvent(simulatorTime, this, this, "resume", null);
                this.simulator.scheduleEvent(this.simEvent);
            }
            catch (Exception exception)
            {
                Logger.severe(this, "<init>", exception);
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
    protected void hold(final double duration) throws SimRuntimeException, RemoteException
    {
        // First we schedule the resume operation
        this.simEvent = new SimEvent(this.simulator.getSimulatorTime() + duration, this, this, "resume", null);
        this.simulator.scheduleEvent(this.simEvent);
        // Now we suspend
        this.suspend();
    }

    /**
     * @see Process#cancel()
     */
    @Override
    public void cancel()
    {
        super.cancel();
        if (this.simEvent != null)
        {
            try
            {
                this.simulator.cancelEvent(this.simEvent);
            }
            catch (Exception exception)
            {
                Logger.warning(this, "cancel", exception);
            }
        }
    }

    /**
     * @see Process#resume()
     */
    @Override
    public void resume()
    {
        this.simEvent = null;
        super.resume();
    }

    /**
     * @see nl.tudelft.simulation.dsol.formalisms.ResourceRequestorInterface#receiveRequestedResource(double,
     *      nl.tudelft.simulation.dsol.formalisms.Resource)
     */
    public void receiveRequestedResource(final double requestedCapacity, final Resource resource)
    {
        this.resume();
    }
}