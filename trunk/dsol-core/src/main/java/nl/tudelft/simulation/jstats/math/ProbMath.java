package nl.tudelft.simulation.jstats.math;

/**
 * The PropMath class defines some very basic probabilistic mathematical functions.
 * <p>
 * (c) copyright 2002-2005-2004 <a href="http://www.simulation.tudelft.nl">Delft University of Technology </a>, the
 * Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser General Public License (LGPL) </a>, no
 * warranty.
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @version $Revision: 1.2 $ $Date: 2010/08/10 11:38:40 $
 * @since 1.5
 */
public final class ProbMath
{
    /**
     * constructs a new ProbMath.
     */
    private ProbMath()
    {
        super();
        // unreachable code for the utility class
    }

    /**
     * computes the faculty of n.
     * @param n is the input
     * @return faculty of n
     */
    public static double faculty(final int n)
    {
        if (n < 0)
        {
            throw new IllegalArgumentException("n! with n<0 is invalid");
        }
        if (n > 170)
        {
            throw new IllegalArgumentException("n! with n>170 is infinitely");
        }
        double result = 1.0;
        for (int i = 1; i <= n; i++)
        {
            result = result * i;
        }
        return result;
    }

    /**
     * computes the permutations of n over m.
     * @param n the first parameter
     * @param m the second parameter
     * @return the permutations of n over m
     */
    public static double permutations(final int n, final int m)
    {
        if (m > n)
        {
            throw new IllegalArgumentException("permutations of (n,m) with m>n ?...");
        }
        return faculty(n) / (faculty(m) * faculty(n - m));
    }
}
