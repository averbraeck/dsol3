/*
 * @(#) ZeroFilterTest.java Sep 28, 2004 Copyright (c) 2002-2005 Delft
 * University of Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All
 * rights reserved. This software is proprietary information of Delft University
 * of Technology 
 */
package nl.tudelft.simulation.jstats.filters;

import junit.framework.Assert;
import junit.framework.TestCase;
import nl.tudelft.simulation.language.filters.FilterInterface;

/**
 * Tests the MaxDiffFilter.
 * <p>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="http://web.eur.nl/fbk/dep/dep1/Introduction/Staff/People/Lang">Niels Lang </a><a
 *         href="http://www.peter-jacobs.com/index.htm">Peter Jacobs </a>
 * @version 1.2 Sep 28, 2004
 * @since 1.5
 */
public class MaxDiffFilterTest extends TestCase
{
    /**
     * constructs a new ZeroFilterTest
     */
    public MaxDiffFilterTest()
    {
        this("test");
    }

    /**
     * constructs a new ZeroFilterTest
     * @param arg0
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