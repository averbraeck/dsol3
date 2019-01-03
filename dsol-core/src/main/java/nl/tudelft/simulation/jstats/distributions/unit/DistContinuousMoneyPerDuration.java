package nl.tudelft.simulation.jstats.distributions.unit;

import org.djunits.unit.MoneyPerDurationUnit;
import org.djunits.value.vdouble.scalar.MoneyPerDuration;

import nl.tudelft.simulation.jstats.distributions.DistContinuous;

/**
 * DistContinuousMoneyPerDuration is class defining a distribution for a MoneyPerDuration scalar. <br>
 * <br>
 * Copyright (c) 2003-2019 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://www.simulation.tudelft.nl/" target="_blank">www.simulation.tudelft.nl</a>. The
 * source code and binary code of this software is proprietary information of Delft University of Technology.
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank">Alexander Verbraeck</a>
 */
public class DistContinuousMoneyPerDuration extends DistContinuousUnit<MoneyPerDurationUnit, MoneyPerDuration>
{
    /** */
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new continuous distribution that draws MoneyPerDuration scalars.
     * @param wrappedDistribution DistContinuous; the wrapped continuous distribution
     * @param unit MoneyPerDurationUnit; the unit for the values of the distribution
     */
    public DistContinuousMoneyPerDuration(final DistContinuous wrappedDistribution, final MoneyPerDurationUnit unit)
    {
        super(wrappedDistribution, unit);
    }

    /** {@inheritDoc} */
    @Override
    public MoneyPerDuration draw()
    {
        return new MoneyPerDuration(this.wrappedDistribution.draw(), this.unit);
    }
}
