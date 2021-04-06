package nl.tudelft.simulation.dsol.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.djunits.value.vdouble.scalar.Duration;
import org.djunits.value.vdouble.scalar.Time;
import org.djunits.value.vfloat.scalar.FloatDuration;
import org.djunits.value.vfloat.scalar.FloatTime;

import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.model.inputparameters.InputParameterMap;
import nl.tudelft.simulation.dsol.simtime.SimTime;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simtime.SimTimeDoubleUnit;
import nl.tudelft.simulation.dsol.simtime.SimTimeFloat;
import nl.tudelft.simulation.dsol.simtime.SimTimeFloatUnit;
import nl.tudelft.simulation.dsol.simtime.SimTimeLong;
import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;
import nl.tudelft.simulation.dsol.statistics.StatisticsInterface;
import nl.tudelft.simulation.jstats.streams.StreamInterface;

/**
 * The model interface defines the model object. Since version 2.1.0 of DSOL, the DSOLModel now knows its simulator and can
 * return it to anyone interested. Through the Simulator, the Replication can be requested and through that the Experiment and
 * the Treatment under which the simulation is running.
 * <p>
 * Copyright (c) 2003-2021 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank">Alexander Verbraeck</a>
 * @param <A> the absolute storage type for the simulation time, e.g. Time, Float, or Double.
 * @param <R> the relative type for time storage, e.g. Duration for absolute Time. For most non-unit types, the absolute and
 *            relative types are the same.
 * @param <T> the extended type itself to be able to implement a comparator on the simulation time.
 * @param <S> the simulator to use
 */
public interface DSOLModel<A extends Comparable<A> & Serializable, R extends Number & Comparable<R>, T extends SimTime<A, R, T>,
        S extends SimulatorInterface<A, R, T>> extends Serializable
{
    /**
     * construct a model on a simulator.
     * @throws SimRuntimeException on model construction failure
     */
    void constructModel() throws SimRuntimeException;

    /**
     * Return the simulator for this model.
     * @return the simulator for the model
     */
    S getSimulator();

    /**
     * Get the input parameters for this model.
     * @return List&lt;InputParameter&gt; the input parameters for this model
     */
    InputParameterMap getInputParameterMap();

    /**
     * Get the output statistics for this model.
     * @return List&lt;StatisticsInterface&gt; the output statistics for this model
     */
    List<StatisticsInterface<A, R, T>> getOutputStatistics();

    /**
     * Set the initial streams of the model. This method has to be called from the constructor, so the random streams can be
     * used in the constructModel() method, and the seed values can be changed by the experiment for subsequent replications,
     * prior to calling constructModel().
     */
    void setInitialStreams();

    /**
     * Return the streams of this model, mapping stream ids to streams.
     * @return Map&lt;String, StreamInterface&gt;; the streams of this model
     */
    Map<String, StreamInterface> getStreams();

    /**
     * Return a specific stream of this model, based on a stream id.
     * @param streamId String; the id of the stream to be retrieved
     * @return StreamInterface the stream
     */
    StreamInterface getStream(String streamId);

    /**
     * Reset the streams to their original seed values.
     */
    void resetStreams();

    /* ********************************************************************************************************* */
    /* ********************************** EASY ACCESS INTERFACE EXTENSIONS ************************************* */
    /* ********************************************************************************************************* */

    /**
     * Easy access interface DSOLModel.TimeDouble.
     * @param <S> the simulator to use
     */
    public interface TimeDouble<S extends SimulatorInterface<Double, Double, SimTimeDouble>>
            extends DSOLModel<Double, Double, SimTimeDouble, S>
    {
        // easy access extension
    }

    /**
     * Easy access interface DSOLModel.TimeFloat.
     * @param <S> the simulator to use
     */
    public interface TimeFloat<S extends SimulatorInterface<Float, Float, SimTimeFloat>>
            extends DSOLModel<Float, Float, SimTimeFloat, S>
    {
        // easy access extension
    }

    /**
     * Easy access interface DSOLModel.TimeLong.
     * @param <S> the simulator to use
     */
    public interface TimeLong<S extends SimulatorInterface<Long, Long, SimTimeLong>>
            extends DSOLModel<Long, Long, SimTimeLong, S>
    {
        // easy access extension
    }

    /**
     * Easy access interface DSOLModel.TimeDoubleUnit.
     * @param <S> the simulator to use
     */
    public interface TimeDoubleUnit<S extends SimulatorInterface<Time, Duration, SimTimeDoubleUnit>>
            extends DSOLModel<Time, Duration, SimTimeDoubleUnit, S>
    {
        // easy access extension
    }

    /**
     * Easy access interface DSOLModel.TimeFloatUnit.
     * @param <S> the simulator to use
     */
    public interface TimeFloatUnit<S extends SimulatorInterface<FloatTime, FloatDuration, SimTimeFloatUnit>>
            extends DSOLModel<FloatTime, FloatDuration, SimTimeFloatUnit, S>
    {
        // easy access extension
    }

}
