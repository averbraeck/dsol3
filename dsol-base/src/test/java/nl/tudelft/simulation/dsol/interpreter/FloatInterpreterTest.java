package nl.tudelft.simulation.dsol.interpreter;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * <p />
 * (c) copyright 2002-2014 <a href="http://www.simulation.tudelft.nl">Delft University of Technology</a>. <br />
 * BSD-style license. See <a href="http://www.simulation.tudelft.nl/dsol/3.0/license.html">DSOL License</a>. <br />
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs</a>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @version Aug 31, 2014
 */
public class FloatInterpreterTest
{
    /** precision */
    private static final float DELTA = 0.0f;
    
    /**
     * Compare a number of methods when executed and when interpreted.
     */
    @Test
    public void testInterpretation()
    {
        System.out.println("FloatInterpreterTest");

        FloatMethods e1 = new FloatMethods(1.0f);
        FloatMethods i1 = new FloatMethods(1.0f);

        // integer methods
        e1.add(2.0f);
        Interpreter.invoke(i1, "add", new Float[]{2.0f}, new Class<?>[]{float.class});
        assertEquals("test Value(1.0f).add(2.0f) == 3.0f", e1.getValue(), i1.getValue(), DELTA);

        e1.subtract(4.0f);
        Interpreter.invoke(i1, "subtract", new Float[]{4.0f}, new Class<?>[]{float.class});
        assertEquals("test Value(3.0f).subtract(4.0f) == -1.0f", e1.getValue(), i1.getValue(), DELTA);

        e1.multiplyBy(-10.0f);
        Interpreter.invoke(i1, "multiplyBy", new Float[]{-10.0f}, new Class<?>[]{float.class});
        assertEquals("test Value(-1).multiplyBy(-10.0f) == 10.0", e1.getValue(), i1.getValue(), DELTA);

        e1.divideBy(3.0f);
        Interpreter.invoke(i1, "divideBy", new Float[]{3.0f}, new Class<?>[]{float.class});
        assertEquals("test Value(10).divideBy(3.0f) == 3", e1.getValue(), i1.getValue(), DELTA);

        // static methods
        assertEquals("FloatMethods.plus(2.0f, 3.0f)",
                ((Float) Interpreter.invoke(FloatMethods.class, "plus", new Float[]{2.0f, 3.0f}, new Class<?>[]{
                        float.class, float.class})).floatValue(), FloatMethods.plus(2.0f, 3.0f), DELTA);
        assertEquals("FloatMethods.minus(2.0f, 3.0f)",
                ((Float) Interpreter.invoke(FloatMethods.class, "minus", new Float[]{2.0f, 3.0f}, new Class<?>[]{
                        float.class, float.class})).floatValue(), FloatMethods.minus(2.0f, 3.0f), DELTA);
        assertEquals("FloatMethods.product(2.0f, 3.0f)",
                ((Float) Interpreter.invoke(FloatMethods.class, "product", new Float[]{2.0f, 3.0f}, new Class<?>[]{
                        float.class, float.class})).floatValue(), FloatMethods.product(2.0f, 3.0f), DELTA);
        assertEquals("FloatMethods.divide(2.0f, 3.0f)",
                ((Float) Interpreter.invoke(FloatMethods.class, "divide", new Float[]{2.0f, 3.0f}, new Class<?>[]{
                        float.class, float.class})).floatValue(), FloatMethods.divide(2.0f, 3.0f), DELTA);
    }
}
