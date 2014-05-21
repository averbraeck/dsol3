/*
 * @(#)CounterTest.java Apr 3, 2003 Copyright (c) 2002-2005 Delft University of
 * Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved.
 * This software is proprietary information of Delft University of Technology
 * 
 */
package nl.tudelft.simulation.jstats.statistics;

import javax.swing.table.TableModel;

import nl.tudelft.simulation.event.Event;
import nl.tudelft.simulation.event.EventInterface;
import nl.tudelft.simulation.event.EventListenerInterface;
import nl.tudelft.simulation.event.EventType;

/**
 * The Counter class defines a statistics event counter.
 * <p>
 * (c) copyright 2002-2005-2004 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="http://www.peter-jacobs.com">Peter Jacobs </a>
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:38:40 $
 * @since 1.5
 */
public class Counter extends StatisticsObject implements EventListenerInterface
{
    /** COUNT_EVENT is fired whenever setCount() is invoked */
    public static final EventType COUNT_EVENT = new EventType("COUNT_EVENT");

    /** N_EVENT is fired on every new measurement */
    public static final EventType N_EVENT = new EventType("N_EVENT");

    /** count represents the value of the counter */
    protected long count = Long.MIN_VALUE;

    /** n represents the number of measurements */
    protected long n = Long.MIN_VALUE;

    /** description refers to the title of this counter */
    protected String description;

    /** the semaphore */
    private Object semaphore = new Object();

    /**
     * constructs a new CounterTest
     * @param description the description for this counter
     */
    public Counter(final String description)
    {
        super();
        this.description = description;
    }

    /**
     * Returns the current counter value
     * @return long the counter value
     */
    public long getCount()
    {
        return this.count;
    }

    /**
     * Returns the current number of observations
     * @return long the number of observations
     */
    public long getN()
    {
        return this.n;
    }

    /**
     * @see nl.tudelft.simulation.event.EventListenerInterface #notify(nl.tudelft.simulation.event.EventInterface)
     */
    public void notify(final EventInterface event)
    {
        long value = 1;
        if (event.getContent() instanceof Number)
        {
            value = Math.round(((Number) event.getContent()).doubleValue());
        }
        if (!this.filter.accept(new double[]{value, value}))
        {
            return;
        }
        synchronized (this.semaphore)
        {
            this.setCount(this.count + value);
            this.setN(this.n + 1);
            if (!super.listeners.isEmpty())
            {
                this.fireEvent(Counter.COUNT_EVENT, this.count);
                this.fireEvent(Counter.N_EVENT, this.n);
            }
        }
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return this.description;
    }

    /**
     * initializes the counter
     */
    public void initialize()
    {
        synchronized (this.semaphore)
        {
            this.setN(0);
            this.setCount(0);
        }
    }

    /**
     * is the counter initialized?
     * @return returns whether the counter is initialized
     */
    public boolean isInitialized()
    {
        return this.n != Long.MIN_VALUE;
    }

    /**
     * sets the count
     * @param count the value
     */
    private void setCount(final long count)
    {
        this.count = count;
        this.fireEvent(new Event(COUNT_EVENT, this, new Long(this.count)));
    }

    /**
     * sets n
     * @param n the number of measurements
     */
    private void setN(final long n)
    {
        this.n = n;
        this.fireEvent(new Event(N_EVENT, this, new Long(this.n)));
    }

    /**
     * returns the description of the counter
     * @return String the description
     */
    public String getDescription()
    {
        return this.description;
    }

    /**
     * @see nl.tudelft.simulation.jstats.statistics.StatisticsObject #getTable()
     */
    @Override
    public TableModel getTable()
    {
        String[] columnNames = {"field", "value"};
        EventType[] eventTypes = {null, Counter.N_EVENT, Counter.COUNT_EVENT};
        StatisticsTableModel result = new StatisticsTableModel(columnNames, eventTypes, 3);
        this.addListener(result, Counter.N_EVENT, true);
        this.addListener(result, Counter.COUNT_EVENT, true);

        result.setValueAt("name", 0, 0);
        result.setValueAt("n", 1, 0);
        result.setValueAt("count", 2, 0);
        result.setValueAt(this.description, 0, 1);
        result.setValueAt(new Long(this.n), 1, 1);
        result.setValueAt(new Long(this.count), 2, 1);
        return result;
    }
}