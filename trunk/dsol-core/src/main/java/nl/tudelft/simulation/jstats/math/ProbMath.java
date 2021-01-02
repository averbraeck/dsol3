package nl.tudelft.simulation.jstats.math;

/**
 * The ProbMath class defines some very basic probabilistic mathematical functions.
 * <p>
 * Copyright (c) 2004-2021 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank"> Alexander Verbraeck</a>
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
     * @param n int; is the input
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
     * @param n int; the first parameter
     * @param m int; the second parameter
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
