package nl.tudelft.simulation.dsol.simulators;

import java.io.Serializable;

import org.djunits.value.vdouble.scalar.Duration;
import org.djunits.value.vdouble.scalar.Time;
import org.djunits.value.vfloat.scalar.FloatDuration;
import org.djunits.value.vfloat.scalar.FloatTime;

import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.experiment.Replication;
import nl.tudelft.simulation.dsol.experiment.ReplicationMode;
import nl.tudelft.simulation.dsol.simtime.SimTime;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simtime.SimTimeDoubleUnit;
import nl.tudelft.simulation.dsol.simtime.SimTimeFloat;
import nl.tudelft.simulation.dsol.simtime.SimTimeFloatUnit;
import nl.tudelft.simulation.dsol.simtime.SimTimeLong;

/**
 * The DESS defines the interface of the DESS simulator. DESS stands for the Differential Equation System Specification. More
 * information on Modeling and Simulation can be found in "Theory of Modeling and Simulation" by Bernard Zeigler et.al.
 * <p>
 * Copyright (c) 2002-2021 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
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
 */
public class DESSSimulator<A extends Comparable<A> & Serializable, R extends Number & Comparable<R>, T extends SimTime<A, R, T>>
        extends Simulator<A, R, T> implements DESSSimulatorInterface<A, R, T>
{
    /** */
    private static final long serialVersionUID = 20140805L;

    /** timeStep represents the timestep of the DESS simulator. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected R timeStep;

    /**
     * Construct a DESSSimulator with an initial time step for the integration process.
     * @param initialTimeStep R; the initial time step to use in the integration.
     * @param id the id of the simulator, used in logging and firing of events.
     * @throws SimRuntimeException when initialTimeStep &lt;=0, NaN, or Infinity
     */
    public DESSSimulator(final Serializable id, final R initialTimeStep) throws SimRuntimeException
    {
        super(id);
        setTimeStep(initialTimeStep);
    }

    /** {@inheritDoc} */
    @Override
    public void initialize(final Replication<A, R, T, ? extends SimulatorInterface<A, R, T>> initReplication,
            final ReplicationMode replicationMode) throws SimRuntimeException
    {
        super.initialize(initReplication, replicationMode);
    }

    /** {@inheritDoc} */
    @Override
    public final R getTimeStep()
    {
        return this.timeStep;
    }

    /** {@inheritDoc} */
    @Override
    public final void setTimeStep(final R timeStep) throws SimRuntimeException
    {
        synchronized (super.semaphore)
        {
            if (timeStep.doubleValue() <= 0 || Double.isNaN(timeStep.doubleValue())
                    || Double.isInfinite(timeStep.doubleValue()))
            {
                throw new SimRuntimeException(
                        "DESSSimulator.setTimeStep: timeStep <= 0, NaN, or Infinity. Value provided = : " + timeStep);
            }
            this.timeStep = timeStep;
            this.fireEvent(TIME_STEP_CHANGED_EVENT, timeStep);
        }
    }

    /** {@inheritDoc} */
    @Override
    protected void stepImpl()
    {
        this.simulatorTime = this.simulatorTime.plus(this.timeStep);
        if (this.simulatorTime.gt(this.replication.getTreatment().getEndSimTime()))
        {
            this.simulatorTime = this.replication.getTreatment().getEndSimTime().copy();
            this.endReplication();
        }
        this.fireUnverifiedTimedEvent(SimulatorInterface.TIME_CHANGED_EVENT, null, this.simulatorTime.get());
    }

    /** {@inheritDoc} */
    @Override
    public void run()
    {
        while (this.simulatorTime.compareTo(this.runUntilTime) < 0 && !isStoppingOrStopped())
        {
            synchronized (super.semaphore)
            {
                stepImpl();
            }
        }
    }

    /***********************************************************************************************************/
    /************************************* EASY ACCESS CLASS EXTENSIONS ****************************************/
    /***********************************************************************************************************/

    /** Easy access class DESSSimulator.TimeDouble. */
    public static class TimeDouble extends DESSSimulator<Double, Double, SimTimeDouble>
            implements DESSSimulatorInterface.TimeDouble
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /**
         * @param initialTimeStep Double; the initial time step to use in the integration.
         * @param id the id of the simulator, used in logging and firing of events.
         * @throws SimRuntimeException when initialTimeStep &lt;=0, NaN, or Infinity
         */
        public TimeDouble(final Serializable id, final Double initialTimeStep) throws SimRuntimeException
        {
            super(id, initialTimeStep);
        }

        /** {@inheritDoc} */
        @SuppressWarnings("unchecked")
        @Override
        public Replication.TimeDouble<? extends DESSSimulatorInterface.TimeDouble> getReplication()
        {
            return (Replication.TimeDouble<? extends DESSSimulatorInterface.TimeDouble>) super.getReplication();
        }
    }

    /** Easy access class DESSSimulator.TimeFloat. */
    public static class TimeFloat extends DESSSimulator<Float, Float, SimTimeFloat> implements DESSSimulatorInterface.TimeFloat
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /**
         * @param initialTimeStep Float; the initial time step to use in the integration.
         * @param id the id of the simulator, used in logging and firing of events.
         * @throws SimRuntimeException when initialTimeStep &lt;=0, NaN, or Infinity
         */
        public TimeFloat(final Serializable id, final Float initialTimeStep) throws SimRuntimeException
        {
            super(id, initialTimeStep);
        }

        /** {@inheritDoc} */
        @SuppressWarnings("unchecked")
        @Override
        public Replication.TimeFloat<? extends DESSSimulatorInterface.TimeFloat> getReplication()
        {
            return (Replication.TimeFloat<? extends DESSSimulatorInterface.TimeFloat>) super.getReplication();
        }
    }

    /** Easy access class DESSSimulator.TimeLong. */
    public static class TimeLong extends DESSSimulator<Long, Long, SimTimeLong> implements DESSSimulatorInterface.TimeLong
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /**
         * @param initialTimeStep Long; the initial time step to use in the integration.
         * @param id the id of the simulator, used in logging and firing of events.
         * @throws SimRuntimeException when initialTimeStep &lt;=0, NaN, or Infinity
         */
        public TimeLong(final Serializable id, final Long initialTimeStep) throws SimRuntimeException
        {
            super(id, initialTimeStep);
        }

        /** {@inheritDoc} */
        @SuppressWarnings("unchecked")
        @Override
        public Replication.TimeLong<? extends DESSSimulatorInterface.TimeLong> getReplication()
        {
            return (Replication.TimeLong<? extends DESSSimulatorInterface.TimeLong>) super.getReplication();
        }
    }

    /** Easy access class DESSSimulator.TimeDoubleUnit. */
    public static class TimeDoubleUnit extends DESSSimulator<Time, Duration, SimTimeDoubleUnit>
            implements DESSSimulatorInterface.TimeDoubleUnit
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /**
         * @param initialTimeStep Duration; the initial time step to use in the integration.
         * @param id the id of the simulator, used in logging and firing of events.
         * @throws SimRuntimeException when initialTimeStep &lt;=0, NaN, or Infinity
         */
        public TimeDoubleUnit(final Serializable id, final Duration initialTimeStep) throws SimRuntimeException
        {
            super(id, initialTimeStep);
        }

        /** {@inheritDoc} */
        @SuppressWarnings("unchecked")
        @Override
        public Replication.TimeDoubleUnit<DESSSimulatorInterface.TimeDoubleUnit> getReplication()
        {
            return (Replication.TimeDoubleUnit<DESSSimulatorInterface.TimeDoubleUnit>) super.getReplication();
        }
    }

    /** Easy access class DESSSimulator.TimeFloatUnit. */
    public static class TimeFloatUnit extends DESSSimulator<FloatTime, FloatDuration, SimTimeFloatUnit>
            implements DESSSimulatorInterface.TimeFloatUnit
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /**
         * @param initialTimeStep FloatDuration; the initial time step to use in the integration.
         * @param id the id of the simulator, used in logging and firing of events.
         * @throws SimRuntimeException when initialTimeStep &lt;=0, NaN, or Infinity
         */
        public TimeFloatUnit(final Serializable id, final FloatDuration initialTimeStep) throws SimRuntimeException
        {
            super(id, initialTimeStep);
        }

        /** {@inheritDoc} */
        @SuppressWarnings("unchecked")
        @Override
        public Replication.TimeFloatUnit<? extends DESSSimulatorInterface.TimeFloatUnit> getReplication()
        {
            return (Replication.TimeFloatUnit<? extends DESSSimulatorInterface.TimeFloatUnit>) super.getReplication();
        }
    }

}
