package nl.tudelft.simulation.dsol.simulators;

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

/**
 * The DESS defines the interface of the DESS simulator. DESS stands for the Differential Equation System Specification.
 * More information on Modeling and Simulation can be found in "Theory of Modeling and Simulation" by Bernard Zeigler
 * et. al. <br>
 * (c) 2002-2018 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:36:44 $
 * @author Peter Jacobs, Alexander Verbraeck
 * @param <A> the absolute storage type for the simulation time, e.g. Calendar, Duration, or Double.
 * @param <R> the relative type for time storage, e.g. Long for the Calendar. For most non-calendar types, the absolute
 *            and relative types are the same.
 * @param <T> the extended type itself to be able to implement a comparator on the simulation time.
 */
public class DESSSimulator<A extends Comparable<A>, R extends Number & Comparable<R>, T extends SimTime<A, R, T>>
        extends Simulator<A, R, T> implements DESSSimulatorInterface<A, R, T>
{
    /** */
    private static final long serialVersionUID = 20140805L;

    /** timeStep represents the timestep of the DESS simulator. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected R timeStep;

    /**
     * Construct a DESSSimulator with an initial time step for the integration process.
     * @param initialTimeStep the initial time step to use in the integration.
     * @throws SimRuntimeException when initialTimeStep &lt;=0, NaN, or Infinity
     */
    public DESSSimulator(final R initialTimeStep) throws SimRuntimeException
    {
        super();
        setTimeStep(initialTimeStep);
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("checkstyle:designforextension")
    public void initialize(final Replication<A, R, T> initReplication, final ReplicationMode replicationMode)
            throws SimRuntimeException
    {
        super.initialize(initReplication, replicationMode);
        this.replication.getTreatment().getExperiment().getModel().constructModel(this);
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
    @SuppressWarnings("checkstyle:designforextension")
    public void run()
    {
        while (this.simulatorTime.lt(this.replication.getTreatment().getEndSimTime()) && isRunning())
        {
            synchronized (super.semaphore)
            {
                this.simulatorTime = this.simulatorTime.plus(this.timeStep);
                if (this.simulatorTime.gt(this.replication.getTreatment().getEndSimTime()))
                {
                    this.simulatorTime = this.replication.getTreatment().getEndSimTime().copy();
                    this.stop();
                }
                this.fireTimedEvent(SimulatorInterface.TIME_CHANGED_EVENT, this.simulatorTime,
                        this.simulatorTime.get());
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
         * @param initialTimeStep the initial time step to use in the integration.
         * @throws SimRuntimeException when initialTimeStep &lt;=0, NaN, or Infinity
         */
        public TimeDouble(final Double initialTimeStep) throws SimRuntimeException
        {
            super(initialTimeStep);
        }
    }

    /** Easy access class DESSSimulator.TimeFloat. */
    public static class TimeFloat extends DESSSimulator<Float, Float, SimTimeFloat>
            implements DESSSimulatorInterface.TimeFloat
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /**
         * @param initialTimeStep the initial time step to use in the integration.
         * @throws SimRuntimeException when initialTimeStep &lt;=0, NaN, or Infinity
         */
        public TimeFloat(final Float initialTimeStep) throws SimRuntimeException
        {
            super(initialTimeStep);
        }
    }

    /** Easy access class DESSSimulator.TimeLong. */
    public static class TimeLong extends DESSSimulator<Long, Long, SimTimeLong>
            implements DESSSimulatorInterface.TimeLong
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /**
         * @param initialTimeStep the initial time step to use in the integration.
         * @throws SimRuntimeException when initialTimeStep &lt;=0, NaN, or Infinity
         */
        public TimeLong(final Long initialTimeStep) throws SimRuntimeException
        {
            super(initialTimeStep);
        }
    }

    /** Easy access class DESSSimulator.TimeDoubleUnit. */
    public static class TimeDoubleUnit extends DESSSimulator<Time, Duration, SimTimeDoubleUnit>
            implements DESSSimulatorInterface.TimeDoubleUnit
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /**
         * @param initialTimeStep the initial time step to use in the integration.
         * @throws SimRuntimeException when initialTimeStep &lt;=0, NaN, or Infinity
         */
        public TimeDoubleUnit(final Duration initialTimeStep) throws SimRuntimeException
        {
            super(initialTimeStep);
        }
    }

    /** Easy access class DESSSimulator.TimeFloatUnit. */
    public static class TimeFloatUnit extends DESSSimulator<FloatTime, FloatDuration, SimTimeFloatUnit>
            implements DESSSimulatorInterface.TimeFloatUnit
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /**
         * @param initialTimeStep the initial time step to use in the integration.
         * @throws SimRuntimeException when initialTimeStep &lt;=0, NaN, or Infinity
         */
        public TimeFloatUnit(final FloatDuration initialTimeStep) throws SimRuntimeException
        {
            super(initialTimeStep);
        }
    }

    /** Easy access class DESSSimulator.CalendarDouble. */
    public static class CalendarDouble extends DESSSimulator<Calendar, Duration, SimTimeCalendarDouble>
            implements DESSSimulatorInterface.CalendarDouble
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /**
         * @param initialTimeStep the initial time step to use in the integration.
         * @throws SimRuntimeException when initialTimeStep &lt;=0, NaN, or Infinity
         */
        public CalendarDouble(final Duration initialTimeStep) throws SimRuntimeException
        {
            super(initialTimeStep);
        }
    }

    /** Easy access class DESSSimulator.CalendarFloat. */
    public static class CalendarFloat extends DESSSimulator<Calendar, FloatDuration, SimTimeCalendarFloat>
            implements DESSSimulatorInterface.CalendarFloat
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /**
         * @param initialTimeStep the initial time step to use in the integration.
         * @throws SimRuntimeException when initialTimeStep &lt;=0, NaN, or Infinity
         */
        public CalendarFloat(final FloatDuration initialTimeStep) throws SimRuntimeException
        {
            super(initialTimeStep);
        }
    }

    /** Easy access class DESSSimulator.CalendarLong. */
    public static class CalendarLong extends DESSSimulator<Calendar, Long, SimTimeCalendarLong>
            implements DESSSimulatorInterface.CalendarLong
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /**
         * @param initialTimeStep the initial time step to use in the integration.
         * @throws SimRuntimeException when initialTimeStep &lt;=0, NaN, or Infinity
         */
        public CalendarLong(final Long initialTimeStep) throws SimRuntimeException
        {
            super(initialTimeStep);
        }
    }

}
