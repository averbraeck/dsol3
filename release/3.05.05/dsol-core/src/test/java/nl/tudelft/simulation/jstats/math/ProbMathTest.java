package nl.tudelft.simulation.jstats.math;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * The test script for the ProbMath class.
 * <p>
 * Copyright (c) 2002-2021 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @since 1.5
 */
public class ProbMathTest
{
    /**
     * tests the probMath class.
     */
    @Test
    public void testProbMath()
    {
        // First the faculty function
        try
        {
            ProbMath.faculty(-1);
            fail();
        }
        catch (Exception exception)
        {
            assertEquals(exception.getClass(), IllegalArgumentException.class);
        }
        assertTrue(ProbMath.faculty(0) == 1.0);
        assertTrue(ProbMath.faculty(10) == 3628800.0);
        try
        {
            ProbMath.faculty(171);
            fail();
        }
        catch (Exception exception)
        {
            assertEquals(exception.getClass(), IllegalArgumentException.class);
        }

        // Permutations
        try
        {
            ProbMath.permutations(2, 5);
        }
        catch (Exception exception)
        {
            assertEquals(exception.getClass(), IllegalArgumentException.class);
        }
    }
}
