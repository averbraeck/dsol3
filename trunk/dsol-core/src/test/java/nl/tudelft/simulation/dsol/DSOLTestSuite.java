package nl.tudelft.simulation.dsol;

import junit.framework.Test;
import junit.framework.TestSuite;
import nl.tudelft.simulation.dsol.eventList.EventListTest;
import nl.tudelft.simulation.dsol.serialize.SerializeTest;

/**
 * The DSOL TestSuite defines the JUnit Test Suite which tests all DSOL classes.
 * <p>
 * Copyright (c) 2002-2019 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>,
 *         <a href="mailto:a.verbraeck@tudelft.nl">Alexander Verbraeck </a>
 */
public final class DSOLTestSuite
{
    /**
     * constructs a new DSOLTestSuite.
     */
    private DSOLTestSuite()
    {
        super();
    }

    /**
     * constructs the test suite.
     * @return Test the main DSOL Test Suite
     */
    public static Test suite()
    {
        TestSuite suite = new TestSuite("DSOLTestSuite");
        suite.addTest(new EventListTest());
        suite.addTest(new SerializeTest("test"));
        // suite.addTest(new DESSSimulatorTest());
        // suite.addTest(new DEVSSimulatorTest());
        // suite.addTest(new RealTimeClockTest_Failed());
        // TODO suite.addTest(new SimulatorTest());
        return suite;
    }
}
