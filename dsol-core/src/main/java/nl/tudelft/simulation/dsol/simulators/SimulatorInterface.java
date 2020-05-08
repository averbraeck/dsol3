package nl.tudelft.simulation.dsol.simulators;

import java.io.Serializable;
import java.rmi.Remote;
import java.util.Calendar;

import org.djunits.value.vdouble.scalar.Duration;
import org.djunits.value.vdouble.scalar.Time;
import org.djunits.value.vfloat.scalar.FloatDuration;
import org.djunits.value.vfloat.scalar.FloatTime;
import org.djutils.event.EventProducerInterface;
import org.djutils.event.EventType;
import org.djutils.event.TimedEventType;
import org.djutils.metadata.MetaData;

import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.experiment.Replication;
import nl.tudelft.simulation.dsol.experiment.ReplicationMode;
import nl.tudelft.simulation.dsol.logger.SimLogger;
import nl.tudelft.simulation.dsol.simtime.SimTime;
import nl.tudelft.simulation.dsol.simtime.SimTimeCalendarDouble;
import nl.tudelft.simulation.dsol.simtime.SimTimeCalendarFloat;
import nl.tudelft.simulation.dsol.simtime.SimTimeCalendarLong;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simtime.SimTimeDoubleUnit;
import nl.tudelft.simulation.dsol.simtime.SimTimeFloat;
import nl.tudelft.simulation.dsol.simtime.SimTimeFloatUnit;
import nl.tudelft.simulation.dsol.simtime.SimTimeLong;

/**
 * The SimulatorInterface defines the behavior of the simulators in the DSOL framework. The simulator is defined as the
 * computational object capable of executing the model. The simulator is therefore an object which must can be stopped, paused,
 * started, reset, etc.<br>
 * <br>
 * The START_REPLICATION_EVENT, WARMUP_EVENT, and END_REPLICATION_EVENT are fired exactly one time per replication. Note that
 * the WARMUP_EVENT is fired right after the START_REPLICATION_EVENT when the warmup time is zero. The START_EVENT, STOP_EVENT
 * and STEP_EVENT should only be fired between the start of the replication and the end of the replication. The START_EVENT and
 * STEP_EVENT should only be fired when the simulator is not running (and then started or stepped); the STOP_EVENT should only
 * be fired when the simulator is running (and then stopped).<br>
 * <br>
 * The TIME_CHANGED_EVENT should only be fired once when the simulation time has changed. For event-based simulators, it should
 * not be fired at every event but only when the event is executed at a different time than the previous event.<br>
 * <br>
 * The typical event sequence for a simulation execution is shown below. All events are fired from the run() thread except for
 * the STARTING_EVENT (fired when the start of a model is initiated from the UI or main thread), the STOPPING_EVENT (fired when
 * the stop of a model is initiated from the UI or main thread), and the STEP_EVENT (fired when executing a single step of a
 * model, initiated from the UI or main thread).
 * 
 * <pre>
 * ... wait for experimentalFram.start() method being called
 * START_EXPERIMENTALFRAME_EVENT
 *   START_EXPERIMENT_EVENT
 *     START_REPLICATION_EVENT
 *       START_EVENT
 *       TIME_CHANGED_EVENT (several times)
 *       WARMUP_EVENT
 *       TIME_CHANGED_EVENT (several times)
 *       STOP_EVENT
 *     END_REPLICATION_EVENT
 *   END_EXPERIMENT_EVENT
 *   START_EXPERIMENT_EVENT
 *     START_REPLICATION_EVENT
 *       START_EVENT
 *       TIME_CHANGED_EVENT (several times)
 *       WARMUP_EVENT
 *       TIME_CHANGED_EVENT (several times)
 *       STOP_EVENT
 *     END_REPLICATION_EVENT
 *   END_EXPERIMENT_EVENT
 * END_EXPERIMENTALFRAME_EVENT
 * </pre>
 * <p>
 * Copyright (c) 2002-2020 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @param <A> the absolute storage type for the simulation time, e.g. Calendar, Duration, or Double.
 * @param <R> the relative type for time storage, e.g. Long for the Calendar. For most non-calendar types, the absolute and
 *            relative types are the same.
 * @param <T> the extended type itself to be able to implement a comparator on the simulation time.
 * @since 1.5
 */
