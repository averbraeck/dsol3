package nl.tudelft.simulation.language.primitive;

import junit.framework.TestCase;
import nl.tudelft.simulation.language.primitives.Primitive;

/**
 * The JUNIT Test for the <code>Primitive</code>.
 * <p>
 * (c) 2002-2018 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
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
public class PrimitiveTest extends TestCase
{

    /**
     * constructs a new PrimitiveTest.
     */
    public PrimitiveTest()
    {
        this("test");
    }

    /**
     * constructs a new PrimitiveTest.
     * @param arg0
     */
    public PrimitiveTest(String arg0)
    {
        super(arg0);
    }

    /**
     * tests the Primitive
     */
    public void test()
    {
        TestCase.assertEquals(Primitive.getPrimitive(Boolean.class), boolean.class);
        TestCase.assertEquals(Primitive.getPrimitive(Integer.class), int.class);
        TestCase.assertEquals(Primitive.getPrimitive(Double.class), double.class);
        TestCase.assertEquals(Primitive.getPrimitive(Float.class), float.class);
        TestCase.assertEquals(Primitive.getPrimitive(Character.class), char.class);
        TestCase.assertEquals(Primitive.getPrimitive(Byte.class), byte.class);
        TestCase.assertEquals(Primitive.getPrimitive(Short.class), short.class);
        TestCase.assertEquals(Primitive.getPrimitive(Long.class), long.class);

        TestCase.assertEquals(Primitive.getWrapper(boolean.class), Boolean.class);
        TestCase.assertEquals(Primitive.getWrapper(int.class), Integer.class);
        TestCase.assertEquals(Primitive.getWrapper(double.class), Double.class);
        TestCase.assertEquals(Primitive.getWrapper(float.class), Float.class);
        TestCase.assertEquals(Primitive.getWrapper(char.class), Character.class);
        TestCase.assertEquals(Primitive.getWrapper(byte.class), Byte.class);
        TestCase.assertEquals(Primitive.getWrapper(long.class), Long.class);

        TestCase.assertEquals(Primitive.toBoolean(new Integer(1)), Boolean.TRUE);
        TestCase.assertEquals(Primitive.toBoolean(new Double(1.0)), Boolean.TRUE);
        TestCase.assertEquals(Primitive.toBoolean(new Float(1.0)), Boolean.TRUE);
        TestCase.assertEquals(Primitive.toBoolean(new Short((short) 1.0)), Boolean.TRUE);
        TestCase.assertEquals(Primitive.toBoolean(new Long(1l)), Boolean.TRUE);

        TestCase.assertEquals(Primitive.toBoolean(new Integer(0)), Boolean.FALSE);
        TestCase.assertEquals(Primitive.toBoolean(new Double(0.0)), Boolean.FALSE);
        TestCase.assertEquals(Primitive.toBoolean(new Float(0.0)), Boolean.FALSE);
        TestCase.assertEquals(Primitive.toBoolean(new Short((short) 0.0)), Boolean.FALSE);
        TestCase.assertEquals(Primitive.toBoolean(new Long(0l)), Boolean.FALSE);
    }
}
