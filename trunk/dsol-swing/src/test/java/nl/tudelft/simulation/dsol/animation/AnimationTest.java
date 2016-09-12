package nl.tudelft.simulation.dsol.animation;

import junit.framework.Assert;
import junit.framework.TestCase;
import nl.tudelft.simulation.language.d3.CartesianPoint;

/**
 * This class defines the JUnit test for the D2Test <br>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @version 2.0 21.09.2003 <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>,
 *         <a href="mailto:a.verbraeck@tudelft.nl">Alexander Verbraeck </a>
 */
public class AnimationTest extends TestCase
{
    /** TEST_METHOD_NAME refers to the name of the test method. */
    public static final String TEST_METHOD_NAME = "test";

    /**
     * constructs a new D2Test.
     */
    public AnimationTest()
    {
        super(TEST_METHOD_NAME);
    }

    /**
     * tests the TreeMapEventListOld
     */
    public void test()
    {
        CartesianPoint point1 = new CartesianPoint(1.0, 1.0, 1.0);
        CartesianPoint point2 = new CartesianPoint(1.0, 1.0, 1.0);

        Assert.assertTrue(point1.distance(point2) == 0);
        Assert.assertNotSame(point1, point2);
    }
}
