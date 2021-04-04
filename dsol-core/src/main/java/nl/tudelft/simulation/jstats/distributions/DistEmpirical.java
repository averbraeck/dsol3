package nl.tudelft.simulation.jstats.distributions;

import nl.tudelft.simulation.jstats.distributions.empirical.DistributionEntry;
import nl.tudelft.simulation.jstats.distributions.empirical.EmpiricalDistributionInterface;
import nl.tudelft.simulation.jstats.streams.StreamInterface;

/**
 * The empirical distribution is a distribution where the information is stored in an EmpiricalDistribution, consisting of pairs
 * of values and cumulative probabilities. <br>
 * Note that when interpolated is false in the EmpiricalDistribution, the function in essence behaves as a <b>discrete</b>
 * distribution, albeit with double values as the outcome. In that case, the probability density function returns the equivalent
 * of the discrete distribution function; of course the real probability density function from the viewpoint of a continuous
 * distribution does not exist.
 * <p>
 * Copyright (c) 2002-2021 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://simulation.tudelft.nl/" target="_blank"> https://simulation.tudelft.nl</a>. The DSOL
 * project is distributed under a three-clause BSD-style license, which can be found at
 * <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class DistEmpirical extends DistContinuous
{
    /** */
    private static final long serialVersionUID = 20210403L;

    /** is the distribution grouped? */
    private final EmpiricalDistributionInterface empiricalDistribution;

    /**
     * constructs a new DistEmpirical.
     * @param stream StreamInterface; the stream to use
     * @param empiricalDistribution EmpiricalDistributionInterface; the cumulative distribution to use
     */
    public DistEmpirical(final StreamInterface stream, final EmpiricalDistributionInterface empiricalDistribution)
    {
        super(stream);
        this.empiricalDistribution = empiricalDistribution;
    }

    /** {@inheritDoc} */
    @Override
    public double draw()
    {
        double u = this.stream.nextDouble();
        if (this.empiricalDistribution.isInterpolated())
        {
            DistributionEntry entry0 = this.empiricalDistribution.getFloorEntry(u);
            DistributionEntry entry1 = this.empiricalDistribution.getCeilingEntry(u);
            double v1 = entry1.getValue().doubleValue();
            double v0 = entry0 != null ? entry0.getValue().doubleValue() : v1;
            double c1 = entry1.getCumulativeProbability();
            double c0 = entry0 != null ? entry0.getCumulativeProbability() : 0.0;
            return v0 + (v1 - v0) * (u - c0) / (c1 - c0);
        }
        return this.empiricalDistribution.getCeilingEntry(u).getValue().doubleValue();
    }

    /** {@inheritDoc} */
    @Override
    public double getProbabilityDensity(final double x)
    {
        DistributionEntry entry0 = this.empiricalDistribution.getFloorEntryForValue(x);
        if (this.empiricalDistribution.isInterpolated())
        {
            DistributionEntry entry1 = this.empiricalDistribution.getCeilingEntryForValue(x);
            if (entry0 == null || entry1 == null)
            {
                return 0.0;
            }
            double v0 = entry0.getValue().doubleValue();
            double v1 = entry1.getValue().doubleValue();
            double c0 = entry0.getCumulativeProbability();
            double c1 = entry1.getCumulativeProbability();
            return (c1 - c0) / (v1 - v0);
        }

        // not interpolated; behave as if this is a discrete distribution with a double-valued outcome
        if (entry0 == null || entry0.getValue().longValue() != x)
        {
            return 0.0;
        }
        double c1 = entry0.getCumulativeProbability();
        DistributionEntry entryp = this.empiricalDistribution.getPrevEntry(c1);
        return (entryp == null) ? c1 : c1 - entryp.getCumulativeProbability();
    }

}
