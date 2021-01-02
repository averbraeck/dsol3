package nl.tudelft.simulation.dsol.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

import org.djunits.value.vdouble.scalar.Duration;
import org.djunits.value.vdouble.scalar.Time;
import org.djunits.value.vfloat.scalar.FloatDuration;
import org.djunits.value.vfloat.scalar.FloatTime;

import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.model.inputparameters.InputParameterMap;
import nl.tudelft.simulation.dsol.model.outputstatistics.OutputStatistic;
import nl.tudelft.simulation.dsol.simtime.SimTime;
import nl.tudelft.simulation.dsol.simtime.SimTimeCalendarDouble;
import nl.tudelft.simulation.dsol.simtime.SimTimeCalendarFloat;
import nl.tudelft.simulation.dsol.simtime.SimTimeCalendarLong;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simtime.SimTimeDoubleUnit;
import nl.tudelft.simulation.dsol.simtime.SimTimeFloat;
import nl.tudelft.simulation.dsol.simtime.SimTimeFloatUnit;
import nl.tudelft.simulation.dsol.simtime.SimTimeLong;
import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;

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
 * @param <A> the absolute storage type for the simulation time, e.g. Calendar, Duration, or Double.
 * @param <R> the relative type for time storage, e.g. Long for the Calendar. For most non-calendar types, the absolute and
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
     * @return List&lt;OutputStatistic&gt; the output statistics for this model
     */
    List<OutputStatistic<?>> getOutputStatistics();

    /***********************************************************************************************************/
    /*********************************** EASY ACCESS INTERFACE EXTENSIONS **************************************/
    /***********************************************************************************************************/

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

    /**
     * Easy access interface DSOLModel.TimeCalendarDouble.
     * @param <S> the simulator to use
     */
    public interface CalendarDouble<S extends SimulatorInterface<Calendar, Duration, SimTimeCalendarDouble>>
            extends DSOLModel<Calendar, Duration, SimTimeCalendarDouble, S>
    {
        // easy access extension
    }

    /**
     * Easy access interface DSOLModel.TimeCalendarFloat.
     * @param <S> the simulator to use
     */
    public interface CalendarFloat<S extends SimulatorInterface<Calendar, FloatDuration, SimTimeCalendarFloat>>
            extends DSOLModel<Calendar, FloatDuration, SimTimeCalendarFloat, S>
    {
        // easy access extension
    }

    /**
     * Easy access interface DSOLModel.TimeCalendarLong.
     * @param <S> the simulator to use
     */
    public interface CalendarLong<S extends SimulatorInterface<Calendar, Long, SimTimeCalendarLong>>
            extends DSOLModel<Calendar, Long, SimTimeCalendarLong, S>
    {
        // easy access extension
    }
}
