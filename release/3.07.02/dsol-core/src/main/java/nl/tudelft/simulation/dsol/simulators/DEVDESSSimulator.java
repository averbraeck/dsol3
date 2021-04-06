package nl.tudelft.simulation.dsol.simulators;

import java.io.Serializable;

import org.djunits.value.vdouble.scalar.Duration;
import org.djunits.value.vdouble.scalar.Time;
import org.djunits.value.vfloat.scalar.FloatDuration;
import org.djunits.value.vfloat.scalar.FloatTime;

import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.experiment.ReplicationInterface;
import nl.tudelft.simulation.dsol.formalisms.eventscheduling.SimEventInterface;
import nl.tudelft.simulation.dsol.simtime.SimTime;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;
import nl.tudelft.simulation.dsol.simtime.SimTimeDoubleUnit;
import nl.tudelft.simulation.dsol.simtime.SimTimeFloat;
import nl.tudelft.simulation.dsol.simtime.SimTimeFloatUnit;
import nl.tudelft.simulation.dsol.simtime.SimTimeLong;

/**
 * The reference implementation of the DEVDESS simulator.
 * <p>
 * Copyright (c) 2002-2021 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @param <A> the absolute storage type for the simulation time, e.g. Time, Float, or Double.
 * @param <R> the relative type for time storage, e.g. Duration for absolute Time. For most non-unit types, the absolute and
 *            relative types are the same.
 * @param <T> the extended type itself to be able to implement a comparator on the simulation time.
 * @since 1.5
 */
