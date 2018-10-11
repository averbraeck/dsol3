package nl.tudelft.simulation.event;

import junit.framework.Test;
import junit.framework.TestSuite;
import nl.tudelft.simulation.event.ref.EventRefTest;
import nl.tudelft.simulation.event.util.EventIteratorTest;

/**
 * The EventTestSuite defines the JUnit Test Suite which tests all Event classes.
 * <p>
 * Copyright (c) 2002-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. See for project information <a href="https://simulation.tudelft.nl/" target="_blank">
 * https://simulation.tudelft.nl</a>. The DSOL project is distributed under a three-clause BSD-style license, which can
 * be found at <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @since 1.5
 */
public final class EventTestSuite
{
    /**
     * constructs a new EventRefTestSuite.
     */
    private EventTestSuite()
    {
        super();
    }

    /**
     * constructs the test suite
     * @return Test the main DSOL Test Suite
     */
    public static Test suite()
    {
        TestSuite suite = new TestSuite("EVENT Test Suite");

        // nl.tudelft.simulation.event.ref
        suite.addTest(new EventRefTest());

        // nl.tudelft.simulation.event.util
        suite.addTest(new EventIteratorTest());

        // nl.tudelft.simulation.event
        suite.addTest(new EventTest());
        suite.addTest(new EventProducerTest());
        return suite;
    }
}
