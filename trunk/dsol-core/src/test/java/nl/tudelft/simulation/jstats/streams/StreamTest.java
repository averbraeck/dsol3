package nl.tudelft.simulation.jstats.streams;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * The test script for the Stream class.
 * <p>
 * (c) copyright 2002-2005-2004 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @version 1.0, 2004-03-18
 * @since 1.5
 */
public class StreamTest extends TestCase
{
    /** TEST_METHOD is the name of the test method. */
    public static final String TEST_METHOD = "test";

    /**
     * constructs a new EventIteratorTest.
     */
    public StreamTest()
    {
        this(TEST_METHOD);
    }

    /**
     * constructs a new EventIteratorTest.
     * @param method the name of the test method
     */
    public StreamTest(final String method)
    {
        super(method);
    }

    /**
     * tests the classes in the reference class.
     */
    public void test()
    {
        StreamInterface[] streams = {new Java2Random(), new MersenneTwister(), new DX120Generator()};
        for (int i = 0; i < 1000000; i++)
        {
            for (int j = 0; j < streams.length; j++)
            {
                double value = streams[j].nextDouble();
                Assert.assertTrue(value > 0.0);
                Assert.assertTrue(value < 1.0);
            }
        }
    }
}
