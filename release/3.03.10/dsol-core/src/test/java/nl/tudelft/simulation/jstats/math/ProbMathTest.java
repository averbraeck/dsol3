package nl.tudelft.simulation.jstats.math;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * The test script for the ProbMath class.
 * <p>
 * Copyright (c) 2002-2019 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
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
