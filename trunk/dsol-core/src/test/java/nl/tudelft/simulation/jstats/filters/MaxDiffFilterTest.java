package nl.tudelft.simulation.jstats.filters;

import junit.framework.Assert;
import junit.framework.TestCase;
import nl.tudelft.simulation.language.filters.FilterInterface;

/**
 * Tests the MaxDiffFilter.
 * <p>
 * Copyright (c) 2002-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. See for project information <a href="https://simulation.tudelft.nl/" target="_blank">
 * https://simulation.tudelft.nl</a>. The DSOL project is distributed under a three-clause BSD-style license, which can
 * be found at <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="http://web.eur.nl/fbk/dep/dep1/Introduction/Staff/People/Lang">Niels Lang
 *         </a><a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @version 1.2 Sep 28, 2004
 * @since 1.5
 */
public class MaxDiffFilterTest extends TestCase
{
    /**
     * constructs a new ZeroFilterTest.
     */
    public MaxDiffFilterTest()
    {
        this("test");
    }

    /**
     * constructs a new ZeroFilterTest.
     * @param arg0 arg
     */
    public MaxDiffFilterTest(String arg0)
    {
        super(arg0);
    }

    /**
     * tests the ZeroFilter.
     */
    public void test()
    {
        FilterInterface filter = new MaxDiffFilter(0.25);
        Assert.assertTrue(filter.accept(new double[]{0.0, 10}));
        Assert.assertFalse(filter.accept(new double[]{0.0, 12.4}));
        Assert.assertFalse(filter.accept(new double[]{0.0, 10}));
        Assert.assertTrue(filter.accept(new double[]{0.0, 12.6}));
    }
}