@SuppressWarnings("checkstyle:linelength")
public interface SimulatorInterface<A extends Comparable<A> & Serializable, R extends Number & Comparable<R>,
        T extends SimTime<A, R, T>> extends Remote, Serializable, EventProducerInterface
{
    /** STARTING_EVENT is fired when the simulator.start() method is called (the run() method still needs to start). */
    EventType STARTING_EVENT = new EventType(new MetaData("STARTING_EVENT", "simulator starting"));

    /** START_EVENT is fired when the simulator is started. */
    TimedEventType START_EVENT = new TimedEventType(new MetaData("START_EVENT", "simulator started"));

    /** STEP_EVENT is fired when the simulator is stepped. */
    TimedEventType STEP_EVENT = new TimedEventType(new MetaData("STEP_EVENT", "simulator stepped"));

    /** STOPPING_EVENT is fired when the simulator.stop() method is called (the run() method still needs to be stopped). */
    EventType STOPPING_EVENT = new EventType(new MetaData("STOPPING_EVENT", "simulator stopping"));

    /** STOP_EVENT is fired when the simulator is stopped. */
    TimedEventType STOP_EVENT = new TimedEventType(new MetaData("STOP_EVENT", "simulator stopped"));

    /** TIME_CHANGED_EVENT is fired when the simulatorTime is updated. */
    TimedEventType TIME_CHANGED_EVENT = new TimedEventType(new MetaData("TIME_CHANGED_EVENT", "time changed"));

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
    Replication<A, R, T, ? extends SimulatorInterface<A, R, T>> getReplication();

    /**
     * Initializes the simulator with a specified replication. It immediately fires a START_REPLICATION_EVENT and a
     * TIME_CHANGED_EVENT with the starting time. It does not yet fire a WARMUP_EVENT in case the warmup time is zero; this will
     * only be done after the simulator has been started. Note that the listeners of all statistics objects are removed when the
     * simulator is initialized with the replication. Connecting the statistics objects to the simulation should be done between
     * the initialize(...) method and starting the simulator, or could even be delayed till the WARMUP_EVENT has been fired.
     * @param replication Replication&lt;A, R, T, ? extends SimulatorInterface&lt;A, R, T&gt;&gt;; the replication
     * @param replicationMode ReplicationMode; the replication mode, i.e. steady state or terminating
     * @throws SimRuntimeException when the simulator is running
     */
    void initialize(Replication<A, R, T, ? extends SimulatorInterface<A, R, T>> replication, ReplicationMode replicationMode)
            throws SimRuntimeException;

    /**
     * Return whether the simulator is running.
     * @return boolean; whether the simulator is running or not
     */
    boolean isRunning();

    /**
     * Starts the simulator, and fire a START_EVENT that the simulator was started. Note that when the simulator was already
     * started an exception will be thrown, and no event will be fired.
     * @throws SimRuntimeException whenever starting fails. Possible occasions include starting an already started simulator
     */
    void start() throws SimRuntimeException;

    /**
     * Steps the simulator, and fire a STEP_EVENT to indicate the simulator made a step. Note that when the simulator is running
     * an exception will be thrown, and no event will be fired.
     * @throws SimRuntimeException whenever stepping fails. Possible occasions include stepping an already running simulator
     */
    void step() throws SimRuntimeException;

    /**
     * Stops the simulator, and fire a STOP_EVENT that the simulator was stopped. Note that when the simulator was already
     * stopped an exception will be thrown, and no event will be fired.
     * @throws SimRuntimeException whenever stopping fails. Possible occasions include stopping an already stopped simulator
     */
    void stop() throws SimRuntimeException;

    /**
     * Runs the simulator up to a certain time; any events at that time, or the solving of the differential equation at that
     * timestep, will not yet be executed.
     * @param stopTime A; the absolute time till when we want to run the simulation
     * @throws SimRuntimeException whenever starting fails. Possible occasions include starting a started simulator
     */
    void runUpTo(A stopTime) throws SimRuntimeException;

    /**
     * Runs the simulator up to a certain time; all events at that time, or the solving of the differential equation at that
     * timestep, will be executed.
     * @param stopTime A; the absolute time till when we want to run the simulation
     * @throws SimRuntimeException whenever starting fails. Possible occasions include starting a started simulator
     */
    void runUpToAndIncluding(A stopTime) throws SimRuntimeException;

    /**
     * Get the logger for a simulator. Since the loggers display the simulator time, each logger that runs in the same JVM needs
     * to have its own logger.
     * @return SimLogger; the logger that is specific for this simulator
     */
    SimLogger getLogger();

    /** {@inheritDoc} */
    @Override
    Serializable getSourceId();

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
