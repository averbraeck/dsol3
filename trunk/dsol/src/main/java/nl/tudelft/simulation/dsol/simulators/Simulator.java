/*
 * @(#)Simulator.java Aug 18, 2003 Copyright (c) 2002-2005 Delft University of
 * Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved.
 * This software is proprietary information of Delft University of Technology
 * 
 */
package nl.tudelft.simulation.dsol.simulators;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.rmi.RemoteException;

import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.experiment.Replication;
import nl.tudelft.simulation.dsol.experiment.ReplicationMode;
import nl.tudelft.simulation.dsol.simtime.SimTime;
import nl.tudelft.simulation.dsol.simtime.SimTimeEventProducer;
import nl.tudelft.simulation.event.Event;
import nl.tudelft.simulation.jstats.statistics.StatisticsObject;
import nl.tudelft.simulation.language.concurrent.WorkerThread;
import nl.tudelft.simulation.logger.Logger;

/**
 * The Simulator class is an abstract implementation of the SimulatorInterface.
 * <p>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl"> www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="http://www.peter-jacobs.com">Peter Jacobs </a>
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:36:44 $
 * @param <A> the absolute storage type for the simulation time, e.g. Calendar, UnitTimeDouble, or Double.
 * @param <R> the relative type for time storage, e.g. Long for the Calendar. For most non-calendar types, the absolute
 *            and relative types are the same.
 * @param <T> the extended type itself to be able to implement a comparator on the simulation time.
 * @since 1.5
 */
public abstract class Simulator<A extends Comparable<A>, R extends Number & Comparable<R>, T extends SimTime<A, R, T>> extends
        SimTimeEventProducer implements SimulatorInterface<A, R, T>, Runnable
{
    /** */
    private static final long serialVersionUID = 1L;

    /** simulatorTime represents the simulationTime */
    protected T simulatorTime;

    /** running represents the binary state of the simulator */
    protected transient boolean running = false;

    /** replication represents the currently active replication */
    protected Replication<A, R, T> replication = null;

    /** a worker */
    protected transient WorkerThread worker = null;

    /** the simulatorSemaphore */
    protected transient Object semaphore = new Object();

    /**
     * constructs a new Simulator
     */
    public Simulator()
    {
        super();
        this.worker = new WorkerThread(this.getClass().getName(), this);
    }

    /**
     * @see nl.tudelft.simulation.dsol.simulators.SimulatorInterface #getSimulatorTime()
     */
    public T getSimulatorTime()
    {
        return this.simulatorTime;
    }

    /**
     * @see nl.tudelft.simulation.dsol.simulators.SimulatorInterface #getReplication()
     */
    public Replication<A, R, T> getReplication()
    {
        return this.replication;
    }

    /**
     * @see nl.tudelft.simulation.dsol.simulators.SimulatorInterface #initialize(Replication,short)
     */
    public void initialize(final Replication<A, R, T> initReplication, final ReplicationMode replicationMode)
            throws RemoteException, SimRuntimeException
    {
        if (initReplication == null)
        {
            throw new IllegalArgumentException("replication == null ?");
        }
        if (this.isRunning())
        {
            throw new SimRuntimeException("Cannot initialize a running simulator");
        }
        synchronized (this.semaphore)
        {
            this.removeAllListeners(StatisticsObject.class);
            this.replication = initReplication;
            this.simulatorTime.setZero();
            this.fireEvent(SimulatorInterface.START_REPLICATION_EVENT, this.simulatorTime, this.simulatorTime);
            this.fireEvent(SimulatorInterface.TIME_CHANGED_EVENT, this.simulatorTime, this.simulatorTime);
            Logger.finer(this, "initialize", "OK");
        }
    }

    /**
     * @see nl.tudelft.simulation.dsol.simulators.SimulatorInterface#isRunning()
     */
    public boolean isRunning()
    {
        return this.running;
    }

    /**
     * The run method defines the actual time step mechanism of the simulator. The implementation of this method depends
     * on the formalism. Where discrete event formalisms loop over an eventlist continuous simulators take pre-defined
     * time steps.
     */
    public abstract void run();

    /**
     * @see nl.tudelft.simulation.dsol.simulators.SimulatorInterface#start()
     */
    public void start() throws SimRuntimeException
    {
        if (this.isRunning())
        {
            throw new SimRuntimeException("Cannot start a running simulator");
        }
        if (this.replication == null)
        {
            throw new SimRuntimeException("Cannot start a simulator" + " without replication details");
        }
        if (this.simulatorTime.ge(this.replication.getTreatment().getEndTime()))
        {
            throw new SimRuntimeException("Cannot start simulator : " + "simulatorTime = runLength");
        }
        synchronized (this.semaphore)
        {
            this.running = true;
            this.fireEvent(START_EVENT, null);
            this.fireEvent(SimulatorInterface.TIME_CHANGED_EVENT, this.simulatorTime, this.simulatorTime);
            Logger.finer(this, "start", "OK");
            if (!Thread.currentThread().getName().equals(this.worker.getName()))
            {
                this.worker.interrupt();
            }
            else
            {
                this.run();
            }
        }
    }

    /**
     * @see nl.tudelft.simulation.dsol.simulators.SimulatorInterface#step()
     */
    public void step() throws SimRuntimeException
    {
        if (this.isRunning())
        {
            throw new SimRuntimeException("Cannot step a running simulator");
        }
        if (this.replication == null)
        {
            throw new SimRuntimeException("Cannot step a simulator " + "without replication details");
        }
        if (this.simulatorTime.ge(this.replication.getTreatment().getEndTime()))
        {
            throw new SimRuntimeException("Cannot step simulator: " + "SimulatorTime = runControl.runLength");
        }
        Logger.finer(this, "step", "OK");
        this.fireEvent(SimulatorInterface.STEP_EVENT, null);
    }

    /**
     * @see nl.tudelft.simulation.dsol.simulators.SimulatorInterface#stop()
     */
    public void stop()
    {
        if (this.isRunning())
        {
            this.running = false;
            Logger.finer(this, "stop", "OK");
            if (this.simulatorTime.ge(this.getReplication().getTreatment().getEndTime()))
            {
                this.fireEvent(new Event(SimulatorInterface.END_OF_REPLICATION_EVENT, this, this.simulatorTime));
            }
            this.fireEvent(SimulatorInterface.STOP_EVENT, null);
        }
    }

    /**
     * writes a serializable method to stream
     * @param out the outputstream
     * @throws IOException on IOException
     */
    private synchronized void writeObject(final ObjectOutputStream out) throws IOException
    {
        out.writeObject(this.simulatorTime);
        out.writeObject(this.replication);
    }

    /**
     * reads a serializable method from stream
     * @param in the inputstream
     * @throws IOException on IOException
     */
    @SuppressWarnings("unchecked")
    private synchronized void readObject(final java.io.ObjectInputStream in) throws IOException
    {
        try
        {
            this.simulatorTime = (T) in.readObject();
            this.replication = (Replication<A, R, T>) in.readObject();
            this.running = false;
            this.semaphore = new Object();
            this.worker = new WorkerThread(this.getClass().getName(), this);
        }
        catch (Exception exception)
        {
            throw new IOException(exception.getMessage());
        }
    }
}