public class DEVDESSSimulator<A extends Comparable<A> & Serializable, R extends Number & Comparable<R>,
        T extends SimTime<A, R, T>> extends DEVSSimulator<A, R, T> implements DEVDESSSimulatorInterface<A, R, T>
{
    /** */
    private static final long serialVersionUID = 20140805L;

    /** timeStep represents the timestep of the DESS simulator. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected R timeStep;

    /**
     * Construct a DEVDESSSimulator with an initial time step for the integration process.
     * @param initialTimeStep R; the initial time step to use in the integration.
     * @param id the id of the simulator, used in logging and firing of events.
     * @throws SimRuntimeException when initialTimeStep &lt;= 0, NaN, or Infinity
     */
    public DEVDESSSimulator(final Serializable id, final R initialTimeStep) throws SimRuntimeException
    {
        super(id);
        setTimeStep(initialTimeStep);
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
            if (!Double.isFinite(timeStep.doubleValue()) || timeStep.doubleValue() <= 0.0)
            {
                throw new SimRuntimeException("Timestep for DESSimulator has illegal value: " + timeStep);
            }
            this.timeStep = timeStep;
            this.fireEvent(TIME_STEP_CHANGED_EVENT, timeStep);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void run()
    {
        while (!isStoppingOrStopped() && !this.eventList.isEmpty() && this.simulatorTime.le(this.replication.getEndSimTime()))
        {
            synchronized (super.semaphore)
            {
                T runUntil = this.simulatorTime.plus(this.timeStep);
                while (!this.eventList.isEmpty() && !isStoppingOrStopped()
                        && runUntil.ge(this.eventList.first().getAbsoluteExecutionTime()))
                {
                    int cmp = this.eventList.first().getAbsoluteExecutionTime().compareTo(this.runUntilTime);
                    if ((cmp == 0 && !this.runUntilIncluding) || cmp > 0)
                    {
                        this.simulatorTime.set(this.runUntilTime.get());
                        this.runState = RunState.STOPPING;
                        break;
                    }

                    SimEventInterface<T> event = this.eventList.removeFirst();
                    if (event.getAbsoluteExecutionTime().ne(super.simulatorTime))
                    {
                        super.fireUnverifiedTimedEvent(SimulatorInterface.TIME_CHANGED_EVENT, null,
                                event.getAbsoluteExecutionTime().get());
                    }
                    this.simulatorTime = event.getAbsoluteExecutionTime();
                    try
                    {
                        event.execute();
                        if (this.eventList.isEmpty())
                        {
                            this.simulatorTime.set(this.runUntilTime.get());
                            this.runState = RunState.STOPPING;
                            break;
                        }
                    }
                    catch (Exception exception)
                    {
                        getLogger().always().error(exception);
                        if (this.isPauseOnError())
                        {
                            try
                            {
                                this.runState = RunState.STOPPING;
                                this.stop();
                            }
                            catch (SimRuntimeException stopException)
                            {
                                getLogger().always().error(stopException);
                            }
                        }
                    }
                }
                if (!isStoppingOrStopped())
                {
                    this.simulatorTime = runUntil;
                }
                this.fireTimedEvent(SimulatorInterface.TIME_CHANGED_EVENT, null, this.simulatorTime.get());
            }
        }
    }

    /***********************************************************************************************************/
    /************************************* EASY ACCESS CLASS EXTENSIONS ****************************************/
    /***********************************************************************************************************/

    /** Easy access class DEVDESSSimulator.TimeDouble. */
    public static class TimeDouble extends DEVDESSSimulator<Double, Double, SimTimeDouble>
            implements DEVDESSSimulatorInterface.TimeDouble
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /**
         * @param initialTimeStep Double; the initial time step to use in the integration.
         * @param id the id of the simulator, used in logging and firing of events.
         * @throws SimRuntimeException when initialTimeStep &lt;= 0, NaN, or Infinity
         */
        public TimeDouble(final Serializable id, final Double initialTimeStep) throws SimRuntimeException
        {
            super(id, initialTimeStep);
        }

        /** {@inheritDoc} */
        @Override
        public ReplicationInterface.TimeDouble getReplication()
        {
            return (ReplicationInterface.TimeDouble) super.getReplication();
        }
    }

    /** Easy access class DEVDESSSimulator.TimeFloat. */
    public static class TimeFloat extends DEVDESSSimulator<Float, Float, SimTimeFloat>
            implements DEVDESSSimulatorInterface.TimeFloat
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /**
         * @param initialTimeStep Float; the initial time step to use in the integration.
         * @param id the id of the simulator, used in logging and firing of events.
         * @throws SimRuntimeException when initialTimeStep &lt;= 0, NaN, or Infinity
         */
        public TimeFloat(final Serializable id, final Float initialTimeStep) throws SimRuntimeException
        {
            super(id, initialTimeStep);
        }

        /** {@inheritDoc} */
        @Override
        public ReplicationInterface.TimeFloat getReplication()
        {
            return (ReplicationInterface.TimeFloat) super.getReplication();
        }
    }

    /** Easy access class DEVDESSSimulator.TimeLong. */
    public static class TimeLong extends DEVDESSSimulator<Long, Long, SimTimeLong> implements DEVDESSSimulatorInterface.TimeLong
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /**
         * @param initialTimeStep Long; the initial time step to use in the integration.
         * @param id the id of the simulator, used in logging and firing of events.
         * @throws SimRuntimeException when initialTimeStep &lt;= 0, NaN, or Infinity
         */
        public TimeLong(final Serializable id, final Long initialTimeStep) throws SimRuntimeException
        {
            super(id, initialTimeStep);
        }

        /** {@inheritDoc} */
        @Override
        public ReplicationInterface.TimeLong getReplication()
        {
            return (ReplicationInterface.TimeLong) super.getReplication();
        }
    }

    /** Easy access class DEVDESSSimulator.TimeDoubleUnit. */
    public static class TimeDoubleUnit extends DEVDESSSimulator<Time, Duration, SimTimeDoubleUnit>
            implements DEVDESSSimulatorInterface.TimeDoubleUnit
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /**
         * @param initialTimeStep Duration; the initial time step to use in the integration.
         * @param id the id of the simulator, used in logging and firing of events.
         * @throws SimRuntimeException when initialTimeStep &lt;= 0, NaN, or Infinity
         */
        public TimeDoubleUnit(final Serializable id, final Duration initialTimeStep) throws SimRuntimeException
        {
            super(id, initialTimeStep);
        }

        /** {@inheritDoc} */
        @Override
        public ReplicationInterface.TimeDoubleUnit getReplication()
        {
            return (ReplicationInterface.TimeDoubleUnit) super.getReplication();
        }
    }

    /** Easy access class DEVDESSSimulator.TimeFloatUnit. */
    public static class TimeFloatUnit extends DEVDESSSimulator<FloatTime, FloatDuration, SimTimeFloatUnit>
            implements DEVDESSSimulatorInterface.TimeFloatUnit
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /**
         * @param initialTimeStep FloatDuration; the initial time step to use in the integration.
         * @param id the id of the simulator, used in logging and firing of events.
         * @throws SimRuntimeException when initialTimeStep &lt;= 0, NaN, or Infinity
         */
        public TimeFloatUnit(final Serializable id, final FloatDuration initialTimeStep) throws SimRuntimeException
        {
            super(id, initialTimeStep);
        }

        /** {@inheritDoc} */
        @Override
        public ReplicationInterface.TimeFloatUnit getReplication()
        {
            return (ReplicationInterface.TimeFloatUnit) super.getReplication();
        }
    }

}
