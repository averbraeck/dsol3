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
import java.util.Calendar;

import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.experiment.Replication;
import nl.tudelft.simulation.dsol.experiment.ReplicationMode;
import nl.tudelft.simulation.dsol.simtime.SimTime;
import nl.tudelft.simulation.dsol.simtime.SimTimeCalendarDouble;
import nl.tudelft.simulation.dsol.simtime.SimTimeCalendarFloat;
import nl.tudelft.simulation.dsol.simtime.SimTimeCalendarLong;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simtime.SimTimeDoubleUnit;
import nl.tudelft.simulation.dsol.simtime.SimTimeFloat;
import nl.tudelft.simulation.dsol.simtime.SimTimeFloatUnit;
import nl.tudelft.simulation.dsol.simtime.SimTimeLong;
import nl.tudelft.simulation.dsol.simtime.SimTimeLongUnit;
import nl.tudelft.simulation.dsol.simtime.UnitTimeDouble;
import nl.tudelft.simulation.dsol.simtime.UnitTimeFloat;
import nl.tudelft.simulation.dsol.simtime.UnitTimeLong;
import nl.tudelft.simulation.event.EventProducerInterface;
import nl.tudelft.simulation.event.EventType;

/**
 * The SimulatorInterface defines the behavior of the simulators in the DSOL framework. The simulator is defined as the
 * computational object capable of executing the model. The simulator is therefore an object which must can be stopped,
 * paused, started, reset, etc.
 * <p>
 * (c) copyright 2002-2014 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl"> www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="http://www.peter-jacobs.com">Peter Jacobs </a>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:36:44 $
 * @param <A> the absolute storage type for the simulation time, e.g. Calendar, UnitTimeDouble, or Double.
 * @param <R> the relative type for time storage, e.g. Long for the Calendar. For most non-calendar types, the absolute
 *            and relative types are the same.
 * @param <T> the extended type itself to be able to implement a comparator on the simulation time.
 * @since 1.5
 */
public abstract interface SimulatorInterface<A extends Comparable<A>, R extends Number & Comparable<R>, T extends SimTime<A, R, T>>
        extends Remote, Serializable, EventProducerInterface
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
    T getSimulatorTime() throws RemoteException;

    /**
     * returns the currently executed replication.
     * @return the current replication
     * @throws RemoteException on network failure
     */
    Replication<A, R, T> getReplication() throws RemoteException;

    /**
     * initializes the simulator with a specified replication.
     * @param replication the replication
     * @param replicationMode the replication mode, i.e. steady state or terminating
     * @throws RemoteException on network failure
     * @throws SimRuntimeException on simulator failure (simulator is running)
     */
    void initialize(Replication<A, R, T> replication, ReplicationMode replicationMode) throws RemoteException,
            SimRuntimeException;

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

    /***********************************************************************************************************/
    /*********************************** EASY ACCESS INTERFACE EXTENSIONS **************************************/
    /***********************************************************************************************************/

    /** Easy access interface SimulatorInterface.Double */
    public interface Double extends SimulatorInterface<java.lang.Double, java.lang.Double, SimTimeDouble>
    {
        // typed extension
    }

    /** Easy access interface SimulatorInterface.Float */
    public interface Float extends SimulatorInterface<java.lang.Float, java.lang.Float, SimTimeFloat>
    {
        // typed extension
    }

    /** Easy access interface SimulatorInterface.Long */
    public interface Long extends SimulatorInterface<java.lang.Long, java.lang.Long, SimTimeLong>
    {
        // typed extension
    }

    /** Easy access interface SimulatorInterface.DoubleUnit */
    public interface DoubleUnit extends SimulatorInterface<UnitTimeDouble, UnitTimeDouble, SimTimeDoubleUnit>
    {
        // typed extension
    }

    /** Easy access interface SimulatorInterface.FloatUnit */
    public interface FloatUnit extends SimulatorInterface<UnitTimeFloat, UnitTimeFloat, SimTimeFloatUnit>
    {
        // typed extension
    }

    /** Easy access interface SimulatorInterface.LongUnit */
    public interface LongUnit extends SimulatorInterface<UnitTimeLong, UnitTimeLong, SimTimeLongUnit>
    {
        // typed extension
    }

    /** Easy access interface SimulatorInterface.CalendarDouble */
    public interface CalendarDouble extends SimulatorInterface<Calendar, UnitTimeDouble, SimTimeCalendarDouble>
    {
        // typed extension
    }

    /** Easy access interface SimulatorInterface.CalendarFloat */
    public interface CalendarFloat extends SimulatorInterface<Calendar, UnitTimeFloat, SimTimeCalendarFloat>
    {
        // typed extension
    }

    /** Easy access interface SimulatorInterface.CalendarLong */
    public interface CalendarLong extends SimulatorInterface<Calendar, UnitTimeLong, SimTimeCalendarLong>
    {
        // typed extension
    }

}