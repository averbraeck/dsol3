package nl.tudelft.simulation.dsol.formalisms.eventscheduling;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;

import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.simtime.SimTime;

/**
 * The AbstractSimEvent forms the basement for SimEvents and defines a compare method by which eventLists can compare
 * priority of the event.
 * <p>
 * (c) 2002-2018 <a href="https://simulation.tudelft.nl">Delft University of Technology </a>, the Netherlands. <br>
 * See for project information <a href="https://simulation.tudelft.nl"> www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @version $Revision: 1.1 $ $Date: 2010/08/10 11:36:43 $
 * @param <T> the type of simulation time, e.g. SimTimeCalendarLong or SimTimeDouble or SimTimeDoubleUnit.
 * @since 1.5
 */
public abstract class AbstractSimEvent<T extends SimTime<?, ?, T>>
        implements SimEventInterface<T>, Comparable<AbstractSimEvent<T>>, Serializable
{
    /** */
    private static final long serialVersionUID = 20140804L;

    /** a counter counting the number of constructed simEvents. */
    private static AtomicLong constructorCounter = new AtomicLong();

    /** absoluteExecutionTime reflects the time at which the event is scheduled. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected T absoluteExecutionTime;

    /** priority reflects the priority of the event. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected short priority = SimEventInterface.NORMAL_PRIORITY;

    /** the id used in compare statements. */
    @SuppressWarnings("checkstyle:visibilitymodifier")
    protected long id = 0L;

    /**
     * The constructor of the event stores the time the event must be executed and the object and method to invoke.
     * @param executionTime reflects the time the event has to be executed.
     */
    public AbstractSimEvent(final T executionTime)
    {
        this(executionTime, SimEventInterface.NORMAL_PRIORITY);
    }

    /**
     * The constructor of the event stores the time the event must be executed and the object and method to invoke.
     * @param executionTime reflects the time the event has to be executed.
     * @param priority reflects the priority of the event
     */
    public AbstractSimEvent(final T executionTime, final short priority)
    {
        this.absoluteExecutionTime = executionTime;
        if (priority < SimEventInterface.MIN_PRIORITY - 1 || priority > SimEventInterface.MAX_PRIORITY + 1)
        {
            throw new IllegalArgumentException("priority must be between [" + SimEventInterface.MIN_PRIORITY + ".."
                    + SimEventInterface.MAX_PRIORITY + "]");
        }
        this.priority = priority;

        this.id = AbstractSimEvent.constructorCounter.incrementAndGet();
    }

    /** {@inheritDoc} */
    @Override
    public final int compareTo(final AbstractSimEvent<T> simEvent)
    {
        if (this.equals(simEvent))
        {
            return 0;
        }
        if (this.absoluteExecutionTime.lt(simEvent.getAbsoluteExecutionTime()))
        {
            return -1;
        }
        if (this.absoluteExecutionTime.gt(simEvent.getAbsoluteExecutionTime()))
        {
            return 1;
        }
        if (this.priority < simEvent.getPriority())
        {
            return 1;
        }
        if (this.priority > simEvent.getPriority())
        {
            return -1;
        }
        if (this.id < simEvent.id)
        {
            return -1;
        }
        if (this.id > simEvent.id)
        {
            return 1;
        }
        throw new IllegalStateException("This may never occur! " + this + " !=" + simEvent + ". Almost returned 0");
    }

    /** {@inheritDoc} */
    @Override
    public abstract void execute() throws SimRuntimeException;

    /** {@inheritDoc} */
    @Override
    public final T getAbsoluteExecutionTime()
    {
        return this.absoluteExecutionTime;
    }

    /** {@inheritDoc} */
    @Override
    public final short getPriority()
    {
        return this.priority;
    }

}
