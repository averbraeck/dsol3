package nl.tudelft.simulation.jstats.distributions.unit;

import org.djunits.unit.MoneyUnit;
import org.djunits.value.vdouble.scalar.Money;

import nl.tudelft.simulation.jstats.distributions.DistContinuous;

/**
 * DistContinuousMoney is class defining a distribution for a Money scalar. <br>
 * <br>
 * Copyright (c) 2003-2019 Delft University of Technology, Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved. See
 * for project information <a href="https://www.simulation.tudelft.nl/" target="_blank">www.simulation.tudelft.nl</a>. The
 * source code and binary code of this software is proprietary information of Delft University of Technology.
 * @author <a href="https://www.tudelft.nl/averbraeck" target="_blank">Alexander Verbraeck</a>
 */
public class DistContinuousMoney extends DistContinuousUnit<MoneyUnit, Money>
{
    /** */
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new continuous distribution that draws Money scalars.
     * @param wrappedDistribution DistContinuous; the wrapped continuous distribution
     * @param unit MoneyUnit; the unit for the values of the distribution
     */
    public DistContinuousMoney(final DistContinuous wrappedDistribution, final MoneyUnit unit)
    {
        super(wrappedDistribution, unit);
    }

    /** {@inheritDoc} */
    @Override
    public Money draw()
    {
        return new Money(this.wrappedDistribution.draw(), this.unit);
    }
}
