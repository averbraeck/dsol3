package nl.tudelft.simulation.dsol.simulators;

import java.rmi.RemoteException;
import java.util.Calendar;

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
import nl.tudelft.simulation.dsol.simtime.SimTimeLongUnit;
import nl.tudelft.simulation.dsol.simtime.UnitTimeDouble;
import nl.tudelft.simulation.dsol.simtime.UnitTimeFloat;
import nl.tudelft.simulation.dsol.simtime.UnitTimeLong;

/**
 * The DESS defines the interface of the DESS simulator. DESS stands for the Differential Equation System Specification.
 * More information on Modeling & Simulation can be found in "Theory of Modeling and Simulation" by Bernard Zeigler et.
 * al. <br>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:36:44 $
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>, <a
 *         href="mailto:a.verbraeck@tudelft.nl">Alexander Verbraeck </a>
 * @param <A> the absolute storage type for the simulation time, e.g. Calendar, UnitTimeDouble, or Double.
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
    protected R timeStep;

    /**
     * Construct a DESSSimulator with an initial time step for the integration process.
     * @param initialTimeStep the initial time step to use in the integration.
     * @throws SimRuntimeException when initialTimeStep <=0, NaN, or Infinity
     */
    public DESSSimulator(final R initialTimeStep) throws SimRuntimeException
    {
        super();
        setTimeStep(initialTimeStep);
    }

    /** {@inheritDoc} */
    @Override
    public void initialize(final Replication<A, R, T> initReplication, final ReplicationMode replicationMode)
            throws RemoteException, SimRuntimeException
    {
        super.initialize(initReplication, replicationMode);
        this.replication.getTreatment().getExperiment().getModel().constructModel(this);
    }

    /** {@inheritDoc} */
    @Override
    public R getTimeStep()
    {
        return this.timeStep;
    }

    /** {@inheritDoc} */
    @Override
    public void setTimeStep(final R timeStep) throws SimRuntimeException
    {
        synchronized (super.semaphore)
        {
            if (timeStep.doubleValue() <= 0 || java.lang.Double.isNaN(timeStep.doubleValue())
                    || java.lang.Double.isInfinite(timeStep.doubleValue()))
                throw new SimRuntimeException(
                        "DESSSimulator.setTimeStep: timeStep <= 0, NaN, or Infinity. Value provided = : " + timeStep);
            this.timeStep = timeStep;
            this.fireEvent(TIME_STEP_CHANGED_EVENT, timeStep);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void run()
    {
        while (this.simulatorTime.lt(this.replication.getTreatment().getEndTime()) && isRunning())
        {
            synchronized (super.semaphore)
            {
                this.simulatorTime = this.simulatorTime.plus(this.timeStep);
                if (this.simulatorTime.gt(this.replication.getTreatment().getEndTime()))
                {
                    this.simulatorTime = this.replication.getTreatment().getEndTime().copy();
                    this.stop();
                }
                this.fireTimedEvent(SimulatorInterface.TIME_CHANGED_EVENT, this.simulatorTime, this.simulatorTime);
            }
        }
    }

    /***********************************************************************************************************/
    /************************************* EASY ACCESS CLASS EXTENSIONS ****************************************/
    /***********************************************************************************************************/

    /** Easy access class DESSSimulator.Double. */
    public static class Double extends DESSSimulator<java.lang.Double, java.lang.Double, SimTimeDouble> implements
            DESSSimulatorInterface.Double
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /**
         * @param initialTimeStep
         * @throws SimRuntimeException when initialTimeStep <=0, NaN, or Infinity
         */
        public Double(final java.lang.Double initialTimeStep) throws SimRuntimeException
        {
            super(initialTimeStep);
        }
    }

    /** Easy access class DESSSimulator.Float. */
    public static class Float extends DESSSimulator<java.lang.Float, java.lang.Float, SimTimeFloat> implements
            DESSSimulatorInterface.Float
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /**
         * @param initialTimeStep
         * @throws SimRuntimeException when initialTimeStep <=0, NaN, or Infinity
         */
        public Float(final java.lang.Float initialTimeStep) throws SimRuntimeException
        {
            super(initialTimeStep);
        }
    }

    /** Easy access class DESSSimulator.Long. */
    public static class Long extends DESSSimulator<java.lang.Long, java.lang.Long, SimTimeLong> implements
            DESSSimulatorInterface.Long
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /**
         * @param initialTimeStep
         * @throws SimRuntimeException when initialTimeStep <=0, NaN, or Infinity
         */
        public Long(final java.lang.Long initialTimeStep) throws SimRuntimeException
        {
            super(initialTimeStep);
        }
    }

    /** Easy access class DESSSimulator.DoubleUnit. */
    public static class DoubleUnit extends DESSSimulator<UnitTimeDouble, UnitTimeDouble, SimTimeDoubleUnit> implements
            DESSSimulatorInterface.DoubleUnit
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /**
         * @param initialTimeStep
         * @throws SimRuntimeException when initialTimeStep <=0, NaN, or Infinity
         */
        public DoubleUnit(final UnitTimeDouble initialTimeStep) throws SimRuntimeException
        {
            super(initialTimeStep);
        }
    }

    /** Easy access class DESSSimulator.FloatUnit. */
    public static class FloatUnit extends DESSSimulator<UnitTimeFloat, UnitTimeFloat, SimTimeFloatUnit> implements
            DESSSimulatorInterface.FloatUnit
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /**
         * @param initialTimeStep
         * @throws SimRuntimeException when initialTimeStep <=0, NaN, or Infinity
         */
        public FloatUnit(final UnitTimeFloat initialTimeStep) throws SimRuntimeException
        {
            super(initialTimeStep);
        }
    }

    /** Easy access class DESSSimulator.LongUnit. */
    public static class LongUnit extends DESSSimulator<UnitTimeLong, UnitTimeLong, SimTimeLongUnit> implements
            DESSSimulatorInterface.LongUnit
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /**
         * @param initialTimeStep
         * @throws SimRuntimeException when initialTimeStep <=0, NaN, or Infinity
         */
        public LongUnit(final UnitTimeLong initialTimeStep) throws SimRuntimeException
        {
            super(initialTimeStep);
        }
    }

    /** Easy access class DESSSimulator.CalendarDouble. */
    public static class CalendarDouble extends DESSSimulator<Calendar, UnitTimeDouble, SimTimeCalendarDouble> implements
            DESSSimulatorInterface.CalendarDouble
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /**
         * @param initialTimeStep
         * @throws SimRuntimeException when initialTimeStep <=0, NaN, or Infinity
         */
        public CalendarDouble(final UnitTimeDouble initialTimeStep) throws SimRuntimeException
        {
            super(initialTimeStep);
        }
    }

    /** Easy access class DESSSimulator.CalendarFloat. */
    public static class CalendarFloat extends DESSSimulator<Calendar, UnitTimeFloat, SimTimeCalendarFloat> implements
            DESSSimulatorInterface.CalendarFloat
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /**
         * @param initialTimeStep
         * @throws SimRuntimeException when initialTimeStep <=0, NaN, or Infinity
         */
        public CalendarFloat(final UnitTimeFloat initialTimeStep) throws SimRuntimeException
        {
            super(initialTimeStep);
        }
    }

    /** Easy access class DESSSimulator.CalendarLong. */
    public static class CalendarLong extends DESSSimulator<Calendar, UnitTimeLong, SimTimeCalendarLong> implements
            DESSSimulatorInterface.CalendarLong
    {
        /** */
        private static final long serialVersionUID = 20140805L;

        /**
         * @param initialTimeStep
         * @throws SimRuntimeException when initialTimeStep <=0, NaN, or Infinity
         */
        public CalendarLong(final UnitTimeLong initialTimeStep) throws SimRuntimeException
        {
            super(initialTimeStep);
        }
    }

}
