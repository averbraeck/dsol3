package nl.tudelft.simulation.event.util;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import nl.tudelft.simulation.event.EventInterface;
import nl.tudelft.simulation.event.EventListenerInterface;

/**
 * The test script for the EventIterator class.
 * <p>
 * Copyright (c) 2004-2019 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @since 1.5
 */
public class EventIteratorTest implements EventListenerInterface
{
    /** a check on the removed state. */
    private boolean removed = false;

    /** TEST_METHOD is the name of the test method. */
    public static final String TEST_METHOD = "test";

    /**
     * tests the classes in the reference class.
     */
    @Test
    public void eventIteratorTest()
    {
        List<Object> list = new ArrayList<Object>();
        list.add(new Object());
        EventIterator<Object> iterator = new EventIterator<Object>(list.iterator());
        iterator.next();
        iterator.addListener(this, EventIterator.OBJECT_REMOVED_EVENT);
        iterator.remove();
        assertTrue(this.removed);
    }

    /** {@inheritDoc} */
    @Override
    public void notify(final EventInterface event)
    {
        if (event.getType().equals(EventIterator.OBJECT_REMOVED_EVENT))
        {
            this.removed = true;
        }
    }
}
