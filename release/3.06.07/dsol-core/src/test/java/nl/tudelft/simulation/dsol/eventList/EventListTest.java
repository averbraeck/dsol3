package nl.tudelft.simulation.dsol.eventList;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import nl.tudelft.simulation.dsol.eventlists.EventListInterface;
import nl.tudelft.simulation.dsol.eventlists.RedBlackTree;
import nl.tudelft.simulation.dsol.formalisms.eventscheduling.SimEvent;
import nl.tudelft.simulation.dsol.formalisms.eventscheduling.SimEventInterface;
import nl.tudelft.simulation.dsol.simtime.SimTimeDouble;

/**
 * This class defines the JUnit test for the TreeMapEventListOld.
 * <p>
 * Copyright (c) 2002-2021 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>,
 *         <a href="mailto:a.verbraeck@tudelft.nl">Alexander Verbraeck </a>
 */
public class EventListTest
{
    /** eventList is the eventList on which the test is fired. */
    private EventListInterface<SimTimeDouble> eventList = null;

    /**
     * constructs a new RedblackTree.
     */
    public EventListTest()
    {
        this.eventList = new RedBlackTree<SimTimeDouble>();
    }

    /**
     * tests the TreeMapEventListOld.
     */
    @Test
    public void testEventList()
    {
        assertNotNull(this.eventList);
        try
        {
            // We fill the eventList with 500 events with random times
            // between [0..200]
            for (int i = 0; i < 500; i++)
            {
                this.eventList.add(
                        new SimEvent<SimTimeDouble>(new SimTimeDouble(200 * Math.random()), this, new String(), "trim", null));
            }

            // Now we assert some getters on the eventList
            assertTrue(!this.eventList.isEmpty());
            assertTrue(this.eventList.size() == 500);

            // Let's see if the eventList was properly ordered
            double time = 0;
            for (int i = 0; i < 500; i++)
            {
                SimEventInterface<SimTimeDouble> simEvent = this.eventList.first();
                this.eventList.remove(this.eventList.first());
                double executionTime = simEvent.getAbsoluteExecutionTime().get().doubleValue();
                assertTrue(executionTime >= 0.0);
                assertTrue(executionTime <= 200.0);
                assertTrue(executionTime >= time);
                time = executionTime;
            }

            // Now we fill the eventList with a number of events with different priorities on time=0.0
            for (int i = 1; i < 10; i++)
            {
                this.eventList
                        .add(new SimEvent<SimTimeDouble>(new SimTimeDouble(0.0), (short) i, this, new String(), "trim", null));
            }
            short priority = SimEventInterface.MAX_PRIORITY;

            // Let's empty the eventList and check the priorities
            while (!this.eventList.isEmpty())
            {
                SimEventInterface<SimTimeDouble> simEvent = this.eventList.first();
                this.eventList.remove(this.eventList.first());
                double executionTime = simEvent.getAbsoluteExecutionTime().get().doubleValue();
                short eventPriority = simEvent.getPriority();

                assertTrue(executionTime == 0.0);
                assertTrue(eventPriority <= SimEventInterface.MAX_PRIORITY);
                assertTrue(eventPriority >= SimEventInterface.MIN_PRIORITY);
                assertTrue(eventPriority <= priority);
                priority = eventPriority;
            }

            // Let's check the empty eventList
            assertTrue(this.eventList.isEmpty());
            assertNull(this.eventList.first());
            assertFalse(this.eventList.remove(
                    new SimEvent<SimTimeDouble>(new SimTimeDouble(200 * Math.random()), this, new String(), "trim", null)));
            this.eventList.clear();

            // Let's cancel an event
            this.eventList.add(new SimEvent<SimTimeDouble>(new SimTimeDouble(100), this, this, "toString", null));
            SimEventInterface<SimTimeDouble> simEvent = new SimEvent<>(new SimTimeDouble(100), this, this, "toString", null);
            this.eventList.add(simEvent);
            assertTrue(this.eventList.remove(simEvent));
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
            fail(exception.getMessage());
        }
    }
}
