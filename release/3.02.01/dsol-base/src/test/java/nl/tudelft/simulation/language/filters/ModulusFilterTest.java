package nl.tudelft.simulation.language.filters;

import junit.framework.TestCase;

/**
 * Tests the ModulusFilter.
 * <p>
 * (c) 2002-2018 <a href="https://simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands.
 * <p>
 * See for project information <a
 * href="https://simulation.tudelft.nl/dsol/language">www.simulation.tudelft.nl/language </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty
 * @author <a href="http://web.eur.nl/fbk/dep/dep1/Introduction/Staff/People/Lang">Niels Lang </a><a
 *         href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @version 1.2 Sep 28, 2004
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
