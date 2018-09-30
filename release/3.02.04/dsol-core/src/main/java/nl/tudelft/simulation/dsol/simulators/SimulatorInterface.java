package nl.tudelft.simulation.dsol.simulators;

import java.io.Serializable;
import java.rmi.Remote;
import java.util.Calendar;

import org.djunits.value.vdouble.scalar.Duration;
import org.djunits.value.vdouble.scalar.Time;
import org.djunits.value.vfloat.scalar.FloatDuration;
import org.djunits.value.vfloat.scalar.FloatTime;

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
import nl.tudelft.simulation.event.EventProducerInterface;
import nl.tudelft.simulation.event.EventType;

/**
 * The SimulatorInterface defines the behavior of the simulators in the DSOL framework. The simulator is defined as the
 * computational object capable of executing the model. The simulator is therefore an object which must can be stopped,
 * paused, started, reset, etc.<br>
 * <br>
 * The START_REPLICATION_EVENT, WARMUP_EVENT, and END_REPLICATION_EVENT are fired exactly one time per replication. Note
 * that the WARMUP_EVENT is fired right after the START_REPLICATION_EVENT when the warmup time is zero. The START_EVENT,
 * STOP_EVENT and STEP_EVENT should only be fired between the start of the replication and the end of the replication.
 * The START_EVENT and STEP_EVENT should only be fired when the simulator is stopped; the STOP_EVENT should only be
 * fired when the simulator is running.
 * <p>
 * Copyright (c) 2002-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. See for project information <a href="https://simulation.tudelft.nl/">https://simulation.tudelft.nl</a>. The
 * DSOL project is distributed under a three-clause BSD-style license, which can be found at <a href=
 * "https://simulation.tudelft.nl/dsol/3.0/license.html">https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @param <A> the absolute storage type for the simulation time, e.g. Calendar, Duration, or Double.
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
    EventType END_REPLICATION_EVENT = new EventType("END_REPLICATION_EVENT");

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
     * Returns the absolute simulator time.
     * @return the simulator time.
     */
    A getSimulatorTime();

    /**
     * Returns the wrapper SimTime object for the simulator time.
     * @return the simulator time.
     */
    T getSimTime();

    /**
     * Returns the currently executed replication.
     * @return the current replication
     */
    Replication<A, R, T> getReplication();

    /**
     * Initializes the simulator with a specified replication. It immediately fires a START_REPLICATION_EVENT and a
     * TIME_CHANGED_EVENT with the starting time. It does not yet fire a WARMUP_EVENT in case the warmup time is zero;
     * this will only be done after the simulator has been started. Note that the listeners of all statistics objects
     * are removed when the simulator is initialized with the replication. Connecting the statistics objects to the
     * simulation should be done between the initialize(...) method and starting the simulator, or could even be delayed
     * till the WARMUP_EVENT has been fired.
     * @param replication the replication
     * @param replicationMode the replication mode, i.e. steady state or terminating
     * @throws SimRuntimeException when the simulator is running
     */
    void initialize(Replication<A, R, T> replication, ReplicationMode replicationMode) throws SimRuntimeException;

    /**
     * The method that is called when the replication ends. Note that it can also forcefully terminate the replication
     * before the actual replication time is over. It immediately fires a STOP_EVENT followed by an
     * END_REPLICATION_EVENT, and stops the running of the simulator. When the simulation time is not equal to or larger
     * than the length of the replication, a logger warning is given, but the method is fully executed. In that case it
     * does set the simulation time to the end time of the replication, to avoid restarting of the simulator.
     */
    void endReplication();

    /**
     * Return whether the simulator is running.
     * @return boolean; whether the simulator is running or not
     */
    boolean isRunning();

    /**
     * Starts the simulator, and fire a START_EVENT that the simulator was started. Note that when the simulator was
     * already started an exception will be thrown, and no event will be fired.
     * @throws SimRuntimeException whenever starting fails. Possible occasions include starting an already started
     *             simulator
     */
    void start() throws SimRuntimeException;

    /**
     * Starts the simulator. Depending on the boolean parameter, a START_EVENT can be fired that the simulator was
     * started; note that when the simulator is running an exception will be thrown, and no event will be fired.
     * @param fireStartEvent boolean; determine whether to fire a START_EVENT that the simulator was started
     * @throws SimRuntimeException whenever starting fails. Possible occasions include starting a started simulator
     */
    void start(boolean fireStartEvent) throws SimRuntimeException;

    /**
     * Steps the simulator, and fire a STEP_EVENT to indicate the simulator made a step. Note that when the simulator is
     * running an exception will be thrown, and no event will be fired.
     * @throws SimRuntimeException whenever stepping fails. Possible occasions include stepping an already running
     *             simulator
     */
    void step() throws SimRuntimeException;

    /**
     * Steps the simulator. Depending on the boolean parameter, a STEP_EVENT can be fired that the simulator was
     * stepped; note that when the simulator is running an exception will be thrown, and no event will be fired.
     * @param fireStepEvent boolean; determine whether to fire a STEP_EVENT that the simulator was stepped
     * @throws SimRuntimeException whenever stepping fails. Possible occasions include stepping a running simulator
     */
    void step(boolean fireStepEvent) throws SimRuntimeException;

    /**
     * Stops the simulator, and fire a STOP_EVENT that the simulator was stopped. Note that when the simulator was
     * already stopped an exception will be thrown, and no event will be fired.
     * @throws SimRuntimeException whenever stopping fails. Possible occasions include stopping an already stopped
     *             simulator
     */
    void stop() throws SimRuntimeException;

    /**
     * Stops the simulator. Depending on the boolean parameter, a STOP_EVENT can be fired that the simulator was
     * stopped; note that when the simulator is not running an exception will be thrown, and no event will be fired.
     * @param fireStopEvent boolean; determine whether to fire a STOP_EVENT that the simulator was stopped
     * @throws SimRuntimeException whenever stopping fails. Possible occasions include stopping an already stopped
     *             simulator
     */
    void stop(boolean fireStopEvent) throws SimRuntimeException;

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
    public interface TimeDoubleUnit extends SimulatorInterface<Time, Duration, SimTimeDoubleUnit>
    {
        // typed extension
    }

    /** Easy access interface SimulatorInterface.FloatUnit. */
    public interface TimeFloatUnit extends SimulatorInterface<FloatTime, FloatDuration, SimTimeFloatUnit>
    {
        // typed extension
    }

    /** Easy access interface SimulatorInterface.CalendarDouble. */
    public interface CalendarDouble extends SimulatorInterface<Calendar, Duration, SimTimeCalendarDouble>
    {
        // typed extension
    }

    /** Easy access interface SimulatorInterface.CalendarFloat. */
    public interface CalendarFloat extends SimulatorInterface<Calendar, FloatDuration, SimTimeCalendarFloat>
    {
        // typed extension
    }

    /** Easy access interface SimulatorInterface.CalendarLong. */
    public interface CalendarLong extends SimulatorInterface<Calendar, Long, SimTimeCalendarLong>
    {
        // typed extension
    }

}
