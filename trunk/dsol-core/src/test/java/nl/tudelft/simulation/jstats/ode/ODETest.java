package nl.tudelft.simulation.jstats.ode;

import junit.framework.TestCase;
import nl.tudelft.simulation.jstats.ode.integrators.NumericalIntegrator;

/**
 * The test script for the ODE package.
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
public class ODETest extends TestCase
{
    /** TEST_METHOD is the name of the test method. */
    public static final String TEST_METHOD = "test";

    /**
     * constructs a new EventIteratorTest.
     */
    public ODETest()
    {
        this(TEST_METHOD);
    }

    /**
     * constructs a new EventIteratorTest.
     * @param method the name of the test method
     */
    public ODETest(final String method)
    {
        super(method);
    }

    /**
     * tests the classes in the reference class.
     */
    public void test()
    {
        Function function = new Function(0.1, NumericalIntegrator.RUNGEKUTTA4);
        double[] result = function.y(30.0);
        System.out.println(result[0]);
    }
}
