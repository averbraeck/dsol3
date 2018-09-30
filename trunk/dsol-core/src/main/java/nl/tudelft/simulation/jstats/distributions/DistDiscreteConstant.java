package nl.tudelft.simulation.jstats.distributions;

import nl.tudelft.simulation.jstats.streams.StreamInterface;

/**
 * The Constant distribution. This distribution maskers a constant discrete value.
 * <p>
 * Copyright (c) 2002-2018 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights
 * reserved. See for project information <a href="https://simulation.tudelft.nl/" target="_blank">
 * https://simulation.tudelft.nl</a>. The DSOL project is distributed under a three-clause BSD-style license, which can
 * be found at <a href="https://simulation.tudelft.nl/dsol/3.0/license.html" target="_blank">
 * https://simulation.tudelft.nl/dsol/3.0/license.html</a>.
 * </p>
 * @author <a href="https://www.linkedin.com/in/peterhmjacobs">Peter Jacobs </a>
 * @author <a href="https://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
 */
public class DistDiscreteConstant extends DistDiscrete
{
    /** */
    private static final long serialVersionUID = 1L;

    /** value is the value of the distribution. */
    private long value;

    /**
     * creates a new discrete constant distribution.
     * @param stream the random number stream
     * @param value the value for this distribution
     */
    public DistDiscreteConstant(final StreamInterface stream, final long value)
    {
        super(stream);
        this.value = value;
    }

    /** {@inheritDoc} */
    @Override
    public long draw()
    {
        this.stream.nextDouble();
        return this.value;
    }

    /** {@inheritDoc} */
    @Override
    public double probability(final int observation)
    {
        if (observation == this.value)
        {
            return 1.0;
        }
        return 0.0;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return "DiscreteConstant(" + this.value + ")";
    }
}
