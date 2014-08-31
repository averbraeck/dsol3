package nl.tudelft.simulation.language.reflection;

import junit.framework.TestCase;

/**
 * The JUNIT Test for the <code>ClassUtilTest</code>.
 * <p>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands.
 * <p>
 * See for project information <a
 * href="http://www.simulation.tudelft.nl/dsol/language">www.simulation.tudelft.nl/language </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @version 1.2 Sep 28, 2004
 * @since 1.5
 */
public class ClassUtilTest extends TestCase
{

    /**
     * constructs a new ClassUtilTest.
     */
    public ClassUtilTest()
    {
        this("test");
    }

    /**
     * constructs a new ClassUtilTest.
     * @param arg0
     */
    public ClassUtilTest(String arg0)
    {
        super(arg0);
    }

    /**
     * tests the ClassUtil
     */
    public void test()
    {
        // the getClass method
        TestCase.assertEquals(ClassUtil.getClass(null).length, 0);
        TestCase.assertEquals(ClassUtil.getClass(new Object[]{"Peter"})[0], String.class);
    }
}
