package nl.tudelft.simulation.event;

import junit.framework.Test;
import junit.framework.TestSuite;
import nl.tudelft.simulation.event.ref.EventRefTest;
import nl.tudelft.simulation.event.util.EventIteratorTest;

/**
 * The EventTestSuite defines the JUnit Test Suite which tests all Event classes.
 * <p>
 * (c) 2002-2018 <a href="https://simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="https://simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:38:11 $
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
