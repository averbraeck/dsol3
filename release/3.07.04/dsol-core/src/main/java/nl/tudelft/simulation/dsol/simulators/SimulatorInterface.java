package nl.tudelft.simulation.dsol.simulators;

import java.io.Serializable;
import java.rmi.Remote;

import org.djunits.value.vdouble.scalar.Duration;
import org.djunits.value.vdouble.scalar.Time;
import org.djunits.value.vfloat.scalar.FloatDuration;
import org.djunits.value.vfloat.scalar.FloatTime;
import org.djutils.event.EventProducerInterface;
import org.djutils.event.EventType;
import org.djutils.event.TimedEventType;
import org.djutils.metadata.MetaData;

import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.experiment.ReplicationInterface;
import nl.tudelft.simulation.dsol.logger.SimLogger;
import nl.tudelft.simulation.dsol.model.DSOLModel;
import nl.tudelft.simulation.dsol.simtime.SimTime;
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
 * Copyright (c) 2002-2021 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @param <A> the absolute storage type for the simulation time, e.g. Time, Float, or Double.
 * @param <R> the relative type for time storage, e.g. Duration for absolute Time. For most non-unit types, the absolute and
 *            relative types are the same.
 * @param <T> the extended simulation time type to be able to implement a comparator on the simulation time.
 */
@SuppressWarnings("checkstyle:linelength")
public interface SimulatorInterface<A extends Comparable<A> & Serializable, R extends Number & Comparable<R>,
        T extends SimTime<A, R, T>> extends Remote, Serializable, EventProducerInterface
{
    /** STARTING_EVENT is fired when the simulator.start() method is called (the run() method still needs to start). */
    EventType STARTING_EVENT = new EventType(new MetaData("STARTING_EVENT", "simulator starting"));

    /** START_EVENT is fired when the simulator is started. */
    TimedEventType START_EVENT = new TimedEventType(new MetaData("START_EVENT", "simulator started"));

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
     * Returns the currently executed replication, or null when the initialize method has not yet been called.
     * @return ReplicationInterface&lt;A, R, T&gt;; the current replication, or null when the model has not yet been initialized
     */
    ReplicationInterface<A, R, T> getReplication();

    /**
     * Returns the currently executed model, or null when the initialize method has not yet been called.
     * @return DSOLModel&lt;A, R, T, ? extends SimulatorInterface&gt;; the currently executed model, or null when the model has
     *         not yet been initialized
     */
    DSOLModel<A, R, T, ? extends SimulatorInterface<A, R, T>> getModel();

    /**
     * Initializes the simulator with a replication for a model. It immediately fires a START_REPLICATION_EVENT and a
     * TIME_CHANGED_EVENT with the starting time. It does not yet fire a WARMUP_EVENT in case the warmup time is zero; this will
     * only be done after the simulator has been started. Note that the listeners of all statistics objects are removed when the
     * simulator is initialized with the replication. Connecting the statistics objects to the simulation should be done between
     * the initialize(...) method and starting the simulator, or could even be delayed till the WARMUP_EVENT has been fired.
     * @param model DSOLModel&lt;A, R, T, S&gt;; the model to initialize
     * @param replication Replication&lt;A, R, T, ? extends SimulatorInterface&lt;A, R, T&gt;&gt;; the replication to use for
     *            running the model
     * @throws SimRuntimeException when the simulator is running
     */
    void initialize(DSOLModel<A, R, T, ? extends SimulatorInterface<A, R, T>> model, ReplicationInterface<A, R, T> replication)
            throws SimRuntimeException;

    /**
     * Clean up the simulator after a replication. Remove the worker thread.
     */
    void cleanUp();

    /**
     * Starts the simulator, and fire a START_EVENT that the simulator was started. Note that when the simulator was already
     * started an exception will be thrown, and no event will be fired. The start uses the RunUntil property with a value of the
     * end time of the replication whenstarting the simulator.
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
     * @param stopTime T; the absolute time till when we want to run the simulation, coded as a SimTime object
     * @throws SimRuntimeException whenever starting fails. Possible occasions include starting a started simulator
     */
    void runUpTo(T stopTime) throws SimRuntimeException;

    /**
     * Runs the simulator up to a certain time; all events at that time, or the solving of the differential equation at that
     * timestep, will be executed.
     * @param stopTime T; the absolute time till when we want to run the simulation, coded as a SimTime object
     * @throws SimRuntimeException whenever starting fails. Possible occasions include starting a started simulator
     */
    void runUpToAndIncluding(T stopTime) throws SimRuntimeException;

    /**
     * Get the logger for a simulator. Since the loggers display the simulator time, each logger that runs in the same JVM needs
     * to have its own logger.
     * @return SimLogger; the logger that is specific for this simulator
     */
    SimLogger getLogger();

    /** {@inheritDoc} */
    @Override
    Serializable getSourceId();

    /**
     * Get the run state of the simulator.
     * @return RunState; the run state of the simulator
     */
    RunState getRunState();

    /**
     * isInitialized is true from the moment that the Simulator has been initialized with the Replication till the moment that
     * the replication has ended.
     * @return boolean; whether the Simulator has been initialized with a Replication
     */
    default boolean isInitialized()
    {
        return getRunState() != RunState.NOT_INITIALIZED;
    }

    /**
     * isStartingOrRunning is true between the moment the start has been initiated, till the moment the stop has been initiated.
     * It includes the STARTING state and the STARTED state.
     * @return boolean; whether the Simulator has been started or has successfully started, and has not yet been stopped
     */
    default boolean isStartingOrRunning()
    {
        return getRunState() == RunState.STARTED || getRunState() == RunState.STARTING;
    }

    /**
     * isStoppingOrStopped is true for all states where it has not been started or it is not running. It includes all states
     * except the STARTING state and the STARTED state.
     * @return boolean; whether the Simulator has not been started and is not running
     */
    default boolean isStoppingOrStopped()
    {
        return !isStartingOrRunning();
    }

    /**
     * Get the replication state of the simulator.
     * @return ReplicationState; the replication state of the simulator
     */
    ReplicationState getReplicationState();

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

}
