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
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:36:44 $
 * @param <A> the absolute storage type for the simulation time, e.g. Calendar, UnitTimeDouble, or Double.
 * @param <R> the relative type for time storage, e.g. Long for the Calendar. For most non-calendar types, the absolute
 *            and relative types are the same.
 * @param <T> the extended type itself to be able to implement a comparator on the simulation time.
 * @since 1.5
 */
@SuppressWarnings("checkstyle:linelength")
public interface SimulatorInterface<A extends Comparable<A>, R extends Number & Comparable<R>, T extends SimTime<A, R, T>>
        extends Remote, Serializable, EventProducerInterface
{
    /** END_OF_REPLICATION_EVENT is fired when a replication is finished. */
    EventType END_OF_REPLICATION_EVENT = new EventType("END_OF_REPLICATION_EVENT");

    /** START_EVENT is fired when the simulator is started. */
    EventType START_REPLICATION_EVENT = new EventType("START_REPLICATION_EVENT");

    /** START_EVENT is fired when the simulator is started. */
    EventType START_EVENT = new EventType("START_EVENT");

    /** STEP_EVENT is fired when the simulator is stepped. */
    EventType STEP_EVENT = new EventType("STEP_EVENT");

    /** STOP_EVENT is fired when the simulator is stopped. */
    EventType STOP_EVENT = new EventType("STOP_EVENT");

    /** TIME_CHANGED_EVENT is fired when the simulatorTime is updated. */
    EventType TIME_CHANGED_EVENT = new EventType("TIME_CHANGED_EVENT");

    /** WARMUP_EVENT is fired when the initialize method is invoked. */
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
    void initialize(Replication<A, R, T> replication, ReplicationMode replicationMode)
            throws RemoteException, SimRuntimeException;

    /**
     * is the simulator running.
     * @return boolean
     * @throws RemoteException on network failure
     */
    boolean isRunning() throws RemoteException;

    /**
     * starts the simulator, and fire a START event that the simulator was started.
     * @throws RemoteException on network failure
     * @throws SimRuntimeException whenever starting fails. Possible occasions include starting a started simulator
     */
    void start() throws RemoteException, SimRuntimeException;

    /**
     * starts the simulator.
     * @param fireStartEvent boolean; determine whether to fire a START event that the simulator was started
     * @throws RemoteException on network failure
     * @throws SimRuntimeException whenever starting fails. Possible occasions include starting a started simulator
     */
    void start(boolean fireStartEvent) throws RemoteException, SimRuntimeException;

    /**
     * steps the simulator, and fire a STEP event to indicate the simulator made a step.
     * @throws RemoteException on network failure
     * @throws SimRuntimeException whenever stepping fails. Possible occasions include stepping a stopped simulator
     */
    void step() throws RemoteException, SimRuntimeException;

    /**
     * steps the simulator.
     * @param fireStepEvent boolean; determine whether to fire a STEP event that the simulator was stepped
     * @throws RemoteException on network failure
     * @throws SimRuntimeException whenever stepping fails. Possible occasions include starting a started simulator
     */
    void step(boolean fireStepEvent) throws RemoteException, SimRuntimeException;

    /**
     * stops the simulator, and fire a STOP event that the simulator was stopped.
     * @throws RemoteException on network failure
     * @throws SimRuntimeException whenever stopping fails. Possible occasions include stopping a stopped simulator
     */
    void stop() throws RemoteException, SimRuntimeException;

    /**
     * stops the simulator.
     * @param fireStopEvent boolean; determine whether to fire a STOP event that the simulator was stopped
     * @throws RemoteException on network failure
     * @throws SimRuntimeException whenever stopping fails. Possible occasions include starting a started simulator
     */
    void stop(boolean fireStopEvent) throws RemoteException, SimRuntimeException;

    /***********************************************************************************************************/
    /*********************************** EASY ACCESS INTERFACE EXTENSIONS **************************************/
    /***********************************************************************************************************/

    /** Easy access interface SimulatorInterface.Double. */
    public interface TimeDouble extends SimulatorInterface<Double, Double, SimTimeDouble>
    {
        // typed extension
    }

    /** Easy access interface SimulatorInterface.Float. */
    public interface TimeFloat extends SimulatorInterface<Float, Float, SimTimeFloat>
    {
        // typed extension
    }

    /** Easy access interface SimulatorInterface.Long. */
    public interface TimeLong extends SimulatorInterface<Long, Long, SimTimeLong>
    {
        // typed extension
    }

    /** Easy access interface SimulatorInterface.DoubleUnit. */
    public interface TimeDoubleUnit extends SimulatorInterface<UnitTimeDouble, UnitTimeDouble, SimTimeDoubleUnit>
    {
        // typed extension
    }

    /** Easy access interface SimulatorInterface.FloatUnit. */
    public interface TimeFloatUnit extends SimulatorInterface<UnitTimeFloat, UnitTimeFloat, SimTimeFloatUnit>
    {
        // typed extension
    }

    /** Easy access interface SimulatorInterface.LongUnit. */
    public interface TimeLongUnit extends SimulatorInterface<UnitTimeLong, UnitTimeLong, SimTimeLongUnit>
    {
        // typed extension
    }

    /** Easy access interface SimulatorInterface.CalendarDouble. */
    public interface CalendarDouble extends SimulatorInterface<Calendar, UnitTimeDouble, SimTimeCalendarDouble>
    {
        // typed extension
    }

    /** Easy access interface SimulatorInterface.CalendarFloat. */
    public interface CalendarFloat extends SimulatorInterface<Calendar, UnitTimeFloat, SimTimeCalendarFloat>
    {
        // typed extension
    }

    /** Easy access interface SimulatorInterface.CalendarLong. */
    public interface CalendarLong extends SimulatorInterface<Calendar, UnitTimeLong, SimTimeCalendarLong>
    {
        // typed extension
    }

}
