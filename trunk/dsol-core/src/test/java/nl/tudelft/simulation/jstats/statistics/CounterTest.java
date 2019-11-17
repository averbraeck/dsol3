package nl.tudelft.simulation.jstats.statistics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import nl.tudelft.simulation.event.Event;
import nl.tudelft.simulation.event.EventInterface;
import nl.tudelft.simulation.event.EventListenerInterface;

/**
 * The counterTest test the counter.
 * <p>
 * Copyright (c) 2002-2019 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @since 1.5
 */
public class CounterTest
{
    /**
     * tests the counter.
     */
    @Test
    public void test()
    {
        String description = "counter description";
        Counter counter = new Counter(description);
        assertEquals(counter.toString(), description);
        assertEquals(counter.getDescription(), description);

        assertTrue(counter.getN() == Long.MIN_VALUE);
        assertTrue(counter.getCount() == Long.MIN_VALUE);

        counter.initialize();

        counter.addListener(new EventListenerInterface()
        {
            @Override
            public void notify(final EventInterface event)
            {
                assertTrue(event.getType().equals(Counter.COUNT_EVENT));
                assertTrue(event.getContent().getClass().equals(Long.class));
            }
        }, Counter.COUNT_EVENT);

        long value = 0;
        for (int i = 0; i < 100; i++)
        {
            counter.notify(new Event(Counter.COUNT_EVENT, this, Long.valueOf(2 * i)));
            value = value + 2 * i;
        }
        assertTrue(counter.getN() == 100);
        assertTrue(counter.getCount() == value);
    }
}
