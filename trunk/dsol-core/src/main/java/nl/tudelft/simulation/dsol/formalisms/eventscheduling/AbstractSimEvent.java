/*
 * @(#)SimEvent.java Feb 1, 2003 Copyright (c) 2002-2005 Delft University of
 * Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved.
 * This software is proprietary information of Delft University of Technology
 * 
 */
package nl.tudelft.simulation.dsol.formalisms.eventscheduling;

import java.io.Serializable;

import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.simtime.SimTime;

/**
 * The AbstractSimEvent forms the basement for SimEvents and defines a compare method by which eventLists can compare
 * priority of the event.
 * <p>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl"> www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="http://www.peter-jacobs.com">Peter Jacobs </a>
 * @version $Revision: 1.1 $ $Date: 2010/08/10 11:36:43 $
 * @param <T> the type of simulation time, e.g. SimTimeCalendarLong or SimTimeDouble or SimTimeDoubleUnit.
 * @since 1.5
 */
public abstract class AbstractSimEvent<T extends SimTime<?, ?, T>> implements SimEventInterface<T>, Comparable<AbstractSimEvent<T>>, Serializable
{
    /** */
    private static final long serialVersionUID = 20140804L;

    /** a counter counting the number of constructed simEvents */
    private static long constructorCounter = 0L;

    /** absoluteExecutionTime reflects the time at which the event is scheduled */
    protected T absoluteExecutionTime;

    /** priority reflects the priority of the event */
    protected short priority = SimEventInterface.NORMAL_PRIORITY;

    /** the id used in compare statements */
    protected long id = 0L;

    /**
     * The constuctor of the event stores the time the event must be executed and the object and method to invoke
     * @param executionTime reflects the time the event has to be executed.
     */
    public AbstractSimEvent(final T executionTime)
    {
        this(executionTime, SimEventInterface.NORMAL_PRIORITY);
    }

    /**
     * The constuctor of the event stores the time the event must be executed and the object and method to invoke
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
        AbstractSimEvent.constructorCounter++;
        this.id = AbstractSimEvent.constructorCounter;
    }

    /**
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(final AbstractSimEvent<T> simEvent)
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

    /**
     * executes the simEvent
     * @throws SimRuntimeException on execution failure
     */
    public abstract void execute() throws SimRuntimeException;

    /**
     * @return The execution time of a simulation event
     */
    public T getAbsoluteExecutionTime()
    {
        return this.absoluteExecutionTime;
    }

    /**
     * @return The priority of a simulation event. The priorities are programmed according to the Java thread priority.
     *         Use 10 (MAX_PRIORITY), -9, .. , 5 (NORMAL_PRIORITY), 1(MIN_PRIORITY)
     */
    public short getPriority()
    {
        return this.priority;
    }
}