package nl.tudelft.simulation.jstats.math;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * The test script for the ProbMath class.
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
public class ProbMathTest extends TestCase
{
    /** TEST_METHOD is the name of the test method. */
    public static final String TEST_METHOD = "test";

    /**
     * constructs a new EventIteratorTest.
     */
    public ProbMathTest()
    {
        this(TEST_METHOD);
    }

    /**
     * constructs a new EventIteratorTest.
     * @param method the name of the test method
     */
    public ProbMathTest(final String method)
    {
        super(method);
    }

    /**
     * tests the classes in the reference class.
     */
    public void test()
    {
        // //First the faculty function
        try
        {
            ProbMath.faculty(-1);
            Assert.fail();
        }
        catch (Exception exception)
        {
            Assert.assertEquals(exception.getClass(), IllegalArgumentException.class);
        }
        Assert.assertTrue(ProbMath.faculty(0) == 1.0);
        Assert.assertTrue(ProbMath.faculty(10) == 3628800.0);
        try
        {
            ProbMath.faculty(171);
            Assert.fail();
        }
        catch (Exception exception)
        {
            Assert.assertEquals(exception.getClass(), IllegalArgumentException.class);
        }

        // Permutations
        try
        {
            ProbMath.permutations(2, 5);
        }
        catch (Exception exception)
        {
            Assert.assertEquals(exception.getClass(), IllegalArgumentException.class);
        }
    }
}
