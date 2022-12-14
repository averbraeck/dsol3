package nl.tudelft.simulation.dsol.formalisms.flow;

import java.io.Serializable;

import org.djunits.value.vdouble.scalar.Duration;
import org.djunits.value.vdouble.scalar.Time;
import org.djunits.value.vfloat.scalar.FloatDuration;
import org.djunits.value.vfloat.scalar.FloatTime;
import org.djutils.event.EventProducer;

import nl.tudelft.simulation.dsol.simtime.SimTime;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simtime.SimTimeDoubleUnit;
import nl.tudelft.simulation.dsol.simtime.SimTimeFloat;
import nl.tudelft.simulation.dsol.simtime.SimTimeFloatUnit;
import nl.tudelft.simulation.dsol.simtime.SimTimeLong;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;

/**
 * A station is an object which can accept other objects.
 * <p>
 * Copyright (c) 2002-2022 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @param <A> the absolute storage type for the simulation time, e.g. Time, Float, or Double.
 * @param <R> the relative type for time storage, e.g. Duration for absolute Time. For most non-unit types, the absolute and
 *            relative types are the same.
 * @param <T> the extended type itself to be able to implement a comparator on the simulation time.
 * @since 1.5
 */
public abstract class Station<A extends Comparable<A> & Serializable, R extends Number & Comparable<R>,
        T extends SimTime<A, R, T>> extends EventProducer implements StationInterface<A, R, T>
{
    /** */
    private static final long serialVersionUID = 20140805L;

    /** simulator is the simulator on which behavior is scheduled. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected DEVSSimulatorInterface<A, R, T> simulator;

    /** destination refers to the next station in the process-model chain. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected StationInterface<A, R, T> destination;

    /** the id of the Station. */
    private Serializable id;

    /**
     * constructs a new Station.
     * @param id Serializable; the id of the Station
     * @param simulator DEVSSimulatorInterface&lt;A,R,T&gt;; is the simulator on which behavior is scheduled
     */
    public Station(final Serializable id, final DEVSSimulatorInterface<A, R, T> simulator)
    {
        this.id = id;
        this.simulator = simulator;
    }

    /** {@inheritDoc} */
    @Override
    public Serializable getSourceId()
    {
        return this.id;
    }

    /** {@inheritDoc} */
    @Override
    public void receiveObject(final Object object)
    {
        this.fireTimedEvent(StationInterface.RECEIVE_EVENT, 1.0, this.simulator.getSimulatorTime());
    }

    /** {@inheritDoc} */
    @Override
    public void setDestination(final StationInterface<A, R, T> destination)
    {
        this.destination = destination;
    }

    /**
     * releases an object.
     * @param object Object; is the entity
     */
    protected synchronized void releaseObject(final Object object)
    {
        this.fireTimedEvent(StationInterface.RELEASE_EVENT, 0.0, this.simulator.getSimulatorTime());
        if (this.destination != null)
        {
            this.destination.receiveObject(object);
        }
    }

    /** {@inheritDoc} */
    @Override
    public StationInterface<A, R, T> getDestination()
    {
        return this.destination;
    }

    /**
     * @return simulator
     */
    public DEVSSimulatorInterface<A, R, T> getSimulator()
    {
        return this.simulator;
    }

    /***********************************************************************************************************/
    /************************************* EASY ACCESS CLASS EXTENSIONS ****************************************/
    /***********************************************************************************************************/

    /** Easy access class Station.TimeDouble. */
    public static class TimeDouble extends Station<Double, Double, SimTimeDouble> implements StationInterface.TimeDouble
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * constructs a new Station.TimeDouble.
         * @param id Serializable; the id of the Station
         * @param simulator DEVSSimulatorInterface.TimeDouble; is the simulator on which behavior is scheduled
         */
        public TimeDouble(final Serializable id, final DEVSSimulatorInterface.TimeDouble simulator)
        {
            super(id, simulator);
        }
    }

    /** Easy access class Station.TimeFloat. */
    public static class TimeFloat extends Station<Float, Float, SimTimeFloat> implements StationInterface.TimeFloat
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * constructs a new Station.TimeFloat.
         * @param id Serializable; the id of the Station
         * @param simulator DEVSSimulatorInterface.TimeFloat; is the simulator on which behavior is scheduled
         */
        public TimeFloat(final Serializable id, final DEVSSimulatorInterface.TimeFloat simulator)
        {
            super(id, simulator);
        }
    }

    /** Easy access class Station.TimeLong. */
    public static class TimeLong extends Station<Long, Long, SimTimeLong> implements StationInterface.TimeLong
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * constructs a new Station.TimeLong.
         * @param id Serializable; the id of the Station
         * @param simulator DEVSSimulatorInterface.TimeLong; is the simulator on which behavior is scheduled
         */
        public TimeLong(final Serializable id, final DEVSSimulatorInterface.TimeLong simulator)
        {
            super(id, simulator);
        }
    }

    /** Easy access class Station.TimeDoubleUnit. */
    public static class TimeDoubleUnit extends Station<Time, Duration, SimTimeDoubleUnit>
            implements StationInterface.TimeDoubleUnit
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * constructs a new Station.TimeDoubleUnit.
         * @param id Serializable; the id of the Station
         * @param simulator DEVSSimulatorInterface.TimeDoubleUnit; is the simulator on which behavior is scheduled
         */
        public TimeDoubleUnit(final Serializable id, final DEVSSimulatorInterface.TimeDoubleUnit simulator)
        {
            super(id, simulator);
        }
    }

    /** Easy access class Station.TimeFloatUnit. */
    public static class TimeFloatUnit extends Station<FloatTime, FloatDuration, SimTimeFloatUnit>
            implements StationInterface.TimeFloatUnit
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * constructs a new Station.TimeFloatUnit.
         * @param id Serializable; the id of the Station
         * @param simulator DEVSSimulatorInterface.TimeFloatUnit; is the simulator on which behavior is scheduled
         */
        public TimeFloatUnit(final Serializable id, final DEVSSimulatorInterface.TimeFloatUnit simulator)
        {
            super(id, simulator);
        }
    }

}
