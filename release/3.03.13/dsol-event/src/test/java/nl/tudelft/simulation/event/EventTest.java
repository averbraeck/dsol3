package nl.tudelft.simulation.event;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * The test script for the Event class.
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
public class EventTest
{
    /**
     * tests the classes in the reference class.
     */
    @Test
    public void eventTest()
    {
        Object source = this;
        EventType eventType = new EventType("TEST_TYPE");
        Object content = new Object();

        // Some basic tests
        EventInterface event = new Event(eventType, source, content);
        assertEquals(event.getContent(), content);
        assertEquals(event.getSource(), source);
        assertEquals(event.getType(), eventType);

    }
}
