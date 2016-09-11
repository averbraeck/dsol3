package nl.tudelft.simulation.language.filters;

import junit.framework.TestCase;

/**
 * Tests the ZeroFilter.
 * <p>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands.
 * <p>
 * See for project information <a
 * href="http://www.simulation.tudelft.nl/dsol/language">www.simulation.tudelft.nl/language </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty
 * @author <a href="http://web.eur.nl/fbk/dep/dep1/Introduction/Staff/People/Lang">Niels Lang </a><a
 *         href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @version 1.2 Sep 28, 2004
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
