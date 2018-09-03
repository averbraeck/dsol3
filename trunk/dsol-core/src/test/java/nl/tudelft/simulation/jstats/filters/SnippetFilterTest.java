package nl.tudelft.simulation.jstats.filters;

import junit.framework.Assert;
import junit.framework.TestCase;
import nl.tudelft.simulation.language.filters.FilterInterface;

/**
 * Tests the MaxDiffFilter.
 * <p>
 * (c) 2002-2018 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="http://web.eur.nl/fbk/dep/dep1/Introduction/Staff/People/Lang">Niels Lang
 *         </a><a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @version 1.2 Sep 28, 2004
 * @since 1.5
 */
public class SnippetFilterTest extends TestCase
{
    /**
     * constructs a new ZeroFilterTest.
     */
    public SnippetFilterTest()
    {
        this("test");
    }

    /**
     * constructs a new ZeroFilterTest.
     * @param arg0
     */
    public SnippetFilterTest(String arg0)
    {
        super(arg0);
    }

    /**
     * tests the SnippetFilter.
     */
    public void test()
    {
        FilterInterface filter = new SnippetFilter(1.0);
        Assert.assertTrue(filter.accept(new double[]{10.0, 0}));
        Assert.assertFalse(filter.accept(new double[]{10.1, 0.0}));
        Assert.assertTrue(filter.accept(new double[]{11.0, 0.0}));
        Assert.assertFalse(filter.accept(new double[]{11.1, 0.0}));
        Assert.assertFalse(filter.accept(new double[]{11.5, 0.0}));
    }
}
