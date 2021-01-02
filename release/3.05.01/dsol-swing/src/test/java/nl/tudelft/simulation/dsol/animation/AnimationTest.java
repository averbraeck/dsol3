package nl.tudelft.simulation.dsol.animation;

import junit.framework.Assert;
import junit.framework.TestCase;
import nl.tudelft.simulation.language.d3.CartesianPoint;

/**
 * This class defines the JUnit test for the D2Test.
 * <p>
 * Copyright (c) 2002-2021 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
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
