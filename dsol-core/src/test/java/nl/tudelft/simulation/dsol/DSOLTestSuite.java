package nl.tudelft.simulation.dsol;

import nl.tudelft.simulation.dsol.eventList.EventListTest;
import nl.tudelft.simulation.dsol.experiment.TimeUnitTest;
import nl.tudelft.simulation.dsol.serialize.SerializeTest;
import nl.tudelft.simulation.dsol.simulators.DESSSimulatorTest_Failed;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorTest_Failed;
import nl.tudelft.simulation.dsol.simulators.RealTimeClockTest_Failed;
import nl.tudelft.simulation.dsol.simulators.SimulatorTest_NotCorrect;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * The DSOL TestSuite defines the JUnit Test Suite which tests all DSOL classes. <br>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl"> Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl"> www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @version 2.0 21.09.2003 <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>, <a
 *         href="mailto:a.verbraeck@tudelft.nl">Alexander Verbraeck </a>
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
     * constructs the test suite
     * @return Test the main DSOL Test Suite
     */
    public static Test suite()
    {
        TestSuite suite = new TestSuite("DSOL Test Suite");
        suite.addTest(new EventListTest());
        suite.addTest(new TimeUnitTest());
        suite.addTest(new SerializeTest("SerializeTest"));
        // suite.addTest(new DESSSimulatorTest());
        // suite.addTest(new DEVSSimulatorTest());
        // suite.addTest(new RealTimeClockTest_Failed());
        // TODO: suite.addTest(new SimulatorTest());
        return suite;
    }
}
