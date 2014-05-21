/*
 * @(#)SimulatorIntergace.java April 4, 2003 Copyright (c) 2002-2005 Delft
 * University of Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All
 * rights reserved. This software is proprietary information of Delft University
 * of Technology 
 */
package nl.tudelft.simulation.dsol.simulators;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.experiment.Replication;
import nl.tudelft.simulation.event.EventProducerInterface;
import nl.tudelft.simulation.event.EventType;

/**
 * The SimulatorInterface defines the behavior of the simulators in the DSOL framework. The simulator is defined as the
 * computational object capable of executing the model. The simulator is therefore an object which must can be stopped,
 * paused, started, reset, etc.
 * <p>
 * (c) copyright 2002-2004 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl"> www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="http://www.peter-jacobs.com">Peter Jacobs </a>
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:36:44 $
 * @since 1.5
 */

public abstract interface SimulatorInterface extends Remote, Serializable, EventProducerInterface
{
    /** END_OF_REPLICATION_EVENT is fired when a replication is finished */
    EventType END_OF_REPLICATION_EVENT = new EventType("END_OF_REPLICATION_EVENT");

    /** START_EVENT is fired when the simulator is started */
    EventType START_REPLICATION_EVENT = new EventType("START_REPLICATION_EVENT");

    /** START_EVENT is fired when the simulator is started */
    EventType START_EVENT = new EventType("START_EVENT");

    /** STEP_EVENT is fired when the simulator is stepped */
    EventType STEP_EVENT = new EventType("STEP_EVENT");

    /** STOP_EVENT is fired when the simulator is stopped */
    EventType STOP_EVENT = new EventType("STOP_EVENT");

    /** TIME_CHANGED_EVENT is fired when the simulatorTime is updated */
    EventType TIME_CHANGED_EVENT = new EventType("TIME_CHANGED_EVENT");

    /** WARMUP_EVENT is fired when the initialize method is invoked */
    EventType WARMUP_EVENT = new EventType("WARMUP_EVENT");

    /**
     * returns the actual simulator time.
     * @return the simulator time.
     * @throws RemoteException on network failure.
     */
    double getSimulatorTime() throws RemoteException;

    /**
     * returns the currently executed replication.
     * @return the current replication
     * @throws RemoteException on network failure
     */
    Replication getReplication() throws RemoteException;

    /**
     * initializes the simulator with a specified replication.
     * @param replication the replication
     * @param replicationMode the replication mode, i.e. steady state or terminating
     * @throws RemoteException on network failure
     * @throws SimRuntimeException on simulator failure (simulator is running)
     */
    void initialize(Replication replication, short replicationMode) throws RemoteException, SimRuntimeException;

    /**
     * is the simulator running.
     * @return boolean
     * @throws RemoteException on network failure
     */
    boolean isRunning() throws RemoteException;

    /**
     * starts the simulator
     * @throws RemoteException on network failure
     * @throws SimRuntimeException whenever starting fails. Possible occasions include starting a started simulator
     */
    void start() throws RemoteException, SimRuntimeException;

    /**
     * steps the simulator.
     * @throws RemoteException on network failure
     * @throws SimRuntimeException whenever stepping fails. Possible occasions include stepping a stopped simulator
     */
    void step() throws RemoteException, SimRuntimeException;

    /**
     * stops the simulator.
     * @throws RemoteException on network failure
     * @throws SimRuntimeException whenever stopping fails. Possible occasions include stopping a stopped simulator
     */
    void stop() throws RemoteException, SimRuntimeException;
}