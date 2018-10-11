package nl.tudelft.simulation.jstats.ode;

import junit.framework.TestCase;
import nl.tudelft.simulation.jstats.ode.integrators.NumericalIntegrator;

/**
 * The test script for the ODE package.
 * <p>
 * Copyright (c) 2002-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. See for project information <a href="https://simulation.tudelft.nl/" target="_blank">
 * https://simulation.tudelft.nl</a>. The DSOL project is distributed under a three-clause BSD-style license, which can
 * be found at <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
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
