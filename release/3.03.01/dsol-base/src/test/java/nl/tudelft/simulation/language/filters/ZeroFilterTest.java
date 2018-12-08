package nl.tudelft.simulation.language.filters;

import junit.framework.TestCase;

/**
 * Tests the ZeroFilter.
 * <p>
 * Copyright (c) 2002-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. See for project information <a href="https://simulation.tudelft.nl/" target="_blank">
 * https://simulation.tudelft.nl</a>. The DSOL project is distributed under a three-clause BSD-style license, which can
 * be found at <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="http://web.eur.nl/fbk/dep/dep1/Introduction/Staff/People/Lang">Niels Lang
 *         </a><a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @since 1.5
 */
public class ZeroFilterTest extends TestCase
{
    /**
     * constructs a new ZeroFilterTest.
     */
    public ZeroFilterTest()
    {
        this("test");
    }

    /**
     * constructs a new ZeroFilterTest.
     * @param arg0
     */
    public ZeroFilterTest(String arg0)
    {
        super(arg0);
    }

    /**
     * tests the ZeroFilter.
     */
    public void test()
    {
        FilterInterface filter = new ZeroFilter();
        TestCase.assertTrue(filter.accept(null));
        TestCase.assertTrue(filter.accept("entry"));

        // Let's put the filter on inverted mode
        filter.setInverted(true);
        TestCase.assertFalse(filter.accept(null));
        TestCase.assertFalse(filter.accept("entry"));

        // Let's test AND
        filter = filter.and(new ZeroFilter());
        TestCase.assertFalse(filter.accept("entry"));

        // Let's test OR
        FilterInterface filter1 = new ZeroFilter();
        FilterInterface filter2 = new ZeroFilter();
        filter2.setInverted(true);
        filter = filter1.or(filter2);
        TestCase.assertTrue(filter.accept("entry"));
    }
}
