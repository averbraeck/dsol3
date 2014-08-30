package nl.tudelft.simulation.dsol.formalisms.flow;

import java.lang.reflect.Constructor;
import java.rmi.RemoteException;

import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.formalisms.eventscheduling.SimEvent;
import nl.tudelft.simulation.dsol.simtime.SimTime;
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
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>, <a
 *         href="mailto:a.verbraeck@tudelft.nl">Alexander Verbraeck </a>
 * @param <A> the absolute storage type for the simulation time, e.g. Calendar, UnitTimeDouble, or Double.
 * @param <R> the relative type for time storage, e.g. Long for the Calendar. For most non-calendar types, the absolute
 *            and relative types are the same.
 * @param <T> the extended type itself to be able to implement a comparator on the simulation time.
 */
public class Generator<A extends Comparable<A>, R extends Number & Comparable<R>, T extends SimTime<A, R, T>> extends
        Station<A, R, T>
{
    /** */
    private static final long serialVersionUID = 20140805L;

    /** CREATE_EVENT is fired on creation. */
    public static final EventType CREATE_EVENT = new EventType("CREATE_EVENT");

    /**
     * constructorArguments refer to the arguments invoked by the
     */
    protected Object[] constructorArguments;

    /**
     * interval defines the inter construction time
     */
    protected DistContinuousTime<R> interval;

    /**
     * startTime defines the absolute startTime for the generator
     */
    protected DistContinuousSimTime<A, R, T> startTime;

    /**
     * batchsize refers to the number of objects constructed
     */
    private DistDiscrete batchSize;

    /**
     * constructor refers to the constructor to be invoked
     */
    protected SerializableConstructor constructor;

    /**
     * maxNumber is the max number of objects to be created. -1=Long.infinity
     */
    private long maxNumber = -1;

    /** number refers to the currently constructed number. */
    private long number = 0;

    /**
     * nextEvent refers to the next simEvent
     */
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
     * @throws SimRuntimeException on constructor invokation.
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
     * generates a new entity with the basic constructorArguments
     * @throws SimRuntimeException on construction failure
     */
    public void generate() throws SimRuntimeException
    {
        this.generate(this.constructorArguments);
    }

    /**
     * generates a new entity
     * @param specialConstructorArguments are the parameters used in the constructor.
     * @throws SimRuntimeException on construction failure
     */
    public synchronized void generate(final Object[] specialConstructorArguments) throws SimRuntimeException
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
    public void receiveObject(final Object object)
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
     * returns the batchSize
     * @return DistDiscrete
     */
    public DistDiscrete getBatchSize()
    {
        return this.batchSize;
    }

    /**
     * returns the interarrival interval
     * @return DistContinuous
     */
    public DistContinuousTime<R> getInterval()
    {
        return this.interval;
    }

    /**
     * returns the maximum number of entities to be created
     * @return long the maxNumber
     */
    public long getMaxNumber()
    {
        return this.maxNumber;
    }

    /**
     * sets the batchsize of the generator
     * @param batchSize is the number of entities simultaniously constructed
     */
    public void setBatchSize(final DistDiscrete batchSize)
    {
        this.batchSize = batchSize;
    }

    /**
     * sets the interarrival distribution
     * @param interval is the interarrival time
     */
    public void setInterval(final DistContinuousTime<R> interval)
    {
        this.interval = interval;
    }

    /**
     * sets the maximum number of entities to be created
     * @param maxNumber is the maxNumber
     */
    public void setMaxNumber(final long maxNumber)
    {
        this.maxNumber = maxNumber;
    }

    /**
     * returns the startTime of the generator
     * @return DistContinuous
     */
    public DistContinuousSimTime<A, R, T> getStartTime()
    {
        return this.startTime;
    }

    /**
     * sets the startTime
     * @param startTime is the absolute startTime
     */
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

}
