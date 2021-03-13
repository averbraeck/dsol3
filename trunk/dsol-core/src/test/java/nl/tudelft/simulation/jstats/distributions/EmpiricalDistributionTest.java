package nl.tudelft.simulation.jstats.distributions;

import nl.tudelft.simulation.jstats.streams.MersenneTwister;
import nl.tudelft.simulation.jstats.streams.StreamInterface;

/**
 * Test the empirical distributions (continuous and discrete).
 * <p>
 * Copyright (c) 2021-2021 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/dsol/manual/" target="_blank">DSOL Manual</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">DSOL License</a>.
 * </p>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class EmpiricalDistributionTest
{
    /**
     * Test the DistDiscreteEmpirical.
     */
    public void testDistDiscreteEmpirical()
    {
        StreamInterface stream = new MersenneTwister(20L);
        // TODO: test empirical distributions
    }

}
