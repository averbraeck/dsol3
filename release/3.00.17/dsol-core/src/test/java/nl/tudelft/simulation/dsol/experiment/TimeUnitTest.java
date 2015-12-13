package nl.tudelft.simulation.dsol.experiment;

import junit.framework.Assert;
import junit.framework.TestCase;
import nl.tudelft.simulation.dsol.simtime.TimeUnit;

/**
 * This class defines the JUnit test for the TimeUnit class <br>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl"> www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @version 2.0 21.09.2003 <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>, <a href="mailto:a.verbraeck@tudelft.nl">
 *         Alexander Verbraeck </a>
 */
public class TimeUnitTest extends TestCase
{
    /** TEST_METHOD_NAME refers to the name of the test method. */
    public static final String TEST_METHOD_NAME = "test";

    /**
     * constructs a new TimeUnitTest.
     */
    public TimeUnitTest()
    {
        this(TEST_METHOD_NAME);
    }

    /**
     * constructs a new TimeTest.
     * @param arg0 the name of the test method
     */
    public TimeUnitTest(final String arg0)
    {
        super(arg0);
    }

    /**
     * tests the TimeClass
     */
    public void test()
    {
        Assert.assertEquals(new Long((long) TimeUnit.WEEK.getFactor()), new Long(7L * (long) TimeUnit.DAY.getFactor()));
        Assert.assertEquals(new Long((long) TimeUnit.DAY.getFactor()), new Long(24L * (long) TimeUnit.HOUR.getFactor()));
        Assert.assertEquals(new Long((long) TimeUnit.HOUR.getFactor()),
                new Long(60L * (long) TimeUnit.MINUTE.getFactor()));
        Assert.assertEquals(new Long((long) TimeUnit.MINUTE.getFactor()),
                new Long(60L * (long) TimeUnit.SECOND.getFactor()));
        Assert.assertEquals(new Long((long) TimeUnit.SECOND.getFactor()),
                new Long(1000L * (long) TimeUnit.MILLISECOND.getFactor()));
        Assert.assertEquals(new Long((long) TimeUnit.MILLISECOND.getFactor()),
                new Long((long) TimeUnit.UNIT.getFactor()));
    }
}
