/*
 * @(#) JStatsTestSuite.java Aug 26, 2003 Copyright (c) 2002-2005 Delft
 * University of Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All
 * rights reserved. This software is proprietary information of Delft University
 * of Technology 
 */
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
 * The DSOL TestSuite defines the JUnit Test Suite which tests all DSOL classes. <br>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl"> www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:38:41 $
 * @author <a href="http://www.peter-jacobs.com/index.htm">Peter Jacobs </a>
 */
public final class JStatsTestSuite
{
    /**
     * constructs a new JStatsTestSuite
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