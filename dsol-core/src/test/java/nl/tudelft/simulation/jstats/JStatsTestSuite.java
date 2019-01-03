package nl.tudelft.simulation.jstats;

import junit.framework.Test;
import junit.framework.TestSuite;
import nl.tudelft.simulation.jstats.math.ProbMathTest;
import nl.tudelft.simulation.jstats.ode.ODETest;
import nl.tudelft.simulation.jstats.statistics.CounterTest;
import nl.tudelft.simulation.jstats.statistics.PersistentTest;
import nl.tudelft.simulation.jstats.statistics.TallyTest;
import nl.tudelft.simulation.jstats.streams.StreamTest;

/**
 * The DSOL TestSuite defines the JUnit Test Suite which tests all DSOL classes..
 * <p>
 * Copyright (c) 2002-2019 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. See for project information <a href="https://simulation.tudelft.nl/" target="_blank">
 * https://simulation.tudelft.nl</a>. The DSOL project is distributed under a three-clause BSD-style license, which can
 * be found at <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 */
public final class JStatsTestSuite
{
    /**
     * constructs a new JStatsTestSuite.
     */
    private JStatsTestSuite()
    {
        super();
    }

    /**
     * constructs the test suite
     * @return Test the JStats test Suite
     */
    public static Test suite()
    {
        TestSuite suite = new TestSuite("JStats Test Suite");

        suite.addTest(new ProbMathTest());
        suite.addTest(new StreamTest());
        suite.addTest(new CounterTest());
        suite.addTest(new TallyTest());
        suite.addTest(new PersistentTest());
        suite.addTest(new ODETest());
        return suite;
    }
}
