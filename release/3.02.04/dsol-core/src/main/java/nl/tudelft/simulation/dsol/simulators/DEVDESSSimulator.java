package nl.tudelft.simulation.dsol.simulators;

import java.util.Calendar;

import org.djunits.value.vdouble.scalar.Duration;
import org.djunits.value.vdouble.scalar.Time;
import org.djunits.value.vfloat.scalar.FloatDuration;
import org.djunits.value.vfloat.scalar.FloatTime;

import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.formalisms.eventscheduling.SimEventInterface;
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
 * The reference implementation of the DEVDESS simulator.
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
public class DEVDESSSimulator<A extends Comparable<A>, R extends Number & Comparable<R>, T extends SimTime<A, R, T>>
        extends DEVSSimulator<A, R, T> implements DEVDESSSimulatorInterface<A, R, T>
{
    /** */
    private static final long serialVersionUID = 20140805L;

    /** timeStep represents the timestep of the DESS simulator. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected R timeStep;

    /**
     * Construct a DEVDESSSimulator with an initial time step for the integration process.
     * @param initialTimeStep the initial time step to use in the integration.
     * @throws SimRuntimeException when initialTimeStep &lt;= 0, NaN, or Infinity
     */
    public DEVDESSSimulator(final R initialTimeStep) throws SimRuntimeException
    {
        super();
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
    @SuppressWarnings("checkstyle:designforextension")
    public void run()
    {
        while (this.isRunning() && !this.eventList.isEmpty()
                && this.simulatorTime.le(this.replication.getTreatment().getEndSimTime()))
        {
            synchronized (super.semaphore)
            {
                T runUntil = this.simulatorTime.plus(this.timeStep);
                while (!this.eventList.isEmpty() && this.running
                        && runUntil.ge(this.eventList.first().getAbsoluteExecutionTime()))
                {
                    SimEventInterface<T> event = this.eventList.removeFirst();
                    if (event.getAbsoluteExecutionTime().ne(super.simulatorTime))
                    {
                        super.fireTimedEvent(SimulatorInterface.TIME_CHANGED_EVENT, event.getAbsoluteExecutionTime(),
                                event.getAbsoluteExecutionTime().get());
                    }
                    this.simulatorTime = event.getAbsoluteExecutionTime();
                    try
                    {
                        event.execute();
                    }
                    catch (Exception exception)
                    {
                        SimLogger.always().error(exception);
                        if (this.isPauseOnError())
                        {
                            try
                            {
                                this.stop();
                            }
                            catch (SimRuntimeException stopException)
                            {
                                SimLogger.always().error(stopException);
                            }
                        }
                    }
                }
                if (this.running)
                {
                    this.simulatorTime = runUntil;
                }
                this.fireTimedEvent(SimulatorInterface.TIME_CHANGED_EVENT, this.simulatorTime,
                        this.simulatorTime.get());
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
         * @param initialTimeStep the initial time step to use in the integration.
         * @throws SimRuntimeException when initialTimeStep &lt;= 0, NaN, or Infinity
         */
        public TimeDouble(final Double initialTimeStep) throws SimRuntimeException
        {
            super(initialTimeStep);
        }
    }

    /** Easy access class DEVDESSSimulator.TimeFloat. */
    public static class TimeFloat extends DEVDESSSimulator<Float, Float, SimTimeFloat>
            implements DEVDESSSimulatorInterface.TimeFloat
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /**
         * @param initialTimeStep the initial time step to use in the integration.
         * @throws SimRuntimeException when initialTimeStep &lt;= 0, NaN, or Infinity
         */
        public TimeFloat(final Float initialTimeStep) throws SimRuntimeException
        {
            super(initialTimeStep);
        }
    }

    /** Easy access class DEVDESSSimulator.TimeLong. */
    public static class TimeLong extends DEVDESSSimulator<Long, Long, SimTimeLong>
            implements DEVDESSSimulatorInterface.TimeLong
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /**
         * @param initialTimeStep the initial time step to use in the integration.
         * @throws SimRuntimeException when initialTimeStep &lt;= 0, NaN, or Infinity
         */
        public TimeLong(final Long initialTimeStep) throws SimRuntimeException
        {
            super(initialTimeStep);
        }
    }

    /** Easy access class DEVDESSSimulator.TimeDoubleUnit. */
    public static class TimeDoubleUnit extends DEVDESSSimulator<Time, Duration, SimTimeDoubleUnit>
            implements DEVDESSSimulatorInterface.TimeDoubleUnit
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /**
         * @param initialTimeStep the initial time step to use in the integration.
         * @throws SimRuntimeException when initialTimeStep &lt;= 0, NaN, or Infinity
         */
        public TimeDoubleUnit(final Duration initialTimeStep) throws SimRuntimeException
        {
            super(initialTimeStep);
        }
    }

    /** Easy access class DEVDESSSimulator.TimeFloatUnit. */
    public static class TimeFloatUnit extends DEVDESSSimulator<FloatTime, FloatDuration, SimTimeFloatUnit>
            implements DEVDESSSimulatorInterface.TimeFloatUnit
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /**
         * @param initialTimeStep the initial time step to use in the integration.
         * @throws SimRuntimeException when initialTimeStep &lt;= 0, NaN, or Infinity
         */
        public TimeFloatUnit(final FloatDuration initialTimeStep) throws SimRuntimeException
        {
            super(initialTimeStep);
        }
    }

    /** Easy access class DEVDESSSimulator.CalendarDouble. */
    public static class CalendarDouble extends DEVDESSSimulator<Calendar, Duration, SimTimeCalendarDouble>
            implements DEVDESSSimulatorInterface.CalendarDouble
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /**
         * @param initialTimeStep the initial time step to use in the integration.
         * @throws SimRuntimeException when initialTimeStep &lt;= 0, NaN, or Infinity
         */
        public CalendarDouble(final Duration initialTimeStep) throws SimRuntimeException
        {
            super(initialTimeStep);
        }
    }

    /** Easy access class DEVDESSSimulator.CalendarFloat. */
    public static class CalendarFloat extends DEVDESSSimulator<Calendar, FloatDuration, SimTimeCalendarFloat>
            implements DEVDESSSimulatorInterface.CalendarFloat
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /**
         * @param initialTimeStep the initial time step to use in the integration.
         * @throws SimRuntimeException when initialTimeStep &lt;= 0, NaN, or Infinity
         */
        public CalendarFloat(final FloatDuration initialTimeStep) throws SimRuntimeException
        {
            super(initialTimeStep);
        }
    }

    /** Easy access class DEVDESSSimulator.CalendarLong. */
    public static class CalendarLong extends DEVDESSSimulator<Calendar, Long, SimTimeCalendarLong>
            implements DEVDESSSimulatorInterface.CalendarLong
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /**
         * @param initialTimeStep the initial time step to use in the integration.
         * @throws SimRuntimeException when initialTimeStep &lt;= 0, NaN, or Infinity
         */
        public CalendarLong(final Long initialTimeStep) throws SimRuntimeException
        {
            super(initialTimeStep);
        }
    }

}
