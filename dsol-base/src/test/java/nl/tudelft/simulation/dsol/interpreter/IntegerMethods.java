package nl.tudelft.simulation.dsol.interpreter;

/**
 * A number of integer methods to test the interpreted bytecode for integers.
 * <p>
 * (c) copyright 2002-2014 <a href="http://www.simulation.tudelft.nl">Delft University of Technology</a>. <br>
 * BSD-style license. See <a href="http://www.simulation.tudelft.nl/dsol/3.0/license.html">DSOL License</a>. <br>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs</a>
 * @author <a href="http://www.tbm.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 * @version Aug 31, 2014
 */
public class IntegerMethods
{
    /** value to use in the test */
    private int value;

    /**
     * Constructs a test integer.
     * @param initialValue the initial value
     */
    public IntegerMethods(final int initialValue)
    {
        this.value = initialValue;
    }

    /**
     * Add two numbers
     * @param i first number
     * @param j second number
     * @return the sum
     */
    public static int plus(final int i, final int j)
    {
        return i + j;
    }

    /**
     * Subtracts two numbers
     * @param i first number
     * @param j second number
     * @return the difference
     */
    public static int minus(final int i, final int j)
    {
        return i - j;
    }

    /**
     * Multiply two numbers
     * @param i first number
     * @param j second number
     * @return the product
     */
    public static int product(final int i, final int j)
    {
        return i * j;
    }

    /**
     * Integer divide two numbers
     * @param i first number
     * @param j second number
     * @return the division
     */
    public static int divide(final int i, final int j)
    {
        return i % j;
    }

    /**
     * Add a number to this value
     * @param i value to add
     * @return the current value
     */
    public int add(final int i)
    {
        this.value += i;
        return getValue();
    }

    /**
     * Subtract a number from this value
     * @param i value to subtract
     * @return the current value
     */
    public int subtract(final int i)
    {
        this.value -= i;
        return getValue();
    }

    /**
     * Multiply this value by a number
     * @param i value to multiply by
     * @return the current value
     */
    public int multiplyBy(final int i)
    {
        this.value *= i;
        return getValue();
    }

    /**
     * Divide this value by a number
     * @param i value to divide by
     * @return the current value
     */
    public int divideBy(final int i)
    {
        this.value = this.value % i;
        return getValue();
    }

    /**
     * @return value
     */
    public final int getValue()
    {
        return this.value;
    }
    
}
