package nl.tudelft.simulation.dsol.formalisms.flow;

import java.lang.reflect.Constructor;
import java.rmi.RemoteException;
import java.util.Calendar;

import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.formalisms.eventscheduling.SimEvent;
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
import nl.tudelft.simulation.dsol.simtime.dist.DistContinuousSimTime;
import nl.tudelft.simulation.dsol.simtime.dist.DistContinuousTime;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;
import nl.tudelft.simulation.event.EventType;
import nl.tudelft.simulation.jstats.distributions.DistDiscrete;
import nl.tudelft.simulation.language.reflection.ClassUtil;
import nl.tudelft.simulation.language.reflection.SerializableConstructor;
import nl.tudelft.simulation.logger.Logger;

/**
 * This class defines a generator <br>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:36:44 $
 * @author Peter Jacobs, Alexander Verbraeck
 * @param <A> the absolute storage type for the simulation time, e.g. Calendar, UnitTimeDouble, or Double.
 * @param <R> the relative type for time storage, e.g. Long for the Calendar. For most non-calendar types, the absolute
 *            and relative types are the same.
 * @param <T> the extended type itself to be able to implement a comparator on the simulation time.
 */
public class Generator<A extends Comparable<A>, R extends Number & Comparable<R>, T extends SimTime<A, R, T>> extends
        Station<A, R, T>
{
    /** */
    public static final long serialVersionUID = 20140805L;

    /** CREATE_EVENT is fired on creation. */
    public static final EventType CREATE_EVENT = new EventType("CREATE_EVENT");

    /** constructorArguments refer to the arguments of the class invoked by the generator. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected Object[] constructorArguments;

    /** interval defines the inter-construction time. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected DistContinuousTime<R> interval;

    /** startTime defines the absolute startTime for the generator. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected DistContinuousSimTime<A, R, T> startTime;

    /** batchsize refers to the number of objects constructed. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected DistDiscrete batchSize;

    /** constructor refers to the constructor to be invoked. */
    private SerializableConstructor constructor;

    /** maxNumber is the max number of objects to be created. -1=Long.infinity. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected long maxNumber = -1;

    /** number refers to the currently constructed number. */
    private long number = 0;

    /** nextEvent is an internal variable that refers to the next simEvent. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected SimEvent<T> nextEvent = null;

    /**
     * constructs a new generator for objects in a simulation. Constructed objects are sent to the 'destination' of the
     * Generator when a destination has been indicated with the setDestination method. This constructor has a maximum
     * number of entities generated, which results in stopping the generator when the maximum number of entities has
     * been reached.
     * @param simulator is the on which the construction of the objects must be scheduled.
     * @param myClass is the class of which entities are created
     * @param constructorArguments are the parameters for the constructor of myClass. of arguments.
     *            <code>constructorArgument[n]=new Integer(12)</code> may have constructorArgumentClasses[n]=int.class;
     * @throws SimRuntimeException on constructor invocation.
     */
    public Generator(final DEVSSimulatorInterface<A, R, T> simulator, final Class<?> myClass,
            final Object[] constructorArguments) throws SimRuntimeException
    {
        super(simulator);
        try
        {
            Constructor<?> c = ClassUtil.resolveConstructor(myClass, constructorArguments);
            this.constructor = new SerializableConstructor(c);
        }
        catch (Exception exception)
        {
            throw new SimRuntimeException(exception);
        }
        this.constructorArguments = constructorArguments;
    }

    /**
     * generates a new entity with the basic constructorArguments.
     * @throws SimRuntimeException on construction failure
     */
    public final void generate() throws SimRuntimeException
    {
        this.generate(this.constructorArguments);
    }

    /**
     * generates a new entity.
     * @param specialConstructorArguments are the parameters used in the constructor.
     * @throws SimRuntimeException on construction failure
     */
    public final synchronized void generate(final Object[] specialConstructorArguments) throws SimRuntimeException
    {
        try
        {
            if (this.maxNumber == -1 || this.number < this.maxNumber)
            {
                this.number++;
                for (int i = 0; i < this.batchSize.draw(); i++)
                {
                    Object object = this.constructor.deSerialize().newInstance(specialConstructorArguments);
                    Logger.finest(this, "generate", "created " + this.number + "th instance of "
                            + this.constructor.deSerialize().getDeclaringClass());
                    this.fireEvent(Generator.CREATE_EVENT, 1);
                    this.releaseObject(object);
                }
                this.nextEvent =
                        new SimEvent<T>(this.simulator.getSimulatorTime().plus(this.interval.draw()), this, this,
                                "generate", null);
                this.simulator.scheduleEvent(this.nextEvent);
            }
        }
        catch (Exception exception)
        {
            throw new SimRuntimeException(exception);
        }
    }

    /** {@inheritDoc} */
    @Override
    public final void receiveObject(final Object object)
    {
        try
        {
            this.releaseObject(object);
        }
        catch (RemoteException remoteException)
        {
            Logger.warning(this, "receiveObject", remoteException);
        }
    }

    /**
     * returns the batchSize.
     * @return DistDiscrete
     */
    public final DistDiscrete getBatchSize()
    {
        return this.batchSize;
    }

    /**
     * returns the interarrival interval.
     * @return DistContinuous
     */
    @SuppressWarnings("checkstyle:designforextension")
    public DistContinuousTime<R> getInterval()
    {
        return this.interval;
    }

    /**
     * returns the maximum number of entities to be created.
     * @return long the maxNumber
     */
    public final long getMaxNumber()
    {
        return this.maxNumber;
    }

    /**
     * sets the batchsize of the generator.
     * @param batchSize is the number of entities simultaneously constructed
     */
    public final void setBatchSize(final DistDiscrete batchSize)
    {
        this.batchSize = batchSize;
    }

    /**
     * sets the interarrival distribution.
     * @param interval is the interarrival time
     */
    @SuppressWarnings("checkstyle:designforextension")
    public void setInterval(final DistContinuousTime<R> interval)
    {
        this.interval = interval;
    }

    /**
     * sets the maximum number of entities to be created.
     * @param maxNumber is the maxNumber
     */
    public final void setMaxNumber(final long maxNumber)
    {
        this.maxNumber = maxNumber;
    }

    /**
     * returns the startTime of the generator.
     * @return DistContinuous
     */
    @SuppressWarnings("checkstyle:designforextension")
    public DistContinuousSimTime<A, R, T> getStartTime()
    {
        return this.startTime;
    }

    /**
     * sets the startTime.
     * @param startTime is the absolute startTime
     */
    @SuppressWarnings("checkstyle:designforextension")
    public synchronized void setStartTime(final DistContinuousSimTime<A, R, T> startTime)
    {
        this.startTime = startTime;
        try
        {
            this.nextEvent = new SimEvent<T>(startTime.draw(), this, this, "generate", null);
            this.simulator.scheduleEvent(this.nextEvent);
        }
        catch (Exception exception)
        {
            Logger.warning(this, "setStartTime", exception);
        }
    }

    /***********************************************************************************************************/
    /************************************* EASY ACCESS CLASS EXTENSIONS ****************************************/
    /***********************************************************************************************************/

    /** Easy access class Generator.TimeDouble. */
    public static class TimeDouble extends Generator<Double, Double, SimTimeDouble>
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * Constructs a new generator for objects in a simulation. Constructed objects are sent to the 'destination' of
         * the Generator when a destination has been indicated with the setDestination method. This constructor has a
         * maximum number of entities generated, which results in stopping the generator when the maximum number of
         * entities has been reached.
         * @param simulator is the on which the construction of the objects must be scheduled.
         * @param myClass is the class of which entities are created
         * @param constructorArguments are the parameters for the constructor of myClass. of arguments.
         *            <code>constructorArgument[n]=new Integer(12)</code> may have
         *            constructorArgumentClasses[n]=int.class;
         * @throws SimRuntimeException on constructor invocation.
         */
        public TimeDouble(final DEVSSimulatorInterface.TimeDouble simulator, final Class<?> myClass,
                final Object[] constructorArguments) throws SimRuntimeException
        {
            super(simulator, myClass, constructorArguments);
        }

        /**
         * sets the interarrival distribution.
         * @param interval is the interarrival time
         */
        public final void setInterval(final DistContinuousTime.TimeDouble interval)
        {
            super.setInterval(interval);
        }

        /** {@inheritDoc} */
        @Override
        public final DistContinuousTime.TimeDouble getInterval()
        {
            return (DistContinuousTime.TimeDouble) this.interval;
        }

        /**
         * sets the startTime.
         * @param startTime is the absolute startTime
         */
        public final synchronized void setStartTime(final DistContinuousSimTime.TimeDouble startTime)
        {
            super.setStartTime(startTime);
        }

        /** {@inheritDoc} */
        @Override
        public final DistContinuousSimTime.TimeDouble getStartTime()
        {
            return (DistContinuousSimTime.TimeDouble) this.startTime;
        }
    }

    /** Easy access class Generator.TimeFloat. */
    public static class TimeFloat extends Generator<Float, Float, SimTimeFloat>
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * Constructs a new generator for objects in a simulation. Constructed objects are sent to the 'destination' of
         * the Generator when a destination has been indicated with the setDestination method. This constructor has a
         * maximum number of entities generated, which results in stopping the generator when the maximum number of
         * entities has been reached.
         * @param simulator is the on which the construction of the objects must be scheduled.
         * @param myClass is the class of which entities are created
         * @param constructorArguments are the parameters for the constructor of myClass. of arguments.
         *            <code>constructorArgument[n]=new Integer(12)</code> may have
         *            constructorArgumentClasses[n]=int.class;
         * @throws SimRuntimeException on constructor invocation.
         */
        public TimeFloat(final DEVSSimulatorInterface.TimeFloat simulator, final Class<?> myClass,
                final Object[] constructorArguments) throws SimRuntimeException
        {
            super(simulator, myClass, constructorArguments);
        }

        /**
         * sets the interarrival distribution.
         * @param interval is the interarrival time
         */
        public final void setInterval(final DistContinuousTime.TimeFloat interval)
        {
            super.setInterval(interval);
        }

        /** {@inheritDoc} */
        @Override
        public final DistContinuousTime.TimeFloat getInterval()
        {
            return (DistContinuousTime.TimeFloat) this.interval;
        }

        /**
         * sets the startTime.
         * @param startTime is the absolute startTime
         */
        public final synchronized void setStartTime(final DistContinuousSimTime.TimeFloat startTime)
        {
            super.setStartTime(startTime);
        }

        /** {@inheritDoc} */
        @Override
        public final DistContinuousSimTime.TimeFloat getStartTime()
        {
            return (DistContinuousSimTime.TimeFloat) this.startTime;
        }
    }

    /** Easy access class Generator.TimeLong. */
    public static class TimeLong extends Generator<Long, Long, SimTimeLong>
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * Constructs a new generator for objects in a simulation. Constructed objects are sent to the 'destination' of
         * the Generator when a destination has been indicated with the setDestination method. This constructor has a
         * maximum number of entities generated, which results in stopping the generator when the maximum number of
         * entities has been reached.
         * @param simulator is the on which the construction of the objects must be scheduled.
         * @param myClass is the class of which entities are created
         * @param constructorArguments are the parameters for the constructor of myClass. of arguments.
         *            <code>constructorArgument[n]=new Integer(12)</code> may have
         *            constructorArgumentClasses[n]=int.class;
         * @throws SimRuntimeException on constructor invocation.
         */
        public TimeLong(final DEVSSimulatorInterface.TimeLong simulator, final Class<?> myClass,
                final Object[] constructorArguments) throws SimRuntimeException
        {
            super(simulator, myClass, constructorArguments);
        }

        /**
         * sets the interarrival distribution.
         * @param interval is the interarrival time
         */
        public final void setInterval(final DistContinuousTime.TimeLong interval)
        {
            super.setInterval(interval);
        }

        /** {@inheritDoc} */
        @Override
        public final DistContinuousTime.TimeLong getInterval()
        {
            return (DistContinuousTime.TimeLong) this.interval;
        }

        /**
         * sets the startTime.
         * @param startTime is the absolute startTime
         */
        public final synchronized void setStartTime(final DistContinuousSimTime.TimeLong startTime)
        {
            super.setStartTime(startTime);
        }

        /** {@inheritDoc} */
        @Override
        public final DistContinuousSimTime.TimeLong getStartTime()
        {
            return (DistContinuousSimTime.TimeLong) this.startTime;
        }
    }

    /** Easy access class Generator.TimeDoubleUnit. */
    public static class TimeDoubleUnit extends Generator<UnitTimeDouble, UnitTimeDouble, SimTimeDoubleUnit>
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * Constructs a new generator for objects in a simulation. Constructed objects are sent to the 'destination' of
         * the Generator when a destination has been indicated with the setDestination method. This constructor has a
         * maximum number of entities generated, which results in stopping the generator when the maximum number of
         * entities has been reached.
         * @param simulator is the on which the construction of the objects must be scheduled.
         * @param myClass is the class of which entities are created
         * @param constructorArguments are the parameters for the constructor of myClass. of arguments.
         *            <code>constructorArgument[n]=new Integer(12)</code> may have
         *            constructorArgumentClasses[n]=int.class;
         * @throws SimRuntimeException on constructor invocation.
         */
        public TimeDoubleUnit(final DEVSSimulatorInterface.TimeDoubleUnit simulator, final Class<?> myClass,
                final Object[] constructorArguments) throws SimRuntimeException
        {
            super(simulator, myClass, constructorArguments);
        }

        /**
         * sets the interarrival distribution.
         * @param interval is the interarrival time
         */
        public final void setInterval(final DistContinuousTime.TimeDoubleUnit interval)
        {
            super.setInterval(interval);
        }

        /** {@inheritDoc} */
        @Override
        public final DistContinuousTime.TimeDoubleUnit getInterval()
        {
            return (DistContinuousTime.TimeDoubleUnit) this.interval;
        }

        /**
         * sets the startTime.
         * @param startTime is the absolute startTime
         */
        public final synchronized void setStartTime(final DistContinuousSimTime.TimeDoubleUnit startTime)
        {
            super.setStartTime(startTime);
        }

        /** {@inheritDoc} */
        @Override
        public final DistContinuousSimTime.TimeDoubleUnit getStartTime()
        {
            return (DistContinuousSimTime.TimeDoubleUnit) this.startTime;
        }
    }

    /** Easy access class Generator.TimeDoubleUnitUnit. */
    public static class TimeFloatUnit extends Generator<UnitTimeFloat, UnitTimeFloat, SimTimeFloatUnit>
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * Constructs a new generator for objects in a simulation. Constructed objects are sent to the 'destination' of
         * the Generator when a destination has been indicated with the setDestination method. This constructor has a
         * maximum number of entities generated, which results in stopping the generator when the maximum number of
         * entities has been reached.
         * @param simulator is the on which the construction of the objects must be scheduled.
         * @param myClass is the class of which entities are created
         * @param constructorArguments are the parameters for the constructor of myClass. of arguments.
         *            <code>constructorArgument[n]=new Integer(12)</code> may have
         *            constructorArgumentClasses[n]=int.class;
         * @throws SimRuntimeException on constructor invocation.
         */
        public TimeFloatUnit(final DEVSSimulatorInterface.TimeFloatUnit simulator, final Class<?> myClass,
                final Object[] constructorArguments) throws SimRuntimeException
        {
            super(simulator, myClass, constructorArguments);
        }

        /**
         * sets the interarrival distribution.
         * @param interval is the interarrival time
         */
        public final void setInterval(final DistContinuousTime.TimeFloatUnit interval)
        {
            super.setInterval(interval);
        }

        /** {@inheritDoc} */
        @Override
        public final DistContinuousTime.TimeFloatUnit getInterval()
        {
            return (DistContinuousTime.TimeFloatUnit) this.interval;
        }

        /**
         * sets the startTime.
         * @param startTime is the absolute startTime
         */
        public final synchronized void setStartTime(final DistContinuousSimTime.TimeFloatUnit startTime)
        {
            super.setStartTime(startTime);
        }

        /** {@inheritDoc} */
        @Override
        public final DistContinuousSimTime.TimeFloatUnit getStartTime()
        {
            return (DistContinuousSimTime.TimeFloatUnit) this.startTime;
        }
    }

    /** Easy access class Generator.TimeLongUnit. */
    public static class TimeLongUnit extends Generator<UnitTimeLong, UnitTimeLong, SimTimeLongUnit>
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * Constructs a new generator for objects in a simulation. Constructed objects are sent to the 'destination' of
         * the Generator when a destination has been indicated with the setDestination method. This constructor has a
         * maximum number of entities generated, which results in stopping the generator when the maximum number of
         * entities has been reached.
         * @param simulator is the on which the construction of the objects must be scheduled.
         * @param myClass is the class of which entities are created
         * @param constructorArguments are the parameters for the constructor of myClass. of arguments.
         *            <code>constructorArgument[n]=new Integer(12)</code> may have
         *            constructorArgumentClasses[n]=int.class;
         * @throws SimRuntimeException on constructor invocation.
         */
        public TimeLongUnit(final DEVSSimulatorInterface.TimeLongUnit simulator, final Class<?> myClass,
                final Object[] constructorArguments) throws SimRuntimeException
        {
            super(simulator, myClass, constructorArguments);
        }

        /**
         * sets the interarrival distribution.
         * @param interval is the interarrival time
         */
        public final void setInterval(final DistContinuousTime.TimeLongUnit interval)
        {
            super.setInterval(interval);
        }

        /** {@inheritDoc} */
        @Override
        public final DistContinuousTime.TimeLongUnit getInterval()
        {
            return (DistContinuousTime.TimeLongUnit) this.interval;
        }

        /**
         * sets the startTime.
         * @param startTime is the absolute startTime
         */
        public final synchronized void setStartTime(final DistContinuousSimTime.TimeLongUnit startTime)
        {
            super.setStartTime(startTime);
        }

        /** {@inheritDoc} */
        @Override
        public final DistContinuousSimTime.TimeLongUnit getStartTime()
        {
            return (DistContinuousSimTime.TimeLongUnit) this.startTime;
        }
    }

    /** Easy access class Generator.CalendarDouble. */
    public static class CalendarDouble extends Generator<Calendar, UnitTimeDouble, SimTimeCalendarDouble>
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * Constructs a new generator for objects in a simulation. Constructed objects are sent to the 'destination' of
         * the Generator when a destination has been indicated with the setDestination method. This constructor has a
         * maximum number of entities generated, which results in stopping the generator when the maximum number of
         * entities has been reached.
         * @param simulator is the on which the construction of the objects must be scheduled.
         * @param myClass is the class of which entities are created
         * @param constructorArguments are the parameters for the constructor of myClass. of arguments.
         *            <code>constructorArgument[n]=new Integer(12)</code> may have
         *            constructorArgumentClasses[n]=int.class;
         * @throws SimRuntimeException on constructor invocation.
         */
        public CalendarDouble(final DEVSSimulatorInterface.CalendarDouble simulator, final Class<?> myClass,
                final Object[] constructorArguments) throws SimRuntimeException
        {
            super(simulator, myClass, constructorArguments);
        }

        /**
         * sets the interarrival distribution.
         * @param interval is the interarrival time
         */
        public final void setInterval(final DistContinuousTime.CalendarDouble interval)
        {
            super.setInterval(interval);
        }

        /** {@inheritDoc} */
        @Override
        public final DistContinuousTime.CalendarDouble getInterval()
        {
            return (DistContinuousTime.CalendarDouble) this.interval;
        }

        /**
         * sets the startTime.
         * @param startTime is the absolute startTime
         */
        public final synchronized void setStartTime(final DistContinuousSimTime.CalendarDouble startTime)
        {
            super.setStartTime(startTime);
        }

        /** {@inheritDoc} */
        @Override
        public final DistContinuousSimTime.CalendarDouble getStartTime()
        {
            return (DistContinuousSimTime.CalendarDouble) this.startTime;
        }
    }

    /** Easy access class Generator.CalendarFloat. */
    public static class CalendarFloat extends Generator<Calendar, UnitTimeFloat, SimTimeCalendarFloat>
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * Constructs a new generator for objects in a simulation. Constructed objects are sent to the 'destination' of
         * the Generator when a destination has been indicated with the setDestination method. This constructor has a
         * maximum number of entities generated, which results in stopping the generator when the maximum number of
         * entities has been reached.
         * @param simulator is the on which the construction of the objects must be scheduled.
         * @param myClass is the class of which entities are created
         * @param constructorArguments are the parameters for the constructor of myClass. of arguments.
         *            <code>constructorArgument[n]=new Integer(12)</code> may have
         *            constructorArgumentClasses[n]=int.class;
         * @throws SimRuntimeException on constructor invocation.
         */
        public CalendarFloat(final DEVSSimulatorInterface.CalendarFloat simulator, final Class<?> myClass,
                final Object[] constructorArguments) throws SimRuntimeException
        {
            super(simulator, myClass, constructorArguments);
        }

        /**
         * sets the interarrival distribution.
         * @param interval is the interarrival time
         */
        public final void setInterval(final DistContinuousTime.CalendarFloat interval)
        {
            super.setInterval(interval);
        }

        /** {@inheritDoc} */
        @Override
        public final DistContinuousTime.CalendarFloat getInterval()
        {
            return (DistContinuousTime.CalendarFloat) this.interval;
        }

        /**
         * sets the startTime.
         * @param startTime is the absolute startTime
         */
        public final synchronized void setStartTime(final DistContinuousSimTime.CalendarFloat startTime)
        {
            super.setStartTime(startTime);
        }

        /** {@inheritDoc} */
        @Override
        public final DistContinuousSimTime.CalendarFloat getStartTime()
        {
            return (DistContinuousSimTime.CalendarFloat) this.startTime;
        }
    }

    /** Easy access class Generator.CalendarLong. */
    public static class CalendarLong extends Generator<Calendar, UnitTimeLong, SimTimeCalendarLong>
    {
        /** */
        private static final long serialVersionUID = 20150422L;

        /**
         * Constructs a new generator for objects in a simulation. Constructed objects are sent to the 'destination' of
         * the Generator when a destination has been indicated with the setDestination method. This constructor has a
         * maximum number of entities generated, which results in stopping the generator when the maximum number of
         * entities has been reached.
         * @param simulator is the on which the construction of the objects must be scheduled.
         * @param myClass is the class of which entities are created
         * @param constructorArguments are the parameters for the constructor of myClass. of arguments.
         *            <code>constructorArgument[n]=new Integer(12)</code> may have
         *            constructorArgumentClasses[n]=int.class;
         * @throws SimRuntimeException on constructor invocation.
         */
        public CalendarLong(final DEVSSimulatorInterface.CalendarLong simulator, final Class<?> myClass,
                final Object[] constructorArguments) throws SimRuntimeException
        {
            super(simulator, myClass, constructorArguments);
        }

        /**
         * sets the interarrival distribution.
         * @param interval is the interarrival time
         */
        public final void setInterval(final DistContinuousTime.CalendarLong interval)
        {
            super.setInterval(interval);
        }

        /** {@inheritDoc} */
        @Override
        public final DistContinuousTime.CalendarLong getInterval()
        {
            return (DistContinuousTime.CalendarLong) this.interval;
        }

        /**
         * sets the startTime.
         * @param startTime is the absolute startTime
         */
        public final synchronized void setStartTime(final DistContinuousSimTime.CalendarLong startTime)
        {
            super.setStartTime(startTime);
        }

        /** {@inheritDoc} */
        @Override
        public final DistContinuousSimTime.CalendarLong getStartTime()
        {
            return (DistContinuousSimTime.CalendarLong) this.startTime;
        }
    }

}
