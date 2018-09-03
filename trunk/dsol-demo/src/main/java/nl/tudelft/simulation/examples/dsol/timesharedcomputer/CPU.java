package nl.tudelft.simulation.examples.dsol.timesharedcomputer;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.media.j3d.Bounds;

import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.animation.Locatable;
import nl.tudelft.simulation.dsol.formalisms.flow.Station;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;
import nl.tudelft.simulation.event.EventType;
import nl.tudelft.simulation.language.d3.BoundingBox;
import nl.tudelft.simulation.language.d3.CartesianPoint;
import nl.tudelft.simulation.language.d3.DirectedPoint;

/**
 * The CPU example as published in Simulation Modeling and Analysis by A.M. Law &amp; W.D. Kelton section 1.4 and 2.4. <br>
 * Copyright (c) 2003-2018 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the Netherlands.
 * <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/gpl.html">General Public License (GPL) </a>, no warranty <br>
 * @version 2.0 21.09.2003 <br>
 * @author <a href="http://www.tbm.tudelft.nl/webstaf/peterja/index.htm">Peter Jacobs </a>
 */
public class CPU extends Station<Double, Double, SimTimeDouble> implements Locatable
{

    /** UTILIZATION_EVENT are fired on utilization. */
    public static final EventType UTILIZATION_EVENT = new EventType("UTILIZATION_EVENT");

    /** QUEUE_LENGTH_EVENT is fired on changes in the Queue length. */
    public static final EventType QUEUE_LENGTH_EVENT = new EventType("QUEUE_LENGTH_EVENT");

    /** QUANTUM is the QUANTUM of the CPU. */
    public static final double QUANTUM = 0.1;

    /** SWAP is the swap of this cpu. */
    public static final double SWAP = 0.015;

    /** IDLE defines the IDLE state. */
    public static final boolean IDLE = true;

    /** BUSY defines the BUSY state. */
    public static final boolean BUSY = false;

    /** status of the CPU. */
    private boolean status = IDLE;

    /** queue is the queue of waiting jobs. */
    private List queue = Collections.synchronizedList(new ArrayList());

    /** the location. */
    private DirectedPoint location = new DirectedPoint(new CartesianPoint(-90, 0, 0));

    /**
     * constructs a new CPU.
     * @param simulator a devs simulator
     * @throws RemoteException on network failure
     */
    public CPU(final DEVSSimulatorInterface.TimeDouble simulator) throws RemoteException
    {
        super(simulator);
        this.fireTimedEvent(UTILIZATION_EVENT, 0.0, simulator.getSimulatorTime().get());
    }

    /**
     * returns the queue.
     * @return List the queue
     */
    public List getQueue()
    {
        return this.queue;
    }

    /** {@inheritDoc} */
    @Override
    public void receiveObject(final Object object)
    {
        try
        {
            this.queue.add(object);
            this.fireTimedEvent(QUEUE_LENGTH_EVENT, this.queue.size(), this.simulator.getSimulatorTime());
            if (this.status == IDLE)
            {
                try
                {
                    this.next();
                }
                catch (SimRuntimeException exception)
                {
                    exception.printStackTrace();
                }
            }
        }
        catch (RemoteException remoteException)
        {
            remoteException.printStackTrace();
        }
    }

    /** {@inheritDoc} */
    protected void releaseObject(final Object object) throws RemoteException
    {
        this.status = IDLE;
        this.fireTimedEvent(UTILIZATION_EVENT, 0.0, this.simulator.getSimulatorTime());
        ((Job) object).getOwner().receiveObject(object);
        try
        {
            this.next();
        }
        catch (SimRuntimeException exception)
        {
            exception.printStackTrace();
        }
    }

    /**
     * services the next job.
     * @throws RemoteException on network failure
     * @throws SimRuntimeException on simulation failure
     */
    private void next() throws RemoteException, SimRuntimeException
    {
        if (this.queue.size() > 0)
        {
            this.status = BUSY;
            this.fireTimedEvent(UTILIZATION_EVENT, 1.0, this.simulator.getSimulatorTime().get());
            Job job = (Job) this.queue.remove(0);
            this.fireTimedEvent(QUEUE_LENGTH_EVENT, this.queue.size(), this.simulator.getSimulatorTime().get());
            if (job.getServiceTime() > QUANTUM)
            {
                job.setServiceTime(job.getServiceTime() - QUANTUM);
                Object[] args = {job};
                this.simulator.scheduleEventAbs(this.simulator.getSimulatorTime().get() + QUANTUM + SWAP, this, this,
                        "receiveObject", args);
                this.simulator.scheduleEventAbs(this.simulator.getSimulatorTime().get() + QUANTUM + SWAP, this, this,
                        "next", null);
            }
            else
            {
                Object[] args = {job};
                this.simulator.scheduleEventAbs(this.simulator.getSimulatorTime().get() + job.getServiceTime() + SWAP,
                        this, this, "releaseObject", args);
            }
        }
        else
        {
            this.status = IDLE;
            this.fireTimedEvent(UTILIZATION_EVENT, 0.0, this.simulator.getSimulatorTime());
        }
    }

    /** {@inheritDoc} */
    @Override
    public DirectedPoint getLocation()
    {
        return this.location;
    }

    /** {@inheritDoc} */
    @Override
    public Bounds getBounds()
    {
        return new BoundingBox(0, 0, 0);
    }
}
