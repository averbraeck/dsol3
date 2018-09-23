package nl.tudelft.simulation.dsol;

import junit.framework.Test;
import junit.framework.TestSuite;
import nl.tudelft.simulation.dsol.eventList.EventListTest;
import nl.tudelft.simulation.dsol.serialize.SerializeTest;

/**
 * The DSOL TestSuite defines the JUnit Test Suite which tests all DSOL classes. <br>
 * (c) 2002-2018 <a href="https://simulation.tudelft.nl"> Delft University of Technology </a>, the Netherlands. <br>
 * See for project information <a href="https://simulation.tudelft.nl"> www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @version 2.0 21.09.2003 <br>
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
     * constructs the test suite
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
