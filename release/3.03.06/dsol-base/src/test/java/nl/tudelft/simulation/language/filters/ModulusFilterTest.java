package nl.tudelft.simulation.language.filters;

import junit.framework.TestCase;

/**
 * Tests the ModulusFilter.
 * <p>
 * Copyright (c) 2002-2019 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. See for project information <a href="https://simulation.tudelft.nl/" target="_blank">
 * https://simulation.tudelft.nl</a>. The DSOL project is distributed under a three-clause BSD-style license, which can
 * be found at <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="http://web.eur.nl/fbk/dep/dep1/Introduction/Staff/People/Lang">Niels Lang
 *         </a><a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @since 1.5
 */
public class ModulusFilterTest extends TestCase
{
    /**
     * constructs a new ZeroFilterTest.
     */
    public ModulusFilterTest()
    {
        this("test");
    }

    /**
     * constructs a new ZeroFilterTest.
     * @param arg0
     */
    public ModulusFilterTest(String arg0)
    {
        super(arg0);
    }

    /**
     * tests the ZeroFilter.
     */
    public void test()
    {
        FilterInterface filter = new ModulusFilter(10);
        for (int i = 0; i < 100; i++)
        {
            if (i % 10 == 0)
            {
                TestCase.assertTrue(filter.accept("entry"));
            }
            else
            {
                TestCase.assertFalse(filter.accept("entry"));
            }
        }
    }
}
