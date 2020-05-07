package nl.tudelft.simulation.dsol.interpreter;

/**
 * A number of double methods to test the interpreter bytecode for double.
 * <p>
 * copyright (c) 2002-2018  <a href="http://www.simulation.tudelft.nl">Delft University of Technology</a>. <br>
 * BSD-style license. See <a href="http://www.simulation.tudelft.nl/dsol/3.0/license.html">DSOL License</a>. <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs</a>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @version Aug 31, 2014
 */
public class DoubleMethods
{
    /** value to use an the test */
    private double value;

    /**
     * Constructs a test double.
     * @param initialValue the initial value
     */
    public DoubleMethods(final double initialValue)
    {
        this.value = initialValue;
    }

    /**
     * Add two numbers
     * @param a first number
     * @param b second number
     * @return the sum
     */
    public static double plus(final double a, final double b)
    {
        return a + b;
    }

    /**
     * Subtracts two numbers
     * @param a first number
     * @param b second number
     * @return the difference
     */
    public static double minus(final double a, final double b)
    {
        return a - b;
    }

    /**
     * Multiply two numbers
     * @param a first number
     * @param b second number
     * @return the product
     */
    public static double product(final double a, final double b)
    {
        return a * b;
    }

    /**
     * Integer divide two numbers
     * @param a first number
     * @param b second number
     * @return the division
     */
    public static double divide(final double a, final double b)
    {
        return a / b;
    }

    /**
     * Add a number to this value
     * @param a value to add
     * @return the current value
     */
    public double add(final double a)
    {
        this.value += a;
        return getValue();
    }

    /**
     * Subtract a number from this value
     * @param a value to subtract
     * @return the current value
     */
    public double subtract(final double a)
    {
        this.value -= a;
        return getValue();
    }

    /**
     * Multiply this value by a number
     * @param a value to multiply by
     * @return the current value
     */
    public double multiplyBy(final double a)
    {
        this.value *= a;
        return getValue();
    }

    /**
     * Divide this value by a number
     * @param a value to divide by
     * @return the current value
     */
    public double divideBy(final double a)
    {
        this.value = this.value / a;
        return getValue();
    }

    /**
     * @return value
     */
    public final double getValue()
    {
        return this.value;
    }
    
